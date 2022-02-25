package btrace;

/**
 * 自己写的配合ThreadStart的样例代码
 * @author ：li zhen
 * @description:
 * @date ：2022/2/23 16:34
 */
@Deprecated
public class BTrace {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 输出服务启动的进程id（pid）
         * 或使用jps命令查看
         */
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(Long.parseLong(processName.split("@")[0]));

        for (;;){
            new Thread(() -> System.out.println("创建新线程：" + Thread.currentThread().getName() )).start();
//            System.out.println("循环一次");

            Thread.sleep(60000);
        }

    }
}
