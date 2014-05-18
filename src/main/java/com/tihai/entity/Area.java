/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 地区
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
public class Area extends OrderEntity {

	private static final long serialVersionUID = -2158109459123036967L;

	/** 树路径分隔符 */
	private static final String TREE_PATH_SEPARATOR = ",";

	/** 名称 */
	private String name;

	/** 全称 */
	private String fullName;

	/** 树路径 */
	private String treePath;

	/** 上级地区 */
	private Area parent;

	/** 下级地区 */
	private List<Area> children = new ArrayList<Area>();

	/** 会员 */
	private List<Member> members = new ArrayList<Member>();

	/** 收货地址 */
	private List<Receiver> receivers = new ArrayList<Receiver>();

	/** 订单 */
	private List<Order> orders = new ArrayList<Order>();

	/** 发货点 */
	private List<DeliveryCenter> deliveryCenters = new ArrayList<DeliveryCenter>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	@NotEmpty
	@Length(max = 100)
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
	 * 获取全称
	 * 
	 * @return 全称
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置全称
	 * 
	 * @param fullName
	 *            全称
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * 
	 * @param treePath
	 *            树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取上级地区
	 * 
	 * @return 上级地区
	 */
	public Area getParent() {
		return parent;
	}

	/**
	 * 设置上级地区
	 * 
	 * @param parent
	 *            上级地区
	 */
	public void setParent(Area parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级地区
	 * 
	 * @return 下级地区
	 */
	public List<Area> getChildren() {
		return children;
	}

	/**
	 * 设置下级地区
	 * 
	 * @param children
	 *            下级地区
	 */
	public void setChildren(List<Area> children) {
		this.children = children;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public List<Member> getMembers() {
		return members;
	}

	/**
	 * 设置会员
	 * 
	 * @param members
	 *            会员
	 */
	public void setMembers(List<Member> members) {
		this.members = members;
	}

	/**
	 * 获取收货地址
	 * 
	 * @return 收货地址
	 */
	public List<Receiver> getReceivers() {
		return receivers;
	}

	/**
	 * 设置收货地址
	 * 
	 * @param receivers
	 *            收货地址
	 */
	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取发货点
	 * 
	 * @return 发货点
	 */
	public List<DeliveryCenter> getDeliveryCenters() {
		return deliveryCenters;
	}

	/**
	 * 设置发货点
	 * 
	 * @param deliveryCenters
	 *            发货点
	 */
	public void setDeliveryCenters(List<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}

	/**
	 * 持久化前处理
	 */
	public void prePersist() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}

	/**
	 * 更新前处理
	 */
	public void preUpdate() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}

	/**
	 * 删除前处理
	 */
	public void preRemove() {
		List<Member> members = getMembers();
		if (members != null) {
			for (Member member : members) {
				member.setArea(null);
			}
		}
		List<Receiver> receivers = getReceivers();
		if (receivers != null) {
			for (Receiver receiver : receivers) {
				receiver.setArea(null);
			}
		}
		List<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.setArea(null);
			}
		}
		List<DeliveryCenter> deliveryCenters = getDeliveryCenters();
		if (deliveryCenters != null) {
			for (DeliveryCenter deliveryCenter : deliveryCenters) {
				deliveryCenter.setArea(null);
			}
		}
	}

	/**
	 * 重写toString方法
	 * 
	 * @return 全称
	 */
	@Override
	public String toString() {
		return getFullName();
	}

}