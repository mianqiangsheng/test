package 单例设计模式;

/**
 * 饿汉单例（枚举类）
 * 枚举类实现方式不会被反射和反序列化破环单例
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/3/5 9:43
 */
public enum HungrySingleton2 {

    Instance;

    //枚举类默认构造方法私有
    HungrySingleton2() {
        System.out.println("构造方法");
    }

    //获取对象
    public static HungrySingleton2 getInstance() {
        return Instance;
    }

}
