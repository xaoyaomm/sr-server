<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>SmsSendTest</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function() {
        $.ajax({
            url: '/test/getGateWay',
            type: 'post',
            success: function(result) {
            	$("#gates").val(result);
            }
        });
        
     $('#get_code').click(function() { 
     	$.ajax({
            url: '/test/getCodeGateWay',
            type: 'post',
            success: function(result) {
            	$("#gatescode").val(result);
            }
        });
        return false;
    });
        
     $('#change_code').click(function() { 
     	$.ajax({
            url: '/test/setCodeGateWay',
            type: 'post',
            data: "gates="+$("#gatescode")[0].value,
            beforeSend:function(){
              $("#changecode_result").html('修改中......');},
            success: function(result) {
            	$("#changecode_result").html(result);
            }
        });
        return false;
    });
        
    $('#submit').click(function() {
        $.ajax({
            url: '/test/sendsms',
            type: 'post',
            data: "mobile="+$("#mobile")[0].value+"&msg="+$("#msg")[0].value+"&gate="+$("input[id='gate']:checked").val(),
            beforeSend:function(){
              $("#result").html('发送中......');},
            success: function(result) {
            	$("#result").html(result);
            }
        });
        return false;
    });
	
    $('#change').click(function() {
        $.ajax({
            url: '/test/setGateWay',
            type: 'post',
            data: "gates="+$("#gates")[0].value,
            beforeSend:function(){
              $("#change_result").html('修改中......');},
            success: function(result) {
            	$("#change_result").html(result);
            }
        });
        return false;
    });
    
    $('#week').click(function() {
        $.ajax({
            url: '/test/weekonline',
            type: 'post',
            data: "date="+$("#date")[0].value+"&hour="+$("#hour")[0].value+"&cycle="+$("#cycle")[0].value,
            beforeSend:function(){
              $("#week_result").html('计算中......');},
            success: function(result) {
            	$("#week_result").html(result);
            }
        });
        return false;
    });
});
</script>
</head>
<body>
<form id="formTest">
<lable>手机号:</lable><input id="mobile" name="mobile" type="text" /><br/>
<lable>内    容:</lable><input id="msg" name="msg" type="text" /><br/>
<input type="radio" id="gate" name="gate" value="1" checked="checked" />3通 
<input type="radio" id="gate" name="gate" value="2" />优易网 
<input type="radio" id="gate" name="gate" value="3" />翼锋 <br/>
<input type="radio" id="gate" name="gate" value="4" />漫道 <br/>
<input type="button" id="submit" value="发送" />
</form>
<div id="result" ></div>

<div><strong>修改短信发送网关顺序</strong></div>
<ol>
  <li>三通</li>
  <li>优易网</li>
  <li>翼锋</li>
  <li>漫道</li>
</ol>
<p>普通短信：<input type="text" id="gates" name="gates" size="50" value=""/><div id="change_result"></div></p>
<input type="button" id="change" value="修改" />
<p>
<p>验证码短信：<input type="text" id="gatescode" name="gatescode" size="50" value=""/><div id="changecode_result"></div></p>
<input type="button" id="get_code" value="获取" /><input type="button" id="change_code" value="修改" />
<p>
<a href="/conver/getlist">MYSQL－MONGO数据转换</a>
</p>
<p>
<div><strong>测试七天在线用户数</strong></div>
结束日期(yyyy-mm-dd)：<input type="text" id="date" name="date" size="50" value=""/><br/>
每日在线时长(小时)：<input type="text" id="hour" name="hour" size="50" value="14"/><br/>
计算天数：<input type="text" id="cycle" name="cycle" size="50" value="7"/><br/>
<input type="button" id="week" value="统计" />
<div id="week_result"></div>
</p>
</body>
</html>
