package com.yatang.xc.xcr.model;

import java.io.Serializable;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.yatang.xc.xcr.util.InterfacePramsPrivate;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月23日 上午11:16:34
 * @Summary :
 */
@SuppressWarnings("serial")
public class RequestMap implements Serializable {

	public static final String _PAGESIZE = "_pageSize";
	public static final String _PAGENO = "_pageNo";
	public static final String _ORDERBY = "_orderBy";
	private String _orderBy;
	private Boolean _isAsc = Boolean.TRUE;
	private Integer _pageNo = 1;
	private Integer _pageSize = 10;
	private Map<String, Object> params; // 请求参数

	public String getOrderBy() {
		return _orderBy;
	}

	public void setOrderBy(String key) {
		this._orderBy += key;
		if (StringUtils.hasText(key)) {
			this.params.put(_ORDERBY, key + " " + getIsAsc());
		}
	}

	public String getIsAsc() {
		if (this._isAsc) {
			return " ASC ";
		}
		return " DESC ";
	}

	public void setIsAsc(Boolean isAsc) {
		this._isAsc = isAsc;
		setOrderBy(this._orderBy);
	}

	public Integer getPageNo() {
		return _pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this._pageNo = pageNo;
		this.params.put("pageNum", getPageNo());
		this.params.put("pageSize", getPageSize());
	}

	public Integer getPageSize() {
		return _pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this._pageSize = pageSize;
		this.params.put("pageNum", getPageNo());
		this.params.put("pageSize", getPageSize());
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "{\"orderBy\":\"" + _orderBy + "\", \"pageNo\":\"" + _pageNo + "\", \"pageSize\":\"" + _pageSize
				+ "\", \"params\":\"" + params + "\"} ";
	}

	public <T> T convert(Class<T> clz, Map<String, Object> data) {
		return InterfacePramsPrivate.convert(clz, data);
	}

	public <T> T convert(Class<T> clz) {
		return InterfacePramsPrivate.convert(clz, this.getParams());
	}

	public RequestMap(Map<String, Object> params) {
		super();
		this.params = params;
	}

	public RequestMap(String orderBy, Boolean isAsc, Integer pageNo, Integer pageSize, Map<String, Object> params) {
		super();
		this._orderBy = orderBy;
		this._isAsc = isAsc;
		this._pageNo = pageNo == null ? pageSize / pageSize - 1 : pageNo;
		this._pageSize = pageSize == null || pageSize == 0 ? 10 : pageSize;
		this.params = params;
	}

	public RequestMap() {
		super();
	}

}
