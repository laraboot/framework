package com.laraboot.framework.http.aspect;

import com.laraboot.framework.auth.AuthManager;
import com.laraboot.framework.contracts.http.HttpMiddlewareRegister;
import com.laraboot.framework.contracts.kernel.pipeline.Dockable;
import com.laraboot.framework.debug.ExceptionHandler;
import com.laraboot.framework.http.middlewares.MiddlewareTemplate;
import com.laraboot.framework.http.response.Response;
import com.laraboot.framework.http.middlewares.HttpMiddlewarePipeline;
import com.laraboot.framework.http.middlewares.Middleware;
import com.laraboot.framework.util.RequestUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpAspect {

    @Autowired
    ExceptionHandler exceptionHandler;

    @Autowired
    AuthManager authManager;

    private Map<String, List<Dockable>> middlewareGroups;
    private List<Dockable> globalMiddlewareStack;

    @Autowired
    public void setMiddlewareGroups(HttpMiddlewareRegister register) {
        this.middlewareGroups = register.registerMiddlewareGroups();
        this.globalMiddlewareStack = register.registerGlobalMiddlewares();
    }

    public Object aroundPointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestUtil.request();

        // 需要执行的中间件
        List<Dockable> middlewareStack = new ArrayList<>(this.globalMiddlewareStack);
        List<String> groups = new ArrayList<>();
        List<String> excludes = new ArrayList<>();

        Class targetClass = joinPoint.getTarget().getClass();
        Middleware targetClassAnnotation = (Middleware) targetClass.getAnnotation(Middleware.class);

        if (targetClassAnnotation != null) {
            middlewareStack = targetClassAnnotation.excludeGlobal() ? new ArrayList<>() : middlewareStack;
            groups.addAll(Arrays.asList(targetClassAnnotation.groups()));
            excludes.addAll(Arrays.asList(targetClassAnnotation.excludes()));
        }

        MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
        Method method = methodSign.getMethod();
        Middleware targetMethodAnnotation = method.getAnnotation(Middleware.class);

        if (targetMethodAnnotation != null) {
            if (targetMethodAnnotation.template() == MiddlewareTemplate.BLANK) {
                middlewareStack = targetMethodAnnotation.excludeGlobal() ? new ArrayList<>() : new ArrayList<>(this.globalMiddlewareStack);
                groups = new ArrayList<>();
                excludes = new ArrayList<>();
            } else if (targetMethodAnnotation.template() == MiddlewareTemplate.EXTENDS) {
                middlewareStack = targetMethodAnnotation.excludeGlobal() ? new ArrayList<>() : new ArrayList<>(this.globalMiddlewareStack);
            }
            groups.addAll(Arrays.asList(targetMethodAnnotation.groups()));
            excludes.addAll(Arrays.asList(targetMethodAnnotation.excludes()));
        }

        middlewareStack.addAll(getGroups(groups, excludes));

        try {
            // 通过管道执行中间件和控制器逻辑
            return (new HttpMiddlewarePipeline())
                    .send(request)
                    .through(middlewareStack)
                    .then(traveler -> toResponse(joinPoint.proceed()));
        } catch (Throwable throwable) {
            return exceptionHandler.handle(request, throwable);
        }
    }

    /**
     * 获取中间件
     *
     * @param groups  分组名
     * @param exclude 排除的分组名
     * @return middleware
     */
    protected List<Dockable> getGroups(List<String> groups, List<String> exclude) {
        List<Dockable> dockables = new ArrayList<>();

        for (String group : groups) {
            if (exclude.indexOf(group) == -1 && middlewareGroups.containsKey(group)) {
                dockables.addAll(middlewareGroups.get(group));
            }
        }

        return dockables;
    }

    public static final ResponseEntity<?> toResponse(Object data) {
        if (data instanceof ResponseEntity) {
            return (ResponseEntity) data;
        }
        if (data instanceof Response) {
            return ((Response) data).responseEntity();
        }

        return Response.ok(data).responseEntity();
    }
}
