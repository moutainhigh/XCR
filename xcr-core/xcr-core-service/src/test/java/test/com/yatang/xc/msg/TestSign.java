package test.com.yatang.xc.msg;

import com.yatang.xc.xcr.biz.core.dao.UserSignDao;
import com.yatang.xc.xcr.biz.core.domain.UserSignSetPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wangyang on 2017/7/10.
 */
public class TestSign extends BaseJunit{

    @Autowired
    private UserSignDao dao;

    @Test
    public void test1(){
        UserSignSetPO po = new UserSignSetPO();
        po.setContent("测试内容。。。。");
        po.setIsDelete(1);
        po.setType(2);
        po.setData("10");
        dao.updateUserSignSet(po);
    }

}
