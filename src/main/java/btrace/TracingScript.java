package btrace;

import org.openjdk.btrace.core.annotations.*;
import org.openjdk.btrace.core.annotations.BTrace;

import static org.openjdk.btrace.core.BTraceUtils.*;

/**
 * @author ：li zhen
 * @description:
 * @date :2022/2/25 9:41
 */
@BTrace
public class TracingScript {

    @OnMethod(
            clazz="btrace.CaseObject",
            method="execute",
            location=@Location(Kind.RETURN)
    )
    public static void traceExecute(@Self btrace.CaseObject object,int sleepTime, @Return boolean result){
        println("apply!");
        println(strcat("result:",str(result)));
        jstack();
        println(strcat("time:",str(sleepTime)));
    }
}
