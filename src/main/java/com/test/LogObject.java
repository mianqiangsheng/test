package com.test;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ：li zhen
 * @description:
 * @date ：2022/2/23 13:12
 */
public class LogObject {

    @JsonProperty(index = 1)
    private String eventName; //事件名称,一般就是业务方法名称

    @JsonProperty(index = 2)
    private String traceId; //调用链id

    @JsonProperty(index = 3)
    private String msg; //结果消息

    @JsonProperty(index = 4)
    private long costTime; //接口响应时间

    @JsonProperty(index = 6)
    private Integer userId; //C端用户id

    @JsonProperty(index = 7)
    private Object others; //其他业务参数

    @JsonProperty(index = 8)
    private Object request; //接口请求入参

    @JsonProperty(index = 9)
    private Object response; //接口返回值


    public String getEventName() {
        return eventName;
    }

    public LogObject setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public Object getRequest() {
        return request;
    }

    public LogObject setRequest(Object request) {
        this.request = request;
        return this;
    }

    public Object getResponse() {
        return response;
    }

    public LogObject setResponse(Object response) {
        this.response = response;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public LogObject setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public long getCostTime() {
        return costTime;
    }

    public LogObject setCostTime(long costTime) {
        this.costTime = costTime;
        return this;
    }


    public Integer getUserId() {
        return userId;
    }

    public LogObject setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Object getOthers() {
        return others;
    }

    public LogObject setOthers(Object others) {
        this.others = others;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public LogObject setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
