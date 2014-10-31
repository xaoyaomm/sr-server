package com.store.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.job.OnlineWeekJob;
import com.store.api.mysql.service.SmsSendService;
import com.store.api.utils.Utils;

@Controller()
@Scope("prototype")
@RequestMapping("/test")
public class TestAction extends BaseAction {

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private OnlineWeekJob onlineWeek;

    @ResponseBody
    @RequestMapping("/sendsms")
    public String testSendSms(@RequestParam(value = "mobile", required = false, defaultValue = "")
    String mobile, @RequestParam(value = "msg", required = false, defaultValue = "")
    String msg, @RequestParam(value = "gate", required = false, defaultValue = "1")
    int gate) {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if(null==request.getSession().getAttribute("valid"))
            return "请先登录";
        return smsSendService.testSendSms(mobile, msg, gate);
    }

    @ResponseBody
    @RequestMapping("/setGateWay")
    public String setGateWayConfig(@RequestParam(value = "gates", required = false, defaultValue = "")
    String gates) {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if(null==request.getSession().getAttribute("valid"))
            return "请先登录";
        try {
            if (!Utils.isEmpty(gates) && gates.contains(",")) {
                String[] strs = gates.split(",");
                int[] gateways = new int[strs.length];
                for (int i = 0; i < strs.length; i++) {
                    gateways[i] = Integer.parseInt(strs[i]);
                }
                smsSendService.setGates(gateways);
                return "修改成功";
            }
        } catch (Exception e) {
            return "唉，修改失败了呀~" + e;
        }
        return "唉，修改失败了呀~";
    }

    @ResponseBody
    @RequestMapping("/setCodeGateWay")
    public String setCodeGateWayConfig(@RequestParam(value = "gates", required = false, defaultValue = "")
    String gates) {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if(null==request.getSession().getAttribute("valid"))
            return "请先登录";
        try {
            if (!Utils.isEmpty(gates) && gates.contains(",")) {
                String[] strs = gates.split(",");
                int[] gateways = new int[strs.length];
                for (int i = 0; i < strs.length; i++) {
                    gateways[i] = Integer.parseInt(strs[i]);
                }
                smsSendService.setCodeGates(gateways);
                return "修改成功";
            }
        } catch (Exception e) {
            return "唉，修改失败了呀~" + e;
        }
        return "唉，修改失败了呀~";
    }

    @ResponseBody
    @RequestMapping("/getGateWay")
    public String getGateWayConfig() {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if(null==request.getSession().getAttribute("valid"))
            return "请先登录";
        int[] gates = smsSendService.getGates();
        String result = "";
        if (null != gates) {
            for (int i = 0; i < gates.length; i++) {
                result += gates[i];
                if (i != gates.length - 1)
                    result += ",";
            }
            return result;
        }
        return "糟了，未初始化";
    }

    @ResponseBody
    @RequestMapping("/getCodeGateWay")
    public String getCodeGateWayConfig() {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if(null==request.getSession().getAttribute("valid"))
            return "请先登录";
        
        int[] gates = smsSendService.getCodeGates();
        String result = "";
        if (null != gates) {
            for (int i = 0; i < gates.length; i++) {
                result += gates[i];
                if (i != gates.length - 1)
                    result += ",";
            }
            return result;
        }
        return "糟了，未初始化";
    }

    @RequestMapping("/testlogin")
    public String testLogin(@RequestParam(value = "na", required = false, defaultValue = "")
    String name, @RequestParam(value = "pw", required = false, defaultValue = "")
    String pwd) {
        if (name.equalsIgnoreCase("sfc123") && pwd.equals("sfchahaha")){
            request.getSession().setAttribute("valid", "1");
            return "managerSms";
        }
        else
            return null;
    }

    @ResponseBody
    @RequestMapping("/weekonline")
    public String weekOnline(@RequestParam(value = "date", required = false, defaultValue = "")
    String date, @RequestParam(value = "hour", required = false, defaultValue = "0")
    int hour, @RequestParam(value = "cycle", required = false, defaultValue = "")
    int cycle) {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if(null==request.getSession().getAttribute("valid"))
            return "请先登录";
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        try {
            endDate = formatDate.parse(date);
        } catch (ParseException e) {
            LOG.error("date parse fail.", e);
            return "date parse fail.";
        }
        String result = onlineWeek.calculate(endDate, cycle, hour * 60 * 60 * 1000, true);

        return result;
    }

}
