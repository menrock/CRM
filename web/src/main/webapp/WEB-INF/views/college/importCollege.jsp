<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>院校导入</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
<script language='javascript'>
	function ajaxImport(){		
		if( $('#importForm').find("input[name=savingFlag]").val() ==1){
			alert('请不要重复点击');
			return;
		}
		var formData = new FormData($( "#importForm" )[0]); 
		
		if( formData.get("file").name == ''){
			alert('请选择导入文件');
			return;
		}
		
		$('#importForm').find("input[name=savingFlag]").val(1);
        
        $.ajax({ 
        	url: 'doImportCollege.do' ,
        	type: 'POST',
        	data: formData,
        	async: false,
        	cache: false,
        	contentType: false,
        	processData: false,
        	success: function (json) {
        		$('#file').val('');
        		$('#importForm').find("input[name=savingFlag]").val(0)
        		
        		if(json.errorCode == 200){
        			$('#msg').html( ' 成功导入：' + json.data.count + '条');
        		}else{
        			$('#msg').html( ' 导入失败');
        		}
        		$.messager.show({title: '提醒信息', msg: json.data.msg});
        	},
        	error: function (json) {
        		alert('系统异常');
        	}
        });  
	}
</script>
</head>
<body >
<form id='importForm' action="doImportRight.do" method="post" enctype="multipart/form-data"> 
<input type='hidden' name='savingFlag' value="" /> <!-- 是否正在保存 -->
<table>
	<tr>
		<td	style='text-align:right'>院校导入文件</td>
		<td><input type="file" name="file" id="file"/>(excel文件)</td>
	</tr>
	<tr>
		<td	style='text-align:right'>院校类型</td>
		<td>
			<select name="colType">
				<option value=''>请选择</option>
				<option value='低龄'>低龄</option>
				<option value='高中'>高中</option>
				<option value='非美'>非美</option>
			</select>
		</td>
	</tr>
	<tr>
		<td	style='text-align:right'>国家</td>
		<td>
			<select name="countryCode">
				<option value=''>请选择</option>
				<option value='US'>US</option>
				<option value='AU'>澳洲</option>
				<option value='NZ'>新西兰</option>
				<option value='CA'>加拿大</option>
				<option value='GB'>英国</option>
				<option value='SG'>新加坡</option>
			</select>
		</td>
	</tr>
	<tr>
		<td	style='text-align:right'>Level</td>
		<td>
			<select name="colLevel">
				<option value=''>请选择</option>
				<option value='主推'>主推</option>
			</select>
		</td>
	</tr>
	<tr>
		<td></td>
		<td id='msg'></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<input type="button" value="导入" onclick='ajaxImport()'/>
			<b>${msg}</b>
		</td>
	</tr>
</table>			
</form>  
</body>  
</body>
</html>