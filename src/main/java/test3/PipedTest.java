package test3;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhen on 2018/11/28.
 */
public class PipedTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        Pipe pipe = Pipe.open();
        //只读先不写，会阻塞，过5秒写就返回了
        new Thread(new PipeReceiver(pipe)).start();
        TimeUnit.SECONDS.sleep(5);
        new Thread(new PipeSender(pipe)).start();
    }

    @Test
    public void test1(){
        Sender sender = new Sender();
        Receiver receiver = new Receiver();
        PipedInputStream pi = receiver.getPipedInputputStream();
        PipedOutputStream po = sender.getPipedOutputStream();
        try {
            pi.connect(po);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        new Thread(sender).start();
        new Thread(receiver).start();
    }

    @Test
    public void test2() throws IOException {
        Pipe pipe = Pipe.open();
        new Thread(new PipeSender(pipe)).start();
        new Thread(new PipeReceiver(pipe)).start();
    }

    static class Sender implements Runnable {

        PipedOutputStream out;

        public PipedOutputStream getPipedOutputStream() {
            out = new PipedOutputStream();
            return out;
        }

        @Override
        public void run() {

            try {
                out.write("Hello , Reciver!".getBytes());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                out.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Receiver implements Runnable {
        PipedInputStream in;

        public PipedInputStream getPipedInputputStream() {
            in = new PipedInputStream();
            return in;
        }

        @Override
        public void run() {

            byte[] bys = new byte[1024];
            try {
                in.read(bys);
                System.out.println("读取到的信息：" + new String(bys).trim());
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    static class PipeReceiver implements Runnable {

        Pipe pipe;

        public PipeReceiver(Pipe pipe) {
            this.pipe = pipe;
        }

        @Override
        public void run() {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Pipe.SourceChannel source = pipe.source();
            int read;
            try {
                read = source.read(buffer);
                System.out.println(new String(buffer.array(),0,read));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    static class PipeSender implements Runnable {

        Pipe pipe;

        public PipeSender(Pipe pipe) {
            this.pipe = pipe;
        }

        @Override
        public void run() {
            Pipe.SinkChannel sink = pipe.sink();
            try {
                ByteBuffer byteBuffer = ByteBuffer.wrap("通过单向管道发送数据".getBytes());
                sink.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }
    }


}
