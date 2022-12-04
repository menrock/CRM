<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>客户导入</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
<script language='javascript'>
	function jsImport(){
		if($('#companyId').val() ==''){
			$.messager.alert('提醒信息','请选择归属公司');
			return;
		}	
		if($('#file').val() ==''){
			alert('请选择导入文件');
			return;
		}
		var fileName = $('#file').val();
		var reg=/(.)?(csv)$/i;
		if( !reg.test(fileName)){
			alert('只支持csv文件');
			return;
		}
		if( $('#importForm').find("input[name=savingFlag]").val() ==1){
			alert('请不要重复点击');
			return;
		}
		
		$('#importForm').find("input[name=savingFlag]").val(1);
		$('#importForm').submit();
	}
	
	function selectUnit(){
		if($('#companyId').val() == ''){
			$.messager.alert('提醒信息','请选择归属公司');
			return;
		}
		var szUrl = '/unit/treeData?rootId=' + $('#companyId').val();
	
    	unitTree = $('#unitTree').tree({
			lines:true,
			url : szUrl,
			onClick : function(node) {	
				$('#importForm').find("input[name=unitId]").val(node.id);
				$('#importForm').find("input[name=unitName]").val(node.text);
				
				$('#divUnitTree').dialog('close');
			}
		});
		$('#divUnitTree').dialog('open');
    }  
</script>
</head>
<body >
<form id='importForm' action="doImportCust.do" method="post" enctype="multipart/form-data"> 
<input type='hidden' name='savingFlag' value="" /> <!-- 是否正在保存 -->
<table>
	<tr>
		<td style='width:70px;text-align:right'>归属公司</td>
		<td style='width:120px'>
			<select name='companyId' id='companyId'>
				<option value=''>选择</option>
			<c:forEach var="item" items="${companyList}">
				<option value='${item.id}' <c:if test="${user.companyId==item.id}">selected</c:if>>${item.name}</option>
			</c:forEach>
			</select>
		</td>
		<td style='width:70px;text-align:right'>归属部门</td>
		<td>
			<input type='text' name='unitName' style="width:330px;" value='${stu.unitName}' readonly/>
			<a href='javascript:void(0)' onclick='selectUnit()'>...</a>
			<input type='hidden' name='unitId' value='${stu.unitId}' />	
		</td>
	</tr>
	<tr>
		<td	style='text-align:right'>导入文件</td>
		<td colspan='3'><input type="file" name="file" id="file"/>(csv文件)</td>
	</tr>
	<c:if test="${count != null}">
	<tr>
		<td></td>
		<td colspan='3'>
			导入：${count}条;<br>
			导入信息:
			${msg}
		</td>
	</tr>
	</c:if>
	<tr>
		<td></td>
		<td>
			<input type="button" value="导入" onclick='jsImport()'/>
			<b>${msg}</b>
		</td>
	</tr>
</table>			
</form>  

<div id='divUnitTree' class="easyui-dialog" title="部门选择" data-options="closed:true" style="width:500px;height:420px;padding:10px">
	<ul id="unitTree" class="easyui-tree" ></ul>
</div> 
</body>  
</body>
</html>