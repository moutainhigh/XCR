package com.yatang.xc.xcr.biz.mission.dao;

import java.util.List;
import java.util.Set;

import com.yatang.xc.xcr.biz.mission.domain.MissionUserHistoryPO;
import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;

public interface MissionUserHistoryDAO {
    int deleteByPrimaryKey(Long id);

    int insert(MissionUserHistoryPO record);

    int insertSelective(MissionUserHistoryPO record);

    MissionUserHistoryPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MissionUserHistoryPO record);

    int updateByPrimaryKey(MissionUserHistoryPO record);

    List<MissionUserHistoryPO> selectUserHistoryByMerchantId(String id);

    Set<String> queryMerchantIdFromUserHistory();

    /**
     * 获取签到门店编号集合
     *
     * @return
     */
    List<String> getSignShopCodeList();

    /**
     * 根据门店获取签到情况
     *
     * @param shopCode
     * @return
     */
    List<UserSignPO> getSignListByShopCode(String shopCode);

    /**
     * 根据ID更新连续签到天数
     *
     * @param id
     */
    void updateContinueDayById(long id, int continueDay);
}