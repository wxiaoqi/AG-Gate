# 简介
AG-Gate 是一个基于spring cloud的用户资源授权、api管理授权的网关系统，以jwt交互的鉴权token来实施，支持基于Eureka注册中心下的服务鉴权和拦截，同时扩展Eureka下服务失效的通知扩展。
![img](http://ofsc32t59.bkt.clouddn.com/17-07-09/1499585459528.jpg)
![img](http://ofsc32t59.bkt.clouddn.com/17-07-09/1499587875093.jpg)

# 功能列表
- 用户管理
- 角色管理
- 菜单、动作管理
- 操作日志
- 客户端注册
- 服务管理和授权
- 网关黑白名单和IP限制（待完善）
- 服务状态监控与提醒（待完善）
- 服务追踪（待完善）


# 快速运行
## 部署指南
- 运行ace-gate-way下db脚本
- 启动一个redis
- 修改ace-gate-way下的redis配置和数据库配置
- jdk1.8
- IDE插件一个，lombok插件，具体百度即可

## 启动指南
- 依次启动ace-gate-eureka、ace-gate-way
- 访问：http://localhost:8762/index，初始化密码：admin／admin
- 启动鉴权例子ace-gate-demo-provider、ace-gate-demo-client
- 访问：http://localhost:8764/test，即可看到效果

# 关于服务失效提醒
![img](http://ofsc32t59.bkt.clouddn.com/17-07-09/1499586948877.jpg)
可以根据需要扩展不同的消息通知，也可以做钩子结合实效后自动重启服务。

# 关于服务拦截和feign鉴权
**具体例子可以参考ace-gate-demo**
## 服务提供端如何开启权限拦截
### maven依赖，如果没有自行打包
```
<dependency>
    <groupId>com.github.wxiaoqi</groupId>
    <artifactId>ace-gate-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 配置引入
```
gate:
  client:
    # 地址为api-gate所在ip端口
    authHost: http://localhost:8765/api/authen
    # 鉴权头部标志，与客户端一致
    authHeader: access-token
```
### 配置启用
```
/**
 * Created by ace on 2017/7/6.
 */
@Configuration
public class ApiWebAppConfig extends WebMvcConfigurerAdapter {
    @Value("${gate.client.authHost}")
    private String authHost;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor(authHost)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}

```
### 服务拦截启用，rest方法加上`@ApiGateSecurity`即可
```
@ApiGateSecurity
@RequestMapping(value = "/user/un/{username}/system", method = RequestMethod.GET)
@ResponseBody
public String getSystemsByUsername(@PathVariable("username") String username){
    int userId = userBiz.getUserByUsername(username).getId();
    return JSONObject.toJSONString(menuBiz.getUserAuthoritySystemByUserId(userId));
}
```

## Feign客户端如何自动加上鉴权信息
### maven依赖，如果没有自行打包
```
<dependency>
    <groupId>com.github.wxiaoqi</groupId>
    <artifactId>ace-gate-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 配置引入
```
gate:
  client:
  # 页面注册的客户信息
    clientId: admin-ui
    secret: test
    # 与服务端authHeader保持一致
    authHeader: access-token
    authHost: http://localhost:8765/api/authen
    # 与ace-gate-way gate.api.tokenHead保持一致
    tokenHead: ApiGateToken
```

### 配置启用
```
@Configuration
public class FeignApiConfig {
    @Value("${gate.client.clientId}")
    private String clientId;
    @Value("${gate.client.secret}")
    private String secret;
    @Value("${gate.client.authHeader}")
    private String authHeader;
    @Value("${gate.client.authHost}")
    private String authHost;
    @Value("${gate.client.tokenHead}")
    private String tokenHead;

    @Bean
    public FeignInterceptor authenticationInterceptor() {
        return new FeignInterceptor(clientId, secret, authHeader, authHost, tokenHead);
    }

}
```

# 欢迎交流
![img](http://ofsc32t59.bkt.clouddn.com/17-06-16/1497595760484.jpg)


