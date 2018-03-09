package com.yatang.xc.xcr.service;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.service.impl.SwiftPassServiceImpl;
import com.yatang.xc.xcr.vo.SwiftPassReturn;

/**
 * 威富通service服务
 * Created by wangyang on 2017/10/11.
 */
public interface SwiftPassService {

    /**
     * 根据商户号获取门店编号
     *
     * @param mch_id
     * @return
     */
    String getShopCodeByMchId(String mch_id);

    /**
     * 存流水发消息
     * @param swiftPassReturn
     * @return
     */
    Response<String> saveStreamAndSendMessage(SwiftPassReturn swiftPassReturn);
    
    
    /**
	 * 威富通支付回调接口
	 * 
	 * @param prams 入参XML数据流
	 * @return 
	 * {@link SwiftPassServiceImpl#FAIL} </br>
	 * {@link SwiftPassServiceImpl#SUCCESS}
	 *  
	 * <pre style="color:gray;font-size:14px;">
	 * // 通信状态 和 支付状态都必须是成功的 
	 * if(PAY_SUCCESS.equals(pram.getStatus()) && 
	 *    PAY_SUCCESS.equals(pram.getResult_code())){
	 *  	 getStatus = 通信状态/通信标识 
	 * 	 getResult_code = 支付结果标识 
	 * }
	 * </pre>
	 * [详情]
	 * <a href="https://open.swiftpass.cn/openapi/doc?index_1=4&index_2=1&chapter_1=516&chapter_2=518" style="color:red;font-size:14px;">支付回调官方API文档</a>
	 */
    String swiftPassCompleteCallback(String prams);
    
}
