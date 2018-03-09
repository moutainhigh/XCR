package com.yatang.xc.xcr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yatang.xc.xcr.annotations.Payload;
import com.yatang.xc.xcr.annotations.SessionToken;
import com.yatang.xc.xcr.dto.inputs.LogisticsInfoListDto;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.IExpressQueryService;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月29日 14:35
 * @Summary : 快递、物流信息查询
 */
@Controller
@RequestMapping("/User/")
public class ExpressQueryAction {

    private  @Autowired
    IExpressQueryService expressQueryService;

    ///<Summary>
    /// 超级会员订单物流信息v2.7
    ///</Summary>
    @SessionToken
    @RequestMapping(value="LogisticsInfoList",method= RequestMethod.POST)
    public ResultMap logisticsInfoList(@Payload LogisticsInfoListDto model){
        return expressQueryService.logisticsInfoList(model);
    }

}