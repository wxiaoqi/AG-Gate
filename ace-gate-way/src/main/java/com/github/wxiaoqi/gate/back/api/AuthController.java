package com.github.wxiaoqi.gate.back.api;

import com.github.wxiaoqi.gate.back.biz.AuthBiz;
import com.github.wxiaoqi.gate.back.secruity.ApiAuthenticationRequest;
import com.github.wxiaoqi.gate.back.secruity.ApiAuthenticationResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/authen")
public class AuthController {
    @Value("${gate.api.header}")
    private String tokenHeader;

    @Autowired
    private AuthBiz authService;
    @ApiOperation(value="获取有效时长的token", notes="传入网关中心发布的客户端clientId和secret")
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody ApiAuthenticationRequest authenticationRequest) {
        final String token = authService.login(authenticationRequest.getClientId(), authenticationRequest.getSecret());

        // Return the token
        return ResponseEntity.ok(new ApiAuthenticationResponse(token));
    }
    @ApiOperation(value="刷新并获取新的token", notes="传入旧的token")
    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            String token) {
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new ApiAuthenticationResponse(refreshedToken));
        }
    }
    @ApiOperation(value="验证token是否有效", notes="传入申请的token")
    @RequestMapping(value = "verify", method = RequestMethod.GET)
    public ResponseEntity<?> verify(String token,String resource){
        if(authService.validate(token,resource))
            return ResponseEntity.ok(true);
        else
            return ResponseEntity.status(401).body(false);
    }

}
