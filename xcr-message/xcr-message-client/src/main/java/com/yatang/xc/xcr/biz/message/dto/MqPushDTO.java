package com.yatang.xc.xcr.biz.message.dto;

import java.io.Serializable;
import java.util.List;

/**
 * MQ消息接收对象
 * Created by wangyang on 2017/7/4.
 */
public class MqPushDTO implements Serializable {

    private static final long serialVersionUID = 4390031041154503474L;

    private String orderId;  //订单ID
    private String shopCode; //门店编号
    private double amount;   //订单金额

    /**
     * V2.3 新增
     **/
    private String shippingMethod; //配送方式
    private String shippingAddress; //配送地址
    private int type; //消息类型 1订单接单提醒,2蜂鸟配送接单提醒,3用户取消订单提示,4超时未接单提示,5蜂鸟配送完成提醒,6新退货单提醒,7蜂鸟配送拒单提醒（默认1）,8威富通，9待结单
    private List<String> returnProducts; //退货商品名称列表
    private String order_number;  //订单号
    private String cancelId; //退单号,做退货跳转
    private String transId; //威富通Id

    /**
     * V2.4 新增
     */
    private String transContent; //收钱码语音


    public String getTransContent() {
        return transContent;
    }

    public void setTransContent(String transContent) {
        this.transContent = transContent;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getCancelId() {
        return cancelId;
    }

    public void setCancelId(String cancelId) {
        this.cancelId = cancelId;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getReturnProducts() {
        return returnProducts;
    }

    public void setReturnProducts(List<String> returnProducts) {
        this.returnProducts = returnProducts;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MqPushDTO{" +
                "orderId='" + orderId + '\'' +
                ", shopCode='" + shopCode + '\'' +
                ", amount=" + amount +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", type=" + type +
                ", returnProducts=" + returnProducts +
                ", order_number='" + order_number + '\'' +
                ", cancelId='" + cancelId + '\'' +
                '}';
    }
}
