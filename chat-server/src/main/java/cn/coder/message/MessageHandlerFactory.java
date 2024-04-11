package cn.coder.message;

import cn.coder.module.Message;
import cn.coder.module.MessageType;

/**
 * @author zhaoyubo
 * @title MessageHandlerFactory
 * @description 消息处理器工厂
 * @create 2024/3/11 14:23
 **/
public class MessageHandlerFactory {

    public static MessageHandler getMessageHandler(Message message) {
        MessageType type = message.getHeader().getType();
        switch (type) {
            case LOGIN:
                return new LoginMessageHandler();
            case BROADCAST:
                return new BroadcastMessageHandler();
            case NORMAL:
                return new NormalMessageHandler();
            default:
                return null;

        }
    }
}
