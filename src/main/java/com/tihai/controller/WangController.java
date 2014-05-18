package com.tihai.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tihai.entity.Wang;
import com.tihai.query.WangQuery;


/**
 * ≤‚ ‘
 * 
 * @author wangyongxin
 */
@Controller
public class WangController {

	@Resource
	private WangQuery wangQuery;


	@RequestMapping(method = RequestMethod.GET, value = "/wang/test")
	public ModelAndView accountEditConfirm(HttpServletRequest request, Integer id){
		if(id == null){
			id = 1;
		}
		Wang wang = wangQuery.query(id);
		ModelAndView map = new ModelAndView("wang/test");
		map.addObject("wang", wang); 
		map.addObject("test", "jetty works"); 
		map.addObject("test2", "hot deploy works"); 
		return map;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/wang/testSave")
	public ModelAndView save(HttpServletRequest request){
		ModelAndView map = new ModelAndView("wang/testSave");
		Wang wang = new Wang();
		wang.setName("lucy:"+new Date());
		wangQuery.save(wang);
		map.addObject("id", wang.getId()); 
		return map;
	}

}
