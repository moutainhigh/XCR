package test.com.yatang.xc.xcr.biz.core;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.MsgPushDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.MsgQueryDubboService;

/**
 * @描述: 课堂服务
 * @作者: huangjianjun
 * @创建时间: 2017年3月31日-下午8:36:20 .
 * @版本: 1.0 .
 * @param <T>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestMsgQueryDubboService {
	@Autowired
	private MsgQueryDubboService service;


	@Test
	@SuppressWarnings("unchecked")
	public void testGetListPage(){
		MsgPushDTO param = new MsgPushDTO();
		Response<Map<String, Object>> res = service.getMsgList(param, 1, 20,"","");
		Map<String, Object> map = res.getResultObject();
		System.out.println("总记录数:"+map.get("total"));
		for (MsgPushDTO bean : (List<MsgPushDTO>)map.get("data")) {
			System.out.println("beans:"+bean.toString());
		}
	}
	
	@Test
	public void testSelectOne(){
		MsgPushDTO bean  = service.findOneMsg(69L).getResultObject();
		System.out.println(bean.toString());
	}
	
	@Test
	public void testNewMsgCount(){
		Response<Integer> res = service.getMsgCount();
		System.out.println(res.getResultObject());
	}
}

