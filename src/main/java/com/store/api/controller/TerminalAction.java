package com.store.api.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.store.api.common.Common;
import com.store.api.common.Constant;
import com.store.api.common.MD5;
import com.store.api.mongo.entity.CargoAddress;
import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.VeUploadGpsMG;
import com.store.api.mongo.service.CargoAddressService;
import com.store.api.mongo.service.ContactsService;
import com.store.api.mongo.service.VeUploadGpsMGService;
import com.store.api.mysql.entity.BkBooking;
import com.store.api.mysql.entity.BkInquiryLog;
import com.store.api.mysql.entity.BkOrderOffer;
import com.store.api.mysql.entity.BkVoiceOrder;
import com.store.api.mysql.entity.BkVoicePhoto;
import com.store.api.mysql.entity.BkVoiceReceived;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.PlUsersCargo;
import com.store.api.mysql.entity.RetResult;
import com.store.api.mysql.entity.SmsAuthentication;
import com.store.api.mysql.entity.TbFeedback;
import com.store.api.mysql.entity.TbStaff;
import com.store.api.mysql.entity.TbTicketInfo;
import com.store.api.mysql.entity.TbUserBalance;
import com.store.api.mysql.entity.UsersCargoFavorite;
import com.store.api.mysql.entity.VeVehicleInfo;
import com.store.api.mysql.entity.enumeration.TicketType;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.entity.procedure.VeVehicleInfoDetail;
import com.store.api.mysql.entity.push.SpreadPushVo;
import com.store.api.mysql.entity.search.SearchParamVo;
import com.store.api.mysql.entity.webService.VehicleInfo;
import com.store.api.mysql.entity.webService.VehicleSearchReturnVo;
import com.store.api.mysql.entity.webService.locationInfo;
import com.store.api.mysql.service.BkBookingService;
import com.store.api.mysql.service.BkInquiryLogService;
import com.store.api.mysql.service.BkOrderOfferService;
import com.store.api.mysql.service.BkVoiceOrderService;
import com.store.api.mysql.service.BkVoicePhotoService;
import com.store.api.mysql.service.BkVoiceReceivedService;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.PlUsersService;
import com.store.api.mysql.service.PlusersCargoService;
import com.store.api.mysql.service.ProcedureService;
import com.store.api.mysql.service.PushAfterOrderService;
import com.store.api.mysql.service.SearchEngineService;
import com.store.api.mysql.service.SmsAuthenticationService;
import com.store.api.mysql.service.SmsSendService;
import com.store.api.mysql.service.SpreadPushService;
import com.store.api.mysql.service.TbFeedBackService;
import com.store.api.mysql.service.TbStaffService;
import com.store.api.mysql.service.TbTicketInfoService;
import com.store.api.mysql.service.TbUserBalanceService;
import com.store.api.mysql.service.UserFavoriteService;
import com.store.api.mysql.service.VeStatusService;
import com.store.api.mysql.service.VeVehicleInfoService;
import com.store.api.mysql.service.XmppService;
import com.store.api.session.SessionService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.ConstantUtil;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.TimeUtil;
import com.store.api.utils.Utils;
import com.store.api.utils.security.SecurityUtil;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/appterminal")
public class TerminalAction extends BaseAction {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserFavoriteService favoriteService;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private BkBookingService bkBookingService;

    @Autowired
    private BkInquiryLogService bkInquiryLogService;

    @Autowired
    private PlUsersService plUsersService;

    @Autowired
    private VeVehicleInfoService veVehicleInfoService;

    @Autowired
    private TbFeedBackService tbFeedBackService;

    @Autowired
    private XmppService xmppService;

    @Autowired
    private BkOrderOfferService bkOrderOfferService;

    @Autowired
    private BkVoiceOrderService bkVoiceOrderService;

    @Autowired
    private SearchEngineService searchEngineService;

    @Autowired
    private BkVoiceReceivedService bkVoiceReceivedService;

    @Autowired
    private PlusersCargoService plusersCargoService;

    @Autowired
    private VeStatusService veStatusService;

    @Autowired
    private VeUploadGpsMGService veUploadGpsService;

    @Autowired
    private SmsAuthenticationService smsAuthenticationService;

    @Autowired
    private TbStaffService tbStaffService;

    @Autowired
    private BkVoicePhotoService bkVoicePhotoService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private TbTicketInfoService tbTicketInfoService;

    @Autowired
    private CreditAndTicketManagementService creditAndTicketManagementService;

    @Autowired
    private TbUserBalanceService tbUserBalanceService;

    @Autowired
    private CargoAddressService cargoAddressService;

    @Autowired
    private SpreadPushService spreadPushService;
    
    @Autowired
    private PushAfterOrderService pushAfterOrderService;

    private Map<String, Object> result = new HashMap<String, Object>();

    /**
     * 货车搜索列表接口
     * 
     * @param s_x
     *            出发地x坐标
     * @param s_y
     *            出发地y坐标
     * @param d_x
     *            目的地x坐标
     * @param d_y
     *            目的地y坐标
     * @param s_r
     *            出发地半径
     * @param d_r
     *            目的地半径
     * @param cartype_id
     *            车型ID,如果ID值为0,则7种常用车型的3种工作状态的车各返回一辆
     * @return
     */
    @ResponseBody
    @RequestMapping("/appresultlist")
    public Map<String, Object> resultList(@RequestParam(value = "s_x", required = false, defaultValue = "")
    Double s_x, @RequestParam(value = "s_y", required = false, defaultValue = "")
    Double s_y, @RequestParam(value = "d_x", required = false, defaultValue = "0")
    Double d_x, @RequestParam(value = "d_y", required = false, defaultValue = "0")
    Double d_y, @RequestParam(value = "s_r", required = false, defaultValue = "0")
    Long s_r, @RequestParam(value = "d_r", required = false, defaultValue = "0")
    Long d_r, @RequestParam(value = "cartype_id", required = false, defaultValue = "")
    String carTypeId, @RequestParam(value = "sort", required = false, defaultValue = "")
    String sort) {

        if (null == s_x || null == s_y) {
            result.put("errorcode", "2");
            result.put("info", "出发地经纬度为空");
            result.put("data", new String[] {});
            return result;
        }
        int sortnum = 0;
        if (!Utils.isEmpty(sort)) {
            if (sort.equalsIgnoreCase("distance"))
                sortnum = 3;
            else if (sort.equalsIgnoreCase("star"))
                sortnum = 1;
            else if (sort.equalsIgnoreCase("price"))
                sortnum = 2;
        }

        Map<String, Object> priority = new HashMap<String, Object>();
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = null;
        if (null != obj) {
            user = (PlUsersCargo) obj;
            PlUsersCargo thisUser = plusersCargoService.findOne(user.getUserCargoId());
            thisUser.setVersion(getVersionName());
            if (user.getClientType() == null || user.getClientType() == 0) {
                thisUser.setClientType(getClientType());
            }
            plusersCargoService.save(thisUser);
            request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, thisUser);
        }
        
        List<Long> vehicleInfoIds = new ArrayList<Long>();
        if (null != user && user.getUserCargoId() > 0) {
            List<UsersCargoFavorite> fav = favoriteService.findByUserCargoId(user.getUserCargoId());
            for (UsersCargoFavorite usersCargoFavorite : fav) {
                vehicleInfoIds.add(usersCargoFavorite.getVehicleInfoId());
            }
        }

        SearchParamVo paramVo = new SearchParamVo();
        if (carTypeId.equals("0"))
            carTypeId = "";
        if (!Utils.isEmpty(carTypeId)) {
            Integer[] typs = Utils.stringToIntegerArray(carTypeId);
            paramVo.setVehicleType(typs);
        }
        paramVo.setCount(20);
        paramVo.setSort(sortnum);
        paramVo.setOnline(new Boolean[] {true});

        if (null != user && !Utils.isEmpty(user.getMobile()))
            paramVo.setRequester(user.getMobile());
        else
            paramVo.setRequester("");
        paramVo.setStartRadius(Constant.SEARCH_DISTANCE);
        paramVo.setStartX(s_x);
        paramVo.setStartY(s_y);
        paramVo.setVisible(new Boolean[] { true });

