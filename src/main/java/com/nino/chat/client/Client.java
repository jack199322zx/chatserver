package com.nino.chat.client;


import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ss
 * @date 2018/9/11 12:01
 */
public class Client {

    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private ExecutorService executorService;
    private static Scanner scanner;
    private static Random random = new Random();

    public Client(Socket socket) {
        this.socket = socket;
        executorService = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.println("请设置服务器IP：");
        String ip = scanner.next();
        try {
            Socket socket = new Socket(ip, 8088);
            Client client = new Client(socket);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 接收服务器端发送过来的信息的线程启动
        executorService.submit(new Receiver());
        System.out.println("请设置昵称：");
        String nickName = scanner.next();
        printWriter.println(nickName);
        while (true) {
            String message = scanner.next();
            System.out.println("客户端发送消息：" + message);
            printWriter.println(message);
        }
    }

    class Receiver implements Runnable {

        @Override
        public void run() {
            try {
                String message = null;
                while ((message = bufferedReader.readLine()) != null) {
                    int nickNameFlag, contentFlag;
                    if ((nickNameFlag = message.indexOf("@")) > -1 && (contentFlag = message.indexOf(":")) > -1) {
                        String fromName = message.substring(0, nickNameFlag);
                        System.out.println("收到来自" + fromName + "的消息：" + message.substring(contentFlag));
                        return;
                    }
                    System.out.println("收到服务端消息：" + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
