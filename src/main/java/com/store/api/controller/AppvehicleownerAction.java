package com.store.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.StringUtils;
import com.store.api.common.Common;
import com.store.api.common.Constant;
import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.UploadGpsGroup;
import com.store.api.mongo.entity.VeUploadGpsMG;
import com.store.api.mongo.entity.VehicleVersion;
import com.store.api.mongo.service.ContactsService;
import com.store.api.mongo.service.VeUploadGpsMGService;
import com.store.api.mongo.service.VehicleVersionService;
import com.store.api.mysql.entity.BkBooking;
import com.store.api.mysql.entity.BkOrderOffer;
import com.store.api.mysql.entity.BkVoiceOrder;
import com.store.api.mysql.entity.BkVoicePhoto;
import com.store.api.mysql.entity.BkVoiceReceived;
import com.store.api.mysql.entity.OfferList;
import com.store.api.mysql.entity.OfferListVo;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.PlUsersCargo;
import com.store.api.mysql.entity.SmsAuthentication;
import com.store.api.mysql.entity.TbNews;
import com.store.api.mysql.entity.TbStaff;
import com.store.api.mysql.entity.VeBlockInfo;
import com.store.api.mysql.entity.VeStatus;
import com.store.api.mysql.entity.VeUploadGps;
import com.store.api.mysql.entity.VeVehicleInfo;
import com.store.api.mysql.entity.VeVehicleModifyLog;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.entity.procedure.OperationArea;
import com.store.api.mysql.entity.procedure.TopOrder;
import com.store.api.mysql.entity.procedure.Ve_vehicle_info_users_for_detail;
import com.store.api.mysql.entity.procedure.VehicleComment;
import com.store.api.mysql.entity.procedure.VoiceListDetail;
import com.store.api.mysql.entity.search.SearchUpdateVehicleInfoVo;
import com.store.api.mysql.entity.webService.VeUpdateReturnVo;
import com.store.api.mysql.service.BkBookingService;
import com.store.api.mysql.service.BkOrderOfferService;
import com.store.api.mysql.service.BkVoiceOrderService;
import com.store.api.mysql.service.BkVoicePhotoService;
import com.store.api.mysql.service.BkVoiceReceivedService;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.NativeService;
import com.store.api.mysql.service.PlUsersService;
import com.store.api.mysql.service.PlusersCargoService;
import com.store.api.mysql.service.ProcedureService;
import com.store.api.mysql.service.SearchEngineService;
import com.store.api.mysql.service.SmsAuthenticationService;
import com.store.api.mysql.service.TbNewsService;
import com.store.api.mysql.service.TbStaffService;
import com.store.api.mysql.service.UserFavoriteService;
import com.store.api.mysql.service.VeBlockInfoService;
import com.store.api.mysql.service.VeStatusService;
import com.store.api.mysql.service.VeUploadGpsService;
import com.store.api.mysql.service.VeVehicleInfoService;
import com.store.api.mysql.service.VeVehicleModifyLogService;
import com.store.api.mysql.service.XmppService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.ConstantUtil;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Method;
import com.store.api.utils.TimeUtil;
import com.store.api.utils.UploadUtils;
import com.store.api.utils.Utils;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/appvehicleowner")
public class AppvehicleownerAction extends BaseAction {

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private VeVehicleInfoService veVehicleInfoService;

    @Autowired
    private VeStatusService veStatusService;

    @Autowired
    private PlUsersService plUsersService;

    @Autowired
    private VeVehicleModifyLogService veVehicleModifyLogService;

    @Autowired
    private VeBlockInfoService veBlockInfoService;

    @Autowired
    private VeUploadGpsService veUploadGpsService;

    @Autowired
    private BkVoiceOrderService bkVoiceOrderService;

    @Autowired
    private BkVoicePhotoService bkVoicePhotoService;

    @Autowired
    private BkBookingService bkBookingService;

    @Autowired
    private BkOrderOfferService bkOrderOfferService;

    @Autowired
    private NativeService nativeService;

    @Autowired
    private BkVoiceReceivedService bkVoiceReceivedService;

    @Autowired
    private VehicleVersionService vehicleVersionService;

    @Autowired
    private XmppService xmppService;

    @Autowired
    private PlusersCargoService plUsersCargoUsersService;

    @Autowired
    private SmsAuthenticationService smsAuthenticationService;

    @Autowired
    private TbNewsService tbNewsService;

    @Autowired
    private UserFavoriteService userFavoriteService;

    @Autowired
    private PlusersCargoService plusersCargoService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private TbStaffService tbStaffService;

    @Autowired
    private CreditAndTicketManagementService creditAndTicketManagementService;

    @Autowired
    private VeUploadGpsMGService veUploadGpsMGService;

    @Autowired
    private SearchEngineService searchEngineService;

    /***************************************************************
     * 上报车辆状态
     * 
     * @param status
     *            车辆状态
     * @return Json
     * @throws Exception
     * 
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/reporttruckstatus")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String reporttruckstatus(@RequestParam(value = "status", required = false, defaultValue = "0")
    Long status) throws Exception {
        if (0 != status && 1 != status) {
            return JsonUtils.resultJson(2, "车辆状态错误", null);
        }
        VeStatus vestatus = new VeStatus();
        try {
            // 从session获得userId;
            Long user_id = null;
            PlUsers plUsers = (PlUsers) session.getAttribute(Constant.SESSION_PL_USER);
            user_id = plUsers.getUserId();
            List<VeVehicleInfo> list = veVehicleInfoService.findVehicleByUserId(user_id);
            vestatus.setVeStatus(status);
            vestatus.setModelId(list.get(0).getModelId());
            vestatus.setUpdateDt(new Date());
            vestatus.setIsvalid("Y");
            // 更新车辆状态表
            List<VeStatus> veStatusList = null;
            veStatusList = veStatusService.findByUserId(user_id);
            if (veStatusList.size() > 0) { // 如果存在记录
                VeStatus vs = veStatusList.get(0);
                vs.setVeStatus(status); // 更改状态
                veStatusService.save(vs);
            } else {
                vestatus.setVehicleInfoId(list.get(0).getVehicleInfoId());
                vestatus.setUserId(user_id);
                veStatusService.save(vestatus);
            }
            // 更新搜索引擎
            // StringBuffer sbf = new StringBuffer();
            // sbf.append("{__Request:\"Update\" ,__Collection:\"Vehicle\",__Fields:{");
            // sbf.append(" _id:").append(list.get(0).getVehicleInfoId()).append(",");
            // sbf.append(" State:").append(status);
            // sbf.append("} }");
            // LOG.info("SE requestJson is :" + sbf.toString());
            // String webserviceAddress = Constant.VEHICLE_WEBSERVICE_URL;
            // String nameSpace = Constant.COSMOS_NAMESPACE;
            // Object[] params = new Object[] { sbf.toString() };
            // Class[] classs = new Class[] { String.class };
            // String result = wsdlService.webserviceCall(webserviceAddress,
            // nameSpace, "request", params, classs);
            // LOG.info("SE returnJson is " + result);
            // VeUpdateReturnVo vo = (VeUpdateReturnVo)
            // JsonUtils.json2Object(result, VeUpdateReturnVo.class);

            SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
            seVo.setVehicleId(list.get(0).getVehicleInfoId());
            seVo.setStatus(status);
            VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);

            if (!"Success".equals(vo.getResult())) {
                return JsonUtils.resultJson(3, "", null);
            } else {
                return JsonUtils.resultJson(0, "上报成功", null);
            }

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            throw new RuntimeException();
        }

    }

    /****************************************************************
     * 车主上报订单状态
     * 
     * @param status
     *            订单状态
     * @param booking_id
     *            订单id
     * @return Json
     * 
     */

    @ResponseBody
    @RequestMapping("/reportservicestatus")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String reportservicestatus(
    		@RequestParam(value = "status", required = false, defaultValue = "0")
    String status, 
    		@RequestParam(value = "booking_id", required = false, defaultValue = "0")
    Long bookingNo,
    		@RequestParam(value = "lat", required = false, defaultValue = "0")
    String clientLat, 
    	    @RequestParam(value = "lng", required = false, defaultValue = "0")
    String   clientLng
    		) {

        if (!"0".equals(status) && !"2".equals(status) && !"3".equals(status) && !"4".equals(status)) {
            return JsonUtils.resultJson(2, "订单状态错误", null);
        }
        try {
        	Date now = new Date();
            // 从session获得userId
            Long user_id = null;
            Object obj = session.getAttribute(Constant.SESSION_PL_USER);
            PlUsers plUsers = null;
            if (obj != null) {
                plUsers = (PlUsers) obj;
                user_id = plUsers.getUserId();
            }

            BkBooking bk = bkBookingService.findOne(bookingNo);
            if (bk == null) {
                return JsonUtils.resultJson(3, "订单不存在", null);
            }
            if (!bk.getBookingToUsersId().equals(user_id)) {
                return JsonUtils.resultJson(3, "订单不存在", null);
            }
            String bookingStatus = bk.getBookingStatus() + "";
            String booking_number = bk.getOrderNumber();
            if (("0".equals(status) || "2".equals(status)) && !"1".equals(bookingStatus)) {
                return JsonUtils.resultJson(4, "订单不允许修改", null);
            }
            if ("3".equals(status) && !"2".equals(bookingStatus)) {
                return JsonUtils.resultJson(4, "订单不允许修改", null);
            }
            if ("4".equals(status) && !"3".equals(bookingStatus)) {
                return JsonUtils.resultJson(4, "订单不允许修改", null);
            }
            // 查询车主还有没有未完成的运货中的订单
            if ("3".equals(status)) {
                String count = bkBookingService.findByBookingToUsersIdAndBookingStatus(user_id, Long.valueOf(status));
                if (Integer.valueOf(count) > 0) {
                    return JsonUtils.resultJson(4, "亲，请先完成正在“运货中”的订单", null);
                }
            }

            PlUsers puList = plUsersService.findOne(user_id);
            List<VeVehicleInfo> vviList = veVehicleInfoService.findVehicleByUserId(user_id);
            String ve_plates = "";
            String ve_realName = "";
            if (puList != null) {
                ve_realName = puList.getUserName();
            }
            if (vviList.size() > 0) {
                ve_plates = vviList.get(0).getVePlates();
            }
            char statu = status.charAt(0);
            String msg = "";
            switch (statu) {
            case '0':
                msg = "抱歉，车牌号为" + ve_plates + "的师傅此次无法为您提供服务，敬请谅解，请重新语音叫车或联系客服400-9191-365，点击查看订单详情。【闪发车】";
                bk.setCancelTime(now);
                bk.setIsvalid(false);
                bk.setCancelType(1L); //1表示车主取消 2表示货主取消
                // 优惠券重新置为未使用
                if (!Utils.isEmpty(bk.getTicketNo())) {
                    creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(bk.getTicketNo(), bk.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_UNUSED_0,
                            true);
                }
                //改变 报价表里状态 
                if(bk.getVoiceId() != null  && bk.getVoiceId() != 0 ){
                	List<BkOrderOffer> offerList = bkOrderOfferService.findByVehicleInfoIdAndOrderId(bk.getVehicleInfoId(), bk.getVoiceId()); 
                	if(offerList != null && offerList.size()>0){
                		BkOrderOffer offer = offerList.get(0);
                		offer.setStatus(3L);  //0：未选 1：选他 2：货主取消3：车主取消
                		bkOrderOfferService.save(offer);
                	}
                }
                
                break;
            case '2':
                msg = "车牌号为" + ve_plates + "的师傅将为您提供服务，点击查看订单详情。【闪发车】";
                bk.setConfirmTime(now);
                bk.setVehicleImei(getImei());
                break;
            case '3':
                msg = "订单号为" + booking_number + "的订单已开始运货，请与" + ve_realName + "保持联系。【闪发车】";
                bk.setFreightTime(now);
                break;
            case '4':
                msg = ve_realName + "已将货物送到目的地，请尽快确认，点击查看订单详情。【闪发车】";
                bk.setArriveTime(now);
                break;
            }
            bk.setBookingStatus(Long.parseLong(statu + ""));
            bkBookingService.save(bk);
            //上报订单状态的同时  上传一次坐标  （兼容以前 坐标不上传 坐标没上传为0就不做处理） 存到mong mysql
            if(clientLng != null && !"0".equals(clientLng) && clientLat != null && !"0".equals(clientLat)){
            	 //更新mysql
                VeUploadGps vuGps = new VeUploadGps();
                vuGps.setLongitude(clientLng);
                vuGps.setLatitude(clientLat);
                vuGps.setVeModel(plUsers.getMobile());
                vuGps.setUptime(now);
                veUploadGpsService.save(vuGps);
                //更新mongo
                VeUploadGpsMG gpsMg = new VeUploadGpsMG();
                gpsMg.setLng(clientLng);
                gpsMg.setLat(clientLat);
                gpsMg.setUserId(plUsers.getUserId());
                gpsMg.setVehicleId(bk.getVehicleInfoId());
                gpsMg.setMobile(plUsers.getMobile());
                gpsMg.setUptime(now.getTime());
                veUploadGpsMGService.save(gpsMg);
                //更新搜索引擎
                SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
                Double[] d = new Double[2];
                d[0] = Double.valueOf(clientLng);
                d[1] = Double.valueOf(clientLat);
                seVo.setVehicleId(bk.getVehicleInfoId());
                seVo.setLocation(d);
                seVo.setLastTime(now.getTime());
                searchEngineService.updateVehilceInfo(seVo);
                
            }
            // 通知货主，车主已经确认订单
            LinkedHashMap<String, Object> contentMap = new LinkedHashMap<String, Object>();
            contentMap.put("type", "1");
            contentMap.put("booking_id", "" + bookingNo);
            contentMap.put("booking_status", status);
            contentMap.put("msg", msg);
            xmppService.push(bk.getUserCargoId() + "", JsonUtils.object2Json(contentMap), "c_");

            // 新增返回订单的目的地坐标
            // 开始运货的时候
            Map<String, String> resultMap = new HashMap<String, String>();
            if (bk.getVoiceId() != null && bk.getVoiceId() != 0) {
                BkVoiceOrder voiceOrder = bkVoiceOrderService.findOne(bk.getVoiceId());
                if (voiceOrder != null) {
                    Double lng = voiceOrder.getToLongitude();
                    Double lat = voiceOrder.getToLatitude();
                    resultMap.put("toLng", lng + "");
                    resultMap.put("toLat", lat + "");
                }
            }

            return JsonUtils.resultJson(0, "上报成功", resultMap);

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(5, "服务器忙晕啦，请稍候再试", null);
        }
    }

    /**
     * 车主取消订单
     */
    @ResponseBody
    @RequestMapping("/cancelorder")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String cancelorder(@RequestParam(value = "booking_no", required = false, defaultValue = "0")
    Long bookingNo, @RequestParam(value = "cancel_msg", required = false, defaultValue = "")
    String cancel_msg) {
        // 从session获得userId
        Long user_id = 0L;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers plUsers = null;
        if (obj != null) {
            plUsers = (PlUsers) obj;
            user_id = plUsers.getUserId();
        }
        try {
            BkBooking bk = bkBookingService.findOne(bookingNo);

            if (bk == null) {
                return JsonUtils.resultJson(3, "订单不存在", null);
            }
            if (!bk.getBookingToUsersId().equals(user_id)) {
                return JsonUtils.resultJson(3, "订单不存在", null);
            }
            // 获取车牌号
            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            VeVehicleInfo ve = veVehicleInfoService.findOne(vehicleId);
            String vePlate = ve.getVePlates();

            // 订单状态置为0 0 为取消
            bk.setBookingStatus(0L);
            bk.setIsvalid(false);
            bk.setCancelTime(new Date());
            // 设置取消原因
            bk.setCancelType(1L); //1表示车主取消 2表示货主取消
            bk.setRemarks(cancel_msg);
            bkBookingService.save(bk);

            //改变 报价表里状态 
            if(  null != bk.getVoiceId()   && bk.getVoiceId() != 0 ){
            	List<BkOrderOffer> offerList = bkOrderOfferService.findByVehicleInfoIdAndOrderId(vehicleId, bk.getVoiceId()); 
            	if(offerList != null && offerList.size()>0){
            		BkOrderOffer offer = offerList.get(0);
            		offer.setStatus(3L);  //车主取消
            		bkOrderOfferService.save(offer);
            	}
            }
            
            // 优惠券重新置为未使用
            creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(bk.getTicketNo(), bk.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_UNUSED_0, true);
            LOG.info(bk.getTicketNo() + "  优惠券置为未使用！");

            String msg = "抱歉，车牌号为" + vePlate + "的师傅因" + cancel_msg + "的原因取消订单，点击查看订单详情。【闪发车】";
            // 通知货主已经取消订单
            // 获取货主信息
            LinkedHashMap<String, Object> contentMap = new LinkedHashMap<String, Object>();
            PlUsersCargo cargo = plusersCargoService.findOne(bk.getUserCargoId());
            Long userCargoId = cargo.getUserCargoId();
            contentMap.put("type", 1);
            contentMap.put("booking_id", bk.getBookingNo() + "");
            contentMap.put("booking_status", 0);
            contentMap.put("msg", msg);
            xmppService.push(userCargoId + "", JsonUtils.object2Json(contentMap), "c_");
            return JsonUtils.resultJson(0, "上报成功", null);
        } catch (Exception e) {
            return JsonUtils.resultJson(3, "服务器忙晕了 ，请稍后再试", null);
        }

    }

