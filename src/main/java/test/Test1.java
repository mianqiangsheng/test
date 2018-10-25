package test;

import java.util.concurrent.*;

/**
 * Created by lizhen on 2018/10/24.
 */
public class Test1 implements Runnable{

    private CountDownLatch latch;

    private CountDownLatch latch1;

    @Override
    public void run() {

        System.out.println("task-"+Thread.currentThread().getId()+"-string: "+ getString());
        System.out.println("task-"+Thread.currentThread().getId()+"-student: "+ getStudent());
        studentThreadLocal.set(new Student(Thread.currentThread().getName(),(int)Thread.currentThread().getId()));
        stringThreadLocal.set(Thread.currentThread().getName());
        latch.countDown();
        latch1.countDown();
    }

    public void execute(CountDownLatch latch,CountDownLatch latch1){
        set(latch,latch1);
        SERVICE.execute(this);
    }

    class Student {

        private String name;

        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student [name=" + name + ", age=" + age + "]";
        }

    }

    /**
     * 使用定义好的线程池，默认会复用线程
     */
    static ExecutorService SERVICE = Executors.newSingleThreadExecutor();

    /**
     * 使用自定义的多核心线程池，强制不复用线程
     * 也可以使用ThreadPoolExecutor的更便捷的类，如以下2个
     * @see org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean
     * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     */
//    static ExecutorService SERVICE = new ThreadPoolExecutor(2,2,0,
//            TimeUnit.SECONDS, new LinkedBlockingDeque(10));

    ThreadLocal<Student> studentThreadLocal = new ThreadLocal<>();
    ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();


    public void set(CountDownLatch latch, CountDownLatch latch1) {
        this.latch = latch;
        this.latch1 = latch1;
    }

    public Student getStudent() {
        return studentThreadLocal.get();
    }

    public String getString() {
        return stringThreadLocal.get();
    }

    public static ExecutorService getSERVICE() {
        return SERVICE;
    }

    public static void main(String[] args) throws InterruptedException {

        /**
         * 协调main主线程下的ExecutorService控制的多线程任务依次执行，这里task2在task1执行完后执行
         */
        CountDownLatch latch = new CountDownLatch(1);
        /**
         * 协调main线程与ExecutorService控制的多线程任务依次执行，这里main线程在最后执行
         */
        CountDownLatch latch1 = new CountDownLatch(2);

        //创建任务
        Test1 t0 = new Test1();
        //放到线程中执行runnable，作为task1
        t0.execute(latch,latch1); //task1
        //等任务1执行完毕
        latch.await();
        //放到线程中再次执行同一个runnable，作为task2
        /**
         * 可以发现，当重复使用线程池中的线程时，因为ThreadLocal和当前线程绑定，所以在第一次执行run()方法时输出null，并且执行set，
         * 第二次执行run()方法时输出了set后的值，即发生多线程任务串用了任务类中的变量；
         * 如果不重复使用线程池中的线程时，则输出的都是初始值null，因为线程对象不同。
         */
        t0.execute(latch,latch1); //task2
        //等任务2执行完毕
        latch1.await();
        //最后再输出t0任务对象的ThreadLocal变量，发现还是null，因为绑定的是当前main线程
        System.out.println(t0.getString());
        System.out.println(t0.getStudent());

        Test1.getSERVICE().shutdown();

    }

}
