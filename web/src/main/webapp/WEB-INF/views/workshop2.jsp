<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  String leftWidth="210";  
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>crm</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<!-- easyui -->
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/extIcon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
  <script language="javascript">
	<!--
		function expend(){			
			//main_frm.cols="<%=leftWidth%>,10,*";
			document.getElementById("main_frm").setAttribute("cols","<%=leftWidth%>,10,*");
		}
		
		function close(){
			//main_frm.cols="0,10,*";
			document.getElementById("main_frm").setAttribute("cols","0,10,*");
		}
	//-->
   </script>
		
	</HEAD>
<%
		String  user_left_url = "",user_right_url = "lx/student/myStudents.do";
		user_left_url = "navigatorTree.do";
%>	
	<frameset border="0" frameSpacing="0" rows="70,1,*" frameBorder="no">
		<frame name="topFrame" src="top.do" noResize scrolling="no">
		<frame name="topFrame1" src="" noResize scrolling="no">		
		<FRAMESET border="0" id="main_frm" name="main_frm" frameSpacing="0" frameBorder="no" cols="<%=leftWidth%>,10,*">
		
		   <frame style="BORDER-RIGHT: 1px solid; BORDER-TOP: 1px solid; OVERFLOW: auto; BORDER-LEFT: 1px solid; BORDER-BOTTOM: 1px solid; POSITION: static"
					name="MenuFrame" marginWidth="0" marginHeight="0" src="<%=user_left_url %>" 
					DESIGNTIMEDRAGDROP="89" target="PageFrame">
			<frame name="MoveFrame" marginWidth="0" marginHeight="0" src="MoveFrame.htm" frameBorder="no" scrolling="no">
			<frame name="mainFrame" marginWidth="0" marginHeight="0" src="<%=user_right_url %>" frameBorder="no">
		</FRAMESET>
	</frameset>
</HTML>