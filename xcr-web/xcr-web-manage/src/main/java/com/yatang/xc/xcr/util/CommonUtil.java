package com.yatang.xc.xcr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.util.DateUtils;

public class CommonUtil {

    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
    private static int LIMIT_LENTH = 2000;

    public static String getDateString() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取指定日期字符串
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 根据返回条数获取当前页
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static Integer getPageNum(Integer pageIndex, Integer pageSize) {
        return pageSize == 10 ? ((pageIndex + 11) / 10) : pageSize == 25 ?
                ((pageIndex + 26) / 25) : pageSize == 50 ? ((pageIndex + 51)) / 50 : pageSize == 100 ? ((pageIndex + 101) / 100) : 1;
    }


    /**
     * Iframe标签src添加http:,给标签添加id属性
     *
     * @param content
     * @return
     */
    public static String replaceIframe(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        return content.replace("src='//", "id='video' src='http://")
                .replace("width=\"510\"", "width='100%'").replace("width='510'", "width='100%'")
                .replace("height=\"498\"", "height='200'").replace("height='498'", "height='200'");
    }


    /**
     * gaodawei
     *
     * @param method
     * @param param
     * @param location ,0代表调用方法执行开始,1调用方法执行结束
     */
    public static void LogRecond(String methodName, String param, int location, long logTime) {
        if (param != null && param.length() > LIMIT_LENTH) {
            param = "数据太长! 长度为:" + param.length() + " 参数:" + param.substring(0, LIMIT_LENTH / 2);
        }
        if (location == 0) {
            log.info("\n**********于时间" + DateUtils.getLogDataTime(logTime, null) + " 接口 " + methodName + " 开始执行"
                    + "\n**********请求参数为:" + param);

        } else if (location == 1) {
            log.info("\n**********于时间：" + DateUtils.getLogDataTime(logTime, null) + " 接口" + methodName + " 结束请求"
                    + "\n**********响应数据是：" + param
                    + "\n**********花费时间是：" + CommonUtil.costTime(logTime));
        }
    }

    /**
     * gaodawei
     * 返回请求接口花费时间
     *
     * @param startTime
     * @return
     */
    public static String costTime(Long startTime) {
        return Double.valueOf(System.currentTimeMillis() - startTime) / 1000 + "s";
    }

    /**
     * 返回标准格式错误信息值
     *
     * @param code
     * @param errorMsg
     * @param flag
     * @return
     */
    public static JSONObject getResultData(String code, String errorMsg, boolean flag) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("errorMsg", errorMsg);
        json.put("isSuccess", flag);
        return json;
    }

    /**
     * 数字转中文
     *
     * @param intInput
     * @return
     */
    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += ToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

    /**
     * 判断两个时间段是否有交集
     *
     * @param leftStartDate
     * @param leftEndDate
     * @param rightStartDate
     * @param rightEndDate
     * @return
     */
    public static boolean isOverlap(Date leftStartDate, Date leftEndDate, Date rightStartDate, Date rightEndDate) {
        return ((leftStartDate.getTime() >= rightStartDate.getTime())
                && leftStartDate.getTime() < rightEndDate.getTime())
                ||
                ((leftStartDate.getTime() > rightStartDate.getTime())
                        && leftStartDate.getTime() <= rightEndDate.getTime())
                ||
                ((rightStartDate.getTime() >= leftStartDate.getTime())
                        && rightStartDate.getTime() < leftEndDate.getTime())
                ||
                ((rightStartDate.getTime() > leftStartDate.getTime())
                        && rightStartDate.getTime() <= leftEndDate.getTime());

    }


    public static String generateHTMLString(StringBuilder sb, String body) {
        StringBuilder pageHeadBuilder = new StringBuilder();
        pageHeadBuilder
                .append("<!DOCTYPE html><html><head><meta charset=\"utf-8\"/><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" />")
                .append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />")
                .append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />")
                .append("<meta name=\"format-detection\" content=\"telephone=no\" />" + "<title>标题</title>")
                .append("<script src=\"http://cdn.static.runoob.com/libs/jquery/1.10.2/jquery.min.js\"></script>")
                .append(" <style type=\"text/css\"> * { color: #676767; font-family: \"microsoft yahei\"; }")
                .append(" p{ text-align: justify; line-height: 26px; margin: 0 20px;overflow-wrap: break-word; }")
                .append(" p a,p span{ text-align: justify; line-height: 26px;word-wrap: break-word;}")
                .append(" img,p>span img {width: 100%!important;height: auto!important;; margin: 0 auto;}")
                .append(" video {max-width: 100%;height: auto;}" + " @media screen and (max-width: 479px){")
                .append(" body{font-size: 12px;}}" + " @media screen and (min-width: 480px) and (max-width: 767px){")
                .append(" body{font-size: 14px;}}" + " @media screen and (min-width: 768px) and (max-width: 959px){")
                .append(" body{font-size: 16px;}}" + " @media screen and (min-width: 960px) and (max-width: 1199px){")
                .append(" body{font-size: 18px;}}" + " @media screen and (min-width:1200px){body{font-size: 20px;}}")
                .append(" </style></head><body><div class=\"playvideo\">");

        String pageEnd = "</div></body></html>";

        sb.append(pageHeadBuilder.toString());
        sb.append(body);
        sb.append(pageEnd);
        log.debug("html: " + sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        String day1 = "2017-09-02 16:21:22";
        String day2 = "2017-09-02 16:21:24";
        String day3 = "2017-09-02 16:21:22";
        String day4 = "2017-09-04 16:21:22";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            boolean res = isOverlap(simpleDateFormat.parse(day1), simpleDateFormat.parse(day2), simpleDateFormat.parse(day3), simpleDateFormat.parse(day4));
            System.out.println(res);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
