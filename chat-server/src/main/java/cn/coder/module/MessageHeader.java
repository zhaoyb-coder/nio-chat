package cn.coder.module;

/**
 * @author zhaoyubo
 * @title MessageHeader
 * @description <TODO description class purpose>
 * @create 2024/3/8 14:56
 **/
public class MessageHeader {
    private String sender;
    private String receiver;
    private MessageType type;
    private Long timestamp;

    public MessageHeader(String sender, String receiver, MessageType type, Long timestamp)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.timestamp = timestamp;
    }

    public MessageHeader(String sender, String receiver, MessageType type)
    {
        this(sender, receiver, type, System.currentTimeMillis());
    }

    public MessageHeader(String sender, String receiver)
    {
        this(sender, receiver, MessageType.NORMAL);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
