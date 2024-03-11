package cn.coder.user;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaoyubo
 * @title UserManager
 * @description 用户管理器
 * @create 2024/3/11 13:37
 **/
public class UserManager {

    private Map<String, User> users;
    /**
     * key是ip和端口号，value是用户名
     */
    private Map<SocketChannel, String> onlineUsers;

    private static UserManager INSTANCE;

    //设计为单例模式，全局统一调用，统一存储用户登录信息
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


    private UserManager() {
        users = new ConcurrentHashMap<>();
        users.put("user1", User.builder().username("user1").password("pwd1").build());
        users.put("user2", User.builder().username("user2").password("pwd2").build());
        users.put("user3", User.builder().username("user3").password("pwd3").build());
        users.put("user4", User.builder().username("user4").password("pwd4").build());
        users.put("user5", User.builder().username("user5").password("pwd5").build());
        onlineUsers = new ConcurrentHashMap<>();
    }

    public synchronized  boolean login(SocketChannel channel, String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }
        User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            return false;
        }
        if(user.getChannel() != null){
            System.out.println("重复登录.....");
            //重复登录会拒绝第二次登录
            return false;
        }
        user.setChannel(channel);
        onlineUsers.put(channel, username);
        return true;
    }

    public synchronized void logout(SocketChannel channel) {
        String username = onlineUsers.get(channel);
        System.out.printf("s%下线....",username);
        users.get(username).setChannel(null);
        onlineUsers.remove(channel);
    }

    public synchronized SocketChannel getUserChannel(String username) {
        User user = users.get(username);
        if(user == null){
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

