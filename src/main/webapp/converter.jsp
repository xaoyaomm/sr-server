<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>ConvertToMongo</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function() {

    $('div').click(function() {
		var id=this.id;
		if(this.innerText.indexOf("已转换")!= -1)
			return false;
        $.ajax({
            url: '/conver/conver',
            type: 'post',
			dataType:'json',
            data: "tname="+$('#'+id+'_name').text(),
            beforeSend:function(){
              $('#'+id).html('转换中......');},
            success: function(result) {
				if(result.status=='success'){
					$('#'+id).css("color","#00BB00");
					
					$('#'+id).html("已转换");
				}else{
					$('#'+id).html("转换失败");
				}
            }
        });
    });
});
</script>
</head>
<body>
<table width="60%" border="0" cellspacing="1" bgcolor="#000000">
  <tr>
    <td bgcolor="#FFFFFF" width="40%">MYSQL表名</td>
	<td bgcolor="#FFFFFF" width="30%">描述</td>
	<td bgcolor="#FFFFFF" width="30%">状态</td>
  </tr>
<c:forEach var="bean" items="${list}">
<tr>
<td id="con_${bean.id}_name" bgcolor="#FFFFFF">${bean.name}</td>
<td bgcolor="#FFFFFF">${bean.desc}</td>
<td bgcolor="#FFFFFF">
<c:if test="${!bean.done}"><div style="color:#FF2D2D" id="con_${bean.id}"/>点击开始转换</div></c:if>
<c:if test="${bean.done}"><div style="color:#00BB00" id="con_${bean.id}">已转换</div></c:if>
</td>
</tr>
</c:forEach> 
</table>

</body>
</html>
