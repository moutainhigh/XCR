package com.yatang.xc.xcr.web.action;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.image.dubboservice.ImageDubboService;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganzitionXCRBusinessDubboService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.RegistrationParameterDto;
import com.yatang.xc.mbd.biz.region.dto.RegionDTO;
import com.yatang.xc.mbd.biz.region.dubboservice.RegionDubboService;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgQueryDubboService;
import com.yatang.xc.xcr.biz.message.dubboservice.JpushMsgDubboService;
import com.yatang.xc.xcr.util.CommonUtil;
import com.yatang.xc.xcr.vo.MsgPushVO;
import com.yatang.xc.xcr.vo.PageResultModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "xcr/message")
public class MessageManageAction {
    private static Logger log = LoggerFactory.getLogger(MessageManageAction.class);

    @Value("${fileserver.url}")
    private String fileserverUrl;

    @Autowired
    private ImageDubboService imageDubboService;

    @Autowired
    private MsgDubboService msgDubboService;
    @Autowired
    private MsgQueryDubboService msgQueryDubboService;
    @Autowired
    private JpushMsgDubboService JpushMsgDubboService;
    @Autowired
    private OrgnazitionO2ODubboService orgnazitionO2ODubboService;
    @Autowired
    private OrganizationService organizationDubboService;
    @Autowired
    private OrganzitionXCRBusinessDubboService organzitionXCRBusinessDubboService;
    @Autowired
    private RegionDubboService regionDubboService;


