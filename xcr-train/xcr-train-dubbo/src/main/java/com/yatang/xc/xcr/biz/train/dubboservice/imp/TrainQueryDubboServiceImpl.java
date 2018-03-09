package com.yatang.xc.xcr.biz.train.dubboservice.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.busi.common.resp.Response;
import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.train.converter.DateConverter;
import com.yatang.xc.xcr.biz.train.domain.TrainInfoPO;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.biz.train.enums.PublicEnum;
import com.yatang.xc.xcr.biz.train.service.TrainInfoService;

/**
 * @描述: 课堂管理只读dubbo服务实现
 * @作者: huangjianjun
 * @创建时间: 2017年3月30日-下午3:57:59 .
 * @版本: 1.0 .
 * @param <T>
 */
@Service("trainQueryDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class TrainQueryDubboServiceImpl implements TrainQueryDubboService {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private TrainInfoService service;

	@Override
	public Response<TrainInfoDTO> findOneTrain(Long id) {
		Response<TrainInfoDTO> res = new Response<TrainInfoDTO>();
		TrainInfoDTO dto = new TrainInfoDTO();
		TrainInfoPO po = service.findByPrimaryKey(id);
		try {
			ConvertUtils.register(new DateConverter(null), java.util.Date.class);
			BeanUtils.copyProperties(dto, po);
			res.setSuccess(true);
			res.setResultObject(dto);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

	@Override
	public Response<Map<String, Object>> getTrainList(TrainInfoDTO param, int pageNum, int pageSize) {
		Response<Map<String, Object>> res = new Response<Map<String, Object>>();
		List<TrainInfoDTO> rlist = new ArrayList<TrainInfoDTO>();
		TrainInfoDTO dto = null;
		try {
			ConvertUtils.register(new DateConverter(null), java.util.Date.class);
			PageInfo<TrainInfoPO> pageInfo = service.getTrainInfoPage(pageNum, pageSize,
					param != null ? param.getStatus() : null);
			for (TrainInfoPO po : pageInfo.getList()) {
				dto = new TrainInfoDTO();
				BeanUtils.copyProperties(dto, po);
				rlist.add(dto);
			}
			Map<String, Object> rmap = new HashMap<String, Object>();
			rmap.put("total", pageInfo.getTotal());
			rmap.put("data", rlist);
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(rmap);
		} catch (Exception e) {
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return res;
	}

	@Override
	public Response<List<Map<String, Object>>> getListPublishedTrain() {
		Response<List<Map<String, Object>>> res = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> rMap = null;
			List<TrainInfoPO> trains = service.getListPublishedTrain(PublicEnum.ONE.getCode());
			for (TrainInfoPO po : trains) {
				rMap = new HashMap<String, Object>();
				rMap.put(PublicEnum.ID.getCode(), po.getId());
				rMap.put(PublicEnum.NAME.getCode(), po.getName());
				rList.add(rMap);
			}
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(rList);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

	@Override
	public Response<Long> getTrainCount() {
		Response<Long> res = new Response<Long>();
		try {
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(service.getReleaseCount());
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

	@Override
	public Response<Boolean> checkNameExist(String name) {
		Response<Boolean> res = new Response<Boolean>();
		try {
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(service.checkNameExist(name));
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

	@Override
	public Response<Long> queryMaxClassReleaseTime() {
		Response<Long> res = new Response<Long>();
		try {
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(service.queryMaxClassReleaseTime());
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

}
