package 不可重入锁和可重入锁;

import java.util.concurrent.TimeUnit;

/**
 * Created by lizhen on 2018/11/21.
 */
public class Test implements Runnable{

    public Test(MyLock lock) {
        this.lock = lock;
    }

    MyLock lock;

    public void print() throws InterruptedException {
        lock.lock();
        System.out.println("print() do something");
        doAdd();
        lock.unlock();
    }

    public void doAdd() throws InterruptedException {
        lock.lock();
        //do something
        System.out.println("doAdd() do something");
        lock.unlock();
    }

    public void run() {
        try {
            print();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MyNonReentrantLock myNonReentrantLock = new MyNonReentrantLock();
        MyReentrantLock myReentrantLock = new MyReentrantLock();

        Test test1 = new Test(myNonReentrantLock);
        Test test2 = new Test(myReentrantLock);

        /**
         * 不可重入锁，即若当前线程执行某个方法已经获取了该锁，那么在方法中尝试再次获取锁时，就会获取不到被阻塞。当前线程执行print()方法首先获取lock，接下来执行doAdd()方法就无法执行doAdd()中的逻辑，必须先释放锁。这个例子很好的说明了不可重入锁。
         * 因为底层使用的是wait()，所以可以调用interrupt()中断
         */
        Thread thread = new Thread(test1);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();


        /**
         * 可重入锁,线程可以进入它已经拥有的锁的同步代码块。
         * 两个线程调用print()方法，第一个线程调用print()方法获取锁，进入lock()方法，由于初始lockedBy是null，所以不会进入while而挂起当前线程，而是是增量lockedCount并记录lockBy为第一个线程。接着第一个线程进入doAdd()方法，由于同一进程，所以不会进入while而挂起，接着增量lockedCount，当第二个线程尝试lock，由于isLocked=true,所以他不会获取该锁，直到第一个线程调用两次unlock()将lockCount递减为0，才将标记为isLocked设置为false。
         ---------------------
         作者：Androider_Zxg
         来源：CSDN
         原文：https://blog.csdn.net/u012545728/article/details/80843595
         版权声明：本文为博主原创文章，转载请附上博文链接！
         */
//        new Thread(test2).start();
//        new Thread(test2).start();
    }


}
