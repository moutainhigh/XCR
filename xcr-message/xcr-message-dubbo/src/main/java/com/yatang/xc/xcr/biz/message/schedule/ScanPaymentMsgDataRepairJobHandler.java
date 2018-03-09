package com.yatang.xc.xcr.biz.message.schedule;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.yatang.xc.mbd.biz.org.dto.StoreSettlementInfoDto;
import com.yatang.xc.mbd.biz.org.dubboservice.StoreSettlementInfoDubboService;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassMessage;
import com.yatang.xc.xcr.biz.message.service.ScanPaymentMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 收钱码流水无门店编号数据修复任务
 * 由于门店商户号不存在导致出现订单商户号无法查询到门店号问题
 * 1、查询无门店数据
 * 2、获取商户号
 * 3、根据商户号在主数据获取门店号
 * 4、更新流水门店号
 * 5、MQ推送设置为失败以便重推任务能继续将流水同步到数据中心
 * <p>
 * 次为解决数据异常修复,需要主数据先修复商户号
 * Created by wangyang on 2017/12/23.
 */
@JobHander(value = "scanPaymentMsgDataRepairJobHandler")
@Component
public class ScanPaymentMsgDataRepairJobHandler extends IJobHandler {

    @Autowired
    private StoreSettlementInfoDubboService storeSettlementInfoDubboService;
    @Autowired
    private ScanPaymentMessageService scanPaymentMessageService;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        XxlJobLogger.log("【收钱码流水无门店编号数据修复任务】 ...start .");
        List<SwiftPassMessage> swiftPassMessageList = scanPaymentMessageService.getListWithNoShopCode();
        if (CollectionUtils.isEmpty(swiftPassMessageList)) {
            XxlJobLogger.log("目前没有异常数据 ...");
            return ReturnT.SUCCESS;
        }
        for (SwiftPassMessage swiftPassMessage : swiftPassMessageList) {
            String outTradeNo = swiftPassMessage.getOut_trade_no();
            XxlJobLogger.log("正在修复数据 ...outTradeNo：" + outTradeNo + "  开始 -------------------->");
            if (StringUtils.isEmpty(outTradeNo) || outTradeNo.length() < 12) {
                XxlJobLogger.log("该数据有误 -> 订单号错误 ！！！ swiftPassMessage:" + JSONObject.toJSONString(swiftPassMessage));
                XxlJobLogger.log("修复数据 ...outTradeNo：" + outTradeNo + "  结束 -------------------->");
                continue;
            }
            String mchId = outTradeNo.substring(0, 12);
            String shopCode = getShopCodeByMchId(mchId);
            XxlJobLogger.log("根据商户号获取的门店信息 - > mchId:" + mchId + "  shopCode:" + shopCode);
            if (StringUtils.isEmpty(shopCode)) {
                XxlJobLogger.log("根据商户号获取门店编号失败 - > mchId:" + mchId + "  shopCode:" + shopCode);
                XxlJobLogger.log("修复数据 ...outTradeNo：" + outTradeNo + "  结束 -------------------->");
                continue;
            }
            swiftPassMessage.setMch_id(shopCode);
            boolean success = scanPaymentMessageService.updateSwiftPassMessageForHaveNoShopCode(swiftPassMessage);
            if (!success) {
                XxlJobLogger.log("更新门店编号失败 - > mchId:" + mchId + "  shopCode:" + shopCode);
            }
            XxlJobLogger.log("数据修复成功 -> " + mchId + "  shopCode:" + shopCode);
            XxlJobLogger.log("修复数据 ...outTradeNo：" + outTradeNo + "  结束 -------------------->");
        }
        XxlJobLogger.log("【收钱码流水无门店编号数据修复任务】 ...end .");
        return ReturnT.SUCCESS;
    }

    /**
     * 根据商户号获取门店号
     *
     * @param mch_id
     * @return
     */
    private String getShopCodeByMchId(String mch_id) {
        XxlJobLogger.log("根据商户号获取门店编号 -> mch_id:" + mch_id);
        Response<StoreSettlementInfoDto> response = storeSettlementInfoDubboService.queryStoreSettlementInfoByBusinessNumber(mch_id);
        if (response == null || !response.isSuccess()) {
            XxlJobLogger.log("根据商户号获取门店编号 -> 返回结果 -> mch_id:" + mch_id + "  storeSettlementInfoDubboService.queryStoreSettlementInfoByBusinessNumber(mch_id) > response :" + response);
            return null;
        }
        StoreSettlementInfoDto storeSettlementInfoDto = response.getResultObject();
        if (storeSettlementInfoDto == null) {
            XxlJobLogger.log("根据商户号获取门店编号 -> 返回结果 -> mch_id:" + mch_id + "  response.getResultObject() = null !!!");
            return null;
        }
        String storeCode = storeSettlementInfoDto.getStoreCode();
        XxlJobLogger.log("根据商户号获取门店编号 -> 返回结果 -> storeCode:" + storeCode);
        return storeCode;
    }
}
