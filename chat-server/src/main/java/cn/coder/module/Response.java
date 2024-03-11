package cn.coder.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaoyubo
 * @title Response
 * @description <TODO description class purpose>
 * @create 2024/3/11 14:08
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private ResponseHeader header;
    private byte[] body;
}
