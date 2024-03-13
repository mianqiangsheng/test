package volatile的使用例子;

public class ReorderExample {
    private int x = 0;
    private int y = 1;
    private boolean flag = false;

    public void writer() {
        x = 42; //1
        y = 50; //2
        flag = true; //3
    }

    public void reader() {
        if (flag) { //4
            System.out.println("x:" + x); //5
            System.out.println("y:" + y); //6
        }
    }

    public static void main(String[] args) {
        ReorderExample reorderExample = new ReorderExample();

        new Thread(()->reorderExample.writer()).start();
        new Thread(()->reorderExample.reader()).start();
    }
}