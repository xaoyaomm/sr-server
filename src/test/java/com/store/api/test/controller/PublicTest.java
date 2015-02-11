package com.store.api.test.controller;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.store.api.common.Constant;
import com.store.api.common.MD5;
import com.store.api.utils.JsonUtils;


public class PublicTest extends BaseActionTestCase{
    
    @Before
    public void init() throws Exception{
        cookie=new Cookie("STORERUNSSID", "A8LhuB9yHQCiOn1");
    }
    
    
    @Test
    public void testRegister() throws Exception{
    	ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/register").accept(MediaType.ALL)
    			.param("name", "vincent3_merc")
    			.param("pwd", "12345")
    			.param("type", "1")
    			.param("nickname", "龙华天福便利店")
    			.param("phone", "13662241734"));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        log.info(mr.getResponse().getCookie(Constant.SESSION_NAME).getValue());
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testVisitorlogin() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/visitorlogin").accept(MediaType.ALL)
                .param("uuid", "vincent"));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        log.info(mr.getResponse().getCookie(Constant.SESSION_NAME).getValue());
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testCustomerLogin() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/login").accept(MediaType.ALL)
                .param("name", "vincent")
                .param("pwd", MD5.encrypt("123456")));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        log.info(mr.getResponse().getCookie(Constant.SESSION_NAME).getValue());
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testMerchantsLogin() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/login").accept(MediaType.ALL)
                .param("name", "mer1")
                .param("pwd", MD5.encrypt("123456"))
                .param("type", "1"));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        log.info(mr.getResponse().getCookie(Constant.SESSION_NAME).getValue());
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testModify() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/modify").accept(MediaType.ALL)
                .param("phone", "13662241734").cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testUserInfo() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/userinfo").accept(MediaType.ALL)
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testQueryAddress() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/queryaddress").accept(MediaType.ALL)
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testeditAddress() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/editaddress").accept(MediaType.ALL)
                .param("address", "测试地址")
                .param("lat", "144.1234")
                .param("lng", "22.1234")
                .param("def", "1")
                .cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
    @Test
    public void testCheckVersion() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/public/checkversion").accept(MediaType.ALL)
        		.param("type", "1")
                .param("code", "101")
        		.cookie(cookie));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.info(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
    }
    
}