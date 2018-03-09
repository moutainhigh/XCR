package test.com.yatang.xc.xcr.biz.train;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.train.dto.TrainInfoDTO;
import com.yatang.xc.xcr.biz.train.dubboservice.TrainQueryDubboService;
import com.yatang.xc.xcr.biz.train.enums.PublicEnum;

/**
 * @描述: 课堂只读服务
 * @作者: huangjianjun
 * @创建时间: 2017年3月31日-下午8:36:20 .
 * @版本: 1.0 .
 * @param <T>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestTranQueryDubboService {
	@Autowired
	private TrainQueryDubboService service;
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetListPage(){
		TrainInfoDTO dto = new TrainInfoDTO();
		dto.setStatus(1);
		Response<Map<String, Object>> res = service.getTrainList(dto,1,5);
		Map<String, Object> map = res.getResultObject();
		System.out.println("总记录数:"+map.get("total"));
		for (TrainInfoDTO bean : (List<TrainInfoDTO>)map.get("data")) {
			System.out.println(bean.toString());
		}
	}
	
	@Test
	public void testGetListPublishedTrain(){
		Response<List<Map<String,Object>>> list = service.getListPublishedTrain();
		List<Map<String,Object>> ss = list.getResultObject();
		for (Map<String, Object> map : ss) {
			System.out.println("ID:"+map.get(PublicEnum.ID.getCode())+"--NAME:"+map.get(PublicEnum.NAME.getCode()));
		}
	}
	
	@Test
	public void testSelectOne(){
		Response<TrainInfoDTO> resp = service.findOneTrain(28L);
		TrainInfoDTO bean  = resp.getResultObject();
		System.out.println(bean.toString());
	}
	
	@Test
	public void testGetNewTrainCount(){
		Response<Long> res = service.getTrainCount();
		System.out.println(res.getResultObject());
	}
	
	@Test
	public void testCheckName(){
		Response<Boolean> res = service.checkNameExist("扫码调价");
		System.out.println(res.getResultObject());
	}
	
}
