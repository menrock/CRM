<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List, com.niu.crm.model.*, com.niu.crm.model.type.AclScope"%>
<%@ page import="com.niu.crm.vo.UserFuncVO"%>

<%!
	private Func getFunc(List<Func> lsFunc, String funcCode){
		for(Func func:lsFunc){
			if(func.getCode().equals(funcCode))
				return func;
		}
		return null;
	}
	
	private boolean hasFunc(List<UserFuncVO> lsUserFunc, String funcCode){
		for(UserFunc uFunc:lsUserFunc){
			if(uFunc.getFuncCode().equals(funcCode))
				return true;
		}
		return false;
	}
	
	private String showAcl(String szAclScopes,  AclScope aclScope){
		String s = "<select name='aclScope'>"; 
		String[] aclScopes = szAclScopes.split(",");
				
		for(int i=0; i < aclScopes.length; i++){
			AclScope item = AclScope.valueOf(aclScopes[i]);
			boolean bSelected = false;
			if(item == aclScope )
				bSelected = true;
			
			s += "<option value='" + item.name() + "' " + (bSelected?"selected='true'":"") + ">" 
			   + item.getName() + "</option>";
		}
		s += "</select>";
		return s;
	}
	
%>
<%
	out.clear();
  
	List<Func> lsFunc = (List<Func>)request.getAttribute("lsFunc");
	List<UserFuncVO> lsUserFunc = (List<UserFuncVO>)request.getAttribute("lsUserFunc");
	AclScope[] arrAclScope = (AclScope[])request.getAttribute("lsAclScope");
		
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>权限设置</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
<script language='javascript'>
	
$(function() {	
	$('#userFuncForm').form({
		url:'/func/doUserFuncSet.do',
		onSubmit: function(){
        	// do some check
        	// return false to prevent submit;
        },
        error:function(){$.messager.show( {title: '错误信息',msg:'系统异常'} ); },
        success:function(data){
        	$.messager.show( {title: '操作成功',msg:'已保存'} ); 
        }
	});
});

	var arrAclScope = new Array();
	var arrFunc = new Array();
<%
	for(Func func:lsFunc){	
		out.println(" arrFunc.push({code:'" + func.getCode() +"',name:'" + func.getName() +"',aclScopes:'" + func.getAclScopes() + "'});");
	}	
	for(AclScope aclScope:arrAclScope){	
		out.println(" arrAclScope.push({value:'" + aclScope.name() +"',name:'" + aclScope.getName() +"'});");
	}	
%>	
		
	function getFunc(szCode){
		for(var i=0; i < arrFunc.length; i++){
			if(arrFunc[i].code == szCode)
				return arrFunc[i];
		}
		return null;
	}
	function getScope(aclScope){
		for(var i=0; i < arrAclScope.length; i++){
			if(arrAclScope[i].value == aclScope)
				return arrAclScope[i];
		}
		return null;
	}
	
	function showAcl(szAclScopes){
		var aclScopes = szAclScopes.split(",");
		
		s = "<select name='aclScope'>"; 		
		for(var idx=0; idx < aclScopes.length; idx++){
			item = getScope(aclScopes[idx]);
			s += "<option value='" + item.value + "' >" + item.name + "</option>";
		}
		s += "</select>";
		return s;
	}
	
	function jsGrant(obj){
		var tr = obj;
		var szCode = $(tr).attr("code");
		funcData = getFunc(szCode);
		var szName = funcData.name;
		var szScopes = funcData.aclScopes;
		
		$(tr).remove();
		
		var szHtml = '<tr code="' + szCode + '">';
		szHtml +='  <td style="width:120px">' + szCode 
		        +'    <input type="hidden" name="funcCode" value="' + szCode +'"/></td>'
		        +'  <td >' + szName + '</td>'
		        +'  <td >' + showAcl(szScopes) + '</td>'
		        +'  <td >' 
		        +'公司部门<input type="text" name="companyIds" style="width:260px" /><br/>'
		        +'来源渠道<input type="text" name="fromIds" style="width:260px" />'
		        +'  </td>'
		        +'  <td>'
		        +'   <textarea name="clause" style="width:98%" />'
		        +'  </td>'
		        +'  <td class="rightTD">'
		        +'    <a href="javascript:void(0)" onclick="jsRevoke(this)" >取消</a>'
		        +'  </td></tr>';
		$('#uFuncTbl').append(szHtml);
	}
	
	function jsRevoke(obj){
		var tr = null;
		while(true){
			if(obj.tagName == 'TR'){
				tr = obj;
				break;
			}
			obj = obj.parentNode;	
		}
		if(!tr) return;
			
		var szCode = $(tr).attr("code");
		funcData = getFunc(szCode);
		
		$(tr).remove();
		
		var szHtml = '<tr onclick="jsGrant(this)" code="' + szCode +'">';
		szHtml +='  <td ><input type="checkbox" name="code" value="' + szCode +'" /></td>'
		        +'  <td>' + funcData.code + '</td>'
		        +'  <td class="rightTD">' + funcData.name + '</td>'
		        +'</tr>';
		$('#funcTbl').append(szHtml);
	}
	
	function jsSave(){
		$('#userFuncForm').submit();
		/*
		var params =  $.serializeObject();
		params.userId = '${userId}';
		
		$.ajax({
			type:'post',
			url:'/func/doUserFuncSet.do',
			data:params,
			dataType:'json',
			error:function(){$.messager.show( {title: '错误信息',msg:'系统异常'} ); },
			success:function(){
            	$.messager.show( {title: '操作成功',msg:'已保存'} ); 
			}
		}); */
	}
