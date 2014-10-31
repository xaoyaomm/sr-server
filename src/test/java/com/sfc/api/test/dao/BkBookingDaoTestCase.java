package com.sfc.api.test.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sfc.api.test.service.BaseServiceTestCase;
import com.store.api.mysql.dao.BkBookingRepository;
import com.store.api.mysql.entity.BkBooking;
import com.store.api.mysql.entity.TbUserBalance;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.TbUserBalanceService;


public class BkBookingDaoTestCase extends BaseServiceTestCase{
	@Autowired
	BkBookingRepository bkBookingDao;
	
	@Autowired
	TbUserBalanceService tbUserBalanceService;

//	@Test
//	public void testFindone(){
//		Long bookingNo =1L;
//		BkBooking bkBooking =bkBookingDao.findOne(bookingNo);
//		System.out.println(JsonUtils.object2Json(bkBooking));
//	}

	@Test
	public void testGetByVehicleInfoIdAndFixedParam(){

		TbUserBalance bean = tbUserBalanceService.findByUserIdAndUserType(213L, UserType.cargo);
	    System.out.println(bean.getBalance());

	    Long id =2L;
	    List<BkBooking> list =bkBookingDao.getByVehicleInfoIdAndFixedParam(id);
	    Assert.assertTrue(null!=list && list.size()>0);
	}
	
	@Test
	public void testFindOrderCount(){
	    String count=bkBookingDao.findOrderCount(1L);
	    Assert.assertTrue(null!=count);
	}

}
