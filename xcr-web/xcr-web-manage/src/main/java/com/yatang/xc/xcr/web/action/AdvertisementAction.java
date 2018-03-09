package com.yatang.xc.xcr.web.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busi.common.datatable.TableDataResult;
import com.busi.common.resp.Response;
import com.busi.common.utils.BeanConvertUtils;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementGroupQueryDTO;
import com.yatang.xc.xcr.biz.core.dto.AdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dto.StateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService;
import com.yatang.xc.xcr.enums.AdverEnum;
import com.yatang.xc.xcr.vo.AdvertisementGroupQueryVO;
import com.yatang.xc.xcr.vo.AdvertisementGroupVO;
import com.yatang.xc.xcr.vo.AdvertisementUpdateVO;
import com.yatang.xc.xcr.vo.AdvertisementVO;
import com.yatang.xc.xcr.vo.StateQueryVO;

/**
 * 
* 广告位
*		
* @author: zhongrun
* @version: 1.0, 2017年7月8日
 */
@Controller
@RequestMapping("xcr/advertisement")
public class AdvertisementAction {
	private static int MAXLENGTH=10000;
    private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	AdvertisementDubboService advertisementDubboService;
	
	/**
	* 运维查询所有广告信息
	*
	* @param state
	* @return
	 */
	@ResponseBody
	@RequestMapping("/findAdvertise")
	public Response<TableDataResult<AdvertisementVO>> findAllAdvertise( StateQueryVO state){	
		Response<TableDataResult<AdvertisementVO>> response = new Response<TableDataResult<AdvertisementVO>>();
		TableDataResult<AdvertisementVO> table = new TableDataResult<AdvertisementVO>();
		StateDTO sate = BeanConvertUtils.convert(state, StateDTO.class);
		sate.setLength(MAXLENGTH);
		Response<List<AdvertisementDTO>> advertisements = advertisementDubboService.findBystate(sate);
		Integer total = advertisementDubboService.findAdvertisementTotal().getResultObject();
		log.info("/findAdvertiseByState得到的信息是："+advertisements+"总数:"+total);
		
		List<AdvertisementDTO> advertisementDTOs = advertisements.getResultObject();
		List<AdvertisementVO> advertisementVOs = BeanConvertUtils.convertList(advertisementDTOs, AdvertisementVO.class);
		table.setData(advertisementVOs);
		table.setRecordsTotal(total);
		response.setResultObject(table);
		response.setSuccess(advertisements.isSuccess());
		if (!advertisements.isSuccess()) {
				log.error("/findAdvertise错误信息是："+response.getErrorMessage());
			response.setErrorMessage(advertisements.getErrorMessage());
		}
		return response;
	}
	
	
	/**
	 * 运维查询广告组列表
	*
	* @param state
	* @return
	 */
	@ResponseBody
	@RequestMapping("/findAdvertiseGroup")
	public Response<TableDataResult<AdvertisementGroupVO>> findAdvertisementGroup(AdvertisementGroupQueryVO advertisementGroupVO){
		Response<TableDataResult<AdvertisementGroupVO>> response = new Response<TableDataResult<AdvertisementGroupVO>>();
		TableDataResult<AdvertisementGroupVO> table = new TableDataResult<AdvertisementGroupVO>();
		
		log.info("/findAdvertiseGroup请求的信息是："+advertisementGroupVO);
		AdvertisementGroupQueryDTO advertisementGroupDTO = BeanConvertUtils.convert(advertisementGroupVO, AdvertisementGroupQueryDTO.class);
		advertisementGroupDTO.setLength(MAXLENGTH);
		Response<List<AdvertisementGroupDTO>> advertisements = advertisementDubboService.findAdvertisementGroup(advertisementGroupDTO);
		Integer total = advertisementDubboService.findAdvertisementGroupTotal().getResultObject();
		
		log.info("/findAdvertiseByState得到的信息是："+advertisements+"数量："+total);		
		List<AdvertisementGroupVO> advertisementGroupVOs = BeanConvertUtils.convertList(advertisements.getResultObject(), AdvertisementGroupVO.class);		
		table.setRecordsTotal(total);
		table.setData(advertisementGroupVOs);
		response.setResultObject(table);
		response.setSuccess(advertisements.isSuccess());
		if (!advertisements.isSuccess()) {
				log.error("/findAdvertiseGroup错误信息是："+response.getErrorMessage());
			response.setErrorMessage(advertisements.getErrorMessage());
		}
		return response;
		
	}
	
	
	/**
	 * 运维添加广告
	*
	* @param advertisementGroupVO
	* @return
	 */
	@ResponseBody
	@RequestMapping("/addAdvertisement")
	public Response<Boolean> insertAdvertisement(AdvertisementVO advertisementVO){		
		if(advertisementVO.getGroupId()==null||advertisementVO.getPicName()==null||advertisementVO.getPicUrl()==null){
			log.info("/addAdvertisement响应的信息是"+AdverEnum.ADVER_PARAM_ERROR.getMsg());
			return new Response<Boolean>(false,AdverEnum.ADVER_PARAM_ERROR.getMsg(),AdverEnum.ADVER_PARAM_ERROR.getCode());
		}
		
		Response<Boolean> response=null;
		log.info("/addAdvertisement请求的信息是："+advertisementVO);
		AdvertisementDTO advertisementDTO = BeanConvertUtils.convert(advertisementVO, AdvertisementDTO.class);	
		if(advertisementDTO.getId()==null){
			 response = advertisementDubboService.insertAdvertis(advertisementDTO);	
				log.info("/addAdvertisement响应的信息是："+response.getResultObject()+response.getCode());
		}else {
			response = advertisementDubboService.updateAdver(advertisementDTO);
			log.info("/addAdvertisement响应的信息是："+response.getResultObject()+response.getCode());

		}
		if (response.isSuccess()==false) {
			log.error("/addAdvertisement错误信息是："+response.getErrorMessage());
		}
		return response;
	}
	
	
	/**
	 * 运维添加广告组
	*
	* @param advertisementGroupVO
	* @return
	 */
	@ResponseBody
	@RequestMapping("/addAdvertisementGroup")
	public Response<Boolean> insertAdvertisementGroup( AdvertisementGroupVO advertisementGroupVO){
		if (advertisementGroupVO.getGroupName()==null||advertisementGroupVO.getGroupName()==""||
				advertisementGroupVO.getPositionCode()==null) {
			return new Response<Boolean>(false,AdverEnum.ADVER_PARAM_ERROR.getMsg(),AdverEnum.ADVER_PARAM_ERROR.getCode());
		}
		
		log.info("/addAdvertisementGroup请求的信息是："+advertisementGroupVO);
		AdvertisementGroupDTO advertisementGroupDTO = BeanConvertUtils.convert(advertisementGroupVO, AdvertisementGroupDTO.class);
		
		Response<Boolean> response = advertisementDubboService.insertAdvertisementGroup(advertisementGroupDTO);
		
		log.info("/addAdvertisementGroup响应的信息是："+response.getResultObject()+response.getCode());	
		if (response.isSuccess()==false) {
			log.error("/addAdvertisementGroup错误信息是："+response.getErrorMessage());
		}
		return response;
	}
	
