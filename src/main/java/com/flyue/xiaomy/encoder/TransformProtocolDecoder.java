package com.flyue.xiaomy.encoder;

import com.flyue.xiaomy.protocol.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 协议解码器
 */
public class TransformProtocolDecoder extends ByteToMessageDecoder {

    public final int BASE_LENGTH = 4 + 4;

    public final int ERROR_HEAD_LENGTH = 1024 * 10; //10KB

    public final int ERROR_BODY_LENGTH = 1024 * 1024 * 10; //100MB

    private static final Logger logger = LoggerFactory.getLogger(TransformProtocolDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        // 可读长度必须大于基本长度
        logger.debug("first readable length:{}", byteBuf.readableBytes());
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            int headLength = 0;
            int bodyLength = 0;
            int beginReader = 0;
            while (true) {
                //获取包头开始的index
                beginReader = byteBuf.readerIndex();
                //标记包头开始的index
                byteBuf.markReaderIndex();
                // 读到了协议的开始标志，结束while循环
                headLength = byteBuf.readInt();
                bodyLength = byteBuf.readInt();
                // 检查head长度值是否超过10KB
                if (headLength <= 0 || headLength > ERROR_HEAD_LENGTH || bodyLength <= 0 || bodyLength > ERROR_BODY_LENGTH) {
                    //重置回原先位置
                    byteBuf.resetReaderIndex();
                    // 抛弃上个字节，重新读取
                    byteBuf.readByte();
                    logger.debug("third readable length:{}", byteBuf.readableBytes());
                    if (byteBuf.readableBytes() < BASE_LENGTH) {
                        // 可读长度不够
                        return;
                    }
                } else {
                    // 可读长度够，跳出循环，进行数据封装
                    logger.debug("head length:{} body length:{}", headLength, bodyLength);
                    logger.debug("second readable length:{}", byteBuf.readableBytes());
                    if (byteBuf.readableBytes() >= headLength + bodyLength) {
                        break;
                    }
                    // 重置回原先位置
                    byteBuf.resetReaderIndex();
                    // 数据包未到齐,还原指针
                    byteBuf.readerIndex(beginReader);
                    return;
                }
            }
            logger.debug("read message header length:{} body length:{}", headLength, bodyLength);
            // 消息的长度
            // 读取header数据
            byte[] header = new byte[headLength];
            byte[] body = new byte[bodyLength];
            byteBuf.readBytes(header);
            byteBuf.readBytes(body);
            String headerData = new String(header);
            ProtocolMessage message = new ProtocolMessage(headerData, body);
            list.add(message);
        }
    }
}
