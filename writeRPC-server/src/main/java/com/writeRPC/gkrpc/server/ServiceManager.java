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
    //存储服务描述与服务的实例
    private Map<ServiceDescriptor,ServiceInstance> service;
    public ServiceManager(){
        this.service=new ConcurrentHashMap<ServiceDescriptor, ServiceInstance>();//线程安全的HashMap实现原理与HashMap相同
    }

    /**
     * 服务的注册
     * @param interfaceClass 接口类
     * @param bean 实例
     * @param <T>
     */
    public <T> void register(Class<T> interfaceClass,T bean){ //最一开始的<T>是为了定义一个泛型
       Method[] methods=ReflectionUtils.getPublicMethods(interfaceClass);//获取类的所有方法
       for (Method method:methods){
            ServiceInstance sis=new ServiceInstance(bean,method);
            ServiceDescriptor sdp=ServiceDescriptor.from(interfaceClass,method);

            service.put(sdp,sis);
            log.info("register service:{} {}",sdp.getClazz(),sdp.getMethod());
       }
    }

    /**
     * 服务的查找，根据request中的ServiceDescriptor信息查找
     * @param request 收到的客户端的请求
     * @return 返回具体的实例ServiceInstance
     */
    public ServiceInstance lookup(Request request){
        ServiceDescriptor sdp=request.getService();
        return  service.get(sdp); //判断查找需要equal方法，所有要在ServiceDescriptor加上equal方法
    }

}
