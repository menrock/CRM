<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>拥有此功能点的用户</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
	<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
	<script type="text/javascript" src="/_resources/js/date.js"></script>
	<script type="text/javascript" src="/_resources/My97DatePicker/WdatePicker.js"></script>
	<script language='javascript'>
	var grid;
  
	$(function() {
	  grid = $('#grid').datagrid({
			title : '',
			url : 'usersData.do',
			queryParams:{funcId:'${func.id}'},
			striped:true,
			rownumbers : true,
			pagination : true,
			pageSize : 20,
			pageList: [20,30,50],
			idField : 'id',
			columns : [ [ 
			{
				field : 'id',
				checkbox: true
			},{
				width : 150,
				title : '公司',
				align:'center',
				field : 'code',
				formatter:function(value,row,index){
					return row.user.companyName;
				}
			},{
				width : '120',
				title : '姓名',
				align:'center',
				field : 'user_name',
				formatter:function(value,row,index){
					return row.user.name;
				}
			},{
				width : '120',
				title : '账号',
				align:'center',
				field : 'user_account',
				formatter:function(value,row,index){
					return row.user.account;
				}
			},{
				width : '100',
				title : '权限范围',
				halign:'center',
				field : 'aclScope'
			},{
				width : '120',
				title : '公司范围',
				halign:'center',
				field : 'companyIds'
			},{
				width : 120,
				title : '客户来源范围',
				halign:'center',
				field : 'fromIds'
			}
			] ],
				toolbar : '#toolbar'
			});
	  });
	  
	function jsQuery() {
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
		var params =  $.serializeObject($('#searchForm'));
		//params.[[${_csrf.parameterName}]] = '[[${_csrf.token}]]';
		grid.datagrid('load', params); 
	};	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>拥有功能点[${func.code}(${func.name})]的用户</td></tr>
</table>
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>
</body>
</html>