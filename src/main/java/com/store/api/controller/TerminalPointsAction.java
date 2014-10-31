package com.store.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Common;
import com.store.api.common.Constant;
import com.store.api.common.MD5;
import com.store.api.mysql.entity.BkBooking;
import com.store.api.mysql.entity.BkOrderOffer;
import com.store.api.mysql.entity.BkVoiceOrder;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.PlUsersCargo;
import com.store.api.mysql.entity.RetResult;
import com.store.api.mysql.entity.SmsAuthentication;
import com.store.api.mysql.entity.TbBalanceAction;
import com.store.api.mysql.entity.TbConfigInfo;
import com.store.api.mysql.entity.TbNews;
import com.store.api.mysql.entity.TbSmsRecommend;
import com.store.api.mysql.entity.TbStaff;
import com.store.api.mysql.entity.TbTicketInfo;
import com.store.api.mysql.entity.TbUserBalance;
import com.store.api.mysql.entity.TicketStats;
import com.store.api.mysql.entity.UsersCargoFavorite;
import com.store.api.mysql.entity.VeImage;
import com.store.api.mysql.entity.VeVehicleInfo;
import com.store.api.mysql.entity.enumeration.TicketType;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.BkBookingService;
import com.store.api.mysql.service.BkOrderOfferService;
import com.store.api.mysql.service.BkVoiceOrderService;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.PlUsersService;
import com.store.api.mysql.service.PlusersCargoService;
import com.store.api.mysql.service.SmsAuthenticationService;
import com.store.api.mysql.service.TbBalanceActionService;
import com.store.api.mysql.service.TbConfigInfoService;
import com.store.api.mysql.service.TbNewsService;
import com.store.api.mysql.service.TbSmsRecommendService;
import com.store.api.mysql.service.TbStaffService;
import com.store.api.mysql.service.TbUserBalanceService;
import com.store.api.mysql.service.TicketStatsService;
import com.store.api.mysql.service.UserFavoriteService;
import com.store.api.mysql.service.VeImageService;
import com.store.api.mysql.service.VeVehicleInfoService;
import com.store.api.mysql.service.XmppService;
import com.store.api.session.SessionService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.ConstantUtil;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.TimeUtil;
import com.store.api.utils.Utils;
import com.store.api.utils.security.SecurityUtil;

/**
 * @ClassName: TerminationPointsAction
 * @Description: 积分相关action
 * @author weiwei
 * @date 2014-7-22
 */
@Controller()
@Scope("prototype")
@RequestMapping("/V1/appterminal")
public class TerminalPointsAction extends BaseAction {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Map<String, Object> result = new HashMap<String, Object>();

    @Autowired
    private SmsAuthenticationService smsAuthenticationService;

    @Autowired
    private PlusersCargoService plusersCargoService;

    @Autowired
    private PlUsersService plUsersService;

    @Autowired
    private TbStaffService tbStaffService;

    @Autowired
    private XmppService xmppService;

    @Autowired
    private CreditAndTicketManagementService creditAndTicketManagementService;

    @Autowired
    private TbSmsRecommendService tbSmsRecommendService;

    @Autowired
    private TbUserBalanceService tbUserBalanceService;

    @Autowired
    private TicketStatsService ticketStatsService;

    @Autowired
    private TbConfigInfoService tbConfigInfoService;

    @Autowired
    private TbBalanceActionService tbBalanceActionService;

    @Autowired
    private UserFavoriteService userFavoriteService;

