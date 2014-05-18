/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tihai.common.Page;
import com.tihai.common.Pageable;

/**
 * Dao - 基类
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public interface BaseDao<T, ID extends Serializable> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	T find(@Param("id")ID id);

	/**
	 * 查找全部实体对象
	 * @return
	 */
	List<T> findAll();
	/**
	 * 查找实体对象集合
	 * 
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	List<T> findList(@Param("first")Integer first, @Param("count")Integer count);

	/**
	 * 查找实体对象分页
	 * 
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	List<T> findPage(Pageable pageable);

	/**
	 * 查询实体对象数量
	 * 
	 * @param filters
	 *            筛选
	 * @return 实体对象数量
	 */
	long countAll();
	/**
	 * 查询实体对象数量
	 * 
	 * @param filters
	 *            筛选
	 * @return 实体对象数量
	 */
	long count(@Param("conditions")String conditions);

	/**
	 * 持久化实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void save(T entity);

	/**
	 * 合并实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	T update(T entity);

	/**
	 * 移除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void deleteById(@Param("id")ID id);
	
	/**
	 * 移除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	void delete(T entity);

	/**
	 * 查找实体并锁定，更新之后自动解锁
	 * @param id
	 */
	void findForUpdate(@Param("id")ID id);
	
	/**
	 * 获取实体对象ID
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象ID
	 */
	ID getIdentifier(T entity);

}