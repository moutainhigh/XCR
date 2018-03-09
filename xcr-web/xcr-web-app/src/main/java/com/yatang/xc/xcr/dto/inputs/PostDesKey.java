package com.yatang.xc.xcr.dto.inputs;

import com.yatang.xc.xcr.annotations.CookieValue;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2018年01月03日 17:22
 * @Summary :
 */
public class PostDesKey implements Serializable{
    
    /**
     *
     */
    @CookieValue("DeviceId")
    private String  deviceId;

    /**
     *
     */
    @CookieValue("Type")
    private Integer type;

    /**
     *
     */
    private String  msg;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"deviceId\":\"").append(deviceId).append('\"');
        sb.append(",\"type\":\"").append(type).append('\"');
        sb.append(",\"msg\":\"").append(msg).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
