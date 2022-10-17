package com.writeRPC.gkrpc.client;

import com.writeRPC.codec.Decoder;
import com.writeRPC.codec.Encoder;
import com.writeRPC.codec.JSONDecoder;
import com.writeRPC.codec.JSONEncoder;
import com.writeRPC.gkrpc.transport.HTTPTransportClient;
import com.writeRPC.gkrpc.transport.TransportClient;
import com.writeRPC.pkrpc.Peer;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass= HTTPTransportClient.class;
    private Class<? extends Encoder> encoderClass= JSONEncoder.class;
    private Class<? extends Decoder> decoderClass= JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass=RandomTransportSelector.class;
    private int connectCount=1;//severpeer需要多少连接
    private List<Peer> servers= Arrays.asList(
            new Peer("127.0.0.1",3000)
    );
}
