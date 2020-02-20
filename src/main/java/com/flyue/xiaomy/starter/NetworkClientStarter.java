package com.flyue.xiaomy.starter;

import com.flyue.xiaomy.common.http.vo.resp.login.TunnelInfo;
import com.flyue.xiaomy.encoder.TransformProtocolDecoder;
import com.flyue.xiaomy.encoder.TransformProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/17 21:59
 * @Description:
 */
public class NetworkClientStarter {

    private static final Logger logger = LoggerFactory.getLogger(NetworkClientStarter.class);
    private final TunnelInfo tunnelInfo;

    private NioEventLoopGroup worker;
    private ChannelInboundHandler clientHandler;
    private Bootstrap bootstrap;
    private Channel clientChannel;

    private boolean hasInit = false;

    public NetworkClientStarter(TunnelInfo tunnelInfo) {
        this.tunnelInfo = tunnelInfo;
    }

    public TunnelInfo getTunnelInfo() {
        return tunnelInfo;
    }

    public void setClientHandler(ChannelInboundHandler channelInboundHandler) {
        this.clientHandler = channelInboundHandler;
    }

    public void init() {
        if (hasInit) {
            return;
        }
        this.worker = new NioEventLoopGroup(2);
        this.bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(this.worker)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new TransformProtocolEncoder())
                                .addLast(new TransformProtocolDecoder())
                                .addLast(clientHandler);
                    }
                });
    }

    public void connect(String host, int port) {
        this.init();
        try {
            ChannelFuture channelFuture = this.bootstrap.connect(host, port).sync();
            this.clientChannel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ChannelInboundHandler getClientHandler() {
        return clientHandler;
    }

    public Channel getClientChannel() {
        return clientChannel;
    }


    public void closeChannel() {
        this.getClientChannel().close();
    }

    public void waitClose() {
        try {
            logger.info("wait close connect.");
            this.getClientChannel().closeFuture().sync();
            this.worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void close() {
        logger.info("start close connect.");
        this.getClientChannel().close();
        this.closeEventLoop();
    }

    public void closeEventLoop() {
        this.worker.shutdownGracefully(1, 5, TimeUnit.SECONDS);
    }

}
