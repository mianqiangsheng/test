package util;

/**
 * break/continue 默认只能针对直接的那个循环，通过标记写法，可以指定针对某个循环
 * break retry 跳到retry处，且不再进入循环
 * continue retry 跳到retry处，且再次进入循环
 *
 * @author ：li zhen
 * @description:
 * @date ：2023/1/31 13:59
 */
public class LoopSyntax {

    public static void main(String[] args) {
        breakRetry();
        continueRetry();
    }

    private static void breakRetry() {
        int i = 0;
        retry:
        for (; ; ) {
            System.out.println("start");
            for (; ; ) {
                i++;
                if (i == 4)
                    break retry;
            }
        }
        //start 进入外层循环
        //4
        System.out.println(i);
    }

    private static void continueRetry() {
        int i = 0;
        retry:
        for(;;) {
            System.out.println("start");
            for(;;) {
                i++;
                if (i == 3)
                    continue retry;
                System.out.println("end");
                if (i == 4)
                    break retry;
            }
        }
        //start 第一次进入外层循环
        //end i=1输出
        //end i=2输出
        //start 再次进入外层循环
        //end i=4输出
        //4 最后输出
        System.out.println(i);
    }

}
