package com.yatang.xc.xcr.biz.pay.service;

import com.yatang.xc.xcr.biz.pay.domain.DepositPayRecordPO;

/**
 * 支付相关
 * Created by wangyang on 2017/11/3.
 */
public interface SwiftPassPayService {

    /**
     * 添加支付履历
     *
     * @param depositPayRecordDto
     * @return
     */
    boolean insert(DepositPayRecordPO depositPayRecordDto);

    /**
     * 修改支付状态
     *
     * @param mchId
     * @param outTradeNo
     * @param payResult
     * @return
     */
    boolean updatePayState(String mchId, String outTradeNo, int payResult);

    /**
     * 根据商户订单号获取订单状态
     *
     * @param outTradeNo
     * @return
     */
    Integer getStateByOutTradeNo(String outTradeNo);

    /**
     * 根据合同编号和商户号判断是否已存在支付成功合同
     *
     * @param contractId
     * @param mchId
     * @return
     */
    boolean getSuccessIdBycontractId(String contractId, String mchId);

    /**
     * 根据合同编号获取支付履历
     *
     * @param outTradeNo
     * @return
     */
    DepositPayRecordPO getPayRecordByOutTradeNo(String outTradeNo);

    /**
     * 根据tokenId去查询订单状态
     *
     * @param tokenId
     * @return
     */
    DepositPayRecordPO getPayRecordByTokenId(String tokenId);
}
