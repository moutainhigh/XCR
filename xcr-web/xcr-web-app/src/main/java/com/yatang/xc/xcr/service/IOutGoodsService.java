package com.yatang.xc.xcr.service;


import com.yatang.xc.xcr.dto.inputs.NewArrivalsDto;
import com.yatang.xc.xcr.dto.inputs.SpecialGoodsOutListDto;
import com.yatang.xc.xcr.model.ResultMap;

/**
 * @Author : BobLee
 * @CreateTime : 2017年11月28日 10:36
 * @Summary :
 */
public interface IOutGoodsService {

    ResultMap newArrivals(NewArrivalsDto model);

    ResultMap querySvipExclusiveProducts();

    ResultMap specialGoodsOutList(SpecialGoodsOutListDto dto);
}
