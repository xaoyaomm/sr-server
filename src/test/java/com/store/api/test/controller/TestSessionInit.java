package com.store.api.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.common.Constant;
import com.store.api.mongo.entity.User;
import com.store.api.mongo.entity.enumeration.UserType;

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
        User user=new User();
        user.setId(10L);
        user.setPhone("18522648481");
        user.setUserName("test_1");
        user.setType(UserType.customer);
        request.getSession().setAttribute(Constant.SESSION_USER_CUSTOMER, user);
        return "SUCCESS";
    }
    
    @ResponseBody
    @RequestMapping("/plUserCargo")
    public String initSessionWithPlUserCargo(){
    	 User user=new User();
         user.setId(10L);
         user.setPhone("18522648481");
         user.setUserName("test_1");
         user.setType(UserType.merchants);
        request.getSession().setAttribute(Constant.SESSION_USER_MERCHANTS, user);
        return "SUCCESSS";
    }

}
