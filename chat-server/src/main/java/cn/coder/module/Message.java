package cn.coder.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaoyubo
 * @title Message
 * @description <TODO description class purpose>
 * @create 2024/3/8 14:56
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private MessageHeader header;
    private byte[] body;
}
