package com.store.api.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Common;
import com.store.api.common.Constant;
import com.store.api.mongo.entity.Address;
import com.store.api.mongo.entity.ReleaseVersion;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.entity.vo.AddressBean;
import com.store.api.mongo.service.AddressService;
import com.store.api.mongo.service.ReleaseVersionService;
import com.store.api.mongo.service.UserService;
import com.store.api.session.annotation.Authorization;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

/**
 * 公共方法控制器
 * 
 * @author vincent,2014年11月6日 created it
 */
@Controller()
@Scope("prototype")
@RequestMapping("/public")
public class PublicAction extends BaseAction {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ReleaseVersionService releaseVersionService;

	/**
	 * 用户注册
	 * 
	 * @param name 用户名
	 * @param nickname 昵称
	 * @param pwd 密码(MD5)
	 * @param phone 手机号
	 * @param type 用户类型:1 商户 2 顾客
	 * @param code 推广码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/register", produces = "text/plain;charset=UTF-8")
	public String register(@RequestParam(value = "name", required = false, defaultValue = "") String userName,
			@RequestParam(value = "nickname", required = false, defaultValue = "") String nickName, @RequestParam(value = "pwd", required = false, defaultValue = "") String pwd,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone, @RequestParam(value = "type", required = false, defaultValue = "2") int type,
			@RequestParam(value = "promocode", required = false, defaultValue = "") String code) throws Exception {
		if (Utils.isEmpty(userName) || Utils.isEmpty(pwd)) {
			return JsonUtils.resultJson(-2, "用户名或密码不能为空", null);
		}
		if (userService.findByUserName(userName.trim()) != null)
			return JsonUtils.resultJson(-3, "用户名已经被注册", null);
		if (Utils.isEmpty(nickName)) {
			nickName = userName;
		}

		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = null;
		if (null != obj)
			user = (User) obj;
		else
			user = new User();
		user.setUserName(userName.trim());
		user.setNickName(nickName.trim());
		user.setPwd(pwd.trim());
		user.setPhone(phone.trim());
		user.setPromoCode(code.trim());
		user.setType(type == 1 ? UserType.merchants : UserType.customer);
		user.setImei(getImei());
		user.setRegisterVer(getVersionName());
		user.setCurrVer(getVersionName());

		// 通过IP获取位置信息
		if (Utils.isEmpty(user.getCity()) || user.getCityCode() == 0) {
			String ip = request.getRemoteAddr();
			if (!Utils.isEmpty(ip)) {
				AddressBean addr = Common.ipWithBaidu(ip);
				if (null != addr) {
					user.setCity(addr.getCity());
					user.setProvince(addr.getProvince());
					user.setCityCode(addr.getCityCode());
				}
			}
		}

		userService.save(user);
		session.setAttribute(Constant.SESSION_USER, user);

		if (type == 1) {
			initSession(UserType.merchants, user, false);
		} else {
			initSession(UserType.customer, user, false);
		}
		

		List<Address> addrs = addressService.findByUserId(user.getId());
		List<Map<String, String>> resAddr = new LinkedList<Map<String, String>>();
		if (null != addrs && !addrs.isEmpty()) {
			for (Address addr : addrs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("addr_id", addr.getId() + "");
				map.put("address", addr.getAddress());
				map.put("phone", addr.getPhone());
				map.put("name", addr.getName());
				map.put("lng", addr.getLocation()[0] + "");
				map.put("lat", addr.getLocation()[1] + "");
				map.put("def", addr.getId() == user.getAddressId() ? "1" : "0");
				resAddr.add(map);
			}
		}

		Map<String, Object> veResult = new HashMap<String, Object>();
		veResult.put("user_id", user.getId() + "");
		veResult.put("user_name", userName);
		veResult.put("nick_name", nickName);
		veResult.put("phone", phone);
		veResult.put("user_type", "1");
		if(type==1)
			veResult.put("merc_num", user.getMercNum()+"");
		veResult.put("addrs", resAddr);
		return JsonUtils.resultJson(1, "", veResult);
	}

	/**
	 * 临时用户注册、登录
	 * 
	 * @param uuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/visitorlogin", produces = "text/plain;charset=UTF-8")
	public String visitorLogin(@RequestParam(value = "uuid", required = false, defaultValue = "") String uuid) throws Exception {
		if (Utils.isEmpty(uuid))
			return JsonUtils.resultJson(-2, "注册失败", null);
		User user = userService.findByUuid(uuid);
		if (null == user) {
			user = new User();
			user.setType(UserType.visitor);
			user.setImei(getImei());
			user.setRegisterVer(getVersionName());
			user.setCurrVer(getVersionName());
			user.setUuid(uuid);
			// 通过IP获取位置信息
			String ip = request.getRemoteAddr();
			if (!Utils.isEmpty(ip)) {
				AddressBean addr = Common.ipWithBaidu(ip);
				if (null != addr) {
					user.setCity(addr.getCity());
					user.setProvince(addr.getProvince());
					user.setCityCode(addr.getCityCode());
				}
			}
		} else {
			user.setCurrVer(getVersionName());
		}
		userService.save(user);

		initSession(UserType.visitor, user, false);

		Map<String, Object> veResult = new HashMap<String, Object>();
		veResult.put("user_id", user.getId() + "");
		veResult.put("user_type", "0");
		return JsonUtils.resultJson(1, "", veResult);
	}

	/**
	 * 用户登录
	 * 
	 * @param userName
	 * @param pwd
	 * @param type 用户类型:1 商户 2 顾客
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/login", produces = "text/plain;charset=UTF-8")
	public String login(@RequestParam(value = "name", required = false, defaultValue = "") String userName,
			@RequestParam(value = "pwd", required = false, defaultValue = "") String pwd,
			@RequestParam(value = "type", required = false, defaultValue = "2") int type) throws Exception {
		if (Utils.isEmpty(userName) || Utils.isEmpty(pwd))
			return JsonUtils.resultJson(-2, "用户名或密码不能为空", null);
		User user = userService.findByUserName(userName.trim());
		if (null == user)
			return JsonUtils.resultJson(-3, "用户尚未注册", null);
		if (Utils.isEmpty(user.getPwd()) || !user.getPwd().equalsIgnoreCase(pwd.trim()))
			return JsonUtils.resultJson(-4, "密码错误", null);

		if(type==2){
			if(!user.getType().equals(UserType.customer)){
				return JsonUtils.resultJson(-5, "帐号密码错误", null);
			}
		}
		if(type==1){
			if(!user.getType().equals(UserType.merchants)){
				return JsonUtils.resultJson(-5, "帐号密码错误", null);
			}
		}
		initSession(user.getType(), user, false);

		List<Address> addrs = addressService.findByUserId(user.getId());
		List<Map<String, String>> resAddr = new LinkedList<Map<String, String>>();
		if (null != addrs && !addrs.isEmpty()) {
			for (Address addr : addrs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("addr_id", addr.getId() + "");
				map.put("address", addr.getAddress());
				map.put("phone", addr.getPhone());
				map.put("name", addr.getName());
				map.put("lng", addr.getLocation()[0] + "");
				map.put("lat", addr.getLocation()[1] + "");
				map.put("def", addr.getId() == user.getAddressId() ? "1" : "0");
				resAddr.add(map);
			}
		}

		Map<String, Object> veResult = new HashMap<String, Object>();
		veResult.put("user_id", user.getId() + "");
		veResult.put("user_name", user.getUserName());
		veResult.put("nick_name", user.getNickName());
		veResult.put("phone", user.getPhone());
		veResult.put("user_type", "1");
		if(type==1)
			veResult.put("merc_num", user.getMercNum()+"");
		veResult.put("addrs", resAddr);
		return JsonUtils.resultJson(1, "", veResult);
	}

	/**
	 * 修改帐户信息
	 * 
	 * @param nickName
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/modify", produces = "text/plain;charset=UTF-8")
	@Authorization(type = Constant.SESSION_USER)
	public String modify(@RequestParam(value = "nickname", required = false, defaultValue = "") String nickName,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone) throws Exception {
		if (Utils.isEmpty(nickName) && Utils.isEmpty(phone))
			return JsonUtils.resultJson(-2, "修改字段不能为空", null);

		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = userService.findOne(((User) obj).getId());

		if (!Utils.isEmpty(nickName))
			user.setNickName(nickName.trim());
		if (!Utils.isEmpty(phone))
			user.setPhone(phone);
		userService.save(user);
		session.setAttribute(Constant.SESSION_USER, user);
		return JsonUtils.resultJson(1, "", null);
	}

	/**
	 * 查询用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userinfo", produces = "text/plain;charset=UTF-8")
	@Authorization(type = Constant.SESSION_USER)
	public String userInfo() throws Exception {
		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = userService.findOne(((User) obj).getId());

		List<Address> addrs = addressService.findByUserId(user.getId());
		List<Map<String, String>> resAddr = new LinkedList<Map<String, String>>();
		if (null != addrs && !addrs.isEmpty()) {
			for (Address addr : addrs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("addr_id", addr.getId() + "");
				map.put("address", addr.getAddress());
				map.put("phone", addr.getPhone());
				map.put("name", addr.getName());
				map.put("lng", addr.getLocation()[0] + "");
				map.put("lat", addr.getLocation()[1] + "");
				map.put("def", addr.getId() == user.getAddressId() ? "1" : "0");
				resAddr.add(map);
			}
		}
		
		// 通过IP获取位置信息
				if (Utils.isEmpty(user.getCity()) || user.getCityCode() == 0) {
					String ip = request.getRemoteAddr();
					if (!Utils.isEmpty(ip)) {
						AddressBean addr = Common.ipWithBaidu(ip);
						if (null != addr) {
							user.setCity(addr.getCity());
							user.setProvince(addr.getProvince());
							user.setCityCode(addr.getCityCode());
						}
					}
				}

		Map<String, Object> veResult = new HashMap<String, Object>();
		veResult.put("user_id", user.getId() + "");
		veResult.put("user_name", user.getUserName());
		veResult.put("nick_name", user.getNickName());
		veResult.put("phone", user.getPhone());
		if(user.getType().equals(UserType.visitor))
		    veResult.put("user_type", "0");
		else
		    veResult.put("user_type", "1");
		if(user.getType().equals(UserType.merchants))
			veResult.put("merc_num", user.getMercNum()+"");
		veResult.put("addrs", resAddr);
		return JsonUtils.resultJson(1, "", veResult);
	}

	/**
	 * 用户常用地址列表查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/queryaddress", produces = "text/plain;charset=UTF-8")
	@Authorization(type = Constant.SESSION_USER)
	public String queryAddress() throws Exception {
		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = userService.findOne(((User) obj).getId());

		List<Address> addrs = addressService.findByUserId(user.getId());
		List<Map<String, String>> resAddr = new LinkedList<Map<String, String>>();
		if (null != addrs && !addrs.isEmpty()) {
			for (Address addr : addrs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("addr_id", addr.getId() + "");
				map.put("address", addr.getAddress());
				map.put("phone", addr.getPhone());
				map.put("name", addr.getName());
				map.put("lng", addr.getLocation()[0] + "");
				map.put("lat", addr.getLocation()[1] + "");
				map.put("def", addr.getId() == user.getAddressId() ? "1" : "0");
				resAddr.add(map);
			}
		}
		return JsonUtils.resultJson(1, "", resAddr);
	}

	/**
	 * 新增、编辑常用地址
	 * 
	 * @param addrId 地址ID
	 * @param address 地址详细
	 * @param lat 纬度
	 * @param lng 经度
	 * @param def 是否设置为默认（1默认 其它值不设为默认）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editaddress", produces = "text/plain;charset=UTF-8")
	@Authorization(type = Constant.SESSION_USER)
	public String editAddress(@RequestParam(value = "addressid", required = false, defaultValue = "0") Long addrId,
			@RequestParam(value = "address", required = false, defaultValue = "") String address, @RequestParam(value = "lat", required = false, defaultValue = "0") Double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") Double lng, @RequestParam(value = "default", required = false, defaultValue = "") String def,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone, @RequestParam(value = "name", required = false, defaultValue = "") String name)
			throws Exception {
		if (addrId == 0 && (lat <= 0 || lng <= 0))
			return JsonUtils.resultJson(-2, "位置信息错误", null);
		if (addrId == 0 && Utils.isEmpty(address))
			return JsonUtils.resultJson(-3, "请填写地址信息", null);
		boolean isadd = true;
		boolean isdef = false;
		if (addrId > 0)
			isadd = false;
		if (def.equals("1"))
			isdef = true;

		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = userService.findOne(((User) obj).getId());

		Address addObj = null;

		if (user.getType().equals(UserType.merchants)) {// 商户只有一个绑定地址。
			isdef = true;
			List<Address> oldAddress = addressService.findByUserId(user.getId());
			if (isadd && oldAddress.size() > 0)
				return JsonUtils.resultJson(4, "设置地址失败", null);
		}

		if (isadd) {
			addObj = new Address();
			addObj.setUserId(user.getId());
			
			// 获取位置信息
			if (Utils.isEmpty(user.getCity()) || user.getCityCode() == 0) {
					AddressBean addr = Common.geocoderWithBaidu(lng, lat);
					if (null != addr) {
						user.setCity(addr.getCity());
						user.setProvince(addr.getProvince());
						user.setCityCode(addr.getCityCode());
					}
			}
			
		} else {
			addObj = addressService.findOne(addrId);
			if (null == addObj)
			    return JsonUtils.resultJson(5, "地址修改失败", null);
				
		}
		if(!Utils.isEmpty(address))
			addObj.setAddress(address);
		if(lat>0 && lng>0)
			addObj.setLocation(new double[] { lng, lat });
		if(!Utils.isEmpty(phone))
			addObj.setPhone(phone);
		if(!Utils.isEmpty(name))
			addObj.setName(name);

		addressService.save(addObj);

		if (isdef) {
			user.setAddress(addObj.getAddress());
			user.setAddressId(addObj.getId());
			user.setLocation(addObj.getLocation());
			session.setAttribute(Constant.SESSION_USER, user);
		}
		userService.save(user);
		session.setAttribute(Constant.SESSION_USER, user);

		Map<String, String> reMap = new HashMap<String, String>();
		reMap.put("addr_id", addObj.getId() + "");
		reMap.put("address", addObj.getAddress());
		reMap.put("phone", addObj.getPhone());
		reMap.put("name", addObj.getName());
		reMap.put("lng", addObj.getLocation()[0] + "");
		reMap.put("lat", addObj.getLocation()[1] + "");
		reMap.put("def", isdef ? "1" : "0");

		return JsonUtils.resultJson(1, "", reMap);
	}

	/**
	 * 删除地址
	 * 
	 * @param addrId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/removeaddress", produces = "text/plain;charset=UTF-8")
	@Authorization(type = Constant.SESSION_USER)
	public String removeAddress(@RequestParam(value = "addressid", required = false, defaultValue = "0") long addrId) {
		Object obj = session.getAttribute(Constant.SESSION_USER);
		User user = userService.findOne(((User) obj).getId());
		if (addrId > 0) {
			addressService.remove(addrId);
			if (addrId == user.getAddressId()) {
				user.setAddress("");
				user.setAddressId(0);
				user.setLocation(new double[] { 0, 0 });
				userService.save(user);
			}
		}
		return JsonUtils.resultJson(1, "", null);
	}

	/**
	 * 版本更新
	 * 
	 * @param type 客户端类型 1:买家 2:卖家
	 * @param code 版本号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkversion", produces = "text/plain;charset=UTF-8")
	@Authorization(type = Constant.SESSION_USER)
	public String checkVersion(@RequestParam(value = "type", required = false, defaultValue = "1") int type,
			@RequestParam(value = "code", required = false, defaultValue = "0") int code) {
		Map<String, String> reMap = new HashMap<String, String>();
		if (code > 0) {
			ReleaseVersion version = releaseVersionService.findMax(type, code);
			if (null != version) {
				reMap.put("version_name", version.getVersionCodeName());
				reMap.put("version_code", version.getVersionCode() + "");
				reMap.put("desc", version.getDesc());
				reMap.put("down_url", version.getDownloadUrl());
				reMap.put("must_update", version.isMustUpdate() ? "1" : "0");
				reMap.put("release_date", Utils.formatDate(new Date(version.getCreateDate()), "yyyy-MM-dd"));
				return JsonUtils.resultJson(1, "", reMap);
			} else
				return JsonUtils.resultJson(2, "已是最新版本", null);
		} else
			return JsonUtils.resultJson(-2, "版本号错误", null);
	}

}
