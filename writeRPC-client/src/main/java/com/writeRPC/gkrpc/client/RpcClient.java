package com.writeRPC.gkrpc.client;

import com.writeRPC.codec.Decoder;
import com.writeRPC.codec.Encoder;
import com.writeRPC.gkrpc.common.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

/**
 * RPCClient服务信息，通过其获得方法的远程动态代理
 */
public class RpcClient {
    private RpcClientConfig config;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RpcClient(RpcClientConfig config) {
        this.config = config;
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.selector=ReflectionUtils.newInstance(config.getSelectorClass());

        //初始化服务选择器
        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass()
        );
    }

    public RpcClient() {
        this.config=new RpcClientConfig();
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.selector=ReflectionUtils.newInstance(config.getSelectorClass());

        this.selector.init(
                this.config.getServers(),
                this.config.getConnectCount(),
                this.config.getTransportClass()
        );
    }

    /**
     * 获取接口的代理对象，动态代理
     * @param clazz 需要代理的接口
     * @return 返回代理对象
     * @param <T>
     */
    public <T> T getProxy(Class<T> clazz){
        /**
         * loader：用哪个类加载器去加载代理对象
         * new Class[]{clazz}: 动态代理需要实现的接口
         * new RemoteInvoker(): 动态方法在执行时会调用invoker方法
         */
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(), //获取类对象的加载器
                new Class[]{clazz},
                new RemoteInvoker(clazz,encoder,decoder,selector)
        );
    }
}
