package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dubboservice.ScanPaymentRegionSetDubboService;
import com.yatang.xc.xcr.biz.core.service.ScanPaymentRegionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangyang on 2017/12/25.
 */
@Service("scanPaymentRegionSetDubboService")
public class ScanPaymentRegionSetDubboServiceImpl implements ScanPaymentRegionSetDubboService {

    @Autowired
    private ScanPaymentRegionSetService scanPaymentRegionSetService;

    @Override
    public Response<Boolean> checkIsShow(String provinceId) {
        if (scanPaymentRegionSetService.checkIsShow(provinceId)) {
            return new Response<>(true, true);
        }
        return new Response<>(false, false);
    }
}
