package com.github.wxiaoqi.gate.back;

import com.github.wxiaoqi.gate.back.util.DBLog;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-05-25 12:44
 */
@EnableEurekaClient
@EnableHystrix
@SpringBootApplication
@ServletComponentScan("com.github.wxiaoqi.gate.back")
@EnableDiscoveryClient
@EnableZuulProxy
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class GateBootstrap {
    public static void main(String[] args) {
        DBLog.getInstance().start();
        new SpringApplicationBuilder(GateBootstrap.class).web(true).run(args);    }
}
