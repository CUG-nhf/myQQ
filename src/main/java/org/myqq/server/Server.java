package org.myqq.server;

import org.myqq.server.services.LoginService;
import org.myqq.server.services.RegistrationService;
import org.myqq.server.services.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Server {
    // 服务器注册、登陆、通信端口
    private final int REGISTRATION_PORT;
    private final int LOGIN_PORT;
    private final int CHAT_PORT;

    // 服务连接MySQL数据库端口
    private final int MYSQL_PORT;

    // 数据库连接、数据库名
    private final Connection connection;
    private final String databaseName = "my_qq";

    // 服务器提供的服务
    private final RegistrationService registrationService;
    private final LoginService loginService;


    public static void main(String[] args) {
        new Server();
    }

    /**
     * 读取配置文件
     */
    public Server() {
        Properties properties = new Properties();
        // 1. 读取端口配置
        // TODO：把Server的输出写到日志中
        try {
            System.out.println("服务器端读取端口配置....");
            properties.load(Server.class.getResourceAsStream("/serverConfig"));
            REGISTRATION_PORT = Integer.parseInt(properties.getProperty("registration_port"));
            LOGIN_PORT = Integer.parseInt(properties.getProperty("login_port"));
            CHAT_PORT = Integer.parseInt(properties.getProperty("chat_port"));
            MYSQL_PORT = Integer.parseInt(properties.getProperty("mysql_port"));
        } catch (IOException e) {
            throw new RuntimeException("服务器端读取配置文件失败！");
        }

        // 2. 连接 MySQL 数据库
        try {
            System.out.println("服务器端连接数据库....");
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true".formatted(MYSQL_PORT, databaseName);
            String username = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()); // "服务器端连接数据库失败！"
        }

        // 3. 启动服务
        loginService = new LoginService(LOGIN_PORT, connection);
        loginService.start();

        registrationService = new RegistrationService(REGISTRATION_PORT, connection);
        registrationService.start();
    }
}
