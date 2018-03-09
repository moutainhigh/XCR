package com.yatang.xc.xcr.biz.pay.dubboservice.impl;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganzitionXCRBusinessDubboService;
import com.yatang.xc.oles.biz.business.dto.PaymentBackDTO;
import com.yatang.xc.oles.biz.business.dto.PaymentInfoDTO;
import com.yatang.xc.oles.biz.business.dto.StatusDTO;
import com.yatang.xc.oles.biz.business.dubboservice.ContractVerificationDubboService;
import com.yatang.xc.xcr.biz.pay.Dic.PayDic;
import com.yatang.xc.xcr.biz.pay.config.SwiftpassConfig;
import com.yatang.xc.xcr.biz.pay.domain.DepositPayRecordPO;
import com.yatang.xc.xcr.biz.pay.dto.DepositPayRecordDto;
import com.yatang.xc.xcr.biz.pay.dto.PayParamDto;
import com.yatang.xc.xcr.biz.pay.dto.PayReturnDto;
import com.yatang.xc.xcr.biz.pay.dto.PayReturnMessageDto;
import com.yatang.xc.xcr.biz.pay.dubboservice.SwiftPassPayDubboService;
import com.yatang.xc.xcr.biz.pay.enums.MerchantState;
import com.yatang.xc.xcr.biz.pay.enums.PayRecordState;
import com.yatang.xc.xcr.biz.pay.enums.TradeState;
import com.yatang.xc.xcr.biz.pay.service.SwiftPassPayService;
import com.yatang.xc.xcr.biz.pay.util.MD5;
import com.yatang.xc.xcr.biz.pay.util.SignUtils;
import com.yatang.xc.xcr.biz.pay.util.XmlUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 支付接入(威富通)
 * Created by wangyang on 2017/10/26.
 */
