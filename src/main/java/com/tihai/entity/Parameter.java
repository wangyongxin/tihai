/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 参数
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class Parameter extends OrderEntity {

	private static final long serialVersionUID = -5833568086582136314L;

	/** 名称 */
	private String name;

	/** 参数组 */
	private ParameterGroup parameterGroup;

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取参数组
	 * 
	 * @return 参数组
	 */
	public ParameterGroup getParameterGroup() {
		return parameterGroup;
	}

	/**
	 * 设置参数组
	 * 
	 * @param parameterGroup
	 *            参数组
	 */
	public void setParameterGroup(ParameterGroup parameterGroup) {
		this.parameterGroup = parameterGroup;
	}

}