package com.github.wxiaoqi.gate.client.rpc;

import com.github.wxiaoqi.gate.agent.agent.rest.ApiGateSecurity;
import com.github.wxiaoqi.gate.client.hystrix.LanguageServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ace on 2017/7/9.
 */
@FeignClient(value = "gate-demo-provider",fallback = LanguageServiceHystrix.class)
public interface ILanguageService {
    @RequestMapping(value = "/language/chinese",method = RequestMethod.GET)
    public String sayChineseHelloWorld();
    @RequestMapping(value = "/language/english",method = RequestMethod.GET)
    public String sayEnglishHelloWorld();
}
