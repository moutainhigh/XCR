package com.yatang.xc.xcr.biz.mission.service.imp;

import java.util.Date;
import java.util.List;

import com.yatang.xc.xcr.biz.mission.domain.MissionAwardCollectPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.mission.dao.MissionAwardDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteAwardDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteHistoryAwardDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteHistoryAwardPO;
import com.yatang.xc.xcr.biz.mission.service.MissionAwardService;

@Service
@Transactional
public class MissionAwardServiceImpl implements MissionAwardService {

    @Autowired
    private MissionAwardDAO dao;

    @Autowired
    private MissionExecuteAwardDAO executeAwarddao;

    @Autowired
    private MissionExecuteHistoryAwardDAO executeHistoryAwarddao;


    @Override
    public List<MissionAwardPO> queryAwardByMissionId(Long missionId) {
        return dao.queryByMissionId(missionId);
    }


    @Override
    public List<MissionExecuteAwardPO> queryExecuteAwardByExecuteId(Long executeId) {
        return executeAwarddao.queryExecuteAwardByExecuteId(executeId);
    }


    @Override
    public boolean deleteExecuteAwardByExecuteId(Long executeId) {
        List<MissionExecuteAwardPO> awards = executeAwarddao.queryExecuteAwardByExecuteId(executeId);
        if (awards != null && !awards.isEmpty()) {
            int count = 0;
            for (MissionExecuteAwardPO award : awards) {
                count += executeAwarddao.deleteByPrimaryKey(award.getId());
            }
            return count == awards.size();
        }
        return true;
    }


    @Override
    public boolean backMissionAwardToHistory(List<MissionExecuteAwardPO> awards, Long executeHistoryId) {
        if (awards != null && !awards.isEmpty()) {
            int count = 0;
            for (MissionExecuteAwardPO award : awards) {
                MissionExecuteHistoryAwardPO history = new MissionExecuteHistoryAwardPO();
                BeanUtils.copyProperties(award, history);
                int inner = executeHistoryAwarddao.backHistoryAward(award.getId());
                if (inner > 0) {
                    executeAwarddao.deleteByPrimaryKey(award.getId());
                    count++;
                }
            }
            return count == awards.size();
        }
        return true;
    }

    @Override
    public boolean deleteExecuteAwardById(Long id) {
        return executeAwarddao.deleteByPrimaryKey(id) > 0;
    }


    @Override
    public boolean createExecuteAward(MissionExecuteAwardPO eAward) {
        return this.executeAwarddao.insert(eAward) > 0;

    }

    @Override
    public boolean saveAward(MissionAwardCollectPO missionAwardCollectPO) {
        if (missionAwardCollectPO == null) {
            return false;
        }
        missionAwardCollectPO.setCreateTime(new Date());
        missionAwardCollectPO.setUpdateTime(new Date());
        try {
            executeAwarddao.saveAward(missionAwardCollectPO);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public MissionAwardCollectPO getAwardTotal(String shopCode) {
        return executeAwarddao.getAwardTotal(shopCode);
    }

}
