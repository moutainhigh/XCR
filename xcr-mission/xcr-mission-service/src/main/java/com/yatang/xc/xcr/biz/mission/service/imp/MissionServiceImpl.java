package com.yatang.xc.xcr.biz.mission.service.imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.yatang.xc.xcr.biz.mission.dao.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yatang.xc.xcr.biz.mission.bo.BuyListByQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionByCourseIdQuery;
import com.yatang.xc.xcr.biz.mission.bo.MissionInfoQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.MissionAttachmentPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionAwardPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionBuyListPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionInfoPO;
import com.yatang.xc.xcr.biz.mission.domain.MissionTemplatePO;
import com.yatang.xc.xcr.biz.mission.domain.MissionUserHistoryPO;
import com.yatang.xc.xcr.biz.mission.domain.UserSignPO;
import com.yatang.xc.xcr.biz.mission.enums.EnumReason;
import com.yatang.xc.xcr.biz.mission.service.MissionService;

@Service
@Transactional
public class MissionServiceImpl implements MissionService {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MissionInfoDAO dao;

    @Autowired
    private MissionAwardDAO awardDao;

    @Autowired
    private MissionTemplateDAO templateDao;

    @Autowired
    private MissionAttachmentDAO attachmentDao;

    @Autowired
    private MissionBuyListDAO buyListDao;

    @Autowired
    private MissionUserHistoryDAO userDao;

    @Autowired
    private MissionUserHistoryDAO missionUserHistoryDAO;

    @Autowired
    private MissionSignDAO missionSignDAO;

    @Override
    public MissionInfoPO findById(Long missionId) {
        return dao.selectByPrimaryKey(missionId);
    }

    @Override
    public MissionTemplatePO selectTemplateByCode(String code) {
        return templateDao.selectTemplateByCode(code);
    }

    @Override
    public boolean createMissionInfo(MissionInfoPO missionInfo, List<MissionAwardPO> awards,
                                     List<MissionAttachmentPO> attachments) {
        Date now = new Date();
        missionInfo.setCreateTime(now);
        missionInfo.setLastModifyTime(now);
        missionInfo.setDeleted(false);
        missionInfo.setReason(EnumReason.REASON_SYS_CREATE.getCode());
        String specialAwardRemark = "";
        if (!CollectionUtils.isEmpty(awards)) {
            for (MissionAwardPO award : awards) {
                String type = award.getAwardType();
                Double data = award.getGrantNum();
                if ("CASH".equals(type)) {
                    specialAwardRemark = "现金" + data.intValue() + "元";
                } else {
                    specialAwardRemark = data.intValue() + "积分";
                }
            }
            missionInfo.setSpecialAwardRemark(specialAwardRemark);
        }
        int count = dao.insert(missionInfo);
        if (awards != null && !awards.isEmpty() && count > 0) {
            for (MissionAwardPO award : awards) {
                award.setMissonInfoId(missionInfo.getId());
                awardDao.insert(award);
            }
        }
        if (attachments != null && !attachments.isEmpty() && count > 0) {
            for (MissionAttachmentPO attachment : attachments) {
                attachment.setMissionInfoId(String.valueOf(missionInfo.getId()));
                attachmentDao.insert(attachment);
            }
        }
        return count > 0;
    }

    @Override
    public boolean updateMissionInfoStatus(UpdateStatusQuery query) {
        query.setLastModifyTime(new Date());
        int count = dao.updateStatus(query);
        return count > 0;
    }

    @Override
    public List<MissionTemplatePO> queryMissionTemplateByMissionType(String missionType) {
        if (StringUtils.isEmpty(missionType)) {
            return null;
        }
        return templateDao.queryMissionTemplateByMissionType(missionType);
    }

    @Override
    public boolean removeMissionById(Long id) {
        int count = dao.removeMissionById(id);
        return count > 0;
    }

    @Override
    public List<MissionInfoPO> queryMissionInfo(MissionInfoQuery query) {
        if (query == null) {
            query = new MissionInfoQuery();
        }
        return dao.queryMissionInfo(query);
    }

