<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>短信模板</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
</head>
<body>
<form method='post' id='smsForm'>
<table>
<c:if test="${stuId !=null}">
	<tr>
		<td style='text-align:right'>姓名</td>
		<td>
			<input type='text' value='${customer.name}' style='width:320px' readonly>
			<input type='hidden' name='stuId' value='${stuId}' style='width:300px'>
		</td>
	</tr>
</c:if>
	<tr>
		<td style='width:80px;text-align:right'>手机号码</td>
		<td>
			<input type='text' name='mobile' <c:if test="${stuId !=null}">value='${customer.mobile}'</c:if> style='width:320px'>
			<input type='hidden' name='sendingFlag' >
		</td>
	</tr>
	<tr>
		<td style='text-align:right'>短信内容</td>
		<td>
			<textarea name='content' style='width:320px;height:80px'></textarea>
		</td>
	</tr>
</table>
</form>
</body>
</html>