package com.store.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Common;
import com.store.api.common.Constant;
import com.store.api.mongo.service.LuckdrawBlackListService;
import com.store.api.mysql.entity.BkBooking;
import com.store.api.mysql.entity.BkCallRecords;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.PlUsersCargo;
import com.store.api.mysql.entity.RetResult;
import com.store.api.mysql.entity.TbCreditInfo;
import com.store.api.mysql.entity.TbCreditLog;
import com.store.api.mysql.entity.TbPrizeGoods;
import com.store.api.mysql.entity.TbPrizeLog;
import com.store.api.mysql.entity.TbSmsRecommend;
import com.store.api.mysql.entity.TbSpecifyLuckydraw;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.BkBookingService;
import com.store.api.mysql.service.BkCallRecordsService;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.LuckDrawService;
import com.store.api.mysql.service.TbCreditInfoService;
import com.store.api.mysql.service.TbCreditLogService;
import com.store.api.mysql.service.TbPrizeGoodsService;
import com.store.api.mysql.service.TbPrizeLogService;
import com.store.api.mysql.service.TbSmsRecommendService;
import com.store.api.mysql.service.TbSpecifyLuckydrawService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.TimeUtil;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/appvehicleowner")
public class AppvehicleownerPointsAction extends BaseAction {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private Map<String, Object> result = new HashMap<String, Object>();
	@Autowired
	private TbCreditInfoService tbCreditInfoService;
	@Autowired
	private TbCreditLogService tbCreditLogService;
	@Autowired
	private LuckDrawService luckDrawService;
	@Autowired
	private TbPrizeLogService tbPrizeLogService;
	@Autowired
	private TbSpecifyLuckydrawService tbSpecifyLuckydrawService;
	@Autowired
	private CreditAndTicketManagementService creditAndTicketManagementService;
	@Autowired
	private TbSmsRecommendService tbSmsRecommendService;
	@Autowired
    private LuckdrawBlackListService luckdrawBlackListService;
	@Autowired
	private TbPrizeGoodsService tbPrizeGoodsService;
	@Autowired
	private BkBookingService bkBookingService;
	@Autowired
	private BkCallRecordsService bkCallRecordsService;
	
	/**
	 * 车主抽奖活动
	 * 
	 * @return
	 */
	@ResponseBody
	@Transactional
	@RequestMapping("/luckydraw")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> luckyDraw() {
		PlUsers plUsers = (PlUsers) session
				.getAttribute(Constant.SESSION_PL_USER);
		try {
			// 查询用户是否有积分
			TbCreditInfo credit = tbCreditInfoService
					.queryTbCreditInfoByUserIdAndUserType(plUsers.getUserId(),
							UserType.owners);
			if (null == credit || credit.getPoint() < 200) {
				result.put("errorcode", "3");
				result.put("info", "积分不足");
				return result;
			}
			// 减去积分
			Long point = credit.getPoint();
			credit.setPoint(point - 200L);
			tbCreditInfoService.save(credit);
			// 积分使用并记录日志 -200
			TbCreditLog creditLog = new TbCreditLog();
			creditLog.setOperateDt(new Date());
			creditLog.setUserId(plUsers.getUserId());
			creditLog.setUserType(UserType.owners);
			creditLog.setPoint(200L);
			creditLog.setType(1L);
			creditLog.setRemark("抽奖消费积分");
			tbCreditLogService.save(creditLog);

			Long luckDraw_result = 0L;
			// 人为干预
			List<TbSpecifyLuckydraw> specifyLuckdraw = tbSpecifyLuckydrawService
					.findByUserIdAndUserTypeAndStatus(plUsers.getUserId(),
							UserType.owners, 0L);
			
			// 如果有数据就取人工选定的
			if (specifyLuckdraw == null || specifyLuckdraw.size() == 0) {
				// 抽奖
				luckDraw_result = luckDrawService.getLuckDrawResult();
				
				//黑名单处理，如在黑名单中的车主，不允许抽到实物奖
                Set<Long> blackSet=luckdrawBlackListService.findAllUserId();
                if(blackSet.contains(plUsers.getUserId())){
                    while(luckDraw_result<=6){
                        luckDraw_result = luckDrawService.getLuckDrawResult();
                    }
                }
				
			} else {
				TbSpecifyLuckydraw sLuckdraw = specifyLuckdraw.get(0);
				luckDraw_result = sLuckdraw.getPrizeId();
				//处理人工干预抽奖的的状态 status改为1
				sLuckdraw.setStatus(1L); //已经抽奖过了
				tbSpecifyLuckydrawService.save(sLuckdraw);
			}
			if(luckDraw_result == 0L){
				result.put("errorcode", "5");
				result.put("info", "再接再厉");
				result.put("data", luckDraw_result);
				return result;
			}
			
			// 处理抽奖结果
			luckDrawService.dealWithResult(plUsers.getUserId(),
					UserType.owners, luckDraw_result);

			// 记录抽奖结果
			Long status = 0L;
			if( luckDraw_result >=7 && luckDraw_result <= 11){
				//如果是积分 已处理
				status = 1L;
			}
			TbPrizeLog prizeLog = new TbPrizeLog();
			prizeLog.setCreateDt(new Date());
			prizeLog.setPrizeId(Long.valueOf(luckDraw_result + ""));
			prizeLog.setUserId(plUsers.getUserId());
			prizeLog.setUserMobile(plUsers.getMobile());
			prizeLog.setUserType(UserType.owners);
			prizeLog.setStatus(status);
			tbPrizeLogService.save(prizeLog);

			result.put("errorcode", "0");
			result.put("info", "成功");
			result.put("data", luckDraw_result);
			return result;

		} catch (Exception e) {
			result.put("errorcode", "4");
			result.put("info", "服务器忙晕了，请稍后再试");
			LOG.error("The luckyDraw() method invocation exception.", e);
			throw new RuntimeException();
		}

	}

