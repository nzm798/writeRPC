package com.writeRPC.gkrpc.client;

import com.writeRPC.codec.Decoder;
import com.writeRPC.codec.Encoder;
import com.writeRPC.gkrpc.transport.TransportClient;
import com.writeRPC.pkrpc.Request;
import com.writeRPC.pkrpc.Response;
import com.writeRPC.pkrpc.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用远程服务的代理类
 */
@Slf4j
public class RemoteInvoker implements InvocationHandler {

    Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder, TransportSelector selector) {

        this.clazz = clazz;
        this.decoder = decoder;
        this.encoder = encoder;
        this.selector = selector;

    }

    /**
     * 方法调用
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //创建request对象，为了发送请求信息
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method)); //将类型与方法转换成服务描述ServiceDescriptor
        request.setParameters(args);

        //网络传输
        Response resp = invokeRemote(request);
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote: " + resp);
        }
        return resp.getData();
    }

    /**
     * 传送请求调用远程方法，得到返回值
     * @param request 请求方法
     * @return 返回Response
     */
    private Response invokeRemote(Request request) {
        Response resp = null;
        TransportClient client = null; //选择服务提供
        try {
            client = selector.select();

            byte[] outBytes = encoder.encode(request); //编码请求信息
            InputStream revice = client.write(new ByteArrayInputStream(outBytes)); //ByteArrayInputStream创建字节输入流对象

            //获得服务端返回的输出流
            byte[] inBytes=IOUtils.readFully(revice, revice.available());
            resp = decoder.decoder(inBytes, Response.class);
        } catch (IOException e) {
            log.warn(e.getMessage(),e);
            resp.setCode(1);
            resp.setMessage("RpcClient got errot: "+e.getClass()+" : "+e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }
        return resp;
    }
}
