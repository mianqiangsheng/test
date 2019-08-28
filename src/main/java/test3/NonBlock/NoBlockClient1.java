package test3.NonBlock;

/**
 * Created by lizhen on 2018/11/26.
 */
public class NoBlockClient1 {

    private static String server_host = "127.0.0.1";//服务器地址
    private static int server_port = 9999;//服务器端口
    private static ClientHandle clientHandle ;
    public static void start(){
        start(server_host,server_port);
    }

    //启动一个线程用于连接server
    private static synchronized void start(String server_host2, int server_port2) {
        if(clientHandle!=null)
            clientHandle.shop();
        clientHandle = new ClientHandle(server_host2,server_port2);
        new Thread(clientHandle, "client").start();
    }

    //用于业务请求
    public static boolean sendMsg(String msg,int type)throws Exception{
        if(null == msg||"".equals(msg)||" ".equals(msg))
            return false;
        clientHandle.sendMsg(msg,type);
        return true;
    }
    public static void main(String[] args) {
        start();
    }

}
