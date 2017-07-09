package com.github.wxiaoqi.gate.client.rest;

import com.github.wxiaoqi.gate.client.rpc.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ace on 2017/7/9.
 */
@Controller
//@RequestMapping("")/
public class SaysomethingRest {
    @Autowired
    private ILanguageService languageService;
    @RequestMapping("test")
    public @ResponseBody String test(){
        return languageService.sayChineseHelloWorld()+" : "+languageService.sayEnglishHelloWorld();
    }
}
