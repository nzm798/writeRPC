package com.writeRPC.codec;

/**
 * 反序列化
 */
public interface Decoder {
    <T> T decoder(byte[] bytes,Class<T> clazz);
}
