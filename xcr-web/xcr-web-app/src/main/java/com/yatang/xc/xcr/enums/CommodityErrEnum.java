package com.yatang.xc.xcr.enums; 
/** 
* @author gaodawei 
* @Date 2017年12月22日 下午6:49:44 
* @version 1.0.0
* @function 
*/
//7100 主数据接口调用失败
//7101 字段校验不通过
//7102 新增失败 其它原因
public enum CommodityErrEnum {
	
	ERR_7103("7103","称重商品spShortCode不能为空","该条码商品为称重商品，请前往电脑端新增"),
	ERR_7102("7102","商品类型不对","导入失败，商品信息有误"),
	ERR_7101("7101","商品条码信息有误","导入失败，商品信息有误"),;
	
	private String code;
	private String desc;
	private String errTip;
	
	private CommodityErrEnum(String code, String desc,String errTip) {
		this.code = code;
		this.desc = desc;
		this.errTip = errTip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getErrTip() {
		return errTip;
	}

	public void setErrTip(String errTip) {
		this.errTip = errTip;
	}

}
 