@Service("swiftPassPayDubboService")
@Transactional(propagation = Propagation.REQUIRED)
public class SwiftPassPayDubboServiceImpl implements SwiftPassPayDubboService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private SwiftpassConfig swiftpassConfig;
    @Autowired
    private SwiftPassPayService swiftPassPayService;

    @Autowired
    private ContractVerificationDubboService contractVerificationDubboService;
    @Autowired
    private OrganzitionXCRBusinessDubboService ganzitionXCRBusinessDubboService;

    private static final String BODY = "加盟保证金"; //支付商品描述(暂时写死)

    @Override
    public Response<PayReturnDto> payment(PayParamDto dto) {

        log.info("进入swiftpass预下单 ...");
        long startTime = System.currentTimeMillis();
        Response<String> payParamDtoCheckResponse = checkPayParamDto(dto);
        if (!payParamDtoCheckResponse.isSuccess()) {
            log.error(payParamDtoCheckResponse.getErrorMessage() + "  耗时：" + (System.currentTimeMillis() - startTime));
            return new Response<>(false, payParamDtoCheckResponse.getErrorMessage(), "");
        }
        log.info(payParamDtoCheckResponse.getResultObject());

        //商家状态校验
        Response<String> responseMerchantStatus = checkMerchantStatus(dto.getShopNo());
        if (!responseMerchantStatus.isSuccess()) {
            log.info("swiftpass -> app内部支付 -> 预支付 -> " + responseMerchantStatus.getErrorMessage());
            return new Response<>(false, responseMerchantStatus.getErrorMessage(), "");
        }
        log.info("swiftpass -> app内部支付 -> 预支付 -> " + responseMerchantStatus.getResultObject());

        //获取支付信息
        Response<PaymentInfoDTO> paymentInfoDTOResponse = getpaymentInfoDTOResponse(dto.getShopNo());
        if (!paymentInfoDTOResponse.isSuccess()) {
            log.error(paymentInfoDTOResponse.getErrorMessage() + "  耗时：" + (System.currentTimeMillis() - startTime));
            return new Response<>(false, paymentInfoDTOResponse.getErrorMessage(), "");
        }
        PaymentInfoDTO paymentInfoDTO = paymentInfoDTOResponse.getResultObject();
        log.info("swiftpass -> app内部支付 -> 预支付 -> 获取支付信息成功 -> paymentInfoDTO:" + JSONObject.toJSONString(paymentInfoDTO));

        //校验支付信息
        Response<String> checkpaymentResponse = checkpaymentInfo(paymentInfoDTO, dto.getTotalFee());
        if (!checkpaymentResponse.isSuccess()) {
            log.error(checkpaymentResponse.getErrorMessage() + "  耗时：" + (System.currentTimeMillis() - startTime));
            return new Response<>(false, checkpaymentResponse.getErrorMessage(), "");
        }
        log.info(checkpaymentResponse.getResultObject());

        //重复支付校验
        if (checkPayRecord(paymentInfoDTO)) {
            log.info("swiftpass -> app内部支付 -> 预支付 -> 重复支付，该商户该合同已完成支付 -> " + JSONObject.toJSONString(dto));
            return new Response<>(false, PayDic.PAY_RECORD_REPEAT, "");
        }

        //获取支付秘钥
        String key = getKey(paymentInfoDTO.getMchId());
        if (StringUtils.isEmpty(key)) {
            log.error("swiftpass -> app内部支付 -> 预支付 -> 获取该商户的支付秘钥失败 -> key:" + key + "  耗时：" + (System.currentTimeMillis() - startTime));
            return new Response<>(false, PayDic.PAY_KEY_ERROR, "");
        }

        //组装支付参数调用支付url
        SortedMap<String, String> map = getPayParamsMap(paymentInfoDTO, dto, key);
        String reqUrl = swiftpassConfig.getReq_url();
        log.info("swiftpass -> app内部支付 -> 预支付 -> 支付请求地址 -> reqUrl:" + reqUrl);
        log.info("swiftpass -> app内部支付 -> 预支付 -> 支付请求参数 -> reqParams:" + XmlUtils.parseXML(map));
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        String res;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
            httpPost.setEntity(entityParams);
            client = HttpClients.createDefault();
            long startSwiftpassPay = System.currentTimeMillis();
            response = client.execute(httpPost);
            long endSwiftpassPay = System.currentTimeMillis();
            log.info("response = client.execute(httpPost) -> swiftpass接口执行时间：" + (endSwiftpassPay - startSwiftpassPay));

            if (response != null && response.getEntity() != null) {
                Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("swiftpass -> app内部支付 -> 预支付 -> 请求结果 -> res:" + res);

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        log.info("swiftpass -> app内部支付 -> 预支付 -> 验证签名不通过 !!!" + "  耗时：" + (System.currentTimeMillis() - startTime));
                        return new Response<>(false, PayDic.SIGN_ERROR, "");
                    }
                    if ("0".equals(resultMap.get("status"))) {
                        String tokenId = resultMap.get("token_id");//授权码
                        String services = resultMap.get("services");//支持的支付类型,支持的支付类型，多个以“|”连接
                        //存支付履历
                        insertPayRecord(dto, paymentInfoDTO, map, tokenId, PayRecordState.PREPAID_SUCCESS);
                        log.info("swiftpass -> app内部支付 -> 预支付 -> 预支付完成 ->" + "  耗时：" + (System.currentTimeMillis() - startTime));
                        return new Response<>(true, getPayReturnDto(dto, map, tokenId, services));
                    }
                }
            } else {
                insertPayRecord(dto, paymentInfoDTO, map, "", PayRecordState.PREPAID_FAIL);
                log.error("swiftpass -> app内部支付 -> 预支付 -> 操作失败 -> response == null && response.getEntity() == null" + "  耗时：" + (System.currentTimeMillis() - startTime));
                return new Response<>(false, PayDic.SYSTEM_ERROR, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("swiftpass -> app内部支付 -> 预支付 -> 系统异常 -> 耗时：" + (System.currentTimeMillis() - startTime));
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        insertPayRecord(dto, paymentInfoDTO, map, "", PayRecordState.PREPAID_FAIL);
        log.error("swiftpass -> app内部支付 -> 预支付 -> 系统异常 -> 耗时：" + (System.currentTimeMillis() - startTime));
        return new Response<>(false, PayDic.SYSTEM_ERROR, "");
    }

    /**
     * 商家状态校验
     *
     * @param shopNo
     * @return
     */
    private Response<String> checkMerchantStatus(String shopNo) {
        Response<StatusDTO> merchantStatus = contractVerificationDubboService.getMerchantStauts(shopNo);
        if (merchantStatus == null) {
            log.error("商家状态校验 -> 没有获取到支付状态 -> merchantStatus == null");
            return new Response<>(false, PayDic.MERCHANT_STATUS_ISNULL, "");
        }
        if (!merchantStatus.isSuccess()) {
            log.error("商家状态校验 -> 商家状态异常 -> merchantStatus.isSuccess():" + merchantStatus.isSuccess() + "  merchantStatus.getErrorMessage():" + merchantStatus.getErrorMessage());
            return new Response<>(false, PayDic.ERCHANT_PAY_STATUS_ERROR, "");
        }
        StatusDTO statusDTO = merchantStatus.getResultObject();
        if (statusDTO.getCode().equals(MerchantState.CASH_DEPOSIT.getState())) {
            return new Response<>(true, "商家为待支付状态，允许支付");
        }
        log.error("商家状态校验 -> 商家不是待支付状态 -> statusDTO.getCode():" + statusDTO.getCode() + "  statusDTO.getDescription():" + statusDTO.getDescription());
        return new Response<>(false, PayDic.ERCHANT_STATUS_NOTPAY, "");

    }

    /**
     * 重复支付校验
     *
     * @param paymentInfoDTO
     * @return
     */
    private boolean checkPayRecord(PaymentInfoDTO paymentInfoDTO) {
        return swiftPassPayService.getSuccessIdBycontractId(paymentInfoDTO.getMarkId(), paymentInfoDTO.getMchId());
    }

    @Override
    public Response<String> updatePayState(PayReturnMessageDto dto) {
        log.info("in updateSwiftPassPayState -> PayReturnMessageDto -> " + JSONObject.toJSONString(dto));
        if (dto == null) {
            return new Response<>(false, "传入参数有误 -> dto == null !!!");
        }
        return swiftPassPayService.updatePayState(dto.getMchId(), dto.getOutTradeNo(), dto.getPayState().getState()) ?
                new Response<>(true, "操作成功") : new Response<>(false, "操作失败");
    }

    @Override
    public Response<String> payReturnRestitution(String tokenId, PayRecordState payState) {

        if (StringUtils.isEmpty(tokenId) || StringUtils.isEmpty(payState.getStateInfo())) {
            return new Response<>(false, "传入参数有误,tokenId:" + tokenId + "  payState:" + payState);
        }
        //查询订单状态，如果是支付成功或支付失败状态，说明已经异步回调，则不再补偿
        DepositPayRecordPO payRecord = swiftPassPayService.getPayRecordByTokenId(tokenId);
        if (payRecord == null) {
            log.error("没有获取到支付履历 -> tokenId:" + tokenId);
            return new Response<>(false, "没有获取到支付履历>tokenId:" + tokenId, "");
        }
        String mchId = payRecord.getMchId();
        Integer state = payRecord.getState();
        String outTradeNo = payRecord.getOutTradeNo();

        log.info("支付状态补偿 -> payState:" + payState);
        if (state == PayRecordState.PAY_SUCCESS.getState() || state == PayRecordState.PAY_FAIL.getState()) {
            log.info("支付状态已经修改 ... state:" + state);
            return new Response<>(true, "操作成功");
        }
        //获取秘钥
        String key = getKey(payRecord.getMchId());
        SortedMap<String, String> map = getQueryOrderParamsMap(mchId, outTradeNo, key);

        String reqUrl = swiftpassConfig.getReq_url();
        log.info("swiftpass -> app内部支付 -> 查询订单详情 -> 支付请求地址 -> reqUrl:" + reqUrl);
        log.info("swiftpass -> app内部支付 -> 查询订单详情 -> 支付请求参数 -> reqParams:" + XmlUtils.parseXML(map));
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        String res;
        int payRecordState = PayRecordState.PAY_FAIL.getState();
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
            httpPost.setEntity(entityParams);
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if (response != null && response.getEntity() != null) {
                Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("swiftpass -> app内部支付 -> 查询订单详情 -> 请求返回结果 -> res:" + res);
                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        return new Response<>(false, "验证签名不通过", "");
                    }
                    if ("0".equals(resultMap.get("status"))) {
                        if (TradeState.SUCCESS.getState().equals(resultMap.get("trade_state"))) {
                            //查询订单状态，如果已成功则不再修改
                            if (swiftPassPayService.getStateByOutTradeNo(outTradeNo) == PayRecordState.PAY_SUCCESS.getState()) {
                                return new Response<>(true, "操作成功");
                            }
                            payRecordState = PayRecordState.PAY_SUCCESS.getState();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        swiftPassPayService.updatePayState(mchId, outTradeNo, payRecordState);
        callBackCRM(payRecord, payRecordState);
        return new Response<>(true, "操作成功");
    }

    /**
     * 回调CRM，通知支付结果
     *
     * @param payRecord
     * @param payRecordState
     */
    private void callBackCRM(DepositPayRecordPO payRecord, int payRecordState) {
        boolean payResult = false;
        if (payRecordState == PayRecordState.PAY_SUCCESS.getState()) {
            payResult = true;
        }
        PaymentBackDTO dto = new PaymentBackDTO();
        dto.setShopNo(payRecord.getShopCode());
        dto.setContractId(payRecord.getContractNo());
        dto.setAmount(getAmountDouble(payRecord.getTotalFee()));
        dto.setMchId(payRecord.getMchId());
        dto.setPayResult(payResult);
        dto.setPayType("wxpay");
        Response<Boolean> paymentCallBack = contractVerificationDubboService.paymentCallBack(dto);
        if (paymentCallBack == null || !paymentCallBack.isSuccess()) {
            log.error("回调CRM，通知支付结果 -> 异常：paymentCallBack：" + JSONObject.toJSONString(paymentCallBack));
        }
    }

    /**
     * 金额换算
     *
     * @param totalFee
     * @return
     */
    private double getAmountDouble(int totalFee) {
        String amount = new DecimalFormat("0.00").format((double) totalFee / 100);
        return Double.valueOf(amount);
    }

    public static void main(String[] args) {
        int totalFee = 1;
        String amount = new DecimalFormat("0.00").format((double) totalFee / 100);
        System.out.println(Double.valueOf(amount));
    }


    @Override
    public Response<DepositPayRecordDto> getPayRecordByOutTradeNo(String outTradeNo) {
        DepositPayRecordPO depositPayRecordPO = swiftPassPayService.getPayRecordByOutTradeNo(outTradeNo);
        if (depositPayRecordPO == null) {
            return new Response<>(false, "获取数据失败", "");
        }
        DepositPayRecordDto dto = new DepositPayRecordDto();
        try {
            BeanUtils.copyProperties(dto, depositPayRecordPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response<>(true, dto);
    }

    @Override
    public Response<Integer> getStateByOutTradeNo(String outTradeNo) {
        return new Response<>(true, swiftPassPayService.getStateByOutTradeNo(outTradeNo));
    }

    /**
     * 查询订单参数组装
     *
     * @param mchId
     * @param outTradeNo
     * @param key
     * @return
     */
    private SortedMap<String, String> getQueryOrderParamsMap(String mchId, String outTradeNo, String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("service", "unified.trade.query"); //接口类型，固定
        map.put("mch_id", mchId); //商户号
        map.put("out_trade_no", outTradeNo); //商户订单号
        map.put("nonce_str", String.valueOf(new Date().getTime())); //商品描述
        map.put("sign", getSign(map, key));
        return map;
    }

    /**
     * 根据商户号获取支付秘钥
     *
     * @param mchId
     * @return
     */
    private String getKey(String mchId) {
        Response<String> response = ganzitionXCRBusinessDubboService.queryCompanyBusinessNumber(mchId);
        if (response != null && response.isSuccess()) {
            return response.getResultObject();
        }
        return null;
    }

    /**
     * 获取预支付返回值
     *
     * @param dto
     * @param map
     * @param tokenId
     * @param services
     * @return
     */
    private PayReturnDto getPayReturnDto(PayParamDto dto, SortedMap<String, String> map, String tokenId, String services) {
        PayReturnDto payReturnDto = new PayReturnDto();
        payReturnDto.setOutTradeNo(map.get("out_trade_no"));
        payReturnDto.setServices(services);
        payReturnDto.setTokenId(tokenId);
        payReturnDto.setShopNo(dto.getShopNo());
        payReturnDto.setMchId(map.get("mch_id"));
        payReturnDto.setPhone(dto.getPhone());
        return payReturnDto;
    }

    /**
     * 存支付履历
     *
     * @param dto
     * @param map
     * @param tokenId
     * @param prepaidSuccess
     */
    private void insertPayRecord(PayParamDto dto, PaymentInfoDTO paymentInfoDTO, SortedMap<String, String> map, String tokenId, PayRecordState prepaidSuccess) {
        DepositPayRecordPO depositPayRecordDto = new DepositPayRecordPO();
        depositPayRecordDto.setPhone(dto.getPhone());
        depositPayRecordDto.setTokenId(tokenId);
        depositPayRecordDto.setMchId(map.get("mch_id"));
        depositPayRecordDto.setBody(BODY);
        depositPayRecordDto.setContractNo(paymentInfoDTO.getMarkId());
        depositPayRecordDto.setMchCreateIp(map.get("mch_create_ip"));
        depositPayRecordDto.setNotifyUrl(map.get("notify_url"));
        depositPayRecordDto.setOutTradeNo(map.get("out_trade_no"));
        depositPayRecordDto.setShopCode(dto.getShopNo());
        depositPayRecordDto.setTotalFee(dto.getTotalFee());
        depositPayRecordDto.setState(prepaidSuccess.getState());
        depositPayRecordDto.setCreateTime(new Date());
        depositPayRecordDto.setUpdateTime(new Date());
        boolean success = swiftPassPayService.insert(depositPayRecordDto);
        log.info("存支付履历 -> success:" + success + "  no:" + map.get("out_trade_no"));
    }

    /**
     * 校验支付web传入参数
     *
     * @param dto
     * @return
     */
    private Response<String> checkPayParamDto(PayParamDto dto) {
        if (dto == null) {
            log.error("校验支付web传入参数 -> 传入参数为空 -> PayParamDto == null");
            return new Response<>(false, PayDic.PAY_PARAM_ISNULL, "");
        }
        if (dto.getTotalFee() <= 0) {
            log.error("校验支付web传入参数 -> 传入金额异常 -> TotalFee:" + dto.getTotalFee());
            return new Response<>(false, PayDic.PAY_PARAM_AMOUNT_ERROR, "");
        }
        if (StringUtils.isEmpty(dto.getPhone())) {
            log.error("校验支付web传入参数 -> 没有商家电话有误 -> Phone:" + dto.getPhone());
            return new Response<>(false, PayDic.PAY_PARAM_PHONE_ERROR, "");
        }
        if (StringUtils.isEmpty(dto.getShopNo())) {
            log.error("校验支付web传入参数 -> 传入小超编号有误 -> ShopNo:" + dto.getShopNo());
            return new Response<>(false, PayDic.PAY_PARAM_SHOPCODE_ERROR, "");
        }
        if (StringUtils.isEmpty(dto.getMchCreateIp())) {
            log.error("校验支付web传入参数 -> 传入ip地址有误 -> ShopNo:" + dto.getShopNo());
            return new Response<>(false, PayDic.PAY_PARAM_IP_ERROR, "");
        }
        return new Response<>(true, "校验支付web传入参数 -> 校验成功");
    }

    /**
     * 获取并校验支付信息
     *
     * @param shopNo
     * @return
     */
    private Response<PaymentInfoDTO> getpaymentInfoDTOResponse(String shopNo) {
        Response<PaymentInfoDTO> paymentInfoDTOResponse = contractVerificationDubboService.obtainPaymentInfoByXcNumber(shopNo);
        if (paymentInfoDTOResponse == null) {
            log.error("获取并校验支付信息 -> 获取支付信息异常");
            return new Response<>(false, PayDic.GET_PAYMENT_INFO_ERROR, "");
        }
        if (!paymentInfoDTOResponse.isSuccess()) {
            log.error("获取并校验支付信息 -> paymentInfoDTOResponse.isSuccess():" + paymentInfoDTOResponse.isSuccess() + "  paymentInfoDTOResponse.getErrorMessage():" + paymentInfoDTOResponse.getErrorMessage());
            return new Response<>(false, PayDic.PAYMENT_INFO_ERROR, "");
        }
        if (paymentInfoDTOResponse.getResultObject() == null) {
            return new Response<>(false, PayDic.PAYMENT_INFO_ISNULL, "");
        }
        return new Response<>(true, paymentInfoDTOResponse.getResultObject());
    }

    /**
     * 组装支付参数
     *
     * @param paymentInfoDTO
     * @return
     */
    private SortedMap<String, String> getPayParamsMap(PaymentInfoDTO paymentInfoDTO, PayParamDto dto, String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("service", swiftpassConfig.getService()); //接口类型，固定
        map.put("notify_url", swiftpassConfig.getNotify_url());  //回调地址
        map.put("nonce_str", String.valueOf(new Date().getTime()));  //随机字符串
        map.put("mch_id", paymentInfoDTO.getMchId()); //商户号
        map.put("out_trade_no", getOutTradeNo(paymentInfoDTO.getMarkId())); //商户订单号
        map.put("body", BODY); //商品描述
        map.put("total_fee", String.valueOf(dto.getTotalFee()));//总金额
        map.put("mch_create_ip", dto.getMchCreateIp()); //订单生成的机器IP
        map.put("sub_appid", swiftpassConfig.getAppid());
        //获取签名
        map.put("sign", getSign(map, key));
        return map;
    }

    /**
     * 获取签名
     *
     * @param map
     * @param key
     * @return
     */
    private String getSign(SortedMap<String, String> map, String key) {
        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        return MD5.sign(preStr, "&key=" + key, "utf-8");
    }

    /**
     * 获取商户订单号
     * 合同编号_时间撮
     *
     * @param contractId
     * @return
     */
    private String getOutTradeNo(String contractId) {
        return contractId + "_" + new Date().getTime();
    }

    /**
     * 校验支付信息参数
     *
     * @param totalFee
     * @return
     */
    private Response<String> checkpaymentInfo(PaymentInfoDTO paymentInfoDTO, int totalFee) {
        if (StringUtils.isEmpty(paymentInfoDTO.getMarkId())) {
            log.error("校验支付信息参数 -> 合同编号不存在!");
            return new Response<>(false, PayDic.PAYMENT_INFO_MARKID_ISNULL, "");
        }
        if (StringUtils.isEmpty(paymentInfoDTO.getMchId())) {
            log.error("校验支付信息参数 -> 商户号不存在!");
            return new Response<>(false, PayDic.PAYMENT_INFO_MCHID_ISNULL, "");
        }
        if (paymentInfoDTO.getAmount() == null || paymentInfoDTO.getAmount() <= 0) {
            log.error("校验支付信息参数 -> 应付金额异常!amount:" + paymentInfoDTO.getAmount());
            return new Response<>(false, PayDic.PAYMENT_INFO_PAYAMOUNT_ERROR, "");
        }
        Double amount = paymentInfoDTO.getAmount();
        int amountInt = Double.valueOf(amount * 100).intValue();
        if (totalFee != amountInt) {
            log.error("校验支付信息参数 -> 金额不匹配! -> 页面金额：" + totalFee + "  支付信息金额:" + amountInt);
            return new Response<>(false, PayDic.PAYMENT_INFO_AMOUNT_ERROR);
        }
        return new Response<>(true, "支付信息参数 -> 校验成功");

    }

}
