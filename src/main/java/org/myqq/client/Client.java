package org.myqq.client;

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
    }

    private void connectServer() {
        System.out.println("连接服务器进行验证");
    }

}
