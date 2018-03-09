package com.yatang.xc.xcr.biz.core.dubboservice.impl;

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

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.converter.DateConverter;
import com.yatang.xc.xcr.biz.core.domain.MessagePushPO;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgDubboService;
import com.yatang.xc.xcr.biz.core.enums.PublicEnum;
import com.yatang.xc.xcr.biz.core.service.MessagePushService;

import java.util.Arrays;

/**
 * @param <T>
 * @描述: 消息dubbo业务服务.
 * @作者: huangjianjun
 * @创建时间: 2017年3月28日-下午9:24:47 .
 * @版本: 1.0 .
 */
@Service("msgDubboService")
@Transactional(propagation = Propagation.REQUIRED)
public class MsgDubboServiceImpl implements MsgDubboService {
    protected final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MessagePushService messagePushService;

    @Override
    public Response<MsgPushDTO> editMsg(MsgPushDTO dto) {
        log.info("消息保存 -> in editMsg ... MsgPushDTO:" + JSONObject.toJSONString(dto));
        MsgPushDTO msgPushDTO = new MsgPushDTO();
        Response<MsgPushDTO> res = new Response<MsgPushDTO>();
        MessagePushPO po = new MessagePushPO();
        try {
            ConvertUtils.register(new DateConverter(null), java.util.Date.class);
            BeanUtils.copyProperties(po, dto);
            String[] areaArr = dto.getAreaArr();
            String areaStr = "";
            if (areaArr != null && areaArr.length > 0) {
                areaStr = arrayToString(areaArr);
                po.setAreaStr(areaStr);
            }
            if (po.getType().equals("0")) {
                if (po.getId() == 0 && messagePushService.checkTitleExist(po.getTitle())) {
                    res.setCode(PublicEnum.BUSINESS_SAVE_CHECK_CODE.getCode());
                    res.setErrorMessage(PublicEnum.BUSINESS_SAVE_CHECK_STR.getCode());
                    return res;
                }
            }
            log.info("消息保存 -> MessagePushPO:" + JSONObject.toJSONString(po));
            long id = messagePushService.savaOrUpdate(po);
            msgPushDTO.setId(id);
            res.setCode(PublicEnum.SUCCESS_CODE.getCode());
            res.setSuccess(true);
            res.setResultObject(msgPushDTO);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            res.setCode(PublicEnum.ERROR_CODE.getCode());
            res.setSuccess(false);
            res.setErrorMessage(PublicEnum.ERROR_MSG.getCode());
        }
        return res;
    }

    /**
     * 数组转字符串
     *
     * @param a
     * @return
     */
    public static String arrayToString(Object[] a) {
        int iMax = a.length - 1;
        if (iMax == -1)
            return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Response deleteMsg(Long id) {
        Response res = new Response();
        try {
            messagePushService.deleteByPrimaryKey(id);
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

}
