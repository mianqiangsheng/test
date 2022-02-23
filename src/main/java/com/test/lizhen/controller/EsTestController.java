package com.test.lizhen.controller;

import com.google.gson.Gson;
import com.test.LogObject;
import com.test.service.EsTestService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/es/send")
@Api(value = "/es/send", tags = "发送日志")
public class EsTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsTestController.class);

    @Autowired
    private EsTestService esTestService;

    public ResponseEntity<String> trace() {
        LOGGER.trace("This is a trace message" + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    public ResponseEntity<String> error() {
        LOGGER.error("This is a error message" + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    public ResponseEntity<String> debug() {
        LOGGER.debug("This is a debug message" + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    public ResponseEntity<String> info() {
        LOGGER.info("This is a info message" + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    public ResponseEntity<String> warn() {
        LOGGER.warn("This is a warn message" + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    public ResponseEntity<String> throwException() {
        int i = 1 / 0;
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    public ResponseEntity<String> catchException() {
        try {
            throw new ClassNotFoundException();
        }catch (ClassNotFoundException e){
            LOGGER.error("This is a catchException message {}",e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    /**
     * 使用LogObject + try catch finally来记录日志
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/logSave")
    public ResponseEntity<Object> logSave(@RequestBody Object request) throws Exception{

        Gson gson = new Gson();
        String methodName = "/logSave";
        Integer backendId = null;
        String msg = "success";
        long beginTime = System.currentTimeMillis();
        String traceId = "logSave_"+beginTime;
        Object response = null;
        try {
            /**
             * 业务代码赋值响应给response
             * response = ......
             */
            throw new Exception("模拟业务代码报错");
        } catch (Exception e) {
            msg = e.getMessage();
            LOGGER.error(methodName+",userId:"+backendId+",request:"+ gson.toJson(request),e);
            throw new Exception("下单失败");
        }
        finally {
            long endTime = System.currentTimeMillis();
            LogObject logObject = new LogObject();
            logObject.setEventName(methodName)
                    .setMsg(msg)
                    .setTraceId(traceId)
                    .setUserId(backendId)
                    .setRequest(request)
                    .setResponse(response)
                    .setCostTime((endTime - beginTime));

            LOGGER.info(gson.toJson(logObject));
        }

//        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 使用LogObject + AOP + ThreadLocal来记录日志
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/logSave1")
    public ResponseEntity<Object> logSave1(@RequestBody Object request) throws Exception{
//        this.logSave2(new Object()); //同类内部方法调用AOP失效，需要特殊处理AopContext.currentProxy()
        esTestService.method(new Object());
        return ResponseEntity.status(HttpStatus.OK).body("成功返回");
    }

    public ResponseEntity<Object> logSave2(@RequestBody Object request) throws Exception{
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("失败返回");
    }
}
