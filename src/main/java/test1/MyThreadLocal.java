package test1;

/**
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
