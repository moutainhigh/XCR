package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;

/**
 * 审核结果消息
 * Created by wangyang on 2017/11/29.
 */
public class AuditResultDTO implements Serializable {


    private static final long serialVersionUID = -1156557038703437540L;

    private String shopCode;  //门店编号
    private Integer auditType;  //1:店铺内容  2：店铺公告  3:银行卡照片审核
    private Integer result;  //0:审核通过   1：审核不通过
    private String explain;  //结果说明，不通过原因

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getAuditType() {
        return auditType;
    }

    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
