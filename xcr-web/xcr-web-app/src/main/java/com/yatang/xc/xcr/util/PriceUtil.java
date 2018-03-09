package com.yatang.xc.xcr.util;

import java.text.DecimalFormat;

/**
 * 
* <价格>
*		
* @author: zhongrun
* @version: 1.0, 2017年9月11日
 */
public class PriceUtil {
	
	/**
	 * 
	* <保留两位小数>
	*
	* @param money
	* @return
	 */
	public static Double formatPrice(double money){
		DecimalFormat df = new DecimalFormat(".##");
         String money0 = df.format(money);
         return Double.parseDouble(money0);
	}

}
