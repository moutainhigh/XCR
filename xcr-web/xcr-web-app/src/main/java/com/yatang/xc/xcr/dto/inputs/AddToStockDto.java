package com.yatang.xc.xcr.dto.inputs;

import java.io.Serializable;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月04日 10:44
 * @Summary :
 "UnitName": "个",
"GoodsCode": "6924221801120",
"UserId": "jms_902004",
"GoodsName": "iOS1031",
"StockPrice": "25.00",
"StoreSerialNo": "A902004",
"ClassifyName": "无分类",
"Num": "5",
"GoodsId": "5CD659E4EFD5F986E050007F0100198D",
"ClassifyId": "01",
"GoodsCostPrice": "5",
"Token": "72fa1373-bd5b-4a9b-bbc6-aaa5a3e08f43"
 */
public class AddToStockDto implements Serializable{

    private String unitName;
    private String goodsCode;
    private String userId;
    private String goodsName;
    private String stockPrice;
    private String storeSerialNo;
    private String classifyName;
    private String num;
    private String goodsId;
    private String classifyId;
    private String goodsCostPrice;
    private String token;

    @Override
    public String toString() {
        return "AddToStockDto{" +
                "unitName='" + unitName + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", userId='" + userId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", stockPrice='" + stockPrice + '\'' +
                ", storeSerialNo='" + storeSerialNo + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", num='" + num + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", classifyId='" + classifyId + '\'' +
                ", goodsCostPrice='" + goodsCostPrice + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getStoreSerialNo() {
        return storeSerialNo;
    }

    public void setStoreSerialNo(String storeSerialNo) {
        this.storeSerialNo = storeSerialNo;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getGoodsCostPrice() {
        return goodsCostPrice;
    }

    public void setGoodsCostPrice(String goodsCostPrice) {
        this.goodsCostPrice = goodsCostPrice;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


