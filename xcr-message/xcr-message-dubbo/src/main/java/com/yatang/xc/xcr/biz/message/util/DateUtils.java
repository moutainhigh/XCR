package com.yatang.xc.xcr.biz.message.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by wangyang on 2017/12/22.
 */
public class DateUtils {

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateFormat(Date date, String format) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        }
    }

    /**
     * 获取days天前/后的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addOneDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

}



