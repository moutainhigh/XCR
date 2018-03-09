package com.yatang.xc.xcr.enums; 
/** 
* @auth gaodawei 
* @Date 2017年11月10日 下午5:50:26 
* @version 1.0.0
* @function 
*/
public enum BankCardErrEnum {
	
	CK_SUC_0("0","验证成功","用户四要素信息验证成功"),
	CK_ERR_1("-1","用户信息出错，一定要联系客服","请求参数origin为null，响应为“系统来源为空”"),
	CK_ERR_2("-2","用户名为空",""),
	CK_ERR_3("-3","用户不存在",""),
	CK_ERR_4("-4","银行卡已添加","已经绑定过银行卡，再次去绑定就会响应“此用户已经添加当前银行卡不容许添加！”"),
	CK_ERR_5("-5","信息验证失败","成功去请求验证，但是信息有误，验证失败了，深圳包装，其实跟ACCOUNTNO_UNABLE_VERIFY，DIFFERENT一个意思"),
	CK_ERR_6("-6","服务太忙了，请稍后重试","C端程序出错等返回，具体返回“验证失败，请联系管理员”"),
	CK_ERR_100001004("100001004","无效的手机号","无效的手机号码"),
	CK_ERR_30000000("30000000","当前绑定银行卡人数过多，建议错开高峰期","COMMON_TEST_INVOKE_TIMES_NOT_ENOUGH,接口请求已达到上限"),
	CK_ERR_30000001("30000001","用户无权限绑定银行卡，请联系客服","COMMON_NO_OPEN_INVOKE_PERMISSION 接口没有开通权限"),
	CK_ERR_30000002("30000002","用户无权限绑定银行卡，请联系客服","COMMON_IP_NO_ACCESS_PERMISSION 该IP没有访问权限"),
	CK_ERR_30000008("30000008","请不要频繁点击","重复请求去绑定银行卡，请稍后重试"),
	CK_ERR_30001001("30001001","持卡人身份证号未通过校验","持卡人的身份证号为空或者不正确或者校验属于不合法卡号"),
	CK_ERR_30001002("30001002","持卡人姓名未通过校验","持卡人姓名为空或者与银行卡对应姓名不匹配"),
	CK_ERR_90001001("90001001","数据有误，验证失败","银行四要素数据源信息异常，代表天行数科那边请求银联数据源出问题了，概率很小，或者2分钟内同一卡号最好不要超过一次，否则可能会被锁定，如果锁定可以在3分钟后再试；一天以内建议不要超过5次"),//服务过于繁忙，请不要紧急调用，请五分钟后再次尝试绑定，如果未绑定成功，请联系客服处理，否则可能此卡无法绑定成功了
	CK_ERR_90001002("90001002","用户信息有误,请核准相关信息,如果确认无误,请联系客服","三要素验证异常"),
	CK_ERR_90001011("90001011","未查到银行卡信息或未开通网银","卡片信息是不常见银行卡信息或者未开通网银等等"),
	CK_ERR_ACCOUNTNO_UNABLE_VERIFY("ACCOUNTNO_UNABLE_VERIFY","校验失败，您的银行卡已不能正常使用或未开通网银","指多指卡的状态有问题，比如卡被吞了、卡受限等；有些偏僻的银行的银行卡可能不支持这种验证"),//请向相关银行确认银行卡能否正常使用，和是否开通网银，如确认无误，请联系客服
	CK_ERR_DIFFERENT("DIFFERENT","校验失败，请确认银行信息是否填写正确","因为是银行卡的验证，所以只要其他信息与银行卡对应的信息不一致，就返回不一致");//请校验你的姓名、身份证号、手机号是否为银行预留手机号以及核实银行已为你录入用户信息，如确认无误，请联系客服
	
	private String ck_code;
	private String ck_errMsg;
	private String ck_desc;
	/**
	 * @param ck_code
	 * @param ck_err
	 */
	private BankCardErrEnum(String ck_code, String ck_errMsg,String ck_desc) {
		this.ck_code = ck_code;
		this.ck_errMsg = ck_errMsg;
		this.ck_desc = ck_desc;
	}
	public String getCk_code() {
		return ck_code;
	}
	public void setCk_code(String ck_code) {
		this.ck_code = ck_code;
	}
	public String getCk_errMsg() {
		return ck_errMsg;
	}
	public void setCk_errMsg(String ck_errMsg) {
		this.ck_errMsg = ck_errMsg;
	}
	public String getCk_desc() {
		return ck_desc;
	}
	public void setCk_desc(String ck_desc) {
		this.ck_desc = ck_desc;
	}
}
 