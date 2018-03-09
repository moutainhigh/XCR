package com.yatang.xc.xcr.biz.mission;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangyang on 2017/7/8.
 */
public class DateUtil {

    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateByString(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串转日期 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date getDateTimeByString(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
        Date result = null;
        try {
            result = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取某时间加 减几天
     *
     * @param days 天数
     * @return Date
     */
    public static Date addOneDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }


    /**
     * 日期转字符串“yyyy-MM-dd”
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return format(date, YYYYMMDD);
    }

    /**
     * 日期转字符串“yyyy-MM-dd dd:mm:ss”
     *
     * @param date
     * @return
     */
    public static String formatDateDefaule(Date date) {
        return format(date, YYYYMMDDHHMMSS);
    }

    private static String format(Date date, String patten) {
        String result = null;
        if (date != null) {
            if (patten == null) {
                patten = YYYYMMDDHHMMSS;
            }
            DateFormat dateFormat = new SimpleDateFormat(patten);
            try {
                result = dateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Integer getDay(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int year1 = aCalendar.get(Calendar.YEAR);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int year2 = aCalendar.get(Calendar.YEAR);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        if ((year2 - year1) == 1) {
            if (year1 % 400 == 0 || year1 % 4 == 0 && year1 % 100 != 0) {
                if (day2 - day1 == -365) {
                    return 1;
                }
                return (366 - day1 + day2);
            }
            if (day2 - day1 == -364) {
                return 1;
            }
            return (366 - day1 + day2);
        }
        if ((year2 - year1) > 1) {
            return (366 - day1 + day2) * (year2 - year1);
        }
        return day2 - day1;
    }

    public static void main(String[] args) {

        try {
            Date day1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2019-12-31 23:30:30");
            Date day2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2019-12-31 00:00:30");

            Integer a =  getDay(day1,day2);
            System.out.println(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
