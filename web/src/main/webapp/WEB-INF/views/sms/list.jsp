<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
				url : 'listData.do',
				queryParams:{},
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100,200],
				idField : 'id',
				columns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : 85,
					title : '收件人',
					align:'center',
					field : 'mobile'
				},{
					width : 485,
					title : '内容',
					align:'left',
					field : 'content',
					formatter: function(value,row,index){
						return row.content;
					} 
				},{
					width : 85,
					title : '发件人',
					align:'center',
					field : 'sender_id',
					formatter: function(value,row,index){
						return row.senderName;
					} 
				},{
					width : '120',
					title : '发送时间',
					align:'center',
					field : 'createdAt',
					formatter: function(value){
						if(!value) return "";
					    var d = new Date(value);
					    return d.format('yyyy-MM-dd hh:mm');
					} 
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
</script>
</head>
<body data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
	账户余额:${smsCapacity}
	<a href='javascript:void(0)' onclick='jsDelete();' class="easyui-linkbutton" data-options="iconCls:'icon-remove'" 
				style="margin-left: 0px;" >删除</a> 
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>
</body>
</html>