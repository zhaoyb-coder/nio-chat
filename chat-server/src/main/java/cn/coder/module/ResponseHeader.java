package cn.coder.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaoyubo
 * @title ResponseHeader
 * @description <TODO description class purpose>
 * @create 2024/3/11 14:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseHeader {
    private String sender;
    private ResponseType type;
    private Integer responseCode;
    private Long timestamp;
}
