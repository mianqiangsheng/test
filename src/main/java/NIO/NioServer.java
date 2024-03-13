package NIO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/13 14:47
 */
public class NioServer {

    public static void init() {


        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        channel.pipeline()
                                .addLast(new IdleStateHandler(30, 30, 30, TimeUnit.MINUTES))
                                .addLast(new MessageDecode())
                                .addLast(new MessageEncode())
                                .addLast(new ChannelInboundHandler());
                    }
                });
        ChannelFuture future1 = bootstrap.bind(new InetSocketAddress(9000)).syncUninterruptibly();
        ChannelFuture future2 = bootstrap.bind(new InetSocketAddress(9001)).syncUninterruptibly();

        future1.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("tcp server start success! port:"+ 9000);
            } else {
                System.out.println("tcp server start failed:"+ 9000);
            }
        });
        future2.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("tcp server start success! port:"+ 9001);
            } else {
                System.out.println("tcp server start failed:"+ 9001);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });

    }

    public static void main(String[] args) {
        init();
    }
}
