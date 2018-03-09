package test.com.yatang.xc.xcr.biz.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.XcAdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.XcAdvertisementDubboService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TsetXcAdvertiseDubboService {
	
	@Autowired
	XcAdvertisementDubboService xcAdvertisementDubboService;
	
/*	@Test
	public void insert(){
		XcAdvertisementDTO xcAdvertisementDTO = new XcAdvertisementDTO();
		xcAdvertisementDTO.setImgUrl("www.baidu.com");
		xcAdvertisementDTO.setState(0);
		xcAdvertisementDTO.setType(2);
		Response<Boolean> response = xcAdvertisementDubboService.insertAdvertisement(xcAdvertisementDTO);
		System.err.println(JSONObject.toJSON(response));
	}*/
	
/*	@Test
	public void update(){
		XcAdvertisementDTO xcAdvertisementDTO = new XcAdvertisementDTO();
		xcAdvertisementDTO.setImgUrl("www.sohu.com");
		xcAdvertisementDTO.setId(26);
		Response<Boolean> response = xcAdvertisementDubboService.updateAdvertisement(xcAdvertisementDTO);
		System.out.println(JSONObject.toJSON(response));
	}*/
	
	
	@Test
	public void updateState(){
		XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO = new XcAdvertisementUpdateDTO();
		xcAdvertisementUpdateDTO.setId(26);
		xcAdvertisementUpdateDTO.setState(1);
		Response<Boolean> response = xcAdvertisementDubboService.updateState(xcAdvertisementUpdateDTO);
		
		System.out.println(JSONObject.toJSON(response));
		
		
	}
	
/*	@Test
	public void findById(){
		XcAdvertisementQueryDTO xcAdvertisementQueryDTO = new XcAdvertisementQueryDTO();
		//xcAdvertisementQueryDTO.setId(1);
		xcAdvertisementQueryDTO.setType(2);
		Response<List<XcAdvertisementDTO>> response = xcAdvertisementDubboService.findAllById(xcAdvertisementQueryDTO);
		System.err.println(JSONObject.toJSON(response));
	
	}*/

}
