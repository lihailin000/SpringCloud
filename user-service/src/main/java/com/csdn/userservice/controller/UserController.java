package com.csdn.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/user")
public class UserController {

    //localhost:9091/order/query  HttpURLConnection
    @GetMapping("query")
    public String query(){
//        HttpURLConnection
        return "list user";
    }
}
