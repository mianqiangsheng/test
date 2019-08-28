package test3;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by lizhen on 2018/11/27.
 */
public class DataGramChannelTest {

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                DatagramChannel dc = DatagramChannel.open();
                dc.bind(new InetSocketAddress(9898));
                dc.configureBlocking(false);
                Selector selector = Selector.open();
                dc.register(selector, SelectionKey.OP_READ);

                while (true){
                    selector.select(1000L); //设置最多1秒左右阻塞返回
                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();

                    while (keyIterator.hasNext()){
                        SelectionKey key = keyIterator.next();
                        if (key.isValid() && key.isReadable()){
                            ByteBuffer buf = ByteBuffer.allocate(1024);
                            dc.receive(buf);
                            buf.flip();
                            System.out.println(new String(buf.array(),0,buf.limit()));
                            buf.clear();
//                            key.cancel();
                        }
                        keyIterator.remove();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                DatagramChannel dc = DatagramChannel.open();
                dc.configureBlocking(false);
                ByteBuffer buf = ByteBuffer.allocate(1024);
                Scanner scan = new Scanner(System.in);

                while (scan.hasNextLine()){
                    String str = scan.nextLine();
                    buf.put(str.getBytes());
                    buf.flip();
                    dc.send(buf,new InetSocketAddress("127.0.0.1",9898));
                    buf.clear();
                }
                dc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


    }

}
