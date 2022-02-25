package btrace;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/2/25 9:40
 */
public class CaseObject {

    private static int sleepTotalTime=0;

    public boolean execute(int sleepTime) throws Exception{
        System.out.println("sleep: "+sleepTime);
        sleepTotalTime+=sleepTime;
        Thread.sleep(sleepTime);
        if(sleepTime%2==0)
            return true;
        else
            return false;
    }
}
