package test3.NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lizhen on 2018/11/23.
 */
public class NoBlockServer1 {

    public static void main(String[] args) throws IOException {

        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {

            ssc.socket().bind(new InetSocketAddress("127.0.0.1", 6666));
            ssc.configureBlocking(false);

            Selector selector = Selector.open();
            // 注册 channel，并且指定感兴趣的事件是 Accept
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            ByteBuffer writeBuff = ByteBuffer.allocate(128);
            writeBuff.put("img has been recieved succeccfully!".getBytes("UTF-8"));
            writeBuff.flip();
            while(selector.select() >0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isAcceptable()) {
                        // 创建新的连接，并且把连接注册到selector上，而且，
                        // 声明这个channel只对读操作感兴趣。
                        SocketChannel socketChannel = ssc.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {

                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        try (FileChannel outChannel = FileChannel.open(Paths.get("D:\\star_src_1_600_400_1.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

                            while (socketChannel.read(readBuff) != -1) {
                                readBuff.flip();
                                outChannel.write(readBuff);
                                readBuff.clear();
                            }
                        }
                        System.out.println("img has been recieved succeccfully!");
                        key.interestOps(SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        writeBuff.rewind();
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.write(writeBuff);
                    }
                }

            }

        }
    }
}
