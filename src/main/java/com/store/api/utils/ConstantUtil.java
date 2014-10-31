package com.store.api.utils;

/**
 * 常量工具类
 * 
 * @author weiwei
 * @Date 2014-7-24
 */
public class ConstantUtil {

	public final static String REBATE_VOUCHERS = "返利券";
	
	/** -1:券未激活 **/
	public final static Long TICKET_STATUS_INACTIVE_NEGATIVE_1 = -1L;

	/** 0:券未使用 **/
	public final static Long TICKET_STATUS_UNUSED_0 = 0L;

	/** 1:券使用中 **/
	public final static Long TICKET_STATUS_USING_1 = 1L;

	/** 2:券已使用 **/
	public final static Long TICKET_STATUS_USED_2 = 2L;

	/** 3:券已过期 **/
	public final static Long TICKET_STATUS_EXPIRED_3 = 3L;

	/** 受邀人积分数 100 **/
	public final static Long INVITEE_POINT = 100L;

	/** 受邀人积分数常量，数据库中的key值 **/
	public final static String INVITEE_POINT_CONSTANT = "invitee_point";

	/** 邀请人积分数 500 **/
	public final static Long INVITER_POINT = 500L;

	/** 邀请人积分数常量，数据库中的key值 **/
	public final static String INVITER_POINT_CONSTANT = "inviter_point";

	/** 券有效时间长度，单位是天，即XX天之后过期 16 **/
	public final static Long VOUCHER_VALID = 16L;

	/** 券有效时间长度，单位是天，即XX天之后过期 常量，数据库中的key值 **/
	public final static String VOUCHER_VALID_CONSTANT = "voucher_valid";

	/** 积分兑换券比例 100 **/
	public final static Long EXCHANGE_RATIO = 100L;

	/** 积分兑换券比例常量，数据库中的key值 **/
	public final static String EXCHANGE_RATIO_CONSTANT = "exchange_ratio";
	
	/** 积分兑换钱比例 100 **/
	public final static Long EXCHANGE_RATIO_MONEY = 100L;
	
	/** 积分兑换钱比例常量，数据库中的key值 **/
	public final static String EXCHANGE_RATIO_MONEY_CONSTANT = "exchange_ratio_money";

	/** 券的额度，单位是元 10 **/
	public final static Long TICKET_AMOUNT = 10L;

	/** 券的额度，单位是元常量，数据库中的key值 **/
	public final static String TICKET_AMOUNT_CONSTANT = "ticket_amount";
	
	/** 券的额度，单位是元 1 **/
	public final static Long MONEY_AMOUNT = 1L;

	/** 券的额度，单位是元常量，数据库中的key值 **/
	public final static String MONEY_AMOUNT_CONSTANT = "money_amount";

	/** 每次充值额度 50 **/
	public final static Long RECHARGE_RATIO = 50L;

	/** 每次充值额度常量，数据库中的key值 **/
	public final static String RECHARGE_RATIO_CONSTANT = "recharge_ratio";

	/** 送券的数量 3 **/
	public final static Integer NUMBER_OF_CREDIT = 3;
	
	/** 送券的数量的常量 **/
	public final static String NUMBER_OF_CREDIT_CONSTANT= "send_ticket_number";

	/** 下单送积分 100 **/
	public final static Long SEND_INTEGRAL = 100L;

	/** 下单送积分常量 **/
	public final static String SEND_INTEGRAL_CONSTANT = "send_integral";

	/** 下单给邀请人送积分 20 **/
	public final static Long SEND_INTEGRAL_TO_INVITER = 100L;

	/** 下单给邀请人送积分常量 **/
	public final static String SEND_INTEGRAL_TO_INVITER_CONSTANT = "send_integral_to_inviter";

	/** 邀请人积分数 100 车主  **/
	public final static Long INVITER_POINT_DRIVER = 100L;

	/** 邀请人积分数常量，数据库中的key值 车主 **/
	public final static String INVITER_POINT_DRIVER_CONSTANT = "inviter_point_dirver";
	
	/** 活动起始时间常量，数据库中的key值 **/
	public final static String ACTIVITY_START_TIME_CONSTANT = "activity_start_time";
	
	/** 活动结束时间常量，数据库中的key值 **/
	public final static String ACTIVITY_END_TIME_CONSTANT = "activity_end_time";
	
	/** 活动起始时间-积分常量，数据库中的key值 **/
	public final static String ACTIVITY_START_TIME_CREDIT_CONSTANT = "activity_start_time_credit";
	
	/** 活动结束时间-积分常量，数据库中的key值 **/
	public final static String ACTIVITY_END_TIME_CREDIT_CONSTANT = "activity_end_time_credit";
	
	
	/** 邀请人积分数(车主邀请车主) **/
	public final static String INVITER_VE_POINT = "inviter_ve_point";
	/** 邀请人送余额（货主邀请车主）**/
	public final static String INVITER_BALANCE  = "inviter_balance";
	/** 下单给车主送积分 **/
	public final static String SEND_VE_INTEGRAL = "send_ve_integral";
	/**车主加入赠送积分**/
	public final static String SEND_VE_FIRST ="send_ve_first";
	/**货主每下一个有效单 送10元**/
	public final static String SEND_MONEY_BOOKING ="send_money_booking";
	/**货主的第一个有效订单 给邀请人送钱**/
	public final static String SEND_MONEY_FIRST_BOOKING ="send_money_first_booking";
	
	public final static String SEND_POINT_BY_CARGO_REGISTER = "send_point_by_cargo_register";
}