    /********************************************************************
     * 车辆信息更新
     * 
     * @return Json
     * 
     */

    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/updatevehicleinfo")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String updatevehicleinfo(@RequestParam(value = "p_gps_x", required = false, defaultValue = "0")
    Double p_gps_x, @RequestParam(value = "p_gps_y", required = false, defaultValue = "0")
    Double p_gps_y, @RequestParam(value = "p_resident_id", required = false, defaultValue = "0")
    Long p_resident_id, @RequestParam(value = "p_resident_desc", required = false, defaultValue = "")
    String p_resident_desc, @RequestParam(value = "p_block_1_from", required = false, defaultValue = "null")
    Long p_block_1_from, @RequestParam(value = "p_block_2_from", required = false, defaultValue = "null")
    Long p_block_2_from, @RequestParam(value = "p_block_3_from", required = false, defaultValue = "null")
    Long p_block_3_from, @RequestParam(value = "p_block_4_from", required = false, defaultValue = "null")
    Long p_block_4_from, @RequestParam(value = "p_block_1_to", required = false, defaultValue = "null")
    Long p_block_1_to, @RequestParam(value = "p_block_2_to", required = false, defaultValue = "null")
    Long p_block_2_to, @RequestParam(value = "p_block_3_to", required = false, defaultValue = "null")
    Long p_block_3_to, @RequestParam(value = "p_block_4_to", required = false, defaultValue = "null")
    Long p_block_4_to, @RequestParam(value = "p_cargo_list", required = false, defaultValue = "")
    String p_cargo_list, @RequestParam(value = "p_user_name", required = false, defaultValue = "")
    String p_user_name, @RequestParam(value = "p_comment", required = false, defaultValue = "")
    String p_comment, @RequestParam(value = "p_idcard", required = false, defaultValue = "")
    String p_idcard, @RequestParam(value = "p_vin_number", required = false, defaultValue = "")
    String p_vin_number, @RequestParam(value = "p_ve_user_photo", required = false, defaultValue = "")
    MultipartFile p_ve_user_photo, @RequestParam(value = "staff_tel", required = false, defaultValue = "")
    String p_staff_moble) {

        // 从session获取车主手机号码

        String p_old_mobile = null;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers plUsers = null;
        if (obj != null) {
            plUsers = (PlUsers) obj;
            p_old_mobile = plUsers.getMobile();
        }

        String p_mobile = p_old_mobile;

        if (!Utils.checkMobile(p_old_mobile)) {
            return JsonUtils.resultJson(2, "旧的手机号码错误", null);
        }
        if (!Utils.checkMobile(p_mobile)) {
            return JsonUtils.resultJson(3, "新的手机号码错误", null);
        }
        if ((p_resident_id == null || p_resident_id == 0) || "".equals(p_resident_desc)) {
            return JsonUtils.resultJson(4, "驻车地为空", null);
        }
        if ((null == p_gps_x || 0 == p_gps_x) && (null == p_gps_y || 0 == p_gps_y)) {
            return JsonUtils.resultJson(5, "驻车地经纬度为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_cargo_list)) {
            return JsonUtils.resultJson(6, "擅长货类为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_user_name)) {
            return JsonUtils.resultJson(7, "司机姓名为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_vin_number)) {
            return JsonUtils.resultJson(10, "车架号为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_idcard)) {
            return JsonUtils.resultJson(11, "身份证号码为空", null);
        }
        try {
            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            VeVehicleInfo vehicle = veVehicleInfoService.findOne(vehicleId);
            if (null == vehicle || null == vehicle.getVeAuthorise() || vehicle.getVeAuthorise() != 0) {
                return JsonUtils.resultJson(12, "未审核车辆无法修改", null);
            }
            if (!Utils.isEmpty(p_staff_moble)) {

                // 去掉空格神马的
                p_staff_moble = p_staff_moble.replaceAll(" ", "");

                TbStaff staff = tbStaffService.findByStaffTelAndStatus(p_staff_moble, true);
                if (null == staff) {
                    staff = tbStaffService.findOne(Long.valueOf(p_staff_moble));

                }
                if (null == staff || !staff.getStaffStatus()) {
                    return JsonUtils.resultJson(11, "推荐人不是业务员", null);
                }

                vehicle.setStaffId(staff.getStaffId());
                if (!veVehicleInfoService.save(vehicle)) {
                    return JsonUtils.resultJson(3, "服务器忙晕了  请稍后再试", null);
                }
            }

            List<PlUsers> plUsers_list = plUsersService.findByMobileAndIsvalid(p_old_mobile, "Y");
            Long user_id = plUsers_list.get(0).getUserId();
            // 根据userid 查询车辆信息
            List<VeVehicleInfo> vehicleInfo_List = veVehicleInfoService.findVehicleByUserId(user_id);
            VeVehicleInfo ve_info = vehicleInfo_List.get(0);
            // 判断车辆信息是否已经有人车合照
            // 库里没有上传人车合照
            String hezhaoUrl = null;
            if (StringUtils.isNullOrEmpty(ve_info.getVeUserPhotoFile())) {
                if (p_ve_user_photo == null) {
                    return JsonUtils.resultJson(12, "请上传人车合照", null);
                }
                String basePath = Constant.PHOTO_PATH;
                String fullPath = Utils.buildFilePath(basePath);

                // 上传人车合照
                File file = new File(fullPath);
                // 如果文件夹不存在则创建
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdirs();
                }
                File newVeUserPhoto = new File(fullPath + "/" + p_ve_user_photo.getOriginalFilename());
                p_ve_user_photo.transferTo(newVeUserPhoto);
                UploadUtils.uploadImage1(fullPath, newVeUserPhoto, p_ve_user_photo.getContentType(), p_ve_user_photo.getOriginalFilename());
                UploadUtils.pressImage(Constant.WATER_FILE_PATH + "water1.png", fullPath + "/" + p_ve_user_photo.getOriginalFilename(), 5);
                UploadUtils.pressImage(Constant.WATER_FILE_PATH + "water2.png", fullPath + "/" + p_ve_user_photo.getOriginalFilename(), 1);
                hezhaoUrl = Utils.buildFilePath(Constant.PHOTO_PATH_URI_PRE) + "" + p_ve_user_photo.getOriginalFilename();
            }

            // 如果第一次修改，保存原来的信息到日志表
            List<VeVehicleModifyLog> veVehicleModifyLog = veVehicleModifyLogService.findByVehicleInfoId(ve_info.getVehicleInfoId());
            VeVehicleModifyLog vmLog = null;
            if (null == veVehicleModifyLog || veVehicleModifyLog.size() < 1) {
                vmLog = new VeVehicleModifyLog();
                vmLog.setVehicleInfoId(ve_info.getVehicleInfoId());
                vmLog.setBrandId(ve_info.getBrandId());
                vmLog.setModelId(ve_info.getModelId());
                vmLog.setVePlates(ve_info.getVePlates());
                vmLog.setCargoTypeList(ve_info.getCargoTypeList());
                vmLog.setVeResidentX(ve_info.getVeResidentX());
                vmLog.setVeResidentY(ve_info.getVeResidentY());
                vmLog.setVeResidentAddressId(ve_info.getVeResidentAddressId());
                vmLog.setVeResidentDesc(ve_info.getVeResidentDesc());
                vmLog.setHidePlates(ve_info.getHidePlates());
                vmLog.setUserName(plUsers_list.get(0).getUserName());
                vmLog.setMobile(p_old_mobile);
                List<VeBlockInfo> blockInfoList = veBlockInfoService.findByVehicleInfoId(ve_info.getVehicleInfoId());
                int k = 1;
                for (VeBlockInfo block : blockInfoList) {
                    if (k == 1) {
                        vmLog.setFromAddress1Id(block.getFromAddressId());
                        vmLog.setToAddress1Id(block.getToAddressId());
                    }
                    if (k == 2) {
                        vmLog.setFromAddress2Id(block.getFromAddressId());
                        vmLog.setToAddress2Id(block.getToAddressId());
                    }
                    if (k == 3) {
                        vmLog.setFromAddress3Id(block.getFromAddressId());
                        vmLog.setToAddress3Id(block.getToAddressId());
                    }
                    if (k == 4) {
                        vmLog.setFromAddress4Id(block.getFromAddressId());
                        vmLog.setToAddress4Id(block.getToAddressId());
                    }
                    k++;
                }
                vmLog.setComment(ve_info.getComment());
                vmLog.setModifyUserId(0L);
                veVehicleModifyLogService.save(vmLog);
            }
            // 记录车辆修改日志
            VeVehicleModifyLog vvmLog = new VeVehicleModifyLog();
            vvmLog.setVehicleInfoId(ve_info.getVehicleInfoId());
            vvmLog.setBrandId(ve_info.getBrandId());
            vvmLog.setModelId(ve_info.getModelId());
            vvmLog.setVePlates(ve_info.getVePlates());
            vvmLog.setCargoTypeList(p_cargo_list);
            vvmLog.setVeResidentX(p_gps_x);
            vvmLog.setVeResidentY(p_gps_y);
            vvmLog.setVeResidentAddressId(p_resident_id);
            vvmLog.setVeResidentDesc(p_resident_desc);
            vvmLog.setHidePlates(ve_info.getHidePlates());
            vvmLog.setUserName(p_user_name);
            vvmLog.setMobile(p_mobile);
            vvmLog.setFromAddress1Id(p_block_1_from);
            vvmLog.setToAddress1Id(p_block_1_to);
            vvmLog.setFromAddress2Id(p_block_2_from);
            vvmLog.setToAddress2Id(p_block_2_to);
            vvmLog.setFromAddress3Id(p_block_3_from);
            vvmLog.setToAddress3Id(p_block_3_to);
            vvmLog.setFromAddress4Id(p_block_4_from);
            vvmLog.setToAddress4Id(p_block_4_to);
            vvmLog.setComment(p_comment == null ? "" : p_comment);
            vvmLog.setModifyTime(new Date());
            veVehicleModifyLogService.save(vvmLog);
            String result = procedureService.updateVehicleInfo(p_old_mobile, p_gps_x, p_gps_y, p_resident_id, p_resident_desc, p_block_1_from, p_block_1_to, p_block_2_from,
                    p_block_2_to, p_block_3_from, p_block_3_to, p_block_4_from, p_block_4_to, p_cargo_list, p_user_name, p_mobile, "", "", "", hezhaoUrl, (p_comment == null ? ""
                            : p_comment), p_vin_number, p_idcard);
            if ("0".equals(result)) {
                SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
                Double[] d = new Double[2];
                d[0] = ve_info.getVeLastLongitude();
                d[1] = ve_info.getVeLastLatitude();
                seVo.setVehicleId(vehicleId);
                seVo.setVehiclePhone(p_mobile);
                seVo.setDriveName(p_user_name);
                seVo.setCargoList(p_cargo_list);
                seVo.setDomicile(d);
                VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);
                return JsonUtils.resultJson(0, "上报成功", null);
            }
            List<Object> errList = new ArrayList<Object>();
            errList.add(result);
            return JsonUtils.resultJson(8, "更新失败", errList);

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(9, "连接数据库异常", null);
        }
    }

    /*****************************************************************************
     * 注册车辆信息
     * 
     * @return Json.
     * 
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("/addvehicle")
    public String addvehicle(@RequestParam(value = "platenumber", required = false, defaultValue = "")
    String platenumber, @RequestParam(value = "cartypeID", required = false, defaultValue = "0")
    Integer cartypeID, @RequestParam(value = "carbrandID", required = false, defaultValue = "0")
    Integer carbrandID, @RequestParam(value = "chuchangnianfen", required = false, defaultValue = "")
    String chuchangnianfen, @RequestParam(value = "zhuchedi", required = false, defaultValue = "")
    String zhuchedi, @RequestParam(value = "qujiedaoid", required = false, defaultValue = "0")
    Integer qujiedaoid, @RequestParam(value = "gps_longitude", required = false, defaultValue = "0")
    Double gps_longitude, @RequestParam(value = "gps_latitude", required = false, defaultValue = "0")
    Double gps_latitude,
    // 营运区域
            @RequestParam(value = "from_city1_id", required = false, defaultValue = "0")
            Integer from_city1_id, @RequestParam(value = "to_city1_id", required = false, defaultValue = "0")
            Integer to_city1_id, @RequestParam(value = "from_city2_id", required = false, defaultValue = "0")
            Integer from_city2_id, @RequestParam(value = "to_city2_id", required = false, defaultValue = "0")
            Integer to_city2_id, @RequestParam(value = "from_city3_id", required = false, defaultValue = "0")
            Integer from_city3_id, @RequestParam(value = "to_city3_id", required = false, defaultValue = "0")
            Integer to_city3_id, @RequestParam(value = "from_city4_id", required = false, defaultValue = "0")
            Integer from_city4_id, @RequestParam(value = "to_city4_id", required = false, defaultValue = "0")
            Integer to_city4_id, @RequestParam(value = "shanchanghuolei", required = false, defaultValue = "")
            String shanchanghuolei, @RequestParam(value = "personorcompany", required = false, defaultValue = "0")
            Integer personorcompany, @RequestParam(value = "person_username", required = false, defaultValue = "")
            String person_username, @RequestParam(value = "person_telno", required = false, defaultValue = "")
            String person_telno, @RequestParam(value = "driver_username", required = false, defaultValue = "")
            String driver_username, @RequestParam(value = "driver_telno", required = false, defaultValue = "")
            String driver_telno, @RequestParam(value = "company_name", required = false, defaultValue = "")
            String company_name, @RequestParam(value = "company_tel", required = false, defaultValue = "")
            String company_tel, @RequestParam(value = "company_address", required = false, defaultValue = "")
            String company_address,
            // 照片
            @RequestParam(value = "cheweizhao", required = false)
            MultipartFile cheweizhao, @RequestParam(value = "chetouzhao", required = false)
            MultipartFile chetouzhao, @RequestParam(value = "checezhao", required = false)
            MultipartFile checezhao, @RequestParam(value = "hezhao", required = false)
            MultipartFile hezhao,

            @RequestParam(value = "vin", required = false, defaultValue = "")
            String vin, @RequestParam(value = "idcard", required = false, defaultValue = "")
            String idcard, @RequestParam(value = "comment", required = false, defaultValue = "")
            String comment, @RequestParam(value = "recommend", required = false, defaultValue = "")
            String recommend) {
        if ("".equals(platenumber.trim())) {
            return JsonUtils.resultJson(1, "车牌号码为空", null);
        }
        if (null == cartypeID || 0 == cartypeID) {
            return JsonUtils.resultJson(2, "车型错误", null);
        }
        if (null == carbrandID || 0 == carbrandID) {
            return JsonUtils.resultJson(3, "品牌错误", null);
        }
        if ("".equals(zhuchedi.trim())) {
            return JsonUtils.resultJson(4, "驻车地为空", null);
        }
        if ((to_city1_id == null || 0 == to_city1_id) && (to_city2_id == null || 0 == to_city2_id) && (to_city3_id == null || 0 == to_city3_id)
                && (to_city4_id == null || 0 == to_city4_id)) {
            return JsonUtils.resultJson(5, "营运区域为空", null);
        }
        if ("".equals(shanchanghuolei.trim())) {
            return JsonUtils.resultJson(6, "擅长货类为空", null);
        }
        if (personorcompany != 1 && personorcompany != 2) {
            return JsonUtils.resultJson(7, "营运单位错误", null);
        }
        if ("".equals(person_username.trim())) {
            return JsonUtils.resultJson(8, "司机姓名为空", null);
        }
        if (!Utils.checkMobile(person_telno)) {
            return JsonUtils.resultJson(9, "司机手机错误", null);
        }
        if (null == chetouzhao) {
            return JsonUtils.resultJson(10, "车头照为空", null);
        }
        if (null == checezhao) {
            return JsonUtils.resultJson(11, "车侧照为空", null);
        }
        if (null == cheweizhao) {
            return JsonUtils.resultJson(12, "车尾照为空", null);
        }
        if (!"".equals(recommend.trim()) && !Utils.checkMobile(recommend)) {
            return JsonUtils.resultJson(16, "推荐人手机号码错误", null);
        }
        // 个人
        if (personorcompany == 1) {
            driver_username = null;
            driver_telno = null;
            company_name = null;
            company_tel = null;
            company_address = null;
        } else if (personorcompany == 2) {
            driver_username = person_username;
            driver_telno = person_telno;
            person_username = null;
            person_telno = null;
        }
        String basePath = Constant.AUDIO_PATH + "/car";
        Date now = new Date();
        String year = now.getYear() + "";
        String month = now.getMonth() + "";
        String day = now.getDay() + "";
        String hour = now.getHours() + "";
        // TODO 图片处理
        String fullPath = UploadUtils.getAudioPath(basePath, year, month, day, hour);
        try {
            // 保存上传的图片
            cheweizhao.transferTo(new File(fullPath + "/" + cheweizhao.getOriginalFilename()));
            chetouzhao.transferTo(new File(fullPath + "/" + chetouzhao.getOriginalFilename()));
            checezhao.transferTo(new File(fullPath + "/" + checezhao.getOriginalFilename()));
            // 保存缩略图
            // UploadUtils.uploadImage1(fullPath,
            // cheweizhao,cheweizhao.getContentType(),
            // cheweizhao.getOriginalFilename());
            // UploadUtils.uploadImage1(fullPath,
            // chetouzhao,chetouzhao.getContentType(),
            // chetouzhao.getOriginalFilename());
            // UploadUtils.uploadImage1(fullPath,
            // checezhao,checezhao.getContentType(),
            // checezhao.getOriginalFilename());
            // 加水印
            request.getRealPath("");
            UploadUtils.pressImage(fullPath + "/" + cheweizhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water1.png", 5);
            UploadUtils.pressImage(fullPath + "/" + cheweizhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water2.png", 1);
            UploadUtils.pressImage(fullPath + "/" + chetouzhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water1.png", 5);
            UploadUtils.pressImage(fullPath + "/" + chetouzhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water2.png", 1);
            UploadUtils.pressImage(fullPath + "/" + checezhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water1.png", 5);
            UploadUtils.pressImage(fullPath + "/" + checezhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water2.png", 1);
            String img1Url = "/attachment/car" + fullPath + "/" + cheweizhao.getOriginalFilename();
            String img2Url = "/attachment/car" + fullPath + "/" + chetouzhao.getOriginalFilename();
            String img3Url = "/attachment/car" + fullPath + "/" + checezhao.getOriginalFilename();
            String img4Url = null;
            if (!hezhao.isEmpty()) {
                hezhao.transferTo(new File(fullPath + "/" + hezhao.getOriginalFilename()));
                // UploadUtils.uploadImage1(fullPath,
                // hezhao,hezhao.getContentType(),
                // hezhao.getOriginalFilename());
                UploadUtils.pressImage(fullPath + "/" + hezhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water1.png", 5);
                UploadUtils.pressImage(fullPath + "/" + hezhao.getOriginalFilename(), Constant.WATER_FILE_PATH + "water2.png", 1);
            }
            String result = procedureService.uploadVehicleInfo(platenumber, cartypeID, carbrandID, chuchangnianfen, qujiedaoid, zhuchedi, from_city1_id, to_city1_id,
                    from_city2_id, to_city2_id, from_city3_id, to_city3_id, from_city4_id, to_city4_id, gps_longitude, gps_latitude, shanchanghuolei, personorcompany,
                    person_username, person_telno, driver_username, driver_telno, company_name, company_tel, company_address, img1Url, img2Url, img3Url, img4Url, vin, idcard,
                    comment, recommend);
            if ("0".equals(result)) {
                return JsonUtils.resultJson(0, "成功 ", null);
            } else if ("1".equals(result)) {
                return JsonUtils.resultJson(17, "司机手机号码已存在 ", null);
            } else if ("2".equals(result)) {
                return JsonUtils.resultJson(16, "推荐人手机号不存在 ", null);
            } else {
                return JsonUtils.resultJson(13, "'服务器异常，错误码: " + result, null);
            }

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(13, "'服务器异常", null);
        }
    }

    /**************************************************************
     * 上报车辆位置坐标
     * 
     * @param longitude
     *            当前经度
     * @param latitude
     *            当前纬度
     * @return Json *
     * @throws Exception
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/reportlocation")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String reportlocation(@RequestParam(value = "longitude", required = false, defaultValue = "0")
    String longitude, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    String latitude) throws Exception {
        // 从session获取车主手机号码
    	
    	if("0".equals(longitude)  || "0".equals(latitude)){
    		return JsonUtils.resultJson(4, "经纬度错误", null);
    	}
    	
    	
        String mobile = null;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers plUsers = null;
        if (obj != null) {
            plUsers = (PlUsers) obj;
            mobile = plUsers.getMobile();
        }
        try {
            List<PlUsers> plusersList = plUsersService.findByMobileAndIsvalid(mobile, "Y");
            if (plusersList.size() > 0) {
                PlUsers user = plusersList.get(0);
                // 更新实时坐标表(mysql)
                VeUploadGps vuGps = new VeUploadGps();
                vuGps.setLongitude(longitude);
                vuGps.setLatitude(latitude);
                vuGps.setVeModel(mobile);
                vuGps.setUptime(new Date());
                veUploadGpsService.save(vuGps);

                // 更新车辆表 最新坐标
                List<VeVehicleInfo> vehicleInfoList = veVehicleInfoService.findVehicleByUserId(user.getUserId());
                VeVehicleInfo vehicleInfo = vehicleInfoList.get(0);
                vehicleInfo.setVeLastLongitude(Double.parseDouble(longitude));
                vehicleInfo.setVeLastLatitude(Double.parseDouble(latitude));
                veVehicleInfoService.save(vehicleInfo);

                // TODO 更新实时坐标（mongo）
                VeUploadGpsMG gpsMg = new VeUploadGpsMG();
                gpsMg.setLng(longitude);
                gpsMg.setLat(latitude);
                gpsMg.setUserId(user.getUserId());
                gpsMg.setVehicleId(vehicleInfo.getVehicleInfoId());
                gpsMg.setMobile(mobile);
                gpsMg.setUptime(new Date().getTime());
                veUploadGpsMGService.save(gpsMg);

                // 更新搜索引擎
                // StringBuffer sbf = new StringBuffer();
                // sbf.append("{ __Request:\"Update\",");
                // sbf.append("  __Collection:\"Vehicle\",");
                // sbf.append("  __Fields:{ ");
                // sbf.append(" _id:").append(vehicleInfo.getVehicleInfoId()).append(",");
                // sbf.append(" Location:[").append(longitude).append(",").append(latitude).append("]");
                // sbf.append("}}");
                // // LOG.info(" request String is:" + sbf.toString());
                // String webserviceAddress = Constant.VEHICLE_WEBSERVICE_URL;
                // String nameSpace = Constant.COSMOS_NAMESPACE;
                // Object[] params = new Object[] { sbf.toString() };
                // Class[] classs = new Class[] { String.class };
                // String result = wsdlService.webserviceCall(webserviceAddress,
                // nameSpace, "request", params, classs);
                // // LOG.info(" SE response Json is :" + result);
                // VeUpdateReturnVo vo = (VeUpdateReturnVo)
                // JsonUtils.json2Object(result, VeUpdateReturnVo.class);

                SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
                Double[] d = new Double[2];
                d[0] = Double.valueOf(longitude);
                d[1] = Double.valueOf(latitude);
                seVo.setVehicleId(vehicleInfo.getVehicleInfoId());
                seVo.setLocation(d);
                seVo.setLastTime(new Date().getTime());
                VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);
                if (!"Success".equals(vo.getResult())) {
                    return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
                } else {
                    return JsonUtils.resultJson(0, "上报成功", null);
                }
            } else {
                return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
            }
        } catch (Exception e) {
            LOG.error("The reportlocation() method invocation exception.", e);
            // return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
            throw new RuntimeException();

        }
    }

    /********************************************************************
     * 语音详情
     * 
     * @param order_id
     *            语音订单id
     * @return Json
     * 
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/voicedetail")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String voicedetail(@RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long order_id) {
        if (!Utils.checkOrderId(order_id + "") || order_id < 1) {
            return JsonUtils.resultJson(2, "语音不存在", new ArrayList<Object>());
        }
        try {
            List<BkVoiceOrder> bkVoiceOrderList = bkVoiceOrderService.findById(order_id);
            if (bkVoiceOrderList.size() < 1) {
                return JsonUtils.resultJson(2, "语音不存在", new ArrayList<Object>());
            }
            BkVoiceOrder bvo = bkVoiceOrderList.get(0);
            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            dataMap.put("order_id", bvo.getId());
            dataMap.put("voice_file", Constant.VOICE_FILE_URL + bvo.getFilePath() + "/" + bvo.getFileName());
            dataMap.put("create_time", TimeUtil.getDateString(bvo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            dataMap.put("expire_time", TimeUtil.getDateString(bvo.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
            return JsonUtils.resultJson(0, "成功", dataMap);
        } catch (Exception e) {
            LOG.error(" voicedetail method Exception is" + e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
        }
    }

    /******************************************************************
     * 文本叫车详情
     * 
     * @param voice_id
     *            语音订单id
     * @return Json
     * 
     */
    @ResponseBody
    @RequestMapping("/textdetail")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String textdetail(@RequestParam(value = "voice_id", required = false, defaultValue = "0")
    Long voice_id) {
    	
        try {
            BkVoiceOrder voiceOrder = bkVoiceOrderService.findOne(voice_id);
            if (voiceOrder ==  null) {
                return JsonUtils.resultJson(2, "文本叫车信息不存在", new ArrayList<Object>());
            }
            List<BkVoicePhoto> voicePhotoList = bkVoicePhotoService.findByVoiceId(voice_id);
//            BkVoiceOrder voiceOrder = voiceOrderList.get(0);
            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            String fromAddress = voiceOrder.getFromAddress() == null ? "" : voiceOrder.getFromAddress();
            String toAddress = voiceOrder.getToAddress() == null ? "" : voiceOrder.getToAddress();
            String sendTime = voiceOrder.getExpireTime() == null ? "" : TimeUtil.getDateString(voiceOrder.getExpireTime(), "yyyy-MM-dd HH:mm");
            dataMap.put("from_address", fromAddress);
            dataMap.put("to_address", toAddress);
            dataMap.put("send_time", sendTime);

            String goodLength = "长" + Utils.format(voiceOrder.getGoodsLength()) + ",";
            String goodWidth = "宽" + Utils.format(voiceOrder.getGoodsWidth()) + ",";
            String goodHeight = "高" + Utils.format(voiceOrder.getGoodsHeight());

            if (StringUtils.isNullOrEmpty(voiceOrder.getGoodsLength()) || "0".equals(voiceOrder.getGoodsLength())) {
                goodLength = "";
            }
            if (StringUtils.isNullOrEmpty(voiceOrder.getGoodsWidth()) || "0".equals(voiceOrder.getGoodsWidth())) {
                goodWidth = "";
            }
            if (StringUtils.isNullOrEmpty(voiceOrder.getGoodsHeight()) || "0".equals(voiceOrder.getGoodsHeight())) {
                goodHeight = "";
            }

            String fromShortAddress = Utils.isEmpty(voiceOrder.getFromShortAddress()) ? "" : voiceOrder.getFromShortAddress();
            String toShortAddress = Utils.isEmpty(voiceOrder.getToShortAddress()) ? "" : voiceOrder.getToShortAddress();
            String fromDetailAddress = voiceOrder.getFromDetailAddress() == null ? "" : voiceOrder.getFromDetailAddress();
            String toDetailAddress = voiceOrder.getToDetailAddress() == null ? "" : voiceOrder.getToDetailAddress();
            dataMap.put("goods_volume", goodLength + goodWidth + goodHeight);
            dataMap.put("description", voiceOrder.getDescription() == null ? "" : voiceOrder.getDescription());
            dataMap.put("goods_photo", voicePhotoList.size() < 1 ? "" : Constant.IMG_URL_PRE + voicePhotoList.get(0).getFilePath() + "/" + voicePhotoList.get(0).getFileName());
            dataMap.put("from_short_address", fromShortAddress);
            dataMap.put("to_short_address", toShortAddress);

            dataMap.put("from_detail_address", fromDetailAddress);
            dataMap.put("to_detail_address", toDetailAddress);

            String fromToDistance = voiceOrder.getFromToDistance();
            if (!Utils.isEmpty(fromToDistance) && !"0".equals(fromToDistance)) {
                if (Utils.isNumberOrFloat(fromToDistance)) {
                    Double dis = Double.valueOf(fromToDistance) / 1000;
                    fromToDistance = "约" + String.format("%.1f", dis) + "公里";
                }
            } else {
                fromToDistance = "";
            }
            
            String refPrice = (voiceOrder.getReferencePrice() == null || 0.0 == voiceOrder.getReferencePrice())?"" : voiceOrder.getReferencePrice()+"";
            dataMap.put("reference_price", refPrice);
            dataMap.put("from_to_distance", fromToDistance);
            

            // 车型选择
            String needModel = voiceOrder.getNeedModel();
            String modelStr = "";
            
            if(getVersionCode() < 234){
            	 if (!Utils.isEmpty(needModel)) {
                     String[] models = needModel.split(",");
                     for (int i = 0; i < models.length; i++) {
                         modelStr += Common.getModelDesc(models[i])+",";
                     }
                     modelStr.substring(0, modelStr.length()-1);
                 } 
            }else {
            	 //2.3.4版本以后 车型改了
            	if (!Utils.isEmpty(needModel)) {
            		String[] models = needModel.split(",");
            		List<Map<String,String>> carModelList = Common.getCarType();
            		for (String str : models) {
            			for (Map<String, String> map : carModelList) {
    	                	if(map.get("value").contains(str) && !modelStr.contains(map.get("name"))){
    	                		modelStr += map.get("name")+",";
    	                	}
    	    			}
					}
            		modelStr =	modelStr.substring(0, modelStr.length()-1);
            	}
            }
            dataMap.put("need_model", modelStr);

            // 新增 出发地纬度 出发地经度 目的地纬度 目的地经度 2014/9/16

            String fromLat = voiceOrder.getFromLatitude() == null ? "" : voiceOrder.getFromLatitude() + "";
            String fromLng = voiceOrder.getFromLongitude() == null ? "" : voiceOrder.getFromLongitude() + "";
            String toLat = voiceOrder.getToLatitude() == null ? "" : voiceOrder.getToLatitude() + "";
            String toLng = voiceOrder.getToLongitude() == null ? "" : voiceOrder.getToLongitude() + "";
            String timeType = "1";
            
            if (voiceOrder.getCreateTime() != null && voiceOrder.getExpireTime() != null) {
                // 如果大于30分钟就是（2）预约订单，否则是（1）实时订单
                if ((voiceOrder.getExpireTime().getTime() - voiceOrder.getCreateTime().getTime()) / 60 /1000 > 30) {
                    timeType = "2";
                } else {
                    timeType = "1";
                }
            }
            String textContent = "";
            
            if (timeType.equals("1")) {
                textContent = "从" + (Utils.isBusLines(fromAddress)?"":fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + fromShortAddress + ",运到"
                        + (Utils.isBusLines(toAddress)?"":toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + toShortAddress + ","+(Utils.isEmpty(fromToDistance)?"":"最短距离"+fromToDistance)
                        + (Utils.isEmpty(refPrice)?"":"，预估运费"+refPrice+"元")+ (Utils.isEmpty(voiceOrder.getDescription())?"":",备注,"+voiceOrder.getDescription());
            }else{
                textContent = "预约，从" + (Utils.isBusLines(fromAddress)?"":fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + fromShortAddress + ",运到"
                        + (Utils.isBusLines(toAddress)?"":toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + toShortAddress +","+ (Utils.isEmpty(fromToDistance)?"":"最短距离"+fromToDistance)
                        + (Utils.isEmpty(refPrice)?"":"，预估运费"+refPrice+"元")+TimeUtil.getDateString(voiceOrder.getExpireTime(), ",发货时间M月d日H点m分") + (Utils.isEmpty(voiceOrder.getDescription())?"":",备注,"+voiceOrder.getDescription());
            }
            String bookingId = "";
            BkBooking booking = bkBookingService.findByVoiceId(voice_id);
            if (booking != null) {
                bookingId = booking.getBookingNo() + "";
            }
            
            dataMap.put("from_lat", fromLat);
            dataMap.put("from_lng", fromLng);
            dataMap.put("to_lat", toLat);
            dataMap.put("to_lng", toLng);
            dataMap.put("text_content", textContent);
            dataMap.put("time_type", timeType);
            dataMap.put("booking_id", bookingId);

            return JsonUtils.resultJson(0, "成功", dataMap);
        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
        }
    }

    /*****************************************************************
     * 订单列表
     * 
     * @param pageno
     *            页数
     * @param limit
     *            每页数
     * @param status
     *            订单状态
     * @return Json
     * 
     */

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/bookinglist")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String bookinglist(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageno, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int limit, @RequestParam(value = "status", required = false, defaultValue = "")
    String status) {

        try {
            // session获取手机号码
            String mobile = "";
            // session获取 userid
            Long userId = 0L;
            // session 获取vehicleId
            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            Object obj = session.getAttribute(Constant.SESSION_PL_USER);

            PlUsers plUsers = null;
            if (obj != null) {
                plUsers = (PlUsers) obj;
                mobile = plUsers.getMobile();
                userId = plUsers.getUserId();
            }
            Page<BkBooking> page = null;
            Long[] statusArray = null;
            if (!StringUtils.isNullOrEmpty(status)) {
                String[] str = status.split(",");
                statusArray = new Long[str.length];
                int i = 0;
                while (i < str.length) {
                    statusArray[i] = Long.valueOf(str[i]);
                    i++;
                }
            }

            page = bkBookingService.findByBookingToUsersIdAndBookingStatus(userId, statusArray, pageno, limit);
            List<BkBooking> bkbookingList = page.getContent();
            List<LinkedHashMap<String, Object>> orderList = new ArrayList<LinkedHashMap<String, Object>>();
            LinkedHashMap<String, Object> orderListMap = null;
            if (bkbookingList.size() > 0) {
                for (BkBooking bbk : bkbookingList) {
                    String voice_file = "";
                    double offer_price = 0.0;
                    Long bookingType = 0L;
                    String textContent = "";
                    if (null != bbk.getVoiceId() && 0 != bbk.getVoiceId()) {
                        List<BkVoiceOrder> voiceOrderList = bkVoiceOrderService.findById(bbk.getVoiceId());
                        voice_file = Constant.VOICE_FILE_URL + voiceOrderList.get(0).getFilePath() + "/" + voiceOrderList.get(0).getFileName();
                        // 老接口按手机号码查询有问题
                        // List<BkOrderOffer> orderOfferList =
                        // bkOrderOfferService.findByOrderIdAndVeMobile(bbk.getVoiceId(),mobile);
                        List<BkOrderOffer> orderOfferList = bkOrderOfferService.findByVehicleInfoIdAndOrderId(vehicleId, bbk.getVoiceId());
                        BkVoiceOrder bvo = null;
                        String fromAddress=null;
                        String toAddress=null;
                        String fromShortAddress = null;
                        String toShortAddress = null;
                        String fromToDistance = null;

                        bvo = voiceOrderList.get(0);
                        if (orderOfferList.size() > 0) {
                            offer_price = orderOfferList.get(0).getPrice();
                        }
                        fromAddress=Utils.isEmpty(bvo.getFromAddress())?"":bvo.getFromAddress();
                        toAddress=Utils.isEmpty(bvo.getToAddress())?"":bvo.getToAddress();
                        fromShortAddress = Utils.isEmpty(bvo.getFromShortAddress())?"":bvo.getFromShortAddress();
                        toShortAddress = Utils.isEmpty(bvo.getToShortAddress())?"":bvo.getToShortAddress();
                        fromToDistance = bvo.getFromToDistance();
                        // 判断是否为文本订单
                        if (bvo.getOrderType() == 1L || bvo.getOrderType() == 2L) {
                            if (bvo.getOrderType() == 2L) {
                                bookingType = 4L;
                                offer_price = bvo.getReferencePrice();
                            } else {
                                bookingType = 3L;
                            }

                            if (!Utils.isEmpty(fromToDistance)) {
                                if (Utils.isNumberOrFloat(fromToDistance)) {
                                    Double dis = Double.valueOf(fromToDistance) / 1000;
                                    fromToDistance = "，最短距离大约" + String.format("%.2f", dis) + "公里，";
                                }
                            } else {
                                fromToDistance = "";
                            }

                            textContent = "我要从" + (Utils.isBusLines(fromAddress)?"":fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + fromShortAddress +  ",运到"
                                    + (Utils.isBusLines(toAddress)?"":toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + toShortAddress +  fromToDistance
                                    + TimeUtil.getDateString(bvo.getExpireTime(), "发货时间M月d日H点m分") + (Utils.isEmpty(bvo.getDescription())?"":",备注,"+bvo.getDescription());
                        }

                        else {
                            bookingType = 2L;
                        }
                    }
                    orderListMap = new LinkedHashMap<String, Object>();

                    orderListMap.put("booking_id", bbk.getBookingNo());
                    orderListMap.put("ve_mobile", mobile);
                    orderListMap.put("cargo_mobile", bbk.getBookingFromUsersId());
                    orderListMap.put("longitude", bbk.getLng());
                    orderListMap.put("latitude", bbk.getLat());
                    orderListMap.put("status", bbk.getBookingStatus());
                    orderListMap.put("voice_file", voice_file);
                    orderListMap.put("booking_type", bookingType);
                    orderListMap.put("price", offer_price + "");
                    orderListMap.put("order_number", bbk.getOrderNumber());
                    orderListMap.put("order_time", bbk.getCreateDt().getTime() / 1000);
                    orderListMap.put("ve_read", bbk.getVeRead() ? 1 : 0);
                    orderListMap.put("text_content", textContent);
                    LinkedHashMap<String, Object> comment_data = new LinkedHashMap<String, Object>();
                    if (7 == bbk.getBookingStatus()) {
                        comment_data.put("level", bbk.getCommentLevel());
                        comment_data.put("content", bbk.getComment());
                        comment_data.put("time", bbk.getCommentCreateDt().getTime() / 1000);
                    }
                    orderListMap.put("comment", comment_data);
                    orderList.add(orderListMap);

                }
                return JsonUtils.resultJson(0, null, orderList);
            } else {
                return JsonUtils.resultJson(0, "数据已加载完，没有更多数据", orderList);
            }

        } catch (Exception e) {
            LOG.error("The bookinglist() method invocation exception.", e);
            return JsonUtils.resultJson(2, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
        }

    }

    /*****************************************************************
     * 已抢单列表
     * 
     * @param pageno
     *            页数
     * @param limit
     *            每页数
     * @return Json
     * 
     */
    @ResponseBody
    @RequestMapping("/offerlist")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String offerlist(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageno, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int limit) {

        try {
            // session获取手机号码
            String mobile = null;
            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            Object obj = session.getAttribute(Constant.SESSION_PL_USER);
            PlUsers plUsers = null;
            if (obj != null) {
                plUsers = (PlUsers) obj;
                mobile = plUsers.getMobile();
            }
            List<OfferListVo> offerListVoList = nativeService.findOfferList(vehicleId, mobile, pageno, limit);
            OfferList offerListInfo = null;
            List<OfferList> resultList = null;
            resultList = new ArrayList<OfferList>();
            for (OfferListVo vo : offerListVoList) {
                String offer_succ = "";
                BkBooking bkBooking = bkBookingService.findByVoiceId(vo.getOrder_id());
                // 查询文本订单
                BkVoiceOrder voiceorder = bkVoiceOrderService.findOne(vo.getOrder_id());

                if (null != bkBooking) {
                    if (null != bkBooking.getVehicleInfoId() && vehicleId.equals(bkBooking.getVehicleInfoId())) {
                        offer_succ = "抢单成功，请到订单详情确认订单。";
                    } else {
                        offer_succ = "抢单失败，请留意其他抢单信息";
                    }

                } else {
                    if (null != voiceorder && voiceorder.getIsCancel()) {
                        offer_succ = "货主已取消叫车。";
                    } else {
                    	
                        offer_succ = "请等待，货主正在比价。";
                    }
                }

                offerListInfo = new OfferList();
                offerListInfo.setId(vo.getOrder_id());
                offerListInfo.setVoice_file(Constant.VOICE_FILE_URL + ((vo.getFile_path() == null) ? "" : vo.getFile_path()) + "/"
                        + (vo.getFile_name() == null ? "" : vo.getFile_name()));
                if (null != voiceorder && voiceorder.getIsCancel()) {
                    // 如果货主取消了订单 车主端显示抢单时间结束 返回的过期时间 调前
                    offerListInfo.setExpire_time(TimeUtil.getStringToDate("2014-01-01 00:00:00").getTime() / 1000 + "");
                } else {
                    offerListInfo.setExpire_time(vo.getExpire_time() == null ? "" : TimeUtil.getStringToDate(vo.getExpire_time()).getTime() / 1000 + "");
                    
                }

                offerListInfo.setOffer_time(vo.getCreate_time() == null ? "" : TimeUtil.getStringToDate(vo.getCreate_time()).getTime() / 1000 + "");
                offerListInfo.setNow_time(new Date().getTime() / 1000 + "");
                offerListInfo.setPrice(vo.getPrice() + "");
                offerListInfo.setOffer_succ(offer_succ);
                offerListInfo.setOrder_type(vo.getOrder_type() + "");

                String fromToDistance = vo.getFromToDistance();
                if (!Utils.isEmpty(fromToDistance)) {
                    if (Utils.isNumberOrFloat(fromToDistance)) {
                        Double dis = Double.valueOf(fromToDistance) / 1000;
                        fromToDistance = "，最短距离大约" + String.format("%.2f", dis) + "公里，";
                    }
                } else {
                    fromToDistance = "";
                }
                
                String fromAddress=Utils.isEmpty(vo.getFrom_address())?"":vo.getFrom_address();
                String fromShortAddress=Utils.isEmpty(vo.getFromShortAddress())?"":vo.getFromShortAddress();
                String toShortAddress=Utils.isEmpty(vo.getToShortAddress())?"":vo.getToShortAddress();
                String toAddress=Utils.isEmpty(vo.getTo_address())?"":vo.getTo_address();

                String textcontent="我要从" + (Utils.isBusLines(fromAddress)?"":fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + fromShortAddress + ",运到"
                        + (Utils.isBusLines(toAddress)?"":toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + toShortAddress +","+ fromToDistance
                        + TimeUtil.getDateString(TimeUtil.getStringToDate(vo.getExpire_time()), "发货时间M月d日H点m分") + (Utils.isEmpty(vo.getDescription())?"":",备注,"+vo.getDescription());
                offerListInfo.setText_content(vo.getOrder_type() == 1 ? textcontent : "");
                resultList.add(offerListInfo);
            }
            LOG.info("//---------------offerlist   "+JsonUtils.object2Json(resultList));
            if (resultList.size() == 0) {
                return JsonUtils.resultJson(0, "数据已加载完，没有更多数据", resultList);
            } else {
                return JsonUtils.resultJson(0, "成功", resultList);
            }

        } catch (Exception e) {
            LOG.error("The offerlist() method invocation exception.", e);
            return JsonUtils.resultJson(2, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
        }
    }

    /**************************************************************
     * 上报语音接收状态
     * 
     * @param status
     *            订单状态
     * @param order_id
     *            订单id
     * @return Json
     * 
     */

    @ResponseBody
    @RequestMapping("/reportvoicestatus")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String reportvoicestatus(@RequestParam(value = "status", required = false, defaultValue = "0")
    Long status, @RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long order_id) {

        // session获取手机号码
        String mobile = null;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers plUsers = null;
        if (obj != null) {
            plUsers = (PlUsers) obj;
            mobile = plUsers.getMobile();
        }

        try {
            List<BkVoiceOrder> bkVoiceOrderlist = bkVoiceOrderService.findById(order_id);
            if (bkVoiceOrderlist == null || bkVoiceOrderlist.size() < 1) {
                return JsonUtils.resultJson(2, "语音不存在", null);
            }
            List<BkVoiceReceived> bkVoiceReceivedList = bkVoiceReceivedService.findByVoiceIdAndVeMobile(order_id, mobile);
            if (bkVoiceReceivedList == null || bkVoiceReceivedList.size() < 1) {
                return JsonUtils.resultJson(3, "上报失败", null);
            } else {
                BkVoiceReceived br = bkVoiceReceivedList.get(0);
                if (bkVoiceReceivedList.get(0).getIsSucc()) {
                    return JsonUtils.resultJson(3, "上报失败", null);
                }
                if (status == 1)
                    br.setIsReceived(true);
                
                br.setReceivedTime(new Date());
                bkVoiceReceivedService.save(br);
                if (status == 1 && status != 0) {
                    BkVoiceOrder bo = bkVoiceOrderlist.get(0);
                    Long count = bo.getReceivedVehicle();
                    count = count + 1;
                    bo.setReceivedVehicle(count);
                    if (bkVoiceOrderService.save(bo)) {
                        LinkedHashMap<String, Object> contentMap = new LinkedHashMap<String, Object>();
                        contentMap.put("type", "6");
                        contentMap.put("order_id", "" + order_id);
                        xmppService.push(bo.getUserCargoId()+"", JsonUtils.object2Json(contentMap), "c_");
                        return JsonUtils.resultJson(0, "成功", null);

                    } else {
                        return JsonUtils.resultJson(3, "上报失败", null);
                    }
                } else {
                    return JsonUtils.resultJson(0, "成功", null);
                }
            }
        } catch (Exception e) {
            LOG.error("The reportvoicestatus() method invocation exception.", e);
            return JsonUtils.resultJson(4, "服务器忙晕啦，请稍候再试", null);

        }
    }

    /*****************************************************************
     * 订单详情
     * 
     * @param booking_id
     *            订单id
     * @return
     * 
     */
    @ResponseBody
    @RequestMapping("/bookingdetail")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String bookingdetail(@RequestParam(value = "booking_id", required = false, defaultValue = "0")
    Long booking_id) {

        try {
            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());

            List<BkBooking> bkBookingList = bkBookingService.findByBookingNoAndVehicleInfoId(booking_id, vehicleId);
            if (null == bkBookingList || bkBookingList.size() < 1) {
                return JsonUtils.resultJson(2, "订单不存在", new ArrayList<String>());
            }
            // 置为车主已读
            BkBooking bk = bkBookingList.get(0);
            bk.setVeRead(true);
            bkBookingService.save(bk);
            LinkedHashMap<String, Object> bookingdetailMap = new LinkedHashMap<String, Object>();
            VeVehicleInfo vi = veVehicleInfoService.findByVehicleInfoId(bk.getVehicleInfoId());
            PlUsers plUsers = plUsersService.findOne(bk.getBookingToUsersId());
            Long voiceId = bk.getVoiceId();
            List<BkOrderOffer> bkOrderOfferList = bkOrderOfferService.findByVehicleInfoIdAndOrderId(bk.getVehicleInfoId(), voiceId);
            List<BkVoiceOrder> bkVoiceOrderList = bkVoiceOrderService.findById(voiceId);
            PlUsers pu = null;
            BkOrderOffer boo = null;
            BkVoiceOrder bvo = null;
            if (null == vi) {
                vi = new VeVehicleInfo();
            }
            if (plUsers != null) {
                pu = plUsers;
            } else {
                pu = new PlUsers();
            }
            if (bkOrderOfferList != null && bkOrderOfferList.size() > 0) {
                boo = bkOrderOfferList.get(0);
            } else {
                boo = new BkOrderOffer();
            }
            if (bkVoiceOrderList != null && bkVoiceOrderList.size() > 0) {
                bvo = bkVoiceOrderList.get(0);
            } else {
                bvo = new BkVoiceOrder();
            }

            Long bookingType = 1L;
            String TextContent = "";
            if (voiceId != null && voiceId != 0) {
                String goods_height = bvo.getGoodsHeight();
                String goods_width = bvo.getGoodsWidth();
                String goods_length = bvo.getGoodsLength();
                // 判断是否为文本订单
                if (bvo.getOrderType() == 1L || bvo.getOrderType() == 2L) {
                    if (bvo.getOrderType() == 1L)
                        bookingType = 3L;
                    if (bvo.getOrderType() == 2L)
                        bookingType = 4L;
                    String goodsttl = "";
                    // if (StringUtils.isNullOrEmpty(goods_length)
                    // && StringUtils.isNullOrEmpty(goods_height)
                    // && StringUtils.isNullOrEmpty(goods_width)) {
                    // goodsttl = "";
                    // }
                    goods_length = "长" + Utils.format(goods_length) + "，";
                    goods_width = "宽" + Utils.format(goods_length) + "，";
                    goods_height = "高" + Utils.format(goods_length) + "，";

                    if (StringUtils.isNullOrEmpty(bvo.getGoodsLength()) || "0".equals(bvo.getGoodsLength())) {
                        goods_length = "";
                    }
                    if (StringUtils.isNullOrEmpty(bvo.getGoodsWidth()) || "0".equals(bvo.getGoodsWidth())) {
                        goods_width = "";
                    }
                    if (StringUtils.isNullOrEmpty(bvo.getGoodsHeight()) || "0".equals(bvo.getGoodsHeight())) {
                        goods_height = "";
                    }
                    String fromToDistance = bvo.getFromToDistance();
                    if (!Utils.isEmpty(fromToDistance)) {
                        if (Utils.isNumberOrFloat(fromToDistance)) {
                            Double dis = Double.valueOf(fromToDistance) / 1000;
                            fromToDistance = "，最短距离大约" + String.format("%.2f", dis) + "公里，";
                        }
                    } else
                        fromToDistance = "";
                    TextContent = "我要从" + (Utils.isBusLines(bvo.getFromAddress())?"":bvo.getFromAddress()) + (Utils.isEmpty(bvo.getFromShortAddress()) ? "" : bvo.getFromShortAddress())
                            + (Utils.isEmpty(bvo.getFromDetailAddress()) ? "" : bvo.getFromDetailAddress()) + ",运到" + (Utils.isBusLines(bvo.getToAddress())?"":bvo.getToAddress())
                            + (Utils.isEmpty(bvo.getToShortAddress()) ? "" : bvo.getToShortAddress()) + (Utils.isEmpty(bvo.getToDetailAddress()) ? "" : bvo.getToDetailAddress())
                            + fromToDistance + TimeUtil.getDateString(bvo.getExpireTime(), "发货时间M月d日H点m分") + "，" + goodsttl + goods_length + goods_width + goods_height 
                            + (Utils.isEmpty(bvo.getDescription())?"":",备注,"+bvo.getDescription());

                } else {
                    bookingType = 2L;
                }
            }
            bookingdetailMap.put("booking_id", booking_id);
            bookingdetailMap.put("order_number", bk.getOrderNumber());
            bookingdetailMap.put("create_time", TimeUtil.getDateString(bk.getCreateDt(), "yyyy-MM-dd HH:mm:ss"));
            bookingdetailMap.put("status", bk.getBookingStatus());
            bookingdetailMap.put("ve_plates", vi.getVePlates());
            bookingdetailMap.put("ve_type", vi.getModelId());
            bookingdetailMap.put("driver", pu.getUserName());
            bookingdetailMap.put("mobile", pu.getMobile());
            if (null != bvo && voiceId != null && voiceId != 0) {
                if (bvo.getOrderType() == 1L || bvo.getOrderType() == 0L) {
                    if (null != boo)
                        bookingdetailMap.put("price", null != boo.getPrice() ? boo.getPrice() + "" : "");
                } else
                    bookingdetailMap.put("price", null != bvo.getReferencePrice() ? bvo.getReferencePrice() + "" : "");
            } else
                bookingdetailMap.put("price", "");
            bookingdetailMap.put("booking_type", bookingType);
            bookingdetailMap.put("voice_file", bvo.getFilePath() != null ? Constant.IMG_URL_PRE + bvo.getFilePath() + "/" + bvo.getFileName() : "");
            bookingdetailMap.put("confirm_time", bk.getConfirmTime() == null ? "" : TimeUtil.getDateString(bkBookingList.get(0).getConfirmTime(), "yyyy-MM-dd HH:mm:ss"));
            bookingdetailMap.put("freight_time", bk.getFreightTime() == null ? "" : TimeUtil.getDateString(bkBookingList.get(0).getFreightTime(), "yyyy-MM-dd HH:mm:ss"));
            bookingdetailMap.put("arrive_time", bk.getArriveTime() == null ? "" : TimeUtil.getDateString(bkBookingList.get(0).getArriveTime(), "yyyy-MM-dd HH:mm:ss"));
            bookingdetailMap.put("complete_time", bk.getCompleteTime() == null ? "" : TimeUtil.getDateString(bkBookingList.get(0).getCompleteTime(), "yyyy-MM-dd HH:mm:ss"));
            PlUsersCargo cargo = plusersCargoService.findOne(bk.getUserCargoId());
            if (null != cargo)
                bookingdetailMap.put("cargo_mobile", cargo.getMobile());
            else
                bookingdetailMap.put("cargo_mobile", bk.getBookingFromUsersId());
            bookingdetailMap.put("longitude", bk.getLng());
            bookingdetailMap.put("latitude", bk.getLat());
            bookingdetailMap.put("expire_time", bvo.getExpireTime() == null ? "" : TimeUtil.getDateString(bkVoiceOrderList.get(0).getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
            LinkedHashMap<String, Object> commentMap = new LinkedHashMap<String, Object>();
            if (bkBookingList.get(0).getBookingStatus() == 7) {
                commentMap.put("score", bk.getCommentLevel());
                commentMap.put("content", bk.getComment() + "");
                commentMap.put("comment_time", bk.getCommentCreateDt().getTime() / 1000);
            }
            bookingdetailMap.put("comment_data", commentMap);
            bookingdetailMap.put("text_content", TextContent);
            bookingdetailMap.put("voice_id", bk.getVoiceId());
            // bookingdetailList.add(bookingdetailMap);
            return JsonUtils.resultJson(0, "成功", bookingdetailMap);

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", new ArrayList());
        }
    }

    /*****************************************************************
     * 意见反馈
     * 
     * @param content
     *            意见内容
     * @return Json
     * 
     */
    @ResponseBody
    @RequestMapping("/feedback")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String feedback(@RequestParam(value = "content", required = false, defaultValue = "")
    String content) {
        try {

            Object obj = session.getAttribute(Constant.SESSION_PL_USER);
            PlUsers plusers = null;
            if (null != obj) {
                plusers = (PlUsers) obj;
            }

            String clientIp = request.getRemoteAddr();
            Long fromType = 2L; // 1：呼叫中心 2：App
            // 从session 获取手机号码
            String mobile = plusers.getMobile();
            // 转换编码
            // content = URLDecoder.decode(new
            // String(content.getBytes("ISO-8859-1"),"utf-8"), "utf-8");
            if (StringUtils.isNullOrEmpty(content)) {
                return JsonUtils.resultJson(2, "请填写反馈内容", null);
            }
            String result = procedureService.tb_feedback_add(content, mobile, fromType, clientIp);
            if ("0".equals(result)) {
                return JsonUtils.resultJson(0, "提交成功", null);
            } else {
                return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
            }
        } catch (Exception e) {
            LOG.error("The feedback() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
        }

    }

    /**********************************************************************
     * 语音列表
     * 
     * @param pageno
     *            页数
     * @param limit
     *            每页数
     * @param status
     *            订单状态
     * @param ord
     * @return Json
     * 
     */
    @ResponseBody
    @RequestMapping("/voicelist")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String voicelist(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageno, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int limit, @RequestParam(value = "status", required = false, defaultValue = "0")
    Long status, @RequestParam(value = "ord", required = false, defaultValue = "0")
    int ord) {

        // 从session 获得vehicleid
        Long vehicleInfoId = null;
        vehicleInfoId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
        // //查询车辆的审核状态
        VeVehicleInfo ve = veVehicleInfoService.findOne(vehicleInfoId);
        if (ve.getVeAuthorise() == 1L || ve.getVeAuthorise() == 2L) {
            return JsonUtils.resultJson(3, "您的货车审核中，部分功能受限制", null);
        }

        try {

            // 查询车辆的运费
            Double startPrice = ve.getStartingPrice();
            Double startMileage = ve.getStartingMileage();
            Double mileagePrice = ve.getMileagePrice();

            List<VoiceListDetail> voiceList = nativeService.findVoiceList(vehicleInfoId, pageno, limit);
            LinkedHashMap<String, Object> voiceMap = null;
            List<LinkedHashMap<String, Object>> voiceListResult = new ArrayList<LinkedHashMap<String, Object>>();
            if (voiceList != null && voiceList.size() > 0) {
                for (VoiceListDetail vd : voiceList) {
                    BkVoiceOrder bvo = bkVoiceOrderService.findOne(vd.getId());
                    voiceMap = new LinkedHashMap<String, Object>();
                    Date exTime =null;
//						如果单已经被抢了 抢单大厅就不显示了（把过期时间调前）
                    if(null != bvo.getCompleteOffer() &&bvo.getCompleteOffer()){
                    	
                    	exTime = TimeUtil.getStringToDate("2014-01-01 00:00:00");
                    }else {
//						只有10分钟的抢单时间  这里返回的过期时间改成 创建时间加10分钟 （2014-09-28）
                        exTime = new Date(bvo.getCreateTime().getTime()+600*1000);
//                        voiceMap.put("expire_time", vd.getExpireTime().getTime() / 1000);
                    }
                
                    
                    voiceMap.put("expire_time", exTime.getTime() / 1000);
                    voiceMap.put("now_time", new Date().getTime() / 1000);
                    voiceMap.put("id", vd.getId());
                    voiceMap.put("voice_file", Constant.VOICE_FILE_URL + "/" + vd.getFilePath() + "/" + vd.getFileName());
                    voiceMap.put("order_type", vd.getOrderType() ? 1 : 0);
                    String goodsLength = "";
                    String goodsWidth = "";
                    String goodsHeight = "";
                    String goodsttl = "";
                    String textContent = "";
                    // if (StringUtils.isNullOrEmpty(goodsLength)
                    // && StringUtils.isNullOrEmpty(goodsLength)
                    // && StringUtils.isNullOrEmpty(goodsLength)) {
                    // goodsttl = "";
                    //
                    // }

                    goodsLength = "长" + Utils.format(vd.getGoodsLength()) + "，";
                    goodsWidth = "宽" + Utils.format(vd.getGoodsWidth()) + "，";
                    goodsHeight = "高" + Utils.format(vd.getGoodsHeight()) + "，";
                    if (StringUtils.isNullOrEmpty(vd.getGoodsLength()) || "0".equals(vd.getGoodsLength())) {
                        goodsLength = "";
                    }
                    if (StringUtils.isNullOrEmpty(vd.getGoodsWidth()) || "0".equals(vd.getGoodsWidth())) {
                        goodsWidth = "";
                    }
                    if (StringUtils.isNullOrEmpty(vd.getGoodsHeight()) || "0".equals(vd.getGoodsHeight())) {
                        goodsHeight = "";
                    }

                    String fromToDistance = vd.getFromToDistance();
                    if (!Utils.isEmpty(fromToDistance)) {
                        if (Utils.isNumberOrFloat(fromToDistance)) {
                            Double dis = Double.valueOf(fromToDistance) / 1000;
                            fromToDistance = "，最短距离大约" + String.format("%.2f", dis) + "公里，";
                        }
                    } else
                        fromToDistance = "";
                    // 如果是文本订单
                    if (vd.getOrderType()) {
                        textContent = "我要从" + (Utils.isBusLines(vd.getFromAddress())?"":vd.getFromAddress()) + (Utils.isEmpty(vd.getFromShortAddress()) ? "" : vd.getFromShortAddress())
                                + (Utils.isEmpty(bvo.getFromDetailAddress()) ? "" : bvo.getFromDetailAddress()) + "，运到" +(Utils.isBusLines(vd.getToAddress())?"":vd.getToAddress()) 
                                + (Utils.isEmpty(vd.getToShortAddress()) ? "" : vd.getToShortAddress()) + (Utils.isEmpty(bvo.getToDetailAddress()) ? "" : bvo.getToDetailAddress())
                                + fromToDistance + TimeUtil.getDateString(vd.getExpireTime(), "，发货时间M月d日H点m分") + "，" + goodsttl + goodsLength + goodsWidth + goodsHeight 
                                + (Utils.isEmpty(vd.getDescription())?"":",备注,"+vd.getDescription());
                    }
                    voiceMap.put("text_content", textContent);

                    // 有一个为空 运费就是 0
                    String referencePrice = "0";
                    String distance = vd.getFromToDistance();

                    if (!Utils.isEmpty(distance) && Utils.isNumberOrFloat(distance) && null != startPrice && null != startMileage && null != mileagePrice) {
                        if (Double.valueOf(distance) / 1000 < startMileage) // 如果没有超过起步里程
                                                                            // 预估运费直接起步价
                            referencePrice = startPrice + "";
                        else
                            referencePrice = ((int) (startPrice + (Double.valueOf(distance) / 1000 - startMileage) * mileagePrice)) + "";
                    }
                    voiceMap.put("referencePrice", referencePrice);
                    voiceListResult.add(voiceMap);
                }
                return JsonUtils.resultJson(0, "", voiceListResult);
            } else {
                // 如果查出空
                return JsonUtils.resultJson(0, "数据已加载完，没有更多数据", voiceListResult);
            }

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(2, "服务器忙晕啦，请稍候再试", null);
        }

    }

    /*********************************************************************
     * 订单排行榜
     * 
     * @param beginDate
     *            起始时间
     * @param endDate
     *            终止时间
     * 
     * @return json
     */
    @ResponseBody
    @RequestMapping("/toporder")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String toporder(@RequestParam(value = "begin_date", required = false, defaultValue = "")
    String beginDate, @RequestParam(value = "end_date", required = false, defaultValue = "")
    String endDate) {

        try {
            // 从session 获得vehicleid
            Long vehicleInfoId = 1L;
            vehicleInfoId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());

            // 如果传过来的时间大于今天 返回空 （传过来是当天时间 数据显示昨天时间）
            if (!"".equals(beginDate) && !"".equals(endDate)) {
                if (beginDate.equals(endDate) && TimeUtil.getStringToDate(beginDate, 2).getTime() > new Date().getTime()) {
                    return JsonUtils.resultJson(0, "未到开奖日期", null);
                }
            }
            // 如果传过来的时间是同一天并且是当天时间 时间减一天
            if (beginDate.equals(endDate) && TimeUtil.getDateString(new Date(), "yyyy-MM-dd").equals(beginDate)) {
                beginDate = TimeUtil.getYesterday();
                endDate = beginDate;
            }

            VeVehicleInfo vehicleInfo = veVehicleInfoService.findByVehicleInfoId(vehicleInfoId);
            Long modelId = vehicleInfo.getModelId();
            List<TopOrder> topOrderList = nativeService.findTopOrderList(beginDate, endDate, modelId);

            int selfRank = 0;
            int num = 0;
            LinkedHashMap<String, Object> toporderMap = null;

            List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
            for (int x = 0; x < topOrderList.size(); x++) {
                // Map to = (Map) topOrderList.get(x);
                TopOrder to = topOrderList.get(x);
                toporderMap = new LinkedHashMap<String, Object>();
                // 隐藏车牌号
                String plates = Common.hidePlate(to.getVePlate().toString());
                String username = to.getUsername();
                // 车辆id和自己的吻合
                if ((vehicleInfoId + "").equals(to.getVehicleInfoId())) {
                    plates = "我";
                    selfRank = 1;
                    username = "我";
                }
                num = x + 1;
                toporderMap.put("rank", num);
                toporderMap.put("plates", plates);
                toporderMap.put("total_order", to.getCount());
                toporderMap.put("realname", Utils.isEmpty(username) ? "" : username);
                resultList.add(toporderMap);
            }
            // 如果这段时间没有数据
            if (selfRank == 0) {
                toporderMap = new LinkedHashMap<String, Object>();
                toporderMap.put("rank", "...");
                toporderMap.put("plates", "...");
                toporderMap.put("total_order", "...");
                toporderMap.put("realname", "...");
                String selfPlate = "我";
                resultList.add(toporderMap);
                String orderCount = bkBookingService.findOrderCount(vehicleInfoId, beginDate + " 00:00:00", endDate + " 23:59:59");
                if (!StringUtils.isNullOrEmpty(orderCount) && !orderCount.equals("0")) {
                    List<Map<String, Object>> rankRow = bkBookingService.findVehicleIdAndOrderCount(beginDate, endDate, modelId, vehicleInfoId);
                    int orderRanking = 0;
                    int j = 1;
                    for (Map<String, Object> m : rankRow) {
                        if ((vehicleInfoId + "").equals(m.get("vehicleInfoId").toString())) {
                            orderRanking = j;
                        }
                        j++;
                    }
                    toporderMap = new LinkedHashMap<String, Object>();
                    toporderMap.put("rank", orderRanking);
                    toporderMap.put("plates", selfPlate);
                    toporderMap.put("total_order", orderCount);
                    toporderMap.put("realname", "我");
                    resultList.add(toporderMap);
                } else {

                    for (int i = 0; i < (2 - topOrderList.size()); i++) {
                        toporderMap = new LinkedHashMap<String, Object>();
                        toporderMap.put("rank", "...");
                        toporderMap.put("plates", "...");
                        toporderMap.put("total_order", "...");
                        toporderMap.put("realname", "...");
                        resultList.add(toporderMap);
                    }
                    toporderMap = new LinkedHashMap<String, Object>();
                    toporderMap.put("rank", "--");
                    toporderMap.put("plates", selfPlate);
                    toporderMap.put("total_order", "0");
                    toporderMap.put("realname", "我");
                    resultList.add(toporderMap);

                }
            }
            return JsonUtils.resultJson(0, "成功", resultList);
        } catch (Exception e) {
            LOG.error("The toporder() method invocation exception.", e);
            return JsonUtils.resultJson(2, "服务器忙晕啦，请稍候再试", null);
        }
    }

    /***********************************************************************
     * 车辆评论列表 ***************************************************************
     * 
     * @return ***************************************************************
     */
    @ResponseBody
    @RequestMapping("/vehiclecommentlist")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String vehiclecommentlist(@RequestParam(value = "pagenum", required = false, defaultValue = "0")
    String pagenum, @RequestParam(value = "pagesize", required = false, defaultValue = "10")
    String pagesize, @RequestParam(value = "level", required = false, defaultValue = "")
    String level) {

        try {
            // 从session获取手机号码
            String mobile = null;
            Object obj = session.getAttribute(Constant.SESSION_PL_USER);
            PlUsers plUsers = null;
            if (obj != null) {
                plUsers = (PlUsers) obj;
                mobile = plUsers.getMobile();
            }

            List<VehicleComment> list = procedureService.getVehicleCommentList(mobile, 1, pagenum, pagesize, level);
            return JsonUtils.resultJson(0, "成功", list);
        } catch (Exception e) {
            LOG.error("The vehiclecommentlist() method invocation exception.", e);
            return JsonUtils.resultJson(2, "服务器忙晕啦，请稍候再试", new ArrayList());
        }
    }

    /*****************************************************************
     * 车辆信息详情
     * 
     * @return
     * 
     */
    @ResponseBody
    @RequestMapping("/vehicledetail")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String vehicledetail() {

        // 从session中获取mobile
        String mobile = "";
        Long recommendType = 0L;
        Long recommendId = 0L;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        if (obj != null) {
            PlUsers users = (PlUsers) obj;
            mobile = users.getMobile();
            recommendType = users.getRecommendType();
            recommendId = users.getRecommendId();
        }
        Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
        // 查询该车辆审核状态

        Ve_vehicle_info_users_for_detail vv = null;
        OperationArea op = null;
        String imgPath = Constant.IMG_URL_PRE;
        List<Object> list = new ArrayList<Object>();
        try {
            vv = procedureService.findVehicleDetail(mobile, 1);

            if (null == vv) {
                return JsonUtils.resultJson(2, "车辆信息不存在", new ArrayList<Object>());
            } else {
                String img1 = vv.getImg1();
                String img2 = vv.getImg2();
                String img3 = vv.getImg3();
                String img4 = vv.getImg4();
                vv.setImg1("".equals(img1) ? "" : imgPath + img1);
                vv.setImg2("".equals(img2) ? "" : imgPath + img2);
                vv.setImg3("".equals(img3) ? "" : imgPath + img3);
                vv.setImg4("".equals(img4) ? "" : imgPath + img4);
                op = procedureService.findOperationArea(mobile, 2);
                ArrayList<OperationArea> oplist = new ArrayList<OperationArea>();
                oplist.add(op);
                vv.setOperation(oplist);
                String staffMoble = "";
                VeVehicleInfo vehicle = veVehicleInfoService.findOne(vehicleId);
                if (vehicle != null && vehicle.getStaffId() != null) {
                    TbStaff staff = tbStaffService.findOne(vehicle.getStaffId());

                    if (staff != null && staff.getStaffStatus()) {
                        staffMoble = staff.getStaffTel();
                    }
                }
                vv.setStaff_tel(staffMoble);

                String recommendMobile = "";
                if (recommendType != null && recommendType != 0L && recommendId != null && recommendId != 0L) {
                    // 1 货主 2 车主 3 业务员

                    if (recommendType == 1L) {
                        PlUsersCargo cargo = plusersCargoService.findOne(recommendId);
                        if (cargo != null) {
                            recommendMobile = cargo.getMobile();
                        }

                    } else if (recommendType == 2L) {
                        PlUsers veUser = plUsersService.findOne(recommendId);
                        if (veUser != null) {
                            recommendMobile = veUser.getMobile();
                        }
                    } else if (recommendType == 3L) {
                        TbStaff staff = tbStaffService.findOne(recommendId);
                        if (staff != null) {
                            recommendMobile = staff.getStaffTel();
                        }
                    }
                }
                vv.setRecommendMobile(recommendMobile);
            }
            list.add(vv);

            return JsonUtils.resultJson(0, "成功", vv);
        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
        }

    }

    /*****************************************************************************
     * 
     * 车主抢单
     * 
     * @param longitude
     *            经度
     * @param latitude
     *            纬度
     * @param orderId
     *            订单id
     * @param price
     *            报价
     * 
     * @return json
     */
    @ResponseBody
    @RequestMapping("/offer")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String offer(@RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double longitude, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double latitude, @RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long orderId, @RequestParam(value = "price", required = false, defaultValue = "0")
    Double price) {

        // 从session中获取mobile
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers user = null;
        if (null != obj) {
            user = (PlUsers) obj;
        }
        String mobile = user.getMobile();
        // 从session中获取vehicleId
        Object object = session.getAttribute(Constant.SESSION_VE_VEHICLE_ID);
        Long vehicleId = 0L;
        if (null != object) {
            vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
        }

        if (!Utils.checkOrderId(orderId + "") || orderId < 1) {
            return JsonUtils.resultJson(2, "语音不存在", null);
        }
        // 判断价格合法
        if (!Utils.checkPrice(price + "")) {
            return JsonUtils.resultJson(3, "价格错误", null);
        }

        if (longitude > 180 || longitude < 0 || latitude > 90 || latitude < 0) {
            return JsonUtils.resultJson(4, "抢单失败", null);
        }

        try {
            BkVoiceOrder bkVoiceOrder = bkVoiceOrderService.findOne(orderId);

            if (bkVoiceOrder == null) {
                return JsonUtils.resultJson(2, "语音不存在", null);
            } else {
                if (bkVoiceOrder.getIsCancel()) {
                    return JsonUtils.resultJson(4, "抢单失败，该订单已被取消", null);
                }
            }
            List<BkOrderOffer> orderOfferList = bkOrderOfferService.findByVehicleInfoIdAndOrderId(vehicleId, orderId);
            if (orderOfferList != null && orderOfferList.size() > 0) {
                return JsonUtils.resultJson(4, "已经报过价了，请勿重复报价", null);
            }
            // 已经生成订单
            BkBooking booking = bkBookingService.findByVoiceId(orderId);
            if (booking != null) {
                return JsonUtils.resultJson(4, "抢单失败，订单已被抢", null);
            }
            BkOrderOffer boo = new BkOrderOffer();
            boo.setOrderId(orderId);
            boo.setVehicleInfoId(vehicleId);
            boo.setVeMobile(mobile);
            boo.setPrice(price);
            boo.setLongitude(longitude);
            boo.setLatitude(latitude);
            boo.setCreateTime(new Date());
            boo.setStatus(0L);   //初始化状态  未选：0   选他：1  取消选他：2
            // 是否成功存入
            if (!bkOrderOfferService.save(boo)) {
                return JsonUtils.resultJson(5, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
            }
            // 通知货主有车主抢单
            List<PlUsers> plUsersList = plUsersService.findByMobileAndIsvalid(mobile, "Y");
            String veRealName = plUsersList.get(0).getUserName();
            // 货主cargoid
            LinkedHashMap<String, Object> contentMap = new LinkedHashMap<String, Object>();
            contentMap.put("type", 5);
            contentMap.put("order_id", orderId);
            contentMap.put("msg", "订单有新报价，" + veRealName + "报价" + price + "元");
            xmppService.push(bkVoiceOrder.getUserCargoId() + "", JsonUtils.object2Json(contentMap), "c_");
            return JsonUtils.resultJson(0, "成功", null);
        } catch (Exception e) {
            LOG.error("The offer() method invocation exception.", e);
            return JsonUtils.resultJson(5, "服务器忙晕啦，请稍候再试", new ArrayList<Object>());
        }
    }

    /****************************************************************
     * 获取车辆状态
     * 
     * @return Json
     * 
     */
    @ResponseBody
    @RequestMapping("/vehiclestatus")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String vehiclestatus() {

        try {
            // 从session中获取车辆id
            Long vehicleInfoId = 0L;
            vehicleInfoId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            // 更新车主app版本
            String version = getVersionName();
            if (!StringUtils.isNullOrEmpty(version)) {
                VehicleVersion v_version = vehicleVersionService.findByVehicleId(vehicleInfoId);
                if (null != v_version) {
                    v_version.setVersion(version);
                    vehicleVersionService.save(v_version);
                } else {
                    VehicleVersion vvv = new VehicleVersion();
                    vvv.setVehicleId(vehicleInfoId);
                    vvv.setVersion(version);
                    vehicleVersionService.save(vvv);
                }
            }

            VeVehicleInfo info = veVehicleInfoService.findByVehicleInfoId(vehicleInfoId);
            List<VeStatus> statusList = veStatusService.findByVehicleInfoId(vehicleInfoId);
            Integer totalOrder = Integer.valueOf(bkBookingService.findOrderCount(vehicleInfoId));
            Integer goodOrder = Integer.valueOf(bkBookingService.findGoodLevelOrderCount(vehicleInfoId));
            Integer badOrder = Integer.valueOf(bkBookingService.findBadLevelOrderCount(vehicleInfoId));
            String orderRank = "0";
            if (totalOrder > 0) {
                List<TopOrder> topOrderList = nativeService.findRankOrderList(vehicleInfoId, info.getModelId());
                int i = 1;
                for (TopOrder to : topOrderList) {
                    if ((vehicleInfoId + "").equals(to.getVehicleInfoId())) {
                        orderRank = i + "";
                    }
                    i++;
                }
            } else {
                orderRank = "--";
            }
            // 获取收藏数
            Integer favoriteCount = userFavoriteService.getVehicleFavoriteCount(vehicleInfoId);
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            resultMap.put("photo_thumb", Constant.IMG_URL_PRE + info.getVeHeadPath() + "thumb_" + info.getVeHeadFile());
            resultMap.put("star_level", Utils.getGrade(info.getVeLevel()));
            PlUsers users = plUsersService.findOne(info.getUserId());
            resultMap.put("driver_name", users.getUserName());
            resultMap.put("status", statusList.get(0).getVeStatus());
            resultMap.put("total_order", totalOrder + "");
            String positivePercent = "0.00%";

            if ((goodOrder + badOrder) == 0) {
                positivePercent = "0.00%";
            } else {
                positivePercent = (totalOrder == null || totalOrder == 0) ? "--" : Utils
                        .doubleToSize(((double) goodOrder / ((double) goodOrder + (double) badOrder) * 100) + "", 0) + "%";
            }
            resultMap.put("positive_percent", positivePercent);
            resultMap.put("order_ranking", orderRank);
            resultMap.put("favorite_count", favoriteCount);
            
            
            /**
             * 5点到23点在线时长
             */
            Long starttime = System.currentTimeMillis();
            
            PlUsers user = (PlUsers) session.getAttribute(Constant.SESSION_PL_USER);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 5);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date start = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date end = cal.getTime();
            List<UploadGpsGroup> list = veUploadGpsMGService.getUploadGpsGroupByIdAndTime(start.getTime(), end.getTime(), user.getUserId());
            Long first = 0L;
            Long last = 0L;
            		
            if(null != list && list.size()>0 ){
            	first = list.get(0).getFirst();
            	last = list.get(0).getLast();
            }
            Long endtime = System.currentTimeMillis() - starttime;
            
            LOG.info("查询在线时长 耗时："+endtime);
            
            resultMap.put("online_time", last-first+"");
            
            LOG.info("开始上传坐标时间：:"+TimeUtil.getDateString(new Date(first), "yyyy-MM-dd HH:mm:ss"));
            LOG.info("最后上传坐标时间:" +TimeUtil.getDateString(new Date(last), "yyyy-MM-dd HH:mm:ss"));
            
            LOG.info(JsonUtils.object2Json(resultMap));
            return JsonUtils.resultJson(0, null, resultMap);

        } catch (Exception e) {
            LOG.error("The vehiclestatus() method invocation exception.", e);
            return JsonUtils.resultJson(1, null, new ArrayList<Object>());
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
    @Authorization(type = Constant.SESSION_PL_USER)
    public String notice() {
        try {
            List<TbNews> tbNewsList = tbNewsService.findTopTbNews(1L, new Date(),1L, 0, 3).getContent();
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

    /*****************************************************************
     * 公告详情
     * 
     * @return Json
     * 
     */

    @ResponseBody
    @RequestMapping("/announcedetail")
    @Authorization(type = Constant.SESSION_PL_USER)
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
     * 修改手机号码
     * 
     * @return Json *
     * @throws Exception
     * 
     * 
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/modifymobile")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String modifymobile(@RequestParam(value = "mobile", required = false, defaultValue = "0")
    String mobile, @RequestParam(value = "verifycode", required = false, defaultValue = "0")
    String verifycode) throws Exception {
        if (!Utils.checkMobile(mobile)) {
            return JsonUtils.resultJson(2, "新手机号码错误", null);
        }
        // 从session获取车主手机号码
        String oldMobile = null;
        Long userId = null;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers plUser = null;
        if (obj != null) {
            plUser = (PlUsers) obj;
            oldMobile = plUser.getMobile();
            userId = plUser.getUserId();
        }
        if (mobile.equals(oldMobile)) {
            return JsonUtils.resultJson(4, "新手机和原手机号相同", null);
        }
        if (StringUtils.isNullOrEmpty(verifycode)) {
            return JsonUtils.resultJson(5, "验证码错误", null);
        }
        List<PlUsers> plUsersList = plUsersService.findByMobileAndIsvalid(mobile, "Y");
        if (plUsersList != null && plUsersList.size() > 0) {
            return JsonUtils.resultJson(3, "手机号码已存在", null);
        }
        try {
            SmsAuthentication smsAuth = smsAuthenticationService.findByMobileAndStatus(mobile, false);
            if (null == smsAuth || !smsAuth.getAuthCode().equals(verifycode)) {
                return JsonUtils.resultJson(6, "验证码错误", null);
            }
            Long expire = System.currentTimeMillis() - smsAuth.getSendDt().getTime();
            if (expire > 600000) {
                return JsonUtils.resultJson(6, "验证码已过期", null);
            }
            // TODO 更新两个表 验证码表 和 用户表 需要用到事务
            smsAuth.setStatus(true);
            if (!smsAuthenticationService.save(smsAuth)) {
                return JsonUtils.resultJson(6, "服务器忙晕了，请稍后再试", null);
            }
            PlUsers plUsers = plUsersService.findOne(userId);
            if (plUsers == null) {
                return JsonUtils.resultJson(6, "服务器忙晕了，请稍后再试", null);
            }
            plUsers.setMobile(mobile);
            plUsersService.save(plUsers);
            // 注册
            xmppService.register("v_" + mobile);

            // 新手机号码 写入session
            plUser.setMobile(mobile);
            request.getSession().setAttribute(Constant.SESSION_PL_USER, plUser);

            // 更新搜索引擎
            // StringBuffer sbf = new StringBuffer();
            // sbf.append("{ __Request:\"Update\",");
            // sbf.append("  __Collection:\"Vehicle\",");
            // sbf.append("  __Fields:{ ");
            // sbf.append(" _id:").append(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID)).append(",");
            // sbf.append(" Phone").append(":").append("\"").append(mobile).append("\"");
            // sbf.append("}}");
            // String webserviceAddress = Constant.VEHICLE_WEBSERVICE_URL;
            // String nameSpace = Constant.COSMOS_NAMESPACE;
            // Object[] params = new Object[] { sbf.toString() };
            // Class[] classs = new Class[] { String.class };
            // String result = wsdlService.webserviceCall(webserviceAddress,
            // nameSpace, "request", params, classs);
            // VeUpdateReturnVo vo = (VeUpdateReturnVo)
            // JsonUtils.json2Object(result, VeUpdateReturnVo.class);

            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
            seVo.setVehicleId(vehicleId);
            seVo.setVehiclePhone(mobile);
            VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);

            if (!"Success".equals(vo.getResult())) {
                return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
            }

            return JsonUtils.resultJson(0, "成功", null);
        } catch (Exception e) {
            LOG.error("The modifymobile() method invocation exception.", e);
            throw new RuntimeException();
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

            List<TbNews> tbNewsList = tbNewsService.findTopTbNews(1L, new Date(),1L, pageno, limit).getContent();

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
                    if (!StringUtils.isNullOrEmpty(tn.getCoverimg())) {
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

    /**
     * 日周月 排行榜 列表
     */
    @ResponseBody
    @RequestMapping("/toporderlist")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String toporderlist() {
        Long vehicleId = null;
        vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());

        Map<String, Object> m = new HashMap<String, Object>();
        try {
            // 判断该车是货车 还是面包车
            VeVehicleInfo ve = veVehicleInfoService.findOne(vehicleId);
            Long modelId = ve.getModelId();

            // 只能看自己组的排名 当天没有订单就不显示 订单数>好评率>车辆id

            // 判断该车 是否有完成的有效订单
            String count = nativeService.findIsvalidBookingCount(vehicleId, modelId, null, null);

            if (Method.intVal(count) > 0) {

                // 该车总订单数
                String allCount = count;
                // 该车周订单数 （上周）
                String weekCount = nativeService.findIsvalidBookingCount(vehicleId, modelId, TimeUtil.getLastWeekMonday(), TimeUtil.getLastWeekSunday());
                // 该车月订单数（上月）
                String monthCount = nativeService.findIsvalidBookingCount(vehicleId, modelId, TimeUtil.getLastMonthFirst(), TimeUtil.getLastMonthLast());
                // 该车日订单数 (昨天)
                String dayCount = nativeService.findIsvalidBookingCount(vehicleId, modelId, TimeUtil.getYesterday(), TimeUtil.getYesterday());

                // 订单数大于该车总订单数的排名情况
                List<String> allRankList = nativeService.findVehicleOrderRank(vehicleId, modelId, null, null);
                // 订单数 大于该车上周的订单数的排名情况
                List<String> weekRankList = nativeService.findVehicleOrderRank(vehicleId, modelId, TimeUtil.getLastWeekMonday(), TimeUtil.getLastWeekSunday());
                // 订单数大于该车上月的订单数的排名情况
                List<String> monthRankList = nativeService.findVehicleOrderRank(vehicleId, modelId, TimeUtil.getLastMonthFirst(), TimeUtil.getLastMonthLast());
                // 订单数 大于该车昨天的订单数的排名情况
                List<String> dayRankList = nativeService.findVehicleOrderRank(vehicleId, modelId, TimeUtil.getYesterday(), TimeUtil.getYesterday());

                int orderRank1 = 0; // 总排
                int orderRank2 = 0; // 周排
                int orderRank3 = 0; // 月排
                int orderRank4 = 0; // 日排
                // 在我之前的排名情况
                if (dayRankList.size() > 0) {
                    for (int i = 0; i < dayRankList.size(); i++) {
                        if (dayRankList.get(i).equals(vehicleId + "")) {
                            orderRank4 = i + 1;
                        }
                    }
                } else { // 没有 我是第一
                    // 查询当天有没有订单
                    if (Method.intVal(dayCount) > 0) {
                        orderRank4 = 1;
                    } else {// 当天没有 就不显示
                        orderRank4 = 0;
                    }

                }

                if (weekRankList.size() > 0) {
                    for (int j = 0; j < weekRankList.size(); j++) {
                        if (weekRankList.get(j).equals(vehicleId + "")) {
                            orderRank2 = j + 1;
                        }
                    }
                } else { // 没有 我是第一
                    // 查询当周有没有订单
                    if (Method.intVal(weekCount) > 0) {
                        orderRank2 = 1;
                    } else {// 当天没有 就不显示
                        orderRank2 = 0;
                    }
                }
                if (monthRankList.size() > 0) {
                    for (int k = 0; k < monthRankList.size(); k++) {
                        if (monthRankList.get(k).equals(vehicleId + "")) {
                            orderRank3 = k + 2;
                        }
                    }
                } else { // 没有 我是第一
                    // 查询当月有没有订单
                    if (Method.intVal(monthCount) > 0) {
                        orderRank3 = 1;
                    } else {// 当天没有 就不显示
                        orderRank3 = 0;
                    }

                }

                if (allRankList.size() > 0) {
                    for (int l = 0; l < allRankList.size(); l++) {
                        if (allRankList.get(l).equals(vehicleId + "")) {
                            orderRank1 = l + 1;
                        }
                    }
                } else {// 没有 我是第一
                    orderRank1 = 1;
                }

                if (orderRank4 == 0) {
                    m.put("day_top_num", "--");
                } else {
                    m.put("day_top_num", "<font color='#ff6f00'  >第" + orderRank1 + "名(昨日)</font>");
                }

                if (orderRank2 == 0) {
                    m.put("month_top_num", "--");
                } else {
                    // 临时屏蔽周排名
                    // m.put("month_top_num",
                    // "<font color='#ff6f00'  >第"+orderRank2+"名</font>");
                    m.put("month_top_num", "--");
                }
                if (orderRank3 == 0) {
                    m.put("weekday_top_num", "--");
                } else {
                    // 临时屏蔽月排名
                    // m.put("weekday_top_num",
                    // "<font color='#ff6f00'  >第"+orderRank3+"名</font>");
                    m.put("weekday_top_num", "--");
                }
                m.put("total_top_num", "<font color='#ff6f00' >第" + orderRank1 + "名</font>");
            } else {
                // 没有有效订单
                m.put("day_top_num", "--");
                m.put("month_top_num", "--");
                m.put("weekday_top_num", "--");
                m.put("total_top_num", "--");
            }

            if (m.isEmpty()) {
                JsonUtils.resultJson(2, "获取失败", m);
            }
            StringBuffer sbf = new StringBuffer();
            sbf.append("闪发车天天赢活动，奖励天天有<br>\r\n");
            sbf.append("每天订单完成数第一名的司机 奖 <font color='red'>100</font>元现金。<br>\r\n");
            sbf.append("每周订单完成数第一名的司机 奖 <font color='red'>300</font>元现金。<br>\r\n");
            sbf.append("每月订单完成数前三名的司机 奖 <font color='red'>800</font>、<font color='red'>500</font>、<font color='red'>300</font>元现金。<br>");
            m.put("introduction", sbf.toString());
            return JsonUtils.resultJson(0, "成功", m);

        } catch (Exception e) {
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍后再试", null);
        }
    }

    /**
     * 获取服务器时间
     */
    @ResponseBody
    @RequestMapping("/getservicetime")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String getservicetime() {
        Map<String, Object> m = new HashMap<String, Object>();
        Date now = new Date();
        m.put("now_time", now.getTime() / 1000);
        return JsonUtils.resultJson(0, "成功", m);
    }

    /**
     * 车主批量上报车辆坐标
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @Transactional
    @RequestMapping("/reportmorelocation")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String reportmorelocation(@RequestParam(value = "jsoninfo", required = false, defaultValue = "")
    String jsoninfo, @RequestParam(value = "phonetime", required = false, defaultValue = "0")
    Long phonetime) {

        PlUsers user = (PlUsers) session.getAttribute(Constant.SESSION_PL_USER);
        Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
        String mobile = "";
        if (user != null) {
            mobile = user.getMobile();
        }

        try {
            List<Map<String, Object>> list = null;
            list = (List<Map<String, Object>>) JsonUtils.json2Object(jsoninfo, List.class);
            if (list == null || list.size() == 0) {
                return JsonUtils.resultJson(4, "传递数据有误", null);
            }
            if (phonetime == null || phonetime == 0) {
                return JsonUtils.resultJson(5, "手机当前时间有误", null);
            }
            Double lastLongitude = null;
            Double lastLatitude = null;
            Long maxUptime = 0L;
            List<VeUploadGps> insertList = new ArrayList<VeUploadGps>();
            List<VeUploadGpsMG> mongoList = new ArrayList<VeUploadGpsMG>();
            for (Map<String, Object> map : list) {
                Double longitude = Double.valueOf(map.get("y").toString());
                Double latitude = Double.valueOf(map.get("x").toString());
                
                //如果经纬度为0 弃掉数据
                if(latitude == 0 ||longitude == 0 ){
                	
                	continue;
                	
                }
                
                String uptime = map.get("t").toString();
                // 当前时间 时间戳
                Long now = System.currentTimeMillis() / 1000;
                // 服务器时间 与手机当前时间的时间差
                Long timeTemp = now - phonetime;

                Date reporttime = new Date((Long.valueOf(map.get("t").toString()) + timeTemp) * 1000);
                Date dynamics = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
                // 如果时间相差太大，则弃掉本次上传数据
                if (reporttime.getTime() < dynamics.getTime()) {
                    VeUploadGps uploadGps = new VeUploadGps();
                    uploadGps.setVeUploadId(vehicleId);
                    uploadGps.setVeModel(mobile);
                    uploadGps.setLatitude(latitude + "");
                    uploadGps.setLongitude(longitude + "");
                    uploadGps.setUptime(reporttime);
                    insertList.add(uploadGps);
                    // insertValues
                    // +="("+mobile+","+longitude+","+latitude+",'"+reporttime+"'),";
                    // 如果时间 大于上个时间 经纬度保存下来
                    if (reporttime.getTime() > maxUptime) {
                        maxUptime = reporttime.getTime();
                        lastLongitude = longitude;
                        lastLatitude = latitude;
                    }

                    // TODO 更新实时坐标（mongo）
                    VeUploadGpsMG gpsMg = new VeUploadGpsMG();
                    gpsMg.setLng(longitude + "");
                    gpsMg.setLat(latitude + "");
                    gpsMg.setUserId(user.getUserId());
                    gpsMg.setVehicleId(vehicleId);
                    gpsMg.setMobile(mobile);
                    gpsMg.setUptime(reporttime.getTime());
                    mongoList.add(gpsMg);

                }
            }
            veUploadGpsMGService.save(mongoList); // 保存到MONGO
            if (veUploadGpsService.saveMultiGPSInfo(insertList)) {
                // 更新车辆信息表 当前经纬度
                VeVehicleInfo ve = veVehicleInfoService.findOne(vehicleId);
                ve.setVeLastLatitude(lastLatitude);
                ve.setVeLastLongitude(lastLongitude);
                veVehicleInfoService.save(ve);

                SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
                Double[] d = new Double[2];
                d[0] = Double.valueOf(lastLongitude);
                d[1] = Double.valueOf(lastLatitude);
                seVo.setVehicleId(vehicleId);
                seVo.setLocation(d);
                seVo.setLastTime(maxUptime);
                VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);

                if (!"Success".equals(vo.getResult())) {
                    return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
                } else {
                    return JsonUtils.resultJson(0, "上报成功", null);
                }
            } else {
                return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
            }

        } catch (Exception e) {
            LOG.error("The reportmorelocation() method invocation exception.", e);
            throw new RuntimeException();
        }

    }

    /**
     * 更新上报车辆运费
     */
    @ResponseBody
    @RequestMapping("/updatevehiclefreight")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String updatevehiclefreight(@RequestParam(value = "starting_price", required = false, defaultValue = "0")
    Double starting_price, @RequestParam(value = "starting_mileage", required = false, defaultValue = "0")
    Double starting_mileage, @RequestParam(value = "mileage_price", required = false, defaultValue = "0")
    Double mileage_price) {
        if (starting_price == 0 || null == starting_price) {
            return JsonUtils.resultJson(3, "起步价错误", null);
        }
        if (starting_mileage == 0 || null == starting_mileage) {
            return JsonUtils.resultJson(4, "起步公里错误", null);
        }
        if (mileage_price == 0 || null == mileage_price) {
            return JsonUtils.resultJson(5, "里程单价错误", null);
        }
        Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
        try {

            VeVehicleInfo vehicle = veVehicleInfoService.findOne(vehicleId);
            vehicle.setStartingPrice(starting_price);
            vehicle.setStartingMileage(starting_mileage);
            vehicle.setMileagePrice(mileage_price);
            // 保存运费信息
            if (veVehicleInfoService.save(vehicle)) {
                // StringBuffer sbf = new StringBuffer();
                // // 更新搜索引擎
                // sbf.append("");
                // sbf.append("{__Request:\"Update\" ,__Collection:\"Vehicle\",__Fields:{");
                // sbf.append(" _id:").append(vehicleId).append(",");
                // sbf.append(" StartingPrice:").append(starting_price).append(",");
                // sbf.append(" StartingMileage:").append(starting_mileage).append(",");
                // sbf.append(" MileagePrice:").append(mileage_price);
                // sbf.append("} }");
                // LOG.info("SE requestJson is :" + sbf.toString());
                // String webserviceAddress = Constant.VEHICLE_WEBSERVICE_URL;
                // String nameSpace = Constant.COSMOS_NAMESPACE;
                // Object[] params = new Object[] { sbf.toString() };
                // Class[] classs = new Class[] { String.class };
                // String result = wsdlService.webserviceCall(webserviceAddress,
                // nameSpace, "request", params, classs);
                // LOG.info("SE returnJson is " + result);
                // VeUpdateReturnVo vo = (VeUpdateReturnVo)
                // JsonUtils.json2Object(result, VeUpdateReturnVo.class);

                SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
                seVo.setVehicleId(vehicleId);
                seVo.setStartingPrice(starting_price);
                seVo.setStartingMileage(starting_mileage);
                seVo.setMileagePrice(mileage_price);
                VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);

                if (!"Success".equals(vo.getResult())) {
                    return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
                } else {
                    return JsonUtils.resultJson(0, "上报成功", null);
                }
            } else {
                return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
            }
        } catch (Exception e) {
            LOG.error("The updatevehiclefreight() method invocation exception.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取车辆运费
     */
    @ResponseBody
    @RequestMapping("/getvehiclefreight")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String getvehiclefreight() {
        Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
        try {
            VeVehicleInfo vehicle = veVehicleInfoService.findOne(vehicleId);
            if (vehicle == null) {
                return JsonUtils.resultJson(4, "没有找到相关车辆", null);
            }
            Double startingPrice = vehicle.getStartingPrice();
            Double startingMileage = vehicle.getStartingMileage();
            Double mileagePrice = vehicle.getMileagePrice();
            Map<String, Object> dataMap = new HashMap<String, Object>();
            if (startingPrice != null && startingMileage != null && mileagePrice != null) {
                dataMap.put("starting_price", startingPrice);
                dataMap.put("starting_mileage", startingMileage);
                dataMap.put("mileage_price", mileagePrice);
            }
            return JsonUtils.resultJson(0, "成功", dataMap);

        } catch (Exception e) {
            LOG.error("The getvehiclefreight() method invocation exception.", e);
            return JsonUtils.resultJson(3, "服务器忙晕啦，请稍候再试", null);
        }
    }

    /********************************************************************
     * 车辆信息更新
     * 
     * @return Json
     * 
     */

    @SuppressWarnings("deprecation")
    @ResponseBody
    @Transactional
    @RequestMapping("/nof/updatevehicleinfo")
    @Authorization(type = Constant.SESSION_PL_USER)
    public String nofUpdatevehicleinfo(@RequestParam(value = "p_gps_x", required = false, defaultValue = "0")
    Double p_gps_x, @RequestParam(value = "p_gps_y", required = false, defaultValue = "0")
    Double p_gps_y, @RequestParam(value = "p_resident_id", required = false, defaultValue = "0")
    Long p_resident_id, @RequestParam(value = "p_resident_desc", required = false, defaultValue = "")
    String p_resident_desc, @RequestParam(value = "p_block_1_from", required = false, defaultValue = "0")
    Long p_block_1_from, @RequestParam(value = "p_block_2_from", required = false, defaultValue = "0")
    Long p_block_2_from, @RequestParam(value = "p_block_3_from", required = false, defaultValue = "0")
    Long p_block_3_from, @RequestParam(value = "p_block_4_from", required = false, defaultValue = "0")
    Long p_block_4_from, @RequestParam(value = "p_block_1_to", required = false, defaultValue = "0")
    Long p_block_1_to, @RequestParam(value = "p_block_2_to", required = false, defaultValue = "0")
    Long p_block_2_to, @RequestParam(value = "p_block_3_to", required = false, defaultValue = "0")
    Long p_block_3_to, @RequestParam(value = "p_block_4_to", required = false, defaultValue = "0")
    Long p_block_4_to, @RequestParam(value = "p_cargo_list", required = false, defaultValue = "")
    String p_cargo_list, @RequestParam(value = "p_user_name", required = false, defaultValue = "")
    String p_user_name, @RequestParam(value = "p_comment", required = false, defaultValue = "")
    String p_comment, @RequestParam(value = "p_idcard", required = false, defaultValue = "")
    String p_idcard, @RequestParam(value = "p_vin_number", required = false, defaultValue = "")
    String p_vin_number, @RequestParam(value = "staff_tel", required = false, defaultValue = "")
    String p_staff_moble

    ) {

        // 从session获取车主手机号码

        String p_old_mobile = null;
        Object obj = session.getAttribute(Constant.SESSION_PL_USER);
        PlUsers plUsers = null;
        if (obj != null) {
            plUsers = (PlUsers) obj;
            p_old_mobile = plUsers.getMobile();
        }

        String p_mobile = p_old_mobile;

        if (!Utils.checkMobile(p_old_mobile)) {
            return JsonUtils.resultJson(2, "旧的手机号码错误", null);
        }
        if (!Utils.checkMobile(p_mobile)) {
            return JsonUtils.resultJson(3, "新的手机号码错误", null);
        }
        if ((p_resident_id == null || p_resident_id == 0) || "".equals(p_resident_desc)) {
            return JsonUtils.resultJson(4, "驻车地为空", null);
        }
        if ((null == p_gps_x || 0 == p_gps_x) && (null == p_gps_y || 0 == p_gps_y)) {
            return JsonUtils.resultJson(5, "驻车地经纬度为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_cargo_list)) {
            return JsonUtils.resultJson(6, "擅长货类为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_user_name)) {
            return JsonUtils.resultJson(7, "司机姓名为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_vin_number)) {
            return JsonUtils.resultJson(10, "车架号为空", null);
        }
        if (StringUtils.isNullOrEmpty(p_idcard)) {
            return JsonUtils.resultJson(11, "身份证号码为空", null);
        }
        try {
            Long vehicleId = Long.valueOf(session.getAttribute(Constant.SESSION_VE_VEHICLE_ID).toString());
            VeVehicleInfo vehicle = veVehicleInfoService.findOne(vehicleId);
            if (null == vehicle || null == vehicle.getVeAuthorise() || vehicle.getVeAuthorise() != 0) {
                return JsonUtils.resultJson(12, "未审核车辆无法修改", null);
            }
            if (!StringUtils.isNullOrEmpty(p_staff_moble)) {

                // 去掉空格神马的
                p_staff_moble = p_staff_moble.replaceAll(" ", "");

                TbStaff staff = tbStaffService.findByStaffTelAndStatus(p_staff_moble, true);
                if (null == staff) {
                    staff = tbStaffService.findOne(Long.valueOf(p_staff_moble));

                }
                if (null == staff || !staff.getStaffStatus()) {
                    return JsonUtils.resultJson(11, "推荐人不是业务员", null);
                }
                vehicle.setStaffId(staff.getStaffId());
                if (!veVehicleInfoService.save(vehicle)) {
                    return JsonUtils.resultJson(3, "服务器忙晕了  请稍后再试", null);
                }
            }

            Long user_id = null;
            List<PlUsers> plUsers_list = plUsersService.findByMobileAndIsvalid(p_old_mobile, "Y");
            if (plUsers_list != null && plUsers_list.size() > 0) {
                user_id = plUsers_list.get(0).getUserId();
            }

            // 根据userid 查询车辆信息
            List<VeVehicleInfo> vehicleInfo_List = veVehicleInfoService.findVehicleByUserId(user_id);
            VeVehicleInfo ve_info = null;
            if (vehicleInfo_List != null && vehicleInfo_List.size() > 0) {
                ve_info = vehicleInfo_List.get(0);
                // 如果第一次修改，保存原来的信息到日志表
                List<VeVehicleModifyLog> veVehicleModifyLogList = veVehicleModifyLogService.findByVehicleInfoId(ve_info.getVehicleInfoId());
                VeVehicleModifyLog veVehicleModifyLog = null;
                if (veVehicleModifyLogList != null && veVehicleModifyLogList.size() > 0) {
                    veVehicleModifyLog = veVehicleModifyLogList.get(0);
                }
                VeVehicleModifyLog vmLog = null;
                if (null == veVehicleModifyLog) {
                    vmLog = new VeVehicleModifyLog();
                    vmLog.setVehicleInfoId(ve_info.getVehicleInfoId());
                    vmLog.setBrandId(ve_info.getBrandId());
                    vmLog.setModelId(ve_info.getModelId());
                    vmLog.setVePlates(ve_info.getVePlates());
                    vmLog.setCargoTypeList(ve_info.getCargoTypeList());
                    vmLog.setVeResidentX(ve_info.getVeResidentX());
                    vmLog.setVeResidentY(ve_info.getVeResidentY());
                    vmLog.setVeResidentAddressId(ve_info.getVeResidentAddressId());
                    vmLog.setVeResidentDesc(ve_info.getVeResidentDesc());
                    vmLog.setHidePlates(ve_info.getHidePlates());
                    vmLog.setUserName(plUsers_list.get(0).getUserName());
                    vmLog.setMobile(p_old_mobile);
                    List<VeBlockInfo> blockInfoList = veBlockInfoService.findByVehicleInfoId(ve_info.getVehicleInfoId());
                    int k = 1;
                    for (VeBlockInfo block : blockInfoList) {
                        if (k == 1) {
                            vmLog.setFromAddress1Id(block.getFromAddressId());
                            vmLog.setToAddress1Id(block.getToAddressId());
                        }
                        if (k == 2) {
                            vmLog.setFromAddress2Id(block.getFromAddressId());
                            vmLog.setToAddress2Id(block.getToAddressId());
                        }
                        if (k == 3) {
                            vmLog.setFromAddress3Id(block.getFromAddressId());
                            vmLog.setToAddress3Id(block.getToAddressId());
                        }
                        if (k == 4) {
                            vmLog.setFromAddress4Id(block.getFromAddressId());
                            vmLog.setToAddress4Id(block.getToAddressId());
                        }
                        k++;
                    }
                    vmLog.setComment(ve_info.getComment());
                    vmLog.setModifyUserId(0L);
                    veVehicleModifyLogService.save(vmLog);
                }
                // 记录车辆修改日志
                VeVehicleModifyLog vvmLog = new VeVehicleModifyLog();
                vvmLog.setVehicleInfoId(ve_info.getVehicleInfoId());
                vvmLog.setBrandId(ve_info.getBrandId());
                vvmLog.setModelId(ve_info.getModelId());
                vvmLog.setVePlates(ve_info.getVePlates());
                vvmLog.setCargoTypeList(p_cargo_list);
                vvmLog.setVeResidentX(p_gps_x);
                vvmLog.setVeResidentY(p_gps_y);
                vvmLog.setVeResidentAddressId(p_resident_id);
                vvmLog.setVeResidentDesc(p_resident_desc);
                vvmLog.setHidePlates(ve_info.getHidePlates());
                vvmLog.setUserName(p_user_name);
                vvmLog.setMobile(p_mobile);
                vvmLog.setFromAddress1Id(p_block_1_from);
                vvmLog.setToAddress1Id(p_block_1_to);
                vvmLog.setFromAddress2Id(p_block_2_from);
                vvmLog.setToAddress2Id(p_block_2_to);
                vvmLog.setFromAddress3Id(p_block_3_from);
                vvmLog.setToAddress3Id(p_block_3_to);
                vvmLog.setFromAddress4Id(p_block_4_from);
                vvmLog.setToAddress4Id(p_block_4_to);
                vvmLog.setComment(p_comment);
                vvmLog.setModifyTime(new Date());
                veVehicleModifyLogService.save(vvmLog);
            }

            String result = procedureService.updateVehicleInfo(p_old_mobile, p_gps_x, p_gps_y, p_resident_id, p_resident_desc, p_block_1_from, p_block_1_to, p_block_2_from,
                    p_block_2_to, p_block_3_from, p_block_3_to, p_block_4_from, p_block_4_to, p_cargo_list, p_user_name, p_mobile, "", "", "", "", p_comment, p_vin_number,
                    p_idcard);
            if ("0".equals(result)) {
                SearchUpdateVehicleInfoVo seVo = new SearchUpdateVehicleInfoVo();
                Double[] d = new Double[2];
                d[0] = ve_info.getVeLastLongitude();
                d[1] = ve_info.getVeLastLatitude();
                seVo.setVehicleId(vehicleId);
                seVo.setVehiclePhone(p_mobile);
                seVo.setDriveName(p_user_name);
                seVo.setCargoList(p_cargo_list);
                seVo.setDomicile(d);
                VeUpdateReturnVo vo = searchEngineService.updateVehilceInfo(seVo);

                return JsonUtils.resultJson(0, "上报成功", null);
            }
            List<Object> errList = new ArrayList<Object>();
            errList.add(result);
            return JsonUtils.resultJson(8, "更新失败", errList);

        } catch (Exception e) {
            LOG.error("The nofUpdatevehicleinfo() method invocation exception.", e);
            throw new RuntimeException(e);
        }
    }

    /*****************************************************************************
     * 注册车辆信息
     * 
     * @return Json.
     * 
     */
    @ResponseBody
    @RequestMapping("/nof/addvehicle")
    public String nofAddvehicle(@RequestParam(value = "platenumber", required = false, defaultValue = "")
    String platenumber, @RequestParam(value = "cartypeID", required = false, defaultValue = "0")
    Integer cartypeID, @RequestParam(value = "carbrandID", required = false, defaultValue = "0")
    Integer carbrandID, @RequestParam(value = "chuchangnianfen", required = false, defaultValue = "")
    String chuchangnianfen, @RequestParam(value = "zhuchedi", required = false, defaultValue = "")
    String zhuchedi, @RequestParam(value = "qujiedaoid", required = false, defaultValue = "0")
    Integer qujiedaoid, @RequestParam(value = "gps_longitude", required = false, defaultValue = "0")
    Double gps_longitude, @RequestParam(value = "gps_latitude", required = false, defaultValue = "0")
    Double gps_latitude,
    // 营运区域
            @RequestParam(value = "from_city1_id", required = false, defaultValue = "0")
            Integer from_city1_id, @RequestParam(value = "to_city1_id", required = false, defaultValue = "0")
            Integer to_city1_id, @RequestParam(value = "from_city2_id", required = false, defaultValue = "0")
            Integer from_city2_id, @RequestParam(value = "to_city2_id", required = false, defaultValue = "0")
            Integer to_city2_id, @RequestParam(value = "from_city3_id", required = false, defaultValue = "0")
            Integer from_city3_id, @RequestParam(value = "to_city3_id", required = false, defaultValue = "0")
            Integer to_city3_id, @RequestParam(value = "from_city4_id", required = false, defaultValue = "0")
            Integer from_city4_id, @RequestParam(value = "to_city4_id", required = false, defaultValue = "0")
            Integer to_city4_id, @RequestParam(value = "shanchanghuolei", required = false, defaultValue = "")
            String shanchanghuolei, @RequestParam(value = "personorcompany", required = false, defaultValue = "0")
            Integer personorcompany, @RequestParam(value = "person_username", required = false, defaultValue = "")
            String person_username, @RequestParam(value = "person_telno", required = false, defaultValue = "")
            String person_telno, @RequestParam(value = "driver_username", required = false, defaultValue = "")
            String driver_username, @RequestParam(value = "driver_telno", required = false, defaultValue = "")
            String driver_telno, @RequestParam(value = "company_name", required = false, defaultValue = "")
            String company_name, @RequestParam(value = "company_tel", required = false, defaultValue = "")
            String company_tel, @RequestParam(value = "company_address", required = false, defaultValue = "")
            String company_address,

            @RequestParam(value = "vin", required = false, defaultValue = "")
            String vin, @RequestParam(value = "idcard", required = false, defaultValue = "")
            String idcard, @RequestParam(value = "comment", required = false, defaultValue = "")
            String comment, @RequestParam(value = "recommend", required = false, defaultValue = "")
            String recommend) {
        if ("".equals(platenumber.trim())) {
            return JsonUtils.resultJson(1, "车牌号码为空", null);
        }
        if (null == cartypeID || 0 == cartypeID) {
            return JsonUtils.resultJson(2, "车型错误", null);
        }
        if (null == carbrandID || 0 == carbrandID) {
            return JsonUtils.resultJson(3, "品牌错误", null);
        }
        if ("".equals(zhuchedi.trim())) {
            return JsonUtils.resultJson(4, "驻车地为空", null);
        }
        if ((to_city1_id == null || 0 == to_city1_id) && (to_city2_id == null || 0 == to_city2_id) && (to_city3_id == null || 0 == to_city3_id)
                && (to_city4_id == null || 0 == to_city4_id)) {
            return JsonUtils.resultJson(5, "营运区域为空", null);
        }
        if ("".equals(shanchanghuolei.trim())) {
            return JsonUtils.resultJson(6, "擅长货类为空", null);
        }
        if (personorcompany != 1 && personorcompany != 2) {
            return JsonUtils.resultJson(7, "营运单位错误", null);
        }
        if ("".equals(person_username.trim())) {
            return JsonUtils.resultJson(8, "司机姓名为空", null);
        }
        if (!Utils.checkMobile(person_telno)) {
            return JsonUtils.resultJson(9, "司机手机错误", null);
        }

        if (!"".equals(recommend.trim()) && !Utils.checkMobile(recommend)) {
            return JsonUtils.resultJson(16, "推荐人手机号码错误", null);
        }
        // 个人
        if (personorcompany == 1) {
            driver_username = null;
            driver_telno = null;
            company_name = null;
            company_tel = null;
            company_address = null;
        } else if (personorcompany == 2) {
            driver_username = person_username;
            driver_telno = person_telno;
            person_username = null;
            person_telno = null;
        }
        String basePath = Constant.AUDIO_PATH + "/car";
        Date now = new Date();
        String year = now.getYear() + "";
        String month = now.getMonth() + "";
        String day = now.getDay() + "";
        String hour = now.getHours() + "";
        // TODO 图片处理
        String fullPath = UploadUtils.getAudioPath(basePath, year, month, day, hour);
        try {

            String result = procedureService.uploadVehicleInfo(platenumber, cartypeID, carbrandID, chuchangnianfen, qujiedaoid, zhuchedi, from_city1_id, to_city1_id,
                    from_city2_id, to_city2_id, from_city3_id, to_city3_id, from_city4_id, to_city4_id, gps_longitude, gps_latitude, shanchanghuolei, personorcompany,
                    person_username, person_telno, driver_username, driver_telno, company_name, company_tel, company_address, "", "", "", "", vin, idcard, comment, recommend);
            if ("0".equals(result)) {
                return JsonUtils.resultJson(0, "成功 ", null);
            } else if ("1".equals(result)) {
                return JsonUtils.resultJson(17, "司机手机号码已存在 ", null);
            } else if ("2".equals(result)) {
                return JsonUtils.resultJson(16, "推荐人手机号不存在 ", null);
            } else {
                return JsonUtils.resultJson(13, "'服务器异常，错误码: " + result, null);
            }

        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            return JsonUtils.resultJson(13, "'服务器异常", null);
        }
    }

    /**
     * 上传通讯录信息
     * 
     * @param jsoninfo
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("uploadbl")
    @Transactional
    @Authorization(type = Constant.SESSION_PL_USER)
    public String uploadbl(@RequestParam(value = "jsoninfo", required = false, defaultValue = "")
    String jsoninfo) {

        PlUsers plusers = (PlUsers) session.getAttribute(Constant.SESSION_PL_USER);
        try {

            // 查当前数据库里车主的通讯录
            Set<String> contactSet = contactsService.findByUserIdAndType(plusers.getUserId(), com.store.api.mongo.entity.enumeration.UserType.owners);

            List<Map<String, Object>> list = null;
            if (!Utils.isEmpty(jsoninfo)) {
                list = (List<Map<String, Object>>) JsonUtils.json2Object(jsoninfo, List.class);

                if (list != null && list.size() > 0) {
                    // 客户端上传的车主通讯录
                    List<Contact> contactList = new ArrayList<Contact>();
                    Contact contact = null;
                    for (Map<String, Object> map : list) {
                        contact = new Contact();
                        if (null != map.get("m") && null != map.get("n") && !contactSet.contains(map.get("m").toString())) {
                            contact.setMobile(map.get("m").toString());
                            contact.setName(Utils.isChinessNumEnglish(map.get("n").toString()));
                            contact.setUserType(com.store.api.mongo.entity.enumeration.UserType.owners); // 车主
                            contact.setUserId(plusers.getUserId());
                            contact.setCreateDate(new Date().getTime());
                            contactList.add(contact);

                            contactSet.add(map.get("m").toString());
                        }
                    }
                    contactsService.save(contactList);
                }
            }

            List<String> sfcUserList = new ArrayList<String>();
            // 判断是否已加入闪发车
            Set<String> vehicleMobileSet = plUsersService.findAllWithMobileSet();
            Set<String> cargoMobileSet = plusersCargoService.findAllWithMobileSet();
            Set<String> staffMobileSet = tbStaffService.findAllWithMobileSet();

            for (String c : contactSet) {
                // 现在没有要区分是车主还是货主
                if (vehicleMobileSet.contains(c)) {
                    sfcUserList.add(c);
                } else if (cargoMobileSet.contains(c)) {
                    sfcUserList.add(c);
                } else if (staffMobileSet.contains(c)) {
                    sfcUserList.add(c);
                }
            }
            return JsonUtils.resultJson(0, "成功", sfcUserList);
        } catch (Exception e) {
            LOG.error("The uploadbl() method invocation exception.", e);
            throw new RuntimeException();
        }
    }
    
    
    

}
