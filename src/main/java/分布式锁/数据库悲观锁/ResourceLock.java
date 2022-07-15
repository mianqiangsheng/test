package 分布式锁.数据库悲观锁;

import java.time.LocalDateTime;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/7/13 10:53
 */
public class ResourceLock {

    private String key_resource;
    private String status;
    private Integer lock_flag;
    private LocalDateTime begin_time;
    private LocalDateTime end_time;
    private String client_ip;
    private Integer time;

    public String getKey_resource() {
        return key_resource;
    }

    public void setKey_resource(String key_resource) {
        this.key_resource = key_resource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLock_flag() {
        return lock_flag;
    }

    public void setLock_flag(Integer lock_flag) {
        this.lock_flag = lock_flag;
    }

    public LocalDateTime getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(LocalDateTime begin_time) {
        this.begin_time = begin_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
