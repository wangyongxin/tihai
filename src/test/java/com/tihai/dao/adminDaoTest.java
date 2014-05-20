package com.tihai.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.tihai.common.BaseTest;
import com.tihai.entity.Admin;

public class adminDaoTest extends BaseTest{
	
	@Resource
	private AdminDao adminDao;
	
	@Test
	public void saveTest(){
		Admin admin = new Admin();
		admin.setName("test");
		admin.setPassword("test");
		admin.setEmail("test@163.com");
		admin.setUsername("test");
		admin.setDepartment("开发部");
		admin.setIsEnabled(true);
		admin.setIsLocked(false);
		admin.setLockedDate(null);
		admin.setLoginDate(new Date());
		admin.setLoginFailureCount(0);
		admin.setLoginIp(null);
		adminDao.save(admin);
	}
	
}
