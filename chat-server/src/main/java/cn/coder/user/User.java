package cn.coder.user;

import lombok.Builder;

import java.nio.channels.SocketChannel;

/**
 * @author zhaoyubo
 * @title User
 * @description 用户类描述
 * @create 2024/3/11 13:37
 **/
@Builder
public class User {
    private String username;
    private String password;
    private SocketChannel channel;

    public User(String username, String password, SocketChannel channel)
    {
        this.username = username;
        this.password = password;
        this.channel = channel;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public SocketChannel getChannel()
    {
        return channel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }
}
