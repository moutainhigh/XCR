package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 11:50
 * @Summary : 
 * 根据上下架状态查询商品：msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","FrameType":"1"}
 * 根据分类id查询商品：msg={"UserId":"jms_902003","StoreSerialNo":"A902003","Token":"1111","PageIndex":"1","PageSize":"5","FrameType":"1","ClassifyId":""}
 * 根据Search属性查询商品：msg={"UserId":"jms_902003","StoreSerialNo":"A902003", "Token":"1111","PageIndex":"1","PageSize":"5","Search":""}
 */
public class GoodsOutListDto implements Serializable {

	private static final long serialVersionUID = 4174604669000018880L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 门店编号
	 */
	private String storeSerialNo;
	/**
	 * TOKEN
	 */
	private String token;

	/**
	 * 页码 pageNumber
	 */
	private Integer pageIndex;

	/**
	 * pageSize 每页显示条数
	 */
	private Integer pageSize;

	/**
	 * 1: 上架，0下架
	 */
	private String frameType;

	/**
	 * @since 2.5.1 
	 * @Summary 筛选出对一个售卖状态下含有新品推荐标签的商品
	 * @value 0不是1是
	 */
	private String isNewArrivals;
	/**
	 * @since 2.5.1 
	 * @Summary 筛选出对一个售卖状态下含有超级会员专享的商品（仅直营店显示）
	 */
	private String isSuperVip;

	/**
	 *搜索内容
	 * */
	private String search;

	/**
	 *分类Id
	 */
	private String classifyId;

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo + "\",\"token\":\"" + token
				+ "\",\"pageIndex\":\"" + pageIndex + "\",\"pageSize\":\"" + pageSize + "\",\"frameType\":\""
				+ frameType + "\",\"isNewArrivals\":\"" + isNewArrivals + "\",\"isSuperVip\":\"" + isSuperVip
				+ "\",\"search\":\"" + search + "\",\"classifyId\":\"" + classifyId + "\"}  ";
	}

	public String getIsNewArrivals() {
		return isNewArrivals;
	}

	public void setIsNewArrivals(String isNewArrivals) {
		this.isNewArrivals = isNewArrivals;
	}

	public String getIsSuperVip() {
		return isSuperVip;
	}

	public void setIsSuperVip(String isSuperVip) {
		this.isSuperVip = isSuperVip;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStoreSerialNo() {
		return storeSerialNo;
	}

	public void setStoreSerialNo(String storeSerialNo) {
		this.storeSerialNo = storeSerialNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFrameType() {
		return frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
