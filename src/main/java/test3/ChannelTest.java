package test3;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by lizhen on 2018/11/22.
 */
public class ChannelTest {

    public static final String inPath = "D:\\channel_1.txt";
    public static final String outPath = "D:\\channel_2.txt";

    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;

    /**
     * 获得只读的FileChannel通道
     * @param path 文件路径
     * @return FileChannel通道
     * @throws FileNotFoundException
     */
    public FileChannel getInChannel1(String path) throws FileNotFoundException {

        // 1. 通过本地IO的方式来获取通道
         fileInputStream =new FileInputStream(path);

        // 得到文件的输入通道
        FileChannel inChannel = fileInputStream.getChannel();
        return inChannel;
    }

    /**
     * 获得只写的FileChannel通道
     * @param path 文件路径
     * @return FileChannel通道
     * @throws FileNotFoundException
     */
    public FileChannel getOutChannel1(String path) throws IOException {

        File des = new File(path);
        if(!des.exists())
            des.createNewFile();

        // 1. 通过本地IO的方式来获取通道
        fileOutputStream =new FileOutputStream(path);

        // 得到文件的输入通道o
        FileChannel outChannel = fileOutputStream.getChannel();
        return outChannel;
    }

    /**
     * 使用FileChannel.open()获得FileChannel通道,同时用StandardOpenOption指定通道的只读只写属性（可叠加配置）
     * @param path
     * @param option
     * @return
     * @throws IOException
     */
    public FileChannel getChannel2(String path, StandardOpenOption... option) throws IOException {

        File des = new File(path);
        if(!des.exists())
            des.createNewFile();

        // 2. jdk1.7后通过静态方法.open()获取通道
        return FileChannel.open(Paths.get(path), option);
    }

    /**
     * 使用FileChannel配合缓冲区实现文件复制的功能(非直接缓冲区)
     * @param inChannel 读文件通道
     * @param outChannel 写文件通道
     * @throws IOException
     */
    public void channelCopy1(FileChannel inChannel,FileChannel outChannel) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while(inChannel.read(byteBuffer) != -1) {

            byteBuffer.flip();

            outChannel.write(byteBuffer);

            byteBuffer.clear();
        }

        inChannel.close();
        outChannel.close();

    }

    /**
     * 通道之间通过transfer()实现数据的传输(直接缓冲区)
     * @param inChannel 读文件通道
     * @param outChannel 写文件通道
     * @throws IOException
     */
    public void channelCopy2(FileChannel inChannel,FileChannel outChannel) throws IOException {

        inChannel.transferTo(0,inChannel.size(),outChannel);

        inChannel.close();
        outChannel.close();

    }

    /**
     * 在FileChannel上调用map()方法，将文件直接映射到内存中创建,使用内存映射文件的方式实现文件复制的功能(直接缓冲区)
     * @param inChannel 读文件通道
     * @param outChannel 写文件通道
     * @throws IOException
     */
    public void channelCopy3(FileChannel inChannel,FileChannel outChannel) throws IOException {

        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY,0,inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());

        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        inChannel.close();
        outChannel.close();

    }

    /**
     * 从通道读取到多个buffer & 指定编码（系统中已存在的file，只能使用这个file的编码格式进行读取；如果是先创建file，可以使用FileChannel.write(ByteBuffer.wrap("some text encode as utf16e".getBytes("UTF-16BE")));指定写时候的编码）
     * @param inChannel 读文件通道
     * @throws IOException
     */
    public void disperseRead(FileChannel inChannel) throws IOException {

        /**
         * 指定文件保存时的编码格式，确保读出来显示不是乱码
         */
        Charset cs = Charset.forName("GBK");

        /**
         * 分配2个ByteBuffer来读，因为读取后要显示为字符，所以可能存在字节数组大小不匹配当前编码字符所需的字节大小，末尾可能会出现乱码，调整数组大小即可解决.比如 buf1 = ByteBuffer.allocate(10)，会有乱码
         */
        ByteBuffer buf1 = ByteBuffer.allocate(9);
        ByteBuffer buf2 = ByteBuffer.allocate(10);
        ByteBuffer[] bufs = {buf1,buf2};

        /**
         * 按顺序读取到ByteBuffer[]
         */
        inChannel.read(bufs);

        /**
         * 读取到一个ByteBuffer，使用指定编码显示
         */
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        while(inChannel.read(byteBuffer) != -1) {
//            System.out.println("循环");
//        }
//        String result = new String(byteBuffer.array(),0,byteBuffer.position(),cs);
//        System.out.println(result);

        /**
         * CharBuffer配合ByteBuffer使用
         */
//        CharsetEncoder encoder = cs.newEncoder();
//        CharsetDecoder decoder = cs.newDecoder();
//        CharBuffer cBuf1 = CharBuffer.allocate(5);
//        cBuf1.put("我爱你！");
//        ByteBuffer buffer = encoder.encode(cBuf1);
//        CharBuffer decode = decoder.decode(buffer);

        System.out.println(new String(bufs[0].array(),0,bufs[0].position(),cs));
        System.out.println("----------------------------------------------");
        System.out.println(new String(bufs[1].array(),0,bufs[1].position(),cs));

        inChannel.close();
    }

    /**
     * 聚集多个ByteBuffer写入通道，ByteBuffer.wrap()和CharBuffer都可以实现指定编码写入
     * @param outChannel 写文件通道
     * @throws IOException
     */
    public void integrateWrite(FileChannel outChannel) throws IOException {

        ByteBuffer buffer1 = ByteBuffer.wrap("我爱你！".getBytes("GBK"));

        Charset cs = Charset.forName("GBK");
        CharsetEncoder encoder = cs.newEncoder();
        CharBuffer cBuf = CharBuffer.allocate(20);
        cBuf.put("I love you!");
        cBuf.flip(); /** 必须要执行flip()，不然encoder.encode(cBuf)会对cBuf从position开始剩余的字符进行编码（全是空格） */
        ByteBuffer buffer2 = encoder.encode(cBuf);

        ByteBuffer[] bufs = {buffer1,buffer2};
        outChannel.write(bufs);
        outChannel.close();
    }


    public static void main(String[] args) {

        ChannelTest channelTest = new ChannelTest();

        /**
         * 用第一种方式获得通道，用第一种方式实现文件拷贝
         */
        try {
            channelTest.channelCopy1(channelTest.getInChannel1(inPath),channelTest.getOutChannel1(outPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 用第二种方式获得通道，用第二种方式实现文件拷贝
         */
        try {
            channelTest.channelCopy2(channelTest.getChannel2(inPath,StandardOpenOption.READ),channelTest.getChannel2(outPath,StandardOpenOption.WRITE,StandardOpenOption.APPEND));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 用第二种方式获得通道，用第三种方式实现文件拷贝
         * channelTest.getChannel2(outPath,StandardOpenOption.WRITE,StandardOpenOption.READ)必须增加READ，不然会报错。因为FileChannel.MapMode.READ_WRITE
         */
        try {
            channelTest.channelCopy3(channelTest.getChannel2(inPath,StandardOpenOption.READ),channelTest.getChannel2(outPath,StandardOpenOption.WRITE,StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channelTest.disperseRead(channelTest.getChannel2(inPath,StandardOpenOption.READ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channelTest.integrateWrite(channelTest.getChannel2(outPath,StandardOpenOption.WRITE,StandardOpenOption.APPEND));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
