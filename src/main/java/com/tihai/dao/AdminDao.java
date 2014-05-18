/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.tihai.entity.Admin;

/**
 * Dao - 管理员
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public interface AdminDao extends BaseDao<Admin, Long> {

	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(@Param("username")String username);

	/**
	 * 根据用户名查找管理员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByUsername(@Param("username")String username);

}