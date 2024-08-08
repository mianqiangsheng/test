package ThreadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TransmittableThreadLocal使用
 *
 * 相较于自己写的DelegatingThreadLocal，最不同的地方就是多了子线程对接收父线程后的数据进行操作后，有一个恢复的步骤
 * 因为在线程池中，线程可能复用，为了防止在runnable执行中对该线程的ThreadLocal产生了污染，然后该线程被复用去执行其他Runnable时该值已被修改，不再是调用线程的值了，所以需要还原现场。
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/3/9 13:08
 */
public class Test3 {

    public static void main(String[] args) throws InterruptedException {
        TransmittableThreadLocal<String> local = new TransmittableThreadLocal<>();
        local.set("我是主线程");
        //生成额外的代理
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //**核心装饰代码！！！！！！！！！**
        executorService = TtlExecutors.getTtlExecutorService(executorService);
        executorService.submit(() -> {
            System.out.println("子线程：" + local.get());
            local.set("我是线程池线程1");
        });
        TimeUnit.SECONDS.sleep(1);
//        local.set("修改主线程");
        System.out.println("主线程：" + local.get());
        executorService.submit(() -> {
            System.out.println("子线程：" + local.get());
        });
    }

}
