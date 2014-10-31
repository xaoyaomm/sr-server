package com.sfc.api.test.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.store.api.common.Constant;
import com.store.api.common.MD5;
import com.store.api.utils.JsonUtils;

/**
 * 示例
 * 
 * Revision History
 * 
 * 2014年4月24日,vincent,created it
 */

public class TerminalActionTestCase extends BaseActionTestCase{
    
    @Before
    public void initSession() throws Exception{
        cookie=new Cookie("JSESSIONID", "test1234567890");
        headers.add(Constant.SESSION_NAME, "test1234567890");
        headers.add("imei", "1234567890test");
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/sessionInit/plUserCargo").cookie(cookie).headers(headers));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        
        System.out.println("init session:"+result);
    }
    
    
    @Test
    public void testResultList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/V1/appterminal/appresultlist")
                .param("s_x", "114.012317")
                .param("s_y", "22.602268")
                .param("sort", "distance")
                .param("cartype_id", "5,6,1,2")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
//        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testResultdetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/V1/appterminal/appresultdetail")
                .param("car_id", "108")
                .param("order_id", "")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testCallhistorylist() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/callhistorylist")
                .param("pageno", "1")
                .param("limit", "2")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testRemovecallhistory() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/removecallhistory")
                .param("id", "5")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testFeedback() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/feedback")
                .param("content", "testtest")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testReporttextcomment() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/reporttextcomment")
                .param("booking_no", "8")
                .param("comment", "test")
                .param("comment_level","2")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testTelephonerecord() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/telephonerecord")
                .param("vehiclemobile", "18010086868")
                .param("voiceid", "17")
                .param("longitude","114.103391")
                .param("latitude", "22.545261")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    

    @Test
    public void testVoiceOrder() throws Exception{
        byte[] bytes = new byte[] {1, 2,3};
        MockMultipartFile mockfile=new MockMultipartFile("voice_file",bytes);
        ResultActions ra = mockMvc.perform(
//                MockMvcRequestBuilders.post("/appterminal/voiceorder")
                MockMvcRequestBuilders.fileUpload("/appterminal/voiceorder","voice_file").file("voice_file",bytes)
                .param("sendTime", new Date().getTime()+"")
                .param("longitude","114.103391")
                .param("latitude", "22.545261")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .cookie(cookie).headers(headers).accept(MediaType.ALL));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testOfferList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/offerlist")
                .param("pageno", "1")
                .param("limit", "10")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testVoiceOrderDetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/voiceorderdetail")
                .param("order_id", "113")
                .param("sort", "1")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testLastOfferVehicle() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/lastoffervehicle")
                .param("order_id", "113")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testBookingList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/bookinglist")
                .param("pageno", "1")
                .param("limit", "10")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testBookingDetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/bookingdetail")
                .param("booking_id", "4")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testCreatebooking() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/createbooking")
                .param("longitude","114.103391")
                .param("latitude", "22.545261")
                .param("voice_id", "113")
                .param("ve_mobile", "13760301170")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testConfirmbooking() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/confirmbooking")
                .param("booking_id","6")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testNewMessage() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/newmessage")
                .param("booking_id","6")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testCommentlist() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/commentlist")
                .param("vehicle_id","2")
                .param("pageno", "1")
                .param("limit", "10")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testBookingTrack() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/bookingtrack")
                .param("booking_id","9")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testRegister() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/register")
                .param("mobile","13632434676")
                .param("verifycode","96139")
                .param("username","test112113114")
                .param("password","123456789")
                .param("recommender","13926599533")
                .param("longitude","114.103391")
                .param("latitude", "22.545261")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testLogin() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/login")
                .param("username","test112113114")
                .param("password","123456789")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testForgetPasswd() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/forgetpasswd")
                .param("mobile","13632434676")
                .param("password","123456789")
                .param("verifycode","96139")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testModifyPasswd() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/modifypasswd")
                .param("oldpasswd","123456789")
                .param("newpasswd","123456")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testModifymobile() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/modifymobile")
                .param("passwd","123456789")
                .param("mobile","13510636281")
                .param("verifycode","96139")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testTextDetail() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/textdetail")
                .param("order_id","91")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testCancelVoice() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/cancelvoice")
                .param("order_id","91")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testCollectionList() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/collectionlist")
                .param("pageno", "1")
                .param("limit", "10")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testAddCollection() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/addcollection")
                .param("vehicle_id", "3")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testDelCollection() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/delcollection")
                .param("vehicle_id", "1")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testGetUsercargoInfo() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/getusercargoinfo")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testUpdateUsercargoInfo() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/appterminal/updateusercargoinfo")
                .param("username", "猛帅哥")
//                .param("recommender", "13307550000")
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").equals("0"));
    }
    
    @Test
    public void testSendSms() throws Exception{
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.post("/smsinterface/send")
                .param("mobile", "13510636281")
                .param("msg", "就只是测试一下撒")
                .param("gate", "1")
                .param("token", MD5.encrypt(Constant.SFC_KEY+"13510636281"+"就只是测试一下撒"))
                .cookie(cookie).headers(headers).accept(MediaType.APPLICATION_JSON));          
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        log.debug(result);
        Assert.isTrue(StringUtils.isNotEmpty(result));
        Map map=(Map) JsonUtils.json2Object(result, HashMap.class);
        Assert.isTrue(map.get("errorcode").toString().equals("0"));
    }
    
}
