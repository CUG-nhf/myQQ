package org.myqq.server.services;

import java.sql.Connection;

public abstract class Service{

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
    public abstract void startListen();
}
