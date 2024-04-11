package cn.coder.user;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoyubo
 * @title UserManager
 * @description 用户管理器
 * @create 2024/3/11 13:37
 **/
public class UserManager {

    private Map<String, User> users = new HashMap<>();
    /**
     * key是ip和端口号，value是用户名
     */
    private Map<SocketChannel, String> onlineUsers = new HashMap<>();

    private static UserManager INSTANCE;

    // 设计为单例模式，全局统一调用，统一存储用户登录信息
    public static UserManager getInstance() {
        if (INSTANCE == null) {
            synchronized (UserManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserManager();
                }
            }
        }
        return INSTANCE;
    }

    private UserManager() {}

    public synchronized boolean login(SocketChannel channel, String username) {
        if (users.containsKey(username)) {
            System.out.println("重复登录.....");
            return false;
        } else {
            User user = new User(username, channel);
            users.put(username, user);
            onlineUsers.put(channel, username);
            return true;
        }
    }

    public synchronized void logout(SocketChannel channel) {
        String username = onlineUsers.get(channel);
        System.out.printf("s%下线....", username);
        users.remove(username);
        onlineUsers.remove(channel);
    }

    public synchronized SocketChannel getUserChannel(String username) {
        User user = users.get(username);
        if (user == null) {
            return null;
        }
        SocketChannel lastLoginChannel = user.getChannel();
        if (onlineUsers.containsKey(lastLoginChannel)) {
            return lastLoginChannel;
        } else {
            return null;
        }
    }

}
