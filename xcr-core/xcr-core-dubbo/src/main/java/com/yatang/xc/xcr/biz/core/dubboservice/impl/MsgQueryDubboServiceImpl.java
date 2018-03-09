package com.yatang.xc.xcr.biz.core.dubboservice.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.converter.DateConverter;
import com.yatang.xc.xcr.biz.core.domain.MessagePushPO;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgQueryDubboService;
import com.yatang.xc.xcr.biz.core.enums.PublicEnum;
import com.yatang.xc.xcr.biz.core.service.MessagePushService;

/**
 * @param <T>
 * @描述: 消息只读服务
 * @作者: huangjianjun
 * @创建时间: 2017年3月29日-下午4:52:52 .
 * @版本: 1.0 .
 */
@Service("msgQueryDubboService")
@Transactional(propagation=Propagation.REQUIRED)
public class MsgQueryDubboServiceImpl implements MsgQueryDubboService {
    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MessagePushService messagePushService;

    @Override
    public Response<Map<String, Object>> getMsgList(MsgPushDTO param, int pageNum, int pageSize, String shopCode, String provinceId) {
        Response<Map<String, Object>> res = new Response<Map<String, Object>>();
        List<MsgPushDTO> dtoList = new ArrayList<MsgPushDTO>();
        try {
            MsgPushDTO dto = null;
            ConvertUtils.register(new DateConverter(null), java.util.Date.class);
            PageInfo<MessagePushPO> pageInfo = messagePushService.getMsgListPage(pageNum, pageSize, param != null ? param.getStatus() : null, shopCode, provinceId);
            List<MessagePushPO> list = pageInfo.getList();
            for (MessagePushPO po : list) {
                dto = new MsgPushDTO();
                BeanUtils.copyProperties(dto, po);
                dtoList.add(dto);
            }
            Map<String, Object> rmap = new HashMap<String, Object>();
            rmap.put("total", pageInfo.getTotal());
            rmap.put("data", dtoList);
            res.setCode(PublicEnum.SUCCESS_CODE.getCode());
            res.setSuccess(true);
            res.setResultObject(rmap);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            res.setCode(PublicEnum.ERROR_CODE.getCode());
            res.setSuccess(false);
            res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
        }
        return res;
    }

    @Override
    public Response<Map<String, Object>> getMsgListBack(MsgPushDTO param, int pageNum, int pageSize) {
        Response<Map<String, Object>> res = new Response<>();
        List<MsgPushDTO> dtoList = new ArrayList<>();
        try {
            MsgPushDTO dto = null;
            ConvertUtils.register(new DateConverter(null), java.util.Date.class);
            PageInfo<MessagePushPO> pageInfo = messagePushService.getMsgListPageBack(pageNum, pageSize, param != null ? param.getStatus() : null);
            List<MessagePushPO> list = pageInfo.getList();
            for (MessagePushPO po : list) {
                dto = new MsgPushDTO();
                BeanUtils.copyProperties(dto, po);
                String areaStr = po.getAreaStr();
                if (areaStr != null && areaStr.length() > 0) {
                    String[] areaArr = areaStr.split(",");
                    dto.setAreaArr(areaArr);
                }
                dtoList.add(dto);
            }
            Map<String, Object> rmap = new HashMap<String, Object>();
            rmap.put("total", pageInfo.getTotal());
            rmap.put("data", dtoList);
            res.setCode(PublicEnum.SUCCESS_CODE.getCode());
            res.setSuccess(true);
            res.setResultObject(rmap);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            res.setCode(PublicEnum.ERROR_CODE.getCode());
            res.setSuccess(false);
            res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
        }
        return res;
    }

    @Override
    public Response<MsgPushDTO> findOneMsg(Long id) {
        Response<MsgPushDTO> res = new Response<MsgPushDTO>();
        MessagePushPO po = messagePushService.findByPrimaryKey(id);
        MsgPushDTO dto = new MsgPushDTO();
        try {
            ConvertUtils.register(new DateConverter(null), java.util.Date.class);
            BeanUtils.copyProperties(dto, po);
            log.info("MessagePushPO ->" + JSONObject.toJSONString(po));
            log.info("MsgPushDTO ->" + JSONObject.toJSONString(dto));
            res.setCode(PublicEnum.SUCCESS_CODE.getCode());
            res.setSuccess(true);
            res.setResultObject(dto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            res.setCode(PublicEnum.ERROR_CODE.getCode());
            res.setSuccess(false);
            res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
        }
        return res;
    }

    @Override
    public Response<Integer> getMsgCount() {
        Response<Integer> res = new Response<Integer>();
        try {
            Integer count = messagePushService.getMsgCount();
            res.setCode(PublicEnum.SUCCESS_CODE.getCode());
            res.setSuccess(true);
            res.setResultObject(count);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            res.setCode(PublicEnum.ERROR_CODE.getCode());
            res.setSuccess(false);
            res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
        }
        return res;
    }

}
