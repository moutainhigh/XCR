package com.yatang.xc.xcr.biz.core.dao;

import com.yatang.xc.xcr.biz.core.domain.UserAwardCollectPO;
import com.yatang.xc.xcr.biz.core.domain.UserSignPO;
import com.yatang.xc.xcr.biz.core.domain.UserSignSetPO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户签到服务
 * Created by wangyang on 2017/7/7.
 */
public interface UserSignDao {

    long insert(UserSignPO entity);

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
     * @param franchiseeId
     * @param day
     * @param continueDay
     */
    void updateContinueDayToAdd(@Param("franchiseeId") String franchiseeId, @Param("day") String day, @Param("continueDay") int continueDay);

    /**
     * 连续签到中断，更新为1
     *
     * @param franchiseeId
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
    List<UserSignPO> getSignArray(@Param("shopCode") String shopCode, @Param("startDay") Date startDay, @Param("endDay") Date endDay);

    /**
     * 获取总的签到奖励
     *
     * @param shopCode
     * @return
     */
    Double getAwardTotal(String shopCode);

    /**
     * 获取总积分
     *
     * @param shopCode
     * @return
     */
    Double getScoreAwardTotal(String shopCode);

    /**
     * 获取签到说明
     *
     * @return
     */
    String getSignSetCount();

    /**
     * 更新签到信息
     *
     * @param po
     */
    void updateUserSignSet(UserSignSetPO po);

    /**
     * 添加签到信息
     *
     * @param po
     */
    void addUserSignSet(UserSignSetPO po);

    /**
     * 签到奖励统计增量更新
     *
     * @param userAwardCollectPO
     */
    void saveAward(UserAwardCollectPO userAwardCollectPO);

    /**
     * 获取历史签到次数最多的门店
     *
     * @param shopCodeList
     * @return
     */
    String getMostAwardedShopCode(List<String> shopCodeList);

    /**
     * 获取加盟商下所有门店奖励总和
     *
     * @param shopCodeList
     * @return
     */
    Double getAwardTotalByShopCodeList(List<String> shopCodeList);
}
