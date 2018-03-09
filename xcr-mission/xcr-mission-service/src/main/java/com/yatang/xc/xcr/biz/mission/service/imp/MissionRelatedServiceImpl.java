package com.yatang.xc.xcr.biz.mission.service.imp;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yatang.xc.xcr.biz.mission.bo.MissionRelatedQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.dao.MissionRelatedDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionRelatedDetailDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedDetailPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionRelatedPO;
import com.yatang.xc.xcr.biz.mission.enums.EnumReason;
import com.yatang.xc.xcr.biz.mission.service.MissionRelatedService;

@Service
@Transactional
public class MissionRelatedServiceImpl implements MissionRelatedService {


    @Autowired
    private MissionRelatedDAO relatedDao;
    
    @Autowired
    private MissionRelatedDetailDAO relatedDetailDao; 
    

    @Override
    public boolean createRelatedMission(MissionRelatedPO related, List<MissionRelatedDetailPO> details) {
        Date now = new Date();
        related.setCreateTime(now);
        related.setLastModifyTime(now);
        related.setReason(EnumReason.REASON_SYS_CREATE.getCode());
        int count = relatedDao.insert(related);
        if(details!=null && !details.isEmpty() && count>0){
            for(MissionRelatedDetailPO detail:details){
                detail.setMissonRelatedId(related.getId());
                count += relatedDetailDao.insert(detail);
            }
            return count>details.size();
        }
        return count>0;       
    }


    @Override
    public boolean updateRelatedStatus(UpdateStatusQuery query) {
        query.setLastModifyTime(new Date());
        int count = relatedDao.updateStatus(query);
        return count>0;
    }


    @Override
    public List<MissionRelatedDetailPO> findRelatedDetailsByRelatedId(Long relatedId) {
        return relatedDetailDao.findRelatedDetailsByRelatedId(relatedId);
    }


    @Override
    public MissionRelatedPO findById(Long id) {
        return relatedDao.selectByPrimaryKey(id);
    }


    @Override
    public boolean removeMissionRelatedById(Long id) {
        int count = relatedDao.deleteByPrimaryKey(id);
        List<MissionRelatedDetailPO> details = relatedDetailDao.findRelatedDetailsByRelatedId(id);
        if(details!=null && !details.isEmpty()){
            for(MissionRelatedDetailPO detail:details){
                relatedDetailDao.deleteByPrimaryKey(detail.getId());
            }
            return count>details.size();
        }
        return count > 0;
    }


    @Override
    public List<MissionRelatedPO> queryMissionRelated(MissionRelatedQuery query) {
        if(query==null){
            query = new MissionRelatedQuery();
        }
        return relatedDao.queryMissionRelated(query);
    }


    @Override
    public List<MissionRelatedDetailPO> findRelatedDetailsByMissionId(Long id) {
        return relatedDetailDao.findRelatedDetailsByMissionId(id);
    }


    @Override
    public int queryMissionRelatedCount(MissionRelatedQuery query) {
        if(query==null){
            query = new MissionRelatedQuery();
        }
        return relatedDao.queryMissionRelatedCount(query);
    }


    @Override
    public boolean updateRelatedMission(MissionRelatedPO related, List<MissionRelatedDetailPO> details) {
        Date now = new Date();
        related.setLastModifyTime(now);
        List<MissionRelatedDetailPO> oldDetails = relatedDetailDao.findRelatedDetailsByRelatedId(related.getId());
        if(oldDetails!=null && !oldDetails.isEmpty()){
            for(MissionRelatedDetailPO oldDetail:oldDetails){
                relatedDetailDao.deleteByPrimaryKey(oldDetail.getId());
            }
        }
        int count = relatedDao.updateByPrimaryKeySelective(related);
        if(details!=null && !details.isEmpty() && count>0){
            for(MissionRelatedDetailPO detail:details){
                detail.setMissonRelatedId(related.getId());
                count += relatedDetailDao.insert(detail);
            }
            return count>details.size();
        }
        return count>0;       
    }


    @Override
    public boolean checkRelatedMissionName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        int count = relatedDao.countMissionByName(name);
        return count == 0;
    }

}
