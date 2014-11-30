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

public class MerchantsTestCase extends BaseActionTestCase {

	@Before
    public void init() throws Exception{
        cookie=new Cookie("STORERUNSSID", "dZLaA2eedoShQhJqQz");
    }
	
	@Test
    public void testReceiveOrderListTop() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/receiveorderlisttop").accept(MediaType.ALL)
        		 .param("orderid", "35")
        		 .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testReceiveOrderListTail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/receiveorderlisttail").accept(MediaType.ALL)
                 .param("orderid", "36")
                 .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testReceiveOrderDetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/receiveorderdetail").accept(MediaType.ALL)
        		 .param("orderid", "35")
        		 .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testOffer() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/offer").accept(MediaType.ALL)
        		 .param("orderid", "35")
        		 .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testOrderList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/orderlist").accept(MediaType.ALL)
        		 .param("page", "1")
        		 .param("size", "10").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testOrderDetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/orderdetail").accept(MediaType.ALL)
                .param("orderid", "35").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testCancleOrder() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/cancleorder").accept(MediaType.ALL)
                .param("orderid", "35").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
	
	@Test
    public void testArrive() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/merchants/arrive").accept(MediaType.ALL)
                .param("orderid", "35").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
}
