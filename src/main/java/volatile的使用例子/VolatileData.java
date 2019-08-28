package volatile的使用例子;

/**
 * Created by lizhen on 2018/11/22.
 */
public class VolatileData {

    private volatile int counter = 0;

    public int getCounter() {
        return counter;
    }

    /* 使用synchronized，保证counter最终值为2000000,不然不能保证（虽然每个线程执行了1000000次，但是counter++不是原子性的，导致可能增加的值没有写进主存就丢失了值） */
    public void increaseCounter() {
        for (int i = 0;i<1000000;i++)
            counter++;
    }
}
