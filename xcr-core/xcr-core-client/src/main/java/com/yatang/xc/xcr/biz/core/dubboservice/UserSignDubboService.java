package com.yatang.xc.xcr.biz.core.dubboservice;


import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.UserSignDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignInfoDTO;
import com.yatang.xc.xcr.biz.core.dto.UserSignSetDTO;

import java.lang.reflect.InvocationTargetException;

/**
 * 签到服务
 * Created by wangyang on 2017/7/7.
 */
public interface UserSignDubboService {

    /**
     * 记录签到数据
     *
     * @param dto
     * @return
     */
    Response sign(UserSignDTO dto);


    /**
     * 获取当月签到列表数组
     *
     * @param dto
     * @return
     */
    Response<UserSignInfoDTO> getSignArray(UserSignDTO dto);

    Response insertSignSet(UserSignSetDTO dto);

    /**
     * 获取签到生效奖励信息
     *
     * @return
     */
    UserSignSetDTO getUserSignSetInfo();

    /**
     * 更新签到信息
     *
     * @param userSignSetDTO
     * @return
     */
    boolean updateUserSignSet(UserSignSetDTO userSignSetDTO);
}
