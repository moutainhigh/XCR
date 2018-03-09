package test.com.yatang.xc.xcr.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.org.dto.StoreDto;
import com.yatang.xc.mbd.biz.org.dubboservice.OrganizationService;
import com.yatang.xc.mbd.biz.org.dubboservice.OrgnazitionO2ODubboService;
import com.yatang.xc.mbd.biz.org.o2o.dto.RegistrationParameterDto;
import com.yatang.xc.mbd.biz.org.o2o.dto.StoreO2ODto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
public class TestMQAction {

	@Autowired
	private OrgnazitionO2ODubboService orgnazitionO2ODubboService;
	
	@Autowired
	private OrganizationService organizationDubboService;

	@Test
	public void test1() {
		Response<StoreO2ODto> res = orgnazitionO2ODubboService.queryStoreById("A900005");
		System.err.println(res);
		System.err.println(res.isSuccess());
		System.err.println(res.getResultObject());
	}

	@Test
	public void test2() {
		List<String> businessIds = new ArrayList<String>();
		/*businessIds.add("A902004");
		businessIds.add("A900005");*/
		businessIds.add("902004");
		businessIds.add("900005");
		System.err.println("dubbo调用开始 ...");
		//1:门店ID获取加盟商ID
		organizationDubboService.queryStoreById("A900005");
		//加盟商Id获取唯一标识
		Response<List<RegistrationParameterDto>> res = orgnazitionO2ODubboService.queryRegistrationId(businessIds);
		System.err.println("dubbo调用结束 ...");
		System.err.println(res);
		System.err.println(res.isSuccess());
		List<RegistrationParameterDto> dto = res.getResultObject();
		System.err.println(dto);
		if (CollectionUtils.isEmpty(dto)) {
			System.err.println("res.getResultObject() is null or size == 0");
		}
		System.err.println("dto.size():"+dto.size());
		for (RegistrationParameterDto d : dto) {
			System.err.println("getBusinessId:" + d.getBusinessId());
			System.err.println("getRegistrationId:" + d.getRegistrationId());
		}
	}

}
