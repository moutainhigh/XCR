package com.yatang.xc.xcr.biz.pay.dao;

import com.yatang.xc.xcr.biz.pay.domain.DepositPayRecordPO;
import org.apache.ibatis.annotations.Param;

/**
 * 威富通支付
 * Created by wangyang on 2017/11/3.
 */
public interface DepositPayRecordDao {

    /**
     * 插入履历
     *
     * @return
     */
    Integer insert(DepositPayRecordPO depositPayRecordPO);

    /**
     * 更新支付状态
     *
     * @return
     */
    Integer updateState(@Param("mchId") String mchId, @Param("outTradeNo") String outTradeNo, @Param("payResult") int payResult);

    /**
     * 更具商户订单号获取订单状态
     *
     * @param outTradeNo
     * @return
     */
    Integer getStateByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    /**
     * 根据合同编号和商户号获取已支付的履历ID
     *
     * @param contractId
     * @param mchId
     * @return
     */
    Integer getSuccessIdBycontractId(@Param("contractId") String contractId, @Param("mchId") String mchId);

    /**
     * 根据合同编号获取支付履历
     *
     * @param outTradeNo
     * @return
     */
    DepositPayRecordPO getPayRecordByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    /**
     * 根据tokenid获取订单状态
     *
     * @param tokenId
     * @return
     */
    DepositPayRecordPO getPayRecordByTokenId(@Param("tokenId") String tokenId);
}
