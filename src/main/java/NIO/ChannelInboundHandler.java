package NIO;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @className: ChannelInboundHandler.java
 * @version: 1.0.0
 * @description: 处理入站TcpMessage
 * @author: chen qinping
 * @date: 2021/9/27 15:29
 */
public class ChannelInboundHandler extends SimpleChannelInboundHandler<TcpMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpMessage tcpMessage) {

        System.out.println("receive message: " + Thread.currentThread().getName() + tcpMessage.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }
}
