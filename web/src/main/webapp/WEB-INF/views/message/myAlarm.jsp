<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.niu.crm.model.type.MessageType" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>系统消息</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/color.css">
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
				url : '/alarm/myAlarmData.do',
				queryParams:{},
				striped:true,
				rownumbers : true,
				idField : 'id',
				columns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : 485,
					title : '内容',
					align:'left',
					field : 'content',
					formatter: function(value,row,index){
						return row.content;
					} 
				},{
					width : 185,
					title : '提醒对象',
					align:'center',
					field : 'alarm_user_ids',
					formatter: function(value,row,index){
						return row.alarmUserIds;
					} 
				},{
					width : 150,
					title : '提醒方式',
					align:'center',
					field : 'alarmWayNames'
				},{
					width : 100,
					title : '提醒时间',
					align:'center',
					field : 'alarm_time',
					formatter: function(value,row,index){
						if(!row.alarmTime) return "";
					    return row.alarmTime;
					} 
				},{
					width : 70,
					title : '重复方式',
					align:'center',
					field : 'repeat_type',
					formatter: function(value,row,index){
						return row.repeatType;
					} 
				},{
					width : '120',
					title : '重复次数',
					align:'center',
					field : 'repeatDays'
				}
				] ],
				toolbar : '#toolbar'
		});
	}); 	
	
	function jsDelete(){
		var rows = $('#grid').datagrid("getSelections");
		if(rows.length ==0){
			$.messager.alert('消息','请选择要删除的信息');
			return;
		}
		
		$.messager.confirm('确认','确认删除?',function(r){
			if (r)
				realDelete(rows);
		});
	}
	
	function realDelete(rows){
		var ids = '';
		for(i=0; i < rows.length; i++){
			if(i==0)
				ids = rows[i].id;
			else
				ids += "," + rows[i].id;
		}
		$.ajax({
        	type:'post',
        	url:'delete.do',
        	data:{ids:ids},
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
                $('#grid').datagrid('reload');
        	}
		});
	}
	
	function jsCreate(){
		$('#alarmForm').form('clear');
		
		$('#divAlarm').dialog('center');
		$('#divAlarm').dialog('open');
	}
	
	function submitAlarm(){	
		var params =  $.serializeObject($('#alarmForm'));	
		
		$.ajax({
        	type:'post',
        	url:'/alarm/submit.do',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if (result.errorCode != 200){
            		$.messager.show({
                    	title: 'Error',
                        msg: result.error
                    });
                } else {
                	$('#grid').datagrid('reload');
                	$('#divAlarm').dialog('close');     // close the dialog
                }
        	}
		});
	}
</script>
</head>
<body data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
	<a href='javascript:void(0)' onclick='jsDelete();' class="easyui-linkbutton" data-options="iconCls:'icon-remove'" 
				style="margin-left: 0px;" >删除</a> 
	<a href='javascript:void(0)' onclick='jsCreate();' class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				style="margin-left: 0px;" >新建</a> 
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>


<div id='divAlarm' class="easyui-dialog" style="width:550px;height:280px;padding:5px"
	 data-options="closed:true,title:'提醒信息',modal:true,
			buttons:[{
				text:'确定',
				handler:submitAlarm
			},{
				text:'关闭',
				handler:function(){$('#divAlarm').dialog('close');}
			}]">
	<form name='alarmForm' id='alarmForm'>		
	<table>
		<tr>
			<td style='width:60px;text-align:right'>提醒内容</td>
			<td style='width:440px;'>
				<textarea name='content' style='width:400px;height:80px'></textarea>
				<input type='hidden' name='id' />
			</td>
		</tr>
		<tr>	
			<td style='text-align:right'>提醒方式</td>
			<td >
				<input type='checkbox' name='alarm_way' value='1'><%= MessageType.getName(1) %>
				<input type='checkbox' name='alarm_way' value='2'><%= MessageType.getName(2) %>
				<input type='checkbox' name='alarm_way' value='4'><%= MessageType.getName(4) %>
			</td>
		</tr>
		<tr>	
			<td style='text-align:right'>提醒次数</td>
			<td >
				<select name='repeatType'>
					<option value='ONLY'>1次</option>
					<option value='WEEKLY'>每周</option>
					<option value='MONTHLY'>每月</option>
				</select>
			</td>
		</tr>
		<tr>	
			<td style='text-align:right'>提醒时间</td>
			<td >
				<input type='text' name='alarmDate' style='width:70px'>
				<input type='text' name='alarm_time' style='width:40px'>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>提醒对象</td>
			<td>
				<input type="text" name="alarmUserNames" style="width:280px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="selectUser()">...</a>
				<input type="hidden" name="alarmUserIds" >
			</td>
		</tr>
	</table>
	</form>
</div>	


<div id='divUsers' class="easyui-dialog" title="选择顾问" data-options="closed:true" style="width:400px;height:200px;padding:10px">
	公司<select name='companyId' onchange="searchUsers()">
		<option value=''>全部</option>
	<c:forEach var="item" varStatus="status" items="${companyList}">
		<option value='${item.id}' <c:if test="${item.id == stu.companyId}"> selected</c:if> >${item.name}</option>
	</c:forEach>]
		</select>
	姓名<input type='text' name='name' style='width:50px' />
	
	<a href='javascript:void(0)' onclick='searchUsers()'>查询</a>
	
	<br>
	<label class="label-top">顾问:</label>
    <select name='userId' style="width:120px;height:20px">
    </select>
        
    <input type='button' name='userOK' value='确定' onclick='userSelected()'>
</div>


<script language='javascript'>
	function selectUser(){
			
		$('#divUsers').dialog('center');
		$('#divUsers').dialog('open');	
	}
	
	function searchUsers(){
		var params = {};
		params.companyId = $('#divUsers').find("select[name=companyId]").val();
		params.enabled = 'true';
		params.name = $('#divUsers').find("input[name=name]").val();
		
		$.ajax({
			type: 'POST',
		 	url: '/user/listData.do',
		 	data: params,
		 	dataType: "json",
		 	error:function(){ alert('系统异常'); },
		 	success: function(json){
		 		$("#divUsers").find("select[name=userId]").empty();
		 		$("#divUsers").find("select[name=userId]").append("<option value=''>请选择</option>" ); 

		 		for(i=0; i < json.rows.length; i++){
		 			s = json.rows[i].id + "'>" + json.rows[i].name + "</option>";
		 			$("#divUsers").find("select[name=userId]").append("<option value='" + s ); 
		 		}
		 	}
		});
	}
	
	
	function userSelected(){
		var sel = $("#divUsers").find("select[name=userId]");
	
		$('#alarmForm').find("input[name=alarmUserIds]").val( $(sel).val() );
		if( $(sel).val() == '')
			$('#alarmForm').find("input[name=alarmUserNames]").val('');
		else	
			$('#alarmForm').find("input[name=alarmUserNames]").val( $(sel).find("option:selected").text() );
				
		$('#divUsers').dialog('close');
	}
</script>	
</body>
</html>