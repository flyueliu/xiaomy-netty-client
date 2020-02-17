package com.flyue.xiaomy.encoder;

import com.flyue.xiaomy.protocol.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义协议的编码器
 */
public class TransformProtocolEncoder extends MessageToByteEncoder<ProtocolMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          ProtocolMessage msg, ByteBuf out) {
        // 写出header长度
        out.writeInt(msg.getHeadLength());
        // 消息的body长度
        out.writeInt(msg.getBody().length);
        // 写出消息的内容
        out.writeBytes(msg.getHeadByte());
        out.writeBytes(msg.getBody());
    }
}
