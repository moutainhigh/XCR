package com.yatang.xc.xcr.biz.pay.service.impl;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.pay.dao.DepositPayRecordDao;
import com.yatang.xc.xcr.biz.pay.domain.DepositPayRecordPO;
import com.yatang.xc.xcr.biz.pay.service.SwiftPassPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付履历
 * Created by wangyang on 2017/11/3.
 */
@Service
public class SwiftPassPayServiceImpl implements SwiftPassPayService {


    @Autowired
    private DepositPayRecordDao depositPayRecordDao;

    @Override
    public boolean insert(DepositPayRecordPO depositPayRecordDto) {
        return depositPayRecordDao.insert(depositPayRecordDto) > 0;
    }

    @Override
    public boolean updatePayState(String mchId, String outTradeNo, int payResult) {
        return depositPayRecordDao.updateState(mchId, outTradeNo, payResult) > 0;
    }

    @Override
    public Integer getStateByOutTradeNo(String outTradeNo) {
        return depositPayRecordDao.getStateByOutTradeNo(outTradeNo);
    }

    @Override
    public boolean getSuccessIdBycontractId(String contractId, String mchId) {
        Integer id = null;
        try {
            id = depositPayRecordDao.getSuccessIdBycontractId(contractId, mchId);
            if (id == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return id > 0;
    }

    @Override
    public DepositPayRecordPO getPayRecordByOutTradeNo(String outTradeNo) {
        return depositPayRecordDao.getPayRecordByOutTradeNo(outTradeNo);
    }

    @Override
    public DepositPayRecordPO getPayRecordByTokenId(String tokenId) {
        return depositPayRecordDao.getPayRecordByTokenId(tokenId);
    }
}
