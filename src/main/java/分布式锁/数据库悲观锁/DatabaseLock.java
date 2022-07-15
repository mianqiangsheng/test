package 分布式锁.数据库悲观锁;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/7/13 10:39
 */
@Component
public class DatabaseLock {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional //一定要加事务
    public boolean lock(String keyResource, int time){

        String getLockSql = "SELECT * FROM t_resource_lock WHERE key_resource = ? for update";
        List<ResourceLock> resourceLocks = jdbcTemplate.query(getLockSql, new Object[]{keyResource}, new BeanPropertyRowMapper<>(ResourceLock.class));

        LocalDateTime now = LocalDateTime.now();
        try{
            if(CollectionUtils.isEmpty(resourceLocks)){
                //插入锁的数据
                String insertLockSql = "INSERT INTO t_resource_lock VALUES (?, ?, ?, ?, ?, ?, ?)";
                InetAddress ip4 = Inet4Address.getLocalHost();
                int count = jdbcTemplate.update(insertLockSql, keyResource, StatusEnum.PROCESS.getCode(), 1, now, null, ip4.getHostAddress(), time);
                if(count==1){
                    //获取锁成功
                    return true;
                }
                return false;
            }
        }catch(Exception x){
            return false;
        }

        ResourceLock resourceLock = resourceLocks.get(0);
        //没上锁并且锁已经超时，即可以获取锁成功
        if(resourceLock.getLock_flag() == 0 && "S".equals(resourceLock.getStatus())
                && now.isAfter(resourceLock.getBegin_time().plusSeconds(time))){
            //update resourceLock;
            String updateLockSql = "UPDATE t_resource_lock set lock_flag = ?,status = ?,begin_time = ? where key_resource = ?";
            int count = jdbcTemplate.update(updateLockSql, 1, StatusEnum.PROCESS.getCode(), now, keyResource);
            if(count==1){
                //获取锁成功
                return true;
            }
            return false;
        }else if(now.isAfter(resourceLock.getBegin_time().plusSeconds(time))){
            //超时未正常执行结束,获取锁失败
            return false;
        }else{
            return false;
        }
    }

    public void unlock(String keyResource, StatusEnum status){
        //解锁
        //update resourceLock;
        String updateLockSql = "UPDATE t_resource_lock set lock_flag = ?,status = ? where key_resource = ?";
        jdbcTemplate.update(updateLockSql, 0, status.getCode(), keyResource);
    }
}
