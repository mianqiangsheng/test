package 分布式锁.数据库悲观锁;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import 分布式锁.Config;

/**
 * Created by lizhen on 2018/9/18.
 */
public class SpringTransitionTest {

    private ApplicationContext ctx = null;
    private DatabaseLock databaseLock = null;


    {
        ctx = new AnnotationConfigApplicationContext(Config.class);
        databaseLock = ctx.getBean(DatabaseLock.class);
    }

    @Test
    public void testDatabaseLock() {
        StatusEnum statusEnum = null;
        boolean getLock = false;
        try{
            getLock = databaseLock.lock("keyResource1", 60);
            if(getLock){ //加锁
                statusEnum = StatusEnum.SUCCESS;//你的业务逻辑处理
            }
        } finally{
            if (getLock){
                databaseLock.unlock("keyResource1",statusEnum); //释放锁
            }
        }
    }

}
