package com.github.wxiaoqi.gate.back.filter;

import com.github.wxiaoqi.gate.agent.vo.authority.PermissionInfo;
import com.github.wxiaoqi.gate.agent.vo.log.LogInfo;
import com.github.wxiaoqi.gate.agent.vo.user.UserInfo;
import com.github.wxiaoqi.gate.back.service.LogService;
import com.github.wxiaoqi.gate.back.service.UserService;
import com.github.wxiaoqi.gate.back.secruity.SecurityUser;
import com.github.wxiaoqi.gate.back.util.DBLog;
import com.github.wxiaoqi.gate.common.util.ClientUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by ace on 2017/7/8.
 */
@WebFilter(filterName="resourceAccessFilter",urlPatterns="/*")
@Slf4j
public class ResourceAccessFilter implements Filter {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Value("${gate.ignore.startWith}")
    private String startWith;
    @Value("${gate.ignore.contain}")
    private String contain;

    /**
     * 读取权限
     * @param request
     * @param username
     * @return
     */
    private List<PermissionInfo> getPermissionInfos(HttpServletRequest request, String username) {
        List<PermissionInfo> permissionInfos;
        if (request.getSession().getAttribute("permission") == null) {
            permissionInfos = userService.getPermissionByUsername(username);
            request.getSession().setAttribute("permission", permissionInfos);
        } else {
            permissionInfos = (List<PermissionInfo>) request.getSession().getAttribute("permission");
        }
        return permissionInfos;
    }

    /**
     * 权限校验
     * @param requestUri
     * @param method
     */
    private boolean checkAllow(final String requestUri, final String method , HttpServletRequest request, String username) {
        log.debug("uri：" + requestUri + "----method：" + method);
        List<PermissionInfo> permissionInfos = getPermissionInfos(request, username) ;
        Collection<PermissionInfo> result =
                Collections2.filter(permissionInfos, new Predicate<PermissionInfo>() {
                    @Override
                    public boolean apply(PermissionInfo permissionInfo) {
                        String url = permissionInfo.getUri();
                        String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                        String regEx = "^" + uri + "$";
                        return (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url + "/"))
                                && method.equals(permissionInfo.getMethod());
                    }
                });
        if (result.size() <= 0) {
            return false;
        } else{
            PermissionInfo[] pms =  result.toArray(new PermissionInfo[]{});
            PermissionInfo pm = pms[0];
            if(!method.equals("GET")){
                setCurrentUserInfoAndLog(request, username, pm);
            }
            return true;
        }
    }

    private void setCurrentUserInfoAndLog(HttpServletRequest request, String username, PermissionInfo pm) {
        UserInfo info = userService.getUserByUsername(username);
        String host =  ClientUtil.getClientIp(request);
        request.setAttribute("userId", info.getId());
        request.setAttribute("userName", URLEncoder.encode(info.getName()));
        request.setAttribute("userHost", ClientUtil.getClientIp(request));
        LogInfo logInfo = new LogInfo(pm.getMenu(),pm.getName(),pm.getUri(),new Date(),info.getId(),info.getName(),host);
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }

    /**
     * 是否包含某种特征
     * @param requestUri
     * @return
     */
    private boolean isContains(String requestUri) {
        boolean flag = false;
        for (String s : contain.split(",")) {
            if (requestUri.contains(s))
                return true;
        }
        return flag;
    }

    /**
     * URI是否以什么打头
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s))
                return true;
        }
        return flag;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        final String requestUri = request.getRequestURI();
        // 不进行拦截的地址
        if (isStartWith(requestUri) || isContains(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }
        else if(SecurityContextHolder.getContext().getAuthentication()!= null&&!(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof String)){

            final String method = request.getMethod();
            try {
                SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                request.getSession().setAttribute("user",userDetails);
                if (!checkAllow(requestUri, method, request, username)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                    return ;
                }
            }catch (Exception e){
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
