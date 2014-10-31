package com.store.api.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Constant;
import com.store.api.common.MD5;
import com.store.api.mysql.service.SmsSendService;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/Smsinterface")
public class SmsinterfaceAction extends BaseAction{
	
	@Autowired
	private SmsSendService smsSendService;
	
	private Map<String, Object> result = new HashMap<String, Object>();

	
	/**************************************************
	 * 发送短信
	 * 
	 * @return Json
	 */
	@ResponseBody
    @RequestMapping("/send")
    public Map<String, Object> send(@RequestParam(value = "gate", required = false, defaultValue = "1")
    int gate, @RequestParam(value = "mobile", required = false, defaultValue = "")
    String mobile, @RequestParam(value = "msg", required = false, defaultValue = "")
    String msg,@RequestParam(value = "token", required = false, defaultValue = "")
    String token){
		try {
			if(!token.equals(MD5.encrypt(Constant.SFC_KEY+mobile+URLDecoder.decode(msg,"UTF-8")))){
			    result.put("errorcode", "1");
		        result.put("info", "令牌验证失败");
		        return result;
			}
			boolean flag=smsSendService.sendSms(mobile, msg);
			if(flag){
			    result.put("errorcode", "0");
                result.put("info", "短信发送成功");
                return result;
			}
			else{
			    result.put("errorcode", "2");
                result.put("info", "短信发送失败");
                return result;
			}
		} catch (Exception e) {
			LOG.error("sendSms error",e);
			result.put("errorcode", "3");
            result.put("info", "短信接口异常");
            return result;
		}
	}
}
