package com.yatang.xc.xcr.biz.core.dao;


import com.yatang.xc.xcr.biz.core.domain.XcrWhiteListPO;

/**
* @Author : BobLee
* @Summary : Mybatis
* @CreateTime: 2017年12月11日 11:15:25
*/
public interface XcrWhiteListDao {
    
	/**<Summary>
	 * @Function Description : 删除
	 * </Summary>
	 */
    void delete(java.util.Map<String, Object> param);

    /**<Summary>
	 * @Function Description : 更新
	 * </Summary>
	*/
    void update(java.util.Map<String, Object> param);

    /**<Summary>
	 * @Function Description : 查询一个列表
	 * </Summary>
	 */
    java.util.List<XcrWhiteListPO> list(java.util.Map<String, Object> param);

    /**<Summary>
	 * @Function Description :查询一条记录
	 * </Summary>
	*/
	XcrWhiteListPO one(java.util.Map<String, Object> param);

    /**<Summary>
	 * @Function Description : 新增
	 * </Summary>
	*/
    void insert(XcrWhiteListPO param);

}