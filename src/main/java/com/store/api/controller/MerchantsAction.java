package com.store.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Constant;
import com.store.api.mongo.entity.Order;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.entity.subdocument.Offer;
import com.store.api.mongo.entity.subdocument.OrderProduct;
import com.store.api.mongo.service.OrderService;
import com.store.api.mongo.service.PushService;
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
    private PushService pushService;

    /**
     * 接单列表头部,取>订单ID的记录
     * 
     * @param page
     * @param size
     * @return
     */
    @ResponseBody
    @RequestMapping("/receiveorderlisttop")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> receiveOrderListTop(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Long startTime = cal.getTime().getTime();
        int page = 1;
        int size = 50;

        Object obj = session.getAttribute(Constant.SESSION_USER);
        User user = (User) obj;

        int lost = orderService.findTadayLostByUserId(user.getId(), startTime);
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        List<Map<String, String>> opList = null;
        Map<String, Object> orderMap = null;
        Map<String, String> opMap = null;

        Page<Order> orderPage = orderService.findTopOrderWithMercPush(user.getId(), orderId, page, size);
        if (orderPage.hasContent()) {
            for (Order order : orderPage.getContent()) {
                orderMap = new HashMap<String, Object>();
                orderMap.put("order_id", order.getId() + "");
                orderMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
                orderMap.put("to_address", order.getToAddress());
                orderMap.put("product_num", order.getTotalAmount() + "");
                orderMap.put("total_price", order.getTotalPrice() + "");
                orderMap.put("desc", order.getProsDesc());
                if(order.getStatus() > 0){
                	if(order.getMerchantsId()==user.getId())
                		orderMap.put("status", "1");
                	else
                		orderMap.put("status", "2");
                }else
                	orderMap.put("status", "0");
                orderMap.put("phone", order.getCustomerPhone());
                orderMap.put("nick_name", order.getCustomerName());
                
                List<OrderProduct> ops = order.getProducts();
                opList=new ArrayList<Map<String, String>>();
                for (OrderProduct op : ops) {
                    opMap = new HashMap<String, String>();
                    opMap.put("p_id", op.getProductId() + "");
                    opMap.put("p_name", op.getProductName());
                    opMap.put("p_price", op.getProductPrice() + "");
                    opMap.put("p_img", op.getProductImg());
                    opMap.put("p_num", op.getAmount() + "");
                    opList.add(opMap);
                }
                orderMap.put("products", opList);
                reList.add(orderMap);
            }
        }

        reMap.put("lost_num", lost + "");
        reMap.put("orders", reList);

        result.put("errorcode", "1");
        result.put("info", "");
        result.put("data", reMap);
        return result;
    }

    /**
     * 接单列表尾部,取<订单ID的记录
     * 
     * @param page
     * @param size
     * @return
     */
    @ResponseBody
    @RequestMapping("/receiveorderlisttail")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> receiveOrderListTail(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {

        int page = 1;
        int size = 10;

        if (orderId <= 0) {
            result.put("errorcode", "-2");
            result.put("info", "查询失败");
            return result;
        }

        Object obj = session.getAttribute(Constant.SESSION_USER);
        User user = (User) obj;

        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        List<Map<String, String>> opList = null;
        Map<String, Object> orderMap = null;
        Map<String, String> opMap = null;
        Page<Order> orderPage = orderService.findTailOrderWithMercPush(user.getId(), orderId, page, size);

        if (orderPage.hasContent()) {
            for (Order order : orderPage.getContent()) {
                orderMap = new HashMap<String, Object>();
                orderMap.put("order_id", order.getId() + "");
                orderMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
                orderMap.put("to_address", order.getToAddress());
                orderMap.put("product_num", order.getTotalAmount() + "");
                orderMap.put("total_price", order.getTotalPrice() + "");
                orderMap.put("desc", order.getProsDesc());
                if(order.getStatus() > 0){
                	if(order.getMerchantsId()==user.getId())
                		orderMap.put("status", "1");
                	else
                		orderMap.put("status", "2");
                }else
                	orderMap.put("status", "0");
                orderMap.put("phone", order.getCustomerPhone());
                orderMap.put("nick_name", order.getCustomerName());
                
                List<OrderProduct> ops = order.getProducts();
                opList=new ArrayList<Map<String, String>>();
                for (OrderProduct op : ops) {
                    opMap = new HashMap<String, String>();
                    opMap.put("p_id", op.getProductId() + "");
                    opMap.put("p_name", op.getProductName());
                    opMap.put("p_price", op.getProductPrice() + "");
                    opMap.put("p_img", op.getProductImg());
                    opMap.put("p_num", op.getAmount() + "");
                    opList.add(opMap);
                }
                orderMap.put("products", opList);
                reList.add(orderMap);
            }
        }

        reMap.put("orders", reList);

        result.put("errorcode", "1");
        result.put("info", "");
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
    public Map<String, Object> receiveOrderDetail(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        if (orderId > 0) {
        	Object obj = session.getAttribute(Constant.SESSION_USER);
            User user = (User) obj;
            Map<String, Object> reMap = new HashMap<String, Object>();
            List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
            Map<String, String> opMap = null;
            Order order = orderService.findOne(orderId);
            if (null != order) {
                List<OrderProduct> ops = order.getProducts();
                if (ops.size() > 0) {
                    for (OrderProduct op : ops) {
                        opMap = new HashMap<String, String>();
                        opMap.put("p_id", op.getProductId() + "");
                        opMap.put("p_name", op.getProductName());
                        opMap.put("p_price", op.getProductPrice() + "");
                        opMap.put("p_img", op.getProductImg());
                        opMap.put("p_num", op.getAmount() + "");
                        reList.add(opMap);
                    }
                }
                reMap.put("order_id", order.getId()+"");
                reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
                reMap.put("to_address", order.getToAddress());
                reMap.put("phone", order.getCustomerPhone());
                reMap.put("nick_name", order.getCustomerName());
                if(order.getStatus() > 0){
                	if(order.getMerchantsId()==user.getId())
                		reMap.put("status", "1");
                	else
                		reMap.put("status", "2");
                }else
                	reMap.put("status", "0");
                reMap.put("product_num", order.getTotalAmount() + "");
                reMap.put("total_price", order.getTotalPrice() + "");
                reMap.put("products", reList);
            }
            result.put("errorcode", "1");
            result.put("info", "");
            result.put("data", reMap);
            return result;
        } else {
            result.put("errorcode", "-2");
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
    public Map<String, Object> offer(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) throws Exception {
        if (orderId > 0) {
            Object obj = session.getAttribute(Constant.SESSION_USER);
            User user = (User) obj;
            Order order = orderService.findOne(orderId);
            if (null != order) {
                if (order.getStatus() > 0) {
                    if (user.getId()==order.getMerchantsId()) {
                        result.put("errorcode", "2");
                        result.put("info", "该订单已经被您抢到了");
                        result.put("merc_id", order.getMerchantsId()+"");
                        return result;
                    } else {
                    	List<Offer> ofs = order.getOffers();
                        if (ofs.size() > 0) {
                            for (Offer of : ofs) {
                                if (of.getMerchantsId()==user.getId()) {
                                    of.setAct(true);;
                                }
                            }
                        }
                        order.setOffers(ofs);
                        orderService.save(order);
                        result.put("errorcode", "3");
                        result.put("info", "该订单已经被其它人抢了");
                        result.put("merc_id", order.getMerchantsId()+"");
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

                    List<Offer> ofs = order.getOffers();
                    List<String> mersList=new ArrayList<String>();
                    if (ofs.size() > 0) {
                        for (Offer of : ofs) {
                            if (of.getMerchantsId()==user.getId()) {
                                of.setStatus(1);
                                of.setAct(true);
                            } else{
                                of.setStatus(2);
                                mersList.add(of.getMerchantsId()+"");
                            }
                        }
                    }
                    order.setOffers(ofs);
                    orderService.save(order);
                    
                    //推送给买家
                    String title="测试TITLE";
                    Map<String, Object> pushMap=new HashMap<String, Object>();
                    pushMap.put("type", "2");
                    pushMap.put("order_id", order.getId()+"");
                    pushMap.put("msg", "您的订单已经被"+user.getNickName()+"接受，接等待配送");
                    pushService.pushToUser(order.getCustomerId()+"", pushMap, title,UserType.customer);
                    
                    //推送给没抢到单的卖家
                    Map<String, Object> otherPushMap=new HashMap<String, Object>();
                    otherPushMap.put("type", "3");
                    otherPushMap.put("order_id", order.getId()+"");
                    otherPushMap.put("msg", "该订单已经被其它人抢了");
                    pushService.pushToUsers(mersList, otherPushMap, title,UserType.merchants);

                    result.put("errorcode", "1");
                    result.put("info", "");
                    result.put("merc_id", order.getMerchantsId()+"");
                    return result;
                }
            }
            return result;
        } else {
            result.put("errorcode", "-3");
            result.put("info", "抢单失败");
            return result;
        }
    }
    
    
    /**
	 * 订单列表头部,取>订单ID的记录
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/orderlisttop")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> orderListTop(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) {
		int page = 1;
		int size = 50;
		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = (User) obj;
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		List<Map<String, String>> opList = null;
		Map<String, Object> orderMap = null;
		Map<String, String> opMap = null;
		Page<Order> orderPage = orderService.findTopOrderWithMerc(user.getId(), orderId, page, size);
		if (orderPage.hasContent()) {
			for (Order order : orderPage.getContent()) {
				orderMap = new HashMap<String, Object>();
				orderMap.put("order_id", order.getId() + "");
				orderMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
				orderMap.put("to_address", order.getToAddress());
				orderMap.put("nick_name", order.getCustomerName());
				orderMap.put("phone", order.getCustomerPhone());
				orderMap.put("status", order.getStatus() + "");
				orderMap.put("product_num", order.getTotalAmount() + "");
				orderMap.put("total_price", order.getTotalPrice() + "");
				orderMap.put("desc", order.getProsDesc());

				List<OrderProduct> ops = order.getProducts();
				opList = new ArrayList<Map<String, String>>();
				for (OrderProduct op : ops) {
					opMap = new HashMap<String, String>();
					opMap.put("p_id", op.getProductId() + "");
                    opMap.put("p_name", op.getProductName());
                    opMap.put("p_price", op.getProductPrice() + "");
                    opMap.put("p_img", op.getProductImg());
                    opMap.put("p_num", op.getAmount() + "");
					opList.add(opMap);
				}
				orderMap.put("products", opList);
				reList.add(orderMap);
			}
		}
		result.put("errorcode", "1");
		result.put("info", "");
		result.put("data", reList);
		return result;
	}
	
	/**
	 * 订单列表尾部,取<订单ID的记录
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/orderlisttail")
	@Authorization(type = Constant.SESSION_USER)
	public Map<String, Object> orderListTail(@RequestParam(value = "orderid", required = false, defaultValue = "0") Long orderId) {
		int page = 1;
        int size = 10;

        if (orderId <= 0) {
            result.put("errorcode", "-2");
            result.put("info", "查询失败");
            return result;
        }
		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = (User) obj;
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		List<Map<String, String>> opList = null;
		Map<String, Object> orderMap = null;
		Map<String, String> opMap = null;
		Page<Order> orderPage = orderService.findTailOrderWithMerc(user.getId(), orderId, page, size);
		if (orderPage.hasContent()) {
			for (Order order : orderPage.getContent()) {
				orderMap = new HashMap<String, Object>();
				orderMap.put("order_id", order.getId() + "");
				orderMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
				orderMap.put("to_address", order.getToAddress());
				orderMap.put("nick_name", order.getCustomerName());
				orderMap.put("phone", order.getCustomerPhone());
				orderMap.put("status", order.getStatus() + "");
				orderMap.put("product_num", order.getTotalAmount() + "");
				orderMap.put("total_price", order.getTotalPrice() + "");
				orderMap.put("desc", order.getProsDesc());

				List<OrderProduct> ops = order.getProducts();
				opList = new ArrayList<Map<String, String>>();
				for (OrderProduct op : ops) {
					opMap = new HashMap<String, String>();
					opMap.put("p_id", op.getProductId() + "");
                    opMap.put("p_name", op.getProductName());
                    opMap.put("p_price", op.getProductPrice() + "");
                    opMap.put("p_img", op.getProductImg());
                    opMap.put("p_num", op.getAmount() + "");
					opList.add(opMap);
				}
				orderMap.put("products", opList);
				reList.add(orderMap);
			}
		}
		result.put("errorcode", "1");
		result.put("info", "");
		result.put("data", reList);
		return result;
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
    public Map<String, Object> orderList(@RequestParam(value = "page", required = false, defaultValue = "1")
    int page, @RequestParam(value = "size", required = false, defaultValue = "10")
    int size) {
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
                reMap.put("total_price", order.getTotalPrice() + "");
                reMap.put("desc", order.getProsDesc());
                reList.add(reMap);
            }
        }

        result.put("errorcode", "1");
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
    public Map<String, Object> orderDetail(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {

        if (orderId > 0) {
            Map<String, Object> reMap = new HashMap<String, Object>();
            List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
            Order order = orderService.findOne(orderId);
            if (null != order) {
                List<OrderProduct> ops = order.getProducts();
                if (ops.size() > 0) {
                    for (OrderProduct op : ops) {
                        Map<String, String> opMap = new HashMap<String, String>();
                        opMap.put("p_id", op.getProductId() + "");
                        opMap.put("p_name", op.getProductName());
                        opMap.put("p_price", op.getProductPrice() + "");
                        opMap.put("p_img", op.getProductImg());
                        opMap.put("p_num", op.getAmount() + "");
                        reList.add(opMap);
                    }
                }

                reMap.put("order_id", order.getId()+"");
                reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
                reMap.put("to_address", order.getToAddress());
                reMap.put("phone", order.getCustomerPhone());
                reMap.put("nick_name", order.getCustomerName());
                reMap.put("status", order.getStatus() + "");
                reMap.put("product_num", order.getTotalAmount() + "");
                reMap.put("total_price", order.getTotalPrice() + "");
                reMap.put("products", reList);

                result.put("errorcode", "1");
                result.put("info", "");
                result.put("data", reMap);
                return result;
            }else{
                result.put("errorcode", "-2");
                result.put("info", "查询失败");
                return result;
            }
        } else {
            result.put("errorcode", "-3");
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
    public Map<String, Object> cancleOrder(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        if (orderId > 0) {
            Order order = orderService.findOne(orderId);
            if (null != order) {
                order.setCancelDate(System.currentTimeMillis());
                order.setStatus(10);

                orderService.save(order);
                
                String title="测试TITLE";
                Map<String, Object> pushMap=new HashMap<String, Object>();
                pushMap.put("type", "7");
                pushMap.put("order_id", order.getId()+"");
                pushMap.put("msg", "您的订单已被"+order.getMerchantsName()+"取消");
                pushService.pushToUser(order.getCustomerId()+"", pushMap, title,UserType.customer);

                result.put("errorcode", "1");
                result.put("info", "");
                return result;
            } else {
                result.put("errorcode", "-2");
                result.put("info", "取消订单失败");
                return result;
            }
        } else {
            result.put("errorcode", "-3");
            result.put("info", "取消订单失败");
            return result;
        }
    }

    /**
     * 确认送达
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/arrive")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> arriveOrder(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        if (orderId > 0) {
            Order order = orderService.findOne(orderId);
            if (null != order) {
                order.setArrivedDate(System.currentTimeMillis());
                order.setStatus(4);

                orderService.save(order);
                
                String title="测试TITLE";
                Map<String, Object> pushMap=new HashMap<String, Object>();
                pushMap.put("type", "4");
                pushMap.put("order_id", order.getId()+"");
                pushMap.put("msg", "您的订单状态已变更");
                pushService.pushToUser(order.getCustomerId()+"", pushMap, title,UserType.customer);

                result.put("errorcode", "1");
                result.put("info", "");
                return result;
            } else {
                result.put("errorcode", "-2");
                result.put("info", "确认失败");
                return result;
            }
        } else {
            result.put("errorcode", "-3");
            result.put("info", "确认失败");
            return result;
        }
    }
}
