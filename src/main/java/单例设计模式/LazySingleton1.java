package 单例设计模式;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 懒汉单例（双重检查锁）
 * 懒汉式类被加载的时候，没有立刻被实例化，第一次调用getInstance的时候，才真正的实例化。
 * @author ：li zhen
 * @description:
 * @date ：2024/3/5 9:43
 */
public class LazySingleton1 implements Serializable {

    private LazySingleton1() {
    }

    private volatile static LazySingleton1 instance;

    public static LazySingleton1 getInstance() {
        //确定是否需要阻塞
        if (instance == null) {
            // 线程安全：双重检查锁（同步代码块）
            synchronized (LazySingleton1.class) {
                //确定是否需要创建实例
                if (instance == null) {
                    //这里在多线程的情况下会出现指令重排的问题，所以对共有资源instance使用关键字volatile修饰
                    instance = new LazySingleton1();
                }
            }
        }
        return instance;
    }


}
