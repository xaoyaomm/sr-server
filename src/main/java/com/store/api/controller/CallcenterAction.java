package com.store.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.TbSmsRecommend;
import com.store.api.mysql.entity.UsersCargoFavorite;
import com.store.api.mysql.entity.VeVehicleInfo;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.entity.procedure.OperationArea;
import com.store.api.mysql.entity.procedure.Ve_vehicle_info_users_for_detail;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.PlUsersService;
import com.store.api.mysql.service.ProcedureService;
import com.store.api.mysql.service.SmsSendService;
import com.store.api.mysql.service.TbSmsRecommendService;
import com.store.api.mysql.service.UserFavoriteService;
import com.store.api.mysql.service.VeVehicleInfoService;
import com.store.api.mysql.service.XmppService;
import com.store.api.utils.ConstantUtil;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/callcenter")
public class CallcenterAction extends BaseAction {
	@Autowired
	private ProcedureService procedureService;
	@Autowired
	private VeVehicleInfoService veVehicleInfoService;
	@Autowired
	private PlUsersService plUsersService;
	@Autowired
	private CreditAndTicketManagementService creditAndTicketManagementService;
	@Autowired
	private XmppService xmppService;
	@Autowired
	private TbSmsRecommendService tbSmsRecommendService;
	@Autowired
	private SmsSendService smsSendService;
	@Autowired
	private UserFavoriteService userFavoriteService;
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private Map<String, Object> result = new HashMap<String, Object>();

	/**************************************************************
	 * 车辆信息详情（供客服系统查询）
	 * 
	 * @param mobile
	 *            手机号码
	 * 
	 * @return Json
	 * 
	 */
	@ResponseBody
	@RequestMapping("/vedetailforcs")
	public String vedetailforcs(
			@RequestParam(value = "mobile", required = false, defaultValue = "0") String mobile) {
		Ve_vehicle_info_users_for_detail vv = null;
		OperationArea op = null;
		String imgPath = Constant.IMG_URL_PRE;
		List<Ve_vehicle_info_users_for_detail> list = new ArrayList<Ve_vehicle_info_users_for_detail>();
		try {
			vv = procedureService.findVehicleDetail(mobile, 1);

			if (null == vv) {
				return JsonUtils.resultJson(2, "车辆信息不存在", new ArrayList());
			} else {
				vv.setImg1("".equals(vv.getImg1()) ? "" : imgPath
						+ vv.getImg1());
				vv.setImg2("".equals(vv.getImg2()) ? "" : imgPath
						+ vv.getImg2());
				vv.setImg3("".equals(vv.getImg3()) ? "" : imgPath
						+ vv.getImg3());
				vv.setImg4("".equals(vv.getImg4()) ? "" : imgPath
						+ vv.getImg4());
				op = procedureService.findOperationArea(mobile, 2);
				ArrayList<OperationArea> oplist = new ArrayList<OperationArea>();
				oplist.add(op);
				vv.setOperation(oplist);
			}
			list.add(vv);
			return JsonUtils.resultJson(0, "成功", vv);
		} catch (Exception e) {
			LOG.error("The vedetailforcs() method invocation exception.", e);
			return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", new ArrayList());
		}

	}

