package ThreadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 实现子线程通过ThreadLocal从父线程拿到数据，但子线程修改数据不影响主线程（主线程修改数据在子线程可以看到），即父线程->子线程单向数据传递
 *
 * 商业实现：TransmittableThreadLocal（简称TTL，alibaba一个工具包中的类，继承自InheritableThreadLocal，解决线程池场景下的变量传递问题）
 *
 * ThreadLocal：父子线程不会传递threadLocal副本到子线程中
 * InheritableThreadLocal：在子线程创建的时候，父线程会把threadLocal拷贝到子线中（但是线程池的子线程不会频繁创建，就不会传递信息）
 * TransmittableThreadLocal：解决了2中线程池无法传递线程本地副本的问题，在构造类似Runnable接口对象时进行初始化，而且子线程中的本地副本执行完方法后会恢复为传进来的时候的值
 *
 * https://github.com/alibaba/transmittable-thread-local/issues/123
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/3/9 13:08
 */
public class Test2 {

//    private static ThreadLocal<String> localStr = new ThreadLocal<>();
    private static DelegatingThreadLocal<String> localStr = new DelegatingThreadLocal<String>();

//    private static ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static ExecutorService executorService = new DelegatingExecutorService(Executors.newFixedThreadPool(2));


    public static void main(String[] args) throws InterruptedException {

        localStr.set("happy new year");

        final Thread[] thread = new Thread[1];

        executorService.execute(()->{
            System.out.println("我是线程0-0:" + Thread.currentThread().getName() + ":" + localStr.get());
            localStr.set("sad old year");
            System.out.println("我是线程0-1:"+ Thread.currentThread().getName() + ":"+ localStr.get());

            executorService.execute(()->{
                System.out.println("我是线程0-0-0:"+ Thread.currentThread().getName() + ":"+ localStr.get());
            });

            localStr.set("sad old new year");
            thread[0] = new Thread(
                    new DelegatingRunnable(() -> System.out.println("我是线程0-0-1:"+ Thread.currentThread().getName() + ":"+ Thread.currentThread().getName() + ":"+ localStr.get())));
//            thread[0].start();
        });

        TimeUnit.SECONDS.sleep(1);

        localStr.set("----------");

        executorService.execute(()->{
            System.out.println("我是线程0-2:"+ Thread.currentThread().getName() + ":"+ localStr.get());
        });

        TimeUnit.SECONDS.sleep(1);

        executorService.execute(()->{
            System.out.println("我是线程0-3:"+ Thread.currentThread().getName() + ":"+ localStr.get());
        });

        TimeUnit.SECONDS.sleep(1);

        thread[0].start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println("我是线程0:"+ Thread.currentThread().getName() + ":"+ localStr.get());
    }
}
