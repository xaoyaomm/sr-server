package com.store.api.controller;

import java.util.ArrayList;
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
import com.store.api.mongo.entity.Address;
import com.store.api.mongo.entity.Catalog;
import com.store.api.mongo.entity.OftenProduct;
import com.store.api.mongo.entity.Order;
import com.store.api.mongo.entity.Product;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.entity.subdocument.Offer;
import com.store.api.mongo.entity.subdocument.OrderProduct;
import com.store.api.mongo.entity.vo.UserSearch;
import com.store.api.mongo.service.AddressService;
import com.store.api.mongo.service.CatalogService;
import com.store.api.mongo.service.OftenProductService;
import com.store.api.mongo.service.OrderService;
import com.store.api.mongo.service.ProductService;
import com.store.api.mongo.service.PushService;
import com.store.api.mongo.service.UserService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

/**
 * 顾客APP接口控制器
 * 
 * Revision History
 * 
 * @author vincent,2014年11月15日 created it
 */
@Controller()
@Scope("prototype")
@RequestMapping("/customer")
public class CustomerAction extends BaseAction {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PushService pushService;

    @Autowired
    private OftenProductService oftenProductService;

    /**
     * 查询所有商品列表
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cataloglist", produces = "text/plain;charset=UTF-8")
    public String catalogList() {
        List<Catalog> catalogs = catalogService.findAllCatalog();
        Map<String, String> reMap = null;
        List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
        if (null != catalogs && catalogs.size() > 0) {
            for (Catalog catalog : catalogs) {
                reMap = new HashMap<String, String>();
                reMap.put("catalog_id", catalog.getId() + "");
                reMap.put("catalog_name", catalog.getName());
                reMap.put("order", catalog.getOrder() + "");
                reList.add(reMap);
            }
        }
        return JsonUtils.resultJson(1, "", reList);
    }

    /**
     * 查询商品列表
     * 
     * @param ver 版本号
     * @return
     */
    @ResponseBody
    @RequestMapping("/productlist")
    public Map<String, Object> productList(@RequestParam(value = "ver", required = false, defaultValue = "0")
    Long ver) {
        Long areaId = 51L;
        List<Product> list = null;
        Long maxVer = productService.findMaxVer(areaId);
        if (ver.equals(0L)) {
            list = productService.findByAreaId(areaId);
        } else {
            list = productService.findByAreaIdAndVerGreaterThan(areaId, ver);
        }

        Map<String, String> reMap = null;
        List<Map<String, String>> reList = new ArrayList<Map<String, String>>();

        for (Product product : list) {
            reMap = new HashMap<String, String>();
            reMap.put("p_id", product.getId() + "");
            reMap.put("p_name", product.getName());
            reMap.put("p_price", product.getPrice() + "");
            reMap.put("p_img", product.getImgUrl());
            reMap.put("p_catalog", product.getCatalogId() + "");
            reMap.put("p_status", product.getStatus() + "");
            reMap.put("p_order", product.getOrder()+"");
            reList.add(reMap);
        }
        result.put("errorcode", "1");
        result.put("info", "");
        result.put("ver", maxVer + "");
        result.put("data", reList);
        return result;
    }

