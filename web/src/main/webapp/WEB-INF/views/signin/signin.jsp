<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN">
<head>
	<meta charset="utf-8" />
    <title>用户登录</title>
    <STYLE>
body {
	background: #ebebeb;
	font-family: "Helvetica Neue", "Hiragino Sans GB", "Microsoft YaHei",
		"\9ED1\4F53", Arial, sans-serif;
	color: #222;
	font-size: 12px;
}

* {
	padding: 0px;
	margin: 0px;
}

.top_div {
	background: #E47833;
	width: 100%;
	height: 400px;
}

.ipt1 {
	border: 1px solid #d3d3d3;
	padding: 10px 10px;
	width: 190px;
	border-radius: 4px;
	padding-left: 35px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s
}

.ipt {
	border: 1px solid #d3d3d3;
	padding: 10px 10px;
	width: 290px;
	border-radius: 4px;
	padding-left: 35px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s
}

.ipt:focus {
	border-color: #66afe9;
	outline: 0;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px
		rgba(102, 175, 233, .6);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px
		rgba(102, 175, 233, .6)
}

.u_logo {
	background: url("_resources/images/username.png") no-repeat;
	padding: 10px 10px;
	position: absolute;
	top: 43px;
	left: 40px;
}

.p_logo {
	background: url("_resources/images/password.png") no-repeat;
	padding: 10px 10px;
	position: absolute;
	top: 12px;
	left: 40px;
}

a {
	text-decoration: none;
}

.logo {
	width: 97px;
	height: 92px;
	position: absolute;
	top: -94px;
	left: 140px;
}

</STYLE>
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/extIcon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>

<script language='javascript'>
$(function(){
	$("#f").submit(function() {
		var username=$("#j_username").val();
		var password=$("#j_password").val();
		var data={username:username,password:password}; 
		var url="/login"; 
		$.ajax({
			type: "POST",
			url: url,
			data: data,
			dataType: "json",
			success:function (result) {
				if(result.successFlag){
					location.href="/workshop";
				}else{
					$.messager.show({title: '登录失败', msg: result.msg});
					refreshVerifyCode();
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert('读取超时，请检查网络连接'); 
			}
		});
		return false;
	});
});
		
	function refreshVerifyCode(){
		var imgUrl = '/verifyCode?t=' + (new Date()).getTime();
		$('#imgVerifyCode').attr('src', imgUrl);
	}	
</script>
</head>
<body>
<DIV class="top_div"></DIV>
<!--
<form  id="f" class="form-narrow form-horizontal" action="/j_spring_security_check" method="post" th:action="@{/j_spring_security_check}">
-->
<form method='post' id="f" class="form-narrow form-horizontal" action="/login">
    	<DIV style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px;
    	 height: 250px; text-align: center;">
		<DIV style="width: 165px; height: 96px; position: absolute;">
			<DIV class="logo"></DIV>
		</DIV>
		<P style="padding: 3px 0px 5px;position: relative;">
		 ${message}
		</P>		
    	<P style="padding: 30px 0px 0px; position: relative;">
			<SPAN class="u_logo"></SPAN> <INPUT class="ipt" id="j_username" name="j_username" type="text"
				placeholder="请输入用户名" value=""/>
		</P>
		<P style="padding: 5px 0px 0px; position: relative;">
			<SPAN class="p_logo"></SPAN> <INPUT class="ipt" id="j_password" name="j_password"
				type="password" placeholder="请输入密码" value=""/>
		</P>
		<DIV style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
			<P style="margin: 0px 35px 20px 45px;">
				<SPAN style="float: left;"> <input type="checkbox" name="_spring_security_remember_me" /> Remember me</SPAN> 
				<SPAN style="float: right;"><button type="submit" class="btn btn-default" style="background: rgb(0, 142, 173); 
					  padding: 7px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); 
					  font-weight: bold;">Sign in</button>
				</SPAN>
			</P>
		</DIV>				
    </DIV>
</form>


</body>
</html>