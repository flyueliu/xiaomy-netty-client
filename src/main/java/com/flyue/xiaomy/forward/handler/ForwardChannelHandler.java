package com.flyue.xiaomy.forward.handler;

import com.flyue.xiaomy.starter.BaseClientStarter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/19 9:53
 * @Description:
 */
public class ForwardChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ForwardChannelHandler.class);

    private BaseClientStarter forwardClient;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        forwardClient.getClientChannel().write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        forwardClient.getClientChannel().flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connect {} success!", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("{}", cause.getMessage());
        ctx.channel().close();
        forwardClient.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("disconnect {}", ctx.channel().remoteAddress());
        forwardClient.close();
    }

    public void setForwardClient(BaseClientStarter forwardClient) {
        this.forwardClient = forwardClient;
    }
}