	/**
	 * 供客服审核通过的时候调用()
	 */
	@ResponseBody
	@Transactional
	@RequestMapping("/callvehiclechecked")
	public Map<String, Object> callVehicleChecked(
			@RequestParam(value = "vehicle_info_id", required = false, defaultValue = "0") Long vehicleInfoId) {
		// TODO 考虑安全性
		// 允许请求ip
		String ip = request.getRemoteAddr();
		if (Utils.isEmpty(ip)
				|| !(ip.equals("127.0.0.1") || ip.equals("192.168.1.51")
						|| ip.equals("202.96.155.42") || ip
							.equals("202.105.131.215") || ip.equals("202.96.155.196") || ip.equals("202.96.155.198")) ) {
			result.put("errorCode", "2");
			result.put("info", "非法请求");
			return result;
		}

		try {
			VeVehicleInfo vehicle = veVehicleInfoService.findOne(vehicleInfoId);
			if (vehicle == null) {
				result.put("errorCode", "3");
				result.put("info", "车辆信息不存在");
				return result;
			}
			Map<String, Object> configMap = creditAndTicketManagementService
					.getAllConfigInfo();
			PlUsers vehicleUser = plUsersService.findOne(vehicle.getUserId());
			String vehicleMobile = vehicleUser.getMobile();
			//车主审核通过获得积分
			 creditAndTicketManagementService
				.sendIntegral(
						vehicleUser.getUserId(),
						UserType.owners,
						Long.valueOf(configMap
								.get(ConstantUtil.SEND_VE_FIRST)
								.toString()),
						"车主审核通过 赠送"
								+ configMap
										.get(ConstantUtil.INVITEE_POINT_CONSTANT)
								+ "积分",true);
			// 发push
			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("type", "9");
			pMap.put(
					"msg","敬上"
							+ configMap
									.get(ConstantUtil.SEND_VE_FIRST)
							+ "积分！积分可以抽奖哦");
			xmppService.push(vehicleUser.getMobile() + "",
					JsonUtils.object2Json(pMap), "v_");
			xmppService.push(vehicleUser.getUserId() + "",
					JsonUtils.object2Json(pMap), "v_"); 
			 
			TbSmsRecommend recommend = creditAndTicketManagementService
					.getFirstTbSmsRecommendByMoible(vehicleMobile,0L);
			LOG.info("推荐人：" + JsonUtils.object2Json(recommend));
			
			if (recommend != null) {
				// 邀请人是货主
				if (recommend.getUserType().equals(UserType.cargo)) {
				// 货主增加积分
					creditAndTicketManagementService
							.sendIntegral(
									recommend.getUserId(),
									recommend.getUserType(),
									Long.valueOf(configMap
											.get(ConstantUtil.INVITER_POINT_CONSTANT)
											.toString()),
									"车主审核通过 推荐人增加"
											+ configMap
													.get(ConstantUtil.INVITER_POINT_CONSTANT)
											+ "积分",true);
					
					// 货主增加提现金额
					 
					creditAndTicketManagementService.addBalanceToUser(
					recommend.getUserId(),
					recommend.getUserType(),
					Double.valueOf(configMap.get(
							ConstantUtil.INVITER_BALANCE).toString()),
					recommend.getUserMobile(),
					"车主审核通过 推荐人增加"
							+ configMap
									.get(ConstantUtil.INVITER_BALANCE)
							+ "余额",true);
					// 发push
					Map<String, String> pushMap = new HashMap<String, String>();
					pushMap.put("type", "10");  //跳到我的财富页面
					pushMap.put(
							"msg",
							"手机号"
									+ Common.formatPhoneNumber(vehicleMobile)
									+ "的好友接受您的邀请加入【闪发车】，送上"
									+ configMap
											.get(ConstantUtil.INVITER_POINT_CONSTANT)
									+ "积分和"+configMap
									.get(ConstantUtil.INVITER_BALANCE)+"元现金");
					xmppService.push(recommend.getUserId() + "",
							JsonUtils.object2Json(pushMap), "c_");
					xmppService.push(recommend.getUserMobile() + "",
							JsonUtils.object2Json(pushMap), "c_");
					
					// 货主收藏此车主
					UsersCargoFavorite usersCargoFavorite = new UsersCargoFavorite();
					usersCargoFavorite.setCollectTime(new Date());
					usersCargoFavorite.setUserCargoId(recommend.getUserId());
					usersCargoFavorite.setVehicleInfoId(vehicleInfoId);
					userFavoriteService.save(usersCargoFavorite);
					// 推荐人为审核车主的推荐人
					vehicleUser.setRecommendType(1L);
					vehicleUser.setRecommendId(recommend.getUserId());
					plUsersService.save(vehicleUser);
					// 发短信
					smsSendService
							.sendSms(
									recommend.getUserMobile(),
									"手机号"
											+ Common.formatPhoneNumber(vehicleMobile)
											+ "的好友接受您的邀请加入【闪发车】，送上"
											+ configMap
													.get(ConstantUtil.INVITER_POINT_CONSTANT)
											+ "积分和"+configMap
											.get(ConstantUtil.INVITER_BALANCE)+"元现金");
				}
				// 邀请人是车主
				if (recommend.getUserType().equals(UserType.owners)) {
					// 车主增加积分
					creditAndTicketManagementService.sendIntegral(
							recommend.getUserId(),
							recommend.getUserType(),
							Long.valueOf(configMap.get(
									ConstantUtil.INVITER_VE_POINT).toString()),
							"车主审核通过 推荐车主增加"
									+ configMap
											.get(ConstantUtil.INVITER_VE_POINT)
									+ "积分",true);
					// 推荐人为审核车主的推荐人
					vehicleUser.setRecommendType(2L);
					vehicleUser.setRecommendId(recommend.getUserId());
					plUsersService.save(vehicleUser);
					
					// 发短信
					smsSendService
							.sendSms(
									recommend.getUserMobile(),
									"手机号"
											+ Common.formatPhoneNumber(vehicleMobile)
											+ "的好友接受您的邀请加入【闪发车】，送上"
											+ configMap
													.get(ConstantUtil.INVITER_VE_POINT)
											+ "积分");
					// 发push
					Map<String, String> pushMap = new HashMap<String, String>();
					pushMap.put("type", "9");
					pushMap.put(
							"msg",
							"手机号"
									+ Common.formatPhoneNumber(vehicleMobile)
									+ "的好友接受您的邀请加入【闪发车】啦，敬上"
									+ configMap
											.get(ConstantUtil.INVITER_VE_POINT)
									+ "积分！积分可以抽奖哦");
					xmppService.push(recommend.getUserMobile() + "",
							JsonUtils.object2Json(pushMap), "v_");
					xmppService.push(recommend.getUserId() + "",
							JsonUtils.object2Json(pushMap), "v_");
				}

				// 推荐表里改变状态
				List<TbSmsRecommend> smsList = tbSmsRecommendService
						.queryTbSmsRecommendByMoble(vehicleMobile);
				if (CollectionUtils.isNotEmpty(smsList)) {
					for (TbSmsRecommend tbSms : smsList)
						tbSms.setStatus((long) 1);
					tbSmsRecommendService.save(smsList);
				}

				LOG.info("手机号" + Common.formatPhoneNumber(vehicleMobile)
						+ "的好友接受您的邀请加入【闪发车】，送上" + ConstantUtil.INVITER_POINT
						+ "积分");
			}
			
			
			
			
			
			result.put("errorCode", "0");
			result.put("info", "成功");
			return result;
		} catch (Exception e) {
			result.put("errorCode", "1");
			result.put("info", "服务器忙晕了，请稍后再试！");
			throw new RuntimeException(e);
		}

	}
}
