package 单例设计模式;

import java.io.Serializable;

/**
 * 懒汉单例（静态内部类）
 * 静态内部类相对来说更优，LazyHoler里面的逻辑需等外部方法调用时候才执行（内部类延迟加载），
 * 所以也属于懒汉式，巧妙运用了内部类的特性，JVM底层逻辑，完美避免了线程安全问题。
 * @author ：li zhen
 * @description:
 * @date ：2024/3/5 9:43
 */
public class LazySingleton2 implements Serializable {

    //虽然构造方法私有了，但是逃不过反射的法眼
    private LazySingleton2(){
        // 防止调用者反射攻击;
        if(LazyHoler.LAZY != null){
            throw new RuntimeException("禁止创建多个实例！"); //其他写法也可加上
        }
    }

    // 懒汉式单例
    // LazyHoler里面的逻辑需等外部方法调用时候才执行
    // 巧妙运用了内部类的特性
    // JVM底层逻辑，完美避免了线程安全问题
    public static final LazySingleton2 getInstance(){
        return LazyHoler.LAZY;
    }

    public static class LazyHoler{
        private static final LazySingleton2 LAZY = new LazySingleton2();
    }


}
