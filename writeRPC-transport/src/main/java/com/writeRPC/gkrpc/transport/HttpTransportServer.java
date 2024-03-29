package com.writeRPC.gkrpc.transport;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 服务端的网络传递模块
 */
@Slf4j
public class HttpTransportServer implements TransportServer{
    private RequestHandler handler;
    private Server server; //服务端的http服务
    public void init(int port, RequestHandler handler) {
        this.handler=handler;
        this.server=new Server(port); //赋值端口

        //servlet接受请求
        ServletContextHandler ctx=new ServletContextHandler();
        server.setHandler(ctx);

        ServletHolder holder=new ServletHolder(new RequestServlet());
        ctx.addServlet(holder,"/*");

    }

    public void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    class RequestServlet extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            log.info("client connect");
            InputStream in = req.getInputStream();
            OutputStream out= resp.getOutputStream();

            if (handler!=null){
                handler.onRequest(in,out);
            }
            out.flush(); //用于清空缓存中的数据流
        }
    }
}
