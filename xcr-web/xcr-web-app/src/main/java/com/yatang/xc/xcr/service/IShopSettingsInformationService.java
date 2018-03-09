package com.yatang.xc.xcr.service;


import com.yatang.xc.xcr.dto.inputs.*;
import com.yatang.xc.xcr.model.ResultMap;

public interface IShopSettingsInformationService {

	ResultMap setShopAddress(SetShopAddressDto model);

	ResultMap setShopInfo(SetShopInfoDto model);

	ResultMap setShopTime(SetShopTimeDto model);

	ResultMap introduceRelease(IntroduceReleaseDto model);

	ResultMap noticesRelease(NoticesReleaseDto model);

	ResultMap setIntroduceStatue(SetIntroduceStatueDto dto);

	ResultMap setNoticesShown(SetNoticesShownDto dto);
}
