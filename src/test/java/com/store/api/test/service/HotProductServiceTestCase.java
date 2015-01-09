package com.store.api.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mongo.entity.Campaigns;
import com.store.api.mongo.entity.HotProduct;
import com.store.api.mongo.service.CampaignsService;
import com.store.api.mongo.service.HotProductService;

public class HotProductServiceTestCase extends BaseServiceTestCase {
	
	@Autowired
	HotProductService service;
	
	@Autowired
	CampaignsService camservice;
	
	@Test
	public void testSave(){
		HotProduct hp=new HotProduct();
		hp.setId(13);
		hp.setName("零食13");
		hp.setTotal(500);
		service.save(hp);
	}
	
	@Test
	public void testCamSave(){
		Campaigns cam=new Campaigns();
		cam.setBannerUrl("http://xxx.xxx.xx");
		cam.setName("test");
		cam.setPageUrl("http://bbb.bb.bb");
		cam.setStart(System.currentTimeMillis());
		cam.setEnd(System.currentTimeMillis());
		camservice.save(cam);
	}

}
