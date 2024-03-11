package cn.coder.module;

/**
 * @author zhaoyubo
 * @title Message
 * @description <TODO description class purpose>
 * @create 2024/3/8 14:56
 **/
public class Message {
    private MessageHeader header;
    private byte[] body;

    public Message(MessageHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
