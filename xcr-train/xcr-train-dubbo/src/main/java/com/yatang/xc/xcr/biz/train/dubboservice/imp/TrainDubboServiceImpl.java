package com.yatang.xc.xcr.biz.train.dubboservice.imp;

import java.util.Date;

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
import com.yatang.xc.xcr.biz.train.converter.DateConverter;
import com.yatang.xc.xcr.biz.train.domain.TrainInfoPO;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainDubboService;
import com.yatang.xc.xcr.biz.train.enums.PublicEnum;
import com.yatang.xc.xcr.biz.train.service.TrainInfoService;

/**
 * @描述: 课堂管理dubbo服务
 * @作者: huangjianjun
 * @创建时间: 2017年3月28日-下午9:25:44 .
 * @版本: 1.0 .
 * @param <T>
 */
@Service("trainDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class TrainDubboServiceImpl implements TrainDubboService {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private TrainInfoService trainInfoService;

	@Override
	@SuppressWarnings("rawtypes")
	public Response editTrain(TrainInfoDTO dto) {
		Response<TrainInfoDTO> res = new Response<TrainInfoDTO>();
		TrainInfoPO po = new TrainInfoPO();
		try {
			ConvertUtils.register(new DateConverter(null), java.util.Date.class);
			BeanUtils.copyProperties(po, dto);
			trainInfoService.savaOrUpdate(po);
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(null);
		} catch (Exception e) {
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		return res;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response deleteMsg(Long id) {
		Response res = new Response();
		try {
			trainInfoService.deleteByPrimaryKey(id);
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(null);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Response setUpTrainMission(Long id) {
		Response res = new Response();
		try {
			TrainInfoPO po = new TrainInfoPO();
			po.setId(id);
			po.setModifyTime(new Date());
			trainInfoService.updateByPrimaryKey(po);
			res.setCode(PublicEnum.SUCCESS_CODE.getCode());
			res.setSuccess(true);
			res.setResultObject(null);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Response downShelfOrReleases(Long id, String status) {
		Response res = new Response();
		try {
			if(trainInfoService.downShelfOrReleases(id,status)){
				res.setCode(PublicEnum.SUCCESS_CODE.getCode());
				res.setSuccess(true);
				res.setResultObject(null);
			}
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			res.setCode(PublicEnum.ERROR_CODE.getCode());
			res.setSuccess(false);
			res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
		}
		return res;
	}
}