    @Autowired
    private VeVehicleInfoService veVehicleInfoService;
    @Autowired
    private TbNewsService tbNewsService;
    @Autowired
    private BkBookingService bkBookingService;
    @Autowired
    private BkVoiceOrderService bkVoiceOrderService;
    @Autowired
    private BkOrderOfferService bkOrderOfferService;
    @Autowired
    private VeImageService veImageService;
    /**
     * 货主注册
     * 
     * @param mobile
     *            手机号
     * @param verifyCode
     *            短信验证码
     * @param userName
     *            用户名
     * @param passWord
     *            密码
     * @param recommender
     *            推荐人
     * @param lng
     *            经度
     * @param lat
     *            纬度
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/register")
    public Map<String, Object> register(@RequestParam(value = "mobile", required = false, defaultValue = "")
    String mobile, @RequestParam(value = "verifycode", required = false, defaultValue = "")
    String verifyCode, @RequestParam(value = "username", required = false, defaultValue = "")
    String userName, @RequestParam(value = "password", required = false, defaultValue = "")
    String passWord, @RequestParam(value = "recommender", required = false, defaultValue = "")
    String recommender, @RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat) {
        if (!Utils.checkMobile(mobile)) {
            result.put("errorcode", "2");
            result.put("info", "手机号码不合法");
            return result;
        }
        if (Utils.isEmpty(verifyCode)) {
            result.put("errorcode", "3");
            result.put("info", "验证码不能为空");
            return result;
        }
        if (!Utils.isEmpty(userName) && !Utils.checkUserName(userName)) {
            result.put("errorcode", "4");
            result.put("info", "用户名不合法");
            return result;
        }
        // TODO 密码需要加密传输
        if (Utils.isEmpty(passWord) || passWord.length() < 6 || passWord.length() > 24) {
            result.put("errorcode", "5");
            result.put("info", "密码不合法");
            return result;
        }
        if (!Utils.isEmpty(recommender) && !Utils.checkMobile(recommender)) {
            result.put("errorcode", "9");
            result.put("info", "推荐人不存在");
            return result;
        }

        try {
            SmsAuthentication smsAuth = smsAuthenticationService.findByMobileAndStatus(mobile, false);
            if (null == smsAuth || !smsAuth.getAuthCode().equals(verifyCode)) {
                result.put("errorcode", "6");
                result.put("info", "验证码错误");
                return result;
            }
            Long expire = System.currentTimeMillis() - smsAuth.getSendDt().getTime();
            if (expire > 600000) {
                result.put("errorcode", "6");
                result.put("info", "验证码已过期");
                return result;
            }

            // 查询条件中均带有密码不为空的条件，兼容未设置密码的老用户
            boolean mobileFlag = plusersCargoService.existByMobileWithPwdNotNull(mobile, true);
            if (mobileFlag) {
                result.put("errorcode", "7");
                result.put("info", "手机号码已经注册");
                return result;
            }
            // 查询条件中均带有密码不为空的条件，兼容未设置密码的老用户
            if (!Utils.isEmpty(userName)) {
                boolean unameFlag = plusersCargoService.existByUserNameWithPwdNotNull(userName);
                if (unameFlag) {
                    result.put("errorcode", "8");
                    result.put("info", "用户名已存在");
                    return result;
                }
            }

            Map<String, Object> reMap = new HashMap<String, Object>();

            Long recommendId = null;
            Long recommendType = null;
            if (!Utils.isEmpty(recommender)) {
                PlUsersCargo reCargo = plusersCargoService.findByMobile(recommender);
                List<PlUsers> reUsers = plUsersService.findByMobileAndIsvalid(recommender, "Y");
                TbStaff reStaff = tbStaffService.findByStaffTelAndStatus(recommender, true);
                if (null != reStaff) {
                    recommendId = reStaff.getStaffId();
                    recommendType = 1L;
                } else if (null != reUsers && reUsers.size() > 0) {
                    recommendId = reUsers.get(0).getUserId();
                    recommendType = 2L;
                } else if (null != reCargo) {
                    recommendId = reCargo.getUserCargoId();
                    recommendType = 3L;
                } else {
                    result.put("errorcode", "9");
                    result.put("info", "推荐人不存在");
                    return result;
                }
                if (recommender.equals(mobile)) {
                    result.put("errorcode", "9");
                    result.put("info", "抱歉，推荐人不能设置为自己");
                    return result;
                }
            }

            smsAuth.setStatus(true);
            smsAuthenticationService.save(smsAuth);
            String userId = "";
            PlUsersCargo userCargo = plusersCargoService.findByMobile(mobile);

            String tips = "";

            if (null != userCargo) {// 如果有此用户，则更新信息
                if (!Utils.isEmpty(recommender)) {
                    userCargo.setRecommendId(recommendId);
                    userCargo.setRecommendType(recommendType);
                }
                if (!Utils.isEmpty(userName))
                    userCargo.setUserName(userName);
                userCargo.setUserPwd(MD5.encrypt(passWord));
                userCargo.setVersion(getVersionName());
                userCargo.setGpsX(lng);
                userCargo.setGpsY(lat);
                userCargo.setIp(request.getRemoteAddr());
                plusersCargoService.save(userCargo);
                session.setAttribute(Constant.SESSION_PL_USER_CARGO, userCargo);
                userId = userCargo.getUserCargoId().toString();
            } else {// 如无此用户，则新增
                PlUsersCargo userCargoNew = new PlUsersCargo();
                if (!Utils.isEmpty(userName))
                    userCargoNew.setUserName(userName);
                userCargoNew.setUserPwd(MD5.encrypt(passWord));
                userCargoNew.setVersion(getVersionName());
                userCargoNew.setRegVersion(getVersionName());
                userCargoNew.setGpsX(lng);
                userCargoNew.setGpsY(lat);
                userCargoNew.setIp(request.getRemoteAddr());
                userCargoNew.setIsvalid("Y");
                userCargoNew.setMobile(mobile);
                userCargoNew.setMobileValidate(1L);
                userCargoNew.setCreateDt(new Date());
                // 邀请人
                TbSmsRecommend sms = creditAndTicketManagementService.getFirstTbSmsRecommendByMoible(mobile,0L);
                if (null != sms) {
                    recommendId = sms.getUserId();
                    recommendType = 1L;
                    if (UserType.owners == sms.getUserType()) {
                        recommendType = 2L;
                    }
                    reMap.put("recommend_mobile", sms.getMobile());
                }
                if (null != recommendId)
                    userCargoNew.setRecommendId(recommendId);
                if (null != recommendType)
                    userCargoNew.setRecommendType(recommendType);
                plusersCargoService.save(userCargoNew);
                userId = userCargoNew.getUserCargoId().toString();

                reMap.put("username", Utils.isEmpty(userCargoNew.getUserName()) ? "" : userCargoNew.getUserName());
                reMap.put("mobile", userCargoNew.getMobile());
                reMap.put("user_cargo_id", null != userCargoNew.getUserCargoId() ? userCargoNew.getUserCargoId() + "" : "");

                reMap.put("username", Utils.isEmpty(userCargoNew.getUserName()) ? "" : userCargoNew.getUserName());
                reMap.put("mobile", userCargoNew.getMobile());
                reMap.put("user_cargo_id", null != userCargoNew.getUserCargoId() ? userCargoNew.getUserCargoId() + "" : "");

                // 生成新SESSIONID
                String oldSessionId = null;
                if (null != request.getSession().getAttribute(Constant.SESSION_PL_USER_CARGO))
                    oldSessionId = request.getSession().getId();
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.SESSION_PL_USER_CARGO, JsonUtils.object2Json(userCargoNew));
                String sessid = SessionService.getInstance().createSession(userCargoNew.getUserCargoId() + "", SessionService.USER_TYPE_CARGO, map, oldSessionId);
                if (!Utils.isEmpty(sessid))
                    setValueToCookie(Constant.SESSION_NAME, SecurityUtil.encrypt(sessid));

                // 若是车主则自动收藏
                if (null != sms) {
                    if (UserType.owners == sms.getUserType()) {
                        List<VeVehicleInfo> dataList = veVehicleInfoService.findVehicleByUserId(sms.getUserId());
                        if (CollectionUtils.isNotEmpty(dataList)) {
                            UsersCargoFavorite entity = new UsersCargoFavorite();
                            entity.setCollectTime(new Date());
                            entity.setUserCargoId(Long.parseLong(userId));
                            entity.setVehicleInfoId(dataList.get(0).getVehicleInfoId());
                            userFavoriteService.save(entity);
                        }
                    }
                }

                // 注册完后送积分,邀请人和受邀人分别加积分
                RetResult retResult = creditAndTicketManagementService.registrationPlusIntegral(sms, Long.parseLong(userId), mobile, "用户注册送积分", true);
                
                if (null != retResult && "0".equals(retResult.getErrorcode())) {
                    // 更新邀请人状态
                    List<TbSmsRecommend> smsList = tbSmsRecommendService.findByMobileAndStatusOrderBySendTimeAsc(mobile, 0L);
                    if (CollectionUtils.isNotEmpty(smsList)) {
                        for (TbSmsRecommend tbSms : smsList)
                            tbSms.setStatus((long) 1);
                        tbSmsRecommendService.save(smsList);
                    }
                    Map<String, Object> configMap = creditAndTicketManagementService.getAllConfigInfo();
                    Long inviteePoint = Long.parseLong(configMap.get(ConstantUtil.INVITEE_POINT_CONSTANT).toString());// 注册送积分
//                    tips = " 注册成功，赠送" + inviteePoint + "积分请笑纳";
                }
                
                // 注册后送券给受邀人
                // creditAndTicketManagementService.registrationPlusTicket(
                // Long.parseLong(userId), userName, "用户注册送券");
                // TODO 送特殊券
                // creditAndTicketManagementService.registrationPlusSpecialTicket(
                // Long.parseLong(userId), userName);
                
                
                //2014/9/23  货主注册成功之后赠送1000积分
                Map<String, Object> configMap = creditAndTicketManagementService.getAllConfigInfo();
                Long registerPoint = Long.parseLong(configMap.get(ConstantUtil.SEND_POINT_BY_CARGO_REGISTER).toString());
                creditAndTicketManagementService.sendIntegral(userCargoNew.getUserCargoId(), UserType.cargo, registerPoint, "货主注册成功赠送1000积分", true);
                tips = " 注册成功，" + registerPoint + "积分到手！";
                
                
            }
            // 注册XMPP按用户id来推送通知
            LOG.info("Openfire 注册 c_" + userId);
            xmppService.register("c_" + userId);

            result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", reMap);
            result.put("tips", tips);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The register() method invocation exception.", e);
            throw new RuntimeException("The register() method invocation fail.");
        }
    }

    /**
     * 货主查询积分
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryusercredit")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> queryUserCreditByCargo() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (null == user) {
            result.put("errorcode", "1");
            result.put("info", "帐号不存在");
            return result;
        }
        Map<String, String> reMap = creditAndTicketManagementService.getUserCreditInfo(user.getUserCargoId(), UserType.cargo, 0);
        result.put("errorcode", "0");
        result.put("info", "查询用户积分成功");
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 积分兑换券
     * 
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/redeemcredits")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> redeemCreditsByCargo(@RequestParam(value = "credit_num", required = true, defaultValue = "0")
    Integer num) {
        if (null == num || 0 == num) {
            result.put("errorcode", "2");
            result.put("info", "兑换数量不能为空");
            return result;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (null == user) {
            result.put("errorcode", "1");
            result.put("info", "帐号不存在");
            return result;
        }
        try {
            RetResult retResult = creditAndTicketManagementService.redeemCreditsByCargo(user.getUserCargoId(), user.getUserName(), num);
            result.put("errorcode", retResult.getErrorcode());
            result.put("info", retResult.getInfo());
            LOG.info("result" + JsonUtils.object2Json(result));
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The redeemCreditsByCargo() method invocation exception.", e);
            throw new RuntimeException("The redeemCreditsByCargo() method invocation fail.");
        }
        return result;
    }

    /**
     * 券查询接口
     * 
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/querycreditandbalance")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> queryUserCreditAndBalance() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (null == user) {
            result.put("errorcode", "1");
            result.put("info", "帐号不存在");
            return result;
        }
        try {
            // 检测是否有过期的券
            Map<String, Object> reMap = new HashMap<String, Object>();
            // 取出券
            List<TicketStats> ticketStatsList = ticketStatsService.queryUnuserAndInactiveTicketStatsByLessThanStatusAndUserIdAndUserType(user.getUserCargoId(), UserType.cargo);
            reMap.put("ticketList", ticketStatsList);
            TbUserBalance bean = tbUserBalanceService.findByUserIdAndUserType(user.getUserCargoId(), UserType.cargo);
            // 可充值余额
            Long balance = 0L;
            if (null != bean && null != bean.getBalance())
                balance = bean.getBalance().longValue();
            // 可充值的余额
            reMap.put("totalBalance", balance + "");
            // 每次充值的额度大小
            TbConfigInfo config = tbConfigInfoService.queryTbConfigInfoByConfigType("recharge_ratio");
            Integer rechargeRatio = 50;// 每次充值的额度大小，默认50元
            if (null != config && StringUtils.isNotEmpty(config.getConfigValue()))
                rechargeRatio = Integer.parseInt(config.getConfigValue());
            reMap.put("rechargeAmount", rechargeRatio.toString());

            result.put("errorcode", "0");
            result.put("info", "查询用户充值信息成功");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The queryUserCreditAndBalance() method invocation exception.", e);
            throw new RuntimeException("The queryUserCreditAndBalance() method invocation fail.");
        }
        return result;
    }

    /**
     * 充值接口
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("prepaid")
    @Transactional
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> prepaid(@RequestParam(value = "money", required = false)
    BigDecimal money) {
        try {
            PlUsersCargo cargo = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
            if (money == null) {
                result.put("errorcode", "5");
                result.put("info", "充值金额为空");
                return result;
            }
            // 查询余额表余额
            TbUserBalance blance = tbUserBalanceService.findByUserIdAndUserType(cargo.getUserCargoId(), UserType.cargo);
            // 如果没有当前用户的余额信息 新增一个当前用户余额信息 余额为0
            if (blance == null) {
                blance = new TbUserBalance();
                blance.setUserId(cargo.getUserCargoId());
                blance.setUserType(UserType.cargo);
                blance.setBalance(new BigDecimal(0));
            }
            BigDecimal selfMoney = blance.getBalance();
            if (selfMoney == null) {
                selfMoney = new BigDecimal(0);
            }
            if (selfMoney.compareTo(money) < 0) {
                result.put("errorcode", "4");
                result.put("info", "余额不足");
                return result;
            }
            // 余额减去充值金额
            blance.setBalance(selfMoney.subtract(money));
            tbUserBalanceService.save(blance);
            // 重置记录添加数据
            TbBalanceAction tbBalanceAction = new TbBalanceAction();
            tbBalanceAction.setUserId(cargo.getUserCargoId());
            tbBalanceAction.setUserType(UserType.cargo);
            tbBalanceAction.setCreateTime(new Date());
            tbBalanceAction.setMoney(money);
            tbBalanceAction.setActionType(1L); // 1:话费充值
            tbBalanceAction.setStatus(0L);
            tbBalanceAction.setUserName(cargo.getUserName());
            tbBalanceAction.setMobile(cargo.getMobile());

            tbBalanceActionService.save(tbBalanceAction);
            result.put("errorcode", "0");
            result.put("info", "充值成功");
            return result;
        } catch (Exception e) {
            LOG.error("The register() method invocation exception.", e);
            throw new RuntimeException("The prepaid() method invocation fail.");
        }
    }

    /**
     * 提现接口
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("prepaidmoney")
    @Transactional
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> prepaidmoney(@RequestParam(value = "money", required = true, defaultValue = "0")
    Integer money) {
        try {
            PlUsersCargo cargo = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
            if (money == null || money < 100) {
                result.put("errorcode", "5");
                result.put("info", "提现金额不能空且大于100元");
                return result;
            }
            // 查询余额表余额
            TbUserBalance userBalance = tbUserBalanceService.findByUserIdAndUserType(cargo.getUserCargoId(), UserType.cargo);
            RetResult retResult = creditAndTicketManagementService.subBalanceToUser(userBalance, (double) money, cargo.getMobile(), "用户提现"+money+"元");
            if (null != retResult && "0".equals(retResult.getErrorcode())) {
                // 重置记录添加数据
                TbBalanceAction tbBalanceAction = new TbBalanceAction();
                tbBalanceAction.setUserId(cargo.getUserCargoId());
                tbBalanceAction.setUserType(UserType.cargo);
                tbBalanceAction.setCreateTime(new Date());
                tbBalanceAction.setMoney(new BigDecimal(money));
                tbBalanceAction.setActionType(2L); // 1:话费充值 2:提现
                tbBalanceAction.setStatus(0L);
                tbBalanceAction.setUserName(cargo.getUserName());
                tbBalanceAction.setMobile(cargo.getMobile());
                tbBalanceActionService.save(tbBalanceAction);
            }
            result.put("errorcode", retResult.getErrorcode());
            result.put("info", retResult.getInfo());
            return result;
        } catch (Exception e) {
            LOG.error("The register() method invocation exception.", e);
            throw new RuntimeException("The prepaid() method invocation fail.");
        }
    }

    /**
     * 短信邀请通知接口
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("msgInformation")
    @Transactional
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> msgInformation(@RequestParam(value = "jsoninfo", required = false, defaultValue = "")
    String jsoninfo) {
        try {
            PlUsersCargo plUsersCargo = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
            List<Map<String, Object>> list = null;
            list = (List<Map<String, Object>>) JsonUtils.json2Object(jsoninfo, List.class);
            List<TbSmsRecommend> tbSmsRecommendList = new ArrayList<TbSmsRecommend>();
            TbSmsRecommend tbSmsRecommend = null;
            for (Map<String, Object> map : list) {
                tbSmsRecommend = new TbSmsRecommend();
                tbSmsRecommend.setMobile(map.get("m").toString());
                tbSmsRecommend.setUserType(UserType.cargo);
                tbSmsRecommend.setUserMobile(plUsersCargo.getMobile());
                tbSmsRecommend.setUserId(plUsersCargo.getUserCargoId());
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
     * 用户查询有哪些可使用的券
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("queryuserticked")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> queryTicket() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (null == user) {
            result.put("errorcode", "1");
            result.put("info", "帐号不存在");
            return result;
        }
        try {
            List<TbTicketInfo> ticketList = ticketStatsService.findByUserIdAndUserTypeAndTicketTypeAndTicketStatusAndNowGreaterThan(user.getUserCargoId(), UserType.cargo,
                    TicketType.voucher, ConstantUtil.TICKET_STATUS_UNUSED_0);
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("ticketList", ticketList);
            result.put("errorcode", "0");
            result.put("info", "查询用户券信息成功");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The queryTicket() method invocation exception.", e);
            throw new RuntimeException("The queryTicket() method invocation fail.");
        }
        return result;
    }

    /**
     * 查询是否有可使用的券
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("queryusertotalticket")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> queryCountTicket() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (null == user) {
            result.put("errorcode", "1");
            result.put("info", "帐号不存在");
            return result;
        }
        try {
            List<TbTicketInfo> dataList = ticketStatsService.findByUserIdAndUserTypeAndTicketTypeAndTicketStatusAndNowGreaterThan(user.getUserCargoId(), UserType.cargo,
                    TicketType.voucher, ConstantUtil.TICKET_STATUS_UNUSED_0);
            Map<String, Object> reMap = new HashMap<String, Object>();
            String data = "0";
            Long amount = 0L;
            if (null != dataList && !dataList.isEmpty()) {
                data = dataList.size() + "";
                TbTicketInfo info = dataList.get(0);
                if (null != info && null != info.getTicketAmount())
                    amount = info.getTicketAmount();
            }
            reMap.put("totalTicket", data);
            reMap.put("denomination", amount + ""); // 券额度
            result.put("errorcode", "0");
            result.put("info", "查询用户券信息成功");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The queryCountTicket() method invocation exception.", e);
            throw new RuntimeException("The queryCountTicket() method invocation fail.");
        }
        return result;
    }

    /**
     * 货主的用户提现公告
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getcargowithdraw")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> getCargoWithdraw(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageNo, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int size) {
        try {
            Page<TbBalanceAction> page = tbBalanceActionService.findByUserTypeAndActionTypeAndStatus(UserType.cargo, 2L, 0L, pageNo, size);
            List<String> comentList = new ArrayList<String>();
            if (page.hasContent()) {
                for (TbBalanceAction tbBalanceAction : page.getContent()) {
                    if (null != tbBalanceAction.getCreateTime() && null != tbBalanceAction.getMobile() && null != tbBalanceAction.getMoney()) {
                        String comment = "";
                        comment = TimeUtil.getDateString(tbBalanceAction.getCreateTime(), "M月d日") + " 尾号"
                                + tbBalanceAction.getMobile().substring(tbBalanceAction.getMobile().length() - 4) + "的用户成功提现" + tbBalanceAction.getMoney() + "元";
                        comentList.add(comment);
                    }

                }
                result.put("errorcode", "0");
                result.put("info", "成功");
                result.put("total_page", page.getTotalPages() + "");
                result.put("data", comentList);
                return result;
            } else {
                if (page.getTotalPages() - 1 <= pageNo) {
                    result.put("errorcode", "0");
                    result.put("total_page", page.getTotalPages() + "");
                    result.put("data", comentList);
                    if (pageNo == 0) {
                        result.put("info", "");
                    } else {
                        result.put("info", "没有更多记录");
                    }
                }
                return result;
            }
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The getCargoWithdraw() method invocation exception.", e);
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
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> creditToMoneyByCargo(@RequestParam(value = "money_num", required = true, defaultValue = "0")
    Integer money) {
        if (null == money || 0 == money) {
            result.put("errorcode", "7");
            result.put("info", "兑换数量不能为空");
            return result;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        try {
            RetResult retResult = creditAndTicketManagementService.creditToMoney(user.getUserCargoId(), UserType.cargo, money, user.getMobile(), null);
            result.put("errorcode", retResult.getErrorcode());
            result.put("info", retResult.getInfo());
            LOG.info("result" + JsonUtils.object2Json(result));
        } catch (Exception e) {
            result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The creditToMoneyByCargo() method invocation exception.", e);
            throw new RuntimeException("The creditToMoneyByCargo() method invocation fail.");
        }
        return result;
    }

    /**
     * 货主查询积分2
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/querycargocredit")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> queryNewUserCreditByCargo() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (null == user) {
            result.put("errorcode", "1");
            result.put("info", "帐号不存在");
            return result;
        }
        Map<String, String> reMap = creditAndTicketManagementService.getUserCreditInfo(user.getUserCargoId(), UserType.cargo, 1);
        result.put("errorcode", "0");
        result.put("info", "查询用户积分成功");
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }
    
    /**
     * 货主查询提现记录
     */
    @ResponseBody
    @RequestMapping("/withdrawalrecord")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String,Object> withdrawalRecord(
    		@RequestParam(value = "pageno", required = false, defaultValue = "0")int pageNo, 
    		@RequestParam(value = "limit", required = false, defaultValue = "10")int size
    	    ){
    	try {
    		Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
            PlUsersCargo user = (PlUsersCargo) obj;
        	Page<TbBalanceAction> balanceActionPage =tbBalanceActionService.findByUserTypeAndUserIdAndActionType(UserType.cargo, user.getUserCargoId(), 2L, pageNo, size);
        	List<TbBalanceAction> balanceActionList = balanceActionPage.getContent();
        	List<String> reList = new ArrayList<String>();
        	for (TbBalanceAction tbBalanceAction : balanceActionList) {
    			String date = TimeUtil.getDateString(tbBalanceAction.getCreateTime(), "M月dd日 HH:dd");
    			String money= "提现"+tbBalanceAction.getMoney()+"元人民币";
    			String strContent = date+money;
    			reList.add(strContent);
    		}
        	result.put("errorcode", "0");
        	result.put("info", "成功");
        	result.put("data", reList);
        	return result;
		} catch (Exception e) {
			result.put("errorcode", "10");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The withdrawalrecord() method invocation exception.", e);
            return result;
		}
    	
    }
    
