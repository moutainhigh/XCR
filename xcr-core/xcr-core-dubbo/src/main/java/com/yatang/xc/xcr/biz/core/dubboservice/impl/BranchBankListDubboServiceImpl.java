package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.BeanConvertUtils;
import com.yatang.xc.xcr.biz.core.domain.BranchBankPO;
import com.yatang.xc.xcr.biz.core.domain.ResultBranchBankPO;
import com.yatang.xc.xcr.biz.core.dto.BranchBankDTO;
import com.yatang.xc.xcr.biz.core.dto.ResultBranchBankDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.BranchBankListDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.util.AdverEnum;
import com.yatang.xc.xcr.biz.core.service.BranchBankService;

/**
 * 
 * @author dongshengde
 *
 */
@Service("branchBankListDubboService")
@Transactional
public class BranchBankListDubboServiceImpl implements BranchBankListDubboService {
	protected final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	private BranchBankService branchBankService;

	/**
	 * 根据银行名进行模糊查询，且根据银行编码进行精确查询
	 */
	@Override
	public Response<ResultBranchBankDTO> findBranchListByBankName(BranchBankDTO branchBankDTO) {

		BranchBankPO branchBankPO = BeanConvertUtils.convert(branchBankDTO, BranchBankPO.class);
		try {
			log.info("\n*****************于组织中心接口branchBankService.findBranchListByBankName请求数据为:"
					+ JSONObject.toJSONString(branchBankPO));
			ResultBranchBankPO res = branchBankService.findBranchListByBankName(branchBankPO);
			log.info("\n*****************于组织中心接口branchBankService.findBranchListByBankName响应数据为:"
					+ JSON.toJSONString(res) + "\n*************花费时间为：");
			ResultBranchBankDTO resultBranchBankDTO = BeanConvertUtils.convert(res, ResultBranchBankDTO.class);
			return new Response<ResultBranchBankDTO>(true, resultBranchBankDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<ResultBranchBankDTO>(false, e.getMessage(), AdverEnum.ERROR_CODE.getCode());
		}
	}

	/**
	 * 根据银行编码进行精确查询
	 */
	@Override
	public Response<ResultBranchBankDTO> findBranchListByBankCode(BranchBankDTO branchBankDTO) {
		BranchBankPO branchBankPO = BeanConvertUtils.convert(branchBankDTO, BranchBankPO.class);
		try {
			log.info("\n*****************于组织中心接口branchBankService.findBranchListByBankName请求数据为:"
					+ JSONObject.toJSONString(branchBankPO));
			ResultBranchBankPO res = branchBankService.findBranchListByBankCode(branchBankPO);
			log.info("\n*****************于组织中心接口branchBankService.findBranchListByBankName响应数据为:"
					+ JSON.toJSONString(res) + "\n*************花费时间为：");
			ResultBranchBankDTO resultBranchBankDTO = BeanConvertUtils.convert(res, ResultBranchBankDTO.class);
			return new Response<ResultBranchBankDTO>(true, resultBranchBankDTO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			return new Response<ResultBranchBankDTO>(false, e.getMessage(), AdverEnum.ERROR_CODE.getCode());
		}
	}

}
