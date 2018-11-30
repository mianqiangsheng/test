package 回调函数;

/**
 * Created by lizhen on 2018/11/30.
 */
public class B {

    /*
	 * 调用回调函数，这里对象B调用了对象A的callBack()方法
	 */
    public void call(CallBack a){
		/*
		 * b help a solve the priblem
		 */
        System.out.println("B help A solve the problem!");
		/*
		 * call back
		 */
        a.callBack();

    }
}
