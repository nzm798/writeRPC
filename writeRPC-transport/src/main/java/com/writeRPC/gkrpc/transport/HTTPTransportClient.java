package com.writeRPC.gkrpc.transport;

import com.writeRPC.pkrpc.Peer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 客户端数据请求和接收
 */
public class HTTPTransportClient implements TransportClient {
    private String url;

    public void connect(Peer peer) {
        this.url = "http://" + peer.getHost() + ":" + peer.getPort();

    }

    /**
     * 发送请求信息，收到回复的输出流
     *
     * @param data
     * @return
     */
    public InputStream write(InputStream data) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");

            httpConn.connect();
            IOUtils.copy(data, httpConn.getOutputStream()); //将要请求的信息放在了输出流中，发送给服务端

            int resultCode = httpConn.getResponseCode(); //获得返回的信息
            if (resultCode == HttpURLConnection.HTTP_OK) {
                return httpConn.getInputStream();
            } else {
                return httpConn.getErrorStream();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {

    }
}
