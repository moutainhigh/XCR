package com.yatang.xc.xcr.biz.mission.dubboservice.imp;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.oc.b.member.biz.core.dto.MemberScoreInfoPoDto;
import com.yatang.xc.oc.b.member.biz.core.dubboservice.MemberScoreDubboService;
import com.yatang.xc.xcr.biz.mission.bo.MissionExecuteQuery;
import com.yatang.xc.xcr.biz.mission.bo.UpdateStatusQuery;
import com.yatang.xc.xcr.biz.mission.domain.*;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionBuyDto;
import com.yatang.xc.xcr.biz.mission.dto.center.MissionExecuteQueryDto;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AttachmentDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.AwardInfoDto;
import com.yatang.xc.xcr.biz.mission.dto.manage.UserDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionExecuteDubboService;
import com.yatang.xc.xcr.biz.mission.enums.*;
import com.yatang.xc.xcr.biz.mission.flow.MissionExecuteFlowService;
import com.yatang.xc.xcr.biz.mission.redis.lock.DistributedLockManger;
import com.yatang.xc.xcr.biz.mission.schedule.thread.IAuditMissionThread;
import com.yatang.xc.xcr.biz.mission.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 任务dubbo服务
 *
 * @author yangqingsong
 */
@Service("missionExecuteDubboService")
@Transactional(propagation = Propagation.REQUIRED)
public class MissionExecuteDubboServiceImpl implements MissionExecuteDubboService {

	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private MissionService missionService;

	@Autowired
	private MissionAwardService missionAwardService;

	@Autowired
	private MissionRelatedService missionRelatedService;

	@Autowired
	private MissionExecuteService missionExecuteService;

	@Autowired
	private MissionExecuteFlowService executeFlowService;

	@Value("${mission.enableBpmAwardAudit}")
	private boolean enableBpmAwardAudit;

	@Value("${mission.bpmUseMock}")
	private boolean bpmUseMock;

