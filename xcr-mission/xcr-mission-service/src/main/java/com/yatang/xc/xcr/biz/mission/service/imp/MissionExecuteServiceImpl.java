package com.yatang.xc.xcr.biz.mission.service.imp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.busi.common.exception.BusinessException;
import com.yatang.xc.xcr.biz.mission.bo.ExecuteByMerchantIdAndCourseIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.ExecuteByMissionIdAndMerchantIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.ExpireQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.bo.OfflineListQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusAndBpmIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.dao.MissionAttachmentDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteAwardDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteHistoryDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionExecuteInfoDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionInfoDAO;
import com.yatang.xc.xcr.biz.mission.dao.MissionOfflineExistsListDAO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteHistoryPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionExecuteInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionOfflineExistsListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;
import com.yatang.xc.xcr.biz.mission.enums.EnumReason;
import com.yatang.xc.xcr.biz.mission.service.MissionExecuteService;

@Service
@Transactional
public class MissionExecuteServiceImpl implements MissionExecuteService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionInfoDAO missionDao;

    @Autowired
    private MissionExecuteInfoDAO dao;

    @Autowired
    private MissionExecuteHistoryDAO historyDao;

    @Autowired
    private MissionAttachmentDAO attachmentDao;

    @Autowired
    private MissionOfflineExistsListDAO offlineDao;


    @Override
    public List<MissionExecuteInfoPO> findExecutableMissions(String merchantId)
            throws BusinessException {
        if (StringUtils.isEmpty(merchantId)) {
            return null;
        }
        return dao.selectByMerchantId(merchantId);
    }


    @Override
    public MissionExecuteInfoPO findExecuteMissionById(String id)
            throws BusinessException {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return dao.selectByPrimaryKey(Long.valueOf(id));
    }


    @Override
    public List<MissionExecuteInfoPO> queryMissionExecute(MissionExecuteQuery query) {
        if (query == null) {
            query = new MissionExecuteQuery();
        }
        query.setNow(new Date());
        return dao.queryMissionExecute(query);
    }


    @Override
    public boolean updateMissionExecuteStatus(UpdateStatusQuery query) {
        query.setLastModifyTime(new Date());
        int count = dao.updateStatus(query);
        return count > 0;
    }


    @Override
    public List<MissionExecuteInfoPO> queryExecuteMissionByMissionIdAndMerchantId(Long missonInfoId, String merchantId) {
        ExecuteByMissionIdAndMerchantIdQuery query = new ExecuteByMissionIdAndMerchantIdQuery();
        query.setMerchantId(merchantId);
        query.setMissionId(missonInfoId);
        return dao.queryExecuteMissionByMissionIdAndMerchantId(query);
    }


    @Override
    public int queryMissionExecuteCount(MissionExecuteQuery query) {
        if (query == null) {
            query = new MissionExecuteQuery();
        }
        query.setNow(new Date());
        return dao.queryMissionExecuteCount(query);
    }


    @Override
    public boolean deleteExecuteById(Long id) {
        if (id == 0) {
            return false;
        }
        int count = dao.deleteByPrimaryKey(id);
        return count > 0;
    }


    @Override
    public MissionExecuteInfoPO findExecuteMissionByMerchantIdAndCourseId(String merchantId, String courseId, String status) {
        ExecuteByMerchantIdAndCourseIdQuery query = new ExecuteByMerchantIdAndCourseIdQuery();
        query.setCourseId(courseId);
        query.setStatus(status);
        query.setMerchantId(merchantId);
        List<MissionExecuteInfoPO> executes = dao.queryExecuteMissionByMerchantIdAndCourseId(query);
        if (executes != null && !executes.isEmpty()) {
            return executes.get(0);
        }
        return null;
    }

    @Override
    public MissionExecuteInfoPO findExecuteMissionByMerchantIdAndCourseIdInHistory(String merchantId, String courseId, String status) {
        ExecuteByMerchantIdAndCourseIdQuery query = new ExecuteByMerchantIdAndCourseIdQuery();
        query.setCourseId(courseId);
        query.setStatus(status);
        query.setMerchantId(merchantId);
        List<MissionExecuteInfoPO> executes = dao.queryExecuteMissionByMerchantIdAndCourseIdInHistory(query);
        if (executes != null && !executes.isEmpty()) {
            return executes.get(0);
        }
        return null;
    }


    @Override
    public MissionExecuteInfoPO createMissionEexecute(String merchantId, MissionInfoPO missionInof, MissionTemplatePO template, String initStatus) {
        if (missionInof == null) {
            return null;
        }
        MissionExecuteInfoPO execute = new MissionExecuteInfoPO();
        BeanUtils.copyProperties(template, execute, "id");
        BeanUtils.copyProperties(missionInof, execute, "id");
        execute.setRule(missionInof.getRule());
        execute.setMissonInfoId(missionInof.getId());
        execute.setMerchantId(merchantId);
        execute.setSpecialAwardRemark(template.getSpecialAwardRemark());
        Date now = new Date();
        execute.setCreateTime(now);
        execute.setLastModifyTime(now);
        execute.setDeleted(false);
        execute.setReason(EnumReason.REASON_SYS_CREATE.getCode());
        execute.setStatus(initStatus);

        //修改任务持续时间为后台配置传入时间
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, template.getStartHour());
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        execute.setStartTime(calendar.getTime());
//        calendar.set(Calendar.HOUR_OF_DAY, template.getStartHour() + template.getDurationHours());
//        execute.setEndTime(calendar.getTime());
        execute.setStartTime(missionInof.getDurationTimeStart());
        execute.setEndTime(missionInof.getDurationTimeEnd());
        //添加任务有效期
        execute.setValidTimeStart(missionInof.getValidTimeStart());
        execute.setValidTimeEnd(missionInof.getValidTimeEnd());
        //添加任务描述
        execute.setDescription(missionInof.getDescription());
        execute.setSpecialAwardRemark(missionInof.getSpecialAwardRemark()); //任务描述
        int count = dao.insert(execute);
        if (count > 0) {
            return execute;
        }
        return null;
    }


    @Override
    public List<MissionExecuteInfoPO> queryExecuteMissionByMissionIdAndMerchantIdInHistory(Long missonInfoId, String merchantId) {
        ExecuteByMissionIdAndMerchantIdQuery query = new ExecuteByMissionIdAndMerchantIdQuery();
        query.setMerchantId(merchantId);
        query.setMissionId(missonInfoId);
        return dao.queryExecuteMissionByMissionIdAndMerchantIdInHistory(query);
    }


    @Override
    public boolean backMissionExecuteToHistory(Long executeId) {

        MissionExecuteHistoryPO history = historyDao.selectByPrimaryKey(executeId);
        if (history == null) {
            boolean saveExecute = this.historyDao.backMissionExecuteToHistory(executeId);
            if (saveExecute) {
                dao.removeByPrimaryKey(executeId);
            }
        } else {
            dao.removeByPrimaryKey(executeId);
        }
        return true;
    }

    @Override
    public boolean removeMissionExecuteByMissionId(Long missionInfoId) {
        try {
            dao.removeByMissionInfoId(missionInfoId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public boolean saveAttachment(MissionAttachmentPO a) {
        return this.attachmentDao.insert(a) > 0;
    }


    @Override
    public boolean backAttachmentToHistory(Long executeId, String type) {
        List<MissionAttachmentPO> attachments = this.attachmentDao.queryAttachmentByExecuteId(executeId);
        for (MissionAttachmentPO attachment : attachments) {
            if (type.equals(attachment.getType())) {
                int count = attachmentDao.insertToHistory(attachment.getId());
                if (count > 0) {
                    attachmentDao.deleteByPrimaryKey(attachment.getId());
                }
            }
        }
        return true;
    }


    @Override
    public List<MissionOfflineExistsListPO> queryOfflineList(String merchantId, String templateCode) {
        OfflineListQuery query = new OfflineListQuery();
        query.setMerchantId(merchantId);
        query.setTemplateCode(templateCode);
        return offlineDao.queryOfflineList(query);
    }


    @Override
    public boolean updateMissionExecuteStatusAndBpmId(UpdateStatusAndBpmIdQuery query) {
        query.setLastModifyTime(new Date());
        int count = dao.updateStatusAndBpmId(query);
        return count > 0;

    }

    @Autowired
    private MissionExecuteAwardDAO executeAwarddao;


    @Override
    public boolean expireMissionExecutes(String status) {
        log.info("expireMissionExecutes start: status:" + status);
        Date now = new Date();
        ExpireQuery query = new ExpireQuery();
        query.setNow(now);
        query.setStatus(status);
        query.setReason("MISSION_NOT_EXIST");
        dao.expireNotExistMissionExecutes(query);
        query.setReason("AUTO_EXPIRE");
        dao.expireMissionExecutes(query);
        dao.backExpireMissionExecuteToHistory(status);
        executeAwarddao.backExpireAward(status);
        executeAwarddao.deleteExpireAward(status);
        attachmentDao.backExpireAttachment(status);
        attachmentDao.deleteExpireAttachment(status);
        dao.removeExpireMisiionExecute(status);
        dao.deleteMissionExecuteByIsDeleted();
        log.info("expireMissionExecutes end: status:" + status);
        return true;
    }

    @Override
    public boolean expireMissionExecutesForDayMission(String status) {
        log.info("expireMissionExecutes start: status:" + status);
        ExpireQuery query = new ExpireQuery();
        query.setStatus(status);
        query.setNow(new Date());
        dao.expireMissionDayExecutes(query);
        log.info("expireMissionExecutes end: status:" + status);
        return true;
    }


    @Override
    public boolean removeMisiionExecuteByStatus(String status) {
        dao.removeExpireMisiionExecute(status);
        return true;
    }


    @Override
    public List<MissionExecuteInfoPO> queryMissionExecuteInHistory(MissionExecuteQuery query) {
        if (query == null) {
            query = new MissionExecuteQuery();
        }
        query.setNow(new Date());
        return dao.queryMissionExecuteInHistory(query);
    }


    @Override
    public int queryMissionExecuteCountInHistory(MissionExecuteQuery query) {
        if (query == null) {
            query = new MissionExecuteQuery();
        }
        query.setNow(new Date());
        return dao.queryMissionExecuteCountInHistory(query);
    }


    @Override
    public List<MissionExecuteInfoPO> queryMissionExecuteOrderByRelated(MissionExecuteQuery query) {
        if (query == null) {
            query = new MissionExecuteQuery();
        }
        query.setNow(new Date());
        return dao.queryMissionExecuteOrderByRelated(query);
    }


    @Override
    public int invalidExecuteMissionHasDelted(String status) {
        return dao.invalidExecuteMissionHasDeleted(status);
    }
}
