package com.writeRPC.gkrpc.client;

import com.writeRPC.gkrpc.common.utils.ReflectionUtils;
import com.writeRPC.gkrpc.transport.TransportClient;
import com.writeRPC.pkrpc.Peer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 实现服务传送的选择接口
 */
@Slf4j
public class RandomTransportSelector implements TransportSelector{
    /**
     * 连接好的client
     */
    private List<TransportClient> clients;

    public RandomTransportSelector() {
        clients=new ArrayList<TransportClient>(); //初始化连接
    }

    public synchronized void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
        count= Math.max(count,1);

        for (Peer peer:peers){
            for (int i=0;i<count;i++){
                TransportClient client= ReflectionUtils.newInstance(clazz);
                client.connect(peer);
                clients.add(client);
            }
            log.info("connect server:{}",peer);
        }
    }

    /**
     * 随机选择服务连接
     * @return 选择一个TransportCLient连接服务
     */
    public synchronized TransportClient select() {
        int i=new Random().nextInt(clients.size());
        return clients.remove(i);//在client池中找到一个client
    }

    /**
     * 释放不使用的连接
     * @param client
     */
    public synchronized void release(TransportClient client) {
        clients.add(client);
    }

    /**
     * 当关闭客户端服务
     * 关闭所有client连接
     */
    public synchronized void close() {
        for(TransportClient client:clients){
            client.close();
        }
        clients.clear();
    }
}
