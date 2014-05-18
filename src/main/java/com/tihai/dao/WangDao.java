package com.tihai.dao;

import org.apache.ibatis.annotations.Param;

import com.tihai.entity.Wang;

/**
 * 
 */
public interface WangDao {

	Wang find(@Param("id")Integer id);

	Integer save(Wang wang);
}