	/**
	 * 运维修改广告状态
	*
	* @param advertisementUpdateVO
	* @return
	 */
	@ResponseBody
	@RequestMapping("/updateAdvertisement")
	public Response<Boolean> updateAdvertisement(AdvertisementUpdateVO advertisementUpdateVO){	
		log.info("/updateAdvertisement请求的信息是："+advertisementUpdateVO);
		Integer id = advertisementUpdateVO.getId();
		Integer state = advertisementUpdateVO.getState();
		
		Response<Boolean> response = advertisementDubboService.updateState(id, state);	
		
		log.info("/updateAdvertisement相应的信息是："+response.getResultObject()+response.getCode());	
		if (response.isSuccess()==false) {
			log.error("/updateAdvertisement错误信息是："+response.getErrorMessage());
		}
		return response;
	}
	
	
	/**
	 * 运维修改广告组状态
	*
	* @param advertisementUpdateVO
	* @return
	 */
	@ResponseBody
	@RequestMapping("/updateAdvertisementGroup")
	public Response<Boolean> updateAdvertisementGroup( AdvertisementUpdateVO advertisementUpdateVO){	
		log.info("/updateAdvertisementGroup请求的信息是："+advertisementUpdateVO);
		AdvertisementUpdateDTO updateDTO = BeanConvertUtils.convert(advertisementUpdateVO, AdvertisementUpdateDTO.class);
		
		Response<Boolean> response = advertisementDubboService.updateAdvertisementGroup(updateDTO);	
		
		log.info("/updateAdvertisementGroup相应的信息是："+response.getResultObject()+response.getCode());
		if (response.isSuccess()==false) {
			log.error("/updateAdvertisementGroup错误信息是："+response.getErrorMessage());
		}
		return response;
	}
	
	
	/**
	 * 通过id查询广告
	* <method description>
	*
	* @param id
	* @return
	 */
	@ResponseBody
	@RequestMapping("/findAdvertisementById")
	public Response<AdvertisementVO> findAdvertisementById(Integer id){	
		log.info("/findAdvertisementById请求的信息是："+id);		
		
		Response<AdvertisementDTO> dubboresult = advertisementDubboService.findAdvertisementByid(id);	
		log.info("/findAdvertisementById请求的信息是："+dubboresult.getResultObject()+dubboresult.getCode());	
		AdvertisementDTO advertisementDTO = dubboresult.getResultObject();
		AdvertisementVO advertisementVO = BeanConvertUtils.convert(advertisementDTO, AdvertisementVO.class);
		Response<AdvertisementVO> response = new Response<AdvertisementVO>();		
		response.setResultObject(advertisementVO);
		response.setSuccess(dubboresult.isSuccess());
		if (!dubboresult.isSuccess()) {
			log.error("/findAdvertisementById错误信息是："+response.getErrorMessage());
			response.setErrorMessage(dubboresult.getErrorMessage());
		}
		return response;
	}
	
