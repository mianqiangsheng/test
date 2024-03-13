package SPI;

/**
 * MyService第一个实现
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/1/12 14:31
 */
public class MyServiceA implements MyService {
    @Override
    public void print(String info) {
        System.out.println(MyServiceA.class.getName() + " print " + info);
    }
}
