package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;

/**
 * 扫码支付流水查询DTO
 * Created by wangyang on 2017/10/12.
 */
public class SwiftPassMessageQuery implements Serializable {

    private static final long serialVersionUID = -528234602270203538L;

    private String mch_id;
    private String trade_type;
    private String time_end;

    private int pageNum;
    private int pageSize;

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
