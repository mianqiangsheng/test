package test3.NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lizhen on 2018/11/26.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            /* 模拟三个发端 */
            /* 发送图片 */
//            new Thread(() -> {
//                SocketChannel socketChannel = null;
//                FileChannel fileChannel;
//                FileInputStream fileInputStream = null;
//                    try {
//                        socketChannel = SocketChannel.open();
//                        socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));
//                        socketChannel.configureBlocking(false);
//                        File file = new File("D:\\star.jpg");
//                        try {
//                            fileInputStream = new FileInputStream(file);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        fileChannel = fileInputStream.getChannel();
//                        ByteBuffer buffer = ByteBuffer.allocate(100);
//
//                        int num;
//                        while ((num = fileChannel.read(buffer)) > 0) {
//                            buffer.flip();
//                            socketChannel.write(buffer);
//                            buffer.clear();
//                        }
//                        if (num == -1) {
//                            fileChannel.close();
//                            socketChannel.shutdownOutput();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//            }).start();

            /* 发送字符串并接受服务器返回的字符串 */
            new Thread(() -> {
                Selector selector = null;
                try {
                    try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888))) {
                        // 1.1切换成非阻塞模式
                        try {
                            socketChannel.configureBlocking(false);
                            // 1.2获取选择器
                            selector = Selector.open();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 1.3将通道注册到选择器中，获取服务端返回的数据
                        try {
                            socketChannel.register(selector, SelectionKey.OP_CONNECT);
                        } catch (ClosedChannelException e) {
                            e.printStackTrace();
                        }
                        byte[] bytes = ("client" + socketChannel.getLocalAddress()).getBytes();
                        //根据数组容量创建ByteBuffer
                        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                        //将字节数组复制到缓冲区
                        writeBuffer.put(bytes);
                        //flip操作
                        writeBuffer.flip();
                        //发送缓冲区的字节数组
                        socketChannel.write(writeBuffer);

                        socketChannel.register(selector, SelectionKey.OP_READ);

                        while(true){
                            try{
                                //无论是否有读写事件发生，selector每隔1s被唤醒一次
                                selector.select(1000);
                                Set<SelectionKey> keys = selector.selectedKeys();
                                Iterator<SelectionKey> it = keys.iterator();
                                SelectionKey key = null;
                                while(it.hasNext()){
                                    key = it.next();
                                    it.remove();
                                    try{
                                        handleInput(key);
                                    }catch(Exception e){
                                        if(key != null){
                                            key.cancel();
                                            if(key.channel() != null){
                                                key.channel().close();
                                            }
                                        }
                                    }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                System.exit(1);
                            }
                        }
                        //selector关闭后会自动释放里面管理的资源
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.yield();
    }


    private static void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            SocketChannel sc = (SocketChannel) key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect());
                else System.exit(1);
            }
            //读消息
            if(key.isReadable()){
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = sc.read(buffer);
                //读取到字节，对字节进行编解码
                if(readBytes>0){
                    //将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    buffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String result = new String(bytes,"UTF-8");
                    System.out.println("客户端收到消息：" + result);
                }
                //没有读取到字节 忽略
//                else if(readBytes==0);
                //链路已经关闭，释放资源
                else if(readBytes<0){
                    key.cancel();
                    sc.close();
                }
            }
        }
    }
}
