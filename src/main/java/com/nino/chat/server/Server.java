package com.nino.chat.server;

import com.nino.chat.server.context.ServerContext;
import com.nino.chat.server.context.TcpServerContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ss
 * @date 2018/9/11 12:01
 */
public class Server {

    private ServerContext serverContext;
    private Properties properties;

    public Server(ConnectType type) {
        properties = new Properties();
        InputStream inputStream = Server.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(inputStream);
            String port = properties.getProperty("port");
            switch (type) {
                case TCP:
                    serverContext = new TcpServerContext(Integer.parseInt(port));
                    break;
                case UDP:
                    System.out.println("UDP");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取配置文件错误");
        }
    }

    public static void main(String[] args) {
        Server server = new Server(ConnectType.TCP);
        server.start();
    }

    public void start() {
        if (serverContext != null) {
            serverContext.startUp();
        }
    }

    public enum ConnectType {
        TCP, UDP
    }
}
