package test3.NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * Created by lizhen on 2018/11/23.
 */
public class NoBlockServer {

    public static void main(String[] args)throws IOException {

        // 1.获取通道
        try (ServerSocketChannel server = ServerSocketChannel.open()) {

            // 2.切换成非阻塞模式
            server.configureBlocking(false);

            // 3. 绑定连接
            server.bind(new InetSocketAddress(6666));

            // 4. 获取选择器
            Selector selector = Selector.open();
            // 4.1将通道注册到选择器上，指定接收“监听通道”事件
            server.register(selector, SelectionKey.OP_ACCEPT);

            // 5. 轮询地获取选择器上已“就绪”的事件--->只要select()>0，说明已就绪
            while (selector.select() > 0) {

                // 6. 获取当前选择器所有注册的“选择键”(已就绪的监听事件)
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                // 7. 获取已“就绪”的事件，(不同的事件做不同的事)
                while (iterator.hasNext()) {

                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    // 接收事件就绪
                    if (selectionKey.isAcceptable()) {

                        // 8. 获取客户端的链接
                        SocketChannel client = server.accept();
                        // 8.1 切换成非阻塞状态
                        client.configureBlocking(false);
                        // 8.2 注册到选择器上-->拿到客户端的连接为了读取通道的数据(监听读就绪事件)
                        client.register(selector, SelectionKey.OP_READ);

                    } else if (selectionKey.isReadable()) {// 读事件就绪
                        // 9. 获取当前选择器读就绪状态的通道
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        // 9.1读取数据
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 9.2得到文件通道，将客户端传递过来的图片写到本地项目下(写模式、没有则创建)
                        try (FileChannel outChannel = FileChannel.open(Paths.get("D:\\star_src_1_600_400_1.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
                            int num = 0;
                            while ((num = client.read(buffer)) > 0) {
                                buffer.flip();
                                // 写入文件
                                outChannel.write(buffer);
                                buffer.clear();
                            }

                            // 调用close为-1 到达末尾
                            if (num == -1) {
                                outChannel.close();
                                System.out.println("上传完毕");
                                buffer.put((client.getRemoteAddress() + "上传成功").getBytes());
                                buffer.clear();
                                client.write(buffer);
                                selectionKey.cancel();
                            }
                        }
                    }

                }

            }
        }

    }
}
