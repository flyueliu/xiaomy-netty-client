package com.flyue.xiaomy.handler;

import com.flyue.xiaomy.SmallAntClient;
import com.flyue.xiaomy.common.http.vo.resp.login.TunnelInfo;
import com.flyue.xiaomy.common.utils.JsonUtils;
import com.flyue.xiaomy.forward.initializer.ForwardChannelInitializer;
import com.flyue.xiaomy.protocol.ProtocolMessage;
import com.flyue.xiaomy.protocol.ResponseMessageHeader;
import com.flyue.xiaomy.starter.BaseClientStarter;
import com.flyue.xiaomy.starter.NetworkClientStarter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/17 21:54
 * @Description:
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelHandler.class);
    private final NetworkClientStarter clientStarter;

    public ClientChannelHandler(NetworkClientStarter clientStarter) {
        this.clientStarter = clientStarter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object data) throws Exception {
        logger.debug("received message:{}", data);
        ProtocolMessage message = (ProtocolMessage) data;
        ResponseMessageHeader respHeader = message.getHeaderMessage(ResponseMessageHeader.class);
        if ("LOGIN".equals(respHeader.getType())) {
            if (!"LOGINOK".equals(respHeader.getMsg())) {
                logger.error("login failed,info:{}", respHeader.getInfo());
                this.clientStarter.close();
                return;
            }
            logger.info("login success!");
        } else if ("CONN".equals(respHeader.getType())) {
            logger.info("receive client connect");
            this.handleReceiveConnectRequest(respHeader);
        } else if ("HEART".equals(respHeader.getType())) {
            logger.debug("receive heart message:{}", message);
        } else if ("MSG".equals(respHeader.getType())) {

        }
    }

    private void handleReceiveConnectRequest(ResponseMessageHeader respHeader) {
        BaseClientStarter clientChannel = new BaseClientStarter();
        BaseClientStarter serverChannel = new BaseClientStarter();
        clientChannel.setInitializer(new ForwardChannelInitializer(serverChannel));
        serverChannel.setInitializer(new ForwardChannelInitializer(clientChannel));
        if (!clientChannel.connect(clientStarter.getTunnelInfo().getClient_host(), clientStarter.getTunnelInfo().getClient_port())) {
            logger.error("connect local server failed,check local server is active");
            return;
        }
        serverChannel.connect(clientStarter.getTunnelInfo().getServer_ip(), 8888);
        ByteBuf buffer = Unpooled.buffer();
        Map<String, Object> header = new HashMap<>();
        header.put("type", "CONN");
        header.put("uid", respHeader.getUid());
        byte[] headerByte = JsonUtils.pojoToJson(header).getBytes();
        buffer.writeInt(headerByte.length);
        byte[] body = "null".getBytes();
        buffer.writeInt(body.length);
        buffer.writeBytes(headerByte);
        buffer.writeBytes(body);
        serverChannel.getClientChannel().writeAndFlush(buffer);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connect {} success", ctx.channel().remoteAddress());
        this.sendHeartBeat(ctx);
    }

    public void sendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.debug("send heart beat message");
                Map<String, Object> map = new HashMap<>();
                map.put("type", "HEART");
                ProtocolMessage message = new ProtocolMessage(JsonUtils.pojoToJson(map), "heart".getBytes());
                ctx.writeAndFlush(message).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        Throwable throwable = future.cause();
                        if (Objects.nonNull(throwable)) {
                            logger.error("send heart beat message error! detail:{}", throwable.getMessage());
                            ctx.close();
                        }
                    }
                });
            }
        }, 3, 10, TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connect disabled");
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("connect exception! detail:{}", cause.getMessage());
        ctx.close();
        ctx.channel().eventLoop().shutdownGracefully(1, 20, TimeUnit.SECONDS);
        this.reconnect();
    }

    public void reconnect() throws InterruptedException {
        try {
            SmallAntClient client = new SmallAntClient(this.clientStarter.getTunnelInfo().getPort_token());
            TunnelInfo tunnelInfo = client.getTunnelInfo();
            client.createNatChannel();
        } catch (Exception e) {
            logger.error("reconnect error! detail:{}", e.getMessage());
            logger.error("wait 5 second reconnect");
            Thread.sleep(5000);
            this.reconnect();
        }
    }
}
