package 单例设计模式;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 饿汉单例（直接实例化、静态代码块）
 * 在类加载的时候就立即初始化，并且创建单例对象。它绝对线程安全，在线程还没出现以前就实例化了，不可能存在访问安全问题。
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/3/5 9:43
 */
public class HungrySingleton1 implements Serializable {
    // 静态实例代码段，饿汉实现类加载初始化时调用构造方法
    // 这里也可以放到stati{}代码块进行实例化
    private static final HungrySingleton1 hungrySingleton = new HungrySingleton1();

    // 私有方法防止外部调用创建对象
    private HungrySingleton1() {
        if(hungrySingleton!=null)// 此处方式反射调用破环单例对象，抛出异常
            throw new RuntimeException("单例模式不能创建");
        System.out.println("构造方法");
    }

    //防止反序列化破坏单例（ObjectStreamClass）
    /** class-defined readResolve method, or null if none
     *  private Method readResolveMethod;
     * */
    public Object readResolve(){
        return  hungrySingleton;
    }

    // 外部类获得单例对象方法
    public static HungrySingleton1 getInstance() {
        return hungrySingleton;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        /**
         * 序列化（反序列化读出原来的实例对象）
         */
        //反序列化
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
        //将类转化
        objectOutputStream.writeObject(HungrySingleton1.getInstance());
        System.out.println(HungrySingleton1.getInstance());
        ObjectInputStream objectInputStream=new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        //读出类，变为一个新的类
        HungrySingleton1 test= (HungrySingleton1) objectInputStream.readObject();
        System.out.println(test);

        /**
         * 反射（禁止通过反射获取新实例）
         */
        System.out.println(HungrySingleton1.getInstance());
        //反射破坏
        //得到类
        Class c = HungrySingleton1.class;
        Constructor<?> constructor = c.getDeclaredConstructor();
        //设置私有可调用
        constructor.setAccessible(true);
        // 打印创建的实例对象
        System.out.println(constructor.newInstance());
    }


}
