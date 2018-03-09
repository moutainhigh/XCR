package test.com.yatang.xc.xcr.service.impl;

import com.yatang.xc.xcr.dto.inputs.AddNewGoodsDto;
import com.yatang.xc.xcr.dto.inputs.EditNewGoodsDto;
import com.yatang.xc.xcr.dto.inputs.NewGoodsDetailDto;
import com.yatang.xc.xcr.dto.inputs.NewGoodsListDto;
import com.yatang.xc.xcr.model.RequestBaseModel;
import com.yatang.xc.xcr.model.ResultMap;
import com.yatang.xc.xcr.service.ICommodityManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNull;

/**
 * @Author : BobLee
 * @CreateTime : 2017年12月14日 10:14
 * @Summary :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test/applicationContext-test.xml"})
public class CommodityManagementServiceImplTest {


    @Autowired
    private ICommodityManagementService commodityManagementService;

    @Test
    public void editNewGoods() throws Exception {
        EditNewGoodsDto model = new EditNewGoodsDto();
        model.setUserId("jms_902004");
        model.setStoreSerialNo("A902004");


        ResultMap resultMap = commodityManagementService.editNewGoods(model);
        assertNull(resultMap);
        throw new RuntimeException(resultMap.toString());
    }

    @Test
    public void newGoodsDetail() throws Exception {
        NewGoodsDetailDto model = new NewGoodsDetailDto();
        model.setUserId("jms_902004");
        model.setStoreSerialNo("A902004");
        model.setGoodsCode("6958966816217");

        ResultMap resultMap = commodityManagementService.newGoodsDetail(model);
        assertNull(resultMap);
        throw new RuntimeException(resultMap.toString());
    }

    @Test
    public void addNewGoods() throws Exception {
        AddNewGoodsDto model = new AddNewGoodsDto();
        model.setUserId("jms_902004");
        model.setStoreSerialNo("A902004");

        ResultMap resultMap = commodityManagementService.addNewGoods(model);
        assertNull(resultMap);
        throw new RuntimeException(resultMap.toString());
    }

    @Test
    public void newGoodsList() throws Exception {
        NewGoodsListDto model = new NewGoodsListDto();
        model.setUserId("jms_902004");
        model.setStoreSerialNo("A902004");
        model.setSearch("牛肉");
        model.setPageIndex(1);
        model.setPageSize(20);

        ResultMap resultMap = commodityManagementService.newGoodsList(model);
        assertNull(resultMap);
        throw new RuntimeException(resultMap.toString());
    }

    @Test
    public void newClassifyList() throws Exception {
        RequestBaseModel model = new RequestBaseModel();
        model.setUserId("jms_902004");
        model.setStoreSerialNo("A902004");

        ResultMap resultMap = commodityManagementService.newClassifyList(model);
        assertNull(resultMap);
        throw new RuntimeException(resultMap.toString());
    }

}