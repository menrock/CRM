<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>资源统计报表</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/extIcon.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
	<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
	<script type="text/javascript" src="/_resources/js/date.js"></script>
	<script type="text/javascript" src="/_resources/My97DatePicker/WdatePicker.js"></script>
<script language='javascript'>
	function jsStat(szStatBy){
		$('#statBy').val(szStatBy);
		$('#statForm').submit();
	}
	
	function selStuFrom(){
		var roots = $('#fromTree').tree('getRoots');
		if(roots == null || roots.length ==0){	
			$('#fromTree').tree({
				lines:true,
				url : '/dict/treeData?rootCode=stufrom',
				onClick : function(node) {						
					var pNode = $('#fromTree').tree('getParent',node.target);
					var szTxt = node.text;
					if(pNode) szTxt = pNode.text + "-" + szTxt;
					
					$('input[name=stuFromCode]').val(node.code);
					$('input[name=stuFromName]').val(szTxt);
					$('#divFromTree').dialog('close');
				}
			});
		}
		$('#divFromTree').dialog('open');
    }
    function clearStuFrom(){
		$('input[name=stuFromCode]').val('');
		$('input[name=stuFromName]').val('');
		$('#divFromTree').dialog('close');
    }
</script>
	
</head>
<body >

<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>资源统计报表</td></tr>
</table>
<form name="statForm" id="statForm" method="post" action="resourceExcel">
	<table border="0">
		<tr> 
			<td style="width:80px;text-align:right">分公司</td>
			<td style="width:120px">
				<select name='company_id' id="company_id" style="width:115px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width:80px;text-align:right">录入日期</td>
			<td style="width:220px">
			  <input name="created_from" id="created_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="created_to"   id="created_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="width:80px;text-align:right">分配时间</td> 
			<td style="width:280px">
				<input name="assign_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="assign_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">咨询渠道</td>
			<td colspan='3'>
				<input type='text' name="stuFromName" style='width:220px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromCode" value="">
			</td>	
			<td colspan='2'>
				<a href="javascript:void(0);" class="easyui-linkbutton" style="margin-left: 20px;" 
				  onclick="jsStat('byFrom')" >按渠道统计</a> 
				
				<a href="javascript:void(0);" class="easyui-linkbutton" style="margin-left: 0px;" 
				  onclick="jsStat('byZxgw')" >按顾问统计</a> 
				<input type='hidden' name="statBy" id="statBy">
			</td>
		</tr>
	</table>
</form>

<div id='divFromTree' class="easyui-dialog" data-options="closed:true,title:'咨询渠道选择',buttons:[{
				text:'clear',
				handler:clearStuFrom
			}]" style="width:500px;height:360px;padding:10px">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>	
</body>
</html>