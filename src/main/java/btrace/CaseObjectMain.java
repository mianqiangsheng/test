package btrace;

import java.util.Random;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/2/25 9:40
 */
public class CaseObjectMain {

    public static void main(String[] args) throws Exception{

        /**
         * 输出服务启动的进程id（pid）
         * 或使用jps命令查看
         */
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(Long.parseLong(processName.split("@")[0]));

        Random random=new Random();
        CaseObject object=new CaseObject();
        while(true){
            boolean result=object.execute(random.nextInt(1000));
            Thread.sleep(60000);
        }
    }
}
