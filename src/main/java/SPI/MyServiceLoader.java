package SPI;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * SPI加载类
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/1/12 14:28
 */
public class MyServiceLoader {

    /**
     * 使用SPI机制加载所有的Class
     */
    public static <S> ServiceLoader<S> load(final Class<S> clazz) {
        return ServiceLoader.load(clazz);
    }

    public static void main(String[] args) {
        ServiceLoader<MyService> loader = MyServiceLoader.load(MyService.class);
        Iterator<MyService> iterator = loader.iterator();
        while (iterator.hasNext()){
            MyService next = iterator.next();
            next.print("Hello World");
        }
//        StreamSupport.stream(loader.spliterator(), false).forEach(s -> s.print("Hello World"));
    }
}
