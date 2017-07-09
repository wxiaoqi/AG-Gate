package com.github.wxiaoqi.gate.demo.provider;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by ace on 2017/7/9.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ProviderBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ProviderBootstrap.class).web(true).run(args);    }
}
