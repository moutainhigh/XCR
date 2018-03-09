package com.yatang.xc.xcr.biz.core.service.impl;

import com.yatang.xc.xcr.biz.core.dao.ScanPaymentRegionSetDao;
import com.yatang.xc.xcr.biz.core.service.ScanPaymentRegionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 扫码支持省份
 * Created by wangyang on 2017/12/25.
 */
@Service
public class ScanPaymentRegionSetServiceImpl implements ScanPaymentRegionSetService {

    @Autowired
    private ScanPaymentRegionSetDao scanPaymentRegionSetDao;

    @Override
    public boolean checkIsShow(String provinceId) {
        Integer id = scanPaymentRegionSetDao.getIdByProvinceIdForIsShow(provinceId);
        return id != null && id > 0;
    }
}
