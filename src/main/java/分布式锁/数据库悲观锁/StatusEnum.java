package 分布式锁.数据库悲观锁;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/7/13 13:06
 */
public enum StatusEnum {

    /**
     * 成功
     */
    SUCCESS("S","成功"),

    /**
     * 失败
     */
    FAIL("F","失败"),

    /**
     * 处理
     */
    PROCESS("P","处理");

    private String code;
    private String des;

    StatusEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

}
