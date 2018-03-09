package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.bo.BuyListByQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionBuyListPO;

public interface MissionBuyListDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionBuyListPO record);

    //int insertSelective(MissionBuyListPO record);

    MissionBuyListPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionBuyListPO record);

    int updateByPrimaryKey(MissionBuyListPO record);

    List<MissionBuyListPO> queryBuyListBy(BuyListByQuery query);
}