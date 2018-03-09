package com.yatang.xc.xcr.biz.pay.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付履历
 * Created by wangyang on 2017/11/3.
 */
public class DepositPayRecordPO implements Serializable {

    private static final long serialVersionUID = 7516421465968394008L;
    private Long id;              //主键ID
    private String mchId;         //商户号
    private String shopCode;      //小超编号
    private String phone;         //商户电话
    private String outTradeNo;    //预下单订单号（商户订单号），用"_"拼接合同编号及时间戳
    private String contractNo;    //合同编号
    private String body;          //商品描述
    private Integer totalFee;         //总金额，以分为单位，不允许包含任何字、符号
    private String mchCreateIp;   //终端IP,订单生成的机器 IP
    private String notifyUrl;     //通知地址,接收平台通知的URL
    private String tokenId;       //支付授权码
    private Integer state;            //订单状态：0、预下单，1、待支付，2、预下单失败，3支付成功，4、支付失败
    private Date createTime;      //创建时间
    private Date updateTime;      //更新时间

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMchCreateIp() {
        return mchCreateIp;
    }

    public void setMchCreateIp(String mchCreateIp) {
        this.mchCreateIp = mchCreateIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
