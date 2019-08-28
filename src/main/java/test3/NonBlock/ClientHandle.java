package test3.NonBlock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lizhen on 2018/11/26.
 */
public class ClientHandle implements Runnable {

    private String host;//地址
    private int port;//端口
    private SocketChannel clientSocketChannel;//通道
    private Selector clientSelector;//选择器
    private volatile boolean started;//线程可见的客户端状态

    public ClientHandle(String server_host2, int server_port2) {
        this.host = server_host2;
        this.port = server_port2;
        try {
            clientSelector = Selector.open();//开启选择器
            clientSocketChannel = clientSocketChannel.open();//开启通道
            clientSocketChannel.configureBlocking(false);//设为非堵塞模式

            started = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shop() {
        started = false;
    }

    @Override
    public void run() {
        try{
            doConnect();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        //循环遍历selector
        while(started){
            try{
                //无论是否有读写事件发生，selector每隔1s被唤醒一次
                clientSelector.select(1000);
                Set<SelectionKey> keys = clientSelector.selectedKeys();
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
        if(clientSelector != null)
            try{
                clientSelector.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void handleInput(SelectionKey key) throws IOException{
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

    public void sendMsg(String msg,int type)throws Exception {
        clientSocketChannel.register(clientSelector, SelectionKey.OP_READ);
        doWrite(clientSocketChannel, msg,type);

    }
    private void doConnect() throws IOException{
        if(clientSocketChannel.connect(new InetSocketAddress(host,port))){

        }
        else clientSocketChannel.register(clientSelector, SelectionKey.OP_CONNECT);
    }

    //异步发送消息
    private void doWrite(SocketChannel channel,String request,int type) throws IOException{
        if(0 == type){
            //将消息编码为字节数组
            byte[] bytes = request.getBytes();
            //根据数组容量创建ByteBuffer
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            //将字节数组复制到缓冲区
            writeBuffer.put(bytes);
            //flip操作
            writeBuffer.flip();
            //发送缓冲区的字节数组
            channel.write(writeBuffer);
            //****此处不含处理“写半包”的代码
        }else if(1 == type){
//            try (FileInputStream fileInputStream = new FileInputStream(new File(request))) {
//                FileChannel fileChannel = fileInputStream.getChannel();
//                ByteBuffer buffer = ByteBuffer.allocate(1024);
//
//                int num;
//                while ((num=fileChannel.read(buffer)) > 0) {
//                    buffer.flip();
//                    channel.write(buffer);
//                    buffer.clear();
//                }
//                if (num == -1) {
//                    fileChannel.close();
//                    channel.shutdownOutput();
//                }
//            }
        }

    }

    public SocketChannel getClientSocketChannel() {
        return clientSocketChannel;
    }
}
