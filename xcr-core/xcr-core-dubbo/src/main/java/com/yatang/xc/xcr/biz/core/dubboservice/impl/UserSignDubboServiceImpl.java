package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.StringUtils;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberScoreInfoPoDto;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberScoreDubboService;
import com.yatang.xc.xcr.biz.core.domain.UserSignPO;
import com.yatang.xc.xcr.biz.core.domain.UserSignSetPO;
import com.yatang.xc.xcr.biz.core.dto.SignDay;
import com.yatang.xc.xcr.biz.core.dto.UserSignDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignInfoDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignSetDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.UserSignDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.DateUtil;
import com.yatang.xc.xcr.biz.core.enums.PublicEnum;
import com.yatang.xc.xcr.biz.core.enums.UserSignTypeEnum;
import com.yatang.xc.xcr.biz.core.redis.lock.DistributedLockManger;
import com.yatang.xc.xcr.biz.core.service.UserSignService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 签到服务
 * Created by wangyang on 2017/7/7.
 */
@Service("userSignDubboService")
@Transactional(propagation = Propagation.REQUIRED)
public class UserSignDubboServiceImpl implements UserSignDubboService {
    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private UserSignService userSignService;
    @Autowired//积分
    private MemberScoreDubboService memberScoreDubboService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private DistributedLockManger distributedLock;

    @Override
    public Response sign(UserSignDTO dto) {
        log.info("【签到dubbo服务】 -> 传入参数 -> UserSignDTO dto:" + JSONObject.toJSONString(dto));
        try {
            if (dto == null) {
                return new Response<>(false, "UserSignDTO->传入参数为空", "");
            }
            String franchiseeId = dto.getUserId();
            String shopCode = getShopCodeByFranchiseeIdForUpToSign(franchiseeId);
            if (StringUtils.isEmpty(shopCode)) {
                shopCode = dto.getShopCode();
            }

            if (StringUtils.isEmpty(shopCode)) {
                return new Response<>(false, "UserSignDTO->shopCode不能为空", "");
            }
            if (StringUtils.isEmpty(franchiseeId)) {
                return new Response<>(false, "UserSignDTO->franchiseeId不能为空", "");
            }

            //根据加盟商号获取用户最后一次签到时间
            SignDay SignDay = getLastSignDay(franchiseeId);
            if (SignDay == null) {
                return new Response<>(false, "当前用户今天已经签过到了", "");
            }
            Date lastSignDay = SignDay.getLastSignDay(); //最后签到时间
            Integer signDays = SignDay.getSignDays();    //连续签到天数

            //获取当前奖励,在数据库中查询当前生效奖励
            UserSignSetPO userSignSetPO = userSignService.getSignSet();
            log.info("【签到dubbo服务】 -> userSignService.getSignSet() -> UserSignSetPO userSignSetPO:" + userSignSetPO);
            if (userSignSetPO == null) {
                return new Response<>(false, "没有获取到奖励信息", "");
            }

            int type = userSignSetPO.getType();
            String data = userSignSetPO.getData();
            String cash = (type == UserSignTypeEnum.CASH.getState() ? data : "0");
            String score = (type == UserSignTypeEnum.SCORE.getState() ? data : "0");

            UserSignPO po = new UserSignPO(franchiseeId, shopCode, score, cash, new Date());
            //签到存库
            log.info("【签到dubbo服务】 -> userSignService.sign(po) -> po:" + JSONObject.toJSONString(po));
            boolean result = userSignService.sign(po);
            if (result) {
                //2:更新连续签到天数
                int day = DateUtil.getDay(lastSignDay, new Date());
                log.info("DateUtil.getDay(lastSignDay, new Date()) -> day:" + day + "  lastSignDay:" + lastSignDay);

                if (day == 1) {
                    //连续签到
                    int continueDay = signDays + 1;
                    String dayTime = DateUtil.formatDate(new Date());
                    log.info("【签到dubbo服务】 -> 连续签到 -> franchiseeId:" + franchiseeId + "  dayTime:" + dayTime + "  continueDay:" + continueDay);
                    userSignService.updateContinueDayToAdd(franchiseeId, dayTime, continueDay);
                } else {
                    //连续签到中断
                    userSignService.updateContinueDayToStart(franchiseeId);
                }
                //奖励入库,加锁
                saveAward(shopCode, score, cash);
                //积分同步
                return synchronizationIntegral(shopCode, score, data, type, franchiseeId);
            }
        } catch (Exception e) {
            log.error("【签到dubbo服务】 -> 异常 -> " + e.getMessage());
            e.printStackTrace();
            return new Response<>(false, PublicEnum.ERROR_MSG.getCode(), PublicEnum.ERROR_CODE.getCode());
        }
        return new Response<>(false, PublicEnum.ERROR_MSG.getCode(), PublicEnum.ERROR_CODE.getCode());
    }

