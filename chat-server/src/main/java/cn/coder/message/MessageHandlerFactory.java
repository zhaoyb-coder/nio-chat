package cn.coder.message;

import cn.coder.module.Message;
import cn.coder.module.MessageType;

/**
 * @author zhaoyubo
 * @title MessageHandlerFactory
 * @description <TODO description class purpose>
 * @create 2024/3/11 14:23
 **/
public class MessageHandlerFactory {

    public static MessageHandler getMessageHandler(Message message){
        MessageType type = message.getHeader().getType();
        switch(type) {
            case LOGIN:
                return new LoginMessageHandler();
            default:
                return null;

        }
    }
}
