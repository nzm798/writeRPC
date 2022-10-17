package com.writeRPC.codec;

import junit.framework.TestCase;

import java.util.Arrays;

public class JSONEncoderTest extends TestCase {

    public void testEncode() {
        Encoder encoder=new JSONEncoder();

        TestBean bean=new TestBean();
        bean.setAge(18);
        bean.setName("nzm");

        byte[] bytes= encoder.encode(bean);
        System.out.println(Arrays.toString(bytes));
        assertNotNull(bytes);
    }
}