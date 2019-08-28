package 回调函数;

/**
 * Created by lizhen on 2018/11/30.
 */
public class A implements CallBack {

    /*
     * 回调函数实现
     */
    @Override
    public void callBack() {
        System.out.println("B→A: the problem is solved!");
    }

    /*
	 * 登记回调函数,这里把对象A指针给了对象B
	 */
    public void askQuestion(B b){
        System.out.println("A→B: ask B solve the problem!");
		/*
		 * 自己去做其它事
		 */
        Thread thread = new Thread(() -> System.out.println("A want to do another thing!"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		/*
		 * 触发回调关联的事件，可以自己设置个条件，这里没设置
		 */
        b.call(this);

        /*
         * 响应回调事件，后续A可以根据B的调用回调函数的结果做进一步的处理
         */
        System.out.println("thanks B");
    }


    public static void main(String[] args)  {
        A a = new A();
        a.askQuestion(new B());
    }
}
