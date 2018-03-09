package test.com.yatang.xc.train;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.train.domain.TrainInfoPO;
import com.yatang.xc.xcr.biz.train.service.TrainInfoService;

public class TestTrainServiceImpl extends BaseJunit {
	@Autowired
	private TrainInfoService service;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testSaveTrainInfo() {
		TrainInfoPO bean = new TrainInfoPO();
		bean.setName("手动调价");
		bean.setContent("contentcontentcontentcontent手动调价contentcontentcontentcontentcontentcontent");
		bean.setTrainLength(10);
		bean.setStatus("0");
		bean.setCreateUid(1);
		bean.setIcon("image/insertGoods.png");
		bean.setCreateTime(new Date());
		bean.setReleasesTime(new Date());
		bean.setRemark("remark手动调价contentcontentcontentcontentcontentcontent");
		service.saveTrainInfo(bean);
	}

	@Test
	public void testFindByPrimaryKey() {
		TrainInfoPO bean = service.findByPrimaryKey(1L);
		System.out.println(bean.toString());
	}

	@Test
	public void testListPage() {
		PageInfo<TrainInfoPO> pageInfo = service.getTrainInfoPage(1, 10, null);
		List<TrainInfoPO> list = pageInfo.getList();
		for (Object object : list) {
			TrainInfoPO bean = (TrainInfoPO) object;
			System.out.println(bean.toString());
		}
	}

	@Test
	public void testDeleteByPrimaryKey() {
		service.deleteByPrimaryKey(2L);
	}

	@Test
	public void testUpdateByPrimaryKey() {
		TrainInfoPO po = new TrainInfoPO();
		po.setId(8L);
		po.setName("批量删除");
		po.setModifyTime(new Date());
		po.setContent("contentcontentcontentcontent批量删除1contentcontentcontentcontentcontentcontent");
		service.updateByPrimaryKey(po);
	}

	@Test
	public void testGetListPublishedTrain() {
		List<TrainInfoPO> listBean = service.getListPublishedTrain("1");
		for (TrainInfoPO bean : listBean) {
			System.out.println(bean.getName() + "--" + bean.getContent() + "--" + bean.getIcon());
		}
	}

	@Test
	public void testGetReleaseCount() {
		Long count = service.getReleaseCount();
		System.out.println(count);
	}

	@Test
	public void testDownShelfOrReleases() {
		System.out.println(service.downShelfOrReleases(30L, "1"));
	}

	@Test
	public void testQueryMaxClassId() {
		Long i = service.queryMaxClassReleaseTime();
		System.out.println(i);
	}

}
