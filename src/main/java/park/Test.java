package park;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/3/2 9:34
 */
public class Test {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        Thread parkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "进入park");
                countDownLatch.countDown();
                /**
                 * 该方法会使当前线程进入wait状态
                 * 可以通过LockSupport.unpark()或Thread.interrupt()或意料之外的错误导致返回来解除wait状态
                 */
                LockSupport.park(this);
                System.out.println(Thread.currentThread().getName() + "解除park");
            }
        }, "parkThread");
        parkThread.start();

        countDownLatch.await();

        System.out.println("block for 3 seconds");
        Thread.sleep(3000);
        System.out.println("unpark parkThread");

        /**
         * 第一种方式通过LockSupport.unpark(thread)
         */
        Thread unParkThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "进入unPark");
            LockSupport.unpark(parkThread);
            System.out.println(Thread.currentThread().getName() + "退出unPark");
        }, "unParkThread");
        unParkThread.start();

        /**
         * 第二种方式通过thread.interrupt()
         */
//        System.out.println("parkThread interrupt");
//        parkThread.interrupt();

    }
}
