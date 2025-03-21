package org.myqq.server.services;

import org.myqq.server.Encryptor;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationService extends Service {

    public RegistrationService(int port, Connection connection) {
        super(port, connection);
        this.name = "Registration Service";
    }

    @Override
    protected void handRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // msg的格式：userName + "|" + password + "|" + phoneNumber;
            String msg = in.readLine();
            String[] msgArr = msg.split("\\|");

            // 检查用户名是否已经注册
            boolean result = addUserIfNotExists(msgArr[2], msgArr[0], msgArr[1]);

            // 返回注册结果给客户端
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeBoolean(result);
            out.flush();

            // 关闭资源
            in.close();
            out.close();
            clientSocket.close();

            if (result) {
                System.out.println("用户 " + msgArr[0] + " 注册成功！");
            } else {
                System.out.println("用户 " + msgArr[0] + " 注册失败！");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean addUserIfNotExists(String phoneNumber, String username, String password) {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE phone_number = ?";
        String insertQuery = "INSERT INTO users (phone_number, name, password) VALUES (?, ?, ?)";

        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        try {
            checkStmt = connection.prepareStatement(checkQuery);
            insertStmt = connection.prepareStatement(insertQuery);

            // 1. 检查手机号是否存在
            checkStmt.setString(1, phoneNumber);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // 手机号已存在，返回 false
                return false;
            }

            // 2. 密码加密
            try {
                password = Encryptor.encrypt(password, Encryptor.getTmpKey());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // 2. 插入新用户
            insertStmt.setString(1, phoneNumber);
            insertStmt.setString(2, username);
            insertStmt.setString(3, password);
            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // 关闭资源
            if (checkStmt != null) {
                try {
                    checkStmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (insertStmt != null) {
                try {
                    insertStmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return false;
    }
}
