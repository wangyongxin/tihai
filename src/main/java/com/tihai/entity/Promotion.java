/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tihai.common.Setting;
import com.tihai.util.FreemarkerUtils;
import com.tihai.util.SettingUtils;

import freemarker.template.TemplateException;

/**
 * Entity - 促销
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class Promotion extends OrderEntity {

	private static final long serialVersionUID = 3536993535267962279L;

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/promotion/content";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 名称 */
	private String name;

	/** 标题 */
	private String title;

	/** 起始日期 */
	private Date beginDate;

	/** 结束日期 */
	private Date endDate;

	/** 最小商品数量 */
	private Integer minimumQuantity;

	/** 最大商品数量 */
	private Integer maximumQuantity;

	/** 最小商品价格 */
	private BigDecimal minimumPrice;

	/** 最大商品价格 */
	private BigDecimal maximumPrice;

	/** 价格运算表达式 */
	private String priceExpression;

	/** 积分运算表达式 */
	private String pointExpression;

	/** 是否免运费 */
	private Boolean isFreeShipping;

	/** 是否允许使用优惠券 */
	private Boolean isCouponAllowed;

	/** 介绍 */
	private String introduction;

	/** 允许参加会员等级 */
	private Set<MemberRank> memberRanks = new HashSet<MemberRank>();

	/** 允许参与商品分类 */
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();

	/** 允许参与商品 */
	private Set<Product> products = new HashSet<Product>();

	/** 允许参与品牌 */
	private Set<Brand> brands = new HashSet<Brand>();

	/** 赠送优惠券 */
	private Set<Coupon> coupons = new HashSet<Coupon>();

	/** 赠品 */
	private List<GiftItem> giftItems = new ArrayList<GiftItem>();

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
	 * 获取标题
	 * 
	 * @return 标题
	 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取起始日期
	 * 
	 * @return 起始日期
	 */
	@JsonProperty
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 设置起始日期
	 * 
	 * @param beginDate
	 *            起始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 获取结束日期
	 * 
	 * @return 结束日期
	 */
	@JsonProperty
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置结束日期
	 * 
	 * @param endDate
	 *            结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取最小商品数量
	 * 
	 * @return 最小商品数量
	 */
	@JsonProperty
	@Min(0)
	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	/**
	 * 设置最小商品数量
	 * 
	 * @param minimumQuantity
	 *            最小商品数量
	 */
	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	/**
	 * 获取最大商品数量
	 * 
	 * @return 最大商品数量
	 */
	@JsonProperty
	@Min(0)
	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	/**
	 * 设置最大商品数量
	 * 
	 * @param maximumQuantity
	 *            最大商品数量
	 */
	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}

	/**
	 * 获取最小商品价格
	 * 
	 * @return 最小商品价格
	 */
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	/**
	 * 设置最小商品价格
	 * 
	 * @param minimumPrice
	 *            最小商品价格
	 */
	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	/**
	 * 获取最大商品价格
	 * 
	 * @return 最大商品价格
	 */
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	public BigDecimal getMaximumPrice() {
		return maximumPrice;
	}

	/**
	 * 设置最大商品价格
	 * 
	 * @param maximumPrice
	 *            最大商品价格
	 */
	public void setMaximumPrice(BigDecimal maximumPrice) {
		this.maximumPrice = maximumPrice;
	}

	/**
	 * 获取价格运算表达式
	 * 
	 * @return 价格运算表达式
	 */
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * 设置价格运算表达式
	 * 
	 * @param priceExpression
	 *            价格运算表达式
	 */
	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}

	/**
	 * 获取积分运算表达式
	 * 
	 * @return 积分运算表达式
	 */
	public String getPointExpression() {
		return pointExpression;
	}

	/**
	 * 设置积分运算表达式
	 * 
	 * @param pointExpression
	 *            积分运算表达式
	 */
	public void setPointExpression(String pointExpression) {
		this.pointExpression = pointExpression;
	}

	/**
	 * 获取是否免运费
	 * 
	 * @return 是否免运费
	 */
	@NotNull
	public Boolean getIsFreeShipping() {
		return isFreeShipping;
	}

	/**
	 * 设置是否免运费
	 * 
	 * @param isFreeShipping
	 *            是否免运费
	 */
	public void setIsFreeShipping(Boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}

	/**
	 * 获取是否允许使用优惠券
	 * 
	 * @return 是否允许使用优惠券
	 */
	@JsonProperty
	@NotNull
	public Boolean getIsCouponAllowed() {
		return isCouponAllowed;
	}

	/**
	 * 设置是否允许使用优惠券
	 * 
	 * @param isCouponAllowed
	 *            是否允许使用优惠券
	 */
	public void setIsCouponAllowed(Boolean isCouponAllowed) {
		this.isCouponAllowed = isCouponAllowed;
	}

	/**
	 * 获取介绍
	 * 
	 * @return 介绍
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置介绍
	 * 
	 * @param introduction
	 *            介绍
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 获取允许参加会员等级
	 * 
	 * @return 允许参加会员等级
	 */
	public Set<MemberRank> getMemberRanks() {
		return memberRanks;
	}

	/**
	 * 设置允许参加会员等级
	 * 
	 * @param memberRanks
	 *            允许参加会员等级
	 */
	public void setMemberRanks(Set<MemberRank> memberRanks) {
		this.memberRanks = memberRanks;
	}

	/**
	 * 获取允许参与商品分类
	 * 
	 * @return 允许参与商品分类
	 */
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * 设置允许参与商品分类
	 * 
	 * @param productCategories
	 *            允许参与商品分类
	 */
	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	/**
	 * 获取允许参与商品
	 * 
	 * @return 允许参与商品
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置允许参与商品
	 * 
	 * @param products
	 *            允许参与商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	/**
	 * 获取允许参与品牌
	 * 
	 * @return 允许参与品牌
	 */
	public Set<Brand> getBrands() {
		return brands;
	}

	/**
	 * 设置允许参与品牌
	 * 
	 * @param brands
	 *            允许参与品牌
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	/**
	 * 获取赠送优惠券
	 * 
	 * @return 赠送优惠券
	 */
	public Set<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * 设置赠送优惠券
	 * 
	 * @param coupons
	 *            赠送优惠券
	 */
	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * 获取赠品
	 * 
	 * @return 赠品
	 */
	@Valid
	public List<GiftItem> getGiftItems() {
		return giftItems;
	}

	/**
	 * 设置赠品
	 * 
	 * @param giftItems
	 *            赠品
	 */
	public void setGiftItems(List<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	/**
	 * 判断是否已开始
	 * 
	 * @return 是否已开始
	 */
	//@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	/**
	 * 判断是否已结束
	 * 
	 * @return 是否已结束
	 */
	//@Transient
	public boolean hasEnded() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	/**
	 * 获取访问路径
	 * 
	 * @return 访问路径
	 */
	//@Transient
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
	}

	/**
	 * 计算促销价格
	 * 
	 * @param quantity
	 *            商品数量
	 * @param price
	 *            商品价格
	 * @return 促销价格
	 */
	//@Transient
	public BigDecimal calculatePrice(Integer quantity, BigDecimal price) {
		if (price == null || StringUtils.isEmpty(getPriceExpression())) {
			return price;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("quantity", quantity);
		model.put("price", price);
		Setting setting = SettingUtils.get();
		try {
			return setting.setScale(new BigDecimal(FreemarkerUtils.process("${(" + getPriceExpression() + ")?string(\"0.######\")}", model)));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return price.compareTo(new BigDecimal(0)) > 0 ? price : new BigDecimal(0);
	}

	/**
	 * 计算促销赠送积分
	 * 
	 * @param quantity
	 *            商品数量
	 * @param point
	 *            赠送积分
	 * @return 促销赠送积分
	 */
	//@Transient
	public Long calculatePoint(Integer quantity, Long point) {
		if (point == null || StringUtils.isEmpty(getPointExpression())) {
			return point;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("quantity", quantity);
		model.put("point", point);
		try {
			return Long.valueOf(FreemarkerUtils.process("${(" + getPointExpression() + ")?string(\"0.######\")}", model));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return point > 0L ? point : 0L;
	}

}