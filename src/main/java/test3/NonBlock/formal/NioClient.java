package test3.NonBlock.formal;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lizhen on 2018/11/27.
 */
public class NioClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1",8080);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String request;
        while (true){

            request = bufferedReader.readLine();

            socket.getOutputStream().write(request.getBytes("UTF-8")); //服务器关闭socketChannel，不影响客户端发送数据，但是服务器不会再对这个socketChannel的数据进行读取了

            byte[] bytes = new byte[1024];
            socket.getInputStream().read(bytes); //服务器关闭socketChannel，获取不到这个socketChannel相关的inputStream了，报错...
            System.out.println("收到服务器返回信息：" + new String(bytes,"UTF-8"));
        }
    }
}
