package com.flyue.xiaomy.tcp;

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

import java.util.UUID;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/17 21:59
 * @Description:
 */
public class NetworkClientStarter {
    private final NioEventLoopGroup worker;
    private final ChannelInboundHandler clientHandler;
    private Bootstrap bootstrap;
    private Channel clientChannel;

    public NetworkClientStarter(ChannelInboundHandler handler) {
        this.worker = new NioEventLoopGroup(2);
        this.clientHandler = handler;
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
        try {
            ChannelFuture channelFuture = this.bootstrap.connect(host, port).sync();
            this.clientChannel = channelFuture.channel();
            System.out.println("connect success");
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

    public void close() {
        try {
            this.clientChannel.closeFuture().sync();
            this.worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
