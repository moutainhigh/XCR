package com.yatang.xc.xcr.web.action;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.image.dubboservice.ImageDubboService;
import com.yatang.xc.xcr.biz.mission.dto.center.ViewMissionExecuteDto;
import com.yatang.xc.xcr.biz.mission.dubboservice.MissionClassroomDubboService;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainDubboService;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.util.DateUtils;
import com.yatang.xc.xcr.util.ResponseDataCheckUtil;
import com.yatang.xc.xcr.vo.ClassListVO;
import com.yatang.xc.xcr.vo.PageResultModel;
import com.yatang.xc.xcr.vo.ReturnClass;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程管理相关处理
 *
 * @author dongshengde
 */
@Controller
@RequestMapping(value = "xcr/train")
public class TrainManageAction {
    private static Logger log = LoggerFactory.getLogger(TrainManageAction.class);

    @Value("${fileserver.url}")
    private String fileserverUrl;

    @Autowired
    private MissionClassroomDubboService missionClassroomDubboService;
    @Autowired
    private TrainDubboService trainDubboService;
    @Autowired
    private TrainQueryDubboService trainQueryDubboService;
    @Autowired
    private ImageDubboService imageDubboService;

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "trainAdd.htm", method = {RequestMethod.POST})
    public ReturnClass addCourseInfo(@RequestBody TrainInfoDTO dto, HttpServletResponse response) throws IOException {
        long methodStart = System.currentTimeMillis();
        ReturnClass returnClass = new ReturnClass();
        log.info("trainAdd -> 查看获取到的参数：" + JSONObject.toJSONString(dto));
        dto.setContent(dto.getContent().replace('\"', '\''));
        if (dto.getId() == null) {
            Response<Boolean> res = trainQueryDubboService.checkNameExist(dto.getName());
            if (res.getResultObject() == true) {
                returnClass.setErrorMessage("课堂名已存在");
                return returnClass;
            }
        }
        if (dto.getStatus() == 0) {// 未发布
            if (dto.getId() == null) {// id为空，，也就是创建课堂
                dto.setCreateTime(new Date(System.currentTimeMillis()));
                dto.setModifyTime(new Date(System.currentTimeMillis()));
            } else// id不为空，也就是更改课堂
                dto.setModifyTime(new Date(System.currentTimeMillis()));
        } else if (dto.getStatus() == 1) {// 点击确认发布会直接跳入这行代码
            dto.setReleasesTime(new Date(System.currentTimeMillis()));
            dto.setModifyTime(new Date(System.currentTimeMillis()));
        } else {// 下架时间
            dto.setModifyTime(new Date(System.currentTimeMillis()));
        }

        String contentHtml = dto.getContent();
        log.info("replaceEmbedMethod result contentHtml:" + contentHtml);
        String fileUrl = getFileserverUrl(contentHtml);
        dto.setFileUrl(fileUrl);
        Long getSignArrayStartTime = System.currentTimeMillis();
        log.info("\n*****************调用trainDubboService.editTrain接口的开始时间："
                + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
                + JSONObject.toJSONString(JSONObject.toJSONString(dto)));
        Response<TrainInfoDTO> result = trainDubboService.editTrain(dto);

        log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
                + "调用trainDubboService.editTrain接口   调用结束" + "\n*****************响应数据是："
                + JSONObject.toJSONString(result.getResultObject()) + "\n***************所花费时间为："
                + CommonUtil.costTime(getSignArrayStartTime));
        log.info("#######################enterGenerate HTML File");

        ResponseDataCheckUtil.showResponseData(result);
        returnClass.setFlag(result.isSuccess());
        long methodEnd = System.currentTimeMillis();
        System.err.println("方法总执行时间:" + (methodEnd - methodStart));
        return returnClass;
    }

    /**
     * 获取文件上传返回地址
     *
     * @param contentHtml
     * @return
     */
    private String getFileserverUrl(String contentHtml) {
        String fileUrl = "";
        contentHtml = CommonUtil.replaceIframe(contentHtml);
        if (StringUtils.isNotEmpty(contentHtml)) {
            contentHtml.replace('\"', '\'');
            StringBuilder sb = new StringBuilder();
            contentHtml = CommonUtil.generateHTMLString(sb, contentHtml);
            Response<String> stringResponse = imageDubboService.uploadFile(contentHtml.getBytes(), "html");
            if (stringResponse != null && stringResponse.isSuccess()) {
                fileUrl = stringResponse.getResultObject();
            }
            log.info("保存消息 -> fileUrl:" + fileUrl);
            return fileserverUrl + "/" + fileUrl;
        }
        return "";
    }


    /**
     * 根据id查找一条记录
     *
     * @param id
     * @param model
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "findOneTrain.htm", method = {RequestMethod.POST})
    public String findOneTrain(Long id, Model model) throws IOException {
        Response<TrainInfoDTO> response = trainQueryDubboService.findOneTrain(id);
        model.addAttribute("trainDTO", response.getResultObject());
        return "views/screen/train/train";
    }

    /**
     * 删除课堂列表
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "deleteTrain.htm", method = {RequestMethod.POST})
    public Boolean delCourse(Long id, HttpServletResponse response) throws IOException {
        System.err.println("查看获取到的参数：" + id);

        Response<TrainInfoDTO> result = trainDubboService.deleteMsg(id);

        // checkdata,this will be removed after test
        ResponseDataCheckUtil.showResponseData(result);
        return result.isSuccess();
    }

    /**
     * 获取课堂列表
     *
     * @param map
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "getTrainList.htm", method = {RequestMethod.POST})
    public PageResultModel<ClassListVO> getTrainList(@RequestBody Map map) throws IOException {
        log.info("in getTrainList ...");
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        pageIndex = pageIndex == null ? 0 : pageIndex;
        pageSize = pageSize == null ? 10 : pageSize;
        log.info("getMessageList-> pageIndex:" + pageIndex + "   pageSize:" + pageSize);

        Integer pageNum = CommonUtil.getPageNum(pageIndex, pageSize);
        Response<Map<String, Object>> res = trainQueryDubboService.getTrainList(null, pageNum, pageSize);
        if (res == null) {
            log.error("trainQueryDubboService.getTrainList(null, pageNum, pageSize) -> res == null");
            return new PageResultModel<>();
        }
        PageResultModel<ClassListVO> pageResultModel = new PageResultModel<>();
        Map<String, Object> resultMap = res.getResultObject();
        if (CollectionUtils.isEmpty(resultMap)) {
            log.error("res.getResultObject(); -> resultMap == null or size == 0");
            return pageResultModel;
        }
        Object totalObj = resultMap.get("total");
        log.info("Object totalObj = " + totalObj);
        totalObj = totalObj == null ? "0" : totalObj;
        Integer total = Integer.valueOf(String.valueOf(totalObj));
        log.info("Integer total = " + total);
        List<TrainInfoDTO> trainInfoDTOs = (List<TrainInfoDTO>) resultMap.get("data");

        List<ClassListVO> classVOList = InitClassVoList(trainInfoDTOs);
        pageResultModel.setRows(total);
        pageResultModel.setTotal(total);
        pageResultModel.setData(classVOList);

        return pageResultModel;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "releaseTrain.htm", method = {RequestMethod.POST})
    public ReturnClass releaseTrain(@RequestBody TrainInfoDTO dto, HttpServletResponse response) throws IOException {
        ReturnClass returnClass = new ReturnClass();
        if (dto.getId() == null) {
            Response<Boolean> res = trainQueryDubboService.checkNameExist(dto.getName());
            if (res.getResultObject() == true) {
                returnClass.setErrorMessage("课堂名已存在");
                return returnClass;
            }
        }
        dto.setStatus(1);
        dto.setReleasesTime(new Date(System.currentTimeMillis()));
        Long getSignArrayStartTime = System.currentTimeMillis();
        log.info("\n*****************调用branchBankListDubboService.findBranchListByBankCode接口的开始时间："
                + DateUtils.getLogDataTime(getSignArrayStartTime, null) + "\n*****************请求数据是："
                + JSONObject.toJSONString(JSONObject.toJSONString(dto)));
        Response<TrainInfoDTO> result = trainDubboService.downShelfOrReleases(dto.getId(), dto.getStatus().toString());
        log.info("\n*****************于时间:" + DateUtils.getLogDataTime(getSignArrayStartTime, null)
                + "调用HttpsClientUtil.sendHttpsPost接口   调用结束" + "\n*****************响应数据是："
                + JSONObject.toJSONString(result.getResultObject()) + "\n***************所花费时间为："
                + CommonUtil.costTime(getSignArrayStartTime));
        // checkdata,this will be removed after test
        ResponseDataCheckUtil.showResponseData(result);
        returnClass.setFlag(result.isSuccess());
        return returnClass;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "underTrain.htm", method = {RequestMethod.POST})
    public Boolean underTrain(@RequestBody TrainInfoDTO dto, HttpServletResponse response) throws IOException {

        dto.setStatus(2);
        // 设置下架时间

        Response<TrainInfoDTO> result = trainDubboService.downShelfOrReleases(dto.getId(), dto.getStatus().toString());
        // checkdata,this will be removed after test
        ResponseDataCheckUtil.showResponseData(result);

        return result.isSuccess();
    }

    /**
     * 根据ID获取课堂详情
     *
     * @param id
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "getTrainById.htm", method = {RequestMethod.POST})
    public TrainInfoDTO getTrainById(Long id) throws IOException {

        log.info("in getTrainById ... ");
        log.info("id:" + id);
        Response<TrainInfoDTO> response = trainQueryDubboService.findOneTrain(id);
        if (response == null) {
            log.error("trainQueryDubboService.findOneTrain(id); -> response == null");
            return new TrainInfoDTO();
        }
        return response.getResultObject();
    }

    /**
     * 组装学习列表页数据
     *
     * @param trainInfoDTOList
     * @return
     */
    private List<ClassListVO> InitClassVoList(List<TrainInfoDTO> trainInfoDTOList) {
        if (CollectionUtils.isEmpty(trainInfoDTOList)) {
            log.error("InitClassVoList -> trainInfoDTOList == null or size == 0");
            return new ArrayList<>();
        }
        List<ClassListVO> classListVOList = new ArrayList<>();
        for (TrainInfoDTO dto : trainInfoDTOList) {
            ClassListVO vo = new ClassListVO();

            Long id = dto.getId();
            Integer status = dto.getStatus(); //状态：0未发布,1已发布
            status = (status == null ? 0 : status);
            String isMission = "0";
            if (status == 1) {
                Response<ViewMissionExecuteDto> response2 = missionClassroomDubboService
                        .queryMissionExecuteByMerchantIdAndCourseId(null, id.toString());
                log.info("missionClassroomDubboService\n"
                        + "                        .queryMissionExecuteByMerchantIdAndCourseId(null, id.toString()) -> response2 = "
                        + response2);
                if (response2.getResultObject() != null) {
                    isMission = "1";
                }
            }
            Date modifyTime = dto.getModifyTime(); //最后修改时间

            vo.setId(dto.getId());
            vo.setName(dto.getName());
            vo.setTrainLength(dto.getTrainLength());
            vo.setIsMission(isMission.equals("1") ? "是" : "否");
            vo.setStatus(status == 1 ? "已发布" : "未发布");
            vo.setModifyTime(CommonUtil.getDateString(modifyTime));
            vo.setContent(dto.getContent());
            vo.setIcon(dto.getIcon());
            vo.setViedioUrl(dto.getViedioUrl());
            vo.setImagesUrl(dto.getImagesUrl()); //图片url(多图以,拼接)
            vo.setFileUrl(dto.getFileUrl());
            vo.setRemark(dto.getRemark());
            vo.setCreateUid(dto.getCreateUid());
            vo.setCreateTime(dto.getCreateTime());
            vo.setModifyUid(dto.getModifyUid());
            vo.setReleasesTime(dto.getReleasesTime());
            classListVOList.add(vo);
        }
        return classListVOList;
    }

}
