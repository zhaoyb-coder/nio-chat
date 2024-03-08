package cn.coder.module;

import java.nio.channels.SocketChannel;

/**
 * @author zhaoyubo
 * @title Task
 * @description 任务的最小单元
 * @create 2024/3/8 14:55
 **/
public class Task {

    /**接收的信息通道*/
    private SocketChannel receiver;
    /**任务类型*/
    private TaskType type;
    /**任务描述*/
    private String desc;
    /**具体信息*/
    private Message message;
}
