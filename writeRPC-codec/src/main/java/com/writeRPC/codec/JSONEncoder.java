package com.writeRPC.codec;
import com.alibaba.fastjson.JSON;

/**
 * 基于json的序列化实现
 */
public class JSONEncoder implements Encoder{
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);//把对象转换成byte反序列化数组
    }
}
