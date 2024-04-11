package cn.coder.user;

import java.nio.channels.SocketChannel;

import lombok.Builder;

/**
 * @author zhaoyubo
 * @title User
 * @description 用户类描述
 * @create 2024/3/11 13:37
 **/
@Builder
public class User {
    private String username;
    private SocketChannel channel;

    public User(String username, SocketChannel channel) {
        this.username = username;
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }
}
