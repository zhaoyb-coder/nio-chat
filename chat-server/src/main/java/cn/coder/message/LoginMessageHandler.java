package cn.coder.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import cn.coder.module.Message;
import cn.coder.module.MessageHeader;
import cn.coder.module.Response;
import cn.coder.module.ResponseCode;
import cn.coder.module.ResponseHeader;
import cn.coder.module.ResponseType;
import cn.coder.module.Task;
import cn.coder.user.UserManager;
import cn.coder.util.ProtoStuffUtil;

/**
 * @author zhaoyubo
 * @title LoginMessageHandler
 * @description 登录信息处理器
 * @create 2024/3/11 13:34
 **/
public class LoginMessageHandler extends MessageHandler {

    private UserManager userManager = UserManager.getInstance();

    @Override
    public void handle(Message message, Selector server, SelectionKey client, BlockingQueue<Task> queue,
        AtomicInteger onlineUsers) throws InterruptedException {
        SocketChannel clientChannel = (SocketChannel)client.channel();
        // 获取登录用户名和密码
        MessageHeader header = message.getHeader();
        String username = header.getSender();
        try {
            boolean login = userManager.login(clientChannel, username);
            if (login) {
                byte[] response = ProtoStuffUtil.serialize(new Response(
                    ResponseHeader.builder().type(ResponseType.PROMPT).sender(message.getHeader().getSender())
                        .timestamp(message.getHeader().getTimestamp())
                        .responseCode(ResponseCode.LOGIN_SUCCESS.getCode()).build(),
                    String.format(PromptMsgProperty.LOGIN_SUCCESS, onlineUsers.incrementAndGet())
                        .getBytes(PromptMsgProperty.charset)));
                clientChannel.write(ByteBuffer.wrap(response));
                // 连续发送信息不可行,必须要暂时中断一下
                // 粘包问题
                Thread.sleep(10);
                // 登录提示广播
                byte[] loginBroadcast = ProtoStuffUtil.serialize(new Response(
                    ResponseHeader.builder().type(ResponseType.NORMAL).sender(SYSTEM_SENDER)
                        .timestamp(message.getHeader().getTimestamp()).build(),
                    String.format(PromptMsgProperty.LOGIN_BROADCAST, message.getHeader().getSender())
                        .getBytes(PromptMsgProperty.charset)));
                super.broadcast(loginBroadcast, server);
            } else {
                // 已经登录，造成登陆失败，提示信息
                byte[] loginFail = ProtoStuffUtil.serialize(new Response(
                    ResponseHeader.builder().type(ResponseType.PROMPT).sender(message.getHeader().getSender())
                        .timestamp(message.getHeader().getTimestamp())
                        .responseCode(ResponseCode.LOGIN_FAILURE.getCode()).build(),
                    PromptMsgProperty.LOGIN_FAILURE.getBytes(PromptMsgProperty.charset)));
                // 发送给自己
                clientChannel.write(ByteBuffer.wrap(loginFail));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
