package com.yatang.xc.xcr.vo;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * 校验返回参数
 * Created by wangyang on 2017/9/26.
 */
public class CheckParamResult {

    private boolean isPass;
    private String message;

    public CheckParamResult(boolean isPass, String message) {
        this.isPass = isPass;
        this.message = message;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




    public static String randomUUID(int size) {
        size = size > 32 ? 32 : size;
        String uuid = StringUtils.remove(UUID.randomUUID().toString(), "-");
        uuid = StringUtils.substring(uuid, 0, size);
        return uuid;
    }

    public static void main(String[] args) {
        String uuid = randomUUID(32);
        System.out.println(uuid);

    }
}
