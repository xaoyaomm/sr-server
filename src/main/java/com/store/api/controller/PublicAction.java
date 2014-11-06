package com.store.api.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.service.UserService;
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
	@RequestMapping("/register")
	public String register(
			@RequestParam(value = "name", required = false, defaultValue = "") String userName,
			@RequestParam(value = "nickname", required = false, defaultValue = "") String nickName,
			@RequestParam(value = "pwd", required = false, defaultValue = "") String pwd,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
			@RequestParam(value = "type", required = false, defaultValue = "2") Long type,
			@RequestParam(value = "promocode", required = false, defaultValue = "") String code)
			throws Exception {
		if (Utils.isEmpty(userName) || Utils.isEmpty(pwd)) {
			return JsonUtils.resultJson(2, "用户名或密码不能为空", null);
		}
		if (Utils.isEmpty(nickName)) {
			nickName = userName;
		}
		User user = new User();
		user.setUserName(userName);
		user.setNickName(nickName);
		user.setPwd(pwd);
		user.setPhone(phone);
		user.setPromoCode(code);
		user.setType(type == 1 ? UserType.merchants : UserType.customer);
		Map<String, Object> veResult = new HashMap<String, Object>();
		return null;

	}

}
