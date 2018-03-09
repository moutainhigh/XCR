package com.yatang.xc.xcr.biz.core.service;

import com.yatang.xc.xcr.biz.core.dao.UserSignDao;
import com.yatang.xc.xcr.biz.core.domain.UserSignPO;
import com.yatang.xc.xcr.biz.core.domain.UserSignSetPO;

import java.util.Date;
import java.util.List;

/**
 * 用户签到
 * Created by wangyang on 2017/7/7.
 */
public interface UserSignService {

    /**
     * 保存签到记录签到
     *
     * @param po
     * @return
     */
    boolean sign(UserSignPO po);

    /**
     * 获取签到奖励信息
     *
     * @return
     */
    UserSignSetPO getSignSet();

    /**
     * 根据门店编号获取最后签到时间
     *
     * @param shopCode
     * @return
     */
    UserSignPO getLastSignDayByShopCode(String shopCode);

    /**
     * 根据加盟商编号获取最后签到时间
     *
     * @param franchiseeId
     * @return
     */
    UserSignPO getLastSignDayByFranchiseeId(String franchiseeId);

    /**
     * 连续签到+1
     *
     * @param shopCode
     */
    void updateContinueDayToAdd(String franchiseeId, String day, int continueDay);

    /**
     * 连续签到中断，更新为1
     *
     * @param shopCode
     */
    void updateContinueDayToStart(String franchiseeId);

    /**
     * 获取用户当月签到情况
     *
     * @param shopCode
     * @param startDay
     * @param endDay
     * @return
     */
    List<UserSignPO> getSignArray(String shopCode, Date startDay, Date endDay);

    /**
     * 获取总的签到奖励
     *
     * @param shopCode
     * @return
     */
    Double getAwardTotal(String shopCode, int type);

    /**
     * 获取签到设置详情
     *
     * @return
     */
    UserSignSetPO getUserSignInfo();

    /**
     * 更新签到信息
     *
     * @param po
     * @return
     */
    boolean updateUserSignSet(UserSignSetPO po);

    /**
     * 奖励统计入库
     *
     * @param shopCode
     * @param cash
     * @param score
     */
    void saveAward(String shopCode, String cash, String score);

    /**
     * 获取签到次数最多的门店
     *
     * @param shopCodeList
     * @return
     */
    String getMostAwardedShopCode(List<String> shopCodeList);

    /**
     * 获取总签到奖励，包含加盟商下所有门店
     *
     * @param shopCodeList
     * @return
     */
    Double getAwardTotalByShopCodeList(List<String> shopCodeList);
}