    @Override
    public int queryMissionInfoCount(MissionInfoQuery query) {
        if (query == null) {
            query = new MissionInfoQuery();
        }
        return dao.queryMissionInfoCount(query);
    }

    @Override
    public boolean updateMissionInfo(MissionInfoPO missionInfo, List<MissionAwardPO> awards,
                                     List<MissionAttachmentPO> attachments) {
        List<MissionAwardPO> oldawards = awardDao.queryByMissionId(missionInfo.getId());
        if (oldawards != null && !oldawards.isEmpty()) {
            for (MissionAwardPO oldaward : oldawards) {
                log.info("更新任务信息 -> awardDao.deleteByPrimaryKey(oldaward.getId()) -> oldaward：" + oldaward);
                awardDao.deleteByPrimaryKey(oldaward.getId());
            }
        }

        List<MissionAttachmentPO> oldattachments = attachmentDao.queryAttachmentByMissionId(missionInfo.getId());
        if (oldattachments != null && !oldattachments.isEmpty()) {
            for (MissionAttachmentPO olda : oldattachments) {
                log.info("更新任务信息 -> attachmentDao.queryAttachmentByMissionId(missionInfo.getId()) -> oldaward：" + olda);
                attachmentDao.deleteByPrimaryKey(olda.getId());
            }
        }

        Date now = new Date();
        missionInfo.setLastModifyTime(now);
        log.info("更新任务信息 -> dao.updateByPrimaryKeySelective(missionInfo) -> missionInfo：" + missionInfo);
        dao.updateByPrimaryKeySelective(missionInfo);
        if (!StringUtils.isEmpty(missionInfo.getStatus())) {
            UpdateStatusQuery query = new UpdateStatusQuery();
            query.setId(missionInfo.getId());
            query.setStatus(missionInfo.getStatus());
            dao.updateStatus(query);
        }
        if (awards != null && !awards.isEmpty()) {
            for (MissionAwardPO award : awards) {
                award.setMissonInfoId(missionInfo.getId());
                awardDao.insert(award);
                log.info("更新任务信息 ->  awardDao.insert(award) -> award：" + award);
            }
        }
        if (attachments != null && !attachments.isEmpty()) {
            for (MissionAttachmentPO attachment : attachments) {
                attachment.setMissionInfoId(String.valueOf(missionInfo.getId()));
                attachmentDao.insert(attachment);
                log.info("更新任务信息 ->  attachmentDao.insert(attachment); -> attachment：" + attachment);
            }
        }
        return true;
    }

    @Override
    public boolean updateMissionSort(MissionInfoPO m) {
        return this.dao.updateSort(m);
    }

    @Override
    public List<MissionAttachmentPO> queryAttachmentByMissionId(Long id) {
        return attachmentDao.queryAttachmentByMissionId(id);
    }

    @Override
    public List<MissionAttachmentPO> queryAttachmentByExecuteId(Long id) {
        return attachmentDao.queryAttachmentByExecuteId(id);
    }

    @Override
    public List<MissionBuyListPO> queryBuyListBy(String login, Long missionId, String executeId) {
        BuyListByQuery query = new BuyListByQuery();
        query.setMissionId(String.valueOf(missionId));
        query.setExecuteId(executeId);
        query.setLogin(login);
        return buyListDao.queryBuyListBy(query);

    }

    @Override
    public boolean updateBuyList(MissionBuyListPO buyList) {
        return buyListDao.updateByPrimaryKeySelective(buyList) > 0;
    }

    @Override
    public boolean createBuyList(MissionBuyListPO buyList) {
        return buyListDao.insert(buyList) > 0;
    }

