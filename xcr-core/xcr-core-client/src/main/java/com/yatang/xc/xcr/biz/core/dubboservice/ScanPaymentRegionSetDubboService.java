package com.yatang.xc.xcr.biz.core.dubboservice;

import com.busi.common.resp.Response;

/**
 * 扫码支付支持地区设置
 * Created by wangyang on 2017/12/25.
 */
public interface ScanPaymentRegionSetDubboService {


    /**
     * 判断省份编号是否启用
     *
     * @param provinceId
     * @return
     */
    Response<Boolean> checkIsShow(String provinceId);
}