    @Override
    public Response<UserSignInfoDTO> getSignArray(UserSignDTO dto) {
        log.info("【签到dubbo服务】 -> Response<UserSignInfoDTO> getSignArray(UserSignDTO dto) -> 传入参数 -> UserSignDTO:" + JSONObject.toJSONString(dto));
        try {
            if (dto == null) {
                return new Response<>(false, "getSignArray -> UserSignDTO -> 传入参数为空", "");
            }
            String shopCode = dto.getShopCode();
            String franchiseeId = dto.getUserId(); //加盟商编号
            if (StringUtils.isEmpty(shopCode)) {
                return new Response<>(false, "getSignArray -> UserSignDTO -> shopCode不能为空", "");
            }
            if (StringUtils.isEmpty(franchiseeId)) {
                return new Response<>(false, "getSignArray -> UserSignDTO -> franchiseeId不能为空", "");
            }

            UserSignInfoDTO userSignInfoDTO = new UserSignInfoDTO();
            UserSignSetPO userSignSetPO = userSignService.getSignSet();
            //获取总的签到奖励
            Double totalData = getTotalData(franchiseeId);

            if (totalData == null) {
                totalData = 0.00;
            }
            //查询签到活动说明
            DecimalFormat df = new DecimalFormat("#.00");
            String data = df.format(totalData);
            String reqard = "元";
            if (userSignSetPO.getType() == UserSignTypeEnum.SCORE.getState()) {
                reqard = "积分";
                data = String.valueOf(totalData.intValue());
            }
            String ContinueSignDays = "0";
            userSignInfoDTO.setCurrentDate(DateUtil.formatDate(new Date()));
            userSignInfoDTO.setSignReward(data);
            userSignInfoDTO.setSignMsg(userSignSetPO.getContent());
            userSignInfoDTO.setRewardUtil(reqard);
            userSignInfoDTO.setContinueSignDays(ContinueSignDays);
            userSignInfoDTO.setIsCurrentDateSign(0);

            Date startDay = DateUtil.getStartOfMonth();
            Date endDay = DateUtil.getEndOfMonth();
            //查询当月签到详情
            shopCode = getShopCodeByFranchiseeIdForUpToSign(franchiseeId);
            List<UserSignPO> signDayArray = userSignService.getSignArray(shopCode, startDay, endDay);
            if (CollectionUtils.isEmpty(signDayArray)) {
                UserSignPO lastSignPO = userSignService.getLastSignDayByShopCode(shopCode);
                userSignInfoDTO.setContinueSignArrayDays(new Integer[0]);
                if (lastSignPO == null) {
                    return new Response<>(true, userSignInfoDTO);
                }
                Date lastSignDay = lastSignPO.getCreateTime();
                if (DateUtil.getDay(lastSignDay, new Date()) == 1) {
                    ContinueSignDays = String.valueOf(lastSignPO.getContinueDay());
                    userSignInfoDTO.setContinueSignDays(ContinueSignDays);
                    return new Response<>(true, userSignInfoDTO);
                }
                return new Response<>(true, userSignInfoDTO);
            }
            List<Integer> signDayList = new ArrayList<>();
            for (UserSignPO str : signDayArray) {
                log.info("返回签到情况：" + str);
                if (DateUtil.getDay(str.getCreateTime(), new Date()) <= 1) {
                    ContinueSignDays = String.valueOf(str.getContinueDay());
                }
                if (DateUtil.getDay(str.getCreateTime(), new Date()) == 0) {
                    userSignInfoDTO.setIsCurrentDateSign(1);
                }
                Date createTime = str.getCreateTime();
                String dayStar = DateUtil.formatDate(createTime);
                signDayList.add(Integer.parseInt(dayStar.substring(dayStar.length() - 2)));
            }
            Integer[] arry = signDayList.toArray(new Integer[signDayList.size()]);
            userSignInfoDTO.setContinueSignArrayDays(arry);
            userSignInfoDTO.setContinueSignDays(ContinueSignDays);
            log.info("UserSignDubboService -> getSignArray(UserSignDTO dto) -> 返回数据 -> userSignInfoDTO:" + userSignInfoDTO);
            return new Response<>(true, userSignInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, PublicEnum.ERROR_MSG.getCode(), PublicEnum.ERROR_CODE.getCode());
        }
    }

