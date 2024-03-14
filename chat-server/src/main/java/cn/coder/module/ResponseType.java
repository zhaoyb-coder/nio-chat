package cn.coder.module;

/**
 * @author zhaoyubo
 * @title ResponseType
 * @description 响应类型
 * @create 2024/3/11 14:09
 **/
public enum ResponseType {
    NORMAL(1,"消息"),
    PROMPT(2,"提示");

    private int code;
    private String desc;

    ResponseType(int code, String desc) {
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

