package com.yatang.xc.xcr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	public static final String LOG_FORMAT="yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPLE_FORMAT = "yyyy-MM-dd";
	public static final String[] weekArray = new String[] { "星期日", "星期一",
			"星期二", "星期三", "星期四", "星期五", "星期六" };

	/**
	 * 
	 * 
	 * 功能说明：将指定日期格式化成yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:06:19
	 * @param date
	 *            日期
	 * @return String
	 */
	public static String dateDefaultFormat(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 
	 * 功能说明：将指定日期格式化成yyyy-MM-dd格式
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:06:19
	 * @param date
	 *            日期
	 * @return String
	 */
	public static String dateSimpleFormat(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_FORMAT);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 
	 * 功能说明：将指定日期根据自定义格式化
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:08:01
	 * @param date
	 *            日期
	 * @param format
	 *            格式化格式
	 * @return String
	 */
	public static String dateFormat(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 
	 * 功能说明：根据string类型的日期格式化成Date类型(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:10:54
	 * @param date
	 * @return Date
	 */
	public static Date stringToDefaultDateFormat(String date) {
		if (date != null && date.trim().length() == 0) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * 
	 * 功能说明：根据string类型的日期格式化成Date类型(yyyy-MM-dd)
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:10:54
	 * @param date
	 * @return Date
	 */
	public static Date stringToSimpleDateFormat(String date) {
		if (date != null && date.trim().length() == 0) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_FORMAT);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * 
	 * 功能说明：据string类型的日期格式化成指定格式的Date类型
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:13:42
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return Date
	 */
	public static Date stringToDateFormat(String date, String format) {
		if (date != null && date.trim().length() == 0) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_FORMAT);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * 
	 * 功能说明：根据指定日期获取指定日期当天是星期几
	 * 
	 * @author liuli
	 * @createTime 2017年2月27日下午12:04:31
	 * @param date
	 * @return
	 */
	public static String getDayForWeek(Date date) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayForWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayForWeek < 1) {
			return null;
		}
		return weekArray[dayForWeek - 1];
	}

	/**
	 * @author zhongrun 获取指定时间d的前day天00:00:00
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}

	/**
	 * @author zhongrun 获取当月第一天00:00:00
	 * 
	 * @param simpleDateFormat
	 * @return
	 */
	public static String getFirstMonthDay(SimpleDateFormat simpleDateFormat) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		String first = simpleDateFormat.format(c.getTime());

		return first;
	}

	/**
	 * @author gaodawei 获取指定时间d的前day天00:00:00
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getDateBefore(Date d, int day,String style) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		SimpleDateFormat dateFormat = new SimpleDateFormat(style);
		return dateFormat.format(now.getTime());
	}

	/**
	 * @author zhongrun 获取指定时间d的前day天23:59:59
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getDateBeforeEnd(Date d, int day,String style) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		return now.getTime().toString();
	}

	/**
	 * gaodawei
	 * 用于获取日志的准确时间，默认返回的就是当前时间
	 * @param logTime      默认值：System.currentTimeMillis()
	 * @param logDateTime  
	 * @return
	 */
	public static String getLogDataTime(Long logTime,Date logDateTime){
		SimpleDateFormat sdf = new SimpleDateFormat(LOG_FORMAT);
		if(logTime!=null){
			return sdf.format(logTime);
		}else if(logDateTime!=null){
			return sdf.format(logDateTime);
		}else{
			return sdf.format(System.currentTimeMillis());
		}
	}
	/**
	 * 比较时间大小
	 * gaodawei
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int sameDate(Date d1, Date d2){  
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		if(fmt.format(d1).equals(fmt.format(d2))){
			return 0;
		}else if(d1.before(d2)){
			return 1;
		}else{
			return -1;
		}
	}
	/**
	 * gaodawei  
	 * 比较时间大小
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public int compareDate(Date dt1,Date dt2){
		if (dt1.getTime() > dt2.getTime()) {
			return -1;
		} else if (dt1.getTime() < dt2.getTime()) {
			return 1;
		} else {//相等
			return 0;
		}
	}


	/**
	 * date2比date1多的天数
	 * @param date1    
	 * @param date2
	 * @return    
	 */
	public static int differentDays(Date date1,Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if(year1 != year2)   //同一年
		{
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++)
			{
				if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
				{
					timeDistance += 366;
				}
				else    //不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2-day1) ;
		}
		else    //不同年
		{
			System.out.println("判断day2 - day1 : " + (day2-day1));
			return day2-day1;
		}
	}

	/**
	 * 
	 * <获取上个月最后一天的时间>
	 *
	 * @return
	 */
	public static String  getLastDayOfLastMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		//获取前月的最后一天
		Calendar cale = Calendar.getInstance();   
		cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		String lastDay = format.format(cale.getTime());
		System.out.println("lastDay:"+lastDay);
		return lastDay;
	}

	/**
	 * 
	 * <获取去年当天时间>
	 *
	 * @return
	 */
	public static String getfirstDayOfLastYear(Date day){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		//过去一年
		c.setTime(day);
		c.add(Calendar.YEAR, -1);
		Date y = c.getTime();
		String year = format.format(y);
		System.out.println("过去一年："+year);
		return year;
	}

	/**
	 * 获得当前23:59:59点的时间
	 * @return
	 * @throws ParseException
	 */
	public static Date getDayNightTime() throws ParseException{
		SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		return sdf1.parse(getDateBeforeEnd(new Date(), 0, ""));
		
	}
	
	public static void main(String[] args) throws ParseException {
		System.err.println(getDayNightTime());
		System.err.println(System.currentTimeMillis()/60000);
	}
}

