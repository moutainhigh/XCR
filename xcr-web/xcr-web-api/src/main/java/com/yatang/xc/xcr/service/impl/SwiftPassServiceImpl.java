package com.yatang.xc.xcr.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yatang.xc.mbd.biz.org.dto.StoreSettlementInfoDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganzitionXCRBusinessDubboService;
import com.yatang.xc.mbd.biz.org.dubboservice.StoreSettlementInfoDubboService;
import com.yatang.xc.oles.biz.business.dto.PaymentBackDTO;
import com.yatang.xc.oles.biz.business.dubboservice.ContractVerificationDubboService;
import com.yatang.xc.xcr.biz.message.dto.SwiftPassReturnDto;
import com.yatang.xc.xcr.biz.message.dubboservice.SwiftPassDubboService;
import com.yatang.xc.xcr.biz.pay.dto.DepositPayRecordDto;
import com.yatang.xc.xcr.biz.pay.dto.PayReturnMessageDto;
import com.yatang.xc.xcr.biz.pay.dubboservice.SwiftPassPayDubboService;
import com.yatang.xc.xcr.biz.pay.enums.PayRecordState;
import com.yatang.xc.xcr.enums.DateEnum;
import com.yatang.xc.xcr.service.SwiftPassService;
import com.yatang.xc.xcr.utils.SignUtils;
import com.yatang.xc.xcr.utils.XmlUtils;
import com.yatang.xc.xcr.vo.SwiftPassMessage;
import com.yatang.xc.xcr.vo.SwiftPassReturn;
import com.yatang.xc.xcr.web.action.SwiftPassAction;
import com.yatang.xc.xcr.web.aop.Cacheable;

/**
 * 威富通service实现
 * Created by wangyang on 2017/10/11.
 */

@Service
public class SwiftPassServiceImpl implements SwiftPassService {

    private static Logger log = LoggerFactory.getLogger(SwiftPassAction.class);
    /******************常量*******************/
    /**
     * 支付回调异常返回
     */
    private final static String FAIL = "fail";
    /**
     * 支付回调，威富通请求回调参数签名信息的Key
     */
    private final static String SIGN_KEY = "sign";
    /**
     * 支付回调成功返回
     */
    private final static String SUCCESS = "success";
    /**
     * 支付回调，威富通支付成功响应标识
     */
    private final static String PAY_SUCCESS = "0";
    /**
     * 支付回调，xcr-pay-client Dubbo对外服务成功返回信息
     */
    private final static String DUBBO_SERVICE_PAY_SUCCESS_COMENT = "操作成功";
    /**
     * 支付类型 微信
     */
    private final static String WECHAT_PAY = "wxpay";
    /**
     * 支付类型 支付宝
     */
    private final static String ALI_PAY = "alipay";

    @Autowired
    private SwiftPassDubboService swiftPassDubboService;
    @Autowired
    private SwiftPassPayDubboService swiftPassPayDubboService;
    @Autowired
    private StoreSettlementInfoDubboService storeSettlementInfoDubboService;
    @Autowired
    private OrganzitionXCRBusinessDubboService organzitionXCRBusinessDubboService;
    @Autowired
    private ContractVerificationDubboService contractVerificationDubboService;

    /**
     * 根据商户号获取门店编号,缓存4小时
     *
     * @param mch_id
     * @return
     */
//	@Cacheable(category = "getShopCodeByMchId", key = "#{mch_id}", expire = 4, dateType = DateEnum.HOURS)
    @Override
    public String getShopCodeByMchId(String mch_id) {
        log.info("根据商户号获取门店编号 -> mch_id:" + mch_id);
        Response<StoreSettlementInfoDto> response = storeSettlementInfoDubboService.queryStoreSettlementInfoByBusinessNumber(mch_id);
        if (response == null || !response.isSuccess()) {
            log.error("根据商户号获取门店编号 -> 返回结果 -> mch_id:" + mch_id + "  storeSettlementInfoDubboService.queryStoreSettlementInfoByBusinessNumber(mch_id) > response :" + response);
            return null;
        }
        StoreSettlementInfoDto storeSettlementInfoDto = response.getResultObject();
        if (storeSettlementInfoDto == null) {
            log.error("根据商户号获取门店编号 -> 返回结果 -> mch_id:" + mch_id + "  response.getResultObject() = null !!!");
            return null;
        }
        String storeCode = storeSettlementInfoDto.getStoreCode();
        log.info("根据商户号获取门店编号 -> 返回结果 -> storeCode:" + storeCode);
        return storeCode;
    }

    /**
     * 存流水发消息
     *
     * @param swiftPassReturn
     * @return
     */
    @Override
    public Response<String> saveStreamAndSendMessage(SwiftPassReturn swiftPassReturn) {
        try {
            SwiftPassReturnDto swiftPassReturnDto = new SwiftPassReturnDto();
            BeanUtils.copyProperties(swiftPassReturnDto, swiftPassReturn);
            log.info("swiftPassDubboService.saveStreamAndSendMessage(swiftPassReturnDto) -> 传入参数 -> swiftPassReturnDto:" + JSONObject.toJSONString(swiftPassReturnDto));
            return swiftPassDubboService.saveStreamAndSendMessage(swiftPassReturnDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, e.getMessage());
        }
    }


