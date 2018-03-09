package com.yatang.xc.xcr.exportUtil;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @描述:Xls下载数据,代表了一个excel表
 * @作者: zby
 * @创建时间: 2017年7月4日
 */
public class XlsData {
	// 导出excel的名字
	private String					workBookName;

	private Map<Class<?>, List<?>>	sheetData;



	public XlsData(String workBookName) {
		this.workBookName = workBookName;
	}



	public void setData(Class<?> clazz, List<?> list) {
		if (null == sheetData) {
			sheetData = new LinkedHashMap<>();
		}
		if (sheetData.containsKey(clazz)) {
			throw new RuntimeException("repeat add sheet!");
		}
		sheetData.put(clazz, list);
	}



	public Map<Class<?>, List<?>> getSheetData() {
		return sheetData;
	}



	public void setSheetData(Map<Class<?>, List<?>> sheetData) {
		this.sheetData = sheetData;
	}



	public String getWorkBookName() {
		return workBookName;
	}



	public void setWorkBookName(String workBookName) {
		this.workBookName = workBookName;
	}
	
}