	@Resource(name = "auditMissionTaskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private DistributedLockManger distributedLock;

	@Autowired
	private MemberScoreDubboService memberScoreDubboService;

	@Autowired
	private OrganizationService organizationService;

	/**
	 * @param
	 * @return Response<List<ViewMissionExecuteDto>>: 返回值类型
	 * @throws @Description：条件查询任务执行（可执行任务）信息
	 * @return: 返回结果描述
	 */
	@Override
	public Response<List<ViewMissionExecuteDto>> queryMissionExecute(MissionExecuteQueryDto queryDto) {
		Response<List<ViewMissionExecuteDto>> result = new Response<List<ViewMissionExecuteDto>>();
		result.setSuccess(false);
		try {
			if (!checkExecuteQuery(queryDto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteQuery query = new MissionExecuteQuery();
			BeanUtils.copyProperties(queryDto, query);
			List<MissionExecuteInfoPO> list = missionExecuteService.queryMissionExecute(query);
			List<ViewMissionExecuteDto> dtos = new ArrayList<ViewMissionExecuteDto>();
			if (list != null && !list.isEmpty()) {

				for (MissionExecuteInfoPO execute : list) {
					dtos.add(executeFlowService.buildViewExecute(execute));
				}

			}
			result.setSuccess(true);
			result.setResultObject(dtos);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * @param
	 * @return Response<List<ViewMissionExecuteDto>>: 返回值类型
	 * @throws @Description：条件查询任务执行（可执行任务）信息：先按时间排序,再按关联任务级别排序
	 * @return: 返回结果描述
	 */
	@Override
	public Response<List<ViewMissionExecuteDto>> queryMissionExecuteOrderByRelated(MissionExecuteQueryDto queryDto) {
		Response<List<ViewMissionExecuteDto>> result = new Response<List<ViewMissionExecuteDto>>();
		result.setSuccess(false);
		try {
			if (!checkExecuteQuery(queryDto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteQuery query = new MissionExecuteQuery();
			BeanUtils.copyProperties(queryDto, query);
			query.setOrderBy(EnumMissionOrderBy.TYPE_DEFAULT.getCode());
			List<MissionExecuteInfoPO> list = missionExecuteService.queryMissionExecuteOrderByRelated(query);
			List<ViewMissionExecuteDto> dtos = new ArrayList<ViewMissionExecuteDto>();
			if (list != null && !list.isEmpty()) {

				for (MissionExecuteInfoPO execute : list) {
					log.info(
							"查询任务列表info数据 -> missionExecuteService.queryMissionExecuteOrderByRelated(query); -> execute:"
									+ execute);
					ViewMissionExecuteDto dto = executeFlowService.buildViewExecute(execute);
					dtos.add(dto);
					log.info("ViewMissionExecuteDto:查询任务列表dtos数据 -> dto:" + dto);
				}
			}
			result.setSuccess(true);
			result.setResultObject(dtos);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * @param executeId
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：获取可执行任务的信息
	 * @return: 返回结果描述
	 */
	@Override
	public Response<ViewMissionExecuteDto> queryMissionExecuteById(String missonId) {
		Response<ViewMissionExecuteDto> result = new Response<ViewMissionExecuteDto>();
		result.setSuccess(false);
		try {
			MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(missonId);
			if (execute == null || execute.isDeleted()
					|| execute.getStatus().equals(EnumMissionExecuteStatus.STATUS_INVALID.getCode())) {
				result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
				return result;
			}
			ViewMissionExecuteDto dto = executeFlowService.buildViewExecute(execute);

			result.setResultObject(dto);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	private boolean checkExecuteQuery(MissionExecuteQueryDto queryDto) {
		if (queryDto == null) {
			return false;
		}
		if (queryDto.getOrderBy() != null && !EnumMissionOrderBy.toMap().containsKey(queryDto.getOrderBy())) {
			return false;
		}
		return true;
	}

	/**
	 * @param
	 * @return Response<Integer>: 返回值类型
	 * @throws @Description：条件查询任务执行（可执行任务）数目
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Integer> queryMissionExecuteCount(MissionExecuteQueryDto queryDto) {
		Response<Integer> result = new Response<Integer>();
		result.setSuccess(false);
		try {
			if (!checkExecuteQuery(queryDto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteQuery query = new MissionExecuteQuery();
			BeanUtils.copyProperties(queryDto, query);
			int count = missionExecuteService.queryMissionExecuteCount(query);
			result.setSuccess(true);
			result.setResultObject(count);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * @param executeId
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：执行任务流程
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Boolean> executeMission(String executeId) {
		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		try {
			boolean success = true;
			if (!checkLongId(executeId)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(executeId);
			if (execute == null || execute.isDeleted()
					|| execute.getStatus().equals(EnumMissionExecuteStatus.STATUS_INVALID.getCode())) {
				result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
				return result;
			}
			MissionInfoPO info = missionService.findById(execute.getMissonInfoId());

			if (info != null) {
				if (StringUtils.isEmpty(execute.getRule())) {
					execute.setRule(info.getRule());
				}
				if (!EnumMissionExecuteStatus.STATUS_INIT.getCode().equals(execute.getStatus())
						&& !EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode().equals(execute.getStatus())) {
					result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_NOT_INIT.getCode());
					result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_NOT_INIT.getMessage());
					return result;
				}
				if (info.isRelated() && !checkRelated(execute.getMerchantId(), execute.getMissonInfoId())) {
					result.setCode(EnumError.ERROR_BUSINESS_RELATED_EXECUTE.getCode());
					result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_EXECUTE.getMessage());
					return result;
				}
				if (info.isManualAudit()) {
					UpdateStatusQuery query = new UpdateStatusQuery();
					query.setId(Long.valueOf(execute.getId()));
					query.setStatus(EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode());
					query.setOldStatus(new String[]{EnumMissionExecuteStatus.STATUS_INIT.getCode(),
							EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode()});
					query.setReason(EnumReason.REASON_MANUAL_AUDIT.getCode());
					missionExecuteService.updateMissionExecuteStatus(query);
					//已经不再需求开启 bpm 流程
					//					if (!bpmUseMock) {
					//						executeFlowService.startBpmProcess(execute.getId(), info.getMissonTemplateCode(), companyId,
					//								execute.getMerchantId(), shopName);
					//					}
				} else {
					// 系统审核 此时可不做操作 等待drools判断 是否任务完成,也可直接调drools判断一次
					// 调用 drools
					success = executeFlowService.calculateMission(execute);
				}
			}

			result.setResultObject(success);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	private boolean checkLongId(String missionId) {
		if (StringUtils.isEmpty(missionId)) {
			return false;
		}
		if (!StringUtils.isNumeric(missionId)) {
			return false;
		}
		return true;
	}

	/**
	 * @param List<AttachmentDto>
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：保存附件信息
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Boolean> saveMissionAttachment(List<AttachmentDto> as) {
		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		try {
			if (!checkAttachments(as)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}

			for (AttachmentDto a : as) {
				MissionAttachmentPO aPO = new MissionAttachmentPO();
				BeanUtils.copyProperties(a, aPO);
				aPO.setAttachmentCode(a.getType() + ":" + a.getMissionExecuteId() + ":" + a.getName());
				missionExecuteService.saveAttachment(aPO);
			}

			result.setResultObject(true);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	private boolean checkAttachments(List<AttachmentDto> as) {
		if (as == null || as.isEmpty()) {
			return false;
		}
		for (AttachmentDto a : as) {
			if (StringUtils.isEmpty(a.getName())) {
				return false;
			}
			if (StringUtils.isEmpty(a.getMissionExecuteId()) || !StringUtils.isNumeric(a.getMissionExecuteId())) {
				return false;
			}
			if (StringUtils.isEmpty(a.getType())) {
				return false;
			}
			if (StringUtils.isEmpty(a.getUrl())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param executeId
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：自动审核（系统审核） 任务是否完成:与 executeMission 接口方法部分内容重复暂时不使用
	 * @return: 返回结果描述
	 */
	// @Override
	public Response<Boolean> autoAuditMission(String executeId) {
		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		if (!checkLongId(executeId)) {
			result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
			result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
			return result;
		}
		MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(executeId);
		return autoAuditMission(execute);
	}

	/**
	 * @param executeId
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：自动审核（系统审核） 任务是否完成:与 executeMission 接口方法部分内容重复暂时不使用
	 * @return: 返回结果描述
	 */
	// @Override
	public Response<Boolean> autoAuditMission(MissionExecuteInfoPO execute) {
		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		try {
			if (execute == null) {
				result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
				return result;
			}
			MissionInfoPO info = missionService.findById(execute.getMissonInfoId());
			if (info.isManualAudit()) {
				result.setCode(EnumError.ERROR_BUSINESS_NEED_MANUAL_AUDIT.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_NEED_MANUAL_AUDIT.getMessage());
				return result;
			}
			if (info.isRelated() && !checkRelated(execute.getMerchantId(), execute.getMissonInfoId())) {
				result.setCode(EnumError.ERROR_BUSINESS_RELATED_EXECUTE.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_RELATED_EXECUTE.getMessage());
				return result;
			}
			result.setResultObject(executeFlowService.calculateMission(execute));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * 使用缓存
	 *
	 * @Description：自动审核（系统审核） 任务是否完成 @param merchantId @return: 返回结果描述 @return
	 * Response<Boolean>: 返回值类型 @throws
	 */
	@Override
	public Response<List<String>> autoAuditMissionByMerchantId(String merchantId) {

		return autoAuditMissionByMerchantId(merchantId, true);
	}

	/**
	 * 可以使用缓存
	 *
	 * @param merchantId @return: 返回结果描述
	 * @return Response<Boolean>: 返回值类型
	 * @throws
	 * @Description：自动审核（系统审核） 任务是否完成
	 */
	private Response<List<String>> autoAuditMissionByMerchantId(String merchantId, boolean useCache) {
		Response<List<String>> result = new Response<List<String>>();
		List<String> successExecute = new ArrayList<String>();
		result.setSuccess(false);
		try {
			if (StringUtils.isEmpty(merchantId)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}

			RedisService redisService = executeFlowService.getRedisService();
			if (redisService != null) {
				String hasCache = redisService.getItem("autoAuditMissionByMerchantId_" + merchantId);
				if (useCache && "true".equals(hasCache)) {
					log.info("redisService hasCache:" + hasCache + " merchantId :" + merchantId);
					result.setResultObject(successExecute);
					result.setSuccess(true);
					return result;
				} else {
					redisService.saveItem("autoAuditMissionByMerchantId_" + merchantId, 60, "true");
				}
			}

			log.info("run autoAuditMissionByMerchantId: merchantId :" + merchantId);
			MissionExecuteQuery query = new MissionExecuteQuery();
			query.setMerchantId(merchantId);
			query.setStatus(EnumMissionExecuteStatus.STATUS_INIT.getCode());

			List<MissionExecuteInfoPO> executes = missionExecuteService.queryMissionExecute(query);
			if (executes != null) {
				List<Future<String>> futures = new ArrayList<Future<String>>();
				for (MissionExecuteInfoPO execute : executes) {
					MissionInfoPO info = missionService.findById(execute.getMissonInfoId());
					// 小超课堂任务不需要规则引擎判断是否成功， 其逻辑为课堂直接调任务接口置为完成
					if (info != null && !info.isManualAudit() && !EnumMissionType.MISSION_TYPE_STUDY.getCode().equals(execute.getStatus())) {
						if (info.isRelated() && !checkRelated(execute.getMerchantId(), execute.getMissonInfoId())) {
							continue;
						}
						// 因为查询用的是不带 blob的查询，所以此次没有rule的值，所以从info上拷贝一份，此代码并非多余
						execute.setRule(info.getRule());
						IAuditMissionThread thread = executeFlowService.initIAuditMissionThread(execute);
						futures.add(taskExecutor.submit(thread));
					}
				}

				// 阻塞获取异步执行结果
				for (Future<String> future : futures) {
					String successExecuteId = future.get();
					if (!StringUtils.isEmpty(successExecuteId)) {
						successExecute.add(successExecuteId);
					}
				}
			}
			result.setResultObject(successExecute);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * 不使用缓存
	 *
	 * @param merchantId
	 * @return
	 */
	@Override
	public Response<List<String>> autoAuditMissionByMerchantIdWithoutCache(String merchantId) {
		return autoAuditMissionByMerchantId(merchantId, false);
	}

	/**
	 * @param executeId
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：领取任务奖励
	 * @return: 返回结果描述
	 */
	@Override
	public synchronized Response<List<AwardInfoDto>> goAndGetAward(String executeId, String merchantId) {

		Response<List<AwardInfoDto>> result = new Response<List<AwardInfoDto>>();
		result.setSuccess(false);

		try {
			boolean isLock = distributedLock.tryLock(merchantId, executeId, 500, TimeUnit.MILLISECONDS);
			if (isLock) {
				// 需要加锁的代码
				try {
					if (!checkLongId(executeId)) {
						result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
						result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
						return result;
					}
					MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(executeId);
					Date now = new Date();

					if (execute == null) {
						result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
						result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
						return result;
					}
					if (!EnumMissionExecuteStatus.STATUS_FINISHED.getCode().equals(execute.getStatus())) {
						result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_UNFINISHED.getCode());
						result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_UNFINISHED.getMessage());
						return result;
					}
					if (now.before(execute.getStartTime()) || now.after(execute.getEndTime())) {
						result.setCode(EnumError.ERROR_BUSINESS_MISSION_EXPIRED.getCode());
						result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_EXPIRED.getMessage());
						return result;
					}

					List<MissionExecuteAwardPO> awards = missionAwardService.queryExecuteAwardByExecuteId(execute.getId());
					List<AwardInfoDto> awardDtos = new ArrayList<AwardInfoDto>();
					List<MemberScoreInfoPoDto> scores = new ArrayList<>();
					if (awards != null && !awards.isEmpty()) {
						for (MissionExecuteAwardPO award : awards) {
							log.info("获取的奖励有 -> execute.getId()" + execute.getId() + "  award:" + JSONObject.toJSONString(award));
							AwardInfoDto awrdDto = new AwardInfoDto();
							BeanUtils.copyProperties(award, awrdDto);
							log.info("获取的奖励有 -> execute.getId()" + execute.getId() + "  awrdDto:" + JSONObject.toJSONString(awrdDto));
							awardDtos.add(awrdDto);

							/**奖励类型为积分*/
							/**if (EnumGrantStyle.GRANT_STYLE_REAL_TIME.getCode().equals(award.getGrantStyle())) {*/
							if (EnumAwardType.AWARD_TYPE_SCORE.getCode().equals(award.getAwardType())) {
								/** 加盟商Id */
								Response<StoreDto> storeById = organizationService.queryStoreById(merchantId);
								if (storeById != null && storeById.isSuccess()) {
									MemberScoreInfoPoDto score = new MemberScoreInfoPoDto();
									score = new MemberScoreInfoPoDto();
									score.setCreateTime(new Timestamp(System.currentTimeMillis()));
									score.setRelateSystemCode("code1"); // 关联系统编码
									score.setRelateSystemName("小超人系统"); // 关联系统名称
									score.setMemberCode(storeById.getResultObject().getFranchiseeId()); // 会员编号
									score.setScore(award.getGrantNum().longValue()); // 积分
									score.setScoreType("MISSION"); // 1 签到 2 任务
									score.setShopCode(merchantId);
									score.setBusinessIdentity(UUID.randomUUID().toString().replaceAll("-", ""));/**唯一标识 不重复唯一即可*/
									MissionInfoPO info = missionService.findById(execute.getMissonInfoId());
									score.setChangeReason(info.getName());
									scores.add(score);
								} else {
									log.error("The remote method OrganizationService.queryStoreById(merchantId:" + merchantId + ") excute fail");
								}
							}
						}
					}

					UpdateStatusQuery query = new UpdateStatusQuery();
					query.setId(Long.valueOf(execute.getId()));
					query.setStatus(EnumMissionExecuteStatus.STATUS_END.getCode());
					query.setOldStatus(new String[]{EnumMissionExecuteStatus.STATUS_FINISHED.getCode()});
					missionExecuteService.updateMissionExecuteStatus(query);
					executeFlowService.backMissionExecuteToHistory(execute.getId(), awards);

					// 奖励入库
					saveAward(merchantId, awardDtos);
					//积分同步
					if (null != scores && scores.size() > 0) {
						log.info("\n[任务积分同步] -> memberScoreDubboService.addScoreBatch(scores) -> 传入参数 score：" + JSONObject.toJSONString(scores));
						Response<Boolean> response = memberScoreDubboService.addScoreBatch(scores);
						if (response == null || !response.isSuccess()) {
							log.info("\n[任务积分同步] -> memberScoreDubboService.addScoreBatch(scores) -> 返回值 response：" + JSONObject.toJSONString(response));
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return new Response<>(false, "积分同步失败", "");
						}
					}
					
					result.setResultObject(awardDtos);
					result.setSuccess(true);
					return result;
				} catch (Exception e) {
					result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
					result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
					log.error(ExceptionUtils.getFullStackTrace(e));
				} finally {
					// 然后释放锁
					distributedLock.unLock(merchantId, executeId);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param MissionBuyDto
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：购买指定商品确认
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Boolean> buyProductConfirm(MissionBuyDto dto) {
		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		try {
			if (!checkMissionBuyDto(dto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(dto.getMissionExecuteId());
			if (execute == null || execute.isDeleted()
					|| execute.getStatus().equals(EnumMissionExecuteStatus.STATUS_INVALID.getCode())) {
				result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
				return result;
			}
			List<MissionBuyListPO> buyList = missionService.queryBuyListBy(dto.getLogin(), execute.getMissonInfoId(),
					dto.getMissionExecuteId());
			if (buyList == null || buyList.isEmpty()) {
				MissionBuyListPO buy = new MissionBuyListPO();
				BeanUtils.copyProperties(dto, buy);
				buy.setMissionId(String.valueOf(execute.getMissonInfoId()));
				buy.setExecuteId(dto.getMissionExecuteId());
				missionService.createBuyList(buy);
			} else {
				MissionBuyListPO buy = buyList.get(0);
				BeanUtils.copyProperties(dto, buy);
				buy.setMissionId(String.valueOf(execute.getMissonInfoId()));
				buy.setExecuteId(dto.getMissionExecuteId());
				missionService.updateBuyList(buy);
			}
			result.setResultObject(true);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * @param MissionBuyDto
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：进入任务中心调用：按门店初始化可执行任务
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Boolean> initMissionExecuteByMerchantId(UserDto userDto) {
		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		try {
			if (!checkUserDto(userDto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}

			MissionUserHistoryPO user = missionService.selectUserHistoryByMerchantId(userDto.getMerchantId());
			executeFlowService.createMissionExecute(userDto.getMerchantId());
			if (user == null) {
				user = new MissionUserHistoryPO();
				BeanUtils.copyProperties(userDto, user);
				missionService.insertUserHistory(user);
			} else if (checkUserChanged(userDto, user)) {
				BeanUtils.copyProperties(userDto, user);
				missionService.updateUserHistory(user, new Date());
			}
			result.setResultObject(true);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	private boolean checkUserChanged(UserDto userDto, MissionUserHistoryPO user) {
		if (user.getCompanyId() == null || !user.getCompanyId().equals(userDto.getCompanyId())) {
			return true;
		}
		if (user.getLogin() == null || !user.getLogin().equals(userDto.getLogin())) {
			return true;
		}
		if (user.getMerchantId() == null || !user.getMerchantId().equals(userDto.getMerchantId())) {
			return true;
		}
		if (user.getRealName() == null || !user.getRealName().equals(userDto.getRealName())) {
			return true;
		}
		if (user.getRegistrationId() == null || !user.getRegistrationId().equals(userDto.getRegistrationId())) {
			return true;
		}
		if (user.getShopName() == null || !user.getShopName().equals(userDto.getShopName())) {
			return true;
		}
		if (user.getUserName() == null || !user.getUserName().equals(userDto.getUserName())) {
			return true;
		}
		return false;
	}

	private boolean checkUserDto(UserDto userDto) {
		if (userDto == null) {
			return false;
		}
		if (StringUtils.isEmpty(userDto.getLogin())) {
			return false;
		}
		if (StringUtils.isEmpty(userDto.getMerchantId())) {
			return false;
		}
		if (StringUtils.isEmpty(userDto.getRealName())) {
			return false;
		}
		if (StringUtils.isEmpty(userDto.getUserName())) {
			return false;
		}
		if (StringUtils.isEmpty(userDto.getRegistrationId())) {
			return false;
		}
		// 等前台修改后 加上
		if (StringUtils.isEmpty(userDto.getShopName())) {
			return false;
		}
		if (StringUtils.isEmpty(userDto.getCompanyId())) {
			return false;
		}
		return true;
	}

	private boolean checkMissionBuyDto(MissionBuyDto dto) {
		if (dto == null) {
			return false;
		}
		if (StringUtils.isEmpty(dto.getLogin())) {
			return false;
		}
		if (StringUtils.isEmpty(dto.getMissionExecuteId())) {
			return false;
		}
		if (StringUtils.isEmpty(dto.getOrderId())) {
			return false;
		}
		if (StringUtils.isEmpty(dto.getProductIds())) {
			return false;
		}
		return true;
	}

	/**
	 * @param execute
	 * @param info
	 * @return boolean: 返回值类型
	 * @throws @Description：检查其关联任务是否完成
	 * @return: 返回结果描述
	 */
	private boolean checkRelated(String merchantId, Long infoId) {
		List<MissionRelatedDetailPO> details = missionRelatedService.findRelatedDetailsByMissionId(infoId);
		if (details == null) {
			return true;
		}
		for (MissionRelatedDetailPO detail : details) {
			int level = detail.getLevel();
			if (level > 1) {
				MissionRelatedPO relate = missionRelatedService.findById(detail.getMissonRelatedId());
				if (relate == null) {
					return true;
				}
				List<MissionRelatedDetailPO> itemDetails = missionRelatedService
						.findRelatedDetailsByRelatedId(relate.getId());
				if (itemDetails == null) {
					return true;
				}
				for (MissionRelatedDetailPO itemDetail : itemDetails) {
					if (itemDetail.getLevel() < level) {
						List<MissionExecuteInfoPO> itemExecutes = missionExecuteService
								.queryExecuteMissionByMissionIdAndMerchantId(itemDetail.getMissonInfoId(), merchantId);
						if (itemExecutes != null && itemExecutes.size() == 1) {
							MissionExecuteInfoPO itemExecute = itemExecutes.get(0);
							if (EnumMissionExecuteStatus.STATUS_INIT.getCode().equals(itemExecute.getStatus())
									|| EnumMissionExecuteStatus.STATUS_UNFINISHED.getCode()
									.equals(itemExecute.getStatus())
									|| EnumMissionExecuteStatus.STATUS_MISSION_AUDIT.getCode()
									.equals(itemExecute.getStatus())) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * @param
	 * @return Response<List<ViewMissionExecuteDto>>: 返回值类型
	 * @throws @Description：条件查询任务执行（可执行任务）信息
	 * @return: 返回结果描述
	 */
	@Override
	public Response<List<ViewMissionExecuteDto>> queryMissionExecuteInHistory(MissionExecuteQueryDto queryDto) {
		Response<List<ViewMissionExecuteDto>> result = new Response<List<ViewMissionExecuteDto>>();
		result.setSuccess(false);
		try {
			if (!checkExecuteQuery(queryDto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteQuery query = new MissionExecuteQuery();
			BeanUtils.copyProperties(queryDto, query);
			List<MissionExecuteInfoPO> list = missionExecuteService.queryMissionExecuteInHistory(query);
			List<ViewMissionExecuteDto> dtos = new ArrayList<ViewMissionExecuteDto>();
			if (list != null && !list.isEmpty()) {

				for (MissionExecuteInfoPO execute : list) {
					dtos.add(executeFlowService.buildViewExecute(execute));
				}

			}
			result.setSuccess(true);
			result.setResultObject(dtos);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	/**
	 * @param
	 * @return Response<Integer>: 返回值类型
	 * @throws @Description：条件查询任务执行（可执行任务）数目
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Integer> queryMissionExecuteCountInHistory(MissionExecuteQueryDto queryDto) {
		Response<Integer> result = new Response<Integer>();
		result.setSuccess(false);
		try {
			if (!checkExecuteQuery(queryDto)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteQuery query = new MissionExecuteQuery();
			BeanUtils.copyProperties(queryDto, query);
			int count = missionExecuteService.queryMissionExecuteCountInHistory(query);
			result.setSuccess(true);
			result.setResultObject(count);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}


	/**
	 * @param executeId
	 * @return Response<Boolean>: 返回值类型
	 * @throws @Description：查询任务是否满足关联任务要求
	 * @return: 返回结果描述
	 */
	@Override
	public Response<Boolean> checkMissionRelatedAchieved(String executeId) {

		Response<Boolean> result = new Response<Boolean>();
		result.setSuccess(false);
		try {
			if (StringUtils.isEmpty(executeId)) {
				result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
				return result;
			}
			MissionExecuteInfoPO execute = missionExecuteService.findExecuteMissionById(executeId);
			if (execute == null) {
				result.setCode(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getCode());
				result.setErrorMessage(EnumError.ERROR_BUSINESS_MISSION_NOT_EXIST.getMessage());
				return result;
			}
			boolean checkResult = checkRelated(execute.getMerchantId(), execute.getMissonInfoId());
			result.setResultObject(checkResult);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setCode(EnumError.ERROR_SYSTEM_EXCEPTION.getCode());
			result.setErrorMessage(EnumError.ERROR_SYSTEM_EXCEPTION.getMessage());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	@Override
	public Response<Boolean> saveAward(String merchantId, List<AwardInfoDto> awardInfoDtos) {
		log.info("任务领取奖励 -> 传入参数：merchantId：" + merchantId + "  awardInfoDtos:" + awardInfoDtos);
		Response<Boolean> result = new Response<>();
		result.setSuccess(false);
		if (StringUtils.isEmpty(merchantId) || CollectionUtils.isEmpty(awardInfoDtos)) {
			result.setCode(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getCode());
			result.setErrorMessage(EnumError.ERROR_BUSINESS_EXECUTE_DATA_ERROR.getMessage());
			return result;
		}

		MissionAwardCollectPO missionAwardCollectPO = new MissionAwardCollectPO(0.00, 0.00, 0.00, 0.00, merchantId);
		for (AwardInfoDto dto : awardInfoDtos) {
			log.info("任务领取奖励 -> AwardInfoDto dto:" + dto);
			String awardType = dto.getAwardType();
			if ("SCORE".equals(awardType)) {
				missionAwardCollectPO.setMessionScoreTotal(dto.getGrantNum());
			}
			if ("CASH".equals(awardType)) {
				missionAwardCollectPO.setMessionCashTotal(dto.getGrantNum());
			}
		}
		log.info("任务领取奖励 -> missionAwardService.saveAward(missionAwardCollectPO):missionAwardCollectPO:"
				+ missionAwardCollectPO);
		boolean res = missionAwardService.saveAward(missionAwardCollectPO);
		log.info("missionAwardService.saveAward(missionAwardCollectPO) -> res:" + res);
		result.setResultObject(res);
		result.setSuccess(true);
		return result;
	}

	@Override
	public Response<Boolean> updateMissionExecute(String merchantId, String taskId) {
		log.info("奖励入库 -> 更新任务历史记录 -> merchantId:" + merchantId + "  taskId:" + taskId);
		Response<Boolean> result = new Response<>();
		result.setSuccess(false);
		Long missionExecuteId;
		try {
			missionExecuteId = Long.valueOf(taskId);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		List<MissionExecuteAwardPO> awards = missionAwardService.queryExecuteAwardByExecuteId(missionExecuteId);
		log.info("奖励入库 -> 更新任务历史记录 -> missionAwardService.queryExecuteAwardByExecuteId(missionExecuteId) awards:"
				+ awards);
		UpdateStatusQuery query = new UpdateStatusQuery();
		query.setId(Long.valueOf(taskId));
		query.setStatus(EnumMissionExecuteStatus.STATUS_END.getCode());
		query.setOldStatus(new String[]{EnumMissionExecuteStatus.STATUS_FINISHED.getCode()});
		log.info("奖励入库 -> 更新任务历史记录 -> query:" + query);
		missionExecuteService.updateMissionExecuteStatus(query);
		log.info("奖励入库 -> 更新任务历史记录 -> missionExecuteId:" + missionExecuteId + "  awards:" + awards);
		executeFlowService.backMissionExecuteToHistory(missionExecuteId, awards);
		result.setResultObject(true);
		result.setSuccess(true);
		return result;
	}

}
