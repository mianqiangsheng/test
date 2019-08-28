package 不可重入锁和可重入锁;

/**
 * Created by lizhen on 2018/11/21.
 */
public interface MyLock {

    void lock() throws InterruptedException;
    void unlock();
}
