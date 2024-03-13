package NIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @className: MessageEncode.java
 * @version: 1.0.0
 * @description: 将TcpMessage字节消息编码为字节
 * @author: chen qinping
 * @date: 2021/9/27 15:25
 */
public class MessageEncode extends MessageToByteEncoder<TcpMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TcpMessage tcpMessage, ByteBuf byteBuf) {
        byteBuf.writeChar(TcpMessage.MESSAGE_HEAD);
        byteBuf.writeByte(tcpMessage.getMessageType());
        byteBuf.writeInt(tcpMessage.getMessageBytes().length);
        byteBuf.writeBytes(tcpMessage.getMessageBytes());
        System.out.println(tcpMessage);
    }
}
