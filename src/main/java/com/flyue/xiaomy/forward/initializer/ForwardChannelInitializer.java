package com.flyue.xiaomy.forward.initializer;

import com.flyue.xiaomy.forward.handler.ForwardChannelHandler;
import com.flyue.xiaomy.starter.BaseClientStarter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/19 9:50
 * @Description:
 */
public class ForwardChannelInitializer extends ChannelInitializer<SocketChannel> {


    private final BaseClientStarter forwardClient;

    public ForwardChannelInitializer(BaseClientStarter forwardClient) {
        this.forwardClient = forwardClient;
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ForwardChannelHandler forwardChannelHandler = new ForwardChannelHandler();
        forwardChannelHandler.setForwardClient(forwardClient);
        ch.pipeline().addLast(forwardChannelHandler);
    }
}
