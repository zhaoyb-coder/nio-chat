package cn.coder;

import cn.coder.util.ProtoStuffUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zhaoyubo
 * @title ChatClient
 * @description <TODO description class purpose>
 * @create 2024/3/8 15:44
 **/
public class ChatClient {

    public static final int DEFAULT_BUFFER_SIZE = 1024;
    private Selector selector;
    private SocketChannel clientChannel;
    private ByteBuffer buf;
    private TextField tfText;
    private TextArea taContent;
    //private ReceiverHandler listener;
    private String username;
    private boolean isLogin = false;
    private boolean isConnected = false;
    private Charset charset = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        System.out.println("开始客户端初始化......");
        ChatClient client = new ChatClient();
        //client.launch();
    }

    public ChatClient() {
        initNetWork();
    }

    private void initNetWork() {
        try {
            selector = Selector.open();
            clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9000));
            //设置客户端为非阻塞模式
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
            login();
            isConnected = true;
        } catch (ConnectException e) {
            System.out.println("服务器未启动，连接服务器失败......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        System.out.println("请输入用户名:");
        Scanner scanner = new Scanner(System.in, "UTF-8");
        String username="";
        while (scanner.hasNext()) {
            username = scanner.next();
            break;
        }
        System.out.println("登录成功："+username);
        try {
            clientChannel.write(ByteBuffer.wrap(ProtoStuffUtil.serialize(username)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username = username;
    }
}
