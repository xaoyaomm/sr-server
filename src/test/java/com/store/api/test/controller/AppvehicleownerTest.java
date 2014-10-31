package com.store.api.test.controller;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.store.api.controller.TerminalAction;


public class AppvehicleownerTest extends BaseActionTestCase{
    
    @Before
    public void initSession() throws Exception{
        cookie=new Cookie("JSESSIONID", "test1234567890plUser");
        headers.add("ssid", "test1234567890plUser");
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/sessionInit/plUser").cookie(cookie).headers(headers));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("init session:"+result);
    }
    
    
    @Test
    public void testResultList() throws Exception{
//        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appvehicleowner/reporttruckstatus").accept(MediaType.ALL).param("status", "0"));          
//        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appvehicleowner/reportservicestatus").accept(MediaType.ALL).param("status", "1").param("booking_id", "1"));
    	ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appvehicleowner/reporttruckstatus").accept(MediaType.ALL)
    			.param("status", "0")
//    			.param("limit", "10")
    			.cookie(cookie).headers(headers));

        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
}