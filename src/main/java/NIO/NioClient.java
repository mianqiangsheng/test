package NIO;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/13 14:47
 */
public class NioClient {

    public static void start(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(2);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new MessageDecode())
                                    .addLast(new MessageEncode())
                                    .addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    static class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
            for (int i = 0; i < 10; i++) {
                TcpMessage tcpMessage = new TcpMessage();
                tcpMessage.setMessage(Thread.currentThread().getId() + ":" + i);
                tcpMessage.setMessageType((byte) 0x3);
                tcpMessage.setMessageBytes(tcpMessage.getMessage().getBytes());
                ctx.writeAndFlush(tcpMessage);
                TimeUnit.SECONDS.sleep(2);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
            System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
        }
    }

    public static void main(String[] args) throws Exception {
        start(9001);
        System.out.println("---------------");
        new Thread(()-> {
            try {
                start(9001);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
