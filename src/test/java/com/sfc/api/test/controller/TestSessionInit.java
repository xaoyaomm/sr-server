package com.sfc.api.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Constant;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.PlUsersCargo;

@Controller()
@Scope("prototype")
@RequestMapping("/sessionInit")
public class TestSessionInit {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @ResponseBody
    @RequestMapping("/plUser")
    public String initSessionWithPlUser() {
        PlUsers user=new PlUsers();
        user.setUserId(10L);
        user.setMobile("18522648481");
        user.setUserName("小松哥");
        user.setUserType(1L);
        user.setIsvalid("Y");
        request.getSession().setAttribute(Constant.SESSION_PL_USER, user);
        return "SUCCESS";
    }
    
    @ResponseBody
    @RequestMapping("/plUserCargo")
    public String initSessionWithPlUserCargo(){
        PlUsersCargo user=new PlUsersCargo();
        user.setUserCargoId(2520L);
        user.setUserName("猛帅哥");
        user.setGpsX(114.10339);
        user.setGpsY(22.545312);
    //    user.setMobile("15000001111");
        user.setMobile("18777779753");
        user.setAddressDesc("广东省深圳市福田区南园路66号");
        user.setMobileValidate(1L);
        user.setIsvalid("Y");
//        user.setRecommendId(2502L);
//        user.setRecommendType(1L);
        request.getSession().setAttribute(Constant.SESSION_PL_USER_CARGO, user);
        return "SUCCESSS";
    }

}