	/**
	 * 获取广告组下广告数量
	* <method description>
	*
	* @param id
	* @return
	 */
	@ResponseBody
	@RequestMapping("/validateTotalAdver")
	public Response<Integer> validateTotalAdver(Integer id){	
		if(id==null){
			log.error("/validateTotalAdver输入为空");
			return new Response<Integer>(false,AdverEnum.ADVER_PARAM_ERROR.getMsg(),AdverEnum.ADVER_PARAM_ERROR.getCode());
		}
		log.info("/validateTotalAdver请求的信息是："+id);		
		Response<Integer>  response = advertisementDubboService.validateTotalAdver(id);
		log.info("/validateTotalAdver响应的的信息是："+response.getResultObject()+response.getCode());		
		if (response.isSuccess()==false) {
			log.error("/validateTotalAdver错误信息是："+response.getErrorMessage());
		}
		return response;
		
	}
	
	
	/**
	 * 通过id查询广告组
	* <method description>
	*
	* @param id
	* @return
	 */
	@ResponseBody
	@RequestMapping("/findAdverGroupById")
	public Response<AdvertisementGroupDTO> findAdverGroupById(Integer id){	
		if(id==null){
			log.error("/findAdverGroupById输入为空");
			return new Response<AdvertisementGroupDTO>(false,AdverEnum.ADVER_PARAM_ERROR.getMsg(),AdverEnum.ADVER_PARAM_ERROR.getCode());
		}
		log.info("/findAdverGroupById请求的信息是："+id);		
		
		Response<AdvertisementGroupDTO>  response = advertisementDubboService.findAdverGroupById(id);
		
		log.info("/findAdverGroupById相应的信息是："+response.getResultObject()+response.getCode());		
		if (response.isSuccess()==false) {
			log.error("/findAdverGroupById错误信息是："+response.getErrorMessage());
		}
		return response;
		
	}
	
	/**
	 * 通过名字查询广告
	* <method description>
	*
	* @param name
	* @return
	 */
	@ResponseBody
	@RequestMapping("/findAdvertisementByName")
	public Response<AdvertisementVO> findAdvertisementByName(String name){	
		log.info("/findAdvertisementByName请求的信息是："+name);	

		Response<AdvertisementDTO> res = advertisementDubboService.findAdverByName(name);
		
		log.info("/findAdvertisementByName响应的信息是："+res.getResultObject()+res.getCode());	
		AdvertisementDTO advertisementDTO = res.getResultObject();
		AdvertisementVO advertisementVO = BeanConvertUtils.convert(advertisementDTO, AdvertisementVO.class);
		Response<AdvertisementVO> response = new Response<AdvertisementVO>();
		response.setResultObject(advertisementVO);
		response.setSuccess(res.isSuccess());
		if (!res.isSuccess()) {
			log.error("/findAdvertisementByName错误信息是："+res.getErrorMessage());
			response.setErrorMessage(res.getErrorMessage());
		}
		return response;
		
	}
	
	/**
	 * 通过名字查询广告组
	* <method description>
	*
	* @param name
	* @return
	 */
	@ResponseBody
	@RequestMapping("/findAdvertisementGroupByName")
	public Response<AdvertisementGroupVO> findAdvertisementGroupByName(String name){			
		log.info("/findAdvertisementGroupByName请求的信息是："+name);	
		Response<AdvertisementGroupDTO> res = advertisementDubboService.findAdverGroupByName(name);
		log.info("/findAdvertisementGroupByName相应的信息是："+res.getResultObject()+res.getCode());	
		AdvertisementGroupDTO advertisementGroupDTO = res.getResultObject();
		AdvertisementGroupVO advertisementGroupVO = BeanConvertUtils.convert(advertisementGroupDTO, AdvertisementGroupVO.class);
		Response<AdvertisementGroupVO> response = new Response<AdvertisementGroupVO>();
		response.setResultObject(advertisementGroupVO);
		response.setSuccess(res.isSuccess());
		if (!response.isSuccess()) {
			log.error("/findAdvertisementGroupByName错误信息是："+res.getErrorMessage());	
			response.setErrorMessage(res.getErrorMessage());
		}
		return response;
		
	}
	
	
	
	
	
}
