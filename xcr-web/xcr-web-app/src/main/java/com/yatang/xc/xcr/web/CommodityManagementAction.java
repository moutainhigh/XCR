package com.yatang.xc.xcr.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.annotations.Payload;
import com.yatang.xc.xcr.annotations.SessionToken;
import com.yatang.xc.xcr.dto.inputs.AddNewGoodsDto;
import com.yatang.xc.xcr.dto.inputs.EditNewGoodsDto;
import com.yatang.xc.xcr.dto.inputs.ModifyGoodsPricesDto;
import com.yatang.xc.xcr.dto.inputs.NewGoodsDetailDto;
import com.yatang.xc.xcr.dto.inputs.NewGoodsListDto;
import com.yatang.xc.xcr.model.RequestBaseModel;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.ICommodityManagementService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.web.interceptor.BuryingPoint;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月08日 14:35
 * @Summary : 商品管理
 * @since 2.6
 */
@Controller
@RequestMapping("/User/")
public class CommodityManagementAction {
	
	private final Logger log = LoggerFactory.getLogger(CommodityManagementAction.class);

    @Autowired
    private ICommodityManagementService commodityManagementService;

    ///<Summary>
    ///  新增商品v2.6
    ///</Summary>
    @BuryingPoint
    @SessionToken
    @RequestMapping(value = "AddNewGoods", method = RequestMethod.POST)
    public ResultMap addNewGoods(@Payload AddNewGoodsDto model) {
        return commodityManagementService.addNewGoods(model);
    }

    ///<Summary>
    ///   编辑商品v2.6
    ///</Summary>
    @BuryingPoint
    @SessionToken
    @RequestMapping(value = "EditNewGoods", method = RequestMethod.POST)
    public ResultMap editNewGoods(@Payload EditNewGoodsDto model) {
        return commodityManagementService.editNewGoods(model);
    }

    ///<Summary>
    /// 商品详情v2.6
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "NewGoodsDetail", method = RequestMethod.POST)
    public ResultMap newGoodsDetail(@Payload NewGoodsDetailDto model) {
    	long startTime=System.currentTimeMillis();
    	log.info("\n******于时间："+DateUtils.getLogDataTime(startTime, null)+" 开始请求[主数据]->NewGoodsDetail"
    			+"\n******请求参数："+JSONObject.toJSONString(model));
    	ResultMap result=commodityManagementService.newGoodsDetail(model);
    	log.info("\n******于时间："+DateUtils.getLogDataTime(startTime, null)+" 结束请求[主数据]->NewGoodsDetail"
    			+"\n******响应为："+JSONObject.toJSONString(result)
    			+"\n******响应为："+CommonUtil.costTime(startTime));
        return result;
    }

    ///<Summary>
    ///  商品列表
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "NewGoodsList", method = RequestMethod.POST)
    public ResultMap newGoodsList(@Payload NewGoodsListDto model) {
        return commodityManagementService.newGoodsList(model);
    }

    ///<Summary>
    ///  分类列表v2_6
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "NewClassifyList", method = RequestMethod.POST)
    public ResultMap newClassifyList(@Payload RequestBaseModel model) {
        return commodityManagementService.newClassifyList(model);
    }

    ///<Summary>
    ///  编辑v2_6
    ///</Summary>
    @SessionToken
    @RequestMapping(value = "NewShopImportGoods", method = RequestMethod.POST)
    public ResultMap newShopImportGoods(@Payload AddNewGoodsDto model) {
    	long startTime=System.currentTimeMillis();
    	log.info("\n******于时间："+DateUtils.getLogDataTime(startTime, null)+" 开始请求[F5]->newShopImportGoods"
    			+"\n******请求参数："+JSONObject.toJSONString(model));
    	ResultMap result=commodityManagementService.newShopImportGoods(model);
    	log.info("\n******于时间："+DateUtils.getLogDataTime(startTime, null)+" 结束请求[F5]->newShopImportGoods"
    			+"\n******响应为："+JSONObject.toJSONString(result)
    			+"\n******响应为："+CommonUtil.costTime(startTime));
        return result;
    }

    /**
     * 2.6.0商品调价
     * @param model
     * @return
     */
    @SessionToken
    @BuryingPoint
    @RequestMapping(value = "/NewModifyGoodsPrices", method = RequestMethod.POST)
    public ResultMap newModifyGoodsPrices(@Payload ModifyGoodsPricesDto model) {
        return commodityManagementService.newModifyGoodsPrices(model);
    }
}
