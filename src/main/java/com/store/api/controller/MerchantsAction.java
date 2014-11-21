package com.store.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Constant;
import com.store.api.mongo.entity.Order;
import com.store.api.mongo.entity.OrderOffer;
import com.store.api.mongo.entity.OrderProduct;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.service.OrderOfferService;
import com.store.api.mongo.service.OrderProductService;
import com.store.api.mongo.service.OrderService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.Utils;

/**
 * 商户APP接口控制器
 * 
 * Revision History
 *
 * @author vincent,2014年11月18日 created it
 */
@Controller()
@Scope("prototype")
@RequestMapping("/merchants")
public class MerchantsAction extends BaseAction {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderProductService orderProductService;

	@Autowired
	private OrderOfferService orderOfferService;

	/**
	 * 接单列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/receiveorderlist")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> receiveOrderList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Long startTime = cal.getTime().getTime();

		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = (User) obj;

		int lost = 0;
		Set<Long> ids = new HashSet<Long>();
		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		Map<String, String> orderMap = null;

		Page<OrderOffer> ofPage = orderOfferService.findByMerchantsIdAndCreateDateGreaterThan(user.getId(), startTime, page, size);
		if (ofPage.hasContent()) {
			for (OrderOffer of : ofPage.getContent()) {
				ids.add(of.getOrderId());
				if (of.getStatus() > 0)
					lost++;
			}
		}
		List<Order> orders = orderService.findAll(ids);

		if (orders.size() > 0) {
			for (Order order : orders) {
				orderMap = new HashMap<String, String>();
				orderMap.put("order_id", order.getId() + "");
				orderMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
				orderMap.put("to_address", order.getToAddress());
				orderMap.put("product_num", order.getTotalAmount() + "");
				orderMap.put("total_price", nfmt.format(order.getTotalPrice()));
				orderMap.put("desc", order.getProsDesc());
				orderMap.put("status", order.getStatus() > 0 ? "1" : "0");
				reList.add(orderMap);
			}
		}

		reMap.put("lost_num", lost + "");
		reMap.put("orders", reList);

		result.put("errorcode", "0");
		result.put("info", "");
		result.put("total_page", ofPage.getTotalPages() + "");
		result.put("data", reMap);
		return result;
	}

	/**
	 * 接单详细
	 * 
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/receiveorderdetail")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> receiveOrderDetail(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) {
		if (orderId > 0) {
			Map<String, Object> reMap = new HashMap<String, Object>();
			List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
			Map<String, String> opMap = null;
			Order order = orderService.findOne(orderId);
			if (null != order) {
				List<OrderProduct> ops = orderProductService.findByOrderId(orderId);
				if (ops.size() > 0) {
					for (OrderProduct op : ops) {
						opMap = new HashMap<String, String>();
						opMap.put("p_id", op.getProductId() + "");
						opMap.put("p_name", op.getProductName());
						opMap.put("p_price", nfmt.format(op.getProductPrice()));
						opMap.put("p_img", op.getProductImg());
						opMap.put("p_num", op.getAmount() + "");
						reList.add(opMap);
					}
				}
				reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
				reMap.put("to_address", order.getToAddress());
				reMap.put("phone", order.getCustomerPhone());
				reMap.put("nick_name", order.getCustomerName());
				reMap.put("total_price", nfmt.format(order.getTotalPrice()));
				reMap.put("products", reList);
			}
			result.put("errorcode", "0");
			result.put("info", "");
			result.put("data", reMap);
			return result;
		} else {
			result.put("errorcode", "2");
			result.put("info", "查询失败");
			return result;
		}
	}

	/**
	 * 商户抢单
	 * 
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/offer")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> offer(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) throws Exception {
		if (orderId > 0) {
			Object obj = session.getAttribute(Constant.SESSION_USER);
			User user = (User) obj;
			Order order = orderService.findOne(orderId);
			if (null != order) {
				if (order.getStatus() > 0) {
					if (user.getId().equals(order.getMerchantsId())) {
						result.put("errorcode", "2");
						result.put("info", "该订单已经被其它人抢了");
						return result;
					} else {
						result.put("errorcode", "3");
						result.put("info", "该订单已经被您抢到了");
						return result;
					}
				} else {
					order.setMerchantsId(user.getId());
					order.setMerchantsName(user.getNickName());
					order.setMerchantsPhone(user.getPhone());
					order.setFromAddress(user.getAddress());
					order.setFromLocation(user.getLocation());
					order.setOfferDate(System.currentTimeMillis());
					order.setStatus(2);

					List<OrderOffer> ofs = orderOfferService.findByOrderId(orderId);
					if (ofs.size() > 0) {
						for (OrderOffer of : ofs) {
							if (of.getMerchantsId().equals(user.getId())) {
								of.setStatus(1);
							} else
								of.setStatus(2);
						}
					}
					orderService.save(order);
					orderOfferService.save(ofs);

					result.put("errorcode", "0");
					result.put("info", "");
					return result;
				}
			}
			return result;
		} else {
			result.put("errorcode", "3");
			result.put("info", "抢单失败");
			return result;
		}
	}

	/**
	 * 订单列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/orderlist")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> orderList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size) {
		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = (User) obj;
		Map<String, String> reMap = null;
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		Page<Order> orderPage = orderService.findByMerchantsId(user.getId(), page, size);

		if (orderPage.hasContent()) {
			for (Order order : orderPage.getContent()) {
				reMap = new HashMap<String, String>();
				reMap.put("order_id", order.getId() + "");
				reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
				reMap.put("to_address", order.getToAddress());
				reMap.put("status", order.getStatus() + "");
				reMap.put("product_num", order.getTotalAmount() + "");
				reMap.put("total_price", nfmt.format(order.getTotalPrice()));
				reMap.put("desc", order.getProsDesc());
				reList.add(reMap);
			}
		}

		result.put("errorcode", "0");
		result.put("total_page", orderPage.getTotalPages() + "");
		result.put("info", "");
		result.put("data", reList);
		return result;
	}

	/**
	 * 查询订单详细
	 * 
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/orderdetail")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> orderDetail(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) {

		if (orderId > 0) {
			Map<String, Object> reMap = new HashMap<String, Object>();
			List<Map<String, String>> reList = new ArrayList<Map<String, String>>();

			List<OrderProduct> ops = orderProductService.findByOrderId(orderId);
			if (ops.size() > 0) {
				for (OrderProduct op : ops) {
					Map<String, String> opMap = new HashMap<String, String>();
					opMap.put("p_id", op.getProductId() + "");
					opMap.put("p_name", op.getProductName());
					opMap.put("p_price", nfmt.format(op.getProductPrice()));
					opMap.put("p_img", op.getProductImg());
					opMap.put("p_num", op.getAmount() + "");
					reList.add(opMap);
				}
			}
			Order order = orderService.findOne(orderId);
			reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
			reMap.put("to_address", order.getToAddress());
			reMap.put("phone", order.getCustomerPhone());
			reMap.put("status", order.getStatus() + "");
			reMap.put("product_num", order.getTotalAmount() + "");
			reMap.put("total_price", nfmt.format(order.getTotalPrice()));
			reMap.put("products", reList);

			result.put("errorcode", "0");
			result.put("info", "");
			result.put("data", reMap);
			return result;
		} else {
			result.put("errorcode", "2");
			result.put("info", "查询失败");
			return result;
		}
	}

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancleorder")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> cancleOrder(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) {
		if (orderId > 0) {
			Order order = orderService.findOne(orderId);
			if(null!=order){
				order.setCancelDate(System.currentTimeMillis());
				order.setStatus(10);
				
				orderService.save(order);
				
				result.put("errorcode", "0");
				result.put("info", "");
				return result;
			}else{
				result.put("errorcode", "2");
				result.put("info", "取消订单失败");
				return result;
			}
		} else {
			result.put("errorcode", "3");
			result.put("info", "取消订单失败");
			return result;
		}
	}
	
	/**
	 * 确认送达
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/arrive")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> arriveOrder(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) {
		if (orderId > 0) {
			Order order = orderService.findOne(orderId);
			if(null!=order){
				order.setArrivedDate(System.currentTimeMillis());
				order.setStatus(4);
				
				orderService.save(order);
				
				result.put("errorcode", "0");
				result.put("info", "");
				return result;
			}else{
				result.put("errorcode", "2");
				result.put("info", "确认失败");
				return result;
			}
		} else {
			result.put("errorcode", "3");
			result.put("info", "确认失败");
			return result;
		}
	}
}
