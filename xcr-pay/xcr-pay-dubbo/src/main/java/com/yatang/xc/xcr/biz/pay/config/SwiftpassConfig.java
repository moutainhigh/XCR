package com.yatang.xc.xcr.biz.pay.config;

import org.springframework.stereotype.Component;

/**
 * 威富通支付配置
 * Created by wangyang on 2017/10/26.
 */
@Component
public class SwiftpassConfig {

    private String service;      //支付类型
    private String req_url;      //接口请求地址，固定不变，无需修改
    private String notify_url;   //通知回调地址
    private String appid;        //APPID

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getReq_url() {
        return req_url;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