    /**
     * 下单接口
     * 
     * @param json
     * @return
     */
    @ResponseBody
    @RequestMapping("/order")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> productList(@RequestParam(value = "goods", required = false, defaultValue = "")
    String json, @RequestParam(value = "addrid", required = false, defaultValue = "")
    Long addrId) {
        if (Utils.isEmpty(json)) {
            result.put("errorcode", "-2");
            result.put("info", "下单失败");
            return result;
        }

        List<OrderProduct> opList = JsonUtils.json2OrderProduct(json);
        if (null == opList) {
            result.put("errorcode", "-3");
            result.put("info", "下单失败");
            return result;
        }

        Object obj = session.getAttribute(Constant.SESSION_USER);
        User user = (User) obj;

        Address address = addressService.findOne(addrId);
        ;
        if (null == address) {
            if (Utils.isEmpty(user.getAddress()) || null == user.getLocation()) {
                result.put("errorcode", "-4");
                result.put("info", "无效的地址，请重新填写地址");
                return result;
            } else
                address = addressService.findOne(user.getAddressId());
        }

        List<Long> proIds = new ArrayList<Long>();
        Map<String, Object> reMap = new HashMap<String, Object>();
        long totalPrice = 0;
        Order order = null;
        List<Offer> offerList = null;
        StringBuffer prosDesc = new StringBuffer();

        for (OrderProduct op : opList) {
            proIds.add(op.getProductId());
        }

        try {

            Map<Long, Product> prosMap = productService.findByIds(proIds);

            for (OrderProduct op : opList) {
                Product pro = prosMap.get(op.getProductId());
                prosDesc.append(pro.getName()).append(",");
                totalPrice += pro.getPrice() * op.getAmount();
                op.setProductImg(pro.getImgUrl());
                op.setProductName(pro.getName());
                op.setProductPrice(pro.getPrice());
            }

            // 保存订单对象
            order = new Order();
            order.setCreateDate(System.currentTimeMillis());
            order.setCustomerId(user.getId());
            order.setCustomerName(address.getName());
            order.setCustomerPhone(address.getPhone());
            order.setToAddress(address.getAddress());
            order.setToLocation(address.getLocation());
            order.setTotalPrice(totalPrice);
            order.setTotalAmount(opList.size());
            order.setProsDesc(prosDesc.toString());
            order.setStatus(0);

            // List<User> users = userService.findByType(UserType.merchants);
            List<UserSearch> pushUsers = userService.geoSearch(UserType.merchants, order.getToLocation(), Constant.SEARCH_DISTANCE);
            List<Map<String, String>> locationList = new ArrayList<Map<String, String>>();
            Map<String, String> locationMap = null;
            offerList = new ArrayList<Offer>();
            List<String> accountList1 = new ArrayList<String>();
            List<String> accountList2 = new ArrayList<String>();
            List<String> accountList3 = new ArrayList<String>();
            for (UserSearch us : pushUsers) {
                Offer offer = new Offer();
                offer.setCreateDate(System.currentTimeMillis());
                offer.setMerchantsId(us.getUser().getId());
                offer.setStatus(0);
                offerList.add(offer);
                locationMap = new HashMap<String, String>();
                locationMap.put("distance", us.getDistance() + "");
                locationMap.put("lng", us.getUser().getLocation()[0] + "");
                locationMap.put("lat", us.getUser().getLocation()[1] + "");
                locationMap.put("merc_name", us.getUser().getNickName());
                locationList.add(locationMap);

                if (us.getDistance() <= 100) // <=100米的商家优先推送
                    accountList1.add(us.getUser().getId() + "");
                else if (us.getDistance() <= 500)
                    accountList2.add(us.getUser().getId() + "");
                else
                    accountList3.add(us.getUser().getId() + "");
            }
            order.setOffers(offerList);
            order.setProducts(opList);
            orderService.save(order);
            // 将所购商品加入常购列表中
            oftenProductService.addToOftenProduct(order.getCustomerId(), opList);

            // TODO 推送给商户 users
            String title = "测试TITLE";
            Map<String, Object> pushMap = new HashMap<String, Object>();
            pushMap.put("type", "1");
            pushMap.put("order_id", order.getId() + "");
            pushMap.put("msg", "测试MSG");
            if (accountList1.size() > 0)
                pushService.orderPushToMerc(accountList1, pushMap, title, 0);
            if (accountList2.size() > 0)
                pushService.orderPushToMerc(accountList2, pushMap, title, 15);
            if (accountList3.size() > 0)
                pushService.orderPushToMerc(accountList3, pushMap, title, 30);

            reMap.put("mercs", locationList);
            reMap.put("order_id", order.getId() + "");
            reMap.put("invalid_time", "300");

            reMap.put("rec_num", pushUsers.size() + "");

        } catch (Exception e) {
            result.put("errorcode", "-4");
            result.put("info", "下单失败");
            return result;
        }

        result.put("errorcode", "1");
        result.put("info", "");
        result.put("data", reMap);
        return result;
    }

