package test3.NonBlock.formal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhen on 2018/11/27.
 */
public class NioServer {

    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,10,0, TimeUnit.SECONDS,new LinkedBlockingQueue<>(10));
    private static ServerSocketChannel serverSocketChannel;
    private static Selector selector;

    public static void main(String[] args) throws IOException {

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8080));
        System.out.println("Nio服务器端口8080开启");

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 不断监听8080端口对每一个连接进行读写操作
        while (true){
            selector.select(1000L); //设置最多1秒左右阻塞返回

            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();

            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //SelectionKey从创建开始有效并保持不变，直到它被取消，其通道关闭或其选择器关闭。所以先判断这个key是否有效，再判断就绪情况，避免可能出现的异常
                if(key.isValid() && key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    System.out.println(socketChannel.getRemoteAddress() + "连接成功...");
                }else if (key.isValid() && key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    socketChannel.configureBlocking(false);
                    key.cancel(); //先从selector中移除这个SelectionKey,因为这里将这个key绑定的channel丢给其他线程处理了，所以要先等待处理完成客户端的第一次请求并返回再接收客户端的第二、第三次请求...避免错乱
                    //读到请求数据，开始业务处理，另开线程操作，避免这个用来负责处理客户连接接收请求数据的线程阻塞，也就达到多客户端实时连接发送请求数据
                    poolExecutor.execute(new NioSocketProcessor(socketChannel));
                }
            }

            selectionKeySet.clear(); // 可选操作
            int now = selector.selectNow(); // 可选操作，都是最大程度保证程序的顺利运行
//            System.out.println("selector.selectNow() selectedKeySet: " + now);
        }

    }


    static class NioSocketProcessor implements Runnable{

        SocketChannel socketChannel;

        public NioSocketProcessor(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int read = socketChannel.read(byteBuffer);
                System.out.println("读取到的字节数数是："+read);

                byteBuffer.flip();
                byte[] bytes = byteBuffer.array();
                String result = new String(bytes,0,read, Charset.forName("UTF-8"));
                if ("exit".equals(result) || "quit".equals(result)){
                    System.out.println(socketChannel.getRemoteAddress() + "断开连接...");

                    String response = socketChannel.getRemoteAddress() + "连接成功断开...";
                    byte[] responseBytes = response.getBytes("UTF-8");
                    ByteBuffer allocate = ByteBuffer.wrap(responseBytes);
                    socketChannel.write(allocate);
                    // 关闭这个socketChannel，不再监听这个socketChannel
                    socketChannel.close();
                    return;
                }

                System.out.println("收到请求数据，当前线程"+Thread.currentThread().getId()+"，数据内容是："+result);

                String response = socketChannel.getRemoteAddress() + "连接成功...，已经成功收到数据";
                byte[] responseBytes = response.getBytes("UTF-8");
                ByteBuffer allocate = ByteBuffer.wrap(responseBytes);
                socketChannel.write(allocate);
                allocate.clear();

            }catch (IOException e){
                e.printStackTrace();
            }finally {
                //线程中的业务逻辑完成后，将这个socketChannel连接重新注册回selector中，达到保持这个长连接的目的
                try {
                    if (socketChannel.isConnected()){
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


