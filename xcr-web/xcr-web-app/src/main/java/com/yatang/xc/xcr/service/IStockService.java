package com.yatang.xc.xcr.service;


import com.yatang.xc.xcr.dto.inputs.AddToStockDto;
import com.yatang.xc.xcr.dto.inputs.AutomaticReceiptDto;
import com.yatang.xc.xcr.dto.inputs.BusinessStateDto;
import com.yatang.xc.xcr.dto.inputs.ShopAbbreviationDto;
import com.yatang.xc.xcr.model.ResultMap;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月04日 12:00
 * @Summary :
 */
public interface IStockService {

    ResultMap addToStock(AddToStockDto model);

    ResultMap setShopBusiness(BusinessStateDto model);

    ResultMap setReciveStatus(AutomaticReceiptDto model);

    ResultMap setStoreAbbrevy(ShopAbbreviationDto model);
}
