package com.yatang.xc.xcr.biz.message.common;

import java.util.HashMap;
import java.util.Set;

/**
 * service层通用参数
 *
 * @author hank.guo
 * @date 2017-07-18 13:40
 */
public class ServiceParam extends HashMap<String, Object> {

	private static final long serialVersionUID = -625113631978665805L;

	/**
	 * 针对非搜索引擎，需要模糊匹配字段
	 */
	private Set<String> fuzzyFields;

	/**
	 * 针对搜索引擎，不分词完全匹配查询字段
	 */
	private Set<String> preciseFields;



	public void setVersionControl(boolean versionControl) {
		this.versionControl = versionControl;
	}



	public boolean isVersionControl() {
		return versionControl;
	}



	/**
	 * 正对查询是否需要数据版本控制
	 */
	private boolean versionControl = true;



	public Set<String> getFuzzyFields() {
		return fuzzyFields;
	}



	public void setFuzzyFields(Set<String> fuzzyFields) {
		this.fuzzyFields = fuzzyFields;
	}



	public Set<String> getPreciseFields() {
		return preciseFields;
	}



	public void setPreciseFields(Set<String> preciseFields) {
		this.preciseFields = preciseFields;
	}



	public ServiceParam() {
		// 初始容量设置为8，扩容因子设置为1，避免不必要的扩容
		super(16, 1);
	}



	public ServiceParam(String key, Object value) {
		this();
		put(key, value);
	}



	public ServiceParam append(String key, Object value) {
		put(key, value);
		return this;
	}



	public <T> T getValue(Object key) {
		return (T) this.get(key);
	}

}
