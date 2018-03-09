package com.yatang.xc.xcr.biz.message.common;

/**
 * Created by eson on 2017/6/26.
 */
public interface RpConstants {


    /**
     * 接口HTTP应答码常量
     */
    interface HTTP_CODE {
        String SUCCESS = "200";
    }

    /**
     * mongodb表常量
     */
    interface MONGO_COLL {

        // 商品表名
        String SCAN_PAYMENT = "scan_payment";

        // 扫码交易流水表
        String SCAN_TURNOVER = "scan_turnover";

        // 自增序列表
        String INC_INDEX = "inc_index";

        // 自增序列表key值
        String KEY_INC_INDEX_XCR = "xcr_message";

    }


}
