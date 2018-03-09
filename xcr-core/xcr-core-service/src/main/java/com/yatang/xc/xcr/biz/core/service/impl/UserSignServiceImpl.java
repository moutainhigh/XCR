package com.yatang.xc.xcr.biz.core.service.impl;

import com.yatang.xc.xcr.biz.core.dao.UserSignDao;
import com.yatang.xc.xcr.biz.core.domain.UserAwardCollectPO;
import com.yatang.xc.xcr.biz.core.domain.UserSignPO;
import com.yatang.xc.xcr.biz.core.domain.UserSignSetPO;
import com.yatang.xc.xcr.biz.core.enums.UserSignTypeEnum;
import com.yatang.xc.xcr.biz.core.service.UserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户签到服务实现
 * Created by wangyang on 2017/7/7.
 */
@Service
public class UserSignServiceImpl implements UserSignService {

    @Autowired
    private UserSignDao userSignDao;

    @Override
    public boolean sign(UserSignPO po) {
        long id = userSignDao.insert(po);
        return id > 0;
    }

    @Override
    public UserSignSetPO getSignSet() {
        return userSignDao.getSignSet();
    }

    @Override
    public UserSignPO getLastSignDayByShopCode(String shopCode) {
        return userSignDao.getLastSignDayByShopCode(shopCode);
    }

    @Override
    public UserSignPO getLastSignDayByFranchiseeId(String franchiseeId) {
        return userSignDao.getLastSignDayByFranchiseeId(franchiseeId);
    }

    @Override
    public void updateContinueDayToAdd(String franchiseeId, String day, int continueDay) {
        userSignDao.updateContinueDayToAdd(franchiseeId, day, continueDay);
    }

    @Override
    public void updateContinueDayToStart(String franchiseeId) {
        userSignDao.updateContinueDayToStart(franchiseeId);
    }

    @Override
    public List<UserSignPO> getSignArray(String shopCode, Date startDay, Date endDay) {
        return userSignDao.getSignArray(shopCode, startDay, endDay);
    }

    @Override
    public Double getAwardTotal(String shopCode, int type) {
        if (type == UserSignTypeEnum.CASH.getState()) {
            return userSignDao.getAwardTotal(shopCode);
        }
        if (type == UserSignTypeEnum.SCORE.getState()) {
            return userSignDao.getScoreAwardTotal(shopCode);
        }
        return 0.0;
    }

    @Override
    public UserSignSetPO getUserSignInfo() {
        return userSignDao.getSignSet();
    }

    @Override
    public boolean updateUserSignSet(UserSignSetPO po) {
        try {
            userSignDao.updateUserSignSet(po);
            userSignDao.addUserSignSet(po);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void saveAward(String shopCode, String cash, String score) {
        Double cashDouble = Double.valueOf(cash);
        Double scoreDouble = Double.valueOf(score);
        UserAwardCollectPO userAwardCollectPO = new UserAwardCollectPO(scoreDouble, 0.00, cashDouble, 0.00, shopCode);
        userSignDao.saveAward(userAwardCollectPO);
    }

    @Override
    public String getMostAwardedShopCode(List<String> shopCodeList) {
        return userSignDao.getMostAwardedShopCode(shopCodeList);
    }

    @Override
    public Double getAwardTotalByShopCodeList(List<String> shopCodeList) {
        return userSignDao.getAwardTotalByShopCodeList(shopCodeList);
    }

}
