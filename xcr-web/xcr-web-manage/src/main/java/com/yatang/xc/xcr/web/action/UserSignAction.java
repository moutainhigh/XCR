package com.yatang.xc.xcr.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.UserSignSetDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.UserSignDubboService;

/**
 * 用户签到管理 Created by wangyang on 2017/7/10.
 */
@Controller
@RequestMapping(value = "xcr")
public class UserSignAction {

    private static Logger log = LoggerFactory.getLogger(UserSignAction.class);

    @Autowired
    private UserSignDubboService userSignDubboService;

    /**
     * 获取奖励详情
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/signAward.htm", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getUserSignSetInfo(ModelMap map) {
        log.info("in getUserSignSetInfo ... ");
        UserSignSetDTO userSignSetDTO = userSignDubboService.getUserSignSetInfo();
        if (userSignSetDTO == null) {
            userSignSetDTO = new UserSignSetDTO(2, "0", "");
        }
        map.put("userSignSetDTO", userSignSetDTO);
        log.info("userSignSetDTO:" + userSignSetDTO);
        return new ModelAndView("/screen/sign/award", map);
    }

    /**
     * 更新奖励
     *
     * @param userSignSetDTO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sign/updateSet.htm")
    public Response<Boolean> updateUserSignSetInfo(@RequestBody UserSignSetDTO userSignSetDTO) {
        log.info("in updateUserSignSetInfo ... ");
        if (userSignSetDTO == null) {
            return new Response<>(false, "传入参数有误！！！", "");
        }
        boolean success = userSignDubboService.updateUserSignSet(userSignSetDTO);
        if (success) {
            return new Response<>(true, "保存成功", "");
        }
        return new Response<>(false, "保存失败！！！", "");
    }
}
