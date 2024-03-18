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
import cn.coder.module.ResponseHeader;
import cn.coder.module.ResponseType;
import cn.coder.module.Task;
import cn.coder.user.UserManager;
import cn.coder.util.ProtoStuffUtil;

/**
 * Created by SinjinSong on 2017/5/23.
 */
public class NormalMessageHandler extends MessageHandler {

    @Override
    public void handle(Message message, Selector server, SelectionKey client, BlockingQueue<Task> queue,
        AtomicInteger onlineUsers) {
        try {
            SocketChannel clientChannel = (SocketChannel)client.channel();
            MessageHeader header = message.getHeader();
            SocketChannel receiverChannel = UserManager.getInstance().getUserChannel(header.getReceiver());
            if (receiverChannel == null) {
                // 接收者下线
                byte[] response = ProtoStuffUtil.serialize(new Response(
                    ResponseHeader.builder().type(ResponseType.PROMPT).sender(message.getHeader().getSender())
                        .timestamp(message.getHeader().getTimestamp()).build(),
                    PromptMsgProperty.RECEIVER_LOGGED_OFF.getBytes(PromptMsgProperty.charset)));
                clientChannel.write(ByteBuffer.wrap(response));
            } else {
                byte[] response =
                    ProtoStuffUtil.serialize(new Response(ResponseHeader.builder().type(ResponseType.NORMAL)
                        .sender(message.getHeader().getSender()).timestamp(message.getHeader().getTimestamp()).build(),
                        message.getBody()));
                receiverChannel.write(ByteBuffer.wrap(response));
                // 也给自己发送一份
                clientChannel.write(ByteBuffer.wrap(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
