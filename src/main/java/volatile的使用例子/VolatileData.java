package volatile的使用例子;

/**
 * Created by lizhen on 2018/11/22.
 */
public class VolatileData {

    private volatile int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void increaseCounter() {
        counter++;
        counter++;
    }
}
