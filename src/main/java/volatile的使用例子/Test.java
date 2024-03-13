package volatile的使用例子;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ：li zhen
 * @description:
 * @date ：2023/12/1 13:20
 */

class Person{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class Test {

    private static int a = 0;
    private static int[] array = new int[1];
    private static volatile Person person = new Person();

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(2,2,0,TimeUnit.SECONDS,new LinkedBlockingQueue<>(10));

    public static void main(String[] args) {

        executor.execute(() -> {

//            while (!(array[0] == 2)){
//
//            }
//            System.out.println("inner Thread: " + array);

            while (person.getName() == null){

            }
            System.out.println("inner Thread: " + person.getName());

        });
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            array[0] = 2;

            person.setName("lizhen");
        });

    }
}
