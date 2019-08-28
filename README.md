# 平时的测试学习代码

#桥接方法概念.Test.java
##什么是桥接方法
桥接方法是 JDK 1.5 引入泛型后，为了使Java的泛型方法生成的字节码和 1.5 版本前的字节码相兼容，由编译器自动生成的方法。
我们可以通过Method.isBridge()方法来判断一个方法是否是桥接方法，在字节码中桥接方法会被标记为ACC_BRIDGE和ACC_SYNTHETIC，其中ACC_BRIDGE用于说明这个方法是由编译生成的桥接方法，ACC_SYNTHETIC说明这个方法是由编译器生成，并且不会在源代码中出现。可以查看jvm规范中对这两个access_flag的解释http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.6。

##什么时候会生成桥接方法
那什么时候编译器会生成桥接方法呢？可以查看JLS中的描述http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.12.4.5。
就是说一个子类在继承（或实现）一个父类（或接口）的泛型方法时，在子类中明确指定了泛型类型，那么在编译时编译器会自动生成桥接方法（当然还有其他情况会生成桥接方法，这里只是列举了其中一种情况）。可以参考package桥接方法概念.Test.java。

以下是使用javap命令查看泛型接口和一个实现该泛型接口的字节码class文件所列出的信息。
```java
D:\ideaProject\test\src\main\java>javap -c -v SubClass.class
Classfile /D:/ideaProject/test/src/main/java/SubClass.class
  Last modified 2019-8-13; size 487 bytes
  MD5 checksum ae550a00029edac1b033475875721e64
  Compiled from "Test.java"
class SubClass extends java.lang.Object implements SuperClass<java.lang.String>
  minor version: 0
  major version: 52
  flags: ACC_SUPER
Constant pool:
   #1 = Methodref          #5.#18         // java/lang/Object."<init>":()V
   #2 = Class              #19            // java/lang/String
   #3 = Methodref          #4.#20         // SubClass.method:(Ljava/lang/String;)Ljava/lang/String;
   #4 = Class              #21            // SubClass
   #5 = Class              #22            // java/lang/Object
   #6 = Class              #23            // SuperClass
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               method
  #12 = Utf8               (Ljava/lang/String;)Ljava/lang/String;
  #13 = Utf8               (Ljava/lang/Object;)Ljava/lang/Object;
  #14 = Utf8               Signature
  #15 = Utf8               Ljava/lang/Object;LSuperClass<Ljava/lang/String;>;
  #16 = Utf8               SourceFile
  #17 = Utf8               Test.java
  #18 = NameAndType        #7:#8          // "<init>":()V
  #19 = Utf8               java/lang/String
  #20 = NameAndType        #11:#12        // method:(Ljava/lang/String;)Ljava/lang/String;
  #21 = Utf8               SubClass
  #22 = Utf8               java/lang/Object
  #23 = Utf8               SuperClass
{
  SubClass();
    descriptor: ()V
    flags:
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 259: 0

  public java.lang.String method(java.lang.String);
    descriptor: (Ljava/lang/String;)Ljava/lang/String;
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=2, args_size=2
         0: aload_1
         1: areturn
      LineNumberTable:
        line 261: 0

  public java.lang.Object method(java.lang.Object);
    descriptor: (Ljava/lang/Object;)Ljava/lang/Object;
    flags: ACC_PUBLIC, ACC_BRIDGE, ACC_SYNTHETIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: aload_1
         2: checkcast     #2                  // class java/lang/String   →指定类型强转为String
         5: invokevirtual #3                  // Method method:(Ljava/lang/String;)Ljava/lang/String;   →指定调用SubClass第二个我们实现的方法
         8: areturn
      LineNumberTable:
        line 259: 0
}
Signature: #15                          // Ljava/lang/Object;LSuperClass<Ljava/lang/String;>;
SourceFile: "Test.java"



D:\ideaProject\test\src\main\java>javap -c -v SuperClass.class
Classfile /D:/ideaProject/test/src/main/java/SuperClass.class
  Last modified 2019-8-13; size 235 bytes
  MD5 checksum 28a4683b169d15ccdbd4d61929037f84
  Compiled from "Test.java"
interface SuperClass<T extends java.lang.Object>
  minor version: 0
  major version: 52
  flags: ACC_INTERFACE, ACC_ABSTRACT
Constant pool:
   #1 = Class              #10            // SuperClass
   #2 = Class              #11            // java/lang/Object
   #3 = Utf8               method
   #4 = Utf8               (Ljava/lang/Object;)Ljava/lang/Object;
   #5 = Utf8               Signature
   #6 = Utf8               (TT;)TT;
   #7 = Utf8               <T:Ljava/lang/Object;>Ljava/lang/Object;
   #8 = Utf8               SourceFile
   #9 = Utf8               Test.java
  #10 = Utf8               SuperClass
  #11 = Utf8               java/lang/Object
{
  public abstract T method(T);
    descriptor: (Ljava/lang/Object;)Ljava/lang/Object;   //→可以发现实际上字节码class文件中泛型T被Object替代了，也就是所谓的泛型擦除
    flags: ACC_PUBLIC, ACC_ABSTRACT
    Signature: #6                           // (TT;)TT;
}
Signature: #7                           // <T:Ljava/lang/Object;>Ljava/lang/Object;
SourceFile: "Test.java"
```
可以发现SubClass实现泛型接口指定泛型后，有3个方法：一个是默认的无参构造方法（代码中虽然没有明确声明，但是编译器会自动生成）；一个是我们实现的泛型接口中的方法实现；
最后一个就是很特殊的**桥接方法**
是编译器自动生成的桥接方法。可以看到flags包括了ACC_BRIDGE和ACC_SYNTHETIC，表示是编译器自动生成的方法，参数类型和返回值类型都是Object。再看这个方法的字节码，它把Object类型的参数强制转换成了String类型，再调用在SubClass类中声明的方法，转换过来其实就是：
```java
    public Object method(Object param) {
        return this.method(((String) param));
    }
```
##为什么要生成桥接方法
在java1.5以前，比如声明一个集合类型：
```java
List list = new ArrayList();
```
那么往list中可以添加任何类型的对象，但是在从集合中获取对象时，无法确定获取到的对象是什么具体的类型，所以在1.5的时候引入了泛型，在声明集合的时候就指定集合中存放的是什么类型的对象：
```java
List<String> list = new ArrayList<String>();
```
那么在获取时就不必担心类型的问题，因为泛型在编译时编译器会检查往集合中添加的对象的类型是否匹配泛型类型，如果不正确会在编译时就会发现错误，而不必等到运行时才发现错误。因为泛型是在1.5引入的，为了向前兼容，所以会在编译时去掉泛型（泛型擦除），但是我们还是可以通过反射API来获取泛型的信息，在编译时可以通过泛型来保证类型的正确性，而不必等到运行时才发现类型不正确。
由于java泛型的擦除特性，如果不生成桥接方法，那么与1.5之前的字节码就不兼容了（SuperClass有Signature: #7 这个参数和返回值都是Object的method()方法，如果不生成桥接方法，就不能运行1.5以前的class文件了）。

通过SuperClass的
```java
Signature: #7   // <T:Ljava/lang/Object;>Ljava/lang/Object;
```
 可以看到，在编译完成后泛型实际上就成了Object了，所以方法实际上成了
```java
public abstract Object method(Object param);
```
而SubClass实现了SuperClass这个接口，如果不生成桥接方法，那么SubClass就没有实现接口中声明的方法，首先语义就不正确了，所以编译器才会自动生成桥接方法，来保证兼容性。

##如何通过桥接方法获取实际的方法
我们在通过反射进行方法调用时，如果获取到桥接方法对应的实际的方法呢？可以查看spring中org.springframework.core.BridgeMethodResolver类的源码。实际上是通过判断方法名、参数的个数以及泛型类型参数来获取的。

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