</script>
<style type="text/css">
        .right{
        }
        .right table{
            background:#E0ECFF;
            width:100%;
        }
        .right tr{
            background:#E0ECFF;
            width:100%;
        }
        .right td{
            background:#E0ECFF;
            text-align:center;
            padding:2px;
            border-left:1px solid #499B33;
            border-bottom:1px solid #499B33;
        }
        .headerTD{
            border-top:1px solid #499B33;
        }
        .rightTD{
            border-right:1px solid #499B33;
        }
</style>
</head>
<body>

<div><strong>${u.name}</strong> 的权限设置</div>
<div>可选功能点</div>
<table class='right' id='funcTbl'>
	<tr>
		<td class='headerTD' style='width:80px'>&nbsp;</td>
		<td class='headerTD' style='width:120px'>功能点</td>
		<td class='headerTD rightTD' style='width:150px'>功能名称</td>
	</tr>
<%
  for(int i=0; i< lsFunc.size(); i++){
  	Func func = lsFunc.get(i);
  	if(hasFunc(lsUserFunc, func.getCode()))
  		continue;
%>
	<tr onclick='jsGrant(this)' code='<%= func.getCode()%>' >
		<td ><input type='checkbox' name='code' value="<%= func.getCode()%>" /></td>
		<td ><%= lsFunc.get(i).getCode()%></td>
		<td class='rightTD'><%= func.getName()%></td>
	</tr>
<%}%>
</table>

<br>
<div>已有功能点</div>
<form id='userFuncForm' method='post'>
<input type='hidden' name='userId' value='${userId}' />
<table class='right' id='uFuncTbl'>
	<tr>
		<td class='headerTD' style='width:120px'>功能点</td>
		<td class='headerTD' style='width:150px'>功能名称</td>
		<td class='headerTD' style='width:120px'>权限范围</td>
		<td class='headerTD' style='width:330px'>限制范围</td>
		<td class='headerTD' style='width:330px'>个性化条件</td>
		<td class='headerTD rightTD' style='width:90px'>&nbsp;</td>
	</tr>
<%
  for(int i=0; i< lsUserFunc.size(); i++){
  	UserFuncVO userFunc = lsUserFunc.get(i);
  	Func func = getFunc(lsFunc, userFunc.getFuncCode());
  	String companyIds = "", companyNames ="";
  	if( userFunc.getAclScope() == AclScope.SOMECOMPANY && userFunc.getCompanyIds() !=null ){
  		companyIds   = userFunc.getCompanyIds();
  		companyNames = userFunc.getCompanyNames();
  	}
  	
  	if( userFunc.getAclScope() == AclScope.SOMEUNIT && userFunc.getUnitIds() !=null ){
  		companyIds   = userFunc.getUnitIds();
  		companyNames = userFunc.getUnitNames();
  	}
  	String fromIds = userFunc.getFromIds()==null?"":userFunc.getFromIds();
  	String clause = userFunc.getClause()==null?"":userFunc.getClause();
%>
	<tr code='<%= userFunc.getFuncCode() %>'>
		<td><%= userFunc.getFuncCode() %></td>
		<td><%= userFunc.getFuncName()%></td>
		<td>
			<%= showAcl(func.getAclScopes(), userFunc.getAclScope() ) %>
			<input type='hidden' name='funcCode' value='<%= userFunc.getFuncCode() %>'>
		</td>
		<td>
			公司部门<input type='text' name='companyIds' value='<%= companyIds %>' style='width:260px' /><br/>
			来源渠道<input type='text' name='fromIds' value='<%= fromIds %>' style='width:260px' />
		</td>
		<td>
			<textarea name='clause' style='width:98%' /><%= clause %></textarea>
		</td>
		<td class='rightTD'>
			<a href='javascript:void(0)' onclick='jsRevoke(this)' >取消</a>
		</td>
	</tr>
<%}%>
</table>
</form>
<a href='javascript:void(0)' onclick='jsSave(this)'>保存</a>

</body>
</html>