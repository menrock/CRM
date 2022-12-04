<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>我的资源共享</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
	<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
	<script type="text/javascript" src="/_resources/js/date.js"></script>
<script language='javascript'>
var grid;
$(function() {
	grid = $('#grid').datagrid({
		title : '资源共享',
		url : '/stushare/myListData.do',
		striped:true,
		rownumbers : true,
		pagination : true,
		pageSize : 15,
		pageList: [15,20,30],
		idField : 'id',
		columns : [ [ 
		{
			field : 'id',
			checkbox: true
		},{
			width : 120,
			title : '顾问(from)',
			align:'center',
			field : 'from_user_id',
			sortable: true,
			formatter: function(value,row,index){return row.fromUserName; } 
		},{
			width : 120,
			title : '顾问(to)',
			halign:'center',
			field : 'to_user_id',
			sortable: true,
			formatter: function(value,row,index){return row.toUserName; } 
		},{
			width : 120,
			title : '操作人',
			halign:'center',
			field : 'creator_id',
			formatter: function(value,row,index){return row.creatorName; } 
		},{
			width : 120,
			title : '录入时间',
			align:'center',
			field : 'created_at',
			formatter: function(value,row,index){
				if(!row.createdAt) return "";
			    var d = new Date(row.createdAt);
			    return d.format('yyyy-MM-dd hh:mm');
			} 
		}
		] ],
		toolbar: [{
			iconCls: 'icon-add',
			handler: openShareDiv
		},'-',{
			iconCls: 'icon-remove',
			handler: jsRemove
		}]
		});
});
	function jsRemove(){
		var selRow = $('#grid').datagrid("getSelected");
		if( null == selRow ){
			alert('请选择要操作的行');
			return;
		}
		
		$.messager.confirm('Confirm','确定删除?',function(r){
			if (!r)
				return;
				
			var szUrl = "cancel";
			$.ajax({
				type: 'POST',
				url: szUrl ,
				data: {id:selRow.id},
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						alert('删除成功');
						jsQuery();
					}else{
						alert('删除失败-' + json.error);
					}
				}
			});	
		}); 
	}
	  
	function jsQuery() {
		//grid.datagrid('clearSelections');
	/*
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		}); */
		var params =  $.serializeObject($('#searchForm'));
		//params.[[${_csrf.parameterName}]] = '[[${_csrf.token}]]';
		grid.datagrid('load', params); 
	};	
	
	function openShareDiv(){
		$('#divShare').dialog('open');	
	}
	
	function searchUsers(){
		var params = {};
		params.companyId = $('#divShare').find("select[name=companyId]").val();
		params.enabled = true;
		params.name = $.trim( $('#divShare').find("input[name=name]").val() );
		if( params.name == ''){
			$.messager.alert({title:'提醒',msg:'请录入姓名'});
			return;
		}
		
		$.ajax({
			type: 'POST',
		 	url: '/user/listData.do',
		 	data: params,
		 	dataType: "json",
		 	error:function(){ alert('系统异常'); },
		 	success: function(json){
		 		$('#tblUser').html('');
		 		if( json.rows.length == 0){
		 			$.messager.alert({title:'提醒',msg:'没找到顾问'});
		 			return;
		 		}
		 		for(i=0; i < json.rows.length; i++){
		 			var item = json.rows[i];
		 			if(i >5) break;
		 			var szName = item.name;
		 			
		 			trHtml = "<tr>"
		 			       + "<td>" + json.rows[i].companyName +"</td>"
		 			       + "<td>" + json.rows[i].unit.name +"</td>"
		 			       + "<td><a href='javascript:void(0)' onclick=\"doShare({id:'" + item.id + "',name:'" + szName + "'})\">"
		 			       + item.name + "</td></tr>"
		 			$('#tblUser').append(trHtml); 
		 		}
		 	}
		});
	}
	
	function doShare(props){
		var params={};
		params.fromUserId = '${user.id}';
		params.toUserId   = props.id;
		params.toUserName = props.name;
		if( params.toUserId == ''){
			alert('请选择顾问');
			return;
		}
		if(params.fromUserId== params.toUserId){
			$.messager.alert({title:'提醒信息',msg:'不能共享给自己'});
			return;
		}
		
		$.messager.confirm('操作确认', '确认和' + params.toUserName + '共享客户资源信息?', function(r){
			if (!r) return;
			
			$.ajax({
				type: 'POST',
				url: 'add.do',
				data: params,
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show({title:'成功提醒',msg:'操作成功'});
						jsQuery();
					}else{
						$.messager.show({title:'操作失败',msg:'操作失败'});
					}
					$('#divShare').dialog('close');
				}
			});
		});
	}	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	
<table id="grid" data-options="fit:true,border:false">
</table>
	
<div id='divShare' class="easyui-dialog" title="选择共享顾问" data-options="closed:true" style="width:400px;height:200px;padding:10px">
	公司<select name='companyId'>
		<option  value=''>请选择</option>
		<c:forEach var="item" items="${companyList}">
			<option value='${item.id}' >${item.name}</option>
		</c:forEach>
		</select>
	姓名<input type='text' name='name' style='width:60px' />	
	<a href='javascript:void(0)' onclick='searchUsers()'>查询</a>
	
	<br>
	<table id='tblUser'>
	</table>
</div>


</body>
</html>