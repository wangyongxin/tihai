/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 货品
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class Goods extends BaseEntity {

	private static final long serialVersionUID = -6977025562650112419L;

	/** 商品 */
	private Set<Product> products = new HashSet<Product>();

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * 
	 * @param products
	 *            商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	/**
	 * 获取规格值
	 * 
	 * @return 规格值
	 */
	//@Transient
	public Set<SpecificationValue> getSpecificationValues() {
		Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();
		if (getProducts() != null) {
			for (Product product : getProducts()) {
				specificationValues.addAll(product.getSpecificationValues());
			}
		}
		return specificationValues;
	}

}