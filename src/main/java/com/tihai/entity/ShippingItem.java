/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 发货项
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class ShippingItem extends BaseEntity {

	private static final long serialVersionUID = 2756395514949325790L;

	/** 商品编号 */
	private String sn;

	/** 商品名称 */
	private String name;

	/** 数量 */
	private Integer quantity;

	/** 发货单 */
	private Shipping shipping;

	/**
	 * 获取商品编号
	 * 
	 * @return 商品编号
	 */
	@NotEmpty
	public String getSn() {
		return sn;
	}

	/**
	 * 设置商品编号
	 * 
	 * @param sn
	 *            商品编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取商品名称
	 * 
	 * @return 商品名称
	 */
	@NotEmpty
	public String getName() {
		return name;
	}

	/**
	 * 设置商品名称
	 * 
	 * @param name
	 *            商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	@NotNull
	@Min(1)
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取发货单
	 * 
	 * @return 发货单
	 */
	public Shipping getShipping() {
		return shipping;
	}

	/**
	 * 设置发货单
	 * 
	 * @param shipping
	 *            发货单
	 */
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

}