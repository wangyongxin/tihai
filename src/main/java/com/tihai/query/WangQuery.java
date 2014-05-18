package com.tihai.query;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tihai.dao.WangDao;
import com.tihai.entity.Wang;

/**
 * @description
 */
@Service
public class WangQuery {

	@Resource
	private WangDao wangDao;

	public Wang query(Integer id) {
		return wangDao.find(id);
	}

	public Integer save(Wang wang) {
		return wangDao.save(wang);
	}
}
