package com.yatang.xc.xcr.biz.train.dao;

import com.yatang.xc.xcr.biz.train.domain.TrainInfoPO;

import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @描述: 课堂数据访问Dao接口.
 * @作者: huangjianjun
 * @创建时间: 2017年3月22日-下午3:10:09 .
 * @版本: 1.0 .
 */
public interface TrainInfoDao {
    /**
     * @Description: 获取最近发布课程数
     * @author huangjianjun
     * @date 2017年4月10日 下午4:34:18
     */
    Long getReleaseCount();

    /**
     * @param id
     * @param status
     * @Description: 发布下架课堂
     * @author huangjianjun
     * @date 2017年4月19日 上午10:48:10
     */
    Integer downShelfOrReleases(Map<String, Object> param);

    /**
     * @Description: 检索课堂名称是否存在
     * @author huangjianjun
     * @date 2017年5月3日 下午1:36:05
     */
    Integer checkNameExist(String name);

    /**
     * 查询最大课堂Id
     *
     * @return
     */
    Long queryMaxClassReleaseTime();

    List<TrainInfoPO> listBy(Map<String, Object> paramMap);

    long insert(TrainInfoPO entity);

    TrainInfoPO getById(long id);

    int update(TrainInfoPO entity);

    int deleteById(long id);
}
