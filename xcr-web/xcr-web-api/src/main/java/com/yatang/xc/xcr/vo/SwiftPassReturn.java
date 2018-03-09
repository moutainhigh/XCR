package com.yatang.xc.xcr.vo;

import java.io.Serializable;

/**
 * 威富通回调参数
 * Created by wangyang on 2017/9/26.
 */
public class SwiftPassReturn implements Serializable {

    private static final long serialVersionUID = -6216160683399517559L;


    private String out_trade_no; //商户订单号,商户系统内部的定单号，32个字符内、可包含字母
    private int total_fee;       //总金额，以分为单位，不允许包含任何字、符号
    private String mch_id;      //商户号，由平台分配
    private String openid;      //用户标识,用户在商户 appid 下的唯一标识
    private String trade_type;  //交易类型,pay.weixin.jspay
    private String time_end;     //支付完成时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
    private int haveShopCode;    //是否有门店编号
    private String merchants_id; //商户号

    public String getMerchants_id() {
        return merchants_id;
    }

    public void setMerchants_id(String merchants_id) {
        this.merchants_id = merchants_id;
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
}