    /**
     * 获取总签到奖励，包含加盟商下所有门店
     *
     * @param franchiseeId
     * @return
     */
    private Double getTotalData(String franchiseeId) {
        log.info("【获取签到历史】 -> franchiseeId:" + franchiseeId);
        List<String> shopCodeList = getShopCodeListByFranchiseeId(franchiseeId);
        return userSignService.getAwardTotalByShopCodeList(shopCodeList);
    }

    @Override
    public Response insertSignSet(UserSignSetDTO dto) {

        return null;
    }

    @Override
    public UserSignSetDTO getUserSignSetInfo() {
        UserSignSetDTO dto = new UserSignSetDTO();
        try {
            UserSignSetPO po = userSignService.getSignSet();
            BeanUtils.copyProperties(dto, po);
        } catch (Exception e) {
            log.error("getUserSignInfo() -> Exception:" + e.getMessage());
            return null;
        }
        return dto;
    }

    @Override
    public boolean updateUserSignSet(UserSignSetDTO userSignSetDTO) {
        if (userSignSetDTO == null) {
            return false;
        }
        UserSignSetPO po = new UserSignSetPO(userSignSetDTO.getType(), userSignSetDTO.getData(), userSignSetDTO.getContent());
        return userSignService.updateUserSignSet(po);
    }

    private String getAlert(String data, int type) {
        if (type == UserSignTypeEnum.CASH.getState()) {
            return "恭喜获得" + data + "元现金";
        }
        if (type == UserSignTypeEnum.SCORE.getState()) {
            return "恭喜获得" + data + "积分";
        }
        return "";
    }

    /**
     * 1、根据加盟商编号获取用户最后签到信息
     * 2、如果没有获取到，根据获取加盟商下签到数最多的门店的最后签到信息
     *
     * @param franchiseeId
     * @return
     */
    private SignDay getLastSignDay(String franchiseeId) {
        UserSignPO lastSignPO = null;
        //根据加盟商编号获取最后签到记录
        lastSignPO = userSignService.getLastSignDayByFranchiseeId(franchiseeId);
        log.info("签到dubbo服务 -> userSignService.getLastSignDay(shopCode) -> UserSignPO lastSignPO:" + lastSignPO + "  franchiseeId:" + franchiseeId);
        Date lastSignDay = new Date();
        int signDays = 0;
        if (lastSignPO != null) {
            lastSignDay = lastSignPO.getCreateTime();
            signDays = lastSignPO.getContinueDay();
            if (lastSignDay != null) {
                if (DateUtil.getDay(lastSignDay, new Date()) == 0) {
                    log.info("签到dubbo服务 -> 当前用户今天已经签过到了 -> franchiseeId:" + franchiseeId);
                    return null;
                }
            } else {
                lastSignDay = new Date();
            }
        }
        return new SignDay(lastSignDay, signDays);
    }