    /**
     * 公告列表
     */
    @ResponseBody
    @RequestMapping("/announcelist")
    public String announcelist(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageno, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int limit) {
        try {

            List<TbNews> tbNewsList = tbNewsService.findTopTbNews(1L, new Date(),2L, pageno, limit).getContent();

            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            if (tbNewsList != null && tbNewsList.size() > 0) {
                TbNews tn = null;
                Map<String, Object> m = null;
                if (tbNewsList.size() < limit) {
                    limit = tbNewsList.size();
                }
                for (int i = 0; i < limit; i++) {
                    tn = tbNewsList.get(i);
                    m = new HashMap<String, Object>();
                    // 给原图
                    String img = "";
                    if (!Utils.isEmpty(tn.getCoverimg())) {
                        img = tn.getCoverimg().replaceFirst("thumb/thumb_", "");
                    }
                    m.put("id", tn.getId());
                    m.put("title", tn.getTitle());
                    m.put("coverimg", img);
                    m.put("ishot", tn.getIshot());
                    m.put("digest", tn.getDigest());
                    m.put("content", tn.getContent());
                    m.put("updatetime", tn.getCreatetime().getTime() / 1000);
                    m.put("url", Constant.WEBSITE_URL + "/Home/mobileannounceinfo/id/" + tn.getId());
                    resultList.add(m);
                }
                return JsonUtils.resultJson(0, "成功", resultList);
            } else {
                return JsonUtils.resultJson(2, "公告信息已加载完！", new ArrayList<Object>());
            }
        } catch (Exception e) {
            LOG.error("The announcelist() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
        }
    }

    /*****
     * 公告详情
     * 
     * @return Json
     * 
     */

    @ResponseBody
    @RequestMapping("/announcedetail")
    public String announcedetail(@RequestParam(value = "announce_id", required = false, defaultValue = "0")
    Long announce_id) {

        try {
            TbNews news = tbNewsService.findOne(announce_id);
            if (null != news) {
                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("id", news.getId());
                m.put("title", news.getTitle());
                m.put("coverimg", news.getCoverimg());
                m.put("ishot", news.getIshot());
                m.put("digest", news.getDigest());
                m.put("content", news.getContent());
                m.put("updatetime", TimeUtil.getDateString(news.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
                m.put("url", Constant.WEBSITE_URL + "/Home/mobileannounceinfo/id/" + news.getId());
                resultList.add(m);
                return JsonUtils.resultJson(0, "成功", resultList);
            } else {
                return JsonUtils.resultJson(2, "公告信息不存在", new ArrayList<Object>());
            }
        } catch (Exception e) {
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
        }
    }

    /*****************************************************************
     * 公告通知(首页滚动)
     * 
     * @return Json
     * 
     */
    @ResponseBody
    @RequestMapping("/notice")
    public String notice() {
        try {
            List<TbNews> tbNewsList = tbNewsService.findTopTbNews(1L, new Date(),2L, 0, 3).getContent();
            List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
            if (tbNewsList != null && tbNewsList.size() > 0) {
                TbNews tn = null;
                Map<String, String> m = null;
                int limit = 3;
                if (tbNewsList.size() < limit) {
                    limit = tbNewsList.size();
                }
                for (int i = 0; i < limit; i++) {
                    tn = tbNewsList.get(i);
                    m = new HashMap<String, String>();
                    m.put("title", tn.getTitle());
                    m.put("url", Constant.WEBSITE_URL + "/Home/mobileannounceinfo/id/" + tn.getId());
                    resultList.add(m);
                }
                return JsonUtils.resultJson(0, "成功", resultList);
            } else {
                return JsonUtils.resultJson(2, "暂无公告", resultList);
            }
        } catch (Exception e) {
            LOG.error("The notice() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
        }
    }
    
    
    
    
    
    /**
     * 客户端车型更新
     */
    @ResponseBody
    @RequestMapping("/vehiclemodellist")
    public Map<String,Object> vehicleModelList(){
    	
    	
    	result.put("errorcode", "0");
    	result.put("info", "成功");
    	result.put("data", Common.getCarType());
    	return result;
    }
    
    /**
     * 货主取消订单
     */
    @ResponseBody
    @RequestMapping("/cancelbooking")
    @Transactional
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String,Object> cancelBooking(
    		@RequestParam(value = "booking_id", required = false, defaultValue = "0")
    	    Long bookingId
    		){
    	try {
			
    		BkBooking booking = bkBookingService.findOne(bookingId);
    		if(booking == null ){
    			result.put("errorcode", "3");
    			result.put("info", "订单不存在！");
    			return result;
    		}
    		
    		if(booking.getBookingStatus() != null && booking.getBookingStatus() > 2){
    			result.put("errorcode", "4");
    			result.put("info", "开始运货后的订单不能取消！");
    			return  result;
    		}
    		//订单取消
    		booking.setBookingStatus(0L);
    		booking.setIsvalid(false);
    		booking.setCancelTime(new Date());
    		booking.setCancelType(2L); //1表示车主取消 2表示货主取消   
    		booking.setRemarks("货主取消");
            bkBookingService.save(booking);
            BkVoiceOrder voiceOrder = null;
            if(null != booking.getVoiceId() && 0 != booking.getVoiceId()){
            	voiceOrder = bkVoiceOrderService.findOne(booking.getVoiceId());
                if(voiceOrder != null && voiceOrder.getOrderType() == 1L){
                	//如果是文本下单 修改报价表里状态
                	 List<BkOrderOffer> offerList = bkOrderOfferService.findByVehicleInfoIdAndOrderId(booking.getVehicleInfoId(), booking.getVoiceId());
                	 if(null != offerList && offerList.size()>0){
                		 BkOrderOffer offer = offerList.get(0);
                		 offer.setStatus(2L); //0：未选 1：选他 2：取消选他
                		 bkOrderOfferService.save(offer);
                	 }
                }
            }
            // 如果有优惠券重新置为未使用
            if( null != booking.getTicketNo() ){
            	creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(booking.getTicketNo(), booking.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_UNUSED_0, true);
                LOG.info(booking.getTicketNo() + "  优惠券置为未使用！");
            }
            
            
            
            // 通知车主 货主已经取消订单 发送push
            String msg = "抱歉，货主已经取消订单，点击查看订单详情。【闪发车】";
            LinkedHashMap<String, Object> contentMap = new LinkedHashMap<String, Object>();
            PlUsers pluser = plUsersService.findOne(booking.getBookingToUsersId());
            String vehicleMobile = pluser.getMobile();
            contentMap.put("type", 1);
            contentMap.put("booking_id", booking.getBookingNo() + "");
            contentMap.put("booking_status", 0);
            contentMap.put("msg", msg);
            xmppService.push(vehicleMobile , JsonUtils.object2Json(contentMap), "v_");
            
            //返回订单类型
            Map<String,Object> reMap = new HashMap<String, Object>();
            if(null == booking.getVoiceId() || 0L == booking.getVoiceId() ){
            	reMap.put("booking_type", "map");
            }else {
            	if(null != voiceOrder.getOrderType() && 1L == voiceOrder.getOrderType()){
            		reMap.put("booking_type", "text");
            	}else if (null != voiceOrder.getOrderType() && 2L == voiceOrder.getOrderType()) {
            		reMap.put("booking_type", "map");
            	}else if (null != voiceOrder.getOrderType() && 0L == voiceOrder.getOrderType()) {
            		reMap.put("booking_type", "voice");
            	}else {
            		reMap.put("booking_type", "other");
            	}
            }
            result.put("errorcode", "0");
            result.put("info", "取消成功");
            result.put("data", reMap);
            return  result;
		} catch (Exception e) {
			LOG.error("The cancelbooking() method invocation exception.", e);
			result.put("errorcode", "5");
			result.put("info", "服务器忙晕了，请稍后再试！");
			return result;
		}
    	
    }
    
    
    
    
    
    /**
     * @param vehicleId
     * @return
     * 金牌车主简介
     */
    @ResponseBody
    @RequestMapping("/getintroduce")
    public Map<String,Object> getIntroduce(
    @RequestParam(value = "vehicle_id", required = false, defaultValue = "0")
    Long vehicleId){
    	String content = "test";
    	Map<String,Object> rep = new HashMap<String, Object>();
    	if(vehicleId ==0 || vehicleId == null){
    		result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", content);
    	}else{
    		List<VeImage> images = veImageService.findByVehicleId(vehicleId);
    		rep.put("has_identy", "1");//此处三个证件时必须的
    		rep.put("has_licence", "1");
    		rep.put("has_driver", "1");
    		if(images != null && images.size() >0 ){
    			VeImage image = images.get(0);
    			rep.put("has_operation", "0");
    			rep.put("has_job", "0");
    			if(!Utils.isEmpty(image.getOperationCertificateImg())){
    				rep.put("has_operation", "1");//1是有证件 0是没有证件
    			}
    			if(!Utils.isEmpty(image.getJobLicenses())){
    				rep.put("has_job", "1");
    			}
    		}
    		result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", rep);
    	}
    	return result;
    }
    
    
    
    
}