    @Override
    public MissionUserHistoryPO selectUserHistoryByMerchantId(String merchantId) {
        List<MissionUserHistoryPO> list = userDao.selectUserHistoryByMerchantId(merchantId);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean insertUserHistory(MissionUserHistoryPO user) {
        user.setLastModifyTime(new Date());
        return userDao.insert(user) > 0;
    }

    @Override
    public boolean updateUserHistory(MissionUserHistoryPO user, Date now) {
        if (user == null) {
            return false;
        }
        if (now != null) {
            user.setLastModifyTime(now);
        }
        return userDao.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public List<MissionInfoPO> queryMissionByCourseId(String courseId, String status) {
        MissionByCourseIdQuery query = new MissionByCourseIdQuery();
        query.setCourseId(courseId);
        query.setStatus(status);
        return dao.queryMissionByCourseId(query);
    }

    @Override
    public Set<String> queryMerchantIdFromUserHistory() {
        return userDao.queryMerchantIdFromUserHistory();
    }

    @Override
    public boolean checkMissionName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        int count = dao.countMissionByName(name);
        return count == 0;
    }

    @Override
    public List<MissionAttachmentPO> queryAttachmentHistoryByExecuteId(Long id) {
        return attachmentDao.queryAttachmentHistoryByExecuteId(id);
    }

    @Override
    public int checkSignFinish(String shopCode, Date validStartDay, Date validEndDay) {
        log.info("dao.getSignPo -> params -> shopCode:" + shopCode + "  validStartDay:" + validStartDay + "  validEndDay:" + validEndDay);
        List<UserSignPO> userSignPOList = missionSignDAO.getSignPo(shopCode, validStartDay, validEndDay);
        log.info("dao.getSignPo -> result -> " + JSONObject.toJSONString(userSignPOList));
        if (CollectionUtils.isEmpty(userSignPOList)) {
            return 0;
        }
        // 排序
        Collections.sort(userSignPOList, new Comparator<UserSignPO>() {
            @Override
            public int compare(UserSignPO o1, UserSignPO o2) {
                if (o1.getCreateTime().getTime() > o2.getCreateTime().getTime()) {
                    return 1;
                }
                if (o1.getCreateTime() == o2.getCreateTime()) {
                    return 0;
                }
                return -1;
            }
        });
        int resultCount = countContinueDate(userSignPOList);
        log.info("连续签到 -> checkSignFinish -> shopCode:" + shopCode + "  validStartDay:" + validStartDay
                + "  validEndDay:" + validEndDay + " return :" + resultCount);
        return resultCount;
    }

    private static int countContinueDate(List<UserSignPO> userSignPOList) {
        int continueDate = 1;
        int continueDay = -1;
        int max = 0;
        for (UserSignPO userSignPO : userSignPOList) {
            if (continueDay > 0 && continueDay + 1 == userSignPO.getContinueDay()) {
                continueDate++;
            } else {
                continueDate = 1;
            }
            if (max < continueDate) {
                max = continueDate;
            }
            continueDay = userSignPO.getContinueDay();
        }
        return max;
    }

    @Override
    public void backupsSignHistory() {
        Integer count = dao.backupsSignHistory();
        if (count != null && count > 0) {
            dao.deleteSignHistoryBackups();
        }
    }

    @Override
    public List<String> getSignShopCodeList() {
        return missionUserHistoryDAO.getSignShopCodeList();
    }

    @Override
    public List<UserSignPO> getSignListByShopCode(String shopCode) {
        return missionUserHistoryDAO.getSignListByShopCode(shopCode);
    }

    @Override
    public void updateContinueDayById(long id, int continueDay) {
        missionUserHistoryDAO.updateContinueDayById(id, continueDay);
    }


    private static int getDay(Date startDate, Date endDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(startDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(endDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    public static void main(String[] args) {
        List<UserSignPO> userSignPOList = new ArrayList<>();
        userSignPOList.add(new UserSignPO(1));
        userSignPOList.add(new UserSignPO(1));
        userSignPOList.add(new UserSignPO(1));
        userSignPOList.add(new UserSignPO(1));
        userSignPOList.add(new UserSignPO(1));
        userSignPOList.add(new UserSignPO(2));
        userSignPOList.add(new UserSignPO(3));
        userSignPOList.add(new UserSignPO(4));
        userSignPOList.add(new UserSignPO(5));

        int count = countContinueDate(userSignPOList);
        System.out.println(count);

    }

}
