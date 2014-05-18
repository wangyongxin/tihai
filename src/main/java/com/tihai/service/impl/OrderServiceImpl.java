/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.tihai.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.tihai.common.Filter;
import com.tihai.common.Page;
import com.tihai.common.Pageable;
import com.tihai.common.Setting;
import com.tihai.common.Setting.StockAllocationTime;
import com.tihai.dao.CartDao;
import com.tihai.dao.CouponCodeDao;
import com.tihai.dao.DepositDao;
import com.tihai.dao.MemberDao;
import com.tihai.dao.MemberRankDao;
import com.tihai.dao.OrderDao;
import com.tihai.dao.OrderItemDao;
import com.tihai.dao.OrderLogDao;
import com.tihai.dao.PaymentDao;
import com.tihai.dao.ProductDao;
import com.tihai.dao.RefundsDao;
import com.tihai.dao.ReturnsDao;
import com.tihai.dao.ShippingDao;
import com.tihai.dao.SnDao;
import com.tihai.entity.Admin;
import com.tihai.entity.Cart;
import com.tihai.entity.CartItem;
import com.tihai.entity.Coupon;
import com.tihai.entity.CouponCode;
import com.tihai.entity.Deposit;
import com.tihai.entity.GiftItem;
import com.tihai.entity.Member;
import com.tihai.entity.MemberRank;
import com.tihai.entity.Order;
import com.tihai.entity.Order.OrderStatus;
import com.tihai.entity.Order.PaymentStatus;
import com.tihai.entity.Order.ShippingStatus;
import com.tihai.entity.OrderItem;
import com.tihai.entity.OrderLog;
import com.tihai.entity.OrderLog.Type;
import com.tihai.entity.Payment;
import com.tihai.entity.PaymentMethod;
import com.tihai.entity.Product;
import com.tihai.entity.Promotion;
import com.tihai.entity.Receiver;
import com.tihai.entity.Refunds;
import com.tihai.entity.Returns;
import com.tihai.entity.ReturnsItem;
import com.tihai.entity.Shipping;
import com.tihai.entity.ShippingItem;
import com.tihai.entity.ShippingMethod;
import com.tihai.entity.Sn;
import com.tihai.service.OrderService;
import com.tihai.service.StaticService;
import com.tihai.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 订单
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

	@Resource(name = "orderDao")
	private OrderDao orderDao;
	@Resource(name = "orderItemDao")
	private OrderItemDao orderItemDao;
	@Resource(name = "orderLogDao")
	private OrderLogDao orderLogDao;
	@Resource(name = "cartDao")
	private CartDao cartDao;
	@Resource(name = "couponCodeDao")
	private CouponCodeDao couponCodeDao;
	@Resource(name = "snDao")
	private SnDao snDao;
	@Resource(name = "memberDao")
	private MemberDao memberDao;
	@Resource(name = "memberRankDao")
	private MemberRankDao memberRankDao;
	@Resource(name = "productDao")
	private ProductDao productDao;
	@Resource(name = "depositDao")
	private DepositDao depositDao;
	@Resource(name = "paymentDao")
	private PaymentDao paymentDao;
	@Resource(name = "refundsDao")
	private RefundsDao refundsDao;
	@Resource(name = "shippingDao")
	private ShippingDao shippingDao;
	@Resource(name = "returnsDao")
	private ReturnsDao returnsDao;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "orderDao")
	public void setBaseDao(OrderDao orderDao) {
		super.setBaseDao(orderDao);
	}

	@Transactional(readOnly = true)
	public Order findBySn(String sn) {
		return orderDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Order> findList(Member member, Integer count, List<Filter> filters, List<com.tihai.common.Order> orders) {
		return orderDao.findList(member, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(Member member, Pageable pageable) {
		return orderDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return orderDao.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		return orderDao.count(orderStatus, paymentStatus, shippingStatus, hasExpired);
	}

	@Transactional(readOnly = true)
	public Long waitingPaymentCount(Member member) {
		return orderDao.waitingPaymentCount(member);
	}

	@Transactional(readOnly = true)
	public Long waitingShippingCount(Member member) {
		return orderDao.waitingShippingCount(member);
	}

	@Transactional(readOnly = true)
	public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
		return orderDao.getSalesAmount(beginDate, endDate);
	}

	@Transactional(readOnly = true)
	public Integer getSalesVolume(Date beginDate, Date endDate) {
		return orderDao.getSalesVolume(beginDate, endDate);
	}

	public void releaseStock() {
		orderDao.releaseStock();
	}

	@Transactional(readOnly = true)
	public Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo) {
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());

		Order order = new Order();
		order.setShippingStatus(ShippingStatus.unshipped);
		order.setFee(new BigDecimal(0));
		order.setPromotionDiscount(cart.getDiscount());
		order.setCouponDiscount(new BigDecimal(0));
		order.setOffsetAmount(new BigDecimal(0));
		order.setPoint(cart.getEffectivePoint());
		order.setMemo(memo);
		order.setMember(cart.getMember());

		if (receiver != null) {
			order.setConsignee(receiver.getConsignee());
			order.setAreaName(receiver.getAreaName());
			order.setAddress(receiver.getAddress());
			order.setZipCode(receiver.getZipCode());
			order.setPhone(receiver.getPhone());
			order.setArea(receiver.getArea());
		}

		if (!cart.getPromotions().isEmpty()) {
			StringBuffer promotionName = new StringBuffer();
			for (Promotion promotion : cart.getPromotions()) {
				if (promotion != null && promotion.getName() != null) {
					promotionName.append(" " + promotion.getName());
				}
			}
			if (promotionName.length() > 0) {
				promotionName.deleteCharAt(0);
			}
			order.setPromotion(promotionName.toString());
		}

		order.setPaymentMethod(paymentMethod);

		if (shippingMethod != null && paymentMethod != null && paymentMethod.getShippingMethods().contains(shippingMethod)) {
			BigDecimal freight = shippingMethod.calculateFreight(cart.getWeight());
			for (Promotion promotion : cart.getPromotions()) {
				if (promotion.getIsFreeShipping()) {
					freight = new BigDecimal(0);
					break;
				}
			}
			order.setFreight(freight);
			order.setShippingMethod(shippingMethod);
		} else {
			order.setFreight(new BigDecimal(0));
		}

		if (couponCode != null && cart.isCouponAllowed()) {
//			couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_READ);
			if (!couponCode.getIsUsed() && couponCode.getCoupon() != null && cart.isValid(couponCode.getCoupon())) {
				BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getQuantity(), cart.getEffectivePrice()));
				couponDiscount = couponDiscount.compareTo(new BigDecimal(0)) > 0 ? couponDiscount : new BigDecimal(0);
				order.setCouponDiscount(couponDiscount);
				order.setCouponCode(couponCode);
			}
		}

		List<OrderItem> orderItems = order.getOrderItems();
		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem != null && cartItem.getProduct() != null) {
				Product product = cartItem.getProduct();
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(product.getSn());
				orderItem.setName(product.getName());
				orderItem.setFullName(product.getFullName());
				orderItem.setPrice(cartItem.getUnitPrice());
				orderItem.setWeight(product.getWeight());
				orderItem.setThumbnail(product.getThumbnail());
				orderItem.setIsGift(false);
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(product);
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
		}

		for (GiftItem giftItem : cart.getGiftItems()) {
			if (giftItem != null && giftItem.getGift() != null) {
				Product gift = giftItem.getGift();
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(gift.getSn());
				orderItem.setName(gift.getName());
				orderItem.setFullName(gift.getFullName());
				orderItem.setPrice(new BigDecimal(0));
				orderItem.setWeight(gift.getWeight());
				orderItem.setThumbnail(gift.getThumbnail());
				orderItem.setIsGift(true);
				orderItem.setQuantity(giftItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(gift);
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
		}

		Setting setting = SettingUtils.get();
		if (setting.getIsInvoiceEnabled() && isInvoice && StringUtils.isNotEmpty(invoiceTitle)) {
			order.setIsInvoice(true);
			order.setInvoiceTitle(invoiceTitle);
			order.setTax(order.calculateTax());
		} else {
			order.setIsInvoice(false);
			order.setTax(new BigDecimal(0));
		}

		if (useBalance) {
			Member member = cart.getMember();
			if (member.getBalance().compareTo(order.getAmount()) >= 0) {
				order.setAmountPaid(order.getAmount());
			} else {
				order.setAmountPaid(member.getBalance());
			}
		} else {
			order.setAmountPaid(new BigDecimal(0));
		}

		if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.paid);
		} else if (order.getAmountPayable().compareTo(new BigDecimal(0)) > 0 && order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.partialPayment);
		} else {
			order.setOrderStatus(OrderStatus.unconfirmed);
			order.setPaymentStatus(PaymentStatus.unpaid);
		}

		if (paymentMethod != null && paymentMethod.getTimeout() != null && order.getPaymentStatus() == PaymentStatus.unpaid) {
			order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
		}

		return order;
	}

	public Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, boolean isInvoice, String invoiceTitle, boolean useBalance, String memo, Admin operator) {
		Assert.notNull(cart);
		Assert.notNull(cart.getMember());
		Assert.notEmpty(cart.getCartItems());
		Assert.notNull(receiver);
		Assert.notNull(paymentMethod);
		Assert.notNull(shippingMethod);

		Order order = build(cart, receiver, paymentMethod, shippingMethod, couponCode, isInvoice, invoiceTitle, useBalance, memo);

		order.setSn(snDao.generate(Sn.Type.order));
		if (paymentMethod.getType() == PaymentMethod.Type.online) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 10));
			order.setOperator(operator);
		}

		if (order.getCouponCode() != null) {
			couponCode.setIsUsed(true);
			couponCode.setUsedDate(new Date());
			couponCodeDao.update(couponCode);
		}

		for (Promotion promotion : cart.getPromotions()) {
			for (Coupon coupon : promotion.getCoupons()) {
				order.getCoupons().add(coupon);
			}
		}

		Setting setting = SettingUtils.get();
		if (setting.getStockAllocationTime() == StockAllocationTime.order || (setting.getStockAllocationTime() == StockAllocationTime.payment && (order.getPaymentStatus() == PaymentStatus.partialPayment || order.getPaymentStatus() == PaymentStatus.paid))) {
			order.setIsAllocatedStock(true);
		} else {
			order.setIsAllocatedStock(false);
		}

		orderDao.save(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.create);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);

		Member member = cart.getMember();
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
//			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().subtract(order.getAmountPaid()));
			memberDao.update(member);

			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(order.getAmountPaid());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			depositDao.save(deposit);
		}

		if (setting.getStockAllocationTime() == StockAllocationTime.order || (setting.getStockAllocationTime() == StockAllocationTime.payment && (order.getPaymentStatus() == PaymentStatus.partialPayment || order.getPaymentStatus() == PaymentStatus.paid))) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
		}

		cartDao.delete(cart);
		return order;
	}

	public void update(Order order, Admin operator) {
		Assert.notNull(order);

		Order pOrder = orderDao.find(order.getId());

		if (pOrder.getIsAllocatedStock()) {
			for (OrderItem orderItem : pOrder.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						productDao.flush();
						staticService.build(product);
					}
				}
			}
		}

		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.modify);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void confirm(Order order, Admin operator) {
		Assert.notNull(order);

		order.setOrderStatus(OrderStatus.confirmed);
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.confirm);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void complete(Order order, Admin operator) {
		Assert.notNull(order);

		Member member = order.getMember();
//		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (order.getShippingStatus() == ShippingStatus.partialShipment || order.getShippingStatus() == ShippingStatus.shipped) {
			member.setPoint(member.getPoint() + order.getPoint());
			for (Coupon coupon : order.getCoupons()) {
				couponCodeDao.build(coupon, member);
			}
		}

		if (order.getShippingStatus() == ShippingStatus.unshipped || order.getShippingStatus() == ShippingStatus.returned) {
			CouponCode couponCode = order.getCouponCode();
			if (couponCode != null) {
				couponCode.setIsUsed(false);
				couponCode.setUsedDate(null);
				couponCodeDao.update(couponCode);

				order.setCouponCode(null);
				orderDao.update(order);
			}
		}

		member.setAmount(member.getAmount().add(order.getAmountPaid()));
		if (!member.getMemberRank().getIsSpecial()) {
			MemberRank memberRank = memberRankDao.findByAmount(member.getAmount());
			if (memberRank != null && memberRank.getAmount().compareTo(member.getMemberRank().getAmount()) > 0) {
				member.setMemberRank(memberRank);
			}
		}
		memberDao.update(member);

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem != null) {
				Product product = orderItem.getProduct();
//				productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if (product != null) {
					Integer quantity = orderItem.getQuantity();
					Calendar nowCalendar = Calendar.getInstance();
					Calendar weekSalesCalendar = DateUtils.toCalendar(product.getWeekSalesDate());
					Calendar monthSalesCalendar = DateUtils.toCalendar(product.getMonthSalesDate());
					if (nowCalendar.get(Calendar.YEAR) != weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
						product.setWeekSales((long) quantity);
					} else {
						product.setWeekSales(product.getWeekSales() + quantity);
					}
					if (nowCalendar.get(Calendar.YEAR) != monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
						product.setMonthSales((long) quantity);
					} else {
						product.setMonthSales(product.getMonthSales() + quantity);
					}
					product.setSales(product.getSales() + quantity);
					product.setWeekSalesDate(new Date());
					product.setMonthSalesDate(new Date());
					productDao.update(product);
//					orderDao.flush();
					staticService.build(product);
				}
			}
		}

		order.setOrderStatus(OrderStatus.completed);
		order.setExpire(null);
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.complete);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void cancel(Order order, Admin operator) {
		Assert.notNull(order);

		CouponCode couponCode = order.getCouponCode();
		if (couponCode != null) {
			couponCode.setIsUsed(false);
			couponCode.setUsedDate(null);
			couponCodeDao.update(couponCode);

			order.setCouponCode(null);
			orderDao.update(order);
		}

		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(false);
		}

		order.setOrderStatus(OrderStatus.cancelled);
		order.setExpire(null);
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.cancel);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void payment(Order order, Payment payment, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(payment);

//		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		payment.setOrder(order);
		paymentDao.update(payment);
		if (payment.getType() == Payment.Type.deposit) {
			Member member = order.getMember();
//			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().subtract(payment.getAmount()));
			memberDao.update(member);

			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(payment.getAmount());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			depositDao.save(deposit);
		}

		Setting setting = SettingUtils.get();
		if (!order.getIsAllocatedStock() && setting.getStockAllocationTime() == StockAllocationTime.payment) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(true);
		}

		order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
		order.setFee(payment.getFee());
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.paid);
		} else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PaymentStatus.partialPayment);
		}
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.payment);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void refunds(Order order, Refunds refunds, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(refunds);

//		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		refunds.setOrder(order);
		refundsDao.save(refunds);
		if (refunds.getType() == Refunds.Type.deposit) {
			Member member = order.getMember();
//			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().add(refunds.getAmount()));
			memberDao.update(member);

			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.adminRefunds);
			deposit.setCredit(refunds.getAmount());
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			deposit.setOrder(order);
			depositDao.save(deposit);
		}

		order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
		order.setExpire(null);
		if (order.getAmountPaid().compareTo(new BigDecimal(0)) == 0) {
			order.setPaymentStatus(PaymentStatus.refunded);
		} else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
			order.setPaymentStatus(PaymentStatus.partialRefunds);
		}
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.refunds);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void shipping(Order order, Shipping shipping, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(shipping);
		Assert.notEmpty(shipping.getShippingItems());

//		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		Setting setting = SettingUtils.get();
		if (!order.getIsAllocatedStock() && setting.getStockAllocationTime() == StockAllocationTime.ship) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
			order.setIsAllocatedStock(true);
		}

		shipping.setOrder(order);
		shippingDao.save(shipping);
		for (ShippingItem shippingItem : shipping.getShippingItems()) {
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
			if (orderItem != null) {
				Product product = orderItem.getProduct();
//				productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if (product != null) {
					if (product.getStock() != null) {
						product.setStock(product.getStock() - shippingItem.getQuantity());
						if (order.getIsAllocatedStock()) {
							product.setAllocatedStock(product.getAllocatedStock() - shippingItem.getQuantity());
						}
					}
					productDao.update(product);
//					orderDao.flush();
					staticService.build(product);
				}
//				orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
				orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
			}
		}
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setShippingStatus(ShippingStatus.shipped);
			order.setIsAllocatedStock(false);
		} else if (order.getShippedQuantity() > 0) {
			order.setShippingStatus(ShippingStatus.partialShipment);
		}
		order.setExpire(null);
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.shipping);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void returns(Order order, Returns returns, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(returns);
		Assert.notEmpty(returns.getReturnsItems());

//		orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

		returns.setOrder(order);
		returnsDao.save(returns);
		for (ReturnsItem returnsItem : returns.getReturnsItems()) {
			OrderItem orderItem = order.getOrderItem(returnsItem.getSn());
			if (orderItem != null) {
//				orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
				orderItem.setReturnQuantity(orderItem.getReturnQuantity() + returnsItem.getQuantity());
			}
		}
		if (order.getReturnQuantity() >= order.getShippedQuantity()) {
			order.setShippingStatus(ShippingStatus.returned);
		} else if (order.getReturnQuantity() > 0) {
			order.setShippingStatus(ShippingStatus.partialReturns);
		}
		order.setExpire(null);
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(Type.returns);
		orderLog.setOperator(operator != null ? operator.getUsername() : null);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	@Override
	public void delete(Order order) {
		if (order.getIsAllocatedStock()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (orderItem != null) {
					Product product = orderItem.getProduct();
//					productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if (product != null && product.getStock() != null) {
						product.setAllocatedStock(product.getAllocatedStock() - (orderItem.getQuantity() - orderItem.getShippedQuantity()));
						productDao.update(product);
//						orderDao.flush();
						staticService.build(product);
					}
				}
			}
		}
		super.delete(order);
	}

}