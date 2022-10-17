package com.writeRPC.gkrpc.server;

import com.writeRPC.gkrpc.common.utils.ReflectionUtils;
import com.writeRPC.pkrpc.Request;
import com.writeRPC.pkrpc.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 暴露rpc暴露的服务
 * 1.注册服务
 * 2.查找服务
 */
@Slf4j
public class ServiceManager {
    private Map<ServiceDescriptor,ServiceInstance> service;
    public ServiceManager(){
        this.service=new ConcurrentHashMap<ServiceDescriptor, ServiceInstance>();
    }

    /**
     * 服务的注册
     * @param interfaceClass 接口类
     * @param bean 实例
     * @param <T>
     */
    public <T> void register(Class<T> interfaceClass,T bean){
       Method[] methods=ReflectionUtils.getPublicMethods(interfaceClass);//获取类的所有方法
       for (Method method:methods){
            ServiceInstance sis=new ServiceInstance(bean,method);
            ServiceDescriptor sdp=ServiceDescriptor.from(interfaceClass,method);

            service.put(sdp,sis);
            log.info("register service:{} {}",sdp.getClazz(),sdp.getMethod());
       }
    }

    public ServiceInstance lookup(Request request){
        ServiceDescriptor sdp=request.getService();
        return  service.get(sdp); //判断查找需要equal方法，所有要在ServiceDescriptor加上equal方法
    }

}
