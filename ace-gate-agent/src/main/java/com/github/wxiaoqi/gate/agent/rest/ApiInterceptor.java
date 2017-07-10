package com.github.wxiaoqi.gate.agent.rest;

import com.alibaba.fastjson.JSON;
import com.github.wxiaoqi.gate.agent.exception.AuthenticationServerErrorException;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api权限拦截器
 * Created by ace on 2017/7/6.
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {
    private String authHost;
    private String tokenHead;

    /**
     * @param tokenHead 认证信息，默认access-token
     */
    public ApiInterceptor(String authHost,String tokenHead) {
        this.authHost = authHost;
        this.tokenHead = tokenHead;
    }

    /**
     * 默认access-token
     *
     */
    public ApiInterceptor(String authHost) {
        this.authHost = authHost;
        this.tokenHead = "access-token";
    }

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ApiGateSecurity methodAnnotation = methodAnnotation =  handlerMethod.getBeanType().getAnnotation(ApiGateSecurity.class);
        if(methodAnnotation==null)
            methodAnnotation = handlerMethod.getMethodAnnotation(ApiGateSecurity.class);
        String token = httpRequest.getHeader(tokenHead);
        if (methodAnnotation != null) {
            HttpResponse response = HttpRequest.get(authHost + "/verify").query("token", token).query("resource", httpRequest.getRequestURI()+":"+httpRequest.getMethod())
                    .send();
            if (response.statusCode() == 200) {
                return super.preHandle(httpRequest, httpResponse, handler);
            } else if (response.statusCode() == 401) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return false;
            } else {
                throw new AuthenticationServerErrorException(JSON.toJSONString(response));
            }
        }
        return super.preHandle(httpRequest, httpResponse, handler);
    }
}
