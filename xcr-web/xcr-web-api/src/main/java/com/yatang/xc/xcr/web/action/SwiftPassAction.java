package com.yatang.xc.xcr.web.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.service.SwiftPassService;
import com.yatang.xc.xcr.utils.SignUtils;
import com.yatang.xc.xcr.utils.XmlUtils;
import com.yatang.xc.xcr.vo.SwiftPassReturn;

/**
 * 威富通回调
 * Created by wangyang on 2017/9/25.
 */
@Controller
@RequestMapping("xcr/api/swiftpass")
public class SwiftPassAction {

    private static Logger log = LoggerFactory.getLogger(SwiftPassAction.class);

    //秘钥
    @Value("${SWIFTPASS_KEY}")
    private String SWIFTPASS_KEY;

    private static final String TRADE_TYPE_CONTRADCT = "pay.weixin.app";

    /**
     * 接口请求、响应Body字符集<pre>charset=UTF-8</pre>
     */
    private final static String STREAM_CHARSET = ";charset=UTF-8";

    @Autowired
    private SwiftPassService swiftPassService;

    @ResponseBody
    @RequestMapping(value = "/index", method = {RequestMethod.POST, RequestMethod.GET})
    public String index() {
        log.info("xcr-web-api connection success !!!");
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/returnMessage", method = RequestMethod.POST)
    public String rerurnMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("in swiftpass ...");
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = XmlUtils.parseRequst(req);
            log.info("swiftpass -> 通知内容 -> resString:" + resString);
            if (StringUtils.isEmpty(resString)) {
                log.error("通知内容为空 -> resString:" + resString);
                return "fail";
            }
            Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
            String res = XmlUtils.toXml(map);
            log.info("swiftpass -> 通知内容 -> res:" + res);
            if (map.containsKey("sign")) {
                if (!SignUtils.checkParam(map, SWIFTPASS_KEY)) {
                    log.error("验证签名不通过!");
                    return "fail";
                }
                if (TRADE_TYPE_CONTRADCT.equals(map.get("trade_type"))) {
                    log.info("本次交易为加盟定金交易，不处理 ..." + JSONObject.toJSONString(map));
                    return "fail";
                }
                log.info("swiftpass -> 验证签名 -> 已通过 !!!");
                SwiftPassReturn swiftPassReturn = getSwiftPassMessage(map);
                log.info("swiftpass -> 收到参数 -> swiftPassReturn:" + JSONObject.toJSONString(swiftPassReturn));
                Response<String> response = swiftPassService.saveStreamAndSendMessage(swiftPassReturn);
                if (response == null) {
                    log.error("swiftPassDubboService.saveStreamAndSendMessage(swiftPassReturnDto) -> response == null !!");
                    return "fail";
                }
                log.info("swiftpass -> 存流水发消息操作 -> response:[" + response.isSuccess() + "," + response.getResultObject() + "]");
                if (response.isSuccess()) {
                    return "success";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "fail";
    }

    /**
     * 获取回调参数对象
     *
     * @param map
     * @return
     */
    private SwiftPassReturn getSwiftPassMessage(Map<String, String> map) {
        SwiftPassReturn passRetuen = new SwiftPassReturn();
        String out_trade_no = map.get("out_trade_no");
        String total_fee = map.get("total_fee");
        String mch_id = map.get("mch_id");
        String openid = map.get("openid");
        String trade_type = map.get("trade_type");
        String time_end = map.get("time_end");

        String shopCode = swiftPassService.getShopCodeByMchId(mch_id);
        if (StringUtils.isEmpty(shopCode)) {
            passRetuen.setHaveShopCode(0);
        } else {
            passRetuen.setHaveShopCode(1);
        }
        passRetuen.setMerchants_id(mch_id);
        passRetuen.setMch_id(shopCode);
        passRetuen.setOpenid(openid);
        passRetuen.setOut_trade_no(out_trade_no);
        if (StringUtils.isNotEmpty(total_fee)) {
            passRetuen.setTotal_fee(Integer.valueOf(total_fee));
        }
        passRetuen.setTrade_type(trade_type);
        passRetuen.setTime_end(time_end);
        return passRetuen;
    }

    /**
     * 威富通支付回调
     *
     * @return
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE + STREAM_CHARSET, TEXT_XML_VALUE + STREAM_CHARSET},
            produces = {APPLICATION_XML_VALUE + STREAM_CHARSET, TEXT_XML_VALUE + STREAM_CHARSET})
    public
    @ResponseBody
    String payCallback(@RequestBody String xml) {
        return swiftPassService.swiftPassCompleteCallback(xml);
    }
}
