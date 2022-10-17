package com.writeRPC.gkrpc.server;

import com.writeRPC.gkrpc.common.utils.ReflectionUtils;
import com.writeRPC.pkrpc.Request;
import com.writeRPC.pkrpc.ServiceDescriptor;
import junit.framework.TestCase;

import java.lang.reflect.Method;

public class ServiceManagerTest extends TestCase {
    ServiceManager sm =new ServiceManager();
    public ServiceManagerTest(){
        TestInterface bean=new TestClass();
        sm.register(TestInterface.class,bean);
    }


    public void testRegister() {
        TestInterface bean=new TestClass();
        sm.register(TestInterface.class,bean);
    }

    public void testLookup() {
        Method method=ReflectionUtils.getPublicMethods(TestInterface.class)[0];
        ServiceDescriptor sdp=ServiceDescriptor.from(TestInterface.class,method);
        Request request=new Request();
        request.setService(sdp);

        ServiceInstance sis = sm.lookup(request);
        assertNotNull(sis);

    }
}