package com.example.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{username}")
@Component
@Slf4j
public class CustomWebSocket {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
     */
    private static Map<String, CustomWebSocket> clients = new ConcurrentHashMap<String, CustomWebSocket>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private String username;


    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.username = username;
        this.session = session;
        CustomWebSocket.onlineCount++;
        log.info("有一个连接进入。当前在线人数为：" + onlineCount + "。接入人名字：" + username);
        clients.put(username,this);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        clients.remove(username);
        //在线数减1
        CustomWebSocket.onlineCount--;
        log.info("有一个连接关闭。当前在线人数为：" + onlineCount);
    }

    /**
     * 收到客户端消息后调用
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("客户端发送的消息：" + message);
        sendMessage(message);
    }


    public static void sendMessage(String message){
        for (CustomWebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

}
