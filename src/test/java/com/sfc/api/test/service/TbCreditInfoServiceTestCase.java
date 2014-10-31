package com.sfc.api.test.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.TbCreditInfo;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.TbCreditInfoService;

public class TbCreditInfoServiceTestCase extends BaseServiceTestCase {
	
	@Autowired
	TbCreditInfoService tbCreditInfoService;
	
	@Test
	public void testSave() {
		TbCreditInfo info = new TbCreditInfo();
		info.setCreateDt(new Date());
		info.setPoint(100L);
		info.setUserId(43434L);
		info.setUserType(UserType.cargo);
		tbCreditInfoService.save(info);
	}

    @Test
    public void testQueryTbCreditInfoByUserIdAndUserType(){
    	TbCreditInfo info = tbCreditInfoService.queryTbCreditInfoByUserIdAndUserType(213L, UserType.cargo);
    	Assert.assertNotNull(info);
    }
}
