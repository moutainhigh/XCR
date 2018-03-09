package com.yatang.xc.xcr.service;

import com.yatang.xc.xcr.dto.inputs.AddNewGoodsDto;
import com.yatang.xc.xcr.dto.inputs.EditNewGoodsDto;
import com.yatang.xc.xcr.dto.inputs.ModifyGoodsPricesDto;
import com.yatang.xc.xcr.dto.inputs.NewGoodsDetailDto;
import com.yatang.xc.xcr.dto.inputs.NewGoodsListDto;
import com.yatang.xc.xcr.model.RequestBaseModel;
import com.yatang.xc.xcr.model.ResultMap;

public interface ICommodityManagementService {

	ResultMap editNewGoods(EditNewGoodsDto model);



	ResultMap newGoodsDetail(NewGoodsDetailDto model);



	ResultMap addNewGoods(AddNewGoodsDto model);



	ResultMap newGoodsList(NewGoodsListDto model);



	ResultMap newClassifyList(RequestBaseModel model);



	ResultMap searchByClassifyId(int pageNum, int pageSize, String classifyId, String storeSerialNo);



	ResultMap searchByTextCondition(int pageNum, int pageSize, String searchText, String storeSerialNo);



	ResultMap newShopImportGoods(AddNewGoodsDto model);



	ResultMap newModifyGoodsPrices(ModifyGoodsPricesDto model);
}
