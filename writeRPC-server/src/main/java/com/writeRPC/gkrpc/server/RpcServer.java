package com.writeRPC.gkrpc.server;

import com.writeRPC.codec.Decoder;
import com.writeRPC.codec.Encoder;
import com.writeRPC.gkrpc.common.utils.ReflectionUtils;
import com.writeRPC.gkrpc.transport.RequestHandler;
import com.writeRPC.gkrpc.transport.TransportServer;
import com.writeRPC.pkrpc.Request;
import com.writeRPC.pkrpc.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 实现服务信息的初始化与服务的注册
 */
@Slf4j
public class RpcServer {
    private RpcServerConfig config; //其下所用到的服务都是从其中获得
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager; //服务管理
    private ServiceInvoker serviceInvoker; //居然方法的调用

    public RpcServer() {
        this.config = new RpcServerConfig();

        //net
        this.net = ReflectionUtils.newInstance(config.getTransportClass()); //初始化实例
        this.net.init(config.getPort(), this.handler); //信息传递模块的初始化

        //codec
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
    }

    public RpcServer(RpcServerConfig config) {
        this.config = config;

        //net
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);

        //codec
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());

        //service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();


    }

    /**
     * 服务的注册
     *
     * @param interfaceClass 注册服务的接口
     * @param bean           服务的具体实现对象
     * @param <T>            泛型
     */
    public <T> void register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean); //调用服务管理模块的注册机制
    }

    /**
     * 服务的开始
     */
    public void start() {
        this.net.start();
    }

    /**
     * 服务结束
     */
    public void stop() {
        this.net.stop();
    }

    /**
     * 重新定义handler，服务处理器。处理其接收和返回值
     */
    private RequestHandler handler = new RequestHandler() {
        public void onRequest(InputStream recive, OutputStream toResp) {
            Response resp = new Response(); //定义一个返回信息
            try {
                byte[] inBytes = IOUtils.readFully(recive, recive.available());//获得revice中所有可以用的数据，available返回recive中可使用的bytes[]
                Request request = decoder.decoder(inBytes, Request.class); //通过反序列化得到信息
                log.info("get request:{}", request);

                ServiceInstance sis = serviceManager.lookup(request); //根据request的请求查找调用方法
                Object ret = serviceInvoker.invoke(sis, request); //调用服务方法
                resp.setData(ret);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                resp.setCode(1); //默认是0所以成功不用赋值
                resp.setMessage("RepcServer got error:" + e.getClass().getName() + " : " + e.getMessage());
            } finally {
                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes); //将返回信息写在返回流中

                    log.info("response client");
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    };
}
