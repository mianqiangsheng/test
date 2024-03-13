package NIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @className: MessageDecode.java
 * @version: 1.0.0
 * @description: 将字节解码为TcpMessage
 * @author: chen qinping
 * @date: 2021/9/26 17:13
 */
public class MessageDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        //收到数据的最小长度：2字节消息头，1字节消息类型，4字节后续消息长度
        int minLength=7;
        if (byteBuf.readableBytes() < minLength) {
            return;
        }
        byteBuf.markReaderIndex();

        char head=byteBuf.readChar();
        if(head!= TcpMessage.MESSAGE_HEAD){
            byteBuf.clear();
        }
        byte type=byteBuf.readByte();
        int msgLength = byteBuf.readInt();
        if (byteBuf.readableBytes()< msgLength) {
            //发生分包现象，可读消息比指示的长度小，则读位置指针退回原始位置，返回，等待下次读取
            byteBuf.resetReaderIndex();
            return ;
        }

        byte[] msgBytes=new byte[msgLength];
        byteBuf.readBytes(msgBytes);
        TcpMessage msg=new TcpMessage();
        msg.setMessageType(type);
        msg.setMessageBytes(msgBytes);
        msg.setMessage(new String(msgBytes));

        list.add(msg);

    }
}
