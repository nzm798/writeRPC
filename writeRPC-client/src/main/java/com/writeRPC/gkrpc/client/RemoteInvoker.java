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

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //创建request对象
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        //网络传输
        Response resp = invokeRemote(request);
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote: " + resp);
        }
        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        Response resp = null;
        TransportClient client = null;
        try {
            client = selector.select();

            byte[] outBytes = encoder.encode(request);
            InputStream revice = client.write(new ByteArrayInputStream(outBytes));

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
