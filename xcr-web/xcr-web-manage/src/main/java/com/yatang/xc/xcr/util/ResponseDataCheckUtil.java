package com.yatang.xc.xcr.util;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;

public class ResponseDataCheckUtil {
	
	public static void showResponseData(Response result){
		System.out.println("\n\n\n");
		System.err.println("===============================以下是调用dubbo返回的数据sta==================================");
		System.err.println("返回的OObject是：    "+JSONObject.toJSONString(result.getResultObject()));
		System.err.println("返回的getCode是：    "+result.getCode());
		System.err.println("返回的ErrorMg是：    "+result.getErrorMessage());
		System.err.println("返回的PageNum是：    "+result.getPageNum());
		System.err.println("返回的IsSuccs是：    "+result.isSuccess());
		System.err.println("===============================以上是调用dubbo返回的数据end==================================");
	}


}
