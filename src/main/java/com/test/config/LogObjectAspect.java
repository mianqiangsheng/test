package com.test.config;

import com.google.gson.Gson;
import com.test.LogObject;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ThreadLocal.MyThreadLocal;

import java.lang.reflect.Method;

/**
 * 切面类
 * Created by lizhen on 2018/11/15.
 */
@Aspect
@Component
public class LogObjectAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogObjectAspect.class);

    private static final Gson gson = new Gson();

    private static final MyThreadLocal<LogObject> logObjectThreadLocal = new MyThreadLocal<>(LogObject.class);

    /**
     * 定义切点
     * 例如：execution (* com.sample.service.impl..*.*(..)
     * 1、execution(): 表达式主体。
     * 2、第一个*号：表示返回类型，*号表示所有的类型。
     * 3、包名：表示需要拦截的包名，后面的两个点表示当前包和当前包的所有子包，
     * 即com.sample.service.impl包、子孙包下所有类的方法。
     * 4、第二个*号：表示类名，*号表示所有的类。
     * 5、*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个点表示任何参数。
     **/
    @Pointcut("execution(* com.test.lizhen.controller.*.*(..))")
    public void pointcut1() {
    }

    @Pointcut("execution(* com.test.service.impl.*.*(..))")
    public void pointcut2(){}

    @Pointcut("pointcut1() || pointcut2()")
    public void pointcut(){}

    @Around("pointcut()")
    public Object aroundPointcut(ProceedingJoinPoint p) {

        System.out.println("当前线程：" + Thread.currentThread().getName() + Thread.currentThread().getId());
        LogObject logObject = logObjectThreadLocal.get();

        Signature signature = p.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();

        String methodName = method.getName();
        Integer backendId = null;
        String msg = "success";
        long beginTime = System.currentTimeMillis();
        String traceId = logObject.getTraceId();
        if (StringUtils.isBlank(logObject.getTraceId())){
            traceId = methodName + "_" + beginTime;
        }
        Object response = null;
        Object[] request = p.getArgs();

        /**
         * 须在执行目标对象方法前将需要线程共享的信息放入ThreadLocal
         */
        logObject.setTraceId(traceId)
                .setUserId(backendId);
        logObjectThreadLocal.set(logObject);

        try {
            response = p.proceed();
        } catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage());
        }

        long endTime = System.currentTimeMillis();

        logObject.setEventName(methodName)
                .setMsg(msg)
                .setRequest(request)
                .setResponse(response)
                .setCostTime((endTime - beginTime));
        LOGGER.info(gson.toJson(logObject));

        return response;

    }


}
