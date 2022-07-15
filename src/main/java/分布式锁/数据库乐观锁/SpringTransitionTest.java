package 分布式锁.数据库乐观锁;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import 分布式锁.Config;
import 分布式锁.数据库悲观锁.DatabaseLock;
import 分布式锁.数据库悲观锁.ResourceLock;
import 分布式锁.数据库悲观锁.StatusEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lizhen on 2018/9/18.
 */
public class SpringTransitionTest {

    private ApplicationContext ctx = null;
    private OptimisticLock optimisticLock = null;


    {
        ctx = new AnnotationConfigApplicationContext(Config.class);
        optimisticLock = ctx.getBean(OptimisticLock.class);
    }

    @Test
    public void testOptimisticLock() {
        Boolean aBoolean = optimisticLock.optimisticLockExec(1, new BigDecimal(5));
        System.out.println(aBoolean);
    }

}
