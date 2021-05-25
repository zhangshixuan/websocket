package com.example.controller;

import com.example.client.CustomWebSocket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {

    @GetMapping("/send")
    public String send(){
        CustomWebSocket.sendMessage("这是群发消息");
        return "发送成功";
    }
}
