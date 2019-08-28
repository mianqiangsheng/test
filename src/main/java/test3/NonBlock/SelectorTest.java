package test3.NonBlock;

import java.util.Scanner;

/**
 * Created by lizhen on 2018/11/26.
 */
public class SelectorTest {

    public static void main(String[] args) throws Exception{
        NoBlockServer3.start(9999);//启动服务器

        Thread.sleep(1000);//等待

        NoBlockClient1.start();//启动客户端

        String line;
        do {
            line = new Scanner(System.in).nextLine();
        } while(NoBlockClient1.sendMsg(line.split(" ")[0],Integer.parseInt(line.split(" ")[1])));


    }
}
