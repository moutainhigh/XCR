package com.yatang.xc.xcr.biz.core.dubboservice.util;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月27日 10:36
 * @Summary :
 */
public final class Assert {

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) { if (!Character.isDigit(str.charAt(i))) { return false; } }
        return true;
    }

}
