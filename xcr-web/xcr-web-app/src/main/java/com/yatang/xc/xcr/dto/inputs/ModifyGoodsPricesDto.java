package com.yatang.xc.xcr.dto.inputs;

import com.yatang.xc.xcr.annotations.GenericParadigmType;

import java.io.Serializable;
import java.util.List;

/**
 * 商品调价
 * Created by wangyang on 2017/12/21.
 */
public class ModifyGoodsPricesDto implements Serializable {

    private static final long serialVersionUID = 4616701054011039659L;
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
     * 调价商品
     */
    private List<GoodsPricesDetail> goodsList;

    @GenericParadigmType(GoodsPricesDetail.class)
    public List<GoodsPricesDetail> getGoodsList() {
        return goodsList;
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


    public void setGoodsList(List<GoodsPricesDetail> goodsList) {
        this.goodsList = goodsList;
    }
}
