package SPI;

/**
 * MyService第二个实现
 *
 * @author ：li zhen
 * @description:
 * @date ：2024/1/12 14:32
 */
public class MyServiceB implements MyService {
    @Override
    public void print(String info) {
        System.out.println(MyServiceB.class.getName() + " print " + info);
    }
}
