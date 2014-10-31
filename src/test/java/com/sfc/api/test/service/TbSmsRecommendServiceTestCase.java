package com.sfc.api.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.service.TbSmsRecommendService;

public class TbSmsRecommendServiceTestCase extends BaseServiceTestCase {

	@Autowired
	TbSmsRecommendService tbSmsRecommendService;
	
	@Test
	public void testUpdateStatusByMobile(){
		Integer in = tbSmsRecommendService.updateStatusByMobile(1L, "15555555555");
		System.out.println(in);
	}
}