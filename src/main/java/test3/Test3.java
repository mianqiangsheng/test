package test3;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lizhen on 2018/11/22.
 */
public class Test3 {

    private long transferFile(File source, File des)throws IOException {

        long startTime = System.currentTimeMillis();

        if(!des.exists())
            des.createNewFile();

        long endTime;

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source))) {

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des))) {

                //将数据源读到的内容写入目的地--使用数组

                byte[] bytes = new byte[1024 * 1024];

                int len;

                while ((len = bis.read(bytes)) != -1) {

                    bos.write(bytes, 0, len);

                }
            }
        }

        endTime = System.currentTimeMillis();

        return endTime - startTime;

    }

    private long transferFileWithNIO(File source, File des)throws IOException{

        long startTime = System.currentTimeMillis();

        if(!des.exists())
            des.createNewFile();

        FileChannel readChannel;
        FileChannel writeChannel;
        ByteBuffer byteBuffer;
        long endTime;

        byteBuffer = ByteBuffer.allocate(1024 * 1024);

        try (RandomAccessFile read = new RandomAccessFile(source, "rw")) {

            readChannel = read.getChannel();

            try (RandomAccessFile write = new RandomAccessFile(des, "rw")) {

                writeChannel = write.getChannel();

                while(readChannel.read(byteBuffer) != -1) {

                    /**
                     * limit = position;
                     * position = 0;
                     * mark = -1;
                     * 准备从buffer中取数据，切换成读模式，能让管道writeChannel继续读取byteBuffer的数据写入到管道目的地
                     */
                    byteBuffer.flip();

                    writeChannel.write(byteBuffer);
                    /**
                     * position = 0;
                     * limit = capacity;
                     * mark = -1;
                     * 复原buffer状态，切换成写模式，能让管道readChannel继续读取文件的数据
                     */
                    byteBuffer.clear();

                }
            }
        }



        endTime = System.currentTimeMillis();

        return endTime - startTime;

    }

    public static void main(String[] args)throws IOException{

        Test3 simpleFileTransferTest =new Test3();

        File sourse =new File("C:\\Users\\Administrator\\Desktop\\videojs试验\\children.mpg");

        File des =new File("D:\\io.avi");

        File nio =new File("D:\\nio.avi");

        long time = simpleFileTransferTest.transferFile(sourse, des);

        System.out.println(time +"：普通字节流时间");

        long timeNio = simpleFileTransferTest.transferFileWithNIO(sourse, nio);

        System.out.println(timeNio +"：NIO时间");

    }
}
