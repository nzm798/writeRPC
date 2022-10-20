package com.writeRPC.codec;

import com.alibaba.fastjson.JSON;

/**
 * 反序列化信息，使用JSON中的序列和反序列化信息
 */
public class JSONDecoder implements Decoder{
    public <T> T decoder(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
