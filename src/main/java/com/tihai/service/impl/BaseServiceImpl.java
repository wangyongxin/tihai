/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.service.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.tihai.common.Filter.Operator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tihai.common.Filter;
import com.tihai.common.Page;
import com.tihai.common.Pageable;
import com.tihai.dao.BaseDao;
import com.tihai.entity.BaseEntity;
import com.tihai.service.BaseService;

/**
 * Service - 基类
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

	/** 更新忽略属性 */
	private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.ID_PROPERTY_NAME, BaseEntity.CREATE_DATE_PROPERTY_NAME, BaseEntity.MODIFY_DATE_PROPERTY_NAME };

	/** baseDao */
	private BaseDao<T, ID> baseDao;

	public void setBaseDao(BaseDao<T, ID> baseDao) {
		this.baseDao = baseDao;
	}

	@Transactional(readOnly = true)
	public T find(ID id) {
		return baseDao.find(id);
	}

	@Transactional(readOnly = true)
	public List<T> findList(ID... ids) {
		List<T> result = new ArrayList<T>();
		if (ids != null) {
			for (ID id : ids) {
				T entity = find(id);
				if (entity != null) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	@Transactional(readOnly = true)
	public List<T> findAll() {
		return baseDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<T> findList(Integer count) {
		return findList(null, count);
	}

	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count) {
		return baseDao.findList(first, count);
	}

	@Transactional(readOnly = true)
	public Page<T> findPage(Pageable pageable) {
		if (pageable == null) {
			pageable = new Pageable();
		}
//		addRestrictions(criteriaQuery, pageable);
//		addOrders(criteriaQuery, pageable);
//		if (criteriaQuery.getOrderList().isEmpty()) {
//			if (OrderEntity.class.isAssignableFrom(entityClass)) {
//				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(OrderEntity.ORDER_PROPERTY_NAME)));
//			} else {
//				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(OrderEntity.CREATE_DATE_PROPERTY_NAME)));
//			}
//		}
//		long total = count(criteriaQuery, null);
//		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
//		if (totalPages < pageable.getPageNumber()) {
//			pageable.setPageNumber(totalPages);
//		}
//		TypedQuery<T> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
//		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
//		query.setMaxResults(pageable.getPageSize());
//		return new Page<T>(query.getResultList(), total, pageable);
		
		
		long total = baseDao.count(getConditions(pageable.getFilters()));
		List<T> list = baseDao.findPage(pageable);
		Page<T> page = new Page<T>(list, total, pageable);
		return page;
	}

//	private void addRestrictions(CriteriaQuery<T> criteriaQuery, Pageable pageable) {
//		if (criteriaQuery == null || pageable == null) {
//			return;
//		}
//		Root<T> root = getRoot(criteriaQuery);
//		if (root == null) {
//			return;
//		}
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		Predicate restrictions = criteriaQuery.getRestriction() != null ? criteriaQuery.getRestriction() : criteriaBuilder.conjunction();
//		if (StringUtils.isNotEmpty(pageable.getSearchProperty()) && StringUtils.isNotEmpty(pageable.getSearchValue())) {
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String> get(pageable.getSearchProperty()), "%" + pageable.getSearchValue() + "%"));
//		}
//		if (pageable.getFilters() != null) {
//			for (Filter filter : pageable.getFilters()) {
//				if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
//					continue;
//				}
//				if (filter.getOperator() == Operator.eq && filter.getValue() != null) {
//					if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
//						restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(criteriaBuilder.lower(root.<String> get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
//					} else {
//						restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
//					}
//				} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {
//					if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
//						restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower(root.<String> get(filter.getProperty())), ((String) filter.getValue()).toLowerCase()));
//					} else {
//						restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
//					}
//				} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {
//					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(root.<Number> get(filter.getProperty()), (Number) filter.getValue()));
//				} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {
//					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt(root.<Number> get(filter.getProperty()), (Number) filter.getValue()));
//				} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {
//					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get(filter.getProperty()), (Number) filter.getValue()));
//				} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {
//					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get(filter.getProperty()), (Number) filter.getValue()));
//				} else if (filter.getOperator() == Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
//					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String> get(filter.getProperty()), (String) filter.getValue()));
//				} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {
//					restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).in(filter.getValue()));
//				} else if (filter.getOperator() == Operator.isNull) {
//					restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).isNull());
//				} else if (filter.getOperator() == Operator.isNotNull) {
//					restrictions = criteriaBuilder.and(restrictions, root.get(filter.getProperty()).isNotNull());
//				}
//			}
//		}
//		criteriaQuery.where(restrictions);
//	}
//
//	private void addOrders(CriteriaQuery<T> criteriaQuery, List<Order> orders) {
//		if (criteriaQuery == null || orders == null || orders.isEmpty()) {
//			return;
//		}
//		Root<T> root = getRoot(criteriaQuery);
//		if (root == null) {
//			return;
//		}
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
//		if (!criteriaQuery.getOrderList().isEmpty()) {
//			orderList.addAll(criteriaQuery.getOrderList());
//		}
//		for (Order order : orders) {
//			if (order.getDirection() == Direction.asc) {
//				orderList.add(criteriaBuilder.asc(root.get(order.getProperty())));
//			} else if (order.getDirection() == Direction.desc) {
//				orderList.add(criteriaBuilder.desc(root.get(order.getProperty())));
//			}
//		}
//		criteriaQuery.orderBy(orderList);
//	}

	@Transactional(readOnly = true)
	public long count() {
		return count(new Filter[] {});
	}

	@Transactional(readOnly = true)
	public long count(Filter... filters) {
		return baseDao.count(getConditions(filters));
	}

	private String getConditions(Filter... filters) {
		StringBuffer conditions = new StringBuffer();
		for(Filter filter : filters){
			addFilter(conditions, filter);
		}
		return conditions.toString();
	}

	private String getConditions(List<Filter> filters) {
		StringBuffer conditions = new StringBuffer();
		for(Filter filter : filters){
			addFilter(conditions, filter);
		}
		return conditions.toString();
	}
	
	private void addFilter(StringBuffer conditions, Filter filter) {
		if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
			return;
		}
		if (filter.getOperator() == Operator.eq && filter.getValue() != null) {
			if (filter.getIgnoreCase() != null && filter.getIgnoreCase()) {
				if(filter.getValue() instanceof Number){
					conditions.append(filter.getProperty() + " = " + filter.getValue());
				}else if(filter.getValue() instanceof String){
					conditions.append(filter.getProperty() + " = '" + StringUtils.lowerCase((String)filter.getValue()) + "'");
				}
			} else {
				if(filter.getValue() instanceof Number){
					conditions.append(filter.getProperty() + " = " + filter.getValue());
				}else if(filter.getValue() instanceof String){
					conditions.append(filter.getProperty() + " = '" + filter.getValue() + "'");
				}
			}
		} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {
			if (filter.getIgnoreCase() != null && filter.getIgnoreCase()) {
				if(filter.getValue() instanceof Number){
					conditions.append(filter.getProperty() + " != " + filter.getValue());
				}else if(filter.getValue() instanceof String){
					conditions.append(filter.getProperty() + " != '" + StringUtils.lowerCase((String)filter.getValue()) + "'");
				}
			} else {
				if(filter.getValue() instanceof Number){
					conditions.append(filter.getProperty() + " != " + filter.getValue());
				}else if(filter.getValue() instanceof String){
					conditions.append(filter.getProperty() + " != '" + (String)filter.getValue() + "'");
				}
			}
		} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {
			conditions.append(filter.getProperty() + " > " + filter.getValue());
		} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {
			conditions.append(filter.getProperty() + " < " + filter.getValue());
		} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {
			conditions.append(filter.getProperty() + " >= " + filter.getValue());
		} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {
			conditions.append(filter.getProperty() + " <= " + filter.getValue());
		} else if (filter.getOperator() == Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
			conditions.append(filter.getProperty() + " like '%" + filter.getValue() + "%' ");
		} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {
			conditions.append(filter.getProperty() + " in (" + filter.getValue() + ") ");
		} else if (filter.getOperator() == Operator.isNull) {
			conditions.append(filter.getProperty() + " is null ");
		} else if (filter.getOperator() == Operator.isNotNull) {
			conditions.append(filter.getProperty() + " is not null ");
		}
	}

	@Transactional(readOnly = true)
	public boolean exists(ID id) {
		return baseDao.find(id) != null;
	}

	@Transactional(readOnly = true)
	public boolean exists(Filter... filters) {
		return baseDao.count(getConditions(filters)) > 0;
	}

	@Transactional
	public void save(T entity) {
		baseDao.save(entity);
	}

	@Transactional
	public T update(T entity) {
		return baseDao.update(entity);
	}

	@Transactional
	public T update(T entity, String... ignoreProperties) {
		Assert.notNull(entity);
		T persistant = baseDao.find(baseDao.getIdentifier(entity));
		if (persistant != null) {
			copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			return update(persistant);
		} else {
			return update(entity);
		}
	}

	@Transactional
	public void delete(ID id) {
		baseDao.deleteById(id);
	}

	@Transactional
	public void delete(ID... ids) {
		if (ids != null) {
			for (ID id : ids) {
				delete(id);
			}
		}
	}

	@Transactional
	public void delete(T entity) {
		delete(baseDao.getIdentifier(entity));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void copyProperties(Object source, Object target, String[] ignoreProperties) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null && (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object sourceValue = readMethod.invoke(source);
						Object targetValue = readMethod.invoke(target);
						if (sourceValue != null && targetValue != null && targetValue instanceof Collection) {
							Collection collection = (Collection) targetValue;
							collection.clear();
							collection.addAll((Collection) sourceValue);
						} else {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, sourceValue);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}

}