package com.store.api.test.controller;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

public class CustomerTestCase extends BaseActionTestCase {
    
    @Before
    public void init() throws Exception{
        cookie=new Cookie("STORERUNSSID", "A8LhuB9yHQCiOn1");
    }
    
    @Test
    public void testCataloglist() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/cataloglist").accept(MediaType.ALL));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testProductList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/productlist").accept(MediaType.ALL)
                .param("ver", "1"));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testOrder() throws Exception{
    	String json="[{\"p_id\":\"16\",\"p_num\":\"2\"},{\"p_id\":\"34\",\"p_num\":\"1\"},{\"p_id\":\"44\",\"p_num\":\"1\"},{\"p_id\":\"27\",\"p_num\":\"1\"}]";
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/order").accept(MediaType.ALL)
                .param("goods",json)
                .param("addrid", "4").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testQueryOffer() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/queryoffer").accept(MediaType.ALL)
                .param("orderid", "36").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testOfferList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/orderlist").accept(MediaType.ALL)
                .param("page", "1")
                .param("size", "10")
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testOrderDetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/orderdetail").accept(MediaType.ALL)
                .param("orderid", "35").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testOften() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/often").accept(MediaType.ALL)
               .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testTopOrder() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/orderlisttop").accept(MediaType.ALL)
        		.param("orderid", "36")
        		.cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testTailOrder() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/orderlisttail").accept(MediaType.ALL)
        		.param("orderid", "39")
        		.cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testConfirm() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/confirm").accept(MediaType.ALL)
                .param("orderid", "35")
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testHot() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/hot").accept(MediaType.ALL)
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testCampaign() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/customer/campaign").accept(MediaType.ALL)
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
}
