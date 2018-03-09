package com.yatang.xc.xcr.biz.core.dubboservice.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by wangyang on 2017/7/7.
 */
public class DateUtil {

    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取两日期之间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
//    public static int getDay(Date startDate, Date endDate) {
//        if (startDate.after(endDate)) {
//            Date cal = startDate;
//            startDate = endDate;
//            endDate = cal;
//        }
//        long sl = startDate.getTime();
//        long el = endDate.getTime();
//        long ei = el - sl;
//        return (int) (ei / 0x5265c00L);
//    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static Date getStartOfMonth() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, 1);// 设为当前月的1号
        // 设置时分秒毫秒为：00:00:00,000
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    /**
     * 获取当月最后一天
     *
     * @return
     */
    public static Date getEndOfMonth() {
        Calendar date = Calendar.getInstance();

        date.setTime(getStartOfMonth());// 设为当前月开始时间
        date.add(Calendar.MONTH, 1);// 加一个月，变为下月的开始时间
        date.add(Calendar.MILLISECOND, -1);// 减去一毫秒，变为当月结束时间
        return date.getTime();
    }

    /**
     * 获取两日期之间的天数
     *
     * @return
     */
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


    /**
     * 日期转字符串“yyyy-MM-dd”
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return format(date, YYYYMMDD);
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

    public static Date stringToDefaultDateFormat(String date) {
        if (date != null && date.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDDHHMMSS);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    public static void main(String[] args) {

        String day1 = "2015-08-10 23:10:10";
        String day2 = "2017-08-12 0:10:10";
        int i = getDay(stringToDefaultDateFormat(day1), stringToDefaultDateFormat(day2));
        System.out.println(i);

//        System.out.println(formatDate(new Date()));
//        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
//        Date start = getStartOfMonth();
//        Date end = getEndOfMonth();
//        System.out.println(format.format(start));
//        System.out.println(format.format(end));

    }

}
