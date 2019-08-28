package test3.NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lizhen on 2018/11/26.
 */
public class ServerHandler implements Runnable{

    private Selector selector;//NIO中的选择器
    private ServerSocketChannel serverchannel;//通道
    private volatile boolean started;//服务器状态并线程可见

    public ServerHandler(int server_port2) {
        try {
            selector=Selector.open();//1.开启选择器，用于监听、轮查
            serverchannel=ServerSocketChannel.open();//2.开启通道
            serverchannel.configureBlocking(false);//3.调用configureBlocking(),开启非堵塞模式，true:堵塞;false:非堵塞

            //4.通过ServerSocketChannel中的socket()获取一个ServerSocket，然后使用ServerSocket的bind方法为其绑定通讯地址，并设置连接队列长度
            serverchannel.socket().bind(new InetSocketAddress(server_port2),1024);
            //5.将信道注册到选择器上并配置事件类型
            /*
                SelectionKey.OP_ACCEPT —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
                SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
                SelectionKey.OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
                SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
            */
            serverchannel.register(selector, SelectionKey.OP_ACCEPT);

            started=true;//6.完成上述操作后，将服务器状态变为启动

            System.out.println("服务器已启动，端口号"+server_port2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shop() {
        started = false;
    }

    @Override
    public void run() {
        //通过started来判断服务器是否启动，并在启动的情况下遍历selector
        while (started) {
            try {
                selector.select(1000);//设置选择器的工作周期1s
                Set<SelectionKey> keys=selector.selectedKeys();//使用Set集合获取当前选择器中的主键集合
                Iterator<SelectionKey> it=keys.iterator();//使用迭代器,遍历set集合
                SelectionKey key = null;  //定义key 用于接收遍历后的值
                while (it.hasNext()) {
                    key=it.next();//赋值
                    //稍微提下 在多线程情况下 要删除集合里的元素 需要使用迭代器的remove方法 因为remove方法可以保障从源集合中安全删除对象
                    it.remove();
                    handleInput(key);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //释放资源
        if(selector != null)
            try{
                selector.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void handleInput(SelectionKey key)throws IOException {
        //判断key是否有效
        if (key.isValid()) {
            //通过isAcceptable方法判断是否有新的信息进入通道，有着进行下步处理
            if (key.isAcceptable()) {
                ServerSocketChannel ssc=(ServerSocketChannel) key.channel();//创建新的通道用于接收key代表的通道
                SocketChannel sc = ssc.accept(); //从accept中获取连接通道
                sc.configureBlocking(false);//设置非堵塞

                sc.register(selector, SelectionKey.OP_READ);//注册到选择器中，并规定模式为读
            }
            //读取通道信息
            if (key.isReadable()) {
                SocketChannel  sc = (SocketChannel) key.channel();//创建新的通道用于接收key代表的通道
                ByteBuffer bb = ByteBuffer.allocate(1024);//使用静态方法allocate定义一个1M的byte类型的缓存区域，用于保存通道中的数据
                int returnReadByte = sc.read(bb);//读取通道中的数据并返回其字节码
                if (returnReadByte > 0) {
                    bb.flip();//pos变为0,从buffer头开始读取数据
                    byte[] b=new byte[bb.remaining()];//使用remaining方法获取buffer中的元素大小，并创建对应大小的byte数组
                    bb.get(b);//将buffer中的数据赋给byte数组中
                    String CilentMsg=new String(b, "utf-8");//编译
                    System.out.println("服务端:接收到的信息是"+CilentMsg);
                    //业务处理...........
                    String result=CilentMsg+"(已处理)";
                    //发送应答消息
                    doWrite(sc,result);
                }
                //链路已经关闭，释放资源
                else if(returnReadByte<0){
                    key.cancel();
                    sc.close();
                }

//                try (FileInputStream fileInputStream = new FileInputStream(new File("D:\\star_src_1_600_400_1.jpg"))) {
//
//                    FileChannel fileChannel = fileInputStream.getChannel();
//                    bb.clear();
//                    SocketChannel socketChannel = (SocketChannel) key.channel();
//                    int num = 0;
//                    try {
//                        while ((num = socketChannel.read(bb)) > 0) {
//                            bb.flip();
//                            // 写入文件
//                            fileChannel.write(bb);
//                            bb.clear();
//                        }
//                    } catch (IOException e) {
//                        key.cancel();
//                        e.printStackTrace();
//                    }
//                    // 调用close为-1 到达末尾
//                    if (num == -1) {
//                        fileChannel.close();
//                        System.out.println("上传完毕");
//                        key.cancel();
//                    }
//                }

//                if (returnReadByte == -1) {
//                    sc.shutdownOutput();
//                }


            }
        }
    }

    private void doWrite(SocketChannel sc, String result)  throws IOException{
        byte[] bytes = result.getBytes(); //将返回信息变成字节数组
        ByteBuffer rebb=ByteBuffer.allocate(bytes.length);//根据数组容量创建ByteBuffer
        rebb.put(bytes);  //将字节数组复制到缓冲区
        rebb.flip();   //flip操作
        sc.write(rebb);  //想通道中发送缓冲区的字节数组
    }

}
