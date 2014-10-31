package com.sfc.api.test.service;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.TbTicketInfo;
import com.store.api.mysql.entity.TicketStats;
import com.store.api.mysql.entity.enumeration.TicketType;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.TbTicketInfoService;
import com.store.api.mysql.service.TicketStatsService;

public class TbTicketInfoServiceTestCase extends BaseServiceTestCase {

	@Autowired
	TbTicketInfoService tbTicketInfoService;

	@Autowired
	TicketStatsService ticketStatsService;

	@Test
	public void testFindByUserIdAndUserTypeAndTicketTypeAndTicketStatus() {
		List<TbTicketInfo> d = tbTicketInfoService
				.findByUserIdAndUserTypeAndTicketTypeAndTicketStatus(213L,
						UserType.cargo, TicketType.voucher, 0L);
		Assert.assertNotNull(d);
	}

	@Test
	public void testQueryTicketStatsByStatus() {
		List<TicketStats> dataList = ticketStatsService
				.queryTicketStatsByStatusAndUserIdAndUserType(0L, 213L,
						UserType.cargo);
		Assert.assertNotNull(dataList);
	}

	@Test
	public void testQueryTicketStatsByStatusAndUserIdAndUserTypeAndEndDt() {
		List<TicketStats> dataList = ticketStatsService
				.queryTicketStatsByStatusAndUserIdAndUserTypeAndEndDt(0L, 213L,
						UserType.cargo, new Date());
		Assert.assertNotNull(dataList);
	}

	@Test
	public void testFindByUserIdAndUserTypeAndTicketTypeAndTicketStatusAndEndDtGreaterThan() {
		List<TbTicketInfo> dataList = ticketStatsService
				.findByUserIdAndUserTypeAndTicketTypeAndTicketStatusAndNowGreaterThan(
						205L, UserType.cargo, TicketType.voucher, 0L);
		Assert.assertNotNull(dataList);
	}	
}
