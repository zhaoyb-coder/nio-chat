package cn.coder.module;

/**
 * @author zhaoyubo
 * @title TaskType
 * @description <TODO description class purpose>
 * @create 2024/3/8 14:58
 **/
public enum TaskType {
    FILE(1,"文件"),
    CRAWL_IMAGE(2,"豆瓣电影图片");
    private int code;
    private String desc;

    TaskType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
