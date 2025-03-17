package org.myqq.example;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TcpExample {

    public static int PORT = 9999;

    public static void main(String[] args) {
        Server s = new Server();
        Client c = new Client();

        s.start();
        c.start();

        try {
            s.join();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Client extends Thread {
    public void run() {
        try {

            Socket socket = new Socket(Inet4Address.getLocalHost(), TcpExample.PORT);

            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            BufferedReader isr = new BufferedReader(new InputStreamReader(System.in));
            String fileName;
            while ((fileName = isr.readLine()) != null) {
                if (fileName.equals("exit")) {
                    out.write(fileName);
                    out.newLine();
                    out.flush();
                    break;
                }
                System.out.println("Clinet require " + fileName);
                out.write(fileName);
                out.newLine();
                out.flush();

                // 首先读取文件大小
                DataInputStream dataIn = new DataInputStream(in);
                int fileNameLength = dataIn.readInt();
                String receivedFileName = new String(in.readNBytes(fileNameLength));

                long fileSize = dataIn.readLong();
                if (fileSize > 0) {
                    byte[] buffer = new byte[8192]; // 使用固定大小的缓冲区
                    FileOutputStream fos = new FileOutputStream("./src/main/resources/abc/" + receivedFileName);

                    long totalRead = 0;
                    int read;
                    while (totalRead < fileSize && (read = in.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalRead))) != -1) {
                        fos.write(buffer, 0, read);
                        totalRead += read;
                    }

                    fos.flush();
                    fos.close();
                    System.out.println("Client received: " + receivedFileName);
                }
            }

            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Server extends Thread {
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(TcpExample.PORT);
            Socket clientSocket = serverSocket.accept();

            // 从客服端读取文件名
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // 传输文件给客户端
            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());


            String filePath = "./src/main/resources/";
            String[] validatedFileNames = new File(filePath).list();

            String s;
            while ((s = in.readLine()) != null) {
                if (s.equals("exit")) {
                    break;
                }
                String fileName = "default.txt"; // 默认文件
                for (String validatedFileName : validatedFileNames) {
                    if (validatedFileName.equals(s)) {
                        fileName = s;
                        break;
                    }
                }

                FileInputStream fis = new FileInputStream(filePath + fileName);
                DataOutputStream dataOut = new DataOutputStream(out);

                // 先发送文件大小
                dataOut.writeInt(fileName.getBytes().length);
                out.write(fileName.getBytes(), 0, fileName.getBytes().length);
                out.flush();

                dataOut.writeLong(fis.available());

                // 发送文件内容
                byte[] buffer = new byte[8192];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                fis.close();
            }

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}