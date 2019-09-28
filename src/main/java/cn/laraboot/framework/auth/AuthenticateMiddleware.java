package cn.laraboot.framework.auth;

import cn.laraboot.framework.contracts.auth.Guard;
import cn.laraboot.framework.http.middlewares.HttpMiddleware;
import cn.laraboot.framework.contracts.kernel.pipeline.Stack;
import cn.laraboot.framework.foundation.App;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class AuthenticateMiddleware extends HttpMiddleware {

    private String guardName;

    public AuthenticateMiddleware(String guard) {
        guardName = guard;
    }

    @Override
    public ResponseEntity handle(HttpServletRequest request, Stack stack) throws Throwable {

        AuthManager authManager = App.getBean(AuthManager.class);
        Guard guard = authManager.driver(guardName);

        if (guard.parseCredentials(request).guest()) {
            throw new UnauthorizedException("未经授权的请求!");
        }

        String credentialsKey = guard.credentialsKey(request);

        // 临时存起来
        authManager.getUsers().put(credentialsKey, guard.user());

        // 执行控制器逻辑
        ResponseEntity response = (ResponseEntity) stack.next(request);

        // 控制器执行完后移除
        authManager.getUsers().remove(credentialsKey);
        return response;
    }
}
