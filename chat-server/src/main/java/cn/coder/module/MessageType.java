package cn.coder.module;

/**
 * @author zhaoyubo
 * @title MessageType
 * @description <TODO description class purpose>
 * @create 2024/3/8 14:57
 **/
public enum MessageType {
    LOGIN(1, "登录"), LOGOUT(2, "注销"), NORMAL(3, "单聊"), BROADCAST(4, "群发");

    private int code;
    private String desc;

    MessageType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
