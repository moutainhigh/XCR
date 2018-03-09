package com.yatang.xc.xcr.biz.mission.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 排序类型枚举
 * @author : zhaokun
 * @date : 2017年3月20日 下午5:39:59  
 * @version : 2017年3月20日  zhaokun 
 */
public enum EnumMissionOrderBy {
    TYPE_TIME("last_modify_time", "时间排序"),
    TYPE_TIME_DESC("last_modify_time desc", "时间排序(倒序)"),
    TYPE_NAME("name", "名称排序"),
    TYPE_DEFAULT("sort", "默认排序");

    private String code;

    private String message;


    EnumMissionOrderBy(String code,String message){
        this.code = code;
        this.message = message;
    }

    public static Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String,String>();
        for(EnumMissionOrderBy item:values()){
            map.put(item.getCode(), item.getMessage());
        }
        return map;
    }

    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    
    public String getMessage() {
        return message;
    }


    
    public void setMessage(String message) {
        this.message = message;
    }










}
