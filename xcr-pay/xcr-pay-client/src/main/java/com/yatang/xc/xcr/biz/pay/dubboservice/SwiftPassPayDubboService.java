package com.yatang.xc.xcr.biz.pay.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.pay.dto.DepositPayRecordDto;
import com.yatang.xc.xcr.biz.pay.dto.PayParamDto;
import com.yatang.xc.xcr.biz.pay.dto.PayReturnDto;
import com.yatang.xc.xcr.biz.pay.dto.PayReturnMessageDto;
import com.yatang.xc.xcr.biz.pay.enums.PayRecordState;

/**
 * 威富通支付服务
 * Created by wangyang on 2017/10/26.
 */
public interface SwiftPassPayDubboService {

    /**
     * 支付调用
     *
     * @param dto
     * @return
     */
    Response<PayReturnDto> payment(PayParamDto dto);

    /**
     * 修改支付履历状态
     *
     * @param dto
     * @return
     */
    Response<String> updatePayState(PayReturnMessageDto dto);

    /**
     * 支付结果主动查询，补偿机制
     *
     * @param tokenId
     * @param payState
     * @return
     */
    Response<String> payReturnRestitution(String tokenId, PayRecordState payState);

    /**
     * 根据商户订单好获取履历
     *
     * @param outTradeNo
     * @return
     */
    Response<DepositPayRecordDto> getPayRecordByOutTradeNo(String outTradeNo);

    /**
     * 查询订单状态
     *
     * @param outTradeNo
     * @return
     */
    Response<Integer> getStateByOutTradeNo(String outTradeNo);

}
