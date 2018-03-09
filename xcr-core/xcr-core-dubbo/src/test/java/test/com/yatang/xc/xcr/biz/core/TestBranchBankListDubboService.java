package test.com.yatang.xc.xcr.biz.core;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.busi.common.resp.Response;
import com.yatang.xc.xcr.biz.core.dto.BranchBankDTO;
import com.yatang.xc.xcr.biz.core.dto.ResultBranchBankDTO;
import com.yatang.xc.xcr.biz.core.dubboservice.BranchBankListDubboService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:test/applicationContext-test.xml" })
public class TestBranchBankListDubboService {
	@Autowired
	private BranchBankListDubboService branchBankListDubboService;

	@Test
	public void testByname() {
		BranchBankDTO branchBankDTO = new BranchBankDTO();
		branchBankDTO.setBranchbankname("富水中路");
		branchBankDTO.setPageIndex(1);
		branchBankDTO.setPageSize(10);
		Response<ResultBranchBankDTO> res = branchBankListDubboService.findBranchListByBankName(branchBankDTO);
		ResultBranchBankDTO resultBranchBankDTO = res.getResultObject();
		List<BranchBankDTO> branchBankDTOs = resultBranchBankDTO.getBranchBanks();
		for (BranchBankDTO branchBankDTO2 : branchBankDTOs) {
			System.out.println(branchBankDTO2.getBranchbankname());
		}
	}

	@Test
	public void testByCode() {
		BranchBankDTO branchBankDTO = new BranchBankDTO();
		branchBankDTO.setBankno("320493100027");
		branchBankDTO.setPageIndex(1);
		branchBankDTO.setPageSize(10);
		Response<ResultBranchBankDTO> res = branchBankListDubboService.findBranchListByBankCode(branchBankDTO);
		ResultBranchBankDTO resultBranchBankDTO = res.getResultObject();
		List<BranchBankDTO> branchBankDTOs = resultBranchBankDTO.getBranchBanks();
		for (BranchBankDTO branchBankDTO2 : branchBankDTOs) {
			System.out.println(branchBankDTO2.getBranchbankname());
		}
	}
}
