package test.com.yatang.xc.xcr.biz.core;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.SupplyAdvertisementGroupDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.SupplyChainAdvertisementDubboService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TsetSupplyChainAdvertiseDubboService {
	
	@Autowired
	SupplyChainAdvertisementDubboService supplyChainAdvertisementDubboService;
	
	/*@Test
	public void findGroupTest(){
		XcAdvertisementQueryDTO xcAdvertisementQueryDTO = new XcAdvertisementQueryDTO();
		xcAdvertisementQueryDTO.setStart(0);
		xcAdvertisementQueryDTO.setLength(10);
		Response<List<AdvertisementGroupDTO>> response = supplyChainAdvertisementDubboService.findGroup(xcAdvertisementQueryDTO);
		System.err.println(JSONObject.toJSON(response));
	}
	
	@Test
	public void findByGroupId(){
		
		Response<List<AdvertisementDTO>> dsResponse =  supplyChainAdvertisementDubboService.findAdvertisementByGroupId(13);
		System.err.println(JSONObject.toJSON(dsResponse));
	}*/
	
	
/*	@Test
	public void insert(){
		
		AdvertisementGroupDTO advertisementGroupDTO = new AdvertisementGroupDTO();
		advertisementGroupDTO.setGroupName("test");
		advertisementGroupDTO.setPositionCode(3);
		advertisementGroupDTO.setId(13);
		List<AdvertisementDTO> advertisementDTOs = new ArrayList<AdvertisementDTO>();
		supplyChainAdvertisementDubboService.insertSupplyChainAdvertisement(advertisementGroupDTO, advertisementDTOs);
		
	}*/
	
/*	@Test
	public void updateStateTest(){
		XcAdvertisementUpdateDTO xcAdvertisementUpdateDTO = new XcAdvertisementUpdateDTO ();
		xcAdvertisementUpdateDTO.setId(13);
		xcAdvertisementUpdateDTO.setState(1);
		supplyChainAdvertisementDubboService.updateGroupState(xcAdvertisementUpdateDTO);
		
	}*/
	
	@Test
	public void findPublishAdver(){
		Response<List<SupplyAdvertisementGroupDTO>> dubboResult =  supplyChainAdvertisementDubboService.findSupplyChainPublishAdver();
		
		System.out.println(JSONObject.toJSON(dubboResult));
	}

}
