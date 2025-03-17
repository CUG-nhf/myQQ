package org.myqq.server;

import org.myqq.server.services.RegistrationService;
import org.myqq.server.services.Service;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Server {
    /**
     * 注册端口
     */
    private static int REGISTRATION_PORT;

    /**
     * 登录端口
     */
    private static int LOGIN_PORT;

    /**
     * 数据库端口
     */
    private static int MYSQL_PORT;

    /**
     * 数据库连接
     */
    private static Connection connection;
    private static final String databaseName = "myqq";


    public static void main(String[] args) {
        new Server().start();
    }

    /**
     * 读取配置文件
     */
    public Server() {
        Properties properties = new Properties();
        // 1. 读取端口配置
        try {
            System.out.println("服务器端读取端口配置....");
            properties.load(Server.class.getResourceAsStream("/serverConfig"));
            REGISTRATION_PORT = Integer.parseInt(properties.getProperty("registration_port"));
            LOGIN_PORT = Integer.parseInt(properties.getProperty("login_port"));
            MYSQL_PORT = Integer.parseInt(properties.getProperty("mysql_port"));
        } catch (IOException e) {
            throw new RuntimeException("服务器端读取配置文件失败！");
        }

        // 2. 连接 MySQL 数据库
        try {
            System.out.println("服务器端连接数据库....");
            String url = "jdbc:mysql://localhost:%d/%s?useSSL=false&serverTimezone=UTC".formatted(MYSQL_PORT, databaseName);
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("服务器端连接数据库失败！");
        }

    }

    public static void start() {
        Service registrationService = new RegistrationService(REGISTRATION_PORT, connection);
        registrationService.startListen();
    }
}
