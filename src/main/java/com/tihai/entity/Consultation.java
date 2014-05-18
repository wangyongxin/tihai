/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 咨询
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class Consultation extends BaseEntity {

	private static final long serialVersionUID = -3950317769006303385L;

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/consultation/content";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 内容 */
	private String content;

	/** 是否显示 */
	private Boolean isShow;

	/** IP */
	private String ip;

	/** 会员 */
	private Member member;

	/** 商品 */
	private Product product;

	/** 咨询 */
	private Consultation forConsultation;

	/** 回复 */
	private Set<Consultation> replyConsultations = new HashSet<Consultation>();

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@NotEmpty
	@Length(max = 200)
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取是否显示
	 * 
	 * @return 是否显示
	 */
	public Boolean getIsShow() {
		return isShow;
	}

	/**
	 * 设置是否显示
	 * 
	 * @param isShow
	 *            是否显示
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	/**
	 * 获取IP
	 * 
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * 
	 * @param ip
	 *            IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
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

	/**
	 * 获取咨询
	 * 
	 * @return 咨询
	 */
	public Consultation getForConsultation() {
		return forConsultation;
	}

	/**
	 * 设置咨询
	 * 
	 * @param forConsultation
	 *            咨询
	 */
	public void setForConsultation(Consultation forConsultation) {
		this.forConsultation = forConsultation;
	}

	/**
	 * 获取回复
	 * 
	 * @return 回复
	 */
	public Set<Consultation> getReplyConsultations() {
		return replyConsultations;
	}

	/**
	 * 设置回复
	 * 
	 * @param replyConsultations
	 *            回复
	 */
	public void setReplyConsultations(Set<Consultation> replyConsultations) {
		this.replyConsultations = replyConsultations;
	}

	/**
	 * 获取访问路径
	 * 
	 * @return 访问路径
	 */
//	@Transient
	public String getPath() {
		if (getProduct() != null && getProduct().getId() != null) {
			return PATH_PREFIX + "/" + getProduct().getId() + PATH_SUFFIX;
		}
		return null;
	}

}