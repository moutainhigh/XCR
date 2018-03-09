package com.yatang.xc.xcr.service;

import com.yatang.xc.xcr.dto.inputs.LogisticsInfoListDto;
import com.yatang.xc.xcr.model.ResultMap;

public interface IExpressQueryService {

    ResultMap logisticsInfoList(LogisticsInfoListDto model);

}
