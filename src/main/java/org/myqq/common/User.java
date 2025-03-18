package org.myqq.common;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String client_IP;
    private int client_port;

    private Socket client_socket;

    public User(String name, String phone, String client_IP, int client_port) {
        this.name = name;
        this.phone = phone;
        this.client_IP = client_IP;
        this.client_port = client_port;
        this.client_socket = null;
    }

    public User(String name, String phone, Socket client_socket) {
        this.name = name;
        this.phone = phone;
        this.client_socket = client_socket;
        this.client_IP = client_socket.getInetAddress().getHostAddress();
        this.client_port = client_socket.getPort();
    }

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
        client_socket = null;
        client_IP = "";
        client_port = -1;
    }

    public User() {
        name = "";
        phone = "";
        client_IP = "";
        client_port = -1;
        client_socket = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClient_IP() {
        return client_IP;
    }

    public void setClient_IP(String client_IP) {
        this.client_IP = client_IP;
    }

    public int getClient_port() {
        return client_port;
    }

    public void setClient_port(int client_port) {
        this.client_port = client_port;
    }

    public Socket getClient_socket() {
        return client_socket;
    }

    public void setClient_socket(Socket client_socket) {
        this.client_socket = client_socket;
    }
}
