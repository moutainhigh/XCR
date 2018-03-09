package com.yatang.xc.xcr.biz.pay.Dic;

/**
 * 支付异常字典
 * Created by wangyang on 2017/11/17.
 */
public class PayDic {

    /**
     * web传入参数校验
     */
    public static final String PAY_PARAM_ISNULL = "小超编号，商家电话异常，请检查";
    public static final String PAY_PARAM_AMOUNT_ERROR = "传入金额异常";
    public static final String PAY_PARAM_PHONE_ERROR = "没有获取到商家电话";
    public static final String PAY_PARAM_SHOPCODE_ERROR = "没有获取到小超编号";
    public static final String PAY_PARAM_IP_ERROR = "没有获取到订单ip地址";

    /**
     * 商家状态校验
     */
    public static final String MERCHANT_STATUS_ISNULL = "没有获取到商家的支付状态";
    public static final String ERCHANT_PAY_STATUS_ERROR = "商家支付状态异常";
    public static final String ERCHANT_STATUS_NOTPAY = "商家不是待支付状态";

    /**
     * 支付信息校验
     */
    public static final String GET_PAYMENT_INFO_ERROR = "获取支付信息异常";
    public static final String PAYMENT_INFO_ISNULL = "没有获取到支付信息";
    public static final String PAYMENT_INFO_ERROR = "支付信息异常";
    public static final String PAYMENT_INFO_MARKID_ISNULL = "合同编号不存在";
    public static final String PAYMENT_INFO_MCHID_ISNULL = "商户号不存在";
    public static final String PAYMENT_INFO_PAYAMOUNT_ERROR = "应付金额异常";
    public static final String PAYMENT_INFO_AMOUNT_ERROR = "合同金额与选择金额不匹配";

    /**
     * 重复支付校验
     */
    public static final String PAY_RECORD_REPEAT = "无需重复支付保证金";
    public static final String PAY_KEY_ERROR = "获取支付秘钥失败";

    /**
     * 签名校验
     */
    public static final String SIGN_ERROR = "验证签名不通过";

    /**
     * 系统异常
     */
    public static final String SYSTEM_ERROR = "系统异常，支付失败";


}
