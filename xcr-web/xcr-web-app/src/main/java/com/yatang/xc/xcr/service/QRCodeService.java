package com.yatang.xc.xcr.service;

/**
 * 
* <获取/生成二维码>
*		
* @author: zhongrun
* @version: 1.0, 2017年10月16日
 */
public interface QRCodeService {

	/**
	 * 
	* <获取门店二维码>
	*
	* @param storeNo
	* @return
	 */
	public String getQRCodeUrl(String storeNo);
	
}
