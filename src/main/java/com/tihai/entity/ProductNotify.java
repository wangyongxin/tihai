/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 到货通知
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class ProductNotify extends BaseEntity {

	private static final long serialVersionUID = 3192904068727393421L;

	/** E-mail */
	private String email;

	/** 是否已发送 */
	private Boolean hasSent;

	/** 会员 */
	private Member member;

	/** 商品 */
	private Product product;

	/**
	 * 获取E-mail
	 * 
	 * @return E-mail
	 */
	@NotEmpty
	@Email
	@Length(max = 200)
	public String getEmail() {
		return email;
	}

	/**
	 * 设置E-mail
	 * 
	 * @param email
	 *            E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取是否已发送
	 * 
	 * @return 是否已发送
	 */
	public Boolean getHasSent() {
		return hasSent;
	}

	/**
	 * 设置是否已发送
	 * 
	 * @param hasSent
	 *            是否已发送
	 */
	public void setHasSent(Boolean hasSent) {
		this.hasSent = hasSent;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * 
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

}