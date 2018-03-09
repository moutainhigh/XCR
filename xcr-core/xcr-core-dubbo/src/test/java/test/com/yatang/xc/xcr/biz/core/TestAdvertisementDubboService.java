package test.com.yatang.xc.xcr.biz.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.core.dto.AdvertisementUpdateDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.AdvertisementDubboService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestAdvertisementDubboService {
	
	@Autowired
	AdvertisementDubboService advertisementDubboService;
	
	
	
	
	
	@Test
	public void findTest(){
		System.out.println(advertisementDubboService.validateTotalAdver(1));
		
	}
	
	
//	@Test
//	public void testInsert(){
//		AdvertisementDTO advertisementDTO = new AdvertisementDTO();
//		advertisementDTO.setActivityUrl("SDAD");
//		advertisementDTO.setCreateTime(new Date().toString());
//		advertisementDTO.setPicName("ceshi");
//		advertisementDTO.setPicUrl("www.baidu.com");
//		advertisementDTO.setState(0);
//		advertisementDTO.setRemaind("3");		
//		advertisementDTO.setType(1);
//		advertisementDTO.setGroupId(1);
//		advertisementDubboService.insertAdvertis(advertisementDTO);
//	}
//	
//	@Test
//	public void testupdate(){
//		advertisementDubboService.updateState(4, 1);
//	}
//	
//	@Test
//	public void testFind(){
//		
//
//		Response<List<AdvertisementDTO>> res = advertisementDubboService.findstate(100);
//		
//		List<AdvertisementDTO> advertisementDTOs =  res.getResultObject();
//		for (AdvertisementDTO advertisementDTO : advertisementDTOs) {
//			System.out.println(advertisementDTO);
//			System.out.println("***********************");
//		}
		
		
//		@Test
//		public void testFindfindstate(){
//			Integer positionCode=100;
//			List<AdvertisementDTO> advertisementDTOs = advertisementDubboService.findstate(positionCode).getResultObject();
//			for (AdvertisementDTO advertisementDTO : advertisementDTOs) {
//				System.out.println(advertisementDTO);
//				System.out.println("*******************************");
//			}
//			
	
	
	
	@Test
	public void test(){
		AdvertisementUpdateDTO ad = new AdvertisementUpdateDTO();
		ad.setId(86);
		ad.setPositionCode("100");
		ad.setState(1);
		advertisementDubboService.updateAdvertisementGroup(ad);
		
		
	}
		}
			
			
			
//			@Test
//			public void testInsertAdvertisementGroup(){
//				AdvertisementGroupDTO advertisementGroupPO = new AdvertisementGroupDTO();
//				advertisementGroupPO.setCreateTime(new Date().toString());
//				advertisementGroupPO.setDescription("aaaa");
//				advertisementGroupPO.setGroupName("第二组");
//				advertisementGroupPO.setIsEnable(0);
//				advertisementGroupPO.setLastModifyTime(new Date().toString());
//				advertisementGroupPO.setPositionCode(200);				
//				advertisementDubboService.insertAdvertisementGroup(advertisementGroupPO);
//				
//				}
				
//				@Test
//				public void testUpdateAdvertisementGroup(){
//					
//					AdvertisementUpdateDTO updateDTO = new AdvertisementUpdateDTO();
//					updateDTO.setId(2);
//					updateDTO.setState(0);
//					advertisementDubboService.updateAdvertisementGroup(updateDTO);
//					}
					
//					@Test
//					public void testFindAdvertisementGroup(){
//						
//						AdvertisementGroupQueryDTO advertisementGroupPO = new AdvertisementGroupQueryDTO();
//						
//						List<AdvertisementGroupDTO> a = advertisementDubboService.findAdvertisementGroup(advertisementGroupPO).getResultObject();
//						
//						for (AdvertisementGroupDTO advertisementGroupDTO : a) {
//							System.out.println(advertisementGroupDTO);
//							System.out.println("***************************");
//						}
//						}

	
//
//}
