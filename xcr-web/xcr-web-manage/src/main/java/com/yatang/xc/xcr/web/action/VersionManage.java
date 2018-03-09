package com.yatang.xc.xcr.web.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.busi.common.utils.BeanConvertUtils;
import com.yatang.xc.xcr.biz.core.dto.UpdateStateDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionDTO;
import com.yatang.xc.xcr.biz.core.dto.VersionListQueryDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.VersionDubboService;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.vo.PageResultModel;
import com.yatang.xc.xcr.vo.UpdateStateVO;
import com.yatang.xc.xcr.vo.VersionListQueryVO;
import com.yatang.xc.xcr.vo.VersionVO;

/**
 * 版本管理
 *
 * @author: zhongrun
 * @version: 1.0, 2017年8月8日
 */
@RequestMapping("xcr")
@Controller
public class VersionManage {


    private static Logger log = LoggerFactory.getLogger(UserSignAction.class);

    @Value("${fileserver.url}")
    private String fileserverUrl;


    @Autowired
    private VersionDubboService versionDubboService;

    @RequestMapping("/versionManage")
    public String skip2VersionList() {

        return "screen/versionManage/versionList";
    }

    @RequestMapping("/versionAdd")
    public String skip2VersionAdd() {
        return "screen/versionManage/versionAdd";
    }

    /**
     * 版本列表
     *
     * @param versionListQueryVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/versionList")
    @ResponseBody
    public PageResultModel<VersionVO> getVersionList(@RequestBody VersionListQueryVO versionListQueryVO) throws Exception {
        VersionListQueryDTO versionListQueryDTO = versionListVO2DTO(versionListQueryVO);

        log.info("versionList request data is:" + JSONObject.toJSONString(versionListQueryVO));
        Response<List<VersionDTO>> dubboResult = versionDubboService.findVersionList(versionListQueryDTO);
        Response<Integer> dubbResponse = versionDubboService.findTotalByCondetion(versionListQueryDTO);
        log.info("versionList response data is:" + JSONObject.toJSONString(dubboResult) + "total is:" + JSONObject.toJSONString(dubbResponse));

        PageResultModel<VersionVO> resultModel = new PageResultModel<VersionVO>();
        if (dubboResult.isSuccess() == false || dubbResponse.isSuccess() == false) {
            return resultModel;
        }
        List<VersionDTO> versionDTOs = dubboResult.getResultObject();
        List<VersionVO> versionVOs = BeanConvertUtils.convertList(versionDTOs, VersionVO.class);

        resultModel.setData(versionVOs);
        resultModel.setRows(dubbResponse.getResultObject());
        resultModel.setTotal(dubbResponse.getResultObject());
        return resultModel;
    }

    private VersionListQueryDTO versionListVO2DTO(
            VersionListQueryVO versionListQueryVO) throws ParseException {
        VersionListQueryDTO versionListQueryDTO = BeanConvertUtils.convert(versionListQueryVO, VersionListQueryDTO.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (!versionListQueryDTO.getStartDay().isEmpty()) {
            versionListQueryDTO.setStartTime(format.parse(versionListQueryDTO.getStartDay()));
        }
        if (!versionListQueryDTO.getEndDay().isEmpty()) {
            versionListQueryDTO.setEndTime(DateUtils.getDateBefore(format.parse(versionListQueryDTO.getEndDay()), -1));
        }
        return versionListQueryDTO;
    }


    /**
     * 版本详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/versionDetail")
    @ResponseBody
    public VersionVO getVersionDetail(Integer id) {
        VersionListQueryDTO versionListQueryDTO = new VersionListQueryDTO();
        versionListQueryDTO.setId(id);
        log.info("versionDetail request data is:" + JSONObject.toJSONString(id));
        Response<List<VersionDTO>> dubboResult = versionDubboService.findVersionList(versionListQueryDTO);
        log.info("versionDetail response data is:" + JSONObject.toJSONString(dubboResult));
        if (dubboResult.isSuccess() == false) {
            return null;
        }
        List<VersionDTO> versionDTOs = dubboResult.getResultObject();
        List<VersionVO> versionVOs = BeanConvertUtils.convertList(versionDTOs, VersionVO.class);
        VersionVO versionVO = versionVOs.get(0);

        return versionVO;
    }

    /**
     * 更新状态
     *
     * @param updateStateVO
     * @return
     */
    @RequestMapping("/version/updateState")
    @ResponseBody
    public boolean updateState(UpdateStateVO updateStateVO) {
        UpdateStateDTO updateStateDTO = BeanConvertUtils.convert(updateStateVO, UpdateStateDTO.class);
        log.info("updateState request data is:" + JSONObject.toJSONString(updateStateDTO));
        Response<Boolean> dubboResponse = versionDubboService.updateState(updateStateDTO);
        log.info("updateState response data is:" + JSONObject.toJSONString(dubboResponse));
        if (dubboResponse.isSuccess() == false) {
            return false;
        }
        boolean flag = dubboResponse.getResultObject();
        return flag;
    }


    /**
     * 新增版本
     *
     * @param file
     * @param request
     * @param response
     * @param versionVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/insertVersion")
    @ResponseBody
    public boolean insertVersion(VersionVO versionVO) throws Exception {
        log.info("in insertVersion ...");
        VersionDTO versionDTO = BeanConvertUtils.convert(versionVO, VersionDTO.class);
        log.info("insertVersion request data is:" + JSONObject.toJSONString(versionDTO));
        Response<Boolean> dubboResponse = versionDubboService.insertVersion(versionDTO);
        log.info("insertVersion response data is:" + JSONObject.toJSONString(dubboResponse));
        if (dubboResponse.isSuccess() == false) {
            return false;
        }
        boolean flag = dubboResponse.getResultObject();
        return flag;
    }


    /**
     * 更新版本
     * <method description>
     *
     * @param file
     * @param request
     * @param response
     * @param versionVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateVersion")
    @ResponseBody
    public boolean updateVersion(VersionVO versionVO) throws Exception {
        VersionDTO versionDTO = BeanConvertUtils.convert(versionVO, VersionDTO.class);
        log.info("updateVersion request data is:" + JSONObject.toJSONString(versionDTO));
        Response<Boolean> dubboResponse = versionDubboService.updateVersion(versionDTO);
        log.info("updateVersion response data is:" + JSONObject.toJSONString(dubboResponse));
        if (dubboResponse.isSuccess() == false) {
            return false;
        }
        boolean flag = dubboResponse.getResultObject();
        return flag;
    }
    
    
    /**
     * 
    * <推荐的code值>
    *
    * @param request
    * @param response
    * @param versionVO
    * @return
    * @throws Exception
     */
    @RequestMapping("/RecommendMaxCode")
    @ResponseBody
    public Response<Integer> RecommendMaxCode(
                                 HttpServletRequest request, HttpServletResponse response, VersionVO versionVO) throws Exception {
        VersionDTO versionDTO = BeanConvertUtils.convert(versionVO, VersionDTO.class);
        log.info("findMaxVersion request data is:" + JSONObject.toJSONString(versionDTO));
        Response<Integer> dubboResponse = versionDubboService.findMaxVersion(versionDTO);
        log.info("findMaxVersion response data is:" + JSONObject.toJSONString(dubboResponse));
        if (!dubboResponse.isSuccess()) {
            return new Response<Integer>(false,"系统错误","500");
        }
        return dubboResponse;
    }
}


