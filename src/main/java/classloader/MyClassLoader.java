package classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ：li zhen
 * @description:
 * @date ：2023/7/13 11:21
 */
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            byte[] bytes = loadByte("C:\\Users\\86181\\Desktop\\".concat(name.replace(".", "\\")).concat(".class"));
            return defineClass(name,bytes,0,bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);

    }

    private byte[] loadByte(String path) throws IOException {
        File file = new File(path);
        FileInputStream fi = new FileInputStream(file);
        int len = fi.available();
        byte[] b = new byte[len];
        fi.read(b);
        return b;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        MyClassLoader classLoader = new MyClassLoader();

        Class<?> globalResponseResultAdvice = classLoader.loadClass("com.sribs.ems.common.response.GlobalResponseResultAdvice");

        MyClassLoader classLoader1 = new MyClassLoader();

        /**
         * 双亲委派，先看class是否加载过，从最上层类加载器开始加载
         * 如果MyClassLoader加载目标class找不到，则向上委托父亲找，这时两个不同的classloader加载的同一个class是相同的，因为都是一个父亲classloader加载的
         */
        Class<?> globalResponseResultAdvice1 = classLoader1.loadClass("com.sribs.ems.common.response.GlobalResponseResultAdvice");

        System.out.println(globalResponseResultAdvice == globalResponseResultAdvice1);

        Object o = globalResponseResultAdvice.getDeclaredConstructor(boolean.class).newInstance(true);
        Object o1 = globalResponseResultAdvice1.getDeclaredConstructor(boolean.class).newInstance(true);
    }
}
