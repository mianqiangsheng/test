package test1;

/**
 * 每一个Thread维护了一个ThreadLocal.ThreadLocalMap，key是ThreadLocal，value是放到ThreadLocal中的与线程绑定的值
 * get：从当前线程中取出ThreadLocalMap，并取出key为ThreadLocal对象的value
 * set：将key为ThreadLocal且value为绑定对象放入当前线程的ThreadLocalMap
 *
 * Created by lizhen on 2018/12/7.
 */
public class MyThreadLocal<T> extends ThreadLocal<T> {

    Class<T> tClass;

    public MyThreadLocal(Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * ThreadLocal.get()前都会调用这个方法，可以设置初始值，默认是返回null
     * 这里发现一个问题，就是Student如果是普通内部类会报错，只能使用静态内部类——普通内部类生成实例前必须要有外部类对象，而直接调用newInstance()无法提供这个外部类对象，所以报错
     * @return
     */
    @Override
    protected T initialValue() {
        try {
            return tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
