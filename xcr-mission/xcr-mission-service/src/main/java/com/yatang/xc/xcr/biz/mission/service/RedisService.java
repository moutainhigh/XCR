package com.yatang.xc.xcr.biz.mission.service;


public interface RedisService {
 

    /**
     * 根据存储对象ID和存储对象class,从redis中获取对象
     * 
     * @param pId
     * @param objClass
     * @return
     */
    public String getItem(String key);


    /**
     * 保存指定对象到redis中
     * 
     * @param pItemObj
     * @return
     */
    public void saveItem(String key, Integer time,String value);

}
