package test.com.yatang.xc.xcr.biz.core;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgDubboService;

/**
 * @描述: 课堂服务
 * @作者: huangjianjun
 * @创建时间: 2017年3月31日-下午8:36:20 .
 * @版本: 1.0 .
 * @param <T>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestMsgDubboService {
	@Autowired
	private MsgDubboService service;

	/**
	 * MsgPushDTO: {
	 "type": "1"
	 "contentHtml": "审核通过",
	 "status": "1",
	 "createUid": 0,
	 "pushType": "1",
	 "pushTo": "A002582",
	 "title": "店铺公告审核通过，发布成功",
	 "createTime": 1512118119517,
	 "modifyTime": 1512118119517,
	 "releasesTime": 1512118119517,
	 "shopNo": "A002582",
	 }
	 */
	@Test
	public void testPersistence(){
		MsgPushDTO msgPushDTO = new MsgPushDTO();
		msgPushDTO.setType("1");
		msgPushDTO.setContentHtml("审核通过");
		msgPushDTO.setStatus("1");
		msgPushDTO.setCreateUid(0);
		msgPushDTO.setPushType("1"); //0：所有，1：定向，2:区域
		msgPushDTO.setPushTo("A002582");
		msgPushDTO.setTitle("店铺公告审核通过，发布成功");
		msgPushDTO.setCreateTime(new Date());
		msgPushDTO.setModifyTime(new Date());
		msgPushDTO.setReleasesTime(new Date());
		msgPushDTO.setShopNo("A002582");
		Response<MsgPushDTO> resp  = service.editMsg(msgPushDTO);
		System.out.println(resp.isSuccess());
	}
}
