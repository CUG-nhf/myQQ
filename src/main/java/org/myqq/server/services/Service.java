package org.myqq.server.services;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class Service extends Thread{

    protected String name;

    /**
     * 要监听的端口号
     */
    protected final int port;

    /**
     * 注册服务是否运行
     */
    protected Boolean isRunning;

    protected final Connection connection;

    /**
     * 构造方法
     * @param port 监听的端口号
     */
    public Service(int port, Connection connection) {
        super();
        this.port = port;
        this.connection = connection;
        this.isRunning = true;
    }

    /**
     * 停止服务
     */
    public void shutdown(Boolean isRunning) {
        this.isRunning = false;
    }

    /**
     * 开始监听
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.printf("%s@服务器 监听端口 %d ...%n", name, port);

            while (isRunning) {
                // 接收客户端连接
                Socket clientSocket = serverSocket.accept();

                // 使用线程池处理注册请求（避免阻塞主线程）
                new Thread(() -> handRequest(clientSocket)).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void handRequest(Socket clientSocket) {

    }
}
