package test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by lizhen on 2018/9/13.
 */
class A{
    private static int count;
    private final int id = count++;


    public A(){
        System.out.println("构造函数中count值为："+count);
    }

    public int getId() {
        return id;
    }
}

/**
 * @SpringBootApplication 配合 new SpringApplicationBuilder(Test.class).run(args)
 * 启动srpring框架的自动启动类，这里会启动内置的tomcat容器......
 */
@SpringBootApplication(scanBasePackages = "test")
public class Test {

    protected static LoggerContext lc;
    protected static Logger logger;

    static {
        File logbackFile = new File(Test.class.getResource("/logback-spring.xml").getFile());
        if (logbackFile.exists()) {
            lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            try {
                configurator.doConfigure(logbackFile);
                logger = lc.getLogger(Test.class);
            }
            catch (JoranException e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args)  {
        A a1 = new A();
        System.out.println(a1.getId());
        A a2 = new A();
        System.out.println(a2.getId());
        int i =0;
        while (i<3){
            logger.trace("This is a trace message"+ i +" at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logger.debug("This is a debug message"+ i +" at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logger.info("This is an info message"+ i +" at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logger.warn("This is a warn message"+ i +" at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logger.error("This is an error message"+ i +" at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            i++;
        }
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ApplicationContext context = new SpringApplicationBuilder(Test.class).run(args);

    }
}
