package com.yatang.xc.xcr.biz.message.dubboservice;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessageDto;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnDto;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnQuery;

import java.util.List;

/**
 * 威富通扫码回调消息
 * Created by wangyang on 2017/9/25.
 */
public interface SwiftPassDubboService {

    /**
     * 存流水,发MQ消息
     *
     * @param swiftPassReturnDto
     * @return
     */
    Response<String> saveStreamAndSendMessage(SwiftPassReturnDto swiftPassReturnDto);

    /**
     * 分页查询消息流水
     *
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    Response<SwiftPassReturnQuery> getSwiftPassMessageList(String shopCode, int pageNum, int pageSize);

    /**
     * 更新收钱码流水
     *
     * @param swiftPassReturnDto
     * @return
     */
    Response<Boolean> updateSwiftPassMessage(SwiftPassReturnDto swiftPassReturnDto);

}
