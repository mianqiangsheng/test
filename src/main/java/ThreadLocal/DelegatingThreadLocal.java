package ThreadLocal;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/9 13:14
 */
public class DelegatingThreadLocal<T> extends ThreadLocal<T> {

    private static ThreadLocal<Map<DelegatingThreadLocal<Object>, Object>> holder;

    static {
        holder = new ThreadLocal<>();
        holder.set(Maps.newHashMap());
    }

    @Override
    public T get() {
        return(T)holder.get().get(this);
    }

    @Override
    public void set(T value) {
        synchronized(holder){ //TransmittableThreadLocal没有同步，因为holdr里只有唯一一个TransmittableThreadLocal为key的键值对
            holder.get().put((DelegatingThreadLocal<Object>)this,value);
        }
    }

    public static Map<DelegatingThreadLocal<Object>, Object> compyFrom(){
        Map<DelegatingThreadLocal<Object>, Object> contextMap = holder.get(); //获取当前线程的TheadLocal的value，即Map<DelegatingThreadLocal<Object>, Object>
        Map<DelegatingThreadLocal<Object>,Object> snapshot = Maps.newHashMap(); //创建Map<DelegatingThreadLocal<Object>, Object>快照
        contextMap.forEach(snapshot::put);
        return snapshot;
    }

    public static void copyTo(Map<DelegatingThreadLocal<Object>, Object> holderFromRunnable){
        Map<DelegatingThreadLocal<Object>, Object> holderFromThread = holder.get();
        if(holderFromThread ==null || (CollectionUtils.isEmpty(holderFromThread.entrySet()))){
            holder.set(holderFromRunnable);
            return;
        }
        holderFromRunnable.forEach((key,value)->{
            if(holderFromThread.containsKey(key)){
                holderFromThread.put(key,value);
            }
        });
    }
}