    /**
     * 根据Id获取消息详情
     *
     * @param id
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "getMessageById.htm", method = {RequestMethod.POST})
    public MsgPushDTO getMessageById(@RequestParam Long id) throws IOException {
        log.info("传入参数 -> id:" + id);
        Response<MsgPushDTO> msgObj = msgQueryDubboService.findOneMsg(id);
        if (msgObj != null) {
            MsgPushDTO msgPushDTO = msgObj.getResultObject();
            log.info("返回数据 -> msgObj:" + JSONObject.toJSONString(msgPushDTO));
            return msgPushDTO;
        }
        return null;
    }

    /**
     * 发布消息，与保存消息的区别就是属性status的值为released
     *
     * @param msgPushDTO
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "releaseMessage.htm", method = {RequestMethod.POST})
    public void messageRelease(@RequestBody MsgPushDTO msgPushDTO, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();

        CommonUtil.LogRecond("releaseMessage.htm", msgPushDTO.toString(), 0, startTime);
        Response<MsgPushDTO> result = null;

        log.info("releaseMessage.htm -> 传入参数为 msgPushDTO:" + JSONObject.toJSONString(msgPushDTO));
        String contentFrom = msgPushDTO.getContentFrom();
        if (StringUtils.isNotEmpty(contentFrom)) {
            if (contentFrom.equals("1")) {
                String contentHtml = msgPushDTO.getContentHtml();
                String fileUrl = getFileserverUrl(contentHtml);
                msgPushDTO.setMsgUrl(fileUrl);
            }
        }

        String pushType = msgPushDTO.getPushType();
        msgPushDTO.setPushTo(msgPushDTO.getShopNo());
        String[] areaArrOrd = msgPushDTO.getAreaArr();
        if (areaArrOrd != null && areaArrOrd.length > 1) {
            if (areaArrOrd.length > 1) {
                List<String> areaArrList = new ArrayList<>(Arrays.asList(areaArrOrd));
                if (areaArrList.contains("on")) {
                    areaArrList.remove("on");
                }
                String[] areaArr = areaArrList.toArray(new String[areaArrList.size()]);
                msgPushDTO.setAreaArr(areaArr);
                log.info("areaArr:" + areaArr);
            }
        }
        if (msgPushDTO.getId() != null) {
            long msgFStartTime = System.currentTimeMillis();
            CommonUtil.LogRecond("findOneMsg", msgPushDTO.getId().toString(), 0, msgFStartTime);
            Response<MsgPushDTO> msgObj = msgQueryDubboService.findOneMsg(msgPushDTO.getId());
            CommonUtil.LogRecond("findOneMsg", JSONObject.toJSONString(msgObj), 1, msgFStartTime);
            msgPushDTO.setCreateTime(msgObj.getResultObject().getCreateTime());
            String pushTo = msgPushDTO.getPushTo();
            long msgPStartTime = System.currentTimeMillis();
            CommonUtil.LogRecond("editMsg", msgPushDTO.toString(), 0, msgPStartTime);
            result = msgDubboService.editMsg(msgPushDTO);
            CommonUtil.LogRecond("editMsg", JSONObject.toJSONString(result), 1, msgPStartTime);
        } else {
            long msgEStartTime = System.currentTimeMillis();
            CommonUtil.LogRecond("editMsg", msgPushDTO.toString(), 0, msgEStartTime);
            result = msgDubboService.editMsg(msgPushDTO);
            CommonUtil.LogRecond("editMsg", JSONObject.toJSONString(result), 1, msgEStartTime);
        }
        if (result.isSuccess()) {
            log.info("判断是否是定向推送 pushType:" + pushType);
            Map<String, String> extras = new HashMap<>();
            //如果发送类型为0 ：发送所有
            if (pushType.equals("0")) {
                if (pushAll(msgPushDTO, extras)) {
                    log.info("发送所有 -> 推送成功 !!! ");
                    response.getWriter().print(0);
                } else {
                    response.getWriter().print(1);
                }
            }
            //如果发送类型为1 ： 定向推送
            if (pushType.equals("1")) {
                if (pushByShopNo(msgPushDTO, extras)) {
                    log.info("定向推送 -> 推送成功 !!! ");
                    response.getWriter().print(0);
                } else {
                    response.getWriter().print(1);
                }
            }
            //如果发送类型为2 ： 区域推送
            if (pushType.equals("2")) {
                if (pushByArea(msgPushDTO, extras)) {
                    log.info("区域推送 -> 推送成功 !!! ");
                    response.getWriter().print(0);
                } else {
                    response.getWriter().print(1);
                }
            }
            response.getWriter().print(1);
        } else {
            response.getWriter().print(0);
        }
    }

    /**
     * 推送所有
     *
     * @param msgPushDTO
     * @param extras
     * @return
     */
    private boolean pushAll(MsgPushDTO msgPushDTO, Map<String, String> extras) {
        try {
            extras.put("Type", "0");
            extras.put("OrderNo", "");
            JpushMsgDubboService.sendPushAll(null, msgPushDTO.getTitle(), extras);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 定向推送
     *
     * @param msgPushDTO
     * @param extras
     */
    private boolean pushByShopNo(MsgPushDTO msgPushDTO, Map<String, String> extras) {
        try {
            extras.put("Type", "0");
            extras.put("OrderNo", "");
            String shopNo = msgPushDTO.getShopNo();
            if (StringUtils.isEmpty(shopNo)) {
                log.error("msgPushDTO.getShopNo(); -> shopNo 为空 ！！！");
                return false;
            }
            List<String> regesterIdList = getRegistIdListByShopCode(shopNo);
            JpushMsgDubboService.sendPushByRegesterId(regesterIdList, null, msgPushDTO.getTitle(), extras);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 区域推送
     *
     * @param msgPushDTO
     * @param extras
     * @return
     */
    private boolean pushByArea(MsgPushDTO msgPushDTO, Map<String, String> extras) {
        try {
            extras.put("Type", "0"); //type必须为0才能识别出小红点
            extras.put("OrderNo", "");
            //1:获取区域编号
            log.info("msgPushDTO:" + msgPushDTO);
            String[] areaArr = msgPushDTO.getAreaArr(); //获取区域编号
            if (areaArr == null || areaArr.length == 0) {
                return false;
            }
            log.info("areaArr.length:" + areaArr.length);
            List<String> registrationIdsAll = getRegistrationIdsByAreaArr(areaArr);
            List<String> regesterIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(registrationIdsAll)) {
                int size = registrationIdsAll.size();
                regesterIds = removeRepeatAndNullForList(registrationIdsAll);
                log.info("regesterIds.size:" + size);
            }
            log.debug("推送结果" + JSONObject.toJSONString(regesterIds));
            //3:推送消息
            JpushMsgDubboService.sendPushByRegesterId(regesterIds, null, msgPushDTO.getTitle(), extras);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据区域编号数组获取推送标识
     *
     * @param areaArr
     * @return
     */
    List<String> getRegistrationIdsByAreaArr(String[] areaArr) {
        List<String> registrationIdsAll = new ArrayList<>();
        for (String anAreaArr : areaArr) {
            Response<List<String>> listResponse = organzitionXCRBusinessDubboService.queryRegistrationIdByRegion(anAreaArr, "", "");
            if (listResponse != null && listResponse.isSuccess()) {
                List<String> regesterIds = listResponse.getResultObject();
                registrationIdsAll.addAll(regesterIds);
            }
        }
        return registrationIdsAll;
    }

    /**
     * 去重去空
     *
     * @param regesterIds
     * @return
     */
    public List<String> removeRepeatAndNullForList(List<String> regesterIds) {
        Set<String> regesterIdSet = new HashSet<>(regesterIds);
        regesterIds.clear();
        regesterIds.addAll(regesterIdSet);
        List<String> nullArr = new ArrayList<>();
        nullArr.add(null);
        regesterIds.removeAll(nullArr);
        List<String> Arr = new ArrayList<>();
        Arr.add("");
        regesterIds.removeAll(Arr);
        return regesterIds;
    }

    /**
     * 保存消息
     *
     * @param msgPushDTO
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "saveMessage.htm", method = {RequestMethod.POST})
    public void saveRelease(@RequestBody MsgPushDTO msgPushDTO, HttpServletResponse response) throws Exception {
        log.info("传入参数 -> msgPushDTO:" + JSONObject.toJSONString(msgPushDTO));

        String contentFrom = msgPushDTO.getContentFrom();
        if (StringUtils.isNotEmpty(contentFrom)) {
            if (contentFrom.equals("1")) {
                String contentHtml = msgPushDTO.getContentHtml();
                String fileUrl = getFileserverUrl(contentHtml);
                msgPushDTO.setMsgUrl(fileUrl);
            }
        }

        String[] areaArrOrd = msgPushDTO.getAreaArr();
        if (areaArrOrd.length > 1) {
            String[] areaArr = new String[areaArrOrd.length - 1];
            System.arraycopy(areaArrOrd, 1, areaArr, 0, areaArr.length);
            log.info("areaArr:" + Arrays.toString(areaArr));
            msgPushDTO.setAreaArr(areaArr);
        }

        long startTime = System.currentTimeMillis();
        CommonUtil.LogRecond("saveMessage.htm", msgPushDTO.toString(), 0, startTime);
        if (msgPushDTO.getCreateTime() != null) {
            long msgLStartTime = System.currentTimeMillis();
            CommonUtil.LogRecond("editMsg", msgPushDTO.toString(), 0, msgLStartTime);
            msgPushDTO.setPushTo(msgPushDTO.getShopNo());

            log.info("保存消息 -> msgPushDTO:" + JSONObject.toJSONString(msgPushDTO));
            Response<MsgPushDTO> result = msgDubboService.editMsg(msgPushDTO);
            CommonUtil.LogRecond("editMsg", JSONObject.toJSONString(result), 1, msgLStartTime);
            if (result.isSuccess()) {
                response.getWriter().print(1);
            } else {
                response.getWriter().print(0);
            }
        } else {
            response.getWriter().print(0);
        }
        CommonUtil.LogRecond("saveMessage.htm", msgPushDTO.toString(), 1, startTime);
    }

    /**
     * 得到全部消息列表
     *
     * @param msgPushDTO
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "getMessageList.htm", method = {RequestMethod.POST})
    public PageResultModel<MsgPushVO> getMessageList(@RequestBody Map map) {

        log.info("in getMessageList ... ");
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        pageIndex = pageIndex == null ? 0 : pageIndex;
        pageSize = pageSize == null ? 10 : pageSize;
        log.info("getMessageList-> pageIndex:" + pageIndex + "   pageSize:" + pageSize);

        // 根据返回条数获取当前页
        Integer pageNum = CommonUtil.getPageNum(pageIndex, pageSize);
        Response<Map<String, Object>> res = msgQueryDubboService.getMsgListBack(null, pageNum, pageSize);
        if (res == null) {
            log.error("msgQueryDubboService.getMsgList(null, pageNum, pageSize)-> res == null");
            return new PageResultModel<>();
        }
        PageResultModel<MsgPushVO> pageResultModel = new PageResultModel<>();
        Map<String, Object> resultMap = res.getResultObject();
        if (CollectionUtils.isEmpty(resultMap)) {
            log.error("res.getResultObject(); -> resultMap == null or size == 0");
            return new PageResultModel<>();
        }
        Object totalObj = resultMap.get("total");
        Integer total = Integer.valueOf(totalObj == null ? "0" : totalObj.toString());
        List<MsgPushDTO> msgPushDTOs = (List<MsgPushDTO>) resultMap.get("data");
        if (CollectionUtils.isEmpty(msgPushDTOs)) {
            return pageResultModel;
        }
        List<MsgPushVO> msgPushVOList = InitMsgPushVoList(msgPushDTOs);
        if (CollectionUtils.isEmpty(msgPushVOList)) {
            return pageResultModel;
        }
        pageResultModel.setRows(total);
        pageResultModel.setTotal(total);
        pageResultModel.setData(msgPushVOList);
        return pageResultModel;
    }

    /**
     * 删除消息 <method description>
     *
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "deleteMessage.htm", method = {RequestMethod.POST})
    public Boolean deleteMessage(@RequestParam Long id) {
        Response response = msgDubboService.deleteMsg(id);
        Boolean flag = response.isSuccess();
        return flag;
    }

    /**
     * 根据门店编号获取推送唯一码
     *
     * @param shopCode
     * @return
     */
    private List<String> getRegistIdListByShopCode(String shopCode) {

        // 门店编号获取加盟商ID
        log.info("organizationDubboService.getConvenientStoreById(merchantId) 开始调用 ....");
        long start = System.currentTimeMillis();
        Response<StoreDto> convenientStoreDTOResult = organizationDubboService
                .queryStoreById(shopCode);
        long end = System.currentTimeMillis();
        log.info("organizationDubboService.getConvenientStoreById(merchantId) 结束调用 ....花费时间为："
                + (end - start));
        if (!convenientStoreDTOResult.isSuccess()) {
            return null;
        }
        StoreDto convenientStoreDTO = convenientStoreDTOResult.getResultObject();
        String allianceBusinessId = convenientStoreDTO.getFranchiseeId();

        log.info("获取的加盟商编号为：allianceBusinessId=" + allianceBusinessId);

        // 根据门店ID获取regesterId
        log.info("  shopCode:" + shopCode);
        if (StringUtils.isEmpty(shopCode)) {
            log.info("没有获取到门店编号 !!!");
            return null;
        }
        log.info("shopCode is not empty ... ");
        List<String> shopCodeList = new ArrayList<String>();
        shopCodeList.add(allianceBusinessId);
        log.info("orgnazitionO2ODubboService start ...");
        Response<List<RegistrationParameterDto>> res = orgnazitionO2ODubboService
                .queryRegistrationId(shopCodeList);
        log.info("orgnazitionO2ODubboService.queryRegistrationId(shopCodeList) result = "
                + res.isSuccess());
        if (!res.isSuccess()) {
            log.info("没有获取到regesterId !!! shopCode:" + shopCode);
            return null;
        }
        List<RegistrationParameterDto> RegistrationParameterDtoList = res.getResultObject();
        if (CollectionUtils.isEmpty(RegistrationParameterDtoList)) {
            log.error("res.getResultObject() == null or size == 0 !!! shopCode:" + shopCode);
            return null;
        }

        List<String> regesterIdList = new ArrayList<String>();
        for (RegistrationParameterDto dto : RegistrationParameterDtoList) {
            String regesterId = dto.getRegistrationId();
            regesterIdList.add(regesterId);
            log.info("推送地址 regesterId:" + regesterId);
        }
        return regesterIdList;
    }

    /**
     * 组装页面VO数据(消息列表)
     *
     * @param msgPushDTOs
     * @return
     */
    private List<MsgPushVO> InitMsgPushVoList(List<MsgPushDTO> msgPushDTOs) {
        if (CollectionUtils.isEmpty(msgPushDTOs)) {
            return new ArrayList<>();
        }
        List<MsgPushVO> voList = new ArrayList<>();
        for (MsgPushDTO dto : msgPushDTOs) {
            MsgPushVO vo = new MsgPushVO();
            String status = dto.getStatus();
            status = status == null ? "" : status;
            String pushType = dto.getPushType();
            String pushTo = dto.getPushTo();
            String[] codeArr = dto.getAreaArr();

            vo.setId(dto.getId());
            vo.setTitle(dto.getTitle());
            vo.setImageUrl(dto.getImageUrl());
            vo.setMsgUrl(dto.getMsgUrl());
            vo.setStatus(status.equals("0") ? "未发布" : "已发布");
            vo.setType(dto.getType());
            vo.setCreateUid(dto.getCreateUid());
            vo.setCreateTime(dto.getCreateTime());
            vo.setModifyUid(dto.getModifyUid());
            vo.setModifyTime(CommonUtil.getDateString(dto.getModifyTime()));
            vo.setReleasesTime(dto.getReleasesTime());
            vo.setPushType(pushType);
            vo.setPushTo(pushType.equals("0") ? "全部用户" : pushType.equals("2") ? getRegionNameByCode(codeArr) : pushTo);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 根据区域编号获取区域名称
     *
     * @param codes
     * @return
     */
    public String getRegionNameByCode(String[] codes) {
        if (codes == null || codes.length <= 0) {
            return "-";
        }
        Response<List<RegionDTO>> response = regionDubboService.selectRegionByParentCode("");
        if (response == null) {
            return "-";
        }
        List<RegionDTO> regionDTOList = response.getResultObject();
        if (CollectionUtils.isEmpty(regionDTOList)) {
            return "-";
        }
        StringBuffer regionNames = new StringBuffer();
        for (RegionDTO dto : regionDTOList) {
            for (String code : codes) {
                if (code.equals(dto.getCode())) {
                    regionNames.append(dto.getRegionName());
                    regionNames.append(",");
                }
            }
        }
        return regionNames.substring(0, regionNames.length() - 1);
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

}
