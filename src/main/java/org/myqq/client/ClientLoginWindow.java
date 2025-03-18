package org.myqq.client;

import org.myqq.common.User;

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class ClientLoginWindow {
    // 服务器的 IP 和端口号
    private static String SERVER_IP;
    private static int SERVER_LOGIN_PORT;
    private static int SERVER_REGISTRATION_PORT;
    private static int SERVER_CHAT_PORT;

    public static void main(String[] args) {
        ClientLoginWindow client = new ClientLoginWindow();
        client.start();
    }

    /**
     * 读取配置文件，获取服务器的IP和端口号
     */
    public ClientLoginWindow() {
        Properties properties = new Properties();
        try {
            properties.load(ClientLoginWindow.class.getClassLoader().getResourceAsStream("serverConfig"));
            SERVER_IP = properties.getProperty("ip");
            SERVER_LOGIN_PORT = Integer.parseInt(properties.getProperty("login_port"));
            SERVER_REGISTRATION_PORT = Integer.parseInt(properties.getProperty("registration_port"));
            SERVER_CHAT_PORT = Integer.parseInt(properties.getProperty("chat_port"));
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
                    if (login()) {
                        return;
                    } else {
                        break;
                    }
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
        new ClientRegistrationWindow(SERVER_IP, SERVER_REGISTRATION_PORT).registration();
    }

    /**
     * 用户登陆：打开登陆界面
     * 输入手机号和密码，客户端进行初步的格式验证，然后发给服务器进一步验证并登陆
     */
    private boolean login() {
        while (true) {
            // 1. 首先输入手机号
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入您的手机号（-1表示退出输入）：");
            String phoneNumber = scanner.next();
            if (phoneNumber.equals("-1")) {
                return false;
            }

            if (utils.isPhoneNumber(phoneNumber)) {
               // 2. 输入密码
                System.out.println("请输入您的密码（-1表示退出输入）：");
                String password = utils.readPassword();
                if (password.equals("-1")) {
                    return false;
                }

                // 3. 验证密码
                User result = null;
                if ((result = verifyPassword(password, phoneNumber)) != null) {
                    System.out.println("验证通过");
                    new ClientMainWindow(SERVER_IP, SERVER_CHAT_PORT, result).start();
                    return true;
                }

            } else {
                System.out.println("手机号格式有误，请重新输入！");
            }
        }
    }

    /**
     *  客户端连接服务器登陆端口，发送密码和手机号，验证密码是否正确
     * @param password 密码
     * @param phoneNumber 手机号
     * @return 验证通过返回true，否则返回false
     */
    private User verifyPassword(String password, String phoneNumber) {
        User result = null;
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_LOGIN_PORT);

            // 密码、手机号拼接发送给服务器
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg = password + "|" + phoneNumber;
            out.write(msg);
            out.newLine();
            out.flush();

            // 接收服务器返回的消息, 如果返回User对象，则验证通过，如果返回NULL则验证失败
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            result = (User) in.readObject();

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