    /**
     * 下单状态查询
     * 
     * @param orderid 订单号
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryoffer")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> queryOffer(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        if (orderId > 0) {
            Order order = orderService.findOne(orderId);
            Map<String, String> reMap = new HashMap<String, String>();
            List<Offer> offerList = order.getOffers();

            if (null != order && order.getStatus() > 0) {
                reMap.put("status", "1");
                reMap.put("rec_num", offerList.size() + "");
                reMap.put("deal_merc", order.getMerchantsName());
            } else {
                reMap.put("status", "0");
                reMap.put("rec_num", offerList.size() + "");
                reMap.put("deal_merc", "");
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
     * 订单列表查询
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
        Page<Order> orderPage = orderService.findByCustomerId(user.getId(), page, size);
        if (orderPage.hasContent()) {
            for (Order order : orderPage.getContent()) {
                reMap = new HashMap<String, String>();
                reMap.put("order_id", order.getId() + "");
                reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
                reMap.put("deal_merc", order.getMerchantsName());
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
            List<OrderProduct> ops = order.getProducts();
            if (ops.size() > 0) {
                for (OrderProduct op : ops) {
                    Map<String, String> opMap = new HashMap<String, String>();
                    opMap.put("p_id", op.getProductId() + "");
                    // opMap.put("p_name", op.getProductName());
                    // opMap.put("p_price", op.getProductPrice()+"");
                    // opMap.put("p_img", op.getProductImg());
                    opMap.put("p_num", op.getAmount() + "");
                    reList.add(opMap);
                }
            }

            reMap.put("date", Utils.formatDate(new Date(order.getCreateDate()), null));
            reMap.put("deal_merc", order.getMerchantsName());
            reMap.put("status", order.getStatus() + "");
            reMap.put("product_num", order.getTotalAmount() + "");
            reMap.put("total_price", order.getTotalPrice() + "");
            reMap.put("products", reList);

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
     * 确认收货
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/confirm")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> confirm(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        if (orderId > 0) {
            Order order = orderService.findOne(orderId);
            order.setConfirmDate(System.currentTimeMillis());
            order.setStatus(6);
            orderService.save(order);

            result.put("errorcode", "1");
            result.put("info", "");
            return result;
        } else {
            result.put("errorcode", "-2");
            result.put("info", "确认收货失败");
            return result;
        }
    }

    /**
     * 标记为未送达
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/unfinish")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> unfinish(@RequestParam(value = "orderid", required = false, defaultValue = "0")
    Long orderId) {
        if (orderId > 0) {
            Order order = orderService.findOne(orderId);
            order.setUnfinishDate(System.currentTimeMillis());
            order.setStatus(9);
            orderService.save(order);

            result.put("errorcode", "1");
            result.put("info", "");
            return result;
        } else {
            result.put("errorcode", "-2");
            result.put("info", "确认收货失败");
            return result;
        }
    }

    /**
     * 查询常购商品
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/often")
    @Authorization(type = Constant.SESSION_USER)
    public Map<String, Object> often() {
        Object obj = session.getAttribute(Constant.SESSION_USER);
        User user = (User) obj;
        List<OftenProduct> list = oftenProductService.findTopByUserId(user.getId(), 10);
        Map<String, Object> reMap = null;
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        for (OftenProduct op : list) {
            reMap = new HashMap<String, Object>();
            reMap.put("p_id", op.getProductId() + "");
            reMap.put("p_num", op.getNum() + "");
            reList.add(reMap);
        }
        if(reList.size()>0)
            result.put("errorcode", "1");
        else
            result.put("errorcode", "2");
        result.put("info", "");
        result.put("data", reList);
        return result;
    }

}