    /**
     * 威富通支付回调接口
     *
     * @param prams 入参XML数据流
     * @return {@link SwiftPassServiceImpl#FAIL} </br>
     * {@link SwiftPassServiceImpl#SUCCESS}
     * <p>
     * <pre style="color:gray;font-size:14px;">
     * // 通信状态 和 支付状态都必须是成功的
     * if(PAY_SUCCESS.equals(pram.getStatus()) &&
     * PAY_SUCCESS.equals(pram.getResult_code())){
     * getStatus = 通信状态/通信标识
     * getResult_code = 支付结果标识
     * }
     * </pre>
     * <p>
     * [详情]
     * <a href="https://open.swiftpass.cn/openapi/doc?index_1=4&index_2=1&chapter_1=516&chapter_2=518" style="color:red;font-size:14px;">支付回调官方API文档</a>
     */
    @Override
    public String swiftPassCompleteCallback(String prams) {
        log.info("The swift callback prams ==> " + prams);

        try {
            Map<String, String> map = XmlUtils.toMap(prams.getBytes(), "utf-8");
            if (map.containsKey(SIGN_KEY)) {
                /**将MAP解析转换成JSON再解析转换成对象*/
                SwiftPassMessage pram = new Gson().fromJson(JSON.toJSONString(map), SwiftPassMessage.class);
                /**查询XML验证密钥*/
                Response<String> keyDto = organzitionXCRBusinessDubboService.queryCompanyBusinessNumber(pram.getMch_id());
                log.info("Call Remote Method {OrganzitionXCRBusinessDubboService.queryCompanyBusinessNumber(Mch_id:" + pram.getMch_id() + ")} ,the result ==>" + JSON.toJSONString(keyDto));

                if (keyDto != null && keyDto.isSuccess()) {
                    /**校验XML签名信息是否匹配*/
                    if (SignUtils.checkParam(map, keyDto.getResultObject())) {

                        /**根据商户订单好获取履历*/
                        Response<DepositPayRecordDto> rep = swiftPassPayDubboService.getPayRecordByOutTradeNo(pram.getOut_trade_no());
                        log.info("Call Remote Method {SwiftPassPayDubboService.getPayRecordByOutTradeNo(Out_trade_no: " + pram.getOut_trade_no() + ")}, the result ==> " + JSON.toJSONString(rep));
                        if (rep != null && rep.isSuccess()) {
                            DepositPayRecordDto info = rep.getResultObject();

                            PaymentBackDTO payCallback = new PaymentBackDTO();
                            double amount = Double.valueOf(new DecimalFormat("0.00").format((double) pram.getTotal_fee() / 100));
                            payCallback.setAmount(amount);                                    /**支付金额*/
                            payCallback.setContractId(info.getContractNo());/**合同编号*/
                            payCallback.setMchId(pram.getMch_id());            /**支付目标商户号*/
                            payCallback.setShopNo(info.getShopCode());            /**小超编号*/
                            payCallback.setPayType(WECHAT_PAY);

                            PayReturnMessageDto payStat = new PayReturnMessageDto();
                            payStat.setMchId(pram.getMch_id());/**商户号*/
                            payStat.setOutTradeNo(pram.getOut_trade_no());/**订单编号（商户订单号）*/
                            payStat.setTimeEnd(new SimpleDateFormat("yyyyMMddHHmmss").parse(pram.getTime_end()));/**支付完成时间*/

                            /**支付结果*/
                            if (PAY_SUCCESS.equals(pram.getStatus()) && PAY_SUCCESS.equals(pram.getResult_code())) {
                                payStat.setPayState(PayRecordState.PAY_SUCCESS);/**支付状态*/
                                payCallback.setPayResult(true);/**支付结果*/
                            } else {
                                payStat.setPayState(PayRecordState.PAY_FAIL);
                                payCallback.setPayResult(false);
                            }

                            /** 查询订单状态*/
                            Response<Integer> stateByOutTradeNo = swiftPassPayDubboService.getStateByOutTradeNo(pram.getOut_trade_no());
                            log.info("Call Remote Method {SwiftPassPayDubboService.getStateByOutTradeNo(Out_trade_no: " + pram.getOut_trade_no() + ")}, the result ==> " + JSON.toJSONString(stateByOutTradeNo));
                            if (stateByOutTradeNo != null && stateByOutTradeNo.isSuccess()) {
                                int payState = stateByOutTradeNo.getResultObject();
                                /**因为支付成功和失败的状态有两个地方可以改，一个是此回调，一个是王洋那边的补偿*/
                                if (payState != PayRecordState.PAY_SUCCESS.getState() && payState != PayRecordState.PAY_FAIL.getState()) {

                                    /**修改支付履历状态*/
                                    Response<String> response = swiftPassPayDubboService.updatePayState(payStat);
                                    if (response == null || !response.isSuccess() || !DUBBO_SERVICE_PAY_SUCCESS_COMENT.equals(response.getResultObject())) {
                                        log.error("The Remote Method {SwiftPassPayDubboService.updatePayState(...)} execute fail ,The prams ==> " + payStat);
                                    } else {
                                        log.info("Call Remote Method {SwiftPassPayDubboService.updatePayState(PayReturnMessageDto: " + payStat + ")}, the result ==> " + JSON.toJSONString(response));
                                    }
                                }
                            }

                            /**支付信息回写*/
                            Response<Boolean> paymentCallBack = contractVerificationDubboService.paymentCallBack(payCallback);
                            if (paymentCallBack == null || !paymentCallBack.isSuccess()) {
                                log.error("The Remote Method {ContractVerificationDubboService.paymentCallBack(...)} execute fail ,The prams ==> " + payCallback);
                            } else {
                                log.info("Call Remote Method {ContractVerificationDubboService.paymentCallBack(PaymentBackDTO: " + payCallback + ")}, the result ==> " + JSON.toJSONString(paymentCallBack));
                            }

                            return SUCCESS;
                        }
                    }
                }
            }
        } catch (JsonSyntaxException e) {
            log.error("Swiftpass callback JsonSyntaxException: " + ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.error("Swiftpass callback Exception: " + ExceptionUtils.getStackTrace(e));
        }
        return FAIL;
    }

}
