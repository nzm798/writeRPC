package com.writeRPC.gkrpc.server;

import com.writeRPC.gkrpc.common.utils.ReflectionUtils;
import com.writeRPC.pkrpc.Request;

/**
 * 调用具体服务
 */
public class ServiceInvoker {
    public Object invoke(ServiceInstance service, Request request){
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),request.getParameters());
    }
}
