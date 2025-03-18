package org.myqq.server.services;

import org.myqq.server.Encryptor;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginService extends Service{

    private final ArrayList<LoginUser> login_Users = new ArrayList<>();

    public LoginService(int port, Connection connection) {
        super(port, connection);
        this.name = "Login Service";
    }

    public ArrayList<LoginUser> getLogin_Users() {
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
            boolean result = verifyPW(msgArr[1], msgArr[0]);

            if (result) {
                // 补全 IP
                login_Users.get(login_Users.size() - 1).user_IP = clientSocket.getInetAddress().getHostAddress();
                System.out.println("用户 " + login_Users.get(login_Users.size() - 1).user_name + " 登录成功！");
            } else {
                System.out.println("用户 " + msgArr[1] + " 登录失败！");
            }

            // 返回注册结果给客户端
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeBoolean(result);
            out.flush();

            // 关闭资源
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean verifyPW(String phoneNumber, String password) {
        String selectQuery = "SELECT * FROM users WHERE phone_number = ?";
        PreparedStatement selectStmt = null;
        try {
            selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, phoneNumber);
            ResultSet rs = selectStmt.executeQuery();
            // 不存在phoneNumber，则返回false
            if (!rs.next()) {
                return false;
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
                // 登录成功，则将用户信息添加到login_Users中
                login_Users.add(new LoginUser(phoneNumber, rs.getString("name")));
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static class LoginUser {
        public String user_phone;
        public String user_name;
        public String user_IP;
        public LoginUser(String phone, String name, String ip) {
            user_IP = ip;
            user_phone = phone;
            user_name = name;
        }

        public LoginUser(String phone, String name) {
            user_IP = "";
            user_phone = phone;
            user_name = name;
        }
    }
}
