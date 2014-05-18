/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 标签
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class Tag extends OrderEntity {

	private static final long serialVersionUID = -2735037966597250149L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 文章标签 */
		article,

		/** 商品标签 */
		product
	};

	/** 名称 */
	private String name;

	/** 类型 */
	private Type type;

	/** 图标 */
	private String icon;

	/** 备注 */
	private String memo;

	/** 文章 */
	private Set<Article> articles = new HashSet<Article>();

	/** 商品 */
	private Set<Product> products = new HashSet<Product>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@NotNull(groups = Save.class)
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取图标
	 * 
	 * @return 图标
	 */
	@Length(max = 200)
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置图标
	 * 
	 * @param icon
	 *            图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	@Length(max = 200)
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取文章
	 * 
	 * @return 文章
	 */
	public Set<Article> getArticles() {
		return articles;
	}

	/**
	 * 设置文章
	 * 
	 * @param articles
	 *            文章
	 */
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

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
	 * 删除前处理
	 */
	public void preRemove() {
		Set<Article> articles = getArticles();
		if (articles != null) {
			for (Article article : articles) {
				article.getTags().remove(this);
			}
		}
		Set<Product> products = getProducts();
		if (products != null) {
			for (Product product : products) {
				product.getTags().remove(this);
			}
		}
	}

}