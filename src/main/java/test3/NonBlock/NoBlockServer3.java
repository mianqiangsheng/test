package test3.NonBlock;

/**
 * Created by lizhen on 2018/11/26.
 */
public class NoBlockServer3 {

    private static int server_port=9999;//服务器端口
    private static  ServerHandler serverhandler;//服务端处理类
    public static void start(){
        start(server_port);
    }

    /**
     * 创建一个同步的线程启动服务端
     *
     * **/
    public static synchronized void  start(int server_port2) {
        if (null!=serverhandler) {
            serverhandler.shop();
        }
        serverhandler = new ServerHandler(server_port2);

        new Thread(serverhandler, "server").start();
    }
    public static void main(String[] args) {
        start();
    }

}
