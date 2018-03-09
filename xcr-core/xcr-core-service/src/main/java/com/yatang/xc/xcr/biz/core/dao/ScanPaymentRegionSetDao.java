package com.yatang.xc.xcr.biz.core.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 扫码支持省份
 * Created by wangyang on 2017/12/25.
 */
public interface ScanPaymentRegionSetDao {

    /**
     * 根据省份查询启用扫码付的ID
     *
     * @param provinceId
     * @return
     */
    Integer getIdByProvinceIdForIsShow(@Param("provinceId") String provinceId);
}
