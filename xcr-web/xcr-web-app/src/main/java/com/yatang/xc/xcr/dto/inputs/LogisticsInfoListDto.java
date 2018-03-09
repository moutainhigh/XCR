package com.yatang.xc.xcr.dto.inputs;

import com.yatang.xc.xcr.model.RequestBaseModel;
import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月29日 14:36
 * @Summary : 超级会员订单物流信息
 */
@SuppressWarnings("serial")
public class LogisticsInfoListDto extends RequestBaseModel implements Serializable{

    /**
     * 订单号
     */
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return new StringBuilder("{").append("\"OrderNo\":\"").append(orderNo).append('\"').append('}').toString();
    }
}
