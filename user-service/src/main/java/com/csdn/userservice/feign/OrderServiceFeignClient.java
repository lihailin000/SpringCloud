package com.csdn.userservice.feign;

import com.csdn.userservice.configuration.OrderServiceFeignConfiguration;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "order-service",configuration = OrderServiceFeignConfiguration.class)
public interface OrderServiceFeignClient {
    /**
     * http://order-service/order/query
     * @return
     */
    @RequestMapping("/order/query")
    public String query();
}
