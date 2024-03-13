package ThreadLocal;

import java.util.Map;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/9 13:43
 */
public class DelegatingRunnable implements Runnable{
    private final Map<DelegatingThreadLocal<Object>,Object> holder;
    private final Runnable runnable;

    public DelegatingRunnable(Runnable runnable) {
        this.holder = DelegatingThreadLocal.compyFrom();
        this.runnable = runnable;
    }

    @Override
    public void run() {
        DelegatingThreadLocal.copyTo(holder);
        runnable.run();
    }
}
