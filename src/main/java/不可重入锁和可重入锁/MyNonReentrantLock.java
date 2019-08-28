package 不可重入锁和可重入锁;

/**
 * Created by lizhen on 2018/11/21.
 */
public class MyNonReentrantLock implements MyLock{

    /* 标识此锁是否被某个线程使用上锁 */
    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException{
        while(isLocked){
            wait();
        }
        isLocked = true;
    }
    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}
