package com.writeRPC.gkrpc.server;

import com.writeRPC.gkrpc.common.utils.ReflectionUtils;
import com.writeRPC.pkrpc.Request;

/**
 * 调用具体服务
 */
public class ServiceInvoker {
    /**
     * 服务的调用方法
     * @param service 所要调用的服务
     * @param request 请求，包含要调用的方法以及参数
     * @return 返回调用的方法返回值
     */
    public Object invoke(ServiceInstance service, Request request){
        return ReflectionUtils.invoke(service.getTarget(),service.getMethod(),request.getParameters());
    }
}
