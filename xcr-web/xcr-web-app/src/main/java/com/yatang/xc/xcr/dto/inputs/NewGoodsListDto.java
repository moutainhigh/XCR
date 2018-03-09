package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @since 2.6
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 15:16
 * @Summary : 商品列表
 */
@SuppressWarnings("serial")
public class NewGoodsListDto implements Serializable{

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
     * 为空查询所有，-1查询无分类数据
      *分类Id
      */
    private String classifyId;

    /**
     *分类名称
     */
    private String classifyName;

    /**
     *搜索内容
     */
    private String search;

    /**
     *页数
     */
    private Integer pageIndex;

    /**
     *每页20条
     * 每页条数
     */
    private Integer pageSize;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"userId\":\"").append(userId).append('\"');
        sb.append(",\"storeSerialNo\":\"").append(storeSerialNo).append('\"');
        sb.append(",\"token\":\"").append(token).append('\"');
        sb.append(",\"classifyId\":\"").append(classifyId).append('\"');
        sb.append(",\"classifyName\":\"").append(classifyName).append('\"');
        sb.append(",\"search\":\"").append(search).append('\"');
        sb.append(",\"pageIndex\":").append(pageIndex);
        sb.append(",\"pageSize\":\"").append(pageSize).append('\"');
        sb.append('}');
        return sb.toString();
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

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
