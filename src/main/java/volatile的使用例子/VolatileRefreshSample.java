package volatile的使用例子;

import java.util.concurrent.TimeUnit;

public class VolatileRefreshSample {
    private static int x = 0;
    private static String y = "a";
    private volatile static boolean flag;

    public void writer() {
        x = 42;
        y = "b";
        flag = true;
    }

    public void reader() {
        System.out.println("x-1:" + x);
        System.out.println("y-1:" + y);
        while (!flag) {

        }
        System.out.println("x-2:" + x);
        System.out.println("y-2:" + y);
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileRefreshSample reorderExample = new VolatileRefreshSample();

        new Thread(()->reorderExample.reader()).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(()->reorderExample.writer()).start();

    }
}