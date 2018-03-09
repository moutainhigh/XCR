package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 供应链消息
 * Created by wangyang on 2018/1/4.
 */
public class SupplyPushDTO implements Serializable {

    private static final long serialVersionUID = -4341275209308708169L;

    private String title;     //消息标题
    private String content;   //消息内容
    private Boolean status;   //心愿结果 true：已完成(跳转)  false：已关闭(不跳转)
    private String productDetailUrl;       //跳转链接
    private List<String> storeIds;  //门店集合


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProductDetailUrl() {
        return productDetailUrl;
    }

    public void setProductDetailUrl(String productDetailUrl) {
        this.productDetailUrl = productDetailUrl;
    }

    public List<String> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<String> storeIds) {
        this.storeIds = storeIds;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
