package com.yatang.xc.xcr.biz.message.service;

import java.util.List;

import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessage;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessageQuery;
import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessage;
import com.yatang.xc.xcr.biz.message.dto.ScanPaymentMessageQuery;

@Transactional
public interface ScanPaymentMessageService {

    /**
     * 查询数量
     *
     * @param query
     * @return
     */
    long queryTotalSize(ScanPaymentMessageQuery query);

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    List<ScanPaymentMessage> queryScanPay(ScanPaymentMessageQuery query);

    /**
     * 单个保存
     *
     * @param scanPay
     * @return
     */
    boolean saveScanPaymentMessage(ScanPaymentMessage scanPay);

    /**
     * 批量保存
     *
     * @param ScanPaymentMessages
     */
    void saveScanPaymentMessage(List<ScanPaymentMessage> ScanPaymentMessages);

    /**
     * 保存单个流水
     *
     * @return
     */
    boolean saveSwiftPassMessage(SwiftPassMessage swiftPassMessage);

    /**
     * 更新当个流水
     *
     * @param swiftPassMessage
     * @return
     */
    boolean updateSwiftPassMessage(SwiftPassMessage swiftPassMessage);

    /**
     * 更新当个流水(补充门店编号)
     *
     * @param swiftPassMessage
     * @return
     */
    boolean updateSwiftPassMessageForHaveNoShopCode(SwiftPassMessage swiftPassMessage);

    /**
     * 分页查询扫码支付流水
     *
     * @param swiftPassMessageQuery
     * @return
     */
    List<SwiftPassMessage> getSwiftPassMessageList(SwiftPassMessageQuery swiftPassMessageQuery);

    /**
     * 获取MQ发送失败流水数据
     *
     * @return
     */
    List<SwiftPassMessage> getListWithFailedToSendMQ(String timeStart, String timeEnd);

    /**
     * 查询查询扫码支付流水总数
     *
     * @param query
     * @return
     */
    long querySwiftPassMessageTotalSize(SwiftPassMessageQuery swiftPassMessageQuery);

    /**
     * 查询没有门店b编号的数据
     *
     * @return
     */
    List<SwiftPassMessage> getListWithNoShopCode();

}
