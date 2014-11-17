package com.store.api.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.mongo.entity.Catalog;
import com.store.api.mongo.entity.Order;
import com.store.api.mongo.entity.Product;
import com.store.api.mongo.service.CatalogService;
import com.store.api.mongo.service.ProductService;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

@Controller()
@Scope("prototype")
@RequestMapping("/customer")
public class CustomerAction extends BaseAction {
    
    private DecimalFormat nfmt = new DecimalFormat("0.0");
    
    @Autowired
    private CatalogService catalogService;
    
    @Autowired
    private ProductService productService;
    
    
    /**
     * 查询所有商品列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/cataloglist")
    public String catalogList(){
        List<Catalog> catalogs=catalogService.findAllCatalog();
        Map<String, String> reMap = null;
        List<Map<String, String>> reList=new ArrayList<Map<String,String>>();
        if(null!=catalogs&& catalogs.size()>0){
            for (Catalog catalog : catalogs) {
                reMap=new HashMap<String, String>();
                reMap.put("catalog_id", catalog.getId()+"");
                reMap.put("catalog_name", catalog.getName());
                reMap.put("order", catalog.getOrder()+"");
                reList.add(reMap);
            }
        }
        return JsonUtils.resultJson(0, "", reList);
    }

    
    /**
     * 查询商品列表
     * @param ver 版本号
     * @return
     */
    @ResponseBody
    @RequestMapping("/productlist")
    public Map<String, Object> productList(@RequestParam(value = "ver", required = false, defaultValue = "0")
    Long ver){
        Long areaId=51L;
        List<Product> list=null;
        Long maxVer=productService.findMaxVer(areaId);
        if(ver.equals(0L)){
            list=productService.findByAreaId(areaId);
        }else{
            list=productService.findByAreaIdAndVerGreaterThan(areaId, ver);
        }
        
        Map<String, String> reMap = null;
        List<Map<String, String>> reList=new ArrayList<Map<String,String>>();
        
        for (Product product : list) {
            reMap=new HashMap<String, String>();
            reMap.put("p_id", product.getId()+"");
            reMap.put("p_name", product.getName());
            reMap.put("p_price", nfmt.format(product.getPrice()));
            reMap.put("p_img", product.getImgUrl());
            reMap.put("p_catalog", product.getCatalogId()+"");
            reMap.put("p_status", product.getStatus()+"");
            reList.add(reMap);
        }
        result.put("errorcode", "0");
        result.put("info", "");
        result.put("ver", maxVer+"");
        result.put("data", reList);
        return result;
    }
    
    
    @ResponseBody
    @RequestMapping("/order")
    public Map<String, Object> productList(@RequestParam(value = "jsoninfo", required = false, defaultValue = "")
    String json){
        if(Utils.isEmpty(json)){
            result.put("errorcode", "2");
            result.put("info", "下单失败");
            return result;
        }
        
        List<Map<Long, Long>> orderList=(List<Map<Long, Long>>) JsonUtils.json2Object(json, new ArrayList<Map<Long, Long>>().getClass());
        if(null==orderList){
            result.put("errorcode", "3");
            result.put("info", "下单失败");
            return result;
        }
        
        Order order=new Order();
        
        return result;
    }
}
