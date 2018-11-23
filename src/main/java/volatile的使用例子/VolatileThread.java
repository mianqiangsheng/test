package volatile的使用例子;

/**
 * Created by lizhen on 2018/11/22.
 */
public class VolatileThread extends Thread {

    private final VolatileData data;

    public VolatileThread(VolatileData data) {
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("[Thread " + Thread.currentThread().getId() + "] first read");
        int oldValue = data.getCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "]: Old value = " + oldValue);

        /* 因为操作的counter参数是int，所以效果就是多线程下读写counter是原子操作。同时是volatile的，而且也不会与其他线程的代码语句被jvm进行重排，一定会在线程中执行完整个方法逻辑代码,即读后增加再赋值一定完整执行 */
        data.increaseCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "] second read");
        int newValue = data.getCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "]: New value = " + newValue);
    }

}
