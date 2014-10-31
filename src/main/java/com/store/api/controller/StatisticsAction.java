package com.store.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.mysql.business.OnlineStatusBL;
import com.store.api.utils.Method;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/statistics")
public class StatisticsAction extends BaseAction {
	@Autowired
	private OnlineStatusBL onlineStatusBL;
	
	private Map<String,Object> result = new HashMap<String, Object>();
	
	private static final String  SFC_KEY = "sfc&2012#^%888";
	
	
	@ResponseBody
    @RequestMapping("/onlinestatus")
	public Map<String,Object> onlinestatus(
		
		@RequestParam(value = "username",required = false ,defaultValue = "") String userName,
		@RequestParam(value = "status" ,required = false ,defaultValue = "-1") Long status,
		@RequestParam(value = "token",required = false  ,defaultValue = "") String token
		){
		String thisToken = Method.MD5(SFC_KEY+userName+status);
		if(!token.equals(thisToken)){
			result.put("errorcode", "2");
			result.put("info", "令牌错误");
			return result;
		}
		if(status != 0 && status != 1){
			result.put("errorcode", "3");
			result.put("info", "状态码错误");
			return result;
		}
		try {
			result = onlineStatusBL.onlineStatusBusiness(userName, status);
			return result;
		} catch (Exception e) {
			LOG.error(" onlinestatus error!! "+e);
			result.put("errorcode", "4");
			result.put("info", "服务器忙晕了，请稍后再试");
			return  result;
		}
		
	}
}