        try {
            VehicleSearchReturnVo vo = searchEngineService.search(paramVo);
            if (null != vo && vo.getStatus().equalsIgnoreCase("success")) {
                List<Object> list = new ArrayList<Object>();
                VehicleInfo[] vInfos = vo.getVehicles();
                if (null != vInfos && vInfos.length > 0) {
                    for (VehicleInfo vehicleInfo : vInfos) {
                        Map<String, Object> veResult = new HashMap<String, Object>();
                        veResult.put("truck_id", vehicleInfo.getId().toString());
                        veResult.put("cartype_id", vehicleInfo.getVehicleType().toString());
                        veResult.put("image_url", Constant.IMG_URL_PRE + vehicleInfo.getImageUrl());
                        veResult.put("distance", vehicleInfo.getDistance().toString());
                        veResult.put("status", vehicleInfo.getState().toString());
                        veResult.put("star_level", Common.getCarCommentGrade(vehicleInfo.getStarLevel()) + "");
                        veResult.put("operunit", vehicleInfo.getOperator().toString());
                        veResult.put("x", vehicleInfo.getLocation()[0].toString());
                        veResult.put("y", vehicleInfo.getLocation()[1].toString());
                        if(vehicleInfoIds.contains(vehicleInfo.getId()))
                            veResult.put("collection", "1");
                        else
                            veResult.put("collection", "0");
                        String fromLine = null;
                        String toLine = null;
                        locationInfo fromInfo = vehicleInfo.getDomicileDetails();
                        locationInfo toInfo = vehicleInfo.getLocationDetails();
                        if (null != fromInfo && null != fromInfo.getAddress()) {
                            if (!StringUtils.isEmpty(fromInfo.getAddress().getStreet()))
                                fromLine = fromInfo.getAddress().getStreet();
                            else if (!StringUtils.isEmpty(fromInfo.getAddress().getDistrict())) {
                                fromLine = fromInfo.getAddress().getDistrict();
                            }
                        }
                        if (null != toInfo && null != toInfo.getAddress()) {
                            if (!StringUtils.isEmpty(toInfo.getAddress().getStreet()))
                                toLine = toInfo.getAddress().getStreet();
                            else if (!StringUtils.isEmpty(toInfo.getAddress().getDistrict())) {
                                toLine = toInfo.getAddress().getDistrict();
                            }
                        }
                        if (vehicleInfo.getState() == 0 && null != fromLine && null != toLine)
                            veResult.put("line", fromLine + "——" + toLine);
                        else
                            veResult.put("line", "");
                        veResult.put("plate_num", vehicleInfo.getVehicleNum());
                        veResult.put("mobile", vehicleInfo.getPhone());
                        veResult.put("resident", null != toLine ? toLine : "");
                        veResult.put("driver", vehicleInfo.getDriver());
                        String specialty = vehicleInfo.getFreightSpecialty();
                        if (!StringUtils.isEmpty(specialty)) {
                            String[] specialtys = specialty.split(",");
                            List<String> specialtysList = new ArrayList<String>();
                            for (String spec : specialtys) {
                                specialtysList.add(Common.getCarAdeptCargo(spec));
                            }
                            veResult.put("cargo_type_list", Utils.listToString(specialtysList));
                        }
                        veResult.put("starting_price", vehicleInfo.getStartingPrice() == null ? "" : vehicleInfo.getStartingPrice());
                        veResult.put("starting_mileage", vehicleInfo.getStartingMileage() == null ? "" : vehicleInfo.getStartingMileage());
                        veResult.put("mileage_price", vehicleInfo.getMileagePrice() == null ? "" : vehicleInfo.getMileagePrice());

                        // 2014/9/23 增加金牌车主、实名认证、是否提供敢用敢赔服务返回数据
                        if (null != vehicleInfo.getOwnerLevel()  && 1001L == vehicleInfo.getOwnerLevel() ) {
                                veResult.put("use_compensation", "1");
                                veResult.put("owner_level","1");
                        } else {
                            veResult.put("owner_level", "0");
                            veResult.put("use_compensation", "0");
                        }

                        veResult.put("identity_auth", null != vehicleInfo.getIdentityAuth() && vehicleInfo.getIdentityAuth() ? "1" : "0");
                        list.add(veResult);
                    }
                }
                result.put("errorcode", "0");
                result.put("info", "");
                result.put("total", vo.getTotal());
                result.put("data", list);
                LOG.debug("result" + JsonUtils.object2Json(result));
                return result;
            } else {
                LOG.info("no Data return from webService or Data format error.");
                result.put("errorcode", "3");
                result.put("info", "没有在您周围找到合适的车辆");
                result.put("data", new String[] {});
            }
        } catch (Exception e) {
            LOG.error("The resultList() method invocation exception.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕啦，请稍候再试");
            result.put("data", new String[] {});
        }
        return result;
    }

    /**
     * 车辆详情信息
     * 
     * @param carId
     *            车辆ID
     * @param orderId
     *            语音订单ID，可为空
     * @return
     */
    @ResponseBody
    @RequestMapping("/appresultdetail")
    public Map<String, Object> appResultDetail(@RequestParam(value = "car_id", defaultValue = "0")
    Long carId, @RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long orderId) {
        VeVehicleInfoDetail info = procedureService.getVehicleDetailByCarId(carId);
        VeVehicleInfo vehicle = veVehicleInfoService.findOne(carId);
        if (null != info && null != info.getCarTypeId() && info.getCarTypeId() > 0) {
            List<BkBooking> bookings = bkBookingService.getByVehicleInfoIdAndFixedParam(carId);
            int goodCommet = 0, badCommet = 0;
            List<Map<String, Object>> commentList = new ArrayList<Map<String, Object>>();

            for (BkBooking bkBooking : bookings) {
                Map<String, Object> comment = new HashMap<String, Object>();
                // comment.put("booking_no",
                // bkBooking.getBookingNo().toString());
                comment.put("name", Common.formatPhoneNumber(bkBooking.getBookingFromUsersId()));
                comment.put("comment_level", bkBooking.getCommentLevel().toString());
                // comment.put("comments", bkBooking.getCommentType().intValue()
                // == 2 ? Constant.IMG_URL_PRE + bkBooking.getComment() :
                // bkBooking.getComment());
                comment.put("comments", bkBooking.getComment());
                comment.put("createtime", Utils.formatDate(bkBooking.getCreateDt(), null));
                // comment.put("comment_type",
                // bkBooking.getCommentType().toString());
                if (commentList.size() < 3)
                    commentList.add(comment);
                if (bkBooking.getCommentLevel() == 1)
                    badCommet++;
                if (bkBooking.getCommentLevel() == 2)
                    goodCommet++;
            }

            Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
            int favCount = 0;
            int userFav = 0;
            if (null != obj) {
                PlUsersCargo user = (PlUsersCargo) obj;
                userFav = favoriteService.getCountByCargoIdAndVehicleInfoId(user.getUserCargoId(), carId);
            }
            favCount = favoriteService.getVehicleFavoriteCount(carId);
            Map<String, Object> veResult = new HashMap<String, Object>();
            veResult.put("vehicle_id", carId + "");
            // 2.4版本之前
            veResult.put("cartype_id", info.getCarTypeId().toString());
            veResult.put("tel", info.getTel());
            veResult.put("star_level", Common.getCarCommentGrade(vehicle.getVeLevel()) + "");
            veResult.put("name", info.getName());
            veResult.put("image_url", null != info.getImageUrl() ? Constant.IMG_URL_PRE + info.getImageUrl() : "");
            veResult.put("image_url_tail", null != info.getImageUrlTail() ? Constant.IMG_URL_PRE + info.getImageUrlTail() : "");
            veResult.put("image_url_side", null != info.getImageUrlSide() ? Constant.IMG_URL_PRE + info.getImageUrlSide() : "");
            veResult.put("image_url_user", null != info.getImageUrlUser() ? Constant.IMG_URL_PRE + info.getImageUrlUser() : "");
            if (info.getCarTypeId() == 5 || info.getCarTypeId() == 6 || info.getCarTypeId() == 7 || info.getHidePlates() == 1)
                veResult.put("car_brand", !Utils.isEmpty(info.getCarBrand()) ? Common.hidePlate(info.getCarBrand()) : "");
            else
                veResult.put("car_brand", !Utils.isEmpty(info.getCarBrand()) ? info.getCarBrand() : "");

            if (info.getCarTypeId() == 5 || info.getCarTypeId() == 6 || info.getCarTypeId() == 7 || info.getHidePlates() == 1)
                veResult.put("car_plates", !Utils.isEmpty(info.getCarPlates()) ? Common.hidePlate(info.getCarPlates()) : "");
            else
                veResult.put("car_plates", !Utils.isEmpty(info.getCarPlates()) ? info.getCarPlates() : "");

            veResult.put("operunit", null != info.getOperunit() ? info.getOperunit().toString() : "");
            veResult.put("longitude", null != info.getLongitude() ? info.getLongitude() + "" : "");
            veResult.put("latitude", null != info.getLatitude() ? info.getLatitude() + "" : "");
            if (!StringUtils.isEmpty(info.getCargoTypeList())) {
                String[] specialtys = info.getCargoTypeList().split(",");
                List<String> specialtysList = new ArrayList<String>();
                for (String spec : specialtys) {
                    specialtysList.add(Common.getCarAdeptCargo(spec));
                }
                veResult.put("cargo_type_list", Utils.listToString(specialtysList));
            }
            String remark = info.getRemark();
            if (!StringUtils.isEmpty(remark) && !remark.equalsIgnoreCase("null"))
                veResult.put("comment", remark);
            else
                veResult.put("comment", "");
            veResult.put("resident", info.getVeResidentDesc());
            veResult.put("order_id", orderId.toString());
            veResult.put("collection", userFav > 0 ? "1" : "0");
            veResult.put("good_comment", goodCommet + "");
            veResult.put("bad_comment", badCommet + "");
            veResult.put("comment_list", commentList);
            veResult.put("favorite_count", favCount + "");

            // 获取运费
            veResult.put("starting_price", vehicle.getStartingPrice() == null ? "" : vehicle.getStartingPrice() + "");
            veResult.put("starting_mileage", vehicle.getStartingMileage() == null ? "" : vehicle.getStartingMileage() + "");
            veResult.put("mileage_price", vehicle.getMileagePrice() == null ? "" : vehicle.getMileagePrice() + "");
            if(vehicle.getOwnerLevel() != null && vehicle.getOwnerLevel() == 1001L){
            	veResult.put("owner_level", "1");
            }else {
            	veResult.put("owner_level", "0");
            }
            
            veResult.put("identity_auth", vehicle.getIdentityAuth() ? "1" : "0");
            veResult.put("use_compensation", null != vehicle.getOwnerLevel() && vehicle.getOwnerLevel() == 1001L ? "1" : "0");
            double goodRate = (double) goodCommet / (double) (goodCommet + badCommet);
            NumberFormat format = NumberFormat.getPercentInstance();
            format.setMinimumFractionDigits(1);
            if (goodRate > 0)
                veResult.put("good_rate", format.format(goodRate));
            else
                veResult.put("good_rate", "0");
            result.put("errorcode", "0");
            result.put("info", "");
            result.put("data", veResult);
        } else {
            result.put("errorcode", "2");
            result.put("info", "获取货车详情失败");
            result.put("data", new String[] {});
        }
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 拨打记录列表
     * 
     * @param pageno
     *            页码
     * @param limit
     *            每页记录数
     * @return
     */
    @ResponseBody
    @RequestMapping("/callhistorylist")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> callHistoryList(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageNo, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int size) {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        Page<BkInquiryLog> page = bkInquiryLogService.findByIsDeleteAndUserCargoId(false, user.getUserCargoId(), pageNo, size);
        List<BkInquiryLog> logs = page.getContent();
        List<Object> list = new ArrayList<Object>();
        if (null != logs && logs.size() > 1) {
            for (BkInquiryLog bkInquiryLog : logs) {
                // TODO 严重性能问题，需要改表结构，适当作字段冗余
                Map<String, Object> inforesult = new HashMap<String, Object>();
                if (null != bkInquiryLog.getVeUserId()) {
                    PlUsers plUser = plUsersService.findOne(bkInquiryLog.getVeUserId());
                    List<VeVehicleInfo> infos = veVehicleInfoService.findVehicleByUserId(bkInquiryLog.getVeUserId());
                    if (null != infos && infos.size() > 0) {
                        inforesult.put("id", bkInquiryLog.getLogId().toString());
                        inforesult.put("driver_name", plUser.getUserName());
                        inforesult.put("vehicle_id", infos.get(0).getVehicleInfoId().toString());
                        if (infos.get(0).getModelId() == 5 || infos.get(0).getModelId() == 6 || infos.get(0).getModelId() == 7 || infos.get(0).getHidePlates() == 1)
                            inforesult.put("ve_plates", !Utils.isEmpty(infos.get(0).getVePlates()) ? Common.hidePlate(infos.get(0).getVePlates()) : "");
                        else
                            inforesult.put("ve_plates", !Utils.isEmpty(infos.get(0).getVePlates()) ? infos.get(0).getVePlates() : "");
                        inforesult.put("photo_thumb", Constant.IMG_URL_PRE + infos.get(0).getVeHeadPath() + "thumb_" + infos.get(0).getVeHeadFile());
                        inforesult.put("star_level", Common.getCarCommentGrade(infos.get(0).getVeLevel()) + "");
                        inforesult.put("call_time", bkInquiryLog.getInquiryDt().getTime() / 1000 + "");
                        list.add(inforesult);
                    }
                }

            }

            result.put("errorcode", "0");
            result.put("info", "");
            result.put("total_page", page.getTotalPages() + "");
            result.put("total_num", page.getTotalElements() + "");
            result.put("data", list);
        } else {
            if (page.getTotalPages() - 1 <= pageNo) {
                result.put("errorcode", "0");
                result.put("total_page", page.getTotalPages() + "");
                result.put("total_num", page.getTotalElements() + "");
                result.put("data", list);
                if (pageNo == 0)
                    result.put("info", "");
                else
                    result.put("info", "没有更多记录");
            }
        }

        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 删除使用记录车辆
     * 
     * @param id
     *            使用记录id
     * @return
     */
    @ResponseBody
    @RequestMapping("/removecallhistory")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> removeCallHistory(@RequestParam(value = "id", defaultValue = "")
    String logIds) {
        if (StringUtils.isEmpty(logIds)) {
            result.put("errorcode", "2");
            result.put("info", "删除失败");
            return result;
        }
        // TODO logId是一个字符串，用‘，’分隔。
        // TODO 客户端选择了全选再清空，无论有没有再选择，点删除会将所有记录删除（BUG）
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        List<BkInquiryLog> logs = bkInquiryLogService.findByUserCargoIdAndLogIdIn(user.getUserCargoId(), logIds.split(","));
        if (null != logs && logs.size() > 0) {
            for (BkInquiryLog log : logs) {
                log.setIsDelete(true);
                bkInquiryLogService.saveEntity(log);
            }
            result.put("errorcode", "0");
            result.put("info", "删除成功");
        } else {
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕啦，请稍候再试");
        }
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 意见反馈
     * 
     * @param content
     *            意见内容
     * @return
     */
    @ResponseBody
    @RequestMapping("/feedback")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> feedback(@RequestParam(value = "content", defaultValue = "")
    String content) {
        String ip = request.getRemoteAddr();
        Long fromType = 2L;
        if (StringUtils.isEmpty(content)) {
            result.put("errorcode", "2");
            result.put("info", "请填写反馈内容");
            return result;
        }
        // TODO 未使用 tb_feedback_add 过程
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        TbFeedback feedback = new TbFeedback();
        feedback.setCreateDt(new Date());
        feedback.setFeedbackContent(content);
        feedback.setFeedbackFrom(fromType);
        feedback.setIp(ip);
        feedback.setTel(user.getMobile());
        tbFeedBackService.save(feedback);
        result.put("errorcode", "0");
        result.put("info", "提交成功");
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 订单评论
     * 
     * @param bookingNo
     *            订单ID
     * @param comment
     *            评价内容
     * @param commentLevel
     *            评价等级
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/reporttextcomment")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> reportTextComment(@RequestParam(value = "booking_no", defaultValue = "0")
    Long bookingNo, @RequestParam(value = "comment", required = false, defaultValue = "")
    String comment, @RequestParam(value = "comment_level", required = false, defaultValue = "0")
    Long commentLevel) throws Exception {
        Long commentType = 1L;
        if (StringUtils.isEmpty(comment)) {
            result.put("errorcode", "2");
            result.put("info", "评价内容不能为空");
            return result;
        }
        if (bookingNo == 0 || (commentLevel != 1 && commentLevel != 2)) {
            result.put("errorcode", "3");
            result.put("info", "评价失败");
            return result;
        }
        // TODO 未使用 bk_booking_update_comments 过程
        try {
            BkBooking booking = bkBookingService.findOne(bookingNo);
            if (null != booking && booking.getBookingStatus() == 5) {
                booking.setBookingStatus(7L);
                booking.setCommentCreateDt(new Date());
                booking.setCommentType(commentType);
                booking.setComment(comment);
                booking.setCommentLevel(commentLevel);

                if (booking.getIsvalid()) {
                    VeVehicleInfo veInfo = veVehicleInfoService.findOne(booking.getVehicleInfoId());
                    if (commentLevel == 1)
                        veInfo.setVeLevel(veInfo.getVeLevel() - 2);
                    if (commentLevel == 2)
                        veInfo.setVeLevel(veInfo.getVeLevel() + 1);
                    bkBookingService.save(booking); // 更新订单信息
                    veVehicleInfoService.save(veInfo); // 更新车辆星级

                    // 更新搜索引擎车辆星级
                    searchEngineService.updateVehicleLevel(booking.getVehicleInfoId(), veInfo.getVeLevel());
                } else {
                    bkBookingService.save(booking); // 更新订单信息
                }

                // 通知车主，货主已评价
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "1");
                params.put("booking_id", bookingNo.toString());
                String vehicleMobile = booking.getBookingFromUsersId();
                StringBuffer sb = new StringBuffer();
                sb.append("电话尾号").append(vehicleMobile.substring(vehicleMobile.length() - 4));
                sb.append("的货主对您的服务进行了评价，点击查看详情。【闪发车】");
                params.put("msg", sb.toString());
                PlUsers plUser = plUsersService.findOne(booking.getBookingToUsersId());
                xmppService.push(plUser.getMobile(), JsonUtils.object2Json(params), "v_");
                result.put("errorcode", "0");
                result.put("info", "评价成功");
                LOG.info("result" + JsonUtils.object2Json(result));
                return result;
            } else {
                result.put("errorcode", "3");
                result.put("info", "评价失败");
                LOG.info("result" + JsonUtils.object2Json(result));
                return result;
            }
        } catch (Exception e) {
            LOG.error("The reporttextcomment() method invocation exception.", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * 上报货主与车主通话记录
     * 
     * @param veMobile
     *            车主手机号
     * @param voiceId
     *            语音ID
     * @param lng
     *            经度
     * @param lat
     *            纬度
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/telephonerecord")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> telephoneRecord(@RequestParam(value = "vehiclemobile", defaultValue = "")
    String veMobile, @RequestParam(value = "voiceid", required = false, defaultValue = "0")
    Long voiceId, @RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat) throws Exception {
        if (!Utils.checkMobile(veMobile)) {
            result.put("errorcode", "2");
            result.put("info", "司机手机号码不合法");
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        }
        List<BkOrderOffer> offers = bkOrderOfferService.findByOrderIdAndVeMobile(voiceId, veMobile);
        if (null != offers && offers.size() > 0) {
            BkOrderOffer offer = offers.get(0);
            offer.setIsTel(true);
            bkOrderOfferService.save(offer);// 更新车主报价表已拨打电话
        }
        List<PlUsers> users = plUsersService.findByMobileAndIsvalid(veMobile, "Y");
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        if (null != users && users.size() > 0 && null != obj) {
            PlUsers plUser = users.get(0);
            PlUsersCargo userCargo = (PlUsersCargo) obj;
            BkInquiryLog inquiryLog = new BkInquiryLog();
            inquiryLog.setUserCargoId(userCargo.getUserCargoId());
            inquiryLog.setUserCargoMobile(userCargo.getMobile());
            inquiryLog.setUserMobile(veMobile);
            inquiryLog.setVeUserId(plUser.getUserId());
            inquiryLog.setInquiryDt(new Date());
            inquiryLog.setIsDelete(false);
            inquiryLog.setLatitude(lat);
            inquiryLog.setLongitude(lng);
            inquiryLog.setVoiceOrderId(voiceId);
            inquiryLog.setStatus(0L);
            bkInquiryLogService.saveEntity(inquiryLog);
            result.put("errorcode", "0");
            result.put("info", "成功");
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } else {
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕啦，请稍候再试");
            return result;
        }
    }

    /**
     * 文本下单
     * 
     * @param lng
     *            当前经度
     * @param lat
     *            当前纬度
     * @param sendTime
     *            发货时间
     * @param fromLng
     *            出发地经度
     * @param fromLat
     *            出发地纬度
     * @param toLng
     *            目的地经度
     * @param toLat
     *            目的地纬度
     * @param fromAddress
     *            出发地地址
     * @param toAddress
     *            目的地地址
     * @param desc
     *            货物描述
     * @param goodsLength
     *            货物长度
     * @param goodsHeight
     *            货物高度
     * @param goodsWidth
     *            货物宽度
     * @param photo
     *            货物图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/textorder")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> textOrder(@RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat, @RequestParam(value = "send_time", required = false, defaultValue = "0")
    Long sendTime, @RequestParam(value = "from_longitude", required = false, defaultValue = "0")
    Double fromLng, @RequestParam(value = "from_latitude", required = false, defaultValue = "0")
    Double fromLat, @RequestParam(value = "to_longitude", required = false, defaultValue = "0")
    Double toLng, @RequestParam(value = "to_latitude", required = false, defaultValue = "0")
    Double toLat, @RequestParam(value = "from_address", required = false, defaultValue = "")
    String fromAddress, @RequestParam(value = "to_address", required = false, defaultValue = "")
    String toAddress, @RequestParam(value = "description", required = false, defaultValue = "")
    String desc, @RequestParam(value = "goods_length", required = false, defaultValue = "")
    String goodsLength, @RequestParam(value = "goods_height", required = false, defaultValue = "")
    String goodsHeight, @RequestParam(value = "goods_width", required = false, defaultValue = "")
    String goodsWidth, @RequestParam(value = "from_short_address", required = false, defaultValue = "")
    String fromAddressShort, @RequestParam(value = "to_short_address", required = false, defaultValue = "")
    String toAddressShort, @RequestParam(value = "from_to_distance", required = false, defaultValue = "")
    String fromToDistance, @RequestParam(value = "goods_photo", required = false)
    MultipartFile photo, @RequestParam(value = "need_model", required = false, defaultValue = "")
    String needModel, @RequestParam(value = "use_ticket", required = false, defaultValue = "false")
    Boolean userTicket, @RequestParam(value = "from_detail_address", required = false, defaultValue = "")
    String fromAddressDetail, @RequestParam(value = "to_detail_address", required = false, defaultValue = "")
    String toAddressDetail) throws Exception {

        // 毫秒
        if (sendTime > 0) {
            sendTime = sendTime * 1000;
        }
        Object obj = request.getSession().getAttribute(Constant.SESSION_PL_USER_CARGO);
        if (fromLng == 0 || fromLat == 0 || Utils.isEmpty(fromAddress)) {
            result.put("errorcode", "3");
            result.put("info", "出发地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if (toLng == 0 || toLat == 0 || Utils.isEmpty(toAddress)) {
            result.put("errorcode", "4");
            result.put("info", "目的地不能为空");
            result.put("data", new String[] {});
            return result;
        }

        if ((fromLng + "").equals(toLng + "") && (fromLat + "").equals(toLat + "")) {
            result.put("errorcode", "5");
            result.put("info", "出发地和目的地经纬度相同，叫车失败");
            result.put("data", new String[] {});
            return result;
        }
        /**
         * 华强北坐标点（114.091837,22.547244）
         * 当文本找车的发货地址为非深圳市（距离华强北50KM以上时）时无法发送，温馨提示“服务暂未拓展到该区域，敬请期待”
         */
        Double dis = Utils.GetDistance(fromLat, fromLng, 22.547244, 114.091837);
        if (dis != null && dis > 50000) {
            result.put("errorcode", "5");
            result.put("info", "服务暂未拓展到该区域，敬请期待");
            result.put("data", new String[] {});
            return result;
        }

        long now = new Date().getTime();
        // 2.4.0版本之前 发货时间传的是时间戳 2.4之后传的是偏移量 (做兼容)
        LOG.info("版本号：" + getVersionCode());
        if (getVersionCode() >= 240) {
            if (sendTime < 0) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能小于当前时间");
                return result;
            }
            if (sendTime > 7776000000L) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能超过3个月");
                return result;
            }
            // 当前时间戳加上偏移量
            sendTime = now + sendTime;
        } else {
            if (sendTime < now) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能小于当前时间");
                return result;
            }
            if (sendTime > now + 7776000000L) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能超过3个月");
                return result;
            }
        }
        
        // if (Utils.isEmpty(desc)) {
        // result.put("errorcode", "6");
        // result.put("info", "货物描述不能为空");
        // return result;
        // }
        if (Utils.isEmpty(needModel)) {
            needModel = "1,2,3,5,6,7";
        }
        try {
            PlUsersCargo userCargo = (PlUsersCargo) obj;
            // 如果帐号状态被改为不可用，返回错误信息，更新SESSION中USER对象。
            
            //TODO 用户需从缓存中获取  不可直接从数据库获取  2014/10/12
            PlUsersCargo user = plusersCargoService.findOne(userCargo.getUserCargoId());
            
            if (null != user) {
                if (!user.getIsvalid().equalsIgnoreCase("Y")) {
                    request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, user);
                    result.put("errorcode", "7");
                    result.put("info", "帐号不存在");
                    return result;
                }
            } else {
                result.put("errorcode", "7");
                result.put("info", "帐号不存在");
                return result;
            }

            // 如果带有优惠券，查询优惠券状态   
            String ticketNo = null;

            if (userTicket) {
                TbTicketInfo ticket = creditAndTicketManagementService.checkTicketAndUseTicket(userTicket, userCargo.getUserCargoId(), UserType.cargo, TicketType.voucher);

                if (ticket == null) {
                    result.put("errorcode", "100");
                    result.put("info", "无可用优惠券");
                    return result;
                }
                ticketNo = ticket.getTicketNo();
            }

            // TODO 转移service    时间统一
            BkVoiceOrder order = new BkVoiceOrder();
            order.setCargoMobile(userCargo.getMobile());
            order.setUserCargoId(userCargo.getUserCargoId());
            order.setCreateTime(new Date());
            order.setExpireTime(new Date(sendTime));
            order.setLatitude(lat);
            order.setLongitude(lng);
            order.setFromAddress(fromAddress);
            order.setFromLatitude(fromLat);
            order.setFromLongitude(fromLng);
            order.setToAddress(toAddress);
            order.setToLatitude(toLat);
            order.setToLongitude(toLng);
            order.setGoodsHeight(goodsHeight);
            order.setGoodsLength(goodsLength);
            order.setGoodsWidth(goodsWidth);
            order.setDescription(desc);
            order.setFileName("");
            order.setFilePath("");
            order.setFileSize(0L);
            order.setIsCancel(false);
            order.setReceivedVehicle(0L);
            order.setFromShortAddress(fromAddressShort);
            order.setFromDetailAddress(fromAddressDetail);
            order.setToShortAddress(toAddressShort);
            order.setToDetailAddress(toAddressDetail);
            order.setFromToDistance(fromToDistance);
            order.setOrderType(1L); // 0为语音订单，1为文本订单。boolean映射0＝false,1=true。
            order.setNeedModel(needModel);
            order.setIsBooking(false); // 是否生成订单
            order.setTicketNo(ticketNo); // 优惠券信息
            order.setCompleteOffer(false);
            bkVoiceOrderService.save(order);

            // 保存发货地址
            if (fromLat > 0 && fromLng > 0) {
            	//TODO 数据优先从缓存中加载
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(1, userCargo.getUserCargoId(), fromAddress, fromAddressShort, fromAddressDetail);
               //TODO 转移service
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(fromLat, fromLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(fromLat);
                        add.setLng(fromLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(fromAddressDetail) ? "" : fromAddressDetail);
                    newadd.setAddress(Utils.isEmpty(fromAddress) ? "" : fromAddress);
                    newadd.setAddressShort(Utils.isEmpty(fromAddressShort) ? "" : fromAddressShort);
                    newadd.setLat(fromLat);
                    newadd.setLng(fromLng);
                    newadd.setType(1);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }
            // 保存收货地址
            if (toLat > 0 && toLng > 0) {
            	//TODO 优先读缓存
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(2, userCargo.getUserCargoId(), toAddress, toAddressShort, toAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(toLat, toLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(toLat);
                        add.setLng(toLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(toAddressDetail) ? "" : toAddressDetail);
                    newadd.setAddress(Utils.isEmpty(toAddress) ? "" : toAddress);
                    newadd.setAddressShort(Utils.isEmpty(toAddressShort) ? "" : toAddressShort);
                    newadd.setLat(toLat);
                    newadd.setLng(toLng);
                    newadd.setType(2);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }

            // 优惠券置为使用中状态
            creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(ticketNo, userCargo.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_USING_1, false);

            if (null != photo) {
                // 图片文件保存
                String filePhysical = Utils.buildFilePath(Constant.PHOTO_PATH);
                String fileUri = Utils.buildFilePath(Constant.PHOTO_PATH_URI_PRE);
                String randomNum = (int) (Math.random() * 899 + 100) + "";
                String realName = photo.getOriginalFilename();
                String suffixName = ".";
                if (!Utils.isEmpty(realName) && realName.contains(".")) {
                    suffixName += realName.split("\\.")[1];
                }
                String fileName = "order_" + System.currentTimeMillis() + randomNum + suffixName;
                File saveDir = new File(filePhysical);
                File saveFile = new File(filePhysical + fileName);
                if (!saveDir.exists())
                    saveDir.mkdirs();
                photo.transferTo(saveFile);
                if (!saveFile.exists() || saveFile.length() < photo.getSize()) {
                    saveFile.delete();
                    result.put("errorcode", "2");
                    result.put("info", "图片上传失败");
                    return result;
                } else {
                    BkVoicePhoto bkPhoto = new BkVoicePhoto();
                    bkPhoto.setFilePath(fileUri);
                    bkPhoto.setFileName(fileName);
                    bkPhoto.setFileSize(saveFile.length());
                    bkPhoto.setVoiceId(order.getId());
                    bkVoicePhotoService.save(bkPhoto);
                }
            }
            
            //发push
            pushAfterOrderService.pushAfterOrder(order, user);
            
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("order_id", null != order.getId() ? order.getId() + "" : "");
            // 返回抢单时间限制 2014/9/24 600秒
            reMap.put("offer_time_limit", Constant.OFFER_TIME_LIMIT + "");
            result.put("errorcode", "0");
            result.put("info", "下单成功");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } catch (Exception e) {
            LOG.error("The textOrder() method invocation exception.", e);
            throw e;
        }
    }

    /**
     * 文本下单,不含文件上传
     * 
     * @param lng
     * @param lat
     * @param sendTime
     * @param fromLng
     * @param fromLat
     * @param toLng
     * @param toLat
     * @param fromAddress
     * @param toAddress
     * @param desc
     * @param goodsLength
     * @param goodsHeight
     * @param goodsWidth
     * @param fromAddressShort
     * @param toAddressShort
     * @param fromToDistance
     * @return
     * @throws Exception
     */
    @ResponseBody
    // @Transactional
    @RequestMapping("/nof/textorder")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> textOrderNoFile(@RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat, @RequestParam(value = "send_time", required = false, defaultValue = "0")
    Long sendTime, @RequestParam(value = "from_longitude", required = false, defaultValue = "0")
    Double fromLng, @RequestParam(value = "from_latitude", required = false, defaultValue = "0")
    Double fromLat, @RequestParam(value = "to_longitude", required = false, defaultValue = "0")
    Double toLng, @RequestParam(value = "to_latitude", required = false, defaultValue = "0")
    Double toLat, @RequestParam(value = "from_address", required = false, defaultValue = "")
    String fromAddress, @RequestParam(value = "to_address", required = false, defaultValue = "")
    String toAddress, @RequestParam(value = "description", required = false, defaultValue = "")
    String desc, @RequestParam(value = "goods_length", required = false, defaultValue = "")
    String goodsLength, @RequestParam(value = "goods_height", required = false, defaultValue = "")
    String goodsHeight, @RequestParam(value = "goods_width", required = false, defaultValue = "")
    String goodsWidth, @RequestParam(value = "from_short_address", required = false, defaultValue = "")
    String fromAddressShort, @RequestParam(value = "to_short_address", required = false, defaultValue = "")
    String toAddressShort, @RequestParam(value = "from_to_distance", required = false, defaultValue = "")
    String fromToDistance, @RequestParam(value = "need_model", required = false, defaultValue = "")
    String needModel, @RequestParam(value = "use_ticket", required = false, defaultValue = "false")
    Boolean userTicket, @RequestParam(value = "from_detail_address", required = false, defaultValue = "")
    String fromAddressDetail, @RequestParam(value = "to_detail_address", required = false, defaultValue = "")
    String toAddressDetail) throws Exception {
        if (sendTime > 0) {
            sendTime = sendTime * 1000;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        if (fromLng == 0 || fromLat == 0 || Utils.isEmpty(fromAddress)) {
            result.put("errorcode", "3");
            result.put("info", "出发地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if (toLng == 0 || toLat == 0 || Utils.isEmpty(toAddress)) {
            result.put("errorcode", "4");
            result.put("info", "目的地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if ((fromLng + "").equals(toLng + "") && (fromLat + "").equals(toLat + "")) {
            result.put("errorcode", "5");
            result.put("info", "出发地和目的地经纬度相同，叫车失败");
            result.put("data", new String[] {});
            return result;
        }

        /**
         * 华强北坐标点（114.091837,22.547244）
         * 当文本找车的发货地址为非深圳市（距离华强北30KM以上时）时无法发送，温馨提示“服务暂未拓展到该区域，敬请期待”
         */
        Double dis = Utils.GetDistance(fromLat, fromLng, 22.547244, 114.091837);
        if (dis != null && dis > 50000) {
            result.put("errorcode", "5");
            result.put("info", "服务暂未拓展到该区域，敬请期待");
            result.put("data", new String[] {});
            return result;
        }

        long now = new Date().getTime();
        // 2.4.0版本之前 发货时间传的是时间戳 2.4之后传的是偏移量 (做兼容)
        LOG.info("版本号：" + getVersionCode());
        if (getVersionCode() >= 240) {
            if (sendTime < 0) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能小于当前时间");
                return result;
            }
            if (sendTime > 7776000000L) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能超过3个月");
                return result;
            }
            // 当前时间戳加上偏移量
            sendTime = now + sendTime;
        } else {
            if (sendTime < now) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能小于当前时间");
                return result;
            }
            if (sendTime > now + 7776000000L) {
                result.put("errorcode", "6");
                result.put("info", "发货时间不能超过3个月");
                return result;
            }
        }

        // if (Utils.isEmpty(desc)) {
        // result.put("errorcode", "6");
        // result.put("info", "货物描述不能为空");
        // return result;
        // }
        if (Utils.isEmpty(needModel)) {
            needModel = "1,2,3,5,6,7";
        }
        /*
         * if (!Utils.isNumber(goodsHeight)) { result.put("errorcode", "8");
         * result.put("info", "货物高度错误"); result.put("data", new String[] {});
         * return result; } if (!Utils.isNumber(goodsLength)) {
         * result.put("errorcode", "8"); result.put("info", "货物长度错误");
         * result.put("data", new String[] {}); return result; } if
         * (!Utils.isNumber(goodsWidth)) { result.put("errorcode", "8");
         * result.put("info", "货物宽度错误"); result.put("data", new String[] {});
         * return result; }
         */
        try {
            PlUsersCargo userCargo = (PlUsersCargo) obj;
            // 如果帐号状态被改为不可用，返回错误信息，更新SESSION中USER对象。
            PlUsersCargo user = plusersCargoService.findOne(userCargo.getUserCargoId());
            if (null != user) {
                if (!user.getIsvalid().equalsIgnoreCase("Y")) {
                    request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, user);
                    result.put("errorcode", "7");
                    result.put("info", "帐号不存在");
                    return result;
                }
            } else {
                result.put("errorcode", "7");
                result.put("info", "帐号不存在");
                return result;
            }

            // 如果带有优惠券，查询优惠券状态
            String ticketNo = null;
            if (userTicket) {
                TbTicketInfo ticket = creditAndTicketManagementService.checkTicketAndUseTicket(userTicket, userCargo.getUserCargoId(), UserType.cargo, TicketType.voucher);

                if (ticket == null) {
                    result.put("errorcode", "100");
                    result.put("info", "无可用优惠券");
                    return result;
                }
                ticketNo = ticket.getTicketNo();
            }
            BkVoiceOrder order = new BkVoiceOrder();
            order.setCargoMobile(userCargo.getMobile());
            order.setUserCargoId(userCargo.getUserCargoId());
            order.setCreateTime(new Date());
            order.setExpireTime(new Date(sendTime));
            order.setLatitude(lat);
            order.setLongitude(lng);
            order.setFromAddress(fromAddress);
            order.setFromLatitude(fromLat);
            order.setFromLongitude(fromLng);
            order.setToAddress(toAddress);
            order.setToLatitude(toLat);
            order.setToLongitude(toLng);
            order.setGoodsHeight(goodsHeight);
            order.setGoodsLength(goodsLength);
            order.setGoodsWidth(goodsWidth);
            order.setDescription(desc);
            order.setFileName("");
            order.setFilePath("");
            order.setFileSize(0L);
            order.setIsCancel(false);
            order.setReceivedVehicle(0L);
            order.setFromShortAddress(fromAddressShort);
            order.setFromDetailAddress(fromAddressDetail);
            order.setToShortAddress(toAddressShort);
            order.setToDetailAddress(toAddressDetail);
            order.setFromToDistance(fromToDistance);
            order.setNeedModel(needModel);
            order.setOrderType(1L); // 0为语音订单，1为文本订单。boolean映射0＝false,1=true。
            order.setIsBooking(false);
            order.setTicketNo(ticketNo); // 优惠券信息
            order.setCompleteOffer(false);

            bkVoiceOrderService.save(order);

            // 保存发货地址
            if (fromLat > 0 && fromLng > 0) {
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(1, userCargo.getUserCargoId(), fromAddress, fromAddressShort, fromAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(fromLat, fromLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(fromLat);
                        add.setLng(fromLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(fromAddressDetail) ? "" : fromAddressDetail);
                    newadd.setAddress(Utils.isEmpty(fromAddress) ? "" : fromAddress);
                    newadd.setAddressShort(Utils.isEmpty(fromAddressShort) ? "" : fromAddressShort);
                    newadd.setLat(fromLat);
                    newadd.setLng(fromLng);
                    newadd.setType(1);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }
            // 保存收货地址
            if (toLat > 0 && toLng > 0) {
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(2, userCargo.getUserCargoId(), toAddress, toAddressShort, toAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(toLat, toLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(toLat);
                        add.setLng(toLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(toAddressDetail) ? "" : toAddressDetail);
                    newadd.setAddress(Utils.isEmpty(toAddress) ? "" : toAddress);
                    newadd.setAddressShort(Utils.isEmpty(toAddressShort) ? "" : toAddressShort);
                    newadd.setLat(toLat);
                    newadd.setLng(toLng);
                    newadd.setType(2);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }

            // 优惠券置为使用中状态
            creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(ticketNo, userCargo.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_USING_1, false);

            //发push
            pushAfterOrderService.pushAfterOrder(order, user);
            
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("order_id", null != order.getId() ? order.getId() + "" : "");
            // 返回抢单时间限制 2014/9/24 600秒
            reMap.put("offer_time_limit", Constant.OFFER_TIME_LIMIT + "");

            result.put("errorcode", "0");
            result.put("info", "下单成功");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;

        } catch (Exception e) {
            LOG.error("The textOrderNoFile() method invocation exception.", e);
            throw e;
        }
    }

    /**
     * 重新生成文本订单
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/retextorder")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> reTextOrder(@RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long orderId) {
        BkVoiceOrder order = bkVoiceOrderService.findOne(orderId);
        if (null != order) {
            Long sendTime = order.getExpireTime().getTime();
            Long createTime = order.getCreateTime().getTime();
            boolean isReal = sendTime - createTime > Constant.REAL_RESERVE_DIFF_TIME ? false : true;
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("lng", null != order.getLongitude() ? order.getLongitude() + "" : "");
            reMap.put("lat", null != order.getLatitude() ? order.getLatitude() + "" : "");
            reMap.put("fromLng", null != order.getFromLongitude() ? order.getFromLongitude() + "" : "");
            reMap.put("fromLat", null != order.getFromLatitude() ? order.getFromLatitude() + "" : "");
            reMap.put("toLng", null != order.getToLongitude() ? order.getToLongitude() + "" : "");
            reMap.put("toLat", null != order.getToLatitude() ? order.getToLatitude() + "" : "");
            reMap.put("fromAddress", Utils.isEmpty(order.getFromAddress()) ? "" : order.getFromAddress());
            reMap.put("toAddress", Utils.isEmpty(order.getToAddress()) ? "" : order.getToAddress());
            reMap.put("desc", Utils.isEmpty(order.getDescription()) ? "" : order.getDescription());
            reMap.put("fromAddressShort", Utils.isEmpty(order.getFromShortAddress()) ? "" : order.getFromShortAddress());
            reMap.put("toAddressShort", Utils.isEmpty(order.getToShortAddress()) ? "" : order.getToShortAddress());
            reMap.put("fromAddressDetail", Utils.isEmpty(order.getFromDetailAddress()) ? "" : order.getFromDetailAddress());
            reMap.put("toAddressDetail", Utils.isEmpty(order.getToDetailAddress()) ? "" : order.getToDetailAddress());
            reMap.put("needModel", Utils.isEmpty(order.getNeedModel()) ? "" : order.getNeedModel());
            reMap.put("fromToDistance", Utils.isEmpty(order.getFromToDistance()) ? "" : order.getFromToDistance());
            if (!isReal) {
                reMap.put("sendTime", order.getExpireTime().getTime() / 1000 + "");
            }
            result.put("errorcode", "0");
            result.put("info", "");
            result.put("data", reMap);
            return result;

        } else {
            result.put("errorcode", "2");
            result.put("info", "重发订单失败");
        }
        return result;
    }

    /**
     * 文本订单详情
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/textdetail")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> textDetail(@RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long orderId) {
        BkVoiceOrder order = bkVoiceOrderService.findOne(orderId);
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (null != order && (order.getOrderType() == 1L || order.getOrderType() == 2L)) {
            List<BkVoicePhoto> photos = bkVoicePhotoService.findByVoiceId(orderId);
            reMap.put("from_address", order.getFromAddress());
            reMap.put("to_address", order.getToAddress());
            reMap.put("send_time", Utils.formatDate(order.getExpireTime(), null));
            reMap.put("goods_length", order.getGoodsLength());
            reMap.put("goods_width", order.getGoodsWidth());
            reMap.put("goods_height", order.getGoodsHeight());
            reMap.put("description", order.getDescription());
            reMap.put("from_short_address", order.getFromShortAddress() == null ? "" : order.getFromShortAddress());
            reMap.put("to_short_address", order.getToShortAddress() == null ? "" : order.getToShortAddress());
            reMap.put("from_detail_address", order.getFromDetailAddress() == null ? "" : order.getFromDetailAddress());
            reMap.put("to_detail_address", order.getToDetailAddress() == null ? "" : order.getToDetailAddress());
            if (!Utils.isEmpty(order.getFromToDistance())) {
                if (Utils.isNumberOrFloat(order.getFromToDistance())) {
                    Double dis = Double.valueOf(order.getFromToDistance()) / 1000;
                    reMap.put("from_to_distance", String.format("%.2f", dis) + "公里");
                } else
                    reMap.put("from_to_distance", order.getFromToDistance());
            } else
                reMap.put("from_to_distance", "未知");
            if (photos.size() > 0) {
                BkVoicePhoto photo = photos.get(0);
                reMap.put("goods_photo", Constant.IMG_URL_PRE + photo.getFilePath() + "/" + photo.getFileName());
            } else
                reMap.put("goods_photo", "");

            // 车型选择
            String needModel = order.getNeedModel();
            String modelStr = "";
            
            if(getVersionCode() < 240){
            	 if (!Utils.isEmpty(needModel)) {
                     String[] models = needModel.split(",");
                     for (int i = 0; i < models.length; i++) {
                         modelStr += Common.getModelDesc(models[i])+",";
                     }
                     modelStr.substring(0, modelStr.length()-1);
                 } 
            }else {
            	 //2.4版本以后 车型改了
            	 if (!Utils.isEmpty(needModel)) {
	                List<Map<String,String>> carModelList = Common.getCarType();
	                for (Map<String, String> map : carModelList) {
	    				if(needModel.equals(map.get("value"))){
	    					modelStr = map.get("name");
	    				}
	                }
            	 }
            }
            reMap.put("need_model", modelStr);
           
            
            result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } else {
            result.put("errorcode", "2");
            result.put("info", "文本叫车信息不存在");
            result.put("data", new String[] {});
            return result;
        }
    }

    /**
     * 语音记录列表
     * 
     * @param pageNo
     *            页码
     * @param size
     *            每页记录数
     * @return
     */
    @ResponseBody
    @RequestMapping("/offerlist")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> offerList(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageNo, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int size) {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        Page<BkVoiceOrder> orders = bkVoiceOrderService.findByIsCancelAndUserCargoIdAndOrderType(false, user.getUserCargoId(), 1L, pageNo, size);
        List<Object> data = new ArrayList<Object>();
        if (orders.hasContent()) {
            for (BkVoiceOrder order : orders.getContent()) {
                Map<String, Object> reMap = new HashMap<String, Object>();
                reMap.put("order_id", null != order.getId() ? order.getId() + "" : "");
                reMap.put("file_name", Constant.VOICE_FILE_URL + order.getFilePath() + "/" + order.getFileName());
                reMap.put("create_time", order.getCreateTime().getTime() / 1000 + "");
                reMap.put("expire_time", order.getExpireTime().getTime() / 1000 + "");
                reMap.put("now_time", new Date().getTime() / 1000 + "");
                reMap.put("order_type", null != order.getOrderType() ? order.getOrderType() + "" : "");
                int offerNum = bkOrderOfferService.getCountWithOrderId(order.getId());
                reMap.put("offer_num", offerNum + "");
                // 返回发货地址 发货时间
                String fromAddress = order.getFromAddress() == null ? "" : order.getFromAddress();
                String fromShortAddress = Utils.isEmpty(order.getFromShortAddress()) ? "" : order.getFromShortAddress();
                String toAddress = order.getToAddress() == null ? "" : order.getToAddress();
                String toShortAddress = Utils.isEmpty(order.getToShortAddress()) ? "" : order.getToShortAddress();
                String deliveryTime = TimeUtil.getDateString(order.getExpireTime(), "yyyy-MM-dd HH:mm");
                reMap.put("from_address", (Utils.isBusLines(fromAddress) ? "" : fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + fromShortAddress);
                reMap.put("to_address", (Utils.isBusLines(toAddress) ? "" : toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "")) + toShortAddress);
                reMap.put("delivery_time", deliveryTime);
                // 返回 订单是否过期（有效）
                String orderValid = "1";
                if (new Date().getTime() > order.getExpireTime().getTime()) {
                    // 当前时间大于发货时间 订单过期
                    orderValid = "0";
                }
                reMap.put("order_valid", orderValid);

                data.add(reMap);
            }
            result.put("errorcode", "0");
            result.put("info", "");
            result.put("total_page", orders.getTotalPages() + "");
            result.put("data", data);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } else {
            if (orders.getTotalPages() - 1 <= pageNo) {
                result.put("errorcode", "0");
                result.put("total_page", orders.getTotalPages() + "");
                result.put("data", data);
                if (pageNo == 0) {
                    result.put("info", "");
                } else {
                    result.put("info", "没有更多记录");
                }
            }

        }
        return result;
    }

    /**
     * 语音订单详情
     * 
     * @param orderId
     *            语音订单ID
     * @param sort
     *            排序字段，0＝按价格，1＝按创建时间
     * @return
     */
    @ResponseBody
    @RequestMapping("/voiceorderdetail")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> voiceOrderDetail(@RequestParam(value = "order_id", required = false, defaultValue = "1")
    Long orderId, @RequestParam(value = "sort", required = false, defaultValue = "0")
    Long sort) {
        String sortField = sort == 1 ? "createTime" : "price";
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        BkVoiceOrder order = bkVoiceOrderService.findByIdAndUserCargoId(orderId, user.getUserCargoId());
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (null != order) {
            reMap.put("order_id", orderId + "");
            reMap.put("file_name", Constant.VOICE_FILE_URL + order.getFilePath() + "/" + order.getFileName());
            reMap.put("create_time", order.getCreateTime().getTime() / 1000 + "");
            reMap.put("expire_time", order.getExpireTime().getTime() / 1000 + "");
            reMap.put("now_time", new Date().getTime() / 1000 + "");
            reMap.put("received_num", null != order.getReceivedVehicle() ? order.getReceivedVehicle() + "" : "");
            reMap.put("longitude", null != order.getLongitude() ? order.getLongitude() + "" : "");
            reMap.put("latitude", null != order.getLatitude() ? order.getLatitude() + "" : "");
            int offerNum = bkOrderOfferService.getCountWithOrderId(order.getId());
            reMap.put("offer_num", offerNum + "");

            BkBooking booking = bkBookingService.findByVoiceId(orderId);

            reMap.put("is_create_booking", null != booking ? "1" : "0");
            List<BkOrderOffer> offers = bkOrderOfferService.getByOrderId(orderId, sortField);
            reMap.put("offer_num", offers.size() + "");

            

            // 返回订单是否失效（如果当前时间已经超过运货时间 或者 已经生成订单 ） 0失效 1有效
            String orderValid = "1";
            if (null != booking || new Date().getTime() > order.getExpireTime().getTime()) {
                orderValid = "0";
            } else {
                orderValid = "1";
            }
            reMap.put("order_valid", orderValid);

            // 查询是否使用了优惠券
            reMap.put("use_ticket", Utils.isEmpty(order.getTicketNo()) ? false : true);
            List<Object> offerList = new ArrayList<Object>();

            boolean flag =false;  // 是否有车主被取消过
            for (BkOrderOffer boo : offers) {
                Map<String, Object> offer = new HashMap<String, Object>();
                VeVehicleInfo veInfo = veVehicleInfoService.findByVehicleInfoId(boo.getVehicleInfoId());
                PlUsers plusers = plUsersService.findOne(veInfo.getUserId());
                offer.put("vehicle_info_id", null != boo.getVehicleInfoId() ? boo.getVehicleInfoId() + "" : "");
                offer.put("cartype_id", null != veInfo.getModelId() ? veInfo.getModelId() + "" : "");
                offer.put("star_level", Common.getCarCommentGrade(veInfo.getVeLevel()));
                offer.put("longitude", null != boo.getLongitude() ? boo.getLongitude() + "" : "");
                offer.put("latitude", null != boo.getLatitude() ? boo.getLatitude() + "" : "");
                offer.put("driver", plusers.getUserName());
                // TODO 图片目录需要按规则统一
                offer.put("photo_thumb", Constant.IMG_URL_PRE + veInfo.getVeHeadPath() + "thumb_" + veInfo.getVeHeadFile());
                offer.put("mobile", boo.getVeMobile());
                offer.put("price", null != boo.getPrice() ? boo.getPrice().intValue() + "" : "");
                offer.put("is_tel", boo.getIsTel() ? "1" : "0");
                offer.put("is_succ", (null != booking && booking.getVehicleInfoId().equals(boo.getVehicleInfoId())) ? "1" : "0");
                if (!StringUtils.isEmpty(veInfo.getCargoTypeList())) {
                    String[] specialtys = veInfo.getCargoTypeList().split(",");
                    List<String> specialtysList = new ArrayList<String>();
                    for (String spec : specialtys) {
                        specialtysList.add(Common.getCarAdeptCargo(spec));
                    }
                    offer.put("cargo_type_list", Utils.listToString(specialtysList));
                }
                // 是否是收藏车主
                Integer userFav = favoriteService.getCountByCargoIdAndVehicleInfoId(user.getUserCargoId(), boo.getVehicleInfoId());
                offer.put("collection", (userFav == null || userFav > 0) ? "1" : "0");

                // 车主等级 0：非金牌车主 1：金牌车主 2：
                if(veInfo.getOwnerLevel() != null && veInfo.getOwnerLevel() == 1001L){
                	offer.put("owner_level", "1");
                }else {
                	offer.put("owner_level", "0");
                }
                
                
                
                // 是否实名认证 0:未实名 1：已实名
                offer.put("identity_auth", veInfo.getIdentityAuth()?"1":"0");

                // 如果是金牌车主 返回敢用敢赔
                if (veInfo.getOwnerLevel() != null && veInfo.getOwnerLevel() == 1001L) {
                    offer.put("use_compensation", "1");
                } else {
                    offer.put("use_compensation", "0");
                }
                // 车主是否取消或者被取消过 0：未取消过 1：车主取消  2 货主取消
                offer.put("is_cancel", "0");   
                if(boo.getStatus() != null ){
                	if(boo.getStatus() == 2L){  
                		flag = true;    //有选过人  抢单时间结束
                		 offer.put("is_cancel", "1"); //货主取消
                	}
                	if(boo.getStatus() == 3L){
                		flag = true;
                		offer.put("is_cancel", "2");  //车主取消
                	}
                	
                	
                }
                offerList.add(offer);
            }
            // 返回 离抢单结束还有多少秒 600秒-（当前时间减去创建时间）
            Long timeLeft = 600 - (new Date().getTime() / 1000 - order.getCreateTime().getTime() / 1000);
            //如果 已经生成订单 或者 已经选过人  返回时间为0
            if(flag || null != booking){
            	reMap.put("time_left",  "0");
            }else {
            	reMap.put("time_left",  timeLeft+"");
            }
            
            reMap.put("offerdata", offerList);
            result.put("errorcode", "0");
            result.put("info", "");
            result.put("data", reMap);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;

        } else {
            result.put("errorcode", "2");
            result.put("info", "获取语音详情失败");
            return result;
        }
    }

    /**
     * 根据语音ID获取最新报价车主信息
     * 
     * @param orderId
     *            语音订单ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/lastoffervehicle")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> lastOfferVehicle(@RequestParam(value = "order_id", required = false, defaultValue = "1")
    Long orderId) {

        PlUsersCargo cargo = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        List<BkOrderOffer> offers = bkOrderOfferService.getByOrderId(orderId, "createTime");
        if (null == offers || offers.size() <= 0) {
            result.put("errorcode", "2");
            result.put("info", "语音信息不存在");
            result.put("data", new String[] {});
            return result;
        }

        VeVehicleInfo info = veVehicleInfoService.findByVehicleInfoId(offers.get(0).getVehicleInfoId());
        PlUsers pluser = plUsersService.findOne(info.getUserId());
        Map<String, Object> reMap = new HashMap<String, Object>();
        reMap.put("vehicle_info_id", null != info.getVehicleInfoId() ? info.getVehicleInfoId() + "" : "");
        reMap.put("cartype_id", null != info.getModelId() ? info.getModelId() + "" : "");
        reMap.put("star_level", Common.getCarCommentGrade(info.getVeLevel()));
        reMap.put("longitude", null != info.getVeLastLongitude() ? info.getVeLastLongitude() + "" : "");
        reMap.put("latitude", null != info.getVeLastLatitude() ? info.getVeLastLatitude() + "" : "");
        reMap.put("driver", pluser.getUserName());
        reMap.put("photo_thumb", Constant.IMG_URL_PRE + info.getVeHeadPath() + "thumb_" + info.getVeHeadFile());
        reMap.put("mobile", offers.get(0).getVeMobile());
        reMap.put("price", null != offers.get(0).getPrice() ? offers.get(0).getPrice().intValue() + "" : "");
        if (!StringUtils.isEmpty(info.getCargoTypeList())) {
            String[] specialtys = info.getCargoTypeList().split(",");
            List<String> specialtysList = new ArrayList<String>();
            for (String spec : specialtys) {
                specialtysList.add(Common.getCarAdeptCargo(spec));
            }
            reMap.put("cargo_type_list", Utils.listToString(specialtysList));
        }

        // 是否是收藏车主
        Integer userFav = favoriteService.getCountByCargoIdAndVehicleInfoId(cargo.getUserCargoId(), info.getVehicleInfoId());
        reMap.put("collection", (userFav == null || userFav > 0) ? "1" : "0");
        
        // 车主等级 0：非金牌车主 1：金牌车主 2：
        if(info.getOwnerLevel() != null && info.getOwnerLevel() == 1001L){
        	reMap.put("owner_level", "1");
        }else {
        	reMap.put("owner_level", "0");
        }
        
        // 是否实名认证 0:未实名 1：已实名
        reMap.put("identity_auth", info.getIdentityAuth()?"1":"0");

        // 如果是金牌车主 返回敢用敢赔
        if (info.getOwnerLevel() != null && info.getOwnerLevel() == 1001L) {
        	reMap.put("use_compensation", "1");
        } else {
        	reMap.put("use_compensation", "0");
        }
        
        
        result.put("errorcode", "0");
        result.put("info", "");
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 订单列表
     * 
     * @param pageNo
     * @param size
     * @return
     */
    @ResponseBody
    @RequestMapping("/bookinglist")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> bookingList(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageNo, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int size, @RequestParam(value = "is_commplete", required = false)
    Long isCommplete) {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        Page<BkBooking> page = bkBookingService.findByUserCargoId(user.getUserCargoId(), pageNo, size,isCommplete);
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        if (page.hasContent()) {
            for (BkBooking booking : page.getContent()) {
                VeVehicleInfo info = veVehicleInfoService.findOne(booking.getVehicleInfoId());
                PlUsers pluser = plUsersService.findOne(booking.getBookingToUsersId());

                Map<String, Object> reMap = new HashMap<String, Object>();
                reMap.put("booking_id", null != booking.getBookingNo() ? booking.getBookingNo() + "" : "");
                reMap.put("order_number", booking.getOrderNumber());
                if (null != info) {
                    if (info.getModelId() == 5 || info.getModelId() == 6 || info.getModelId() == 7 || info.getHidePlates() == 1)
                        reMap.put("ve_plates", !Utils.isEmpty(info.getVePlates()) ? Common.hidePlate(info.getVePlates()) : "");
                    else
                        reMap.put("ve_plates", !Utils.isEmpty(info.getVePlates()) ? info.getVePlates() : "");
                    reMap.put("photo_thumb", Constant.IMG_URL_PRE + info.getVeHeadPath() + "thumb_" + info.getVeHeadFile());
                } else {
                    reMap.put("ve_plates", "");
                    reMap.put("photo_thumb", "");
                }
                reMap.put("driver", null != pluser && !Utils.isEmpty(pluser.getUserName()) ? pluser.getUserName() : "");
                reMap.put("mobile", null != pluser && !Utils.isEmpty(pluser.getMobile()) ? pluser.getMobile() : "");
                reMap.put("create_time", Utils.formatDate(booking.getCreateDt(), null));
                reMap.put("is_read", booking.getIsRead() ? "1" : "0");

                // 如果是 6 默认置为7,跟客户端保持一致
                String status = booking.getBookingStatus() == 6 ? "7" : booking.getBookingStatus() + "";
                // 取消类型的订单 如果是货主取消 status返回-1
                if (booking.getBookingStatus() == 0L && null != booking.getCancelType() && 2L == booking.getCancelType()) {
                    status = "-1"; // -1 货主取消 0 车主取消
                }
                reMap.put("status", status);

                // 订单类型的判断
                if (null != booking.getVoiceId() && booking.getVoiceId() > 0) {
                    BkOrderOffer offer = bkOrderOfferService.getByVehicleInfoIdAndOrderId(booking.getVehicleInfoId(), booking.getVoiceId());
                    BkVoiceOrder order = bkVoiceOrderService.findOne(booking.getVoiceId());
                    String fromAddress = order.getFromAddress() == null ? "" : order.getFromAddress();
                    String fromShortAddress = Utils.isEmpty(order.getFromShortAddress()) ? "" : order.getFromShortAddress();
                    String toAddress = order.getToAddress() == null ? "" : order.getToAddress();
                    String toShortAddress = Utils.isEmpty(order.getToShortAddress()) ? "" : order.getToShortAddress();
                    String deliveryTime = TimeUtil.getDateString(order.getExpireTime(), "yyyy-MM-dd HH:mm");

                    // 在非普通下有两种状态 一个是语音，一个是文本下单
                    if (order.getOrderType() == 1L) {
                        reMap.put("booking_type", "3");// 文本下单
                        reMap.put("price", null != offer && null != offer.getPrice() ? offer.getPrice().intValue() + "" : "");
                        // 返回发货地址 发货时间
                        reMap.put("from_address", fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + fromShortAddress);
                        reMap.put("to_address", toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + toShortAddress);
                        reMap.put("delivery_time", deliveryTime);
                    } else if (order.getOrderType() == 0L) {
                        reMap.put("booking_type", "2");// 语音下单
                        reMap.put("price", null != offer && null != offer.getPrice() ? offer.getPrice().intValue() + "" : "");
                        reMap.put("voice_file", Utils.isEmpty(order.getFilePath()) ? "" : Constant.AUDIO_PATH_URI_PRE + order.getFilePath() + "/" + order.getFileName());
                    } else {
                        reMap.put("booking_type", "4");// 地图下单
                        reMap.put("price", null != order && null != order.getReferencePrice() ? order.getReferencePrice().intValue() + "" : "");
                        // 返回发货地址 发货时间
                        reMap.put("from_address", fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + fromShortAddress);
                        reMap.put("to_address", toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + toShortAddress);
                        reMap.put("delivery_time", deliveryTime);
                    }

                } else
                    reMap.put("booking_type", "1"); // 普通下单
                reList.add(reMap);
            }
        } else {
            if (page.getTotalPages() - 1 <= pageNo) {
                result.put("errorcode", "0");
                result.put("total_page", page.getTotalPages() + "");
                result.put("data", reList);
                if (pageNo == 0) {
                    result.put("info", "");
                } else {
                    result.put("info", "没有更多记录");
                }
                return result;
            }
        }
        result.put("errorcode", "0");
        result.put("total_page", page.getTotalPages() + "");
        result.put("info", "");
        result.put("data", reList);
        LOG.debug("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 订单详情
     * 
     * @param bookingNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/bookingdetail")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> bookingDetail(@RequestParam(value = "booking_id", required = false, defaultValue = "0")
    Long bookingNo) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        BkBooking booking = bkBookingService.findOne(bookingNo);
        if (null == booking) {
            result.put("errorcode", "2");
            result.put("info", "订单不存在");
            result.put("data", new String[] {});
            return result;
        }
        booking.setIsRead(true);
        bkBookingService.save(booking);
        VeVehicleInfo info = veVehicleInfoService.findOne(booking.getVehicleInfoId());
        PlUsers pluser = plUsersService.findOne(booking.getBookingToUsersId());
        BkOrderOffer offer = bkOrderOfferService.getByVehicleInfoIdAndOrderId(booking.getVehicleInfoId(), booking.getVoiceId());
        BkVoiceOrder order = null;
        if (null != booking.getVoiceId())
            order = bkVoiceOrderService.findOne(booking.getVoiceId());
        String orderType = null;
        // 订单是否使用了优惠券
        reMap.put("use_ticket", Utils.isEmpty(booking.getTicketNo()) ? false : true);
        if (null != order && null != order.getOrderType()) {

            String fromAddress = order.getFromAddress() == null ? "" : order.getFromAddress();
            String toAddress = order.getToAddress() == null ? "" : order.getToAddress();
            String fromShortAddress = Utils.isEmpty(order.getFromShortAddress()) ? "" : order.getFromShortAddress();
            String toShortAddress = Utils.isEmpty(order.getToShortAddress()) ? "" : order.getToShortAddress();
            String deliveryTime = TimeUtil.getDateString(order.getExpireTime(), "yyyy-MM-dd HH:mm");
            if (order.getOrderType() == 1L) { // 文本下单
                orderType = "3";
                reMap.put("price", null != offer && null != offer.getPrice() ? offer.getPrice().intValue() + "" : "");
                // 返回 发货和收货地址以及发货时间
                reMap.put("from_address", fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + fromShortAddress);
                reMap.put("to_address", toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + toShortAddress);
                reMap.put("delivery_time", deliveryTime);
            } else if (order.getOrderType() == 0L) { // 语音下单
                orderType = "2";
                reMap.put("price", null != offer && null != offer.getPrice() ? offer.getPrice().intValue() + "" : "");
                reMap.put("voice_file", Utils.isEmpty(order.getFilePath()) ? "" : Constant.AUDIO_PATH_URI_PRE + order.getFilePath() + "/" + order.getFileName());
            } else {// 直接下单
                orderType = "4";
                reMap.put("price", null != order && null != order.getReferencePrice() ? order.getReferencePrice().intValue() + "" : "");
                // 返回 发货和收货地址 以及发货时间
                reMap.put("from_address", fromAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + fromShortAddress);
                reMap.put("to_address", toAddress.replaceFirst("广东省", "").replaceFirst("深圳市", "") + toShortAddress);
                reMap.put("delivery_time", deliveryTime);
            }
        } else
            orderType = "1";

        reMap.put("booking_id", bookingNo + "");
        reMap.put("order_number", booking.getOrderNumber());
        reMap.put("create_time", Utils.formatDate(booking.getCreateDt(), null));
        if (null != info) {
            reMap.put("vehicle_id", info.getVehicleInfoId() + "");
            reMap.put("photo_thumb", Constant.IMG_URL_PRE + info.getVeHeadPath() + "thumb_" + info.getVeHeadFile());
            if (info.getModelId() == 5 || info.getModelId() == 6 || info.getModelId() == 7 || info.getHidePlates() == 1)
                reMap.put("ve_plates", !Utils.isEmpty(info.getVePlates()) ? Common.hidePlate(info.getVePlates()) : "");
            else
                reMap.put("ve_plates", !Utils.isEmpty(info.getVePlates()) ? info.getVePlates() : "");
        } else {
            reMap.put("ve_plates", "");
            reMap.put("photo_thumb", "");
        }
        reMap.put("ve_type", null != info && null != info.getModelId() ? info.getModelId() + "" : "");
        reMap.put("driver", null != pluser && !Utils.isEmpty(pluser.getUserName()) ? pluser.getUserName() : "");
        reMap.put("mobile", null != pluser && !Utils.isEmpty(pluser.getMobile()) ? pluser.getMobile() : "");
        // reMap.put("price", null != offer ? offer.getPrice().intValue()+"" :
        // 0);
        reMap.put("booking_type", orderType);
        reMap.put("order_id", null != booking.getVoiceId() ? booking.getVoiceId() + "" : "");
        reMap.put("confirm_time", null != booking.getConfirmTime() ? Utils.formatDate(booking.getConfirmTime(), "MM-dd HH:mm") : "");
        reMap.put("freight_time", null != booking.getFreightTime() ? Utils.formatDate(booking.getFreightTime(), "MM-dd HH:mm") : "");
        reMap.put("complete_time", null != booking.getCompleteTime() ? Utils.formatDate(booking.getCompleteTime(), "MM-dd HH:mm") : "");
        reMap.put("arrive_time", null != booking.getArriveTime() ? Utils.formatDate(booking.getArriveTime(), "MM-dd HH:mm") : "");
        reMap.put("cancel_time", null != booking.getCancelTime() ? Utils.formatDate(booking.getCancelTime(), "MM-dd HH:mm") : "");
        // reMap.put("voice_file", null == order ||
        // Utils.isEmpty(order.getFilePath()) ? "" : Constant.ATTACH_PATH +
        // order.getFilePath() + "/" + order.getFileName());
        // 如果是 6 默认置为7,跟客户端保持一致
        String status = booking.getBookingStatus() == 6 ? "7" : booking.getBookingStatus() + "";
        // 取消类型的订单 如果是货主取消 status返回-1
        if (booking.getBookingStatus() == 0L && null != booking.getCancelType() && 2L == booking.getCancelType()) {
            status = "-1"; // -1 货主取消 0 车主取消
        }
        reMap.put("status", status);

        // 返回 取消类型 0：未取消 1:车主取消 2：货主取消 3。。 4。。 5。。
        reMap.put("cancle_type", null == booking.getCancelType() ? "" : booking.getCancelType() + "");

        //返回是否已收藏
        int userFav = 0;
        userFav =  favoriteService.getCountByCargoIdAndVehicleInfoId(booking.getUserCargoId(), booking.getVehicleInfoId());
        reMap.put("collection", userFav > 0 ? "1" : "0");
        
        if (booking.getBookingStatus() == 6 || booking.getBookingStatus() == 7) {
            Map<String, Object> comment = new HashMap<String, Object>();
            comment.put("cargo_mobile", booking.getBookingFromUsersId());
            comment.put("score", null != booking.getCommentLevel() ? booking.getCommentLevel() + "" : "");
            comment.put("content", booking.getComment());
            comment.put("comment_time", booking.getCommentCreateDt().getTime() / 1000 + "");
            reMap.put("comment_data", comment);
        }

        result.put("errorcode", "0");
        result.put("info", "");
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 生成订单
     * 
     * @param veMobile
     * @param voiceId
     * @param lng
     * @param lat
     * @return
     */
    @ResponseBody
    @RequestMapping("/createbooking")
    @Transactional
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> createbooking(@RequestParam(value = "ve_mobile", required = false, defaultValue = "0")
    String veMobile, @RequestParam(value = "voice_id", required = false, defaultValue = "0")
    Long voiceId, @RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat) {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;
        if (!Utils.checkMobile(veMobile) || !Utils.checkMobile(user.getMobile())) {
            result.put("errorcode", "2");
            result.put("info", "选择车辆失败");
            return result;
        }

        if (user.getMobile().equalsIgnoreCase(veMobile)) {
            result.put("errorcode", "2");
            result.put("info", "亲，不能给自己下单");
            return result;
        }

        if (lng == 0 || lat == 0) {
            result.put("errorcode", "2");
            result.put("info", "选择车辆失败,无法获取您的位置");
            return result;
        }

        if (voiceId > 0) {
            BkVoiceOrder order = bkVoiceOrderService.findOne(voiceId);
            if (!user.getMobile().equals(order.getCargoMobile())) {
                result.put("errorcode", "4");
                result.put("info", "服务器忙晕啦，请稍候再试");
                return result;
            }
            List<BkOrderOffer> offers = bkOrderOfferService.findByOrderIdAndVeMobile(voiceId, veMobile);
            if (null == offers || offers.size() == 0) {
                result.put("errorcode", "4");
                result.put("info", "服务器忙晕啦，请稍候再试");
                return result;
            }

            int count = bkBookingService.getCountByVehicleInfoIdAndBookingFromUsersIdAndVoiceId(offers.get(0).getVehicleInfoId(), user.getMobile(), voiceId);
            if (count > 0) {
                result.put("errorcode", "3");
                result.put("info", "已成功选择车辆");
                return result;
            }

            // 如果是文本订单 报价表里更改状态 2014/9/24

            if (null != offers && offers.size() > 0) {
                BkOrderOffer offer = offers.get(0);
                offer.setStatus(1L); // 0：未选 , 1：选他 , 2：取消选他
                bkOrderOfferService.save(offer);
            }

            int count2 = bkBookingService.getCountByBookingFromUsersIdAndVoiceId(user.getMobile(), voiceId);
            if (count2 > 0) {
                result.put("errorcode", "3");
                result.put("info", "您已选择其它车辆");
                return result;
            }
        }
        List<PlUsers> plusers = plUsersService.findByMobileAndIsvalid(veMobile, "Y");
        if (null == plusers || plusers.size() == 0) {
            result.put("errorcode", "2");
            result.put("info", "选择车辆失败");
            return result;
        }
        List<VeVehicleInfo> infos = veVehicleInfoService.findVehicleByUserId(plusers.get(0).getUserId());
        if (null == infos || infos.size() == 0) {
            result.put("errorcode", "2");
            result.put("info", "选择车辆失败");
            return result;
        }

        // 校验与车主的是否已经存在未完成的订单
        // 同一个车主对同一个货主，上一订单未确认收货，不能选TA,提示:该车主与您有未完成的订单，您确认收货后才能进行新交易(地图订单有该要求，文本订单不做限制）
        if (voiceId == 0) {
            int unfinishedCount = bkBookingService.getUnfinishedBookingByUserCargoIdAndToUserId(user.getUserCargoId(), plusers.get(0).getUserId());
            if (unfinishedCount > 0) {
                result.put("errorcode", "5");
                result.put("info", "该车辆与您有未完成的订单，您确认收货后才能进行新交易。");
                return result;
            }
        }

        // // 找到最近的通话记录
        // List<BkInquiryLog> inquiryList =
        // bkInquiryLogService.findByUserMobileAndUserCargoMobileOrderByInquiryDtDesc(veMobile,
        // user.getMobile());
        // Long talkSec = 0L;
        // if (inquiryList != null && inquiryList.size() > 0) {
        // // 通话时间 (当前时间 - 第一条记录的时间) 保存到订单表里
        // BkInquiryLog inquiry = inquiryList.get(0);
        // // 点拨打电话时间
        // Long b = inquiry.getInquiryDt().getTime();
        // // 当前时间戳
        // Long now = System.currentTimeMillis();
        // // 相差秒
        // talkSec = (now - b) / 1000;
        // // 如果通话时间太长置为2秒
        // if (talkSec > 1000) {
        // talkSec = 2L;
        // }
        // // 如果通话时间是负数 取绝对值
        // if (talkSec < 0) {
        // talkSec = talkSec * -1;
        // }
        // }

        Map<String, Object> reMap = new HashMap<String, Object>();
        reMap.put("vehicle_info_id", infos.get(0).getVehicleInfoId() + "");
        reMap.put("user_cargo_id", null != user.getUserCargoId() ? user.getUserCargoId() + "" : "");
        reMap.put("booking_from_users_id", user.getMobile());
        reMap.put("booking_to_users_id", plusers.get(0).getUserId() + "");
        reMap.put("booking_status", "1");
        reMap.put("create_dt", Utils.formatDate(new Date(), null));
        reMap.put("voice_id", voiceId + "");
        reMap.put("lng", lng + "");
        reMap.put("lat", lat + "");
        String orderNumberPre = Utils.formatDate(new Date(), "yyyyMMdd");
        long orderNumberTail = (int) (Math.random() * 899999999 + 100000000);
        String orderNumber = orderNumberPre + orderNumberTail;
        reMap.put("order_number", orderNumber);

        BkBooking booking = new BkBooking();
        booking.setVehicleInfoId(infos.get(0).getVehicleInfoId());
        booking.setUserCargoId(user.getUserCargoId());
        booking.setBookingFromUsersId(user.getMobile());
        booking.setBookingToUsersId(plusers.get(0).getUserId());
        booking.setBookingStatus(1L);
        booking.setCreateDt(new Date());
        booking.setVoiceId(voiceId);
        booking.setLat(lat);
        booking.setLng(lng);
        booking.setOrderNumber(orderNumber);
        booking.setCargoImei(getImei());
        // booking.setTalkSecs(talkSec);
        if (voiceId != 0 && voiceId != null) {
            BkVoiceOrder voiceOrder = bkVoiceOrderService.findOne(voiceId);
            if (voiceOrder != null) {
                booking.setTicketNo(voiceOrder.getTicketNo() == null ? "" : voiceOrder.getTicketNo());
            }
        }

        // 如果是金牌车主 加上敢用该赔
        String tips = "选择车辆成功";
        if (infos.get(0).getOwnerLevel() == 1001L) {
            booking.setUseCompensation(true);
            tips = "您会享受到金牌司机的敢用敢赔服务！";
        } else {
            booking.setUseCompensation(false);

        }

        bkBookingService.save(booking);

        if (null != booking.getBookingNo() && booking.getBookingNo() > 0) {
            Map<String, String> pushMap = new HashMap<String, String>();
            String mobileTail = user.getMobile().substring(user.getMobile().length() - 4);
            if (voiceId > 0) {
                pushMap.put("type", "3");
                pushMap.put("booking_id", booking.getBookingNo() + "");
                pushMap.put("msg", "恭喜！抢单成功，电话尾号" + mobileTail + "的闪发车货主选择您的报价，请确定服务。【闪发车】");
            } else {
                pushMap.put("type", "3");
                pushMap.put("booking_id", booking.getBookingNo() + "");
                pushMap.put("msg", "电话尾号" + mobileTail + "的闪发车货主选您运货，请确认订单。【闪发车】");
            }

            // 临时解决方案，货主手机号和ID号注册的XMPP用户都发PUSH
            // 过渡期过后只发给以ID号注册的XPMM用户
            String pushJson = JsonUtils.object2Json(pushMap);
            xmppService.push(veMobile, pushJson, "v_");

            if (voiceId > 0) {
            	
            	boolean flag = true;
            	
                BkVoiceOrder bvo = bkVoiceOrderService.findOne(voiceId);
                if(null != bvo.getCompleteOffer() && bvo.getCompleteOffer() ){
                	flag = false;
                }
                bvo.setIsBooking(true);
             // 生成订单了 报价结束 更新bk_voice_order表里 抢单是否结束的字段
                bvo.setCompleteOffer(true); // 抢单结束
                if (!bkVoiceOrderService.save(bvo)) {
                    result.put("errorcode", "4");
                    result.put("info", "服务器忙晕了，请稍后再试");
                }

                //如果抢单结束了  不对其他抢单失败的发推送了
                if(flag){
                	 List<BkOrderOffer> offers = bkOrderOfferService.getByOrderIdAndMobileNot(voiceId, veMobile);
                     List<String> errMobiles = new ArrayList<String>();
                     if (offers.size() > 0) {
                         for (BkOrderOffer offer : offers) {
                             errMobiles.add(offer.getVeMobile());
                         }
                     }
                     pushMap = new HashMap<String, String>();
                     pushMap.put("type", "7");
                     pushMap.put("msg", "抱歉，抢单失败，货主选择了其他师傅的报价。请再接再厉！【闪发车】");
                     xmppService.push(errMobiles, JsonUtils.object2Json(pushMap), "v_");
                }
               
            }
            reMap.put("booking_id", null != booking.getBookingNo() ? booking.getBookingNo() + "" : "");

        }
        result.put("errorcode", "0");
        result.put("info", tips);
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 确认收货
     * 
     * @param bookingNo
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/confirmbooking")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> confirmBooking(@RequestParam(value = "booking_id", required = false, defaultValue = "0")
    Long bookingNo) {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo user = (PlUsersCargo) obj;

        try {
            BkBooking booking = bkBookingService.findOne(bookingNo);
            if (null == booking || !booking.getUserCargoId().equals(user.getUserCargoId())) {
                result.put("errorcode", "2");
                result.put("info", "订单不存在");
                return result;
            }

            VeVehicleInfo info = veVehicleInfoService.findOne(booking.getVehicleInfoId());

            if (booking.getBookingStatus() != 4 && null != info) {
                result.put("errorcode", "3");
                result.put("info", "订单状态错误");
                return result;
            }

            // PlUsers vehicleUser =
            // plUsersService.findOne(booking.getBookingToUsersId());
            // String vehicleMobile = vehicleUser.getMobile();
            // ====================防止车主耍单 制定市场规则校验 有以下情况置为无效单
            // 控制不刷车辆星级（isvalid=0）
            // ============================//
            //
            // 'FREIGHT_TIME' => 600, //运货时间允许的最小值 10分钟 （时间戳 秒）
            // 'FREIGHT_DISTANCE' => 5000, //开始运货与货已运到两点间的距离 允许最小值 单位（米、m）
            // 'FREIGHT_DELIVERY_DISTANCE' => 1000, //文本订单的开始运货与发货地址两点间的距离 允许最大值
            // 单位（米、m）
            // 'ARRIVE_DESTINATION_DISTANCE' => 1000, //文本订单的货已运到与收货地址两点间的距离
            // 允许最大值
            // 单位（米、m）
            // 'PREVARRIVE_NOWARRIVE_TIME' => 1800 //同一个车主与同一个货主上一单运到 至 现在这单运到时间
            // 时间间隔 允许最小值 30分钟（时间戳 秒）

            int minute_10 = 10 * 60 * 1000;
            int minute_20 = 20 * 60 * 1000;
            int ARRAY_DISTANCE = 1000;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date start = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Date end = cal.getTime();
            String remark = "";
            booking.setIsvalid(true);

            // cooperation＝1为合作车辆，不做规则限制
            if (null == info.getCooperation() || !info.getCooperation()) {

                // =======同一车主，货主在一天内的完成的有效订单数目，已经有3个，视为无效单
                int count = bkBookingService.getCountWithValidOrder(bookingNo, booking.getUserCargoId(), booking.getBookingToUsersId(), start, end);
                if (count > 3) {
                    booking.setIsvalid(false);
                    remark += "同一车主与同一货主一天订单数>3单;";
                }

                // =========下订单时间与确认收货的时间不满20分钟，算无效订单
                if (System.currentTimeMillis() - booking.getCreateDt().getTime() < minute_20) {
                    booking.setIsvalid(false);
                    remark += "订单创建时间与确认收货时间不足20分钟;";
                }

                // =========若开始运货与货已运到的时间间隔不足10分钟该订单将视为无效
                if (booking.getArriveTime().getTime() - booking.getFreightTime().getTime() < minute_10) {
                    booking.setIsvalid(false);
                    remark += "开始运货与货已运到时间不足10分钟;";
                }

                // 面包车1天10单，货车6单，超出视为无效
                int orderCount = bkBookingService.findOrderCount(info.getVehicleInfoId(), start, end);
                if (orderCount > 0) {
                    if (info.getModelId() == 5 || info.getModelId() == 6 || info.getModelId() == 7) {
                        if (orderCount >= 10) {
                            booking.setIsvalid(false);
                            remark += "该面包车当天有效单超过10单;";
                        }
                    } else {
                        if (orderCount >= 6) {
                            booking.setIsvalid(false);
                            remark += "该货车当天有效单超过6单;";
                        }
                    }
                }

                if (!Utils.isEmpty(booking.getCargoImei()) && !Utils.isEmpty(booking.getVehicleImei())) {
                    if (booking.getCargoImei().equals(booking.getVehicleImei())) {
                        booking.setIsvalid(false);
                        remark += "货主与车主IMEI号一致;";
                    }
                }
                // 指定的收货地址和车主点货已运到的地址相差大于500米 订单无效
                Long voiceId = booking.getVoiceId();
                if (voiceId != null && voiceId != 0) {
                    BkVoiceOrder voiceOrder = bkVoiceOrderService.findOne(voiceId);
                    if (voiceOrder != null && voiceOrder.getToLatitude() != null && voiceOrder.getToLongitude() != null) {
                        // 收货地址经纬度
                        Double toLat = voiceOrder.getToLatitude();
                        Double toLng = voiceOrder.getToLongitude();
                        // 货已运到时经纬度
                        List<VeUploadGpsMG> gpsList = veUploadGpsService.findByUserIdAndUptimeBetweenOrderByUptimeDesc(booking.getBookingToUsersId(), booking.getFreightTime()
                                .getTime(), booking.getArriveTime().getTime());
                        if (gpsList == null || gpsList.size() <= 1) {
                            booking.setIsvalid(false);
                            remark += "开始运货到货已运到之间少于或只有一条坐标信息;";
                        } else {
                            VeUploadGpsMG gps = gpsList.get(0);
                            Double arrayLat = Double.valueOf(gps.getLat());
                            Double arrayLng = Double.valueOf(gps.getLng());
                            Double distance = Utils.GetDistance(toLat, toLng, arrayLat, arrayLng);
                            if (distance > ARRAY_DISTANCE) {
                                if (bkBookingService.existInvalidBookingWithDistanceRuleByCargoAndReMark(booking.getUserCargoId(), "货主下单指定的收货地址与车主点货已运到的地址距离")) {
                                    booking.setIsvalid(false);
                                    remark += "货主下单指定的收货地址与车主点货已运到的地址距离（" + distance + "）相差大于1000，订单无效;";
                                } else {
                                    if (booking.getIsvalid())
                                        remark = "货主下单指定的收货地址与车主点货已运到的地址距离（" + distance + "）相差大于1000，订单本无效，因是第一单，改判有效;";
                                    else
                                        remark += "货主下单指定的收货地址与车主点货已运到的地址距离（" + distance + "）相差大于1000，订单无效;";
                                }
                            }
                        }
                    }
                }

                /*
                 * // =======同一个车主与同一个货主上一单运到后30分钟才能成交 否则无效 BkBooking booking2 =
                 * bkBookingService
                 * .getDeliverOrderWithSameCargoAndSameVehicle(bookingNo,
                 * booking.getUserCargoId(), booking.getBookingToUsersId()); if
                 * (null != booking2 && null != booking2.getArriveTime()) { if
                 * (booking.getArriveTime().getTime() -
                 * booking2.getArriveTime().getTime() <
                 * PREVARRIVE_NOWARRIVE_TIME) booking.setIsvalid(false); }
                 * PlUsers pluser =
                 * plUsersService.findOne(booking.getBookingToUsersId());
                 * VeUploadGps first =
                 * veUploadGpsService.findByVeModelAndUptimeAfterAndUptimeBefore
                 * (pluser.getMobile(), booking.getFreightTime(),
                 * booking.getArriveTime()); VeUploadGps last =
                 * veUploadGpsService.findByVeModelAndUptimeAfter
                 * (pluser.getMobile(), booking.getArriveTime());
                 * 
                 * // 查询货已运到的经纬度 // ==========若开始运货与货已运到两点间的距离不足5KM该订单将视为无效 if
                 * (null != first && null != last) { // Double distance =
                 * Common.getDistance(Double.valueOf(first.getLongitude()),
                 * Double.valueOf(first.getLatitude()),
                 * Double.valueOf(last.getLongitude()),
                 * Double.valueOf(last.getLatitude())); // 运货的长度距离不足 if
                 * (distance < FREIGHT_DISTANCE) booking.setIsvalid(false);
                 * 
                 * // 文本订单下处理 if (null != booking.getVoiceId() &&
                 * booking.getVoiceId() > 0) { BkVoiceOrder order =
                 * bkVoiceOrderService.findOne(booking.getVoiceId()); // //
                 * ==========文本订单的开始运货与发货地址两点间的距离超过1KM该订单将视为无效 Double
                 * freDistance =
                 * Common.getDistance(Double.valueOf(first.getLongitude()),
                 * Double.valueOf(first.getLatitude()), order.getLongitude(),
                 * order.getLatitude()); if (freDistance >
                 * FREIGHT_DELIVERY_DISTANCE) booking.setIsvalid(false);
                 * 
                 * // ==========文本订单的货已运到与收货地址两点间的距离超过1KM该订单将视为无效 Double
                 * arrDistance =
                 * Common.getDistance(Double.valueOf(last.getLongitude()),
                 * Double.valueOf(last.getLatitude()), order.getLongitude(),
                 * order.getLatitude()); if (arrDistance >
                 * ARRIVE_DESTINATION_DISTANCE) booking.setIsvalid(false); } }
                 * else booking.setIsvalid(false);
                 */
            }
            booking.setBookingStatus(5L);
            booking.setCompleteTime(new Date());
            booking.setIsRead(true);
            booking.setRemarks(remark);
            bkBookingService.save(booking);// 保存订单

            String tips = ""; // 用作提示用户是否获得积分

            String pushTips = "";

            /**
             * 判断单子是不是华强北(3公里内)的单子 2014/9/16 是的就返现 不是就不返现
             * 华强北坐标点（114.091837,22.547244） 取当前下单的坐标点
             */
            boolean isHQB = false;

            Long voiceId = booking.getVoiceId();
            if (voiceId != null && voiceId != 0) {
                BkVoiceOrder voiceOrder = bkVoiceOrderService.findOne(voiceId);
                // 发货地址经纬度
                Double fromLat = voiceOrder.getFromLatitude();
                Double fromLng = voiceOrder.getFromLongitude();
                // 收货地址经纬度
                Double toLng = voiceOrder.getToLongitude();
                Double toLat = voiceOrder.getToLatitude();

                Double fromDistance = Utils.GetDistance(fromLat, fromLng, 22.547244, 114.091837);
                Double toDistance = Utils.GetDistance(toLat, toLng, 22.547244, 114.091837);

                if (fromDistance <= 3000 || toDistance <= 3000) {
                    isHQB = true;
                } else {
                    isHQB = false;
                }
            }

            // 订单完成 并且有效 代金券变成可提现状态 否则变成未使用状态
            String ticketNo = booking.getTicketNo();
            long ticketStatus = ConstantUtil.TICKET_STATUS_USED_2;
            if (!Utils.isEmpty(ticketNo)) {
                boolean isCancel = false;
                if (booking.getIsvalid()) {

                    isCancel = false;
                    ticketStatus = ConstantUtil.TICKET_STATUS_USED_2;
                    // 优惠券置为已使用
                    creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(ticketNo, booking.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_USED_2, false);
                    // LOG.info(booking.getTicketNo()+"  优惠券置为已使用！");
                    // 货主余额增加代金券面额
                    TbTicketInfo ticket = tbTicketInfoService.queryTbTicketInfoByUserIdAndTicketNoAndUserType(booking.getUserCargoId(), ticketNo, UserType.cargo);
                    // 获取余额
                    TbUserBalance userBlance = tbUserBalanceService.findByUserIdAndUserType(booking.getUserCargoId(), UserType.cargo);
                    // 如果没有当前用户的余额信息 新增一个当前用户余额信息 余额为0
                    if (userBlance == null) {
                        userBlance = new TbUserBalance();
                        userBlance.setUserId(booking.getUserCargoId());
                        userBlance.setUserType(UserType.cargo);
                        userBlance.setBalance(new BigDecimal(0));
                    }
                    BigDecimal blance = userBlance.getBalance();
                    // 将优惠券金额加到用户余额里面
                    userBlance.setBalance(blance.add(new BigDecimal(ticket.getTicketAmount())));
                    tbUserBalanceService.save(userBlance);

                    // 给邀请人增加积分
                    RetResult re = creditAndTicketManagementService.addOrderSendIntegral(booking.getUserCargoId(), UserType.cargo, user.getMobile(), "下单成功 赠送积分", true);
                    LOG.info("订单完成 ，邀请人增加积分：" + JsonUtils.object2Json(re));
                    Map<String, Object> configMap = creditAndTicketManagementService.getAllConfigInfo();
                    Long inviteePoint = Long.parseLong(configMap.get(ConstantUtil.SEND_INTEGRAL_CONSTANT).toString());// 下单送积分
                    // 第一个有效订单给邀请人加钱
                    Double sendMoneyByFirstBk = Double.valueOf(configMap.get(ConstantUtil.SEND_MONEY_FIRST_BOOKING).toString());
                    RetResult rr = creditAndTicketManagementService.giveMoneyToUserCargoByOrderIsvalid(booking.getUserCargoId(), sendMoneyByFirstBk,
                            booking.getBookingFromUsersId(), "", true);
                    LOG.info("第一个有效单，有邀请人送钱" + JsonUtils.object2Json(rr));
                    if ("0".equals(rr.getErrorcode())) {
                        if (rr.getRetData().get("mobile") != null && rr.getRetData().get("user_id") != null) {
                            String recommendMobile = rr.getRetData().get("mobile").toString();
                            String recommendUserId = rr.getRetData().get("user_id").toString();
                            Map<String, String> psMap = new HashMap<String, String>();
                            psMap.put("type", "10");
                            psMap.put("msg", "好友" + Common.formatPhoneNumber(user.getMobile()) + "接受邀请加入【闪发车】，您获得可提现金额" + sendMoneyByFirstBk + "元，请到我的财富查看");
                            xmppService.push(recommendUserId + "", JsonUtils.object2Json(psMap), "c_");
                            // 发短信
                            smsSendService.sendSms(recommendMobile, "好友" + Common.formatPhoneNumber(user.getMobile()) + "接受邀请加入【闪发车】，您获得可提现金额" + sendMoneyByFirstBk + "元，请到我的财富查看");

                        }
                    }

                    // 有效单给货主加10元现金 华强北的才加钱
//                    if (isHQB) {
//                        Double inviteeMoney = Double.valueOf(configMap.get(ConstantUtil.SEND_MONEY_BOOKING).toString());
//                        creditAndTicketManagementService.addBalanceToUser(booking.getUserCargoId(), UserType.cargo, inviteeMoney, booking.getBookingFromUsersId(), "有效单 加10元现金",
//                                true);
//                        tips = "成功完成订单，获得" + inviteeMoney + "元可提现金额和" + inviteePoint + "积分,";
//                    }
                    tips = "成功完成订单，获得" + inviteePoint + "积分";

                    // 有效单 给车主加积分
                    creditAndTicketManagementService.sendIntegral(booking.getBookingToUsersId(), UserType.owners, inviteePoint, "订单有效 ，给车主增加积分", true);
                    pushTips = "完成订单，" + inviteePoint + "积分请笑纳";

                } else {
                    isCancel = true;
                    ticketStatus = ConstantUtil.TICKET_STATUS_UNUSED_0;
                    LOG.info("无效订单，" + booking.getTicketNo() + "  优惠券置为未使用！");
                }
                // 优惠券重新置为未使用
                creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(booking.getTicketNo(), booking.getUserCargoId(), UserType.cargo, ticketStatus, isCancel);

            } else { // 未使用优惠券
                // 有效订单+积分 无效不加积分 不提示
                if (booking.getIsvalid()) {
                    // 给邀请人增加积分
                    RetResult re = creditAndTicketManagementService.addOrderSendIntegral(booking.getUserCargoId(), UserType.cargo, user.getMobile(), "下单成功 赠送积分", true);
                    LOG.info("订单完成 ，邀请人增加积分：" + JsonUtils.object2Json(re));
                    Map<String, Object> configMap = creditAndTicketManagementService.getAllConfigInfo();
                    // 第一个有效订单给邀请人加钱
                    Double sendMoneyByFirstBk = Double.valueOf(configMap.get(ConstantUtil.SEND_MONEY_FIRST_BOOKING).toString());
                    RetResult rr = creditAndTicketManagementService.giveMoneyToUserCargoByOrderIsvalid(booking.getUserCargoId(), sendMoneyByFirstBk,
                            booking.getBookingFromUsersId(), "", true);
                    LOG.info("第一个有效单，有邀请人送钱" + JsonUtils.object2Json(rr));
                    if ("0".equals(rr.getErrorcode())) {

                        if (rr.getRetData().get("mobile") != null && rr.getRetData().get("user_id") != null) {
                            String recommendMobile = rr.getRetData().get("mobile").toString();
                            String recommendUserId = rr.getRetData().get("user_id").toString();
                            Map<String, String> psMap = new HashMap<String, String>();
                            psMap.put("type", "10");
                            psMap.put("msg", "好友" + Common.formatPhoneNumber(user.getMobile()) + "接受邀请加入【闪发车】，您获得可提现金额" + sendMoneyByFirstBk + "元，请到我的财富查看");
                            xmppService.push(recommendUserId + "", JsonUtils.object2Json(psMap), "c_");
                            // 发短信
                            smsSendService.sendSms(recommendMobile, "好友" + Common.formatPhoneNumber(user.getMobile()) + "接受邀请加入【闪发车】，您获得可提现金额" + sendMoneyByFirstBk + "元，请到我的财富查看");
                        }
                    }
                    Long inviteePoint = Long.parseLong(configMap.get(ConstantUtil.SEND_INTEGRAL_CONSTANT).toString());// 下单送积分

                    // 有效单给货主加10元现金 （华强北的才加钱）
//                    if (isHQB) {
//                        Double inviteeMoney = Double.valueOf(configMap.get(ConstantUtil.SEND_MONEY_BOOKING).toString());
//                        creditAndTicketManagementService.addBalanceToUser(booking.getUserCargoId(), UserType.cargo, inviteeMoney, booking.getBookingFromUsersId(), "有效单 加10元现金",
//                                true);
//                        tips = "成功完成订单，获得" + inviteeMoney + "元可提现金额和" + inviteePoint + "积分,";
//                    }
                    tips = "成功完成订单，获得" + inviteePoint + "积分";
                    // 有效单 给车主加积分
                    creditAndTicketManagementService.sendIntegral(booking.getBookingToUsersId(), UserType.owners, inviteePoint, "订单有效 ，给车主增加积分", true);
                    pushTips = "完成订单，" + inviteePoint + "积分请笑纳";

                }
            }

            PlUsers pluser = plUsersService.findOne(booking.getBookingToUsersId());
            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("type", "1");
            pushMap.put("booking_id", booking.getBookingNo().toString());
            pushMap.put("msg", "恭喜交易完成，货主已确认货物运达目的地，" + pushTips + " 点击查看订单详情。【闪发车】");
            xmppService.push(pluser.getMobile(), JsonUtils.object2Json(pushMap), "v_");
            result.put("errorcode", "0");
            result.put("info", "确认成功");
            result.put("complete_time", Utils.formatDate(new Date(), "MM-dd HH:mm"));
            result.put("tips", tips);
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } catch (Exception e) {
            LOG.error("The confirmBooking() method invocation exception.", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * 货主新消息
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/newmessage")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> newMessage() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        try {
            PlUsersCargo user = (PlUsersCargo) obj;
            int bookingCount = bkBookingService.getCountWithFromUserIdAndIsRead(user.getMobile(), false);
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("new_booking_num", bookingCount + "");
            result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", reMap);
            return result;

        } catch (Exception e) {
            result.put("errorcode", "2");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The newMessage() method invocation exception.", e);
        }
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 车辆评论列表
     * 
     * @param vehicleId
     * @param pageNo
     * @param size
     * @param score
     * @return
     */
    @ResponseBody
    @RequestMapping("/commentlist")
    public Map<String, Object> commentList(@RequestParam(value = "vehicle_id", required = false, defaultValue = "0")
    Long vehicleId, @RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageNo, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int size, @RequestParam(value = "score", required = false, defaultValue = "0")
    Long score) {
        int totalGood = 0;
        int totalBad = 0;
        int totalPage = 0;
        List<Object> list = new ArrayList<Object>();
        Page<BkBooking> page = null;
        if (score > 0)
            page = bkBookingService.findByVehicleInfoIdAndBookingStatusAndCommentLevel(vehicleId, 7L, score, pageNo, size);
        else {
            page = bkBookingService.findByVehicleInfoIdAndBookingStatus(vehicleId, 7L, pageNo, size);
            totalGood = bkBookingService.getCommentCountWithCommentLevel(vehicleId, 2L);
            totalBad = bkBookingService.getCommentCountWithCommentLevel(vehicleId, 1L);
        }

        if (page.hasContent()) {
            totalPage = page.getTotalPages();
            for (BkBooking booking : page.getContent()) {
                Map<String, Object> reMap = new HashMap<String, Object>();
                reMap.put("booking_id", booking.getBookingNo() + "");
                reMap.put("cargo_mobile", Common.formatPhoneNumber(booking.getBookingFromUsersId()));
                reMap.put("score", null != booking.getCommentLevel() ? booking.getCommentLevel() + "" : "");
                reMap.put("content", booking.getComment());
                reMap.put("comment_time", booking.getCommentCreateDt().getTime() / 1000 + "");
                list.add(reMap);
            }
        } else {
            if (page.getTotalPages() - 1 <= pageNo) {
                result.put("errorcode", "0");
                result.put("total_num", page.getTotalElements() + "");
                result.put("total_good", totalGood + "");
                result.put("total_bad", totalBad + "");
                result.put("total_page", totalPage + "");
                result.put("data", list);
                if (pageNo == 0) {
                    result.put("info", "");
                } else {
                    result.put("info", "没有更多评论");
                }
                return result;
            }
        }
        result.put("errorcode", "0");
        result.put("total_num", page.getTotalElements() + "");
        result.put("total_good", totalGood + "");
        result.put("total_bad", totalBad + "");
        result.put("total_page", totalPage + "");
        result.put("data", list);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 车辆行驶轨迹
     * 
     * @param bookingNo
     * @return
     */
    @ResponseBody
    @RequestMapping("/bookingtrack")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> bookingTrack(@RequestParam(value = "booking_id", required = false, defaultValue = "0")
    Long bookingNo) {
        BkBooking booking = bkBookingService.findOne(bookingNo);
        if (null == booking) {
            result.put("errorcode", "2");
            result.put("info", "获取运行轨迹失败");
            result.put("data", new String[] {});
            return result;
        }

        Date endTime = null;
        if(null ==  booking.getFreightTime()){
        	result.put("errorcode", "3");
            result.put("info", "运货还没开始");
            result.put("data", new String[] {});
            return result;
        }
        if (null != booking.getArriveTime())
            endTime = booking.getArriveTime();
        else
            endTime = new Date();

        List<VeUploadGpsMG> list = veUploadGpsService
                .findByUserIdAndUptimeBetweenOrderByIdAsc(booking.getBookingToUsersId(), booking.getFreightTime().getTime(), endTime.getTime());
        List<Object> reList = new ArrayList<Object>();
        if (null != list && list.size() > 0) {
            for (VeUploadGpsMG gps : list) {
                Map<String, Object> reMap = new HashMap<String, Object>();
                reMap.put("longitude", gps.getLng());
                reMap.put("latitude", gps.getLat());
                reMap.put("upload_time", gps.getUptime() / 1000 + "");
                reList.add(reMap);
            }
        } else {
            Map<String, Object> reMap = new HashMap<String, Object>();
            reMap.put("longitude", booking.getLng() + "");
            reMap.put("latitude", booking.getLat() + "");
            reMap.put("upload_time", booking.getCreateDt().getTime() / 1000 + "");
            reList.add(reMap);
        }

        result.put("errorcode", "0");
        result.put("info", "");
        result.put("data", reList);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 登录
     * 
     * @param userName
     *            用户名或手机号
     * @param passWord
     *            密码
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestParam(value = "username", required = false, defaultValue = "")
    String userName, @RequestParam(value = "password", required = false, defaultValue = "")
    String passWord) throws Exception {
        if (Utils.isEmpty(userName) || Utils.isEmpty(passWord)) {
            result.put("errorcode", "2");
            result.put("info", "用户名或密码错误");
            return result;
        }
        PlUsersCargo cargo = plusersCargoService.login(userName, MD5.encrypt(passWord));
        if (null == cargo) {
            result.put("errorcode", "2");
            result.put("info", "用户名或密码错误");
            return result;
        }

        cargo.setVersion(getVersionName());
        plusersCargoService.save(cargo);

        Map<String, Object> reMap = new HashMap<String, Object>();

        // 生成新SESSIONID
        String oldSessionId = null;
        if (null != request.getSession().getAttribute(Constant.SESSION_PL_USER_CARGO))
            oldSessionId = request.getSession().getId();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.SESSION_PL_USER_CARGO, JsonUtils.object2Json(cargo));
        String sessid = SessionService.getInstance().createSession(cargo.getUserCargoId() + "", SessionService.USER_TYPE_CARGO, map, oldSessionId);
        if (!Utils.isEmpty(sessid))
            setValueToCookie(Constant.SESSION_NAME, SecurityUtil.encrypt(sessid));
        reMap.put("username", Utils.isEmpty(cargo.getUserName()) ? "" : cargo.getUserName());
        reMap.put("mobile", cargo.getMobile());
        reMap.put("user_cargo_id", null != cargo.getUserCargoId() ? cargo.getUserCargoId() + "" : "");

        Long recId = cargo.getRecommendId();
        Long recType = cargo.getRecommendType();
        String reMobile = null;
        if (null != recId && recId > 0) {
            if (recType == 1) {
                PlUsersCargo recCargo = plusersCargoService.findOne(cargo.getRecommendId());
                if (null != recCargo)
                    reMobile = recCargo.getMobile();
            } else if (recType == 2) {
                PlUsers vehicle = plUsersService.findOne(cargo.getRecommendId());
                if (null != vehicle)
                    reMobile = vehicle.getMobile();
            } else if (recType == 3) {
                TbStaff staff = tbStaffService.findOne(cargo.getRecommendId());
                if (null != staff)
                    reMobile = staff.getStaffTel();
            } else
                reMobile = "";
        }
        reMap.put("recommend_mobile", null != reMobile ? reMobile : "");

        result.put("errorcode", "0");
        result.put("info", "成功");
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 找回密码
     * 
     * @param mobile
     *            手机号
     * @param passWord
     *            密码
     * @param verifyCode
     *            短信验证码
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/forgetpasswd")
    public Map<String, Object> forgetPasswd(@RequestParam(value = "mobile", required = false, defaultValue = "")
    String mobile, @RequestParam(value = "password", required = false, defaultValue = "")
    String passWord, @RequestParam(value = "verifycode", required = false, defaultValue = "")
    String verifyCode) {
        if (!Utils.checkMobile(mobile)) {
            result.put("errorcode", "2");
            result.put("info", "手机号码错误");
            return result;
        }
        if (Utils.isEmpty(verifyCode)) {
            result.put("errorcode", "3");
            result.put("info", "验证码错误");
            return result;
        }
        // TODO 密码需要加密传输
        if (Utils.isEmpty(passWord) || passWord.length() < 6 || passWord.length() > 24) {
            result.put("errorcode", "4");
            result.put("info", "新密码错误");
            return result;
        }

        try {
            PlUsersCargo cargo = plusersCargoService.findByMobileAndUserPwdIsNotNull(mobile);
            if (null == cargo) {
                result.put("errorcode", "2");
                result.put("info", "手机号码不存在");
                return result;
            }

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

            // 更新短信验证码状态
            smsAuth.setStatus(true);
            smsAuthenticationService.save(smsAuth);

            // 更新货主
            cargo.setUserPwd(MD5.encrypt(passWord));
            plusersCargoService.save(cargo);

            result.put("errorcode", "0");
            result.put("info", "成功");
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } catch (Exception e) {
            result.put("errorcode", "5");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The forgetPasswd() method invocation exception.", e);
            throw new RuntimeException("The forgetPasswd() method invocation fail.");
        }
    }

    /**
     * 修改密码
     * 
     * @param oldPw
     * @param newPw
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/modifypasswd")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> modifyPasswd(@RequestParam(value = "oldpasswd", required = false, defaultValue = "")
    String oldPw, @RequestParam(value = "newpasswd", required = false, defaultValue = "")
    String newPw) {
        if (Utils.isEmpty(oldPw)) {
            result.put("errorcode", "3");
            result.put("info", "旧密码错误");
            return result;
        }
        // TODO 密码需要加密传输
        if (Utils.isEmpty(newPw) || newPw.length() < 6 || newPw.length() > 24) {
            result.put("errorcode", "4");
            result.put("info", "新密码不合法");
            return result;
        }

        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);

        try {
            PlUsersCargo sessionCargo = (PlUsersCargo) obj;
            String userName = Utils.isEmpty(sessionCargo.getUserName()) ? sessionCargo.getMobile() : sessionCargo.getUserName();
            PlUsersCargo cargo = plusersCargoService.login(userName, MD5.encrypt(oldPw));
            if (null != cargo) {
                // 更新货主信息、更新SESSION中的货主对象
                cargo.setUserPwd(MD5.encrypt(newPw));
                plusersCargoService.save(cargo);
                request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, cargo);
                result.put("errorcode", "0");
                result.put("info", "密码修改成功");
                LOG.info("result" + JsonUtils.object2Json(result));
                return result;
            } else {
                result.put("errorcode", "3");
                result.put("info", "旧密码错误");
                return result;
            }
        } catch (Exception e) {
            result.put("errorcode", "5");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The modifyPasswd() method invocation Exception.", e);
            throw new RuntimeException("The modifyPasswd() method invocation fail.");
        }
    }

    /**
     * 修改手机号
     * 
     * @param passwd
     * @param mobile
     * @param verifyCode
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/modifymobile")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    Map<String, Object> modifyMobile(@RequestParam(value = "passwd", required = false, defaultValue = "")
    String passwd, @RequestParam(value = "mobile", required = false, defaultValue = "")
    String mobile, @RequestParam(value = "verifycode", required = false, defaultValue = "")
    String verifyCode) {

        // TODO 密码需要加密传输
        if (Utils.isEmpty(passwd) || passwd.length() < 6 || passwd.length() > 24) {
            result.put("errorcode", "2");
            result.put("info", "密码错误");
            return result;
        }
        if (!Utils.checkMobile(mobile)) {
            result.put("errorcode", "3");
            result.put("info", "新手机号错误");
            return result;
        }

        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        try {
            PlUsersCargo sessionCargo = (PlUsersCargo) obj;
            PlUsersCargo verifyCargo = plusersCargoService.findByMobile(mobile);
            if (null != verifyCargo) {
                result.put("errorcode", "4");
                result.put("info", "手机号已存在");
                return result;
            }
            String userName = Utils.isEmpty(sessionCargo.getUserName()) ? sessionCargo.getMobile() : sessionCargo.getUserName();
            PlUsersCargo loginCargo = plusersCargoService.login(userName, MD5.encrypt(passwd));
            if (null == loginCargo) {
                result.put("errorcode", "2");
                result.put("info", "密码错误");
                return result;
            }
            if (mobile.trim().equals(loginCargo.getMobile().trim())) {
                result.put("errorcode", "5");
                result.put("info", "新手机号和原手机号相同");
                return result;
            }

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

            // 更新短信验证码状态
            smsAuth.setStatus(true);
            smsAuthenticationService.save(smsAuth);

            // 更新货主信息、更新SESSION中的货主对象
            loginCargo.setMobile(mobile);
            plusersCargoService.save(loginCargo);
            request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, loginCargo);

            result.put("errorcode", "0");
            result.put("info", "手机号修改成功");
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } catch (Exception e) {
            result.put("errorcode", "7");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The modifyMobile() method invocation Exception.", e);
            throw new RuntimeException("The modifyMobile() method invocation fail.");
        }
    }

    /**
     * 取消叫车
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/cancelvoice")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> cancelVoice(@RequestParam(value = "order_id", required = false, defaultValue = "0")
    Long orderId) {
        try {
            BkVoiceOrder order = bkVoiceOrderService.findOne(orderId);
            if (order == null) {
                result.put("errorcode", "2");
                result.put("info", "叫车信息不存在");
                return result;
            }
            BkBooking booking = bkBookingService.findByVoiceId(orderId);
            if (booking != null) {
                result.put("errorcode", "3");
                result.put("info", "取消找车失败");
                return result;
            }
            order.setIsCancel(true);
            bkVoiceOrderService.save(order);

            // 优惠券重新置为未使用
            RetResult re = creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(order.getTicketNo(), order.getUserCargoId(), UserType.cargo,
                    ConstantUtil.TICKET_STATUS_UNUSED_0, true);
            LOG.info("RetResult:" + JsonUtils.object2Json(re));
            LOG.info(order.getTicketNo() + "  优惠券置为未使用！");

            List<BkOrderOffer> offers = bkOrderOfferService.getByOrderId(orderId, "orderId");
            if (null != offers && offers.size() > 0) {
                List<String> mobiles = new ArrayList<String>();
                for (BkOrderOffer offer : offers) {
                    mobiles.add(offer.getVeMobile());
                }
                Map<String, String> pushMap = new HashMap<String, String>();
                pushMap.put("type", "8");
                pushMap.put("msg", "货主已取消找车【闪发车】");
                xmppService.push(mobiles, JsonUtils.object2Json(pushMap), "v_");
            }
            result.put("errorcode", "0");
            result.put("info", "取消成功");
            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    /**
     * 收藏列表
     * 
     * @param pageNo
     * @param size
     * @return
     */
    @ResponseBody
    @RequestMapping("/collectionlist")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> collectionList(@RequestParam(value = "pageno", required = false, defaultValue = "0")
    int pageNo, @RequestParam(value = "limit", required = false, defaultValue = "10")
    int size) {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();

        // 从DB里面取数据的这段代码写的比较恶心，量小的时候先顶着用。
        Long start = System.currentTimeMillis();
        PlUsersCargo sessionCargo = (PlUsersCargo) obj;
        Page<UsersCargoFavorite> favs = favoriteService.findByUserCargoId(sessionCargo.getUserCargoId(), pageNo, size);
        if (null != favs.getContent() && favs.getContent().size() > 0) {
            Map<Long, VeVehicleInfo> infos = veVehicleInfoService.findByVehicleInfoId(favs.getContent());
            Map<Long, PlUsers> users = plUsersService.findByIds(new ArrayList<VeVehicleInfo>(infos.values()));
            for (UsersCargoFavorite fav : favs) {
                Map<String, Object> reMap = new HashMap<String, Object>();
                VeVehicleInfo info = infos.get(fav.getVehicleInfoId());

                PlUsers user = users.get(info.getUserId());

                if (!StringUtils.isEmpty(info.getCargoTypeList())) {
                    String[] specialtys = info.getCargoTypeList().split(",");
                    List<String> specialtysList = new ArrayList<String>();
                    for (String spec : specialtys) {
                        specialtysList.add(Common.getCarAdeptCargo(spec));
                    }
                    reMap.put("cargo_type_list", Utils.listToString(specialtysList));
                }
                reMap.put("vehicle_id", null != info.getVehicleInfoId() ? info.getVehicleInfoId() + "" : "");
                reMap.put("cartype_id", null != info.getModelId() ? info.getModelId() + "" : "");
                reMap.put("image_url", Constant.IMG_URL_PRE + info.getVeHeadPath() + info.getVeHeadFile());
                reMap.put("star_level", Common.getCarCommentGrade(info.getVeLevel()));
                reMap.put("operunit", null != user.getUserType() ? user.getUserType() + "" : "");
                reMap.put("longtutide", null != info.getVeLastLongitude() ? info.getVeLastLongitude() + "" : "");
                reMap.put("latitude", null != info.getVeLastLatitude() ? info.getVeLastLatitude() + "" : "");
                reMap.put("starting_price", info.getStartingPrice() == null ? "" : info.getStartingPrice() + "");
                reMap.put("starting_mileage", info.getStartingMileage() == null ? "" : info.getStartingMileage() + "");
                reMap.put("mileage_price", info.getMileagePrice() == null ? "" : info.getMileagePrice() + "");
                
                // 2014/9/23 增加金牌车主、实名认证、是否提供敢用敢赔服务返回数据
                if (info.getOwnerLevel() != null && info.getOwnerLevel() > 0) {
                	if(info.getOwnerLevel() == 1001L){
                		reMap.put("owner_level", "1");
                	}else {
                		reMap.put("owner_level", "0");
                	}
                	
                    if (info.getOwnerLevel() == 1001L) {
                    	reMap.put("use_compensation", "1");
                    } else
                    	reMap.put("use_compensation", "1");
                } else {
                	reMap.put("owner_level", "0");
                	reMap.put("use_compensation", "0");
                }

                reMap.put("identity_auth", null != info.getIdentityAuth() && info.getIdentityAuth() ? "1" : "0");
                reMap.put("owner_name", user.getUserName());
                
                
                
                reList.add(reMap);
            }
            
            result.put("errorcode", "0");
            result.put("info", "");
            result.put("total_page", favs.getTotalPages() + "");
            result.put("data", reList);
//            LOG.info("result" + JsonUtils.object2Json(result));
            return result;
        } else {
            if (favs.getTotalPages() - 1 <= pageNo) {
                if (pageNo == 0) {
                    result.put("info", "您尚未收藏车辆");
                    result.put("errorcode", "0");

                } else {
                    result.put("errorcode", "0");
                    result.put("info", "没有更多记录");
                    result.put("total_page", favs.getTotalPages() + "");
                }
            }
            result.put("total_page", favs.getTotalPages() + "");
        }
        
        LOG.info("收藏列表 耗时："+(System.currentTimeMillis() - start)+"毫秒");
        
        return result;
    }

    /**
     * 添加收藏
     * 
     * @param vehicleId
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/addcollection")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> addCollection(@RequestParam(value = "vehicle_id", required = false, defaultValue = "0")
    Long vehicleId) {
        if (vehicleId == 0) {
            result.put("errorcode", "6");
            result.put("info", "收藏失败");
            return result;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo sessionCargo = (PlUsersCargo) obj;
        VeVehicleInfo info = veVehicleInfoService.findOne(vehicleId);
        if (null == info || info.getVeAuthorise() != 0) {
            result.put("errorcode", "2");
            result.put("info", "收藏车辆不存在");
            return result;
        }
        int favCount = favoriteService.getCountByCargoIdAndVehicleInfoId(sessionCargo.getUserCargoId(), info.getVehicleInfoId());
        if (favCount > 0) {
            result.put("errorcode", "2");
            result.put("info", "车辆已经被收藏");
            return result;
        }

        try {
            UsersCargoFavorite fav = new UsersCargoFavorite();
            fav.setUserCargoId(sessionCargo.getUserCargoId());
            fav.setVehicleInfoId(info.getVehicleInfoId());
            fav.setCollectTime(new Date());
            UsersCargoFavorite returnfav = favoriteService.save(fav);
            if (returnfav == null) {
                result.put("errorcode", "5");
                result.put("info", "收藏失败");
                return result;
            } else {
                result.put("errorcode", "0");
                result.put("info", "收藏成功");
                return result;
            }
        } catch (Exception e) {
            result.put("errorcode", "5");
            result.put("info", "服务器忙晕啦，请稍候再试");
            LOG.error("The addCollection() method invocation Exception.", e);
            throw new RuntimeException("The addCollection() method invocation fail.");
        }
    }

    /**
     * 删除收藏
     * 
     * @param vehicleId
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/delcollection")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> delCollection(@RequestParam(value = "vehicle_id", required = false, defaultValue = "0")
    Long vehicleId) {
        if (vehicleId == 0) {
            result.put("errorcode", "2");
            result.put("info", "收藏车辆不存在");
            return result;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo sessionCargo = (PlUsersCargo) obj;
        try {
            UsersCargoFavorite fav = favoriteService.findByUserCargoIdAndVehicleInfoId(sessionCargo.getUserCargoId(), vehicleId);
            if (null != fav) {
                favoriteService.delete(fav.getFavoriteId());
                result.put("errorcode", "0");
                result.put("info", "取消成功");
                return result;
            } else {
                result.put("errorcode", "2");
                result.put("info", "收藏车辆不存在");
                return result;
            }
        } catch (Exception e) {
            result.put("errorcode", "3");
            result.put("info", "取消失败");
            LOG.error("The delCollection() method invocation Exception.", e);
            throw new RuntimeException("The delCollection() method invocation fail.");
        }
    }

    /**
     * 验证码校验
     * 
     * @param identifyCode
     * @param phoneNumber
     * @param lng
     * @param lat
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkidentifycode")
    public Map<String, Object> checkIdentifyCode(@RequestParam(value = "identifycode", required = false, defaultValue = "")
    String identifyCode, @RequestParam(value = "phonenumber", required = false, defaultValue = "")
    String phoneNumber, @RequestParam(value = "longitude", required = false, defaultValue = "")
    String lng, @RequestParam(value = "latitude", required = false, defaultValue = "")
    String lat, @RequestParam(value = "token", required = false, defaultValue = "")
    String token) throws Exception {
        String mk5Key = MD5.encrypt(Constant.SFC_KEY + identifyCode + phoneNumber + lng + lat);
        if (!token.equals(mk5Key)) {
            result.put("errorcode", "6");
            result.put("info", "令牌错误");
            return result;
        }
        if (!Utils.checkMobile(phoneNumber)) {
            result.put("errorcode", "2");
            result.put("info", "手机号码错误");
            return result;
        }
        if (Utils.isEmpty(identifyCode)) {
            result.put("errorcode", "3");
            result.put("info", "验证码不能为空");
            return result;
        }

        SmsAuthentication smsAuth = smsAuthenticationService.findByMobileAndStatus(phoneNumber, false);
        if (null == smsAuth || !smsAuth.getAuthCode().equals(identifyCode)) {
            result.put("errorcode", "4");
            result.put("info", "验证码错误");
            return result;
        }
        Long expire = System.currentTimeMillis() - smsAuth.getSendDt().getTime();
        if (expire > 600000) {
            result.put("errorcode", "4");
            result.put("info", "验证码已过期");
            return result;
        }
        if (smsAuth.getUserType() == 1) {
            smsAuth.setStatus(true);
            smsAuthenticationService.save(smsAuth);
            result.put("errorcode", "0");
            result.put("info", "成功");
            return result;
        } else if (smsAuth.getUserType() == 2) {
            List<PlUsers> plusers = plUsersService.findByMobileAndIsvalid(phoneNumber, "Y");
            if (plusers.size() > 0) {
                PlUsers user = plusers.get(0);
                List<VeVehicleInfo> infos = veVehicleInfoService.findVehicleByUserId(user.getUserId());
                if (infos.size() > 0) {
                    boolean visible = false; // 默认为不显示
                    if (infos.get(0).getVeAuthorise() == 0) {
                        visible = true;
                    }

                    // 更新搜索引擎
                    searchEngineService.updateVehicleVisible(infos.get(0).getVehicleInfoId(), visible);

                    // XMPP注册，临时手机号与用户ＩＤ都注册，以后只使用用户ＩＤ号
                    xmppService.register("v_" + phoneNumber);
                    xmppService.register("v_" + user.getUserId());

                    user.setLoginCnt((user.getLoginCnt() == null ?0:user.getLoginCnt()) + 1);
                    plUsersService.save(user);

                    List<VeVehicleInfo> sinfos = veVehicleInfoService.findVehicleByUserId(user.getUserId());

                    // 将车主对象存入SESSION
                    String oldSessionId = null;
                    if (null != request.getSession().getAttribute(Constant.SESSION_PL_USER))
                        oldSessionId = request.getSession().getId();
                    Map<String, String> map = new HashMap<String, String>();
                    if (null != sinfos && sinfos.size() > 0)
                        map.put(Constant.SESSION_VE_VEHICLE_ID, sinfos.get(0).getVehicleInfoId().toString());
                    map.put(Constant.SESSION_PL_USER, JsonUtils.object2Json(user));
                    String sessid = SessionService.getInstance().createSession(user.getUserId() + "", SessionService.USER_TYPE_DRIVE, map, oldSessionId);
                    if (!Utils.isEmpty(sessid))
                        setValueToCookie(Constant.SESSION_NAME, SecurityUtil.encrypt(sessid));

                    smsAuth.setStatus(true);
                    smsAuthenticationService.save(smsAuth);

                    result.put("errorcode", "0");
                    result.put("info", "成功");
                    return result;

                } else {
                    result.put("errorcode", "4");
                    result.put("info", "您尚未加入闪发车");
                    return result;
                }
            } else {
                result.put("errorcode", "4");
                result.put("info", "您尚未加入闪发车");
                return result;
            }
        } else {
            result.put("errorcode", "5");
            result.put("info", "服务器忙晕啦，请稍候再试");
            return result;
        }
    }

    /**
     * 获取验证码
     * 
     * @param phoneNumber
     * @param token
     * @param userType
     * @return
     */
    @ResponseBody
    @RequestMapping("/registerphonenumber")
    public Map<String, Object> registerPhoneNumber(@RequestParam(value = "phonenumber", required = false, defaultValue = "")
    String phoneNumber, @RequestParam(value = "token", required = false, defaultValue = "")
    String token, @RequestParam(value = "usertype", required = false, defaultValue = "0")
    Long userType) {

        if (userType < 1 || userType > 3) {
            result.put("errorcode", "3");
            result.put("info", "获取失败");
            return result;
        }
        if (!Utils.checkMobile(phoneNumber)) {
            result.put("errorcode", "2");
            result.put("info", "手机号码错误");
            return result;
        }
        String mk5Key = MD5.encrypt(Constant.SFC_KEY + phoneNumber + userType);
        if (!token.equalsIgnoreCase(mk5Key)) {
            result.put("errorcode", "10");
            result.put("info", "令牌错误");
            return result;
        }

        String auth = (int) (Math.random() * 89999 + 10000) + "";
        String code = procedureService.smsAuthAdd(phoneNumber, auth, userType);
        LOG.info("SMS CODE:" + auth);

        if (!Utils.isEmpty(code) && (code.equals("0") || code.equals("2"))) {
            // 发短信
            String msg = "验证码" + auth + ",请在600秒内输入(闪发车工作人员不会索取此验证码，切勿告知他人)。若有疑问请致电400-9191-365";
            boolean flag = smsSendService.sendCodeSms(phoneNumber, msg);
            if (flag) {
                result.put("errorcode", "0");
                result.put("info", "获取验证码成功");
            } else {
                result.put("errorcode", "4");
                result.put("info", "验证码获取失败");
            }
            return result;
        } else if (!Utils.isEmpty(code) && code.equals("1")) {
            result.put("errorcode", "7");
            result.put("info", "您尚未加入闪发车");
            return result;
        } else if (!Utils.isEmpty(code) && code.equals("4")) {
            result.put("errorcode", "8");
            result.put("info", "您的加入申请正在审核，请等待");
            return result;
        } else if (!Utils.isEmpty(code) && code.equals("3")) {
            result.put("errorcode", "9");
            result.put("info", "您的申请未通过审核");
            return result;
        } else {
            result.put("errorcode", "5");
            result.put("info", "服务器忙晕啦，请稍候再试");
            return result;
        }
    }

    /**
     * 获取用户信息
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/getusercargoinfo")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> getUsercargoInfo() {
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo sessionCargo = (PlUsersCargo) obj;
        Long type = sessionCargo.getRecommendType();
        Map<String, String> reMap = new HashMap<String, String>();
        reMap.put("user_name", Utils.isEmpty(sessionCargo.getUserName()) ? "" : sessionCargo.getUserName());
        reMap.put("mobile", sessionCargo.getMobile());
        String reMobile = null;
        if (null != sessionCargo.getRecommendId() && sessionCargo.getRecommendId() > 0) {
            if (type == 1) {
                PlUsersCargo cargo = plusersCargoService.findOne(sessionCargo.getRecommendId());
                if (null != cargo)
                    reMobile = cargo.getMobile();
            } else if (type == 2) {
                PlUsers vehicle = plUsersService.findOne(sessionCargo.getRecommendId());
                if (null != vehicle)
                    reMobile = vehicle.getMobile();
            } else if (type == 3) {
                TbStaff staff = tbStaffService.findOne(sessionCargo.getRecommendId());
                if (null != staff)
                    reMobile = staff.getStaffTel();
            } else
                reMobile = "";
        }
        reMap.put("recommend_mobile", null != reMobile ? reMobile : "");

        result.put("errorcode", "0");
        result.put("info", "获取用户信息成功");
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 修改货主用户信息
     * 
     * @param userName
     * @param recommenderMobile
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateusercargoinfo")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> updateusercargoinfo(@RequestParam(value = "username", required = false, defaultValue = "")
    String userName, @RequestParam(value = "recommender", required = false, defaultValue = "")
    String recommenderMobile) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        PlUsersCargo sessionCargo = (PlUsersCargo) obj;
        String recommenderType = null;
        String recommenderId = null;

        if (Utils.isEmpty(userName) && Utils.isEmpty(recommenderMobile)) {
            result.put("errorcode", "6");
            result.put("info", "参数错误,不能为空");
            return result;
        }

        if (!Utils.isEmpty(userName)) { // 用户名不为空，修改用户名
            userName = userName.replaceAll(" ", "");
            if (!Utils.checkUserName(userName)) {
                result.put("errorcode", "2");
                result.put("info", "用户名不合法");
                return result;
            }
            if (plusersCargoService.existByUserName(userName)) {
                result.put("errorcode", "5");
                result.put("info", "用户名已存在");
                return result;
            }

            reMap.put("user_name", userName);
            sessionCargo.setUserName(userName);
        }

        // 推荐人手机参数不为空，则按手机号查出对应ID号与TYPE。
        if (!Utils.isEmpty(recommenderMobile)) {

            recommenderMobile = recommenderMobile.replaceAll(" ", "");

            if (recommenderMobile.equals(sessionCargo.getMobile())) {
                result.put("errorcode", "3");
                result.put("info", "抱歉，推荐人不能设置为自己");
                return result;
            }

            if (null != sessionCargo.getRecommendId() && sessionCargo.getRecommendId() > 0) {
                result.put("errorcode", "3");
                result.put("info", "推荐人已设置");
                return result;
            }

            if (Utils.isEmpty(recommenderId)) {
                TbStaff staff = tbStaffService.findByStaffTelAndStatus(recommenderMobile, true);
                if (null == staff && Utils.isNumber(recommenderMobile)) {
                    staff = tbStaffService.findOne(Long.valueOf(recommenderMobile));
                    if (null != staff) {
                        recommenderType = "3";
                        recommenderId = staff.getStaffId() + "";
                    }
                }

            }

            if (Utils.isEmpty(recommenderId)) {
                TbStaff staff = tbStaffService.findByStaffTelAndStatus(recommenderMobile, true);
                if (null != staff) {
                    recommenderType = "3";
                    recommenderId = staff.getStaffId() + "";
                }
            }

            if (Utils.isEmpty(recommenderId)) {
                List<PlUsers> vehicles = plUsersService.findByMobileAndIsvalid(recommenderMobile, "Y");
                if (vehicles.size() > 0) {
                    recommenderType = "2";
                    recommenderId = vehicles.get(0).getUserId() + "";
                }
            }

            if (Utils.isEmpty(recommenderId)) {
                PlUsersCargo reCargo = plusersCargoService.findByMobile(recommenderMobile);
                if (null != reCargo) {
                    recommenderType = "1";
                    recommenderId = reCargo.getUserCargoId() + "";
                }
            }

            if (Utils.isEmpty(recommenderId)) {
                result.put("errorcode", "3");
                result.put("info", "推荐人不存在");
                return result;
            }

            sessionCargo.setRecommendId(Long.valueOf(recommenderId));
            sessionCargo.setRecommendType(Long.valueOf(recommenderType));
        }
        if (!Utils.isEmpty(recommenderType))
            reMap.put("recommend_type", recommenderType);
        if (!Utils.isEmpty(recommenderId))
            reMap.put("recommend_id", recommenderId);

        plusersCargoService.save(sessionCargo);
        request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, sessionCargo);
        String info = "";
        if (!Utils.isEmpty(userName) && !Utils.isEmpty(recommenderMobile))
            info = "修改成功";
        else if (!Utils.isEmpty(recommenderMobile))
            info = "设置推荐人成功";
        else if (!Utils.isEmpty(userName))
            info = "设置用户名成功";

        result.put("errorcode", "0");
        result.put("info", info);
        result.put("data", reMap);
        LOG.info("result" + JsonUtils.object2Json(result));
        return result;
    }

    /**
     * 直接下单 地图下单
     * 
     * @param lng
     * @param lat
     * @param sendTime
     *            偏移量
     * @param fromLng
     * @param fromLat
     * @param toLng
     * @param toLat
     * @param fromAddress
     * @param toAddress
     * @param desc
     * @param goodsLength
     * @param goodsHeight
     * @param goodsWidth
     * @param fromAddressShort
     * @param toAddressShort
     * @param fromToDistance
     * @param photo
     * @param referencePrice
     * @param vehicle_mobile
     * @param use_compensation
     *            是否使用敢用敢赔
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/maporder")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> mapOrder(@RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat, @RequestParam(value = "send_time", required = false, defaultValue = "0")
    Long sendTime, @RequestParam(value = "from_longitude", required = false, defaultValue = "0")
    Double fromLng, @RequestParam(value = "from_latitude", required = false, defaultValue = "0")
    Double fromLat, @RequestParam(value = "to_longitude", required = false, defaultValue = "0")
    Double toLng, @RequestParam(value = "to_latitude", required = false, defaultValue = "0")
    Double toLat, @RequestParam(value = "from_address", required = false, defaultValue = "")
    String fromAddress, @RequestParam(value = "to_address", required = false, defaultValue = "")
    String toAddress, @RequestParam(value = "description", required = false, defaultValue = "")
    String desc, @RequestParam(value = "goods_length", required = false, defaultValue = "")
    String goodsLength, @RequestParam(value = "goods_height", required = false, defaultValue = "")
    String goodsHeight, @RequestParam(value = "goods_width", required = false, defaultValue = "")
    String goodsWidth, @RequestParam(value = "from_short_address", required = false, defaultValue = "")
    String fromAddressShort, @RequestParam(value = "to_short_address", required = false, defaultValue = "")
    String toAddressShort, @RequestParam(value = "from_to_distance", required = false, defaultValue = "")
    String fromToDistance, @RequestParam(value = "goods_photo", required = false)
    MultipartFile photo, @RequestParam(value = "reference_price", required = false, defaultValue = "0")
    Double referencePrice, @RequestParam(value = "vehicle_mobile", required = false, defaultValue = "")
    String vehicle_mobile, @RequestParam(value = "use_ticket", required = false)
    Boolean userTicket, @RequestParam(value = "from_detail_address", required = false, defaultValue = "")
    String fromAddressDetail, @RequestParam(value = "to_detail_address", required = false, defaultValue = "")
    String toAddressDetail) {
        if (sendTime > 0) {
            if (sendTime > 1000000000)
                sendTime = sendTime * 1000;
            else
                sendTime = new Date().getTime() + sendTime * 1000;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);

        if (!Utils.checkMobile(vehicle_mobile)) {
            result.put("errorcode", "3");
            result.put("info", "司机手机号码错误");
            result.put("data", new String[] {});
            return result;
        }
        if (fromLng == 0 || fromLat == 0 || Utils.isEmpty(fromAddress)) {
            result.put("errorcode", "3");
            result.put("info", "出发地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if (toLng == 0 || toLat == 0 || Utils.isEmpty(toAddress)) {
            result.put("errorcode", "4");
            result.put("info", "目的地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if ((fromLng + "").equals(toLng + "") && (fromLat + "").equals(toLat + "")) {
            result.put("errorcode", "5");
            result.put("info", "出发地和目的地经纬度相同，叫车失败");
            result.put("data", new String[] {});
            return result;
        }
        long now = new Date().getTime();
        if (sendTime < now) {
            result.put("errorcode", "6");
            result.put("info", "发货时间不能小于当前时间");
            return result;
        }
        if (sendTime - now > 7776000000L) {
            result.put("errorcode", "6");
            result.put("info", "发货时间不能超过3个月");
            return result;
        }
        // if (Utils.isEmpty(desc)) {
        // result.put("errorcode", "6");
        // result.put("info", "货物描述不能为空");
        // return result;
        // }
        try {
            PlUsersCargo userCargo = (PlUsersCargo) obj;
            // 如果帐号状态被改为不可用，返回错误信息，更新SESSION中USER对象。
            PlUsersCargo user = plusersCargoService.findOne(userCargo.getUserCargoId());
            if (null != user) {
                if (!user.getIsvalid().equalsIgnoreCase("Y")) {
                    request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, user);
                    result.put("errorcode", "7");
                    result.put("info", "帐号不存在");
                    return result;
                }
            } else {
                result.put("errorcode", "7");
                result.put("info", "帐号不存在");
                return result;
            }

            // 如果带有优惠券，查询优惠券状态
            String ticketNo = null;
            if (userTicket != null && userTicket) {
                TbTicketInfo ticket = creditAndTicketManagementService.checkTicketAndUseTicket(userTicket, userCargo.getUserCargoId(), UserType.cargo, TicketType.voucher);
                if (ticket == null) {
                    result.put("errorcode", "100");
                    result.put("info", "无可用优惠券");
                    return result;
                }
                ticketNo = ticket.getTicketNo();
            }

            // 保存直接下单信息
            BkVoiceOrder order = new BkVoiceOrder();
            order.setCargoMobile(userCargo.getMobile());
            order.setUserCargoId(userCargo.getUserCargoId());
            order.setCreateTime(new Date());
            order.setExpireTime(new Date(sendTime));
            order.setLatitude(lat);
            order.setLongitude(lng);
            order.setFromAddress(fromAddress);
            order.setFromLatitude(fromLat);
            order.setFromLongitude(fromLng);
            order.setToAddress(toAddress);
            order.setToLatitude(toLat);
            order.setToLongitude(toLng);
            order.setGoodsHeight(goodsHeight);
            order.setGoodsLength(goodsLength);
            order.setGoodsWidth(goodsWidth);
            order.setDescription(desc);
            order.setFileName("");
            order.setFilePath("");
            order.setFileSize(0L);
            order.setIsCancel(false);

            order.setReceivedVehicle(0L);
            order.setFromShortAddress(fromAddressShort);
            order.setFromDetailAddress(fromAddressDetail);
            order.setToShortAddress(toAddressShort);
            order.setToDetailAddress(toAddressDetail);
            order.setFromToDistance(fromToDistance);
            order.setOrderType(2L); // 0为语音订单，1为文本订单,2直接下单。boolean映射0＝false,1=true。
            order.setReferencePrice(referencePrice);
            order.setTicketNo(ticketNo);
            order.setCompleteOffer(false);
            bkVoiceOrderService.save(order);

            if (null != photo) {
                // 图片文件保存
                String filePhysical = Utils.buildFilePath(Constant.PHOTO_PATH);
                String fileUri = Utils.buildFilePath(Constant.PHOTO_PATH_URI_PRE);
                String randomNum = (int) (Math.random() * 899 + 100) + "";
                String realName = photo.getOriginalFilename();
                String suffixName = ".";
                if (!Utils.isEmpty(realName) && realName.contains(".")) {
                    suffixName += realName.split("\\.")[1];
                }
                String fileName = "order_" + System.currentTimeMillis() + randomNum + suffixName;
                File saveDir = new File(filePhysical);
                File saveFile = new File(filePhysical + fileName);
                if (!saveDir.exists())
                    saveDir.mkdirs();
                photo.transferTo(saveFile);
                if (!saveFile.exists() || saveFile.length() < photo.getSize()) {
                    saveFile.delete();
                    result.put("errorcode", "2");
                    result.put("info", "图片上传失败");
                    return result;
                } else {
                    BkVoicePhoto bkPhoto = new BkVoicePhoto();
                    bkPhoto.setFilePath(fileUri);
                    bkPhoto.setFileName(fileName);
                    bkPhoto.setFileSize(saveFile.length());
                    bkPhoto.setVoiceId(order.getId());
                    bkVoicePhotoService.save(bkPhoto);
                }
            }

            // 保存订单信息（直接下单 直接生成订单）
            List<PlUsers> pluserList = plUsersService.findByMobileAndIsvalid(vehicle_mobile, "Y");
            PlUsers pluser = null;
            VeVehicleInfo vehicle = null;
            if (pluserList != null && pluserList.size() > 0) {
                pluser = pluserList.get(0);
            } else {
                throw new RuntimeException("The mapOrder() method invocation exception.");
            }
            if (pluser != null) {
                List<VeVehicleInfo> vehicleList = veVehicleInfoService.findVehicleByUserId(pluser.getUserId());
                vehicle = vehicleList.get(0);
            } else {
                throw new RuntimeException("The mapOrder() method invocation exception.");
            }
            BkBooking booking = new BkBooking();
            booking.setCargoImei(getImei());
            booking.setVehicleInfoId(vehicle.getVehicleInfoId());
            booking.setUserCargoId(user.getUserCargoId());
            booking.setBookingFromUsersId(user.getMobile());
            booking.setBookingToUsersId(pluser.getUserId());
            booking.setBookingStatus(1L);
            booking.setCreateDt(new Date());
            booking.setVoiceId(order.getId());
            booking.setLat(lat);
            booking.setLng(lng);
            String orderNumberPre = Utils.formatDate(new Date(), "yyyyMMdd");
            long orderNumberTail = (int) (Math.random() * 899999999 + 100000000);
            String orderNumber = orderNumberPre + orderNumberTail;
            booking.setOrderNumber(orderNumber);
            booking.setTicketNo(ticketNo); // 优惠券
            booking.setUseCompensation(null != vehicle.getOwnerLevel() && vehicle.getOwnerLevel() == 1001L ? true : false);
            bkBookingService.save(booking);

            // 保存发货地址
            if (fromLat > 0 && fromLng > 0) {
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(1, userCargo.getUserCargoId(), fromAddress, fromAddressShort, fromAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(fromLat, fromLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(fromLat);
                        add.setLng(fromLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(fromAddressDetail) ? "" : fromAddressDetail);
                    newadd.setAddress(Utils.isEmpty(fromAddress) ? "" : fromAddress);
                    newadd.setAddressShort(Utils.isEmpty(fromAddressShort) ? "" : fromAddressShort);
                    newadd.setLat(fromLat);
                    newadd.setLng(fromLng);
                    newadd.setType(1);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }
            // 保存收货地址
            if (toLat > 0 && toLng > 0) {
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(2, userCargo.getUserCargoId(), toAddress, toAddressShort, toAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(toLat, toLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(toLat);
                        add.setLng(toLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(toAddressDetail) ? "" : toAddressDetail);
                    newadd.setAddress(Utils.isEmpty(toAddress) ? "" : toAddress);
                    newadd.setAddressShort(Utils.isEmpty(toAddressShort) ? "" : toAddressShort);
                    newadd.setLat(toLat);
                    newadd.setLng(toLng);
                    newadd.setType(2);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }

            // 优惠券置为使用中状态
            creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(ticketNo, userCargo.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_USING_1, false);

            String datestr = Utils.formatDate(new Date(), "MM.dd HH:mm");
            // 发短信给车主
            smsSendService.sendSms(vehicle_mobile, "生意来啦，有【闪发车】货主请您运货，请打开闪发车 App，到订单管理里查看." + datestr);

            // 发PUSH 推送给车主

            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("type", "10");
            pushMap.put("order_id", order.getId().toString());
            pushMap.put("booking_id", booking.getBookingNo() + "");

            xmppService.push(vehicle_mobile, JsonUtils.object2Json(pushMap), "v_");

            result.put("errorcode", "0");
            result.put("booking_id", booking.getBookingNo() + "");
            result.put("info", "下单成功,等待司机确认");
            return result;

        } catch (Exception e) {
            LOG.error("The mapOrder() method invocation exception.", e);
            throw new RuntimeException("The mapOrder() method invocation exception.");
        }
    }

    /**
     * 直接下单 地图下单 没有图片文件
     * 
     * @param lng
     * @param lat
     * @param sendTime
     * @param fromLng
     * @param fromLat
     * @param toLng
     * @param toLat
     * @param fromAddress
     * @param toAddress
     * @param desc
     * @param goodsLength
     * @param goodsHeight
     * @param goodsWidth
     * @param fromAddressShort
     * @param toAddressShort
     * @param fromToDistance
     * @param referencePrice
     * @param vehicle_mobile
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/nof/maporder")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> mapOrderNoFile(@RequestParam(value = "longitude", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "latitude", required = false, defaultValue = "0")
    Double lat, @RequestParam(value = "send_time", required = false, defaultValue = "0")
    Long sendTime, @RequestParam(value = "from_longitude", required = false, defaultValue = "0")
    Double fromLng, @RequestParam(value = "from_latitude", required = false, defaultValue = "0")
    Double fromLat, @RequestParam(value = "to_longitude", required = false, defaultValue = "0")
    Double toLng, @RequestParam(value = "to_latitude", required = false, defaultValue = "0")
    Double toLat, @RequestParam(value = "from_address", required = false, defaultValue = "")
    String fromAddress, @RequestParam(value = "to_address", required = false, defaultValue = "")
    String toAddress, @RequestParam(value = "description", required = false, defaultValue = "")
    String desc, @RequestParam(value = "goods_length", required = false, defaultValue = "")
    String goodsLength, @RequestParam(value = "goods_height", required = false, defaultValue = "")
    String goodsHeight, @RequestParam(value = "goods_width", required = false, defaultValue = "")
    String goodsWidth, @RequestParam(value = "from_short_address", required = false, defaultValue = "")
    String fromAddressShort, @RequestParam(value = "to_short_address", required = false, defaultValue = "")
    String toAddressShort, @RequestParam(value = "from_to_distance", required = false, defaultValue = "")
    String fromToDistance, @RequestParam(value = "reference_price", required = false, defaultValue = "0")
    Double referencePrice, @RequestParam(value = "vehicle_mobile", required = false, defaultValue = "")
    String vehicle_mobile, @RequestParam(value = "use_ticket", required = false)
    Boolean userTicket, @RequestParam(value = "from_detail_address", required = false, defaultValue = "")
    String fromAddressDetail, @RequestParam(value = "to_detail_address", required = false, defaultValue = "")
    String toAddressDetail) {
        if (sendTime > 0) {
            if (sendTime > 1000000000)
                sendTime = sendTime * 1000;
            else
                sendTime = new Date().getTime() + sendTime * 1000;
        }
        Object obj = session.getAttribute(Constant.SESSION_PL_USER_CARGO);

        if (!Utils.checkMobile(vehicle_mobile)) {
            result.put("errorcode", "3");
            result.put("info", "司机手机号码错误");
            result.put("data", new String[] {});
            return result;
        }
        if (fromLng == 0 || fromLat == 0 || Utils.isEmpty(fromAddress)) {
            result.put("errorcode", "3");
            result.put("info", "出发地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if (toLng == 0 || toLat == 0 || Utils.isEmpty(toAddress)) {
            result.put("errorcode", "4");
            result.put("info", "目的地不能为空");
            result.put("data", new String[] {});
            return result;
        }
        if ((fromLng + "").equals(toLng + "") && (fromLat + "").equals(toLat + "")) {
            result.put("errorcode", "5");
            result.put("info", "出发地和目的地经纬度相同，叫车失败");
            result.put("data", new String[] {});
            return result;
        }
        long now = new Date().getTime();
        if (sendTime < now) {
            result.put("errorcode", "6");
            result.put("info", "发货时间不能小于当前时间");
            return result;
        }
        if (sendTime - now > 7776000000L) {
            result.put("errorcode", "6");
            result.put("info", "发货时间不能超过3个月");
            return result;
        }
        // if (Utils.isEmpty(desc)) {
        // result.put("errorcode", "6");
        // result.put("info", "货物描述不能为空");
        // return result;
        // }
        try {
            PlUsersCargo userCargo = (PlUsersCargo) obj;
            // 如果帐号状态被改为不可用，返回错误信息，更新SESSION中USER对象。
            PlUsersCargo user = plusersCargoService.findOne(userCargo.getUserCargoId());
            if (null != user) {
                if (!user.getIsvalid().equalsIgnoreCase("Y")) {
                    request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, user);
                    result.put("errorcode", "7");
                    result.put("info", "帐号不存在");
                    return result;
                }
            } else {
                result.put("errorcode", "7");
                result.put("info", "帐号不存在");
                return result;
            }

            // 如果带有优惠券，查询优惠券状态
            String ticketNo = null;
            if (userTicket != null && userTicket) {
                TbTicketInfo ticket = creditAndTicketManagementService.checkTicketAndUseTicket(userTicket, userCargo.getUserCargoId(), UserType.cargo, TicketType.voucher);
                if (ticket == null) {
                    result.put("errorcode", "100");
                    result.put("info", "无可用优惠券");
                    return result;
                }

                ticketNo = ticket.getTicketNo();
            }

            // 保存直接下单信息
            BkVoiceOrder order = new BkVoiceOrder();
            order.setCargoMobile(userCargo.getMobile());
            order.setUserCargoId(userCargo.getUserCargoId());
            order.setCreateTime(new Date());
            order.setExpireTime(new Date(sendTime));
            order.setLatitude(lat);
            order.setLongitude(lng);
            order.setFromAddress(fromAddress);
            order.setFromLatitude(fromLat);
            order.setFromLongitude(fromLng);
            order.setToAddress(toAddress);
            order.setToLatitude(toLat);
            order.setToLongitude(toLng);
            order.setGoodsHeight(goodsHeight);
            order.setGoodsLength(goodsLength);
            order.setGoodsWidth(goodsWidth);
            order.setDescription(desc);
            order.setFileName("");
            order.setFilePath("");
            order.setFileSize(0L);
            order.setIsCancel(false);
            order.setReceivedVehicle(0L);
            order.setFromShortAddress(fromAddressShort);
            order.setFromDetailAddress(fromAddressDetail);
            order.setToShortAddress(toAddressShort);
            order.setToDetailAddress(toAddressDetail);
            order.setFromToDistance(fromToDistance);
            order.setOrderType(2L); // 0为语音订单，1为文本订单,2直接下单。boolean映射0＝false,1=true。
            order.setReferencePrice(referencePrice);
            order.setIsBooking(true);
            order.setTicketNo(ticketNo);
            order.setCompleteOffer(false);
            bkVoiceOrderService.save(order);

            // 保存订单信息
            List<PlUsers> pluserList = plUsersService.findByMobileAndIsvalid(vehicle_mobile, "Y");
            PlUsers pluser = null;
            VeVehicleInfo vehicle = null;
            if (pluserList != null && pluserList.size() > 0) {
                pluser = pluserList.get(0);
            } else {
                throw new RuntimeException("The mapOrder() method invocation exception.");
            }
            if (pluser != null) {
                List<VeVehicleInfo> vehicleList = veVehicleInfoService.findVehicleByUserId(pluser.getUserId());
                vehicle = vehicleList.get(0);
            } else {
                throw new RuntimeException("The mapOrder() method invocation exception.");
            }
            BkBooking booking = new BkBooking();
            booking.setCargoImei(getImei());
            booking.setVehicleInfoId(vehicle.getVehicleInfoId());
            booking.setUserCargoId(user.getUserCargoId());
            booking.setBookingFromUsersId(user.getMobile());
            booking.setBookingToUsersId(pluser.getUserId());
            booking.setBookingStatus(1L);
            booking.setCreateDt(new Date());
            booking.setVoiceId(order.getId());
            booking.setLat(lat);
            booking.setLng(lng);
            String orderNumberPre = Utils.formatDate(new Date(), "yyyyMMdd");
            long orderNumberTail = (int) (Math.random() * 899999999 + 100000000);
            String orderNumber = orderNumberPre + orderNumberTail;
            booking.setOrderNumber(orderNumber);
            booking.setTicketNo(ticketNo);
            booking.setUseCompensation(null != vehicle.getOwnerLevel() && vehicle.getOwnerLevel() == 1001l ? true : false);
            bkBookingService.save(booking);

            // 保存发货地址
            if (fromLat > 0 && fromLng > 0) {
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(1, userCargo.getUserCargoId(), fromAddress, fromAddressShort, fromAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(fromLat, fromLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(fromLat);
                        add.setLng(fromLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(fromAddressDetail) ? "" : fromAddressDetail);
                    newadd.setAddress(Utils.isEmpty(fromAddress) ? "" : fromAddress);
                    newadd.setAddressShort(Utils.isEmpty(fromAddressShort) ? "" : fromAddressShort);
                    newadd.setLat(fromLat);
                    newadd.setLng(fromLng);
                    newadd.setType(1);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }
            // 保存收货地址
            if (toLat > 0 && toLng > 0) {
                CargoAddress add = cargoAddressService.findByTypeAndCargoIdAndAddress(2, userCargo.getUserCargoId(), toAddress, toAddressShort, toAddressDetail);
                if (null != add) {
                    int count = add.getUseCount();
                    add.setUseCount(count + 1);
                    Double distance = Utils.GetDistance(toLat, toLng, add.getLat(), add.getLng());
                    if (distance > 500) {
                        add.setLat(toLat);
                        add.setLng(toLng);
                    }
                    cargoAddressService.save(add);
                } else {
                    CargoAddress newadd = new CargoAddress();
                    newadd.setAddressDetail(Utils.isEmpty(toAddressDetail) ? "" : toAddressDetail);
                    newadd.setAddress(Utils.isEmpty(toAddress) ? "" : toAddress);
                    newadd.setAddressShort(Utils.isEmpty(toAddressShort) ? "" : toAddressShort);
                    newadd.setLat(toLat);
                    newadd.setLng(toLng);
                    newadd.setType(2);
                    newadd.setCargoId(userCargo.getUserCargoId());
                    newadd.setUseCount(1);
                    cargoAddressService.save(newadd);
                }
            }

            // 优惠券置为使用中状态
            creditAndTicketManagementService.updateTicketInfoStatusByTicketNo(ticketNo, userCargo.getUserCargoId(), UserType.cargo, ConstantUtil.TICKET_STATUS_USING_1, false);

            String datestr = Utils.formatDate(new Date(), "MM.dd HH:mm");

            // 发短信给车主
            smsSendService.sendSms(vehicle_mobile, "生意来啦，有【闪发车】货主请您运货，请打开闪发车 App，到订单管理里查看," + datestr);

            // 发PUSH

            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("type", "10");
            pushMap.put("order_id", order.getId().toString());
            pushMap.put("booking_id", booking.getBookingNo() + "");

            xmppService.push(vehicle_mobile, JsonUtils.object2Json(pushMap), "v_");

            result.put("errorcode", "0");
            result.put("booking_id", booking.getBookingNo() + "");
            result.put("info", "下单成功,等待司机确认");
            return result;

        } catch (Exception e) {
            LOG.error("The mapOrder() method invocation exception.", e);
            throw new RuntimeException("The mapOrder() method invocation exception.");
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
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public String uploadbl(@RequestParam(value = "jsoninfo", required = false, defaultValue = "")
    String jsoninfo) {

        PlUsersCargo plusers = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        try {

            Set<String> contactSet = contactsService.findByUserIdAndType(plusers.getUserCargoId(), com.store.api.mongo.entity.enumeration.UserType.cargo);
            List<Map<String, Object>> list = null;
            if (!Utils.isEmpty(jsoninfo)) {
                list = (List<Map<String, Object>>) JsonUtils.json2Object(jsoninfo, List.class);

                if (list != null && list.size() > 0) {
                    // 客户端上传的货主通讯录
                    List<Contact> contactList = new ArrayList<Contact>();
                    Contact contact = null;
                    for (Map<String, Object> map : list) {
                        contact = new Contact();
                        if (null != map.get("m") && null != map.get("n") && !contactSet.contains(map.get("m").toString())) {
                            contact.setMobile(map.get("m").toString());
                            contact.setName(Utils.isChinessNumEnglish(map.get("n").toString()));
                            contact.setUserType(com.store.api.mongo.entity.enumeration.UserType.cargo); // 货主
                            contact.setUserId(plusers.getUserCargoId());
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
            staffMobileSet.addAll(cargoMobileSet);
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

    /**
     * 查询常用地址
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/getcargoaddress")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> getCargoAddress() {
        PlUsersCargo cargo = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        Page<CargoAddress> fromadds = cargoAddressService.findByCargoId(1, cargo.getUserCargoId());
        Page<CargoAddress> toadds = cargoAddressService.findByCargoId(2, cargo.getUserCargoId());
        List<Map<String, String>> fromlist = new ArrayList<Map<String, String>>();
        List<Map<String, String>> tolist = new ArrayList<Map<String, String>>();
        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
        for (CargoAddress address : fromadds.getContent()) {
            Map<String, String> reMap = new HashMap<String, String>();
            reMap.put("lng", address.getLng() + "");
            reMap.put("lat", address.getLat() + "");
            reMap.put("address", !Utils.isEmpty(address.getAddress()) ? address.getAddress() : "");
            reMap.put("address_detail", !Utils.isEmpty(address.getAddressDetail()) ? address.getAddressDetail() : "");
            reMap.put("address_short", !Utils.isEmpty(address.getAddressShort()) ? address.getAddressShort() : "");
            reMap.put("count", address.getUseCount() + "");
            fromlist.add(reMap);
        }
        for (CargoAddress address : toadds.getContent()) {
            Map<String, String> reMap = new HashMap<String, String>();
            reMap.put("lng", address.getLng() + "");
            reMap.put("lat", address.getLat() + "");
            reMap.put("address", !Utils.isEmpty(address.getAddress()) ? address.getAddress() : "");
            reMap.put("address_detail", !Utils.isEmpty(address.getAddressDetail()) ? address.getAddressDetail() : "");
            reMap.put("address_short", !Utils.isEmpty(address.getAddressShort()) ? address.getAddressShort() : "");
            reMap.put("count", address.getUseCount() + "");
            tolist.add(reMap);
        }
        map.put("from", fromlist);
        map.put("to", tolist);
        result.put("errorcode", "0");
        result.put("info", "");
        result.put("data", map);
        return result;
    }

    /**
     * 修改常用地址
     * 
     * @param type
     *            1:发货地址 2：收货地址
     * @param oper
     *            1:更新 2：删除
     * @param lng
     *            经度
     * @param lat
     *            纬度
     * @param address
     *            地址
     * @param addressDetail
     *            详细地址
     * @return
     */
    @ResponseBody
    @RequestMapping("/updatecargoaddress")
    @Authorization(type = Constant.SESSION_PL_USER_CARGO)
    public Map<String, Object> updateCargoAddress(@RequestParam(value = "type", required = false, defaultValue = "0")
    int type, @RequestParam(value = "oper", required = false, defaultValue = "0")
    int oper, @RequestParam(value = "id", required = false, defaultValue = "0")
    Long id, @RequestParam(value = "lng", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "lat", required = false, defaultValue = "0")
    Double lat, @RequestParam(value = "address", required = false, defaultValue = "")
    String address, @RequestParam(value = "address_detail", required = false, defaultValue = "")
    String addressDetail, @RequestParam(value = "address_short", required = false, defaultValue = "")
    String addressShort) {
        PlUsersCargo cargo = (PlUsersCargo) session.getAttribute(Constant.SESSION_PL_USER_CARGO);
        if (!(lng > 0 && lat > 0) || !(type == 1 || type == 2)) {
            result.put("errorcode", "2");
            result.put("info", "参数错误");
            return result;
        }
        if (Utils.isEmpty(address) && Utils.isEmpty(addressDetail) && Utils.isEmpty(addressShort)) {
            result.put("errorcode", "3");
            result.put("info", "地址参数错误");
            return result;
        }
        // 更新操作
        if (oper == 1) {
            CargoAddress ca = cargoAddressService.findByTypeAndCargoIdAndAddress(type, cargo.getUserCargoId(), address, addressShort, addressDetail);
            if (null != ca) {
                ca.setAddressDetail(Utils.isEmpty(addressDetail) ? "" : addressDetail);
                ca.setAddress(Utils.isEmpty(address) ? "" : address);
                ca.setAddressShort(Utils.isEmpty(addressShort) ? "" : addressShort);
                ca.setLat(lat);
                ca.setLng(lng);
                cargoAddressService.save(ca);
            } else {
                CargoAddress newca = new CargoAddress();
                newca.setAddressDetail(addressDetail);
                newca.setAddress(address);
                newca.setAddressShort(addressShort);
                newca.setLat(lat);
                newca.setLng(lng);
                newca.setType(type);
                newca.setCargoId(cargo.getUserCargoId());
                newca.setUseCount(1);
                cargoAddressService.save(newca);
            }
            result.put("errorcode", "0");
            result.put("info", "");
            return result;
        } else if (oper == 2) {// 删除
            CargoAddress ca = cargoAddressService.findByTypeAndCargoIdAndAddress(type, cargo.getUserCargoId(), address, addressShort, addressDetail);
            if (null != ca) {
                cargoAddressService.remove(ca.getId());
            }
            result.put("errorcode", "0");
            result.put("info", "");
            return result;
        } else {
            result.put("errorcode", "4");
            result.put("info", "参数错误");
            return result;
        }
    }

}
