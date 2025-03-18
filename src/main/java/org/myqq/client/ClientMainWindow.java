package org.myqq.client;

import org.myqq.common.User;

import java.util.Optional;
import java.util.Scanner;

public class ClientMainWindow {
    private String server_ip;
    private int server_chat_port;
    User user;

    public ClientMainWindow(String ip, int port, User user) {
        this.server_ip = ip;
        this.server_chat_port = port;
        this.user = user;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("TODO 私聊");
                    break;
                case 2:
                    System.out.println("TODO 群聊");
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
        }
    }

    private void printMenu(){
        System.out.println("========= MYQQ =========");
        System.out.println("1. 私聊");
        System.out.println("2. 群聊");
        System.out.println("3. 退出");
        System.out.println("请输入您的选择：");
    }
}
