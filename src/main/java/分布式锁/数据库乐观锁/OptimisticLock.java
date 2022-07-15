package 分布式锁.数据库乐观锁;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/7/13 16:00
 */
@Component
public class OptimisticLock {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean optimisticLockExec(Integer userId, BigDecimal deduct) {
        int i = 0;

        try {
            String selectSql = "SELECT version,balance FROM account WHERE user_id = ?";
            String updateSql = "UPDATE account SET balance = ? ,version = version + 1 where version = ? and balance >= ? and user_id = ?";

            while (i < 3) {
                List<Account> accounts = jdbcTemplate.query(selectSql, new Object[]{userId}, new BeanPropertyRowMapper<>(Account.class));

                if (!CollectionUtils.isEmpty(accounts)) {
                    Account account = accounts.get(0);
                    if (account.getBalance().compareTo(deduct) < 0) {
                        return false;
                    }
                    BigDecimal left_balance = account.getBalance().subtract(deduct);


                    int update = jdbcTemplate.update(updateSql, left_balance, account.getVersion(), left_balance, userId);
                    if (update == 1) {
                        return true;
                    }
                    Thread.sleep(500);
                }
            }
        } catch (Exception x) {
            return false;
        }
        return false;
    }
}
