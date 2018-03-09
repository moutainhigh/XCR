package com.yatang.xc.xcr.vo;

import java.util.List;

/** 
* @author gaodawei 
* @Date 2017年8月24日 下午11:42:52 
* @version 1.0.0
* @function 
*/
public class CommonListVo<V> {
	private String id;//通用Id
	private String back;//留作备用
	private  List<V> paramList;//需要传递的List参数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBack() {
		return back;
	}
	public void setBack(String back) {
		this.back = back;
	}
	public List<V> getParamList() {
		return paramList;
	}
	public void setParamList(List<V> paramList) {
		this.paramList = paramList;
	}
	@Override
	public String toString() {
		return "CommonListVo [id=" + id + ", back=" + back + ", paramList=" + paramList + "]";
	}
	
}
 