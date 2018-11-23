# 平时的测试学习代码

#Test.java
使用logback日志记录框架；
配合logback.xml的配置自定义输出日志记录；
使用LoggerContext自定义日志配置文件名/位置；

#Test1.java
ThreadLocal与线程池的应用注意点——注意复用线程会存在变量的线程同步问题；
使用CountDownLatch实现多个线程任务的顺序执行；

# pom.xml
指定maven编译项目时跳过指定的java源文件
<excludes>
  <exclude>test/Colored.java</exclude>
</excludes>

#Test2.java
java代码使用restemplate调用外部的https的接口




# 研究多线程相关知识

## 不可重入锁和可重入锁 （代码包）
ReentrantLock便是一种可重入锁，可重入锁，也叫做递归锁，指的是同一线程外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响。
在JAVA环境下 ReentrantLock 和synchronized 都是可重入锁。可重入锁最大的作用是避免死锁。

重入锁（ReentrantLock）相比 synchronized 有哪些优势：
<br>1、可以在线程等待锁的时候中断线程(使用lockInterruptibly())，synchronized 是做不到的。
<br>2、可以尝试获取锁，如果获取不到就放弃(tryLock())，或者设置一定的时间（tryLock(long timeout, TimeUnit unit)），这也是 synchroized 做不到的。
<br>3、可以设置公平锁（ReentrantLock(boolean fair)），synchronized 默认是非公平锁，无法实现公平锁。
<br> 当然劣势也有，比如代码书写复杂度上升、必须手动unLock锁，一般都是优先考虑synchronized关键字，再考虑Lock。

## 拓展

公平锁
直接加入同步队列，线程将按照它们发出请求的顺序来获取锁。

非公平锁
线程发出请求的时可以“插队”获取锁，老的线程排队使用锁；但是无法保证新线程抢占已经在排队的线程的锁。若成功立刻返回,失败则加入同步队列

Lock和synchronize都是默认使用非公平锁的（如果不是必要的情况下，不要使用公平锁，公平锁会来带一些性能的消耗）。

ReentrantReadWriteLock读写锁
ReadWriteLock接口定义的方法就两个：

Lock readLock(); 返回一个读锁
Lock writeLock(); 返回一个写锁
规则
读锁不支持条件对象，写锁支持条件对象
读锁不能升级为写锁，写锁可以降级为读锁
读写锁也有公平和非公平模式
读锁支持多个读线程进入临界区，写锁是互斥的

锁优化
减少锁持有时间
需要线程安全的代码放进同步代码块中

减少锁粒度
将大对象拆成更小粒度的小对象加锁，如ConCurrentHashMap

锁分离
读写锁的基本思想是将读与写进行分离，因为读不会改变数据，所以读与读之间不需要进行同步，只要有写锁进入就需要做同步处理，但是对于大多数应用来说，读的场景要远远大于写的场景，因此一旦使用读写锁，在读多写少的场景中，就可以很好的提高系统的性能。如：LinkedBlockingQueue，从头部拿数据(读)，添加数据(写)则在尾部，读与写这两者操作的数据在不同的部位，因此可以同时进行操作，使并发级别更高，除非队列或链表中只有一条数据。这就是读写分离思想的进一下延伸：只要操作不相互影响，锁就可以分离。

锁粗化
一个程序对同一个锁不间断、高频地请求、同步与释放，会消耗掉一定的系统资源。锁粗化就是把很多次锁的请求合并成一个请求，降低短时间内大量锁请求、同步、释放带来的性能损耗。


## volatile的使用例子 （代码包）
volatile 主要有两方面的作用:
<br>1.避免指令重排，例如，JVM 或者 JIT为了获得更好的性能会对语句重排序，但是 volatile 类型变量即使在没有同步块的情况下赋值也不会与其他语句重排序。
<br>2.可见性保证，volatile 提供 happens-before 的保证，确保一个线程的修改能对其他线程是可见的。某些情况下，volatile 还能提供原子性，如读 64 位数据类型，像 long 和 double 都不是原子的(低32位和高32位)，但 volatile 类型的 double 和 long 就是原子的。