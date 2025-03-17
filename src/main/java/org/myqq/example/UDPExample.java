package org.example;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPExample {

    public static int PORT = 9999;

    public static void main(String[] args) {

        int i = 5;
        float f = 3.14f;
        var r = i + f;
        System.out.println();
//        Sender sender = new Sender();
//        Receiver receiver = new Receiver();
//
//        sender.start();
//        receiver.start();
//
//        try {
//            sender.join();
//            receiver.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}

class Receiver extends Thread {
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(UDPExample.PORT);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Received: " + packet.getAddress() + " " + packet.getPort());

                String s = new String(packet.getData(), 0, packet.getLength());
                if (s.equals("四大名著是哪些")) {
                    buffer = "《三国演义》《水浒传》《红楼梦》《西游记》".getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                    break;
                } else {
                    buffer = "What ?".getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Sender extends Thread {
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(UDPExample.PORT + 1);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
            while (true) {
                String s = bufferedReader.readLine();
                DatagramPacket packet = new DatagramPacket(s.getBytes(), s.getBytes().length, InetAddress.getLocalHost(), UDPExample.PORT);
                socket.send(packet);

                byte[] buffer = new byte[1024];
                DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet1);
                System.out.println("Sender: " + packet1.getAddress() + " " + packet1.getPort());
                bufferedWriter.write("Sender Received: " + new String(packet1.getData(), 0, packet1.getLength()));
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if (s.equals("四大名著是哪些")) {
                    break;
                }

            }
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
