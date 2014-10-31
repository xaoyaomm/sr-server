package com.sfc.api.test.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.dao.NativeDao;
import com.store.api.mysql.entity.OfferListVo;

public class NativeServiceImplTestCase extends BaseServiceTestCase {
	
	@Autowired
	private NativeDao dao;
	
	@Test
	public void testFindofferList(){
		List<OfferListVo> list=dao.findofferList(100L, "13418595673", 0, 10);
		System.out.println(list.size());
	}

}
