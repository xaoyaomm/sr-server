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

}
