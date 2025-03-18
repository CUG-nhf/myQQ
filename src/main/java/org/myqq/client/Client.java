package org.myqq.client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private static String SERVER_IP;
    private static int SERVER_LOGIN_PORT;
    private static int SERVER_REGISTRATION_PORT;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    /**
     * 读取配置文件，获取服务器的IP和端口号
     */
    public Client() {
        Properties properties = new Properties();
        try {
            properties.load(Client.class.getClassLoader().getResourceAsStream("serverConfig"));
            SERVER_IP = properties.getProperty("ip");
            SERVER_LOGIN_PORT = Integer.parseInt(properties.getProperty("login_port"));
            SERVER_REGISTRATION_PORT = Integer.parseInt(properties.getProperty("registration_port"));
        } catch (Exception e) {
            throw new RuntimeException("客服端读取配置文件失败！");
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("========== myQQ 客户端登陆界面 ============");
            System.out.println("           1. 登陆");
            System.out.println("           2. 注册");
            System.out.println("           3. 退出");
            System.out.println("请输入您的选择：");
            switch (scanner.nextInt()) {
                case 1:
                    login();
                    break;
                case 2:
                   register();
                    break;
                case 3:
                    exit();
                    break;
                default:
                    System.out.println("输入有误，请重新输入！");
                    break;
            }
        }
    }

    private void exit() {
        System.exit(0);
    }

    /**
     * 用户注册：打开注册界面
     * 输入昵称、密码和手机号，客户端进行初步的格式验证，然后发给服务器进一步验证并注册
     */
    private void register() {
        new Register(SERVER_IP, SERVER_REGISTRATION_PORT).registration();
    }

    private void login() {
        while (true) {
            // 1. 首先输入手机号
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入您的手机号（-1表示退出输入）：");
            String phoneNumber = scanner.next();
            if (phoneNumber.equals("-1")) {
                return;
            }

            if (utils.isPhoneNumber(phoneNumber)) {
               // 2. 输入密码
                System.out.println("请输入您的密码（-1表示退出输入）：");
                String password = utils.readPassword();
                if (password.equals("-1")) {
                    return;
                }

                // 3. 验证密码
                if (verifyPassword(password, phoneNumber)) {
                    System.out.println("验证通过");
                    return;
                    // TODO: 用户主界面
//                    new ClientMainWindow(SERVER_IP, SERVER_LOGIN_PORT).login(phoneNumber, password);
                }

            } else {
                System.out.println("手机号格式有误，请重新输入！");
            }
        }
    }

    private boolean verifyPassword(String password, String phoneNumber) {
        boolean result = false;
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_LOGIN_PORT);

            // 密码、手机号拼接发送给服务器
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg = password + "|" + phoneNumber;
            out.write(msg);
            out.newLine();
            out.flush();

            // 接收服务器返回的消息
            DataInputStream in = new DataInputStream(socket.getInputStream());
            result = in.readBoolean();

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
