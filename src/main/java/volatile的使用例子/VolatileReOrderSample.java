package volatile的使用例子;

/**
 * @author ：li zhen
 * @description:
 * @date ：2023/11/30 11:14
 */
public class VolatileReOrderSample {

    private static int x = 0, y = 0;

    private static int a = 0, b =0;

//    private static volatile int x = 0;
//    private static volatile int y = 0;
//
//    private static int a = 0;
//    private static int b = 0;

    public static void main(String[] args) throws InterruptedException {

        int i = 0;

        for (;;){

            i++;

            x = 0; y = 0;

            a = 0; b = 0;

            Thread t1 = new Thread(() -> {

                a = 1;

                x = b;

            });

            Thread t2 = new Thread(() -> {

                b = 1;

                y = a;

            });

            t1.start();

            t2.start();

            t1.join();

            t2.join();

            String result = "第" + i + "次 (" + x + "," + y + "）";

            if(x == 0 && y == 0) {

                System.err.println(result);

                break;

            } else {

                System.out.println(result);

            }

        }

    }
}
