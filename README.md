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