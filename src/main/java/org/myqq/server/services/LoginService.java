package org.myqq.server.services;

import org.myqq.common.User;
import org.myqq.server.Encryptor;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LoginService extends Service{

    private final HashMap<String, User> login_Users = new HashMap<>();

    public LoginService(int port, Connection connection) {
        super(port, connection);
        this.name = "Login Service";
    }

    public HashMap<String, User> getLogin_Users() {
        return login_Users;
    }

    @Override
    protected void handRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // msg的格式：password + "|" + phoneNumber;
            String msg = in.readLine();
            String[] msgArr = msg.split("\\|");

            // 校验密码
            User result = verifyPW(msgArr[1], msgArr[0]);

            if (result != null) {
                // 补全user信息，添加到在线用户列表
                result.setClient_IP(clientSocket.getInetAddress().getHostAddress());
                result.setClient_port(clientSocket.getPort());
                login_Users.put(result.getPhone(), result);
                System.out.println("用户 " + result.getName() + " 登录成功！");
            } else {
                System.out.println("账号 " + msgArr[1] + " 登录失败！");
            }

            // 返回注册结果给客户端
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(result);
            out.flush();

            // 关闭资源
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 验证密码
     * @param phoneNumber 手机号
     * @param password 密码
     * @return 如果返回User对象，则登录成功，否则登录失败
     */
    private User verifyPW(String phoneNumber, String password) {
        String selectQuery = "SELECT * FROM users WHERE phone_number = ?";
        PreparedStatement selectStmt = null;
        try {
            selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, phoneNumber);
            ResultSet rs = selectStmt.executeQuery();
            // 不存在phoneNumber，则返回false
            if (!rs.next()) {
                return null;
            }
            // 存在phoneNumber，则验证密码
            String encrypted_pw = rs.getString("password");
            String decrypted_pw = "";
            try {
                decrypted_pw = Encryptor.decrypt(encrypted_pw, Encryptor.getTmpKey());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (decrypted_pw.equals(password)) {
                // 登录成功，则返回User对象
                return new User(rs.getString("name"), phoneNumber);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
