package com.github.wxiaoqi.gate.back.controller;

import com.github.wxiaoqi.gate.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-27 12:40
 */
@Controller
@RequestMapping("admin")
public class UserPermissionController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @RequestMapping(value = "/user/system",method = RequestMethod.GET)
    @ResponseBody
    public String getUserSystem(){
       return userService.getSystemsByUsername(getCurrentUserName());
    }
    @RequestMapping(value = "/user/menu",method = RequestMethod.GET)
    @ResponseBody
    public String getUserMenu(@RequestParam(defaultValue = "-1") Integer parentId){
        return userService.getMenusByUsername(getCurrentUserName(),parentId);
    }

    public String getCurrentUserName(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }
}
