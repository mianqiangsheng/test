package 桥接方法概念;

import java.lang.reflect.Method;

/**
 * @author Li Zhen
 * @create 2019/8/14
 * @since 1.0.0
 */

public class Test {

    public static void main(String[] args) {

        Method[] methods = SubClass.class.getMethods();
        for (Method method : methods) {
            int i = method.getModifiers();
            boolean isBridge = method.isBridge();
            System.out.println(method.getName()+": "+ i + "是否是桥接方法：" + isBridge);
            System.out.println(Long.toBinaryString(Integer.toUnsignedLong(i)));
            System.out.println(Long.toBinaryString(0x00000040));
        }


        /**
         * 声明了SuperClass类型的变量指向SubClass类型的实例，典型的多态。
         * 在声明SuperClass类型的变量时，不指定泛型类型，那么在方法调用时就可以传任何类型的参数，
         * 因为SuperClass中的方法参数实际上是Object类型，而且编译器也不能发现错误。
         */
        SuperClass superClass = new SubClass();
        /**
         * 调用的是子类的实际方法，所以不会有问题
         *
         */
        System.out.println(superClass.method("abc123"));
        /**
         * 调用的是桥接方法
         * 在运行时当参数类型不是SubClass声明的类型时，会抛出类型转换异常，
         * 因为这时调用的是桥接方法，而在桥接方法中会进行强制类型转换，所以才会抛出类型转换异常。
         */
        System.out.println(superClass.method(new Object()));
    }

}

/**
 * @author Mikan
 * @date 2015-08-05 17:05
 */
interface SuperClass<T> {

    T method(T param);

}

/**
 * @author Mikan
 * @date 2015-08-05 17:05
 */
class SubClass implements SuperClass<String> {
    public String method(String param) {
        return param;
    }
}