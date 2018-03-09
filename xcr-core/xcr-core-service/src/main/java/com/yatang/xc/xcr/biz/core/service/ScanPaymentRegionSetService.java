package com.yatang.xc.xcr.biz.core.service;

/**
 * 扫码支持省份
 * Created by wangyang on 2017/12/25.
 */
public interface ScanPaymentRegionSetService {

    /**
     * 判断省份编号是否启用扫码付
     *
     * @param provinceId
     * @return
     */
    boolean checkIsShow(String provinceId);
}
