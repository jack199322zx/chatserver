package com.nino.chat.server.context;

import com.nino.chat.server.exception.ServerRuntimeException;
import com.nino.chat.server.model.Message;
import com.nino.chat.server.model.User;
import com.nino.chat.server.processor.MessageProcessor;
import com.nino.chat.server.utils.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author ss
 * @date 2018/9/11 13:58
 */
public class TcpServerContext implements ServerContext {

    private Map<String, PrintWriter> printWriterMap = new ConcurrentHashMap<>();
    private List<User> userInfos = new ArrayList<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    private MessageProcessor processor;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public TcpServerContext(int port) {
        processor = new MessageProcessor(printWriterMap);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerRuntimeException("建立连接失败");
        }
    }

    public void registerClient(User user, PrintWriter printWriter) {
        if (user == null || printWriter == null) return;
        printWriterMap.put(user.getNickName(), printWriter);
        try {
            lock.lock();
            userInfos.add(user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void removeClient(String name) {
        printWriterMap.remove(name);
    }

    public void consoleCurrentUserNum() {
        System.out.println("当前在线人数为："+ printWriterMap.size());
    }

    @Override
    public void startUp() {
        try {
            while (true) {
                System.out.println("等待客户端连接...");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端连接了..." + "，ip是" + serverSocket.getInetAddress());
                executor.submit(new ClientService(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void publishMessage(Message message) {
        processor.addMessage(message);
        processor.processMessage();
    }

    class ClientService implements Runnable {

        private PrintWriter printWriter;
        private BufferedReader bufferedReader;
        private Socket socket;
        private User user;

        ClientService(Socket socket) {
            this.socket = socket;
            try {
                printWriter = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
                bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            user = readUserInfo();
            System.out.println("登录通过");
            registerClient(user, printWriter);
            Message message = new Message(userInfos, user.getNickName() + "上线了!", Message.MessageType.LOGIN);
            publishMessage(message);
            handleUserAction();
        }

        private User readUserInfo() {
            while (true) {
                try {
                    String name = bufferedReader.readLine();
                    if (!StringUtils.isBlank(name)) {
                        if (printWriterMap.containsKey(name)) {
                            printWriter.println("请勿重复登录");
                        } else {
                            // todo 登录校验
                            printWriter.println("登录成功");
                            return User.builder()
                                    .nickName(name)
                                    .build();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleUserAction() {
            try {
                String str = null;
                while((str = bufferedReader.readLine()) != null) {
                    // 检验是否为私聊（格式：@昵称：内容）
                    if(str.startsWith("@")) {
                        int index = str.indexOf(":");
                        if(index >= 0) {
                            //获取昵称
                            String userName = str.substring(1, index);
                            System.out.println("私聊消息：" + str);
                            //将私聊信息发送出去
                            List<User> users = userInfos.stream().filter(info -> info.getNickName().equals(userName)).collect(Collectors.toList());
                            if (users.isEmpty()) return;
                            Message message = new Message(user, users, user.getNickName() + str);
                            publishMessage(message);
                            continue;
                        }
                    }
                    // 遍历所有输出流，将该客户端发送的信息转发给所有客户端
                    System.out.println("公聊消息：" + str);
                    Message message = new Message(user, userInfos, str);
                    publishMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServerRuntimeException("处理用户发送消息发生错误");
            } finally {
                removeClient(user.getNickName());
                Message message = new Message(userInfos, user.getNickName() + "下线了!", Message.MessageType.LOGOUT);
                publishMessage(message);
                consoleCurrentUserNum();
                if(socket!=null) {
                    try {
                        socket.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }
}
