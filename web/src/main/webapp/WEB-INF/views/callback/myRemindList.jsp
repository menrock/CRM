<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.niu.crm.model.ZxgwCallbackRemind" %>
<%@ page import="com.niu.crm.model.type.CallbackRemindType" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>我的回访提醒</title>
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
	var grid;
  
	$(function() {	
		grid = $('#grid').datagrid({
				title : '',
				url: 'listMyRemindData.do',
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100,200],
				idField : 'id',
				frozenColumns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : '85',
					title : '公司',
					align:'center',
					field : 'a.company_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.student.companyName;
					} 
				},{
					width : '85',
					title : '姓名',
					align:'center',
					field : 'c.name',
					sortable: true,
					formatter: function(value,row,index){
						var szName = row.cstmName;
						if(szName == '') szName = '未录入';
					  return "<a href='/lx/student/main.do?id=" + row.stuId +"' target='_blank'>" + szName + "</a>"; 
					} 
				}] ],
				columns : [ [ 
				{
					width : 180,
					title : '咨询渠道',
					halign:'center',
					field : 'stu_from_id',
					sortable: true,
					formatter: function(value,row,index){return row.student.stuFromName; } 
				},{
					width : 140,
					title : '意向国家',
					halign:'center',
					field : 'plan_country',
					sortable: true,
					formatter: function(value,row,index){return row.student.planCountry; } 
				},{
					width : 80,
					title : '申请学历',
					halign:'center',
					field : 'plan_xl',
					sortable: true,
					formatter: function(value,row,index){return row.student.planXl; } 
				},{
					width : 80,
					title : '所在城市',
					align:'center',
					field : 'stu_city',
					sortable: true,
					formatter: function(value,row,index){return row.student.stuCity; } 
				},{
					width : 100,
					title : '顾问评级',
					align:'center',
					field : 'zxgw.stu_level',
					sortable: true,
					formatter: function(value,row,index){return row.stuZxgw.stuLevelName; } 
				},{
					width : '80',
					title : '提醒对象',
					halign:'center',
					field : 'zxgw.zxgw_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.stuZxgw.zxgwName;
					}
				},{
					width : 90,
					title : '提醒类型',
					halign:'center',
					field : 'r.remind_type',
					sortable: true,
					formatter: function(value,row,index){
					    return row.remindTypeName; 
					}
				},{
					width : 120,
					title : '截止时间',
					halign:'center',
					field : 'r.latest_contact_time',
					sortable: true,
					formatter: function(value,row,index){
						if(row.latestContactTime){
							var d = new Date(row.latestContactTime);
							return d.format('yyyy-MM-dd HH:mm');
						}	 
					}
				},{
					width : 120,
					title : '回访时间',
					halign:'center',
					field : 'r.contact_time',
					sortable: true,
					formatter: function(value,row,index){
						if(row.contactTime){
							var d = new Date(row.contactTime);
							return d.format('yyyy-MM-dd HH:mm');
						}	 
					}
				},{
					width : 120,
					title : '提醒时间',
					halign:'center',
					field : 'r.created_at',
					sortable: true,
					formatter: function(value,row,index){
						var d = new Date(row.createdAt);
					    return d.format('yyyy-MM-dd HH:mm'); 
					}
				}
				] ],
				toolbar : '#toolbar'
			});
			
			jsQuery();
	  });
	  
	function jsQuery() {
		grid.datagrid('clearSelections');
	
		var params =  $.serializeObject($('#searchForm'));
		if(params.latest_contact_to == ''){
			params.latest_contact_to = (new Date()).format('yyyy-MM-dd') + ' 23:59';
		}
		//params.[[${_csrf.parameterName}]] = '[[${_csrf.token}]]';
		grid.datagrid({url : 'listMyRemindData.do','queryParams': params}); 
	};	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>回访提醒</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" >
	<table border="0">
		<tr>
			<td style="width:70px;text-align:right">提醒类型</td>
			<td style="width: 120px">
				<select name='callbackType'>
					<option value=''>全部</option>
				<%
				  CallbackRemindType[] remindTypes = CallbackRemindType.values();	
				  for(CallbackRemindType typeItem:  remindTypes){
					out.println("<option value='" + typeItem.name() + "'>" + typeItem.getName() + "</option>");  
				  }
				%>	
				</select>	
			</td>
			<td style="text-align:right">回访状态</td>
			<td style="width: 200px">
				<select name='callback_status'>
					<option value=''>全部</option>
					<option value='none'>未回访</option>
					<option value='ontime'>及时回访</option>
					<option value='delayed'>逾期</option>
				</select>
			</td>
			<td align="right">最迟回访</td>
			<td>
			  <input name="latest_contact_from" id="latest_contact_from" readonly type="text" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />到
			  <input name="latest_contact_to"   id="latest_contact_to"  readonly type="text" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />
			</td>
			<td colspan='2'>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
			</td>
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>
</body>
</html>