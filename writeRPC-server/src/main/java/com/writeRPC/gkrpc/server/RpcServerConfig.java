package com.writeRPC.gkrpc.server;

import com.writeRPC.codec.Decoder;
import com.writeRPC.codec.Encoder;
import com.writeRPC.codec.JSONDecoder;
import com.writeRPC.codec.JSONEncoder;
import com.writeRPC.gkrpc.transport.HttpTransportServer;
import com.writeRPC.gkrpc.transport.TransportServer;
import lombok.Data;

@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass= HttpTransportServer.class;
    private Class<? extends Encoder> encoderClass= JSONEncoder.class;
    private Class<? extends Decoder> decoderClass= JSONDecoder.class;
    private int port=3000;

}
