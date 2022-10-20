package com.writeRPC.pkrpc;
/*
* 表示网络传输的一个端点
* */

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务提供者地址信息
 */
@Data
@AllArgsConstructor
public class Peer {
    private String host;
    private int port;
}
