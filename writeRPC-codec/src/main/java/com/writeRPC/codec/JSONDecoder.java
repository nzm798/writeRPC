package com.writeRPC.codec;

import com.alibaba.fastjson.JSON;

public class JSONDecoder implements Decoder{
    public <T> T decoder(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes,clazz);
    }
}
