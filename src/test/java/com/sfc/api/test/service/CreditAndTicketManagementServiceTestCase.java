package com.sfc.api.test.service;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.RetResult;
import com.store.api.mysql.entity.TbSmsRecommend;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.CreditAndTicketManagementService;

public class CreditAndTicketManagementServiceTestCase extends
		BaseServiceTestCase {

	@Autowired
	CreditAndTicketManagementService creditAndTicketManagementService;

	@Test
	public void testRedeemCreditsByCargo() throws Exception {
		RetResult restult = creditAndTicketManagementService
				.redeemCreditsByCargo(213L, null, 1);
		System.err.println(restult.getErrorcode() + " >> " + restult.getInfo());
	}

	@Test
	public void testGetAllConfigInfo() {
		Map<String, Object> map = creditAndTicketManagementService
				.getAllConfigInfo();
		for (Map.Entry<String, Object> entry : map.entrySet())
			System.out.println(entry.getKey() + " >> " + entry.getValue());
	}

	@Test
	public void testAddOrderSendIntegral() throws Exception {
		RetResult retResult = creditAndTicketManagementService
				.addOrderSendIntegral(204L, UserType.cargo, "13068724479",
						"下单送积分", true);
		Assert.assertNotNull(retResult);
	}

//	@Test
//	public void testGetFirstTbSmsRecommendByMoible() {
//		TbSmsRecommend sms = creditAndTicketManagementService
//				.getFirstTbSmsRecommendByMoible("13537504375");
//		Assert.assertNotNull(sms);
//	}

	@Test
	public void testUpdateTicketInfoStatusByTicketNo() throws Exception {
		RetResult retResult = creditAndTicketManagementService
				.updateTicketInfoStatusByTicketNo("C0B0B0D12C933448", 293L,
						UserType.cargo, 0L, true);
		Assert.assertNotNull(retResult);
	}

	@Test
	public void testAddBalanceToUser() throws Exception {
		RetResult retResult = creditAndTicketManagementService
				.addBalanceToUser(117L, UserType.owners, 0.0, "13760301170",
						"注册送钱啦", true);
		Assert.assertNotNull(retResult);
	}

	@Test
	public void testSubBalanceToUser() throws Exception {
		RetResult retResult = creditAndTicketManagementService
				.subBalanceToUser(117L, UserType.owners, 2000.0, "13760301170",
						"扣钱啦");
		Assert.assertNotNull(retResult);
	}

	@Test
	public void testCheckActivityIsExpiredOrStart() {
		boolean result = creditAndTicketManagementService
				.checkActivityIsExpiredOrStart();
		Assert.assertTrue(result);
	}

	@Test
	public void testCreditToMoney() throws Exception {
		RetResult retResult = creditAndTicketManagementService.creditToMoney(
				117L, UserType.owners, 100, "13760301170", null);
		Assert.assertNotNull(retResult);
	}

	@Test
	public void testSubIntegral() throws Exception {
		RetResult retResult = creditAndTicketManagementService.subIntegral(
				117L, UserType.owners, 100L, null);
		Assert.assertNotNull(retResult);
	}

	@Test
	public void testGiveMoneyToUserCargoByOrderIsvalid() throws Exception {
		RetResult retResult = creditAndTicketManagementService
				.giveMoneyToUserCargoByOrderIsvalid(192L, 100.0, "18976543215",
						null, true);
		Assert.assertNotNull(retResult);
	}

	@Test
	public void testGetUserCreditInfo() {
		Map map = creditAndTicketManagementService.getUserCreditInfo(324L,
				UserType.cargo, 1);
		Assert.assertNotNull(map);
	}
}