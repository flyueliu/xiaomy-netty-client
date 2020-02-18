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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/19 0:03
 * @Description:
 */
public class BaseClientStarter {

    private static final Logger logger = LoggerFactory.getLogger(NetworkClientStarter.class);
    private NioEventLoopGroup worker;
    private Bootstrap bootstrap;
    private Channel clientChannel;

    private boolean hasInit = false;

    private boolean connected = false;
    public ChannelInitializer<SocketChannel> initializer;

    public BaseClientStarter(ChannelInitializer<SocketChannel> initializer) {
        this.initializer = initializer;
    }

    public BaseClientStarter() {

    }

    public void init() {
        if (hasInit) {
            return;
        }
        if (Objects.isNull(this.initializer)) {
            throw new RuntimeException("must set initializer");
        }
        this.worker = new NioEventLoopGroup(2);
        this.bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(this.worker)
                .handler(this.initializer);
    }

    public boolean connect(String host, int port) {
        this.init();
        try {
            ChannelFuture channelFuture = this.bootstrap.connect(host, port).sync();
            this.clientChannel = channelFuture.channel();
            this.connected = true;
            return true;
        } catch (Exception e) {
            logger.error("connect {}:{} error!", host, port);
            return false;
        }
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public void close() {
        try {
            logger.info("disconnect {} ", this.getClientChannel().remoteAddress());
            this.getClientChannel().close().sync();
            this.worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setInitializer(ChannelInitializer<SocketChannel> initializer) {
        this.initializer = initializer;
    }

    public boolean isConnected() {
        return connected;
    }
}
