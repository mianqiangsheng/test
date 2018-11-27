package test3.NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lizhen on 2018/11/26.
 */
public class Server {

    private volatile boolean started = true;//服务器状态线程可见
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    //使用Map保存每个连接，当OP_READ就绪时，根据key找到对应的文件对其进行写入。若将其封装成一个类，作为值保存，可以再上传过程中显示进度等等
    Map<SelectionKey, FileChannel> fileMap = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer(8888);
    }

    public void startServer(int port) throws IOException{
        Selector selector = Selector.open();
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器已开启...");

            while (started) {
                int num = selector.select();
                if (num == 0) continue;
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isValid() && key.isAcceptable()) {

                        ServerSocketChannel serverChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverChannel1.accept();
                        if (socketChannel == null) continue;
                        socketChannel.configureBlocking(false);
                        SelectionKey key1 = socketChannel.register(selector, SelectionKey.OP_READ);

//                        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\star_src_1_600_400_" + socketChannel.getRemoteAddress().toString().substring(11) + ".jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
//                        fileMap.put(key1, fileChannel);

                        System.out.println(socketChannel.getRemoteAddress() + "连接成功...");
                        writeToClient(socketChannel);
                    } else if (key.isValid() && key.isReadable()){

//                        readData(key);

                        ByteBuffer bb = ByteBuffer.allocate(1024);//使用静态方法allocate定义一个1M的byte类型的缓存区域，用于保存通道中的数据
                        SocketChannel channel = (SocketChannel) key.channel();
                        int returnReadByte = channel.read(bb);//读取通道中的数据并返回其字节码
                        if (returnReadByte > 0) {
                            bb.flip();//pos变为0,从buffer头开始读取数据
                            byte[] b=new byte[bb.remaining()];//使用remaining方法获取buffer中的元素大小，并创建对应大小的byte数组
                            bb.get(b);//将buffer中的数据赋给byte数组中
                            String CilentMsg=new String(b, "utf-8");//编译
                            System.out.println("服务端:接收到的信息是"+CilentMsg);
                        }

                        SelectionKey key1 = channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    }else if (key.isValid() && key.isWritable()){
                        System.out.println("channel 可以 write 了");
                    }
                    // NIO的特点只会累加，已选择的键的集合不会删除，ready集合会被清空
                    // 只是临时删除已选择键集合，当该键代表的通道上再次有感兴趣的集合准备好之后，又会被select函数选中
                    it.remove();
                }
            }
        }

    }
    private void writeToClient(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        buffer.put((socketChannel.getRemoteAddress() + "连接成功").getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
    }
    private void readData(SelectionKey key) throws IOException  {
        FileChannel fileChannel = fileMap.get(key);
        buffer.clear();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        try {
            while ((num = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                // 写入文件
                fileChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            key.cancel();
            e.printStackTrace();
        }
        // 调用close为-1 到达末尾
        if (num <=0) {
            buffer.clear();
            fileChannel.close();
            System.out.println("上传完毕");
            buffer.put((socketChannel.getRemoteAddress() + "上传成功").getBytes());
            buffer.clear();
            socketChannel.write(buffer);
            key.cancel();
        }
    }

    public void stop() {
        started = false;
    }
}
