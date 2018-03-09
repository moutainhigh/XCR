package com.yatang.xc.xcr.biz.service;


import com.yatang.xc.xcr.enums.RedisKeyEnum;

/**
 * 
 * 主要功能：保存对象到redis
 * @author liuli
 * @param <V extends Serializable>
 */
public interface RedisService<V> {
	/**
	 * 
	 * 功能说明： 保存对象到redis服务里去
	 * @author liuli
	 * @creationDate 2015年3月31日 上午10:51:04
	 * @param prefixKey 对应redisKey的前缀
	 * @param suffixKey 对应redisKey的后缀
	 * @param timeout 多久过期（按分钟计算）
	 * @param value 对象值（保存的对象必须实现Serializable接口）
	 */
	public void saveObject(RedisKeyEnum prefixKey,String suffixKey,int timeout,V value);

	/**
	 * 
	 * 功能说明：根据key到redis里查询对应的值
	 * @author liuli
	 * @creationDate 2015年3月31日 上午11:59:05
	 * @param prefixKey 对应redisKey的前缀
	 * @param suffixKey 对应redisKey的后缀
	 * @return V
	 */
	public V getKeyForObject(RedisKeyEnum prefixKey,String suffixKey,Class clazz);

	/**
	 * 
	 * 功能说明：根据key移除掉redis服务里的对象
	 * @author liuli
	 * @creationDate 2015年4月1日 下午2:56:58
	 * @param prefixKey 对应redisKey的前缀
	 * @param suffixKey 对应redisKey的后缀
	 */
	public void removeByKey(RedisKeyEnum prefixKey,String suffixKey);

	

}
