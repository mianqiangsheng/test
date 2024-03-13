package volatile的使用例子;

import java.util.concurrent.TimeUnit;

/**
 * @author ：li zhen
 * @description:
 * @date ：2023/12/1 9:04
 */
public class VolatileVisibleSample {

    private static boolean stopRequested;
//    private static volatile boolean stopRequested;
    public static void main(String[] args)
            throws InterruptedException {

        /**
         * stopRequested不是volatile标注时，
         *
         * 当加上System.out.println(stopRequested);这条语句的时候，发现可以感知到其他线程对a的修改，最终通过判断打印出a
         * 当去掉System.out.println(stopRequested);这条语句的时候，发现不可感知到其他线程对a的修改，最终死循环不退出
         *
         * ----》
         *
         * 线程可见性：只要刷写到主存/从主存读，就可见；
         *
         * 没有volatile标注，并非没有感知其他线程动作的能力，只是不能保证线程可以觉察到其他线程动作
         *
         * 出现这种情况可能:
         * 1、两个线程偶然跑在同一个cpu core上（单核跑多线程没有可见性问题，但仍然有原子性、顺序性问题）
         * 2、JVM从单线程角度优化代码，导致从自己线程角度来看，运行中不需要去刷写到主存或者从主存读
         * 3、和cpu指令的硬件逻辑有关——cpu判断下来可能去寄存器读、可能去缓存读，可能去内存读，总之无法保证
         *
         * 添加volatile标注，去掉这种模糊性，保证跨线程的已发生的动作的可见性（happens-before）
         *
         * 保证了线程间本地变量的可见性（保证某个线程做的修改write，其他线程能看到read）；
         * 线程间的代码同步顺序（保证某个线程一旦感知到其他线程某个动作write，某个动作write代码顺序之前的动作一定是已经发生）；
         * 变量读写原子性（原子性从主存读到CPU寄存器、原子性从CPU寄存器刷写到主存）
         *
         * 可以看出是在小范围内解决了线程同步问题，仅仅针对某个volatile变量
         *
         * volatile限制了JVM对代码进行重排序的自由度、限制了CPU如何去刷写到主存或者从主存读的自由度，滥用volatile会降低代码执行效率
         */
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested){
//                System.out.println(stopRequested);
                i++;
            }
            System.out.println(i);
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
