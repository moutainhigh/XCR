package test.com.yatang.xc.msg;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yatang.xc.xcr.biz.core.domain.BranchBankPO;
import com.yatang.xc.xcr.biz.core.domain.ResultBranchBankPO;
import com.yatang.xc.xcr.biz.core.service.BranchBankService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class TestBranchList extends BaseJunit {
	@Autowired
	private BranchBankService branchBankService;

	/**
	 * 根据银行名获取支行列表
	 */
	@Test
	public void testSelectBranchList() {
		System.out.println("----");
		BranchBankPO branchBankPO = new BranchBankPO();
		branchBankPO.setBranchbankname("富水中路");
		branchBankPO.setPageIndex(1);
		branchBankPO.setPageSize(10);
		ResultBranchBankPO res = branchBankService.findBranchListByBankName(branchBankPO);
		System.out.println(res.getPageIndex());
		System.out.println(res.getPageSize());
		System.out.println(res.getTotalCount());
		System.out.println(res.getTotalPage());
		List<BranchBankPO> branchBankPOs = res.getBranchBanks();
		for (BranchBankPO branchBankPO2 : branchBankPOs) {
			System.out.println(branchBankPO2.getBranchbankname());
		}
	}

	/**
	 * 根据银行id获取支行列表
	 */
	@Test
	public void testSelectBranchListByCode() {
		System.out.println("----");
		BranchBankPO branchBankPO = new BranchBankPO();
		branchBankPO.setBankno("320493100027");
		branchBankPO.setPageIndex(1);
		branchBankPO.setPageSize(10);
		ResultBranchBankPO res = branchBankService.findBranchListByBankCode(branchBankPO);
		System.out.println(res.getPageIndex());
		System.out.println(res.getPageSize());
		System.out.println(res.getTotalCount());
		System.out.println(res.getTotalPage());
		List<BranchBankPO> branchBankPOs = res.getBranchBanks();
		for (BranchBankPO branchBankPO2 : branchBankPOs) {
			System.out.println(branchBankPO2.getBranchbankname());
		}
	}
}
