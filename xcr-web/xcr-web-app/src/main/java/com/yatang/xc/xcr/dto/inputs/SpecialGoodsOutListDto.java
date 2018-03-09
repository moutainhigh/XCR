package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月01日 15:15
 * @Summary : 外送商品特殊列表
 */
@SuppressWarnings("serial")
public class SpecialGoodsOutListDto implements Serializable{

    /**
     * TOKEN
     */
    private String token;

    /**
     * 用户ID之类的
     */
    private String userId;

    /**
     *店铺编号
     */
    private String storeSerialNo;

    /**
     *特殊类型
     * 0:新品推荐商品，1:超级会员专享商品
     */
    private Integer specialType;

    /**
     *上下架状态
     * 1：上架，0：下架
     */
    private String frameType;

    /**
     *页数
     */
    private Integer pageIndex;

    /**
     *每页条数
     */
    private Integer pageSize;

    @Override
	public String toString() {
		return "{\"token\":\"" + token + "\",\"userId\":\"" + userId + "\",\"storeSerialNo\":\"" + storeSerialNo
				+ "\",\"specialType\":\"" + specialType + "\",\"frameType\":\"" + frameType + "\",\"pageIndex\":\""
				+ pageIndex + "\",\"pageSize\":\"" + pageSize + "\"}  ";
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Integer getSpecialType() {
        return specialType;
    }

    public void setSpecialType(Integer specialType) {
        this.specialType = specialType;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
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
}