    /**
     * 根据加盟商编号获取签到数最多的门店
     *
     * @param franchiseeId
     * @return
     */
    private String getShopCodeByFranchiseeIdForUpToSign(String franchiseeId) {
        List<String> shopCodeList = getShopCodeListByFranchiseeId(franchiseeId);
        if (CollectionUtils.isEmpty(shopCodeList)) {
            return "";
        }
        return userSignService.getMostAwardedShopCode(shopCodeList);
    }

    /**
     * 获取门店编号集合
     *
     * @param franchiseeId
     * @return
     */
    private List<String> getShopCodeListByFranchiseeId(String franchiseeId) {
        Response<List<StoreDto>> storeDtoResponse = organizationService.queryStoresByFranchiseeId(franchiseeId);
        if (storeDtoResponse == null || !storeDtoResponse.isSuccess()) {
            return new ArrayList<>();
        }
        List<StoreDto> storeDtoList = storeDtoResponse.getResultObject();
        if (CollectionUtils.isEmpty(storeDtoList)) {
            return new ArrayList<>();
        }
        List<String> shopCodeList = new ArrayList<>();
        for (StoreDto dto : storeDtoList) {
            log.info("shop_code:" + dto.getId());
            log.info("【获取签到历史】 -> 获取门店编号集合 shop_code:" + dto.getId());
            shopCodeList.add(dto.getId());
        }
        return shopCodeList;
    }

    /**
     * 奖励入库,加锁
     *
     * @param shopCode
     * @param score
     * @param cash
     */
    private void saveAward(String shopCode, String score, String cash) {
        try {
            boolean isLock = distributedLock.tryLock(shopCode, score, 500, TimeUnit.MILLISECONDS);
            log.info("签到奖励入库 -> 获取锁：isLock：" + isLock + "  key:" + shopCode + "  value:" + score);
            if (isLock) {
                userSignService.saveAward(shopCode, cash, score);
            }
        } catch (InterruptedException e) {
            log.error("签到奖励入库 -> 加锁异常 message：" + e.getMessage());
        } finally {
            distributedLock.unLock(shopCode, score);
        }
    }

    /**
     * 积分同步操作
     *
     * @param shopCode
     * @param score
     * @param data
     * @param type
     * @return
     */
    private Response synchronizationIntegral(String shopCode, String score, String data, int type, String franchiseeId) throws Exception {

        MemberScoreInfoPoDto scoreDto = new MemberScoreInfoPoDto();
        scoreDto.setCreateTime(new Timestamp(System.currentTimeMillis()));/**创建时间*/
        scoreDto.setRelateSystemCode("code1");           /**关联系统编码*/
        scoreDto.setRelateSystemName("小超人系统"); /**关联系统名称*/
        scoreDto.setMemberCode(franchiseeId); /** 会员编号*/
        scoreDto.setScore(Long.parseLong(score)); /**积分*/
        scoreDto.setScoreType("SIGN"); /**1 签到 2 任务*/
        scoreDto.setShopCode(shopCode);
        scoreDto.setChangeReason("签到");
        scoreDto.setBusinessIdentity(UUID.randomUUID().toString().replaceAll("-", ""));/**唯一标识 不重复唯一即可*/

        log.info("[签到积分同步] -> memberScoreDubboService.addScore(scoreDto) -> 传入参数 scoreDto：" + JSONObject.toJSONString(scoreDto));
        Response<Boolean> response = memberScoreDubboService.addScore(scoreDto);
        if (response == null) {
            log.error("会员中心积分同步失败 ->  memberScoreDubboService.addScore(scoreDto) -> response == null");
            throw new Exception();
        }
        if (!response.isSuccess()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("[签到积分同步] -> response:" + JSONObject.toJSONString(response));
            throw new Exception();
        }
        log.info("[签到积分同步]  Call the remote method {MemberScoreDubboService.addScore(MemberScoreInfoPoDto:" + scoreDto + ")} ");
        return new Response<>(true, getAlert(data, type));
    }

}
