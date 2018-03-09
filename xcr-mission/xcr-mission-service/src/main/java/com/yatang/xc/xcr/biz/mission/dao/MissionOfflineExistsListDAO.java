package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;

import com.yatang.xc.xcr.biz.mission.bo.OfflineListQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionOfflineExistsListPO;

public interface MissionOfflineExistsListDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionOfflineExistsListPO record);

    //int insertSelective(MissionOfflineExistsListPO record);

    MissionOfflineExistsListPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionOfflineExistsListPO record);

    int updateByPrimaryKey(MissionOfflineExistsListPO record);

    List<MissionOfflineExistsListPO> queryOfflineList(OfflineListQuery query);
}