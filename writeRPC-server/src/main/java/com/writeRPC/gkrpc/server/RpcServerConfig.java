package com.writeRPC.gkrpc.server;

import com.writeRPC.codec.Decoder;
import com.writeRPC.codec.Encoder;
import com.writeRPC.codec.JSONDecoder;
import com.writeRPC.codec.JSONEncoder;
import com.writeRPC.gkrpc.transport.HttpTransportServer;
import com.writeRPC.gkrpc.transport.TransportServer;
import lombok.Data;

/**
 * RPC服务端所需要的配置信息
 */
@Data
public class RpcServerConfig {
    //信息传递所需要的发送服务
    private Class<? extends TransportServer> transportClass= HttpTransportServer.class;
    //所用的编码器
    private Class<? extends Encoder> encoderClass= JSONEncoder.class;
    //所用的解码器
    private Class<? extends Decoder> decoderClass= JSONDecoder.class;
    //提供服务的端口号
    private int port=3000;

}
