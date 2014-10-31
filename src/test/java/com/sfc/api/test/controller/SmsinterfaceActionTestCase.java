package com.sfc.api.test.controller;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;


public class SmsinterfaceActionTestCase extends BaseActionTestCase{
	
	
	 @Test
	public void test() throws Exception{
		 ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/smsinterface/send").accept(MediaType.ALL)
	    			.param("mobile", "18942529935")
	    			.param("msg", "测试"));

	        MvcResult mr = ra.andReturn();
	        String result = mr.getResponse().getContentAsString();
	        log.debug(result);
	        Assert.isTrue(StringUtils.isNotEmpty(result));
	}
	
}
