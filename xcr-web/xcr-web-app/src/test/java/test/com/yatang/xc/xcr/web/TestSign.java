package test.com.yatang.xc.xcr.web;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.busi.common.utils.DES;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.xcr.util.MD5;
import com.yatang.xc.xcr.util.SignUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.region.dto.RegionBasicDTO;
import com.yatang.xc.mbd.biz.region.dubboservice.RegionDubboService;
import com.yatang.xc.xcr.biz.core.dubboservice.UserSignDubboService;

/**
 * Created by wangyang on 2017/7/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test/applicationContext-test.xml"})
public class TestSign {
    private static Logger log = LoggerFactory.getLogger(TestSign.class);

    @Autowired
    private RegionDubboService regionDubboService;

    @Autowired
    private UserSignDubboService userSignDubboService;


    public static final String SIGN_KEY = "yaxcrd19af8ntp27a5ae4a97d639oyn4";  //签名秘钥
    public static final String DES_KEY = "ytxcr985";


    @Test
    public void test1() {
        Response<List<RegionBasicDTO>> re = regionDubboService.selectRegionBasic();
        log.info(JSONObject.toJSONString(re));
    }

    /**
     * 组装加密参数
     *
     * @param shopCode
     * @param shopName
     * @param key
     * @return
     */
    private static SortedMap<String, String> getParamsMap(String shopCode, String shopName, String key) {
        SortedMap<String, String> map = new TreeMap();
        map.put("shopCode", shopCode);
        map.put("shopName", shopName);
        map.put("sign", getSign(map, key));
        return map;
    }

    /**
     * 组装签名
     *
     * @param map
     * @param key
     * @return
     */
    private static String getSign(SortedMap<String, String> map, String key) {
        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        return MD5.sign(preStr, "&key=" + key, "utf-8");
    }


    public static void main(String[] args) {

        String shopCode = "A902004";
        String shopName = "一个门店";
        SortedMap<String, String> sortedMap = getParamsMap(shopCode, shopName, SIGN_KEY);
        String json = JSON.toJSONString(sortedMap);
        try {
            String ss = DES.encryptDES(json, DES_KEY);

            System.err.println(ss);
        } catch (Exception e) {
            log.error("店铺身份二维码加密串生成 -> 加密异常 -> json:" + json);
            e.printStackTrace();
        }

    }

}
