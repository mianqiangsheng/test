package 不可重入锁和可重入锁;

/**
 * Created by lizhen on 2018/11/21.
 */
public class MyReentrantLock implements MyLock{

    /* 标识此锁是否被某个线程使用上锁 */
    boolean isLocked = false;
    /* 标识此锁是哪个线程使用 */
    Thread  lockedBy = null;
    /* 标识此锁被某个线程重复获得几次 */
    int lockedCount = 0;

    public synchronized void lock() throws InterruptedException{
        Thread thread = Thread.currentThread();
        while(isLocked && lockedBy != thread){
            wait();
        }
        isLocked = true;
        lockedCount++;
        lockedBy = thread;
    }

    public synchronized void unlock(){
        if(Thread.currentThread() == this.lockedBy){
            lockedCount--;
            if(lockedCount == 0){
                isLocked = false;
                notify();
            }
        }
    }
}
