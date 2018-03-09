package com.yatang.xc.xcr.biz.mission.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 统计项目类型枚举
 * @author : zhaokun
 * @date : 2017年7月10日 下午5:39:59  
 * @version : 2017年7月10日  zhaokun 
 */
public enum EnumStatisticsItem {
	STATISTICS_NEED_IMPORT_GOODS("NEED_IMPORT", "需导入商品次数"),
	STATISTICS_IMPORTED_GOODS("IMPORTED", "导入成功次数"),
	STATISTICS_NEED_ADD_GOODS("NEED_ADD", "需新增商品次数"),
	STATISTICS_ADDED_GOODS("ADDED", "新增成功次数"),
	STATISTICS_QUERY_GOODS_DETAIL("QUERY", "扫码/输码成功次数"),
	STATISTICS_ADJUST_PRICE("ADJUST_PRICE", "调价成功次数"),
	STATISTICS_ADD_STOCK("ADD_STOCK", "商品入库成功次数"),
	STATISTICS_FINISH_CLASS("FINISH_CLASS", "完成课堂提交次数");
    

    private String code;

    private String message;


    EnumStatisticsItem(String code,String message){
        this.code = code;
        this.message = message;
    }

    public static Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String,String>();
        for(EnumStatisticsItem item:values()){
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
