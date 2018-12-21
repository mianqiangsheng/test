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

        /* volatile只保证对变量（让long和double原本不是原子性的变量类型也实现原子性）读写（a = ××或者 b = a）的原子性，但不能保证复杂操作的原子性（a++等） */
        data.increaseCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "] second read");
        int newValue = data.getCounter();
        System.out.println("[Thread " + Thread.currentThread().getId() + "]: New value = " + newValue);
    }

}
