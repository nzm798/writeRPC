package com.writeRPC.pkrpc;

import lombok.Data;

/*表示请求*/
@Data
public class Request {
    private ServiceDescriptor service;
    private Object[] parameters;
}
