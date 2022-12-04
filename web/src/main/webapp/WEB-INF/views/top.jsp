<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 //EN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
	<title></title>
<style type="text/css">
.tool{
   color:#18615A;
   font-size:12px;
}	
.Welcome{
   color:#18615A;
   font-size:12px;
   font-weight:bold;
   text-align:center;
   text-valign:middle;   
}

.nwLink { color:#0000FF; font-size:13px;color:#0000FF; font-weight: bold;}
.nwLink A:link{color:#FF0000; font-size:15px; font-weight: bold;}
.nwLink A:visited{color:#0000FF; font-weight: bold;}

</style>
<script language='javascript'>
	var onlineFlag = ${user.online};
	function switchOnline(){
		var szUrl = "/user/online.do";
		if(onlineFlag)
			szUrl = "/user/offline.do";
		$.ajax({
			type: 'POST',
			url: szUrl,
			data: {},
			dataType: "json",
			error:function(){ alert('系统异常'); },
			success: function(json){
				if(json.errorCode == 200){
			  		if(onlineFlag){
			  			$('#aOnline').text('[离线]');
			  			onlineFlag = false;
			  		}else{
			  			$('#aOnline').text('[在线]');
			  			onlineFlag = true;
			  		}		
			  	}
			}
		});
	}
</script>
</head>
<body style="background-color:#2269b8" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" ID="Table1" background="/images/top-bg.gif">
		<tr>
			<td valign="top" height="71" width="170">
			
			</td>
			<td>
			<div class="nwLink" style="color: red;">
			</td>
			<td align="right" >
			  <table >
				<tr>
				  <td class='tool' align='right'>
				  	欢迎：${user.name}
				  	
				  	<a href='javascript:void(0)' onclick='switchOnline()' id='aOnline'>
				  	<c:if test="${user.online}">[在线]</c:if>
				  	<c:if test="${user.online==false}">[离线]</c:if>
				  	</a>
				  </td>
				  <td class="Welcome" height="18" width=150>
					  <a href="/user/changePwd.do" target="mainFrame" class="Welcome">修改密码</a>&nbsp;
					  <a href="/logout" target="_top" class="Welcome">退出系统</a>&nbsp;
				  </td>
				</tr>
			  </table>  
			</td>
		</tr>
	</table>
</body>
</html>