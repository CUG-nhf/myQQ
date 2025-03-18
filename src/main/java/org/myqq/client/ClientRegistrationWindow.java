package org.myqq.client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientRegistrationWindow {
    /**
     * 服务器 IP
     */
    private final String server_ip;

    /**
     * 服务器端口
     */
    private final int port;

    /**
     * 注册时用户输入的用户名
     */
    String userName;

    /**
     * 注册时用户输入的密码
     */
    String password;

    /**
     * 注册时用户输入的手机号
     */
    String phoneNumber;

    /**
     * 构造方法
     *
     * @param ip   服务器 IP
     * @param port 服务器端口
     */
    public ClientRegistrationWindow(String ip, int port) {
        this.server_ip = ip;
        this.port = port;
    }

    /**
     * 注册时必须调用这个函数
     */
    public void registration() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========== 欢迎注册 MyQQ ============");

        // 1. 输入昵称
        do {
            System.out.println("请输入您的昵称（-1表示退出注册界面）：");
            userName = scanner.next();
            if (userName.equals("-1")) {
                return;
            }
        } while (!verifyUserName(userName));

        // 2.输入密码
        while (true) {
            System.out.println("请输入您的密码（-1表示退出注册界面）：");
            password = utils.readPassword();
            if (password.equals("-1")) {
                return;
            }

            if (verifyPassword(password)) {
                System.out.println("请确认密码：");
                String confirmPassword = utils.readPassword();
                if (!confirmPassword.equals(password)) {
                    System.out.println("两次密码不一致，请重新输入！");
                } else {
                    break;
                }
            }
        }

        // 3.输入手机号
        while (true) {
            System.out.println("请输入您的手机号（-1表示退出注册界面）：");
            phoneNumber = scanner.next();
            if (phoneNumber.equals("-1")) {
                return;
            }

            if (verifyPhoneNumber(phoneNumber)) {
                System.out.println("注册成功！");
                return;
            }
        }

    }

    /**
     * 验证昵称
     */
    private boolean verifyUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            System.out.println("昵称不能为空，请重新输入！");
            return false;
        }
        return true;
    }

    /**
     * 验证密码
     * 1. 长度在8-16个字符
     * 2. 不能包含空格
     * 3. 必须包含字母、数字、特殊字符中的两种
     * 4. 请勿输入连续、重复6位以上字母或数字，如 123456、111111、abcdef
     */
    boolean verifyPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < 8 || password.length() > 16) {
            System.out.println("密码长度必须在8-16个字符之间！");
            return false;
        }
        if (password.contains(" ")) {
            System.out.println("密码不能包含空格！");
            return false;
        }

        if (!utils.isCombination(password)) {
            System.out.println("密码必须包含字母、数字、特殊字符中至少两种！");
            return false;
        }

        for (int i = 0; i < password.length() - 5; i++) {
            String substring = password.substring(i, i + 6);
            if (utils.isContinuous(substring) || utils.isSingleChar(substring)) {
                System.out.println("密码不能包含连续、重复的6位以上字母或数字，如 abcdefg、111111、012345");
                return false;
            }
        }

        return true;
    }

    /**
     * 验证手机号
     */
    private boolean verifyPhoneNumber(String phoneNumber) {
        if (utils.isPhoneNumber(phoneNumber)) {
            return connectServer();
        } else {
            System.out.println("手机号格式不正确！");
            return false;
        }
    }

    /**
     * 与服务器建立连接，进行账号注册
     */
    private boolean connectServer() {
        boolean result = false;
        try {
            Socket socket = new Socket(server_ip, port);

            // 昵称、密码、手机号拼接发送给服务器
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg = userName + "|" + password + "|" + phoneNumber;
            out.write(msg);
            out.newLine();
            out.flush();

            // 接收服务器返回的消息
            DataInputStream in = new DataInputStream(socket.getInputStream());
            result = in.readBoolean();
            // TODO: 加入验证码验证

            // 关闭资源
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}