	/**
	 * 车主查询积分
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryusercredit")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> queryUserCreditByCargo() {
		Object obj = session.getAttribute(Constant.SESSION_PL_USER);
		PlUsers user = (PlUsers) obj;
		Map<String, String> reMap = creditAndTicketManagementService
				.getUserCreditInfo(user.getUserId(), UserType.owners, 1);
		result.put("errorcode", "0");
		result.put("info", "查询用户积分成功");
		result.put("data", reMap);
		LOG.info("result" + JsonUtils.object2Json(result));
		return result;
	}

	/**
	 * 车主查询奖品列表
	 */
	@ResponseBody
	@RequestMapping("/getmyprize")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> getMyPrize() {
		Object obj = session.getAttribute(Constant.SESSION_PL_USER);
		PlUsers user = (PlUsers) obj;
		try {
			List<TbPrizeLog> prizeList = tbPrizeLogService
					.findByUserIdAndUserType(user.getUserId(),
							UserType.owners);
			List<Map<String, String>> prize = new ArrayList<Map<String, String>>();
			for (TbPrizeLog tbPrizeLog : prizeList) {
				Map<String, String> prizeMap = new HashMap<String, String>();

				prizeMap.put("prize",
						Common.getprize(tbPrizeLog.getPrizeId() + ""));
				prizeMap.put("time", TimeUtil.getDateString(
						tbPrizeLog.getCreateDt(), "中奖日期：yyyy.MM.d"));
				prizeMap.put("status", tbPrizeLog.getStatus() + "");
				prize.add(prizeMap);
			}
			result.put("errorcode", "0");
			result.put("info", "成功");
			result.put("data", prize);
			return result;
		} catch (Exception e) {
			LOG.error("The getmyprize() method invocation exception.", e);
			result.put("errorcode", "4");
			result.put("info", "服务器忙晕了，请稍后再试");
			return result;
		}
	}

	/**
	 * 中奖公告
	 */
	@ResponseBody
	@RequestMapping("/prizenotice")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> prizeNotice() {
		try {
			// 查询中奖列表
			List<TbPrizeLog> prizeList = tbPrizeLogService
					.findByUserTypeOrderByCreateDtDesc(UserType.owners);
			List<String> prizeNotice = new ArrayList<String>();
			//只显示20条
			int count = 0;
			String notice = "";
			for (TbPrizeLog tbPrizeLog : prizeList) {
				if(tbPrizeLog.getPrizeId().equals(6L)){
					notice = Common.formatPhoneNumber(tbPrizeLog.getUserMobile())
							+ "抽中了 "
							+ Common.getprize(tbPrizeLog.getPrizeId() + "");
					prizeNotice.add(notice);
					count++;
				}
				
			}
			for (TbPrizeLog tbPrizeLog : prizeList) {
				if(tbPrizeLog.getPrizeId().equals(5L)){
					notice = Common.formatPhoneNumber(tbPrizeLog.getUserMobile())
							+ "抽中了 "
							+ Common.getprize(tbPrizeLog.getPrizeId() + "");
					prizeNotice.add(notice);
					count ++;
				}
				
			}
			
			for (TbPrizeLog tbPrizeLog : prizeList) {
				if(!tbPrizeLog.getPrizeId().equals(6L) && !tbPrizeLog.getPrizeId().equals(5L)){
					notice = Common.formatPhoneNumber(tbPrizeLog.getUserMobile())
							+ "抽中了 "
							+ Common.getprize(tbPrizeLog.getPrizeId() + "");
					prizeNotice.add(notice);
					count ++;
				}
				if(count >= 20){
					break;
				}
			}
			result.put("errorcode", "0");
			result.put("info", "成功");
			result.put("data", prizeNotice);
			return result;
		} catch (Exception e) {
			LOG.error("The prizeNotice() method invocation exception.", e);
			result.put("errorcode", "4");
			result.put("info", "服务器忙晕了，请稍后再试");
			return result;
		}
	}

	/**
	 * 积分兑换钱
	 * 
	 * @return
	 */
	@ResponseBody
	@Transactional
	@RequestMapping("/credittomoney")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> creditToMoneyByOwners(
			@RequestParam(value = "money_num", required = true, defaultValue = "0") Integer money) {
		if (null == money || 0 == money) {
			result.put("errorcode", "7");
			result.put("info", "兑换数量不能为空");
			return result;
		}
		Object obj = session.getAttribute(Constant.SESSION_PL_USER);
		PlUsers user = (PlUsers) obj;
		try {
			RetResult retResult = creditAndTicketManagementService
					.creditToMoney(user.getUserId(), UserType.owners,
							money, user.getMobile(), null);
			result.put("errorcode", retResult.getErrorcode());
			result.put("info", retResult.getInfo());
			LOG.info("result" + JsonUtils.object2Json(result));
		} catch (Exception e) {
			result.put("errorcode", "10");
			result.put("info", "服务器忙晕啦，请稍候再试");
			LOG.error(
					"The creditToMoneyByOwners() method invocation exception.",
					e);
			throw new RuntimeException(
					"The creditToMoneyByOwners() method invocation fail.");
		}
		return result;
	}

	
	/**
	 * 短信邀请通知接口
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("msgInformation")
	@Transactional
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> msgInformation(
			@RequestParam(value = "jsoninfo", required = false, defaultValue = "") String jsoninfo) {
		try {
			PlUsers plUsers = (PlUsers) session
					.getAttribute(Constant.SESSION_PL_USER);
			List<Map<String, Object>> list = null;
			list = (List<Map<String, Object>>) JsonUtils.json2Object(jsoninfo,
					List.class);
			List<TbSmsRecommend> tbSmsRecommendList = new ArrayList<TbSmsRecommend>();
			TbSmsRecommend tbSmsRecommend = null;
			for (Map<String, Object> map : list) {
				tbSmsRecommend = new TbSmsRecommend();
				tbSmsRecommend.setMobile(map.get("m").toString());
				tbSmsRecommend.setUserType(UserType.owners);
				tbSmsRecommend.setUserMobile(plUsers.getMobile());
				tbSmsRecommend.setUserId(plUsers.getUserId());
				tbSmsRecommend.setSendTime(new Date());
				tbSmsRecommend.setStatus(0l);
				tbSmsRecommendList.add(tbSmsRecommend);
			}
			tbSmsRecommendService.save(tbSmsRecommendList);
			result.put("errorcode", "0");
			result.put("info", "短信邀请上报成功");
			return result;
		} catch (Exception e) {
			result.put("errorcode", "10");
			result.put("info", "服务器忙晕啦，请稍候再试");
			LOG.error("The register() method invocation exception.", e);
			throw new RuntimeException("The register() method invocation fail.");
		}
	}

	
	/**
	 * 奖品列表初始化
	 */
	@ResponseBody
	@RequestMapping("prizeinit")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String, Object> prizeInit(){
		try {
			List<TbPrizeGoods> prizeList = tbPrizeGoodsService.findAll();
			result.put("errorcode", "0");
			result.put("info", "成功");
			result.put("data", prizeList);
			return result;
		} catch (Exception e) {
			result.put("errorcode", "3");
			result.put("info", "服务器忙晕了，请稍后再试！");
			result.put("data", null);
			return result;
		}
		
	}
	
	/**
	 * 上传车主与货主通话记录
	 */
	@ResponseBody
	@RequestMapping("uploadcallrecords")
	@Authorization(type = Constant.SESSION_PL_USER)
	public Map<String,Object> uploadCallRecords(
			@RequestParam(value = "booking_id", required = false, defaultValue = "") Long bookingId,
			@RequestParam(value = "talk_time", required = false, defaultValue = "") Long talkTime
			){
		
		PlUsers plUsers = (PlUsers) session
				.getAttribute(Constant.SESSION_PL_USER);
		BkBooking booking = bkBookingService.findOne(bookingId);
		
		if( null == booking ){
			result.put("errorcode", "2");
			result.put("info", "订单不存在");
			return result;
		}
		try {
			BkCallRecords records = new BkCallRecords();
			records.setBookingId(bookingId);
			records.setMobileCallFrom(plUsers.getMobile());
			records.setCreateDt(new Date());
			records.setMobileCallTo(booking.getBookingFromUsersId());
			records.setCargoId(booking.getUserCargoId());
			records.setOwnerId(plUsers.getUserId());
			records.setTalkTime(talkTime/1000);  //保存秒数
			bkCallRecordsService.save(records);
			result.put("errorcode", "0");
			result.put("info", "成功");
			return result;
		} catch (Exception e) {
			result.put("errorcode", "3");
			result.put("info", "服务器忙晕了，请稍后再试");
			return result;
		}
		
		
	}
	
}
