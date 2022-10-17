package com.writeRPC.codec;

import junit.framework.TestCase;

import java.util.Arrays;

public class JSONDecoderTest extends TestCase {

    public void testDecoder() {
        Encoder encoder=new JSONEncoder();

        TestBean bean=new TestBean();
        bean.setAge(18);
        bean.setName("nzm");

        byte[] bytes= encoder.encode(bean);

        Decoder decoder=new JSONDecoder();
        TestBean bean2=decoder.decoder(bytes,TestBean.class);
        assertEquals(bean.getName(),bean2.getName());
    }
}