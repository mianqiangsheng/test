package 分布式锁.数据库乐观锁;

import java.math.BigDecimal;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/7/13 15:47
 */
public class Account {

    private Integer user_id;
    private BigDecimal balance;
    private Integer version;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
