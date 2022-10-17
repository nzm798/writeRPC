package com.writeRPC.gkrpc.example;

import com.writeRPC.gkrpc.server.RpcServer;
import com.writeRPC.gkrpc.server.RpcServerConfig;

public class Server {
    public static void main(String[] args) {
        RpcServer server=new RpcServer();
        server.register(CalcService.class,new CalcServiceImpl());
        server.start();
    }
}
