package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;

/**
 * 扫码流水
 * Created by wangyang on 2017/9/26.
 */
public class SwiftPassMessage implements Serializable {

    private static final long serialVersionUID = -868888718762922428L;

    private String out_trade_no; //商户订单号,
    private int total_fee;       //总金额，
    private String mch_id;      //商户号，
    private String openid;      //用户标识,
    private String trade_type;  //交易类型,pay.weixin.jspay
    private String time_end;     //支付完成时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
    private int haveShopCode;    //是否有门店编号
    private String merchants_id; //商户号,存库用

    private String createTime;   //消息插入时间
    private String pushSendTime; //消息发送时间

    private String sendDcMqResult; //0:成功 1：失败

    public String getMerchants_id() {
        return merchants_id;
    }

    public void setMerchants_id(String merchants_id) {
        this.merchants_id = merchants_id;
    }

    public String getSendDcMqResult() {
        return sendDcMqResult;
    }

    public void setSendDcMqResult(String sendDcMqResult) {
        this.sendDcMqResult = sendDcMqResult;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPushSendTime() {
        return pushSendTime;
    }

    public void setPushSendTime(String pushSendTime) {
        this.pushSendTime = pushSendTime;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public int getHaveShopCode() {
        return haveShopCode;
    }

    public void setHaveShopCode(int haveShopCode) {
        this.haveShopCode = haveShopCode;
    }


    @Override
    public String toString() {
        return "SwiftPassMessage{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", total_fee=" + total_fee +
                ", mch_id='" + mch_id + '\'' +
                ", openid='" + openid + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", time_end='" + time_end + '\'' +
                ", haveShopCode=" + haveShopCode +
                '}';
    }
}
