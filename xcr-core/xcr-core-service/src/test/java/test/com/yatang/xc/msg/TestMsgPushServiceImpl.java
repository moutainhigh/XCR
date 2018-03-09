package test.com.yatang.xc.msg;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.yatang.xc.xcr.biz.core.domain.MessagePushPO;
import com.yatang.xc.xcr.biz.core.service.MessagePushService;

public class TestMsgPushServiceImpl extends BaseJunit{
	@Autowired
	private MessagePushService service;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSaveMsgPush() {
		MessagePushPO bean = new MessagePushPO();
		bean.setTitle("五一狂欢节");
		bean.setImageUrl("172.10.30.20:21/etc/www/image/xxx.jpg");
		bean.setStatus("0");
		bean.setType("0");
		bean.setCreateUid(1);
		bean.setMsgUrl("www.baidu.com");
		bean.setCreateTime(new Date());
		bean.setReleasesTime(new Date());
		service.saveMsg(bean);
	}
	
	@Test
	public void testFindByPrimaryKey() {
		MessagePushPO bean = service.findByPrimaryKey(64L);
		System.out.println(bean.toString());
	}
	
	@Test
	public void testListPage() {
		PageInfo<MessagePushPO> pageInfo = service.getMsgListPage(1, 15, null,"","");
		List<MessagePushPO> list= pageInfo.getList();
		for (Object object : list) {
			MessagePushPO bean = (MessagePushPO)object;
			System.out.println(bean.toString());
		}
	}
	
	@Test
	public void testDeleteByPrimaryKey() {
		service.deleteByPrimaryKey(2L);
	}
	
	@Test
	public void testGetNewMsgCount() {
		service.getMsgCount();
	}
	
	@Test
	public void testCheckTitleExist() {
		String title = "五一狂欢节1";
		System.out.println(service.checkTitleExist(title));
	}
	
	@Test
	public void testUpdateByPrimaryKey() {
		MessagePushPO po = new MessagePushPO();
		po.setId(1L);
		po.setTitle("情人节活动1");
		po.setModifyTime(new Date());
		po.setImageUrl("172.10.30.20:21/etc/www/image/gxx.jpg");
		service.updateByPrimaryKey(po);
	}

}
