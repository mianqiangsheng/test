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

#Test3.java
使用java的I/O和NIO的性能比较

#test3.block（代码包）
使用BIO(阻塞I/O)进行文件的传输

#test3.NonBlock代码包）
使用基于NIO的文件数据传输,这里在试验的时候，对大文件传输可能会出现文件不传完全的情况，不知道怎么解决...
test3.NonBlock.formal包里的代码是正式代码，实现了web服务器单线程管理多个channel连接，同时启用多线程
进行接收消息后的业务处理，模拟了Tomcat、Netty、Nginx的大量长连接、少数活跃连接(真正在传输数据)的业务场景

Selector
selectedKeys集合 （【SelectionKey】channel，interestOps，readyOps，valid，attachment）
Keys集合（【SelectionKey】）
cancelledKeys集合（【SelectionKey】）

Selector的Selector.select() 或 Selector.select(long) 或 Selector.selectNow() 操作执行的工作：
1、在cancelledKeys集合中的SelectionKey都将从3个集合中删除，同时设置cancelledKeys集合为空；
2、底层操作系统在select()方法开始执行时会更新每个剩余的channel的感兴趣的主题的准备就绪情况（interestOps），如果一个channel至少有一个感兴趣的主题准备就绪，则会执行以下2步：
2.1、如果这个channel对应的SelectionKey没有在selectedKeys 集合中，SelectionKey会被添加进selectedKeys 集合，同时这个SelectionKey先前记录在就绪集（selectedKeys同时readyOps）中的任何准备就绪信息都被丢弃。
2.2、如果这个channel对应的SelectionKey已经在selectedKeys 集合中，更新就绪集并且先前记录在就绪集中的任何准备信息都被保留。（实测代码没看出？难道是因为socketChannel的isReadable()方法因为只有接收了一次客户端的请求所以只会触发一次，后面再select()读就没有就绪了，所以只有isWritable()是被判断就绪.....如果2个select()期间，正好客户端又发送数据，则会保留读和写吗？）
如果在步骤（2）开始时，keys集合所有的SelectionKey没有感兴趣的主题，则selectedKeys集合和就绪集合都不回被更新。
3、如果在步骤（2）正在进行时将任何SelectionKey添加到cancelledKeys，则按步骤（1）处理它们。

SelectableChannel.register(selector, SelectionKey.OP_READ)——绑定感兴趣的主题注册一个channel到selector中，会被添加进keys集合；
SelectionKey.cancel() 或者 Channel.close() ——对应的SelectionKey会被添加到cancelledKeys集合中；
Selector.select() 或 Selector.select(long) 或 Selector.selectNow() —— SelectionKey会被从selectedKeys 集合中添加（仅能通过Selector操作添加）或者移除。SelectionKey也可以通过Set<SelectionKey>.remove()或Iterator<SelectionKey>.remove()直接从selectedKeys集合中移除，除此之外不能够通过任何方式被直接移除。

多线程并发情况下Selectors本身是线程安全的，但是他们所持有的key sets不是线程安全的。

一个线程通过selector.select()或selector.select(long)方法产生的阻塞可以被其他线程用以下三种方式的任意一种来中断：
1、By invoking the selector’s wakeup() method,
2、By invoking the selector’s close() method, or
3、By invoking the blocked thread’s interrupt() method, in which case its interrupt status will be set and the selector’s wakeup() method will be invoked.




#ChannelTest.java
使用多种方式获得channel，并用多种方式实现基于channel的文件间的拷贝复制

#DataGramChannelTest.java
使用UDP传输协议的数据传输方式，不像TCP协议那样需要握手等一系列对传输质量的保证控制，但是传输速度更快，占用资源更少。
参考：http://donald-draper.iteye.com/blog/2373281

#PipedTest.java
分别使用流形式的PipedInputStream/PipedOutputStream 和管道形式的Pipe.SourceChannel/Pipe.SinkChannel实现线程间的信息传输。
如果一个管道的写端一直在写，而读端的引用计数是否大于0决定管道是否会堵塞，引用计数大于0，只写不读再次调用write会导致管道堵塞； 
如果一个管道的读端一直在读，而写端的引用计数是否大于0决定管道是否会堵塞，引用计数大于0，只读不写再次调用read会导致管道堵塞； 
而当有一端的引用计数等于0时，（读端引用为零）只写不读会导致写端的进程收到一个SIGPIPE信号，导致进程终止，（写端引用为零）只读不写会导致read返回0,就像读到文件末尾一样。
引用指的是管道是否有线程在使用。
--------------------- 
作者：sky_Mata 
来源：CSDN 
原文：https://blog.csdn.net/skyroben/article/details/71513385 
版权声明：本文为博主原创文章，转载请附上博文链接！

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
<br>2.可见性保证，volatile提供happens-before的保证，确保一个线程的修改能对其他线程是可见的。某些情况下，volatile 还能提供原子性，如读64位数据类型，像long和double都不是原子的(低32位和高32位)，但volatile类型的double和long读写是原子的。
<br>3.对任意单个volatile变量的读/写具有原子性，但类似于volatile++这种复合操作不具有原子性。
<br>4.对一个volatile变量的读，总是能看到（任意线程）对这个volatile变量最后的写入。