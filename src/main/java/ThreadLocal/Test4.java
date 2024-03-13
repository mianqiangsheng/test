package ThreadLocal;

import jodd.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/13 9:45
 */
@SpringBootApplication(scanBasePackages = "ThreadLocal")
public class Test4 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test4.class);

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(2);

    private static final TtlThreadPoolTaskExecutor paymentThreadPool = new TtlThreadPoolTaskExecutor();
    static {
        paymentThreadPool.setCorePoolSize(2);
        paymentThreadPool.setMaxPoolSize(2);
        paymentThreadPool.setKeepAliveSeconds(0);
        paymentThreadPool.setQueueCapacity(10);
        paymentThreadPool.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("trans-dispose-%d").get());
        paymentThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        paymentThreadPool.initialize();
    }


    public static void main(String[] args) throws InterruptedException {

//        SpringApplication.run(Test4.class,args);
//
//        /**
//         * 使用原生的MDC，只能追踪同一个线程下的业务流程，无法追踪该线程开启的其他线程的业务流程
//         */
//        MDC.put("trace-id","追踪链路1");
//
//        SERVICE.execute(() -> {
//            LOGGER.info("first time:" + MDC.get("trace-id"));
//        });
//        SERVICE.execute(() -> {
//            LOGGER.info("second time:" + MDC.get("trace-id"));
//        });
//        SERVICE.execute(() -> {
//            LOGGER.info("third time:" + MDC.get("trace-id"));
//        });

        SpringApplication springApplication = new SpringApplication(Test4.class);
        /**
         * TtlMDCAdapterInitializer生效方式3
         * 无需启动加载获得MDCAdapter，由StaticMDCBinder提供给MDC加载创建
         */
        springApplication.addInitializers(new TtlMDCAdapterInitializer());
        springApplication.run(args);

        MDC.put("trace-id","追踪链路1");

        paymentThreadPool.execute(() -> {
            LOGGER.info("first time:" + MDC.get("trace-id"));
        });
        paymentThreadPool.execute(() -> {
            LOGGER.info("second time:" + MDC.get("trace-id"));
        });
        paymentThreadPool.execute(() -> {
            LOGGER.info("third time:" + MDC.get("trace-id"));
            new Thread(() -> {
                LOGGER.info("third time:" + MDC.get("trace-id"));
                MDC.put("trace-id","修改追踪链路1");
            });
        });

        TimeUnit.SECONDS.sleep(1);

        LOGGER.info("forth time:" + MDC.get("trace-id"));
    }
}
