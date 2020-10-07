package com.csdn.userservice.controller;

import com.csdn.userservice.feign.OrderServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
@RefreshScope
public class UserController {

    // 测试远端的配置文件的读取
    @Value("${person.username}")
    private String personUsername;

    @RequestMapping("/nacosconfig")
    public String nacosconfig() {
        return this.personUsername;
    }

    private @Autowired DiscoveryClient discoveryClient;

    private @Autowired RestTemplate restTemplate;
    //localhost:9091/order/query  HttpURLConnection
    @RequestMapping("getInstances")
    public List<ServiceInstance> getInstances(){
        return discoveryClient.getInstances("order-service");
    }

    @RequestMapping("restTemplate1")
    public String restTemplate(){
        return restTemplate.getForObject("http://localhost:9091/order/query",String.class);
    }

    @RequestMapping("test2")
    public String test2(){
        List<ServiceInstance> instances = discoveryClient.getInstances("order-service");
        String url = instances.stream().map(instance -> instance.getUri().toString() + "/order/query").findFirst().
                orElseThrow(()->new IllegalArgumentException("找不到对应实例数据"));
        return restTemplate.getForObject(url,String.class);
    }

    @RequestMapping("test3")
    public String test3(){
        List<ServiceInstance> instances = discoveryClient.getInstances("order-service");
        List<String> urls = instances.stream().map(instance -> instance.getUri().toString() + "/order/query").collect(Collectors.toList());
        //随机的负载均衡算法
        int i = ThreadLocalRandom.current().nextInt(urls.size());
        String url = urls.get(i);
        return restTemplate.getForObject(url,String.class);
    }

    //使用Ribbon的代码
    @RequestMapping("test4")
    public String test4(){
        //Ribbon的底层原理是不是将order-service替换成一个ip：port
     return restTemplate.getForObject("http://order-service/order/query",String.class);
    }

    //feign
    private @Autowired OrderServiceFeignClient orderServiceFeignClient;
    @RequestMapping("test5")
    public String test5(){
        return orderServiceFeignClient.query();
    }


    //sentinel
    @RequestMapping("test")
    public String test(){
        return "sentinel。。。";
    }
}
