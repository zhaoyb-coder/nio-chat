package cn.coder.message;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import cn.coder.module.Message;
import cn.coder.module.Response;
import cn.coder.module.ResponseHeader;
import cn.coder.module.ResponseType;
import cn.coder.module.Task;
import cn.coder.util.ProtoStuffUtil;

/**
 * @author zhaoyubo
 * @description 群发消息处理器
 * @time 2024/3/18 20:38
 **/
public class BroadcastMessageHandler extends MessageHandler {
    @Override
    public void handle(Message message, Selector server, SelectionKey client, BlockingQueue<Task> queue,
        AtomicInteger onlineUsers) {
        try {
            byte[] response = ProtoStuffUtil.serialize(
                new Response(ResponseHeader.builder().type(ResponseType.NORMAL).sender(message.getHeader().getSender())
                    .timestamp(message.getHeader().getTimestamp()).build(), message.getBody()));
            super.broadcast(response, server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
