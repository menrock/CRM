<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>资源共享</title>
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
		url : '/stushare/listData.do',
		striped:true,
		rownumbers : true,
		pagination : true,
		pageSize : 15,
			pageList: [10,20,30],
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
						return row.companyName;
					} 
				},{
					width : '85',
					title : '姓名',
					align:'center',
					field : 'name',
					sortable: true,
					formatter: function(value,row,index){
						var szName = row.customer.name;
						if(szName == '') szName = '未录入';
					  return "<a href='inquireRecord.do?id=" + row.id +"' target='_blank'>" + szName + "</a>"; 
					} 
				}] ],
				columns : [ [ 
				{
					width : 100,
					title : '顾问评级',
					align:'center',
					field : 'stu_level',
					sortable: true,
					formatter: function(value,row,index){return row.stuLevelName; } 
				},{
					width : 180,
					title : '咨询渠道',
					halign:'center',
					field : 'stu_from_id',
					sortable: true,
					formatter: function(value,row,index){return row.stuFromName; } 
				},{
					width : 140,
					title : '意向国家',
					halign:'center',
					field : 'plan_country',
					sortable: true,
					formatter: function(value,row,index){return row.planCountry; } 
				},{
					width : 80,
					title : '申请学历',
					halign:'center',
					field : 'planXl',
					formatter: function(value,row,index){return row.planXl; } 
				},{
					width : 80,
					title : '咨询日期',
					halign:'center',
					field : 'inquire_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.inquireDate) return "";
					    var d = new Date(row.inquireDate);
					    return d.format('yyyy-MM-dd'); 
					} 
				},{
					width : '80',
					title : '咨询顾问',
					halign:'center',
					field : 'a.zxgw_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.zxgwName;
					}
				},{
					width : '120',
					title : '分配时间',
					halign:'center',
					field : 'assign_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.assignDate) return "";
					    var d = new Date(row.assignDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					} 
				},{
					width : 120,
					title : '最后回访时间',
					align:'left',
					field : 'last_contact_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.lastContactDate) return "";
					    var d = new Date(row.lastContactDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					}
				},{
					width : 210,
					title : '最近回访内容',
					align:'left',
					field : 'lastContacts',
					formatter: function(value,row,index){
						if( !row.lastContactRecords )
						  return "";
						  
						szContacts = '';  
						for( idx=0; idx < row.lastContactRecords.length; idx++)
						{  
						   record = row.lastContactRecords[idx];
						   if(idx==0)
						     szContacts = record.contactText;
						   else
						     szContacts = szContacts + '<br>------------------<br>'
						                + record.contactText;
						}  
						return szContacts;
					} 
				},{
					width : 70,
					title : '回访次数',
					align:'right',
					field : 'contactCount'
				},{
					width : '120',
					title : '录入时间',
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
	
	function custRecycle(){
		var szUrl = "assign/recycle.do";		
		var selRows = $('#grid').datagrid("getSelections");
		if( null == selRows || 0 == selRows.length){
			alert('请选择要操作的行');
			return;
		}
		if( !confirm("确认回收?")) return;
		
		var stuIds ;
		for(var i=0; i < selRows.length; i++){
			if(i>0)
				stuIds+=","+ selRows[i].id;
			else 
				stuIds= selRows[i].id;
		}	  
		var params = {stuIds: stuIds};
				
		$.ajax({
		  type: 'POST',
		  url: szUrl ,
		  data: params,
		  dataType: "json",
		  error:function(){ alert('系统异常'); },
		  success: function(json){
		  	if(json.errorCode == 200){
		  		alert('客户回收成功');
		  		jsQuery();
		  	}else{
		  		alert('回事失败-' + json.error);
		  	}
		  }
		});
	}
	
	function selStuFrom(){
    	fromTree = $('#fromTree').tree({
			lines:true,
			url : '/dict/treeData?rootCode=stufrom',
			onClick : function(node) {						
				var pNode = $('#fromTree').tree('getParent',node.target);
				var szTxt = node.text;
					
				$('input[name=stuFromCode]').val(node.code);
				$('input[name=stuFromName]').val(szTxt);
				$('#divFromTree').dialog('close');
			}
		});
		$('#divFromTree').dialog('open');
    } 
	
	function selectUser(objName){
		$('#divUsers').find("input[name=stuIds]").val('');
		$('#divUsers').find("input[name=objName]").val(objName);
		openUsersDiv();
	}
	
	function openUsersDiv(){
		var szCompanyId = $('#searchForm').find("select[name=company_id]").val();
		
		$('#divUsers').find('select[name=companyId]').empty();
		$("#company_id option").each(function(){
			var optionHtml = '<option value="' + $(this).val() + '">' + $(this).html() + '</option>';
			$('#divUsers').find('select[name=companyId]').append(optionHtml);
		});
		
		$('#divUsers').find('input[name=companyId]').val();
		
		var params = {companyId:szCompanyId, "size":6, "pageSize":5,"page":1};
		if($('#divUsers').find("input[name=stuIds]").val() != '')
			$('#divUsers').find("select[name=enabled]").val('true');
		else
			$('#divUsers').find("select[name=enabled]").val('');
			
		$('#divUsers').dialog('open');	
		
	}
	
	function searchUsers(){
		var params = {};
		params.companyId = $('#divUsers').find("select[name=companyId]").val();
		params.enabled = $('#divUsers').find("select[name=enabled]").val();
		params.name = $('#divUsers').find("input[name=name]").val();
		
		$.ajax({
			type: 'POST',
		 	url: '/user/listData.do',
		 	data: params,
		 	dataType: "json",
		 	error:function(){ alert('系统异常'); },
		 	success: function(json){
		 		$("#divUsers").find("select[name=gwId]").empty();
		 		$("#divUsers").find("select[name=gwId]").append("<option value=''>请选择</option>" ); 

		 		for(i=0; i < json.rows.length; i++){
		 			s = json.rows[i].id + "'>" + json.rows[i].name + "</option>";
		 			$("#divUsers").find("select[name=gwId]").append("<option value='" + s ); 
		 		}
		 	}
		});
	}
	
	
	function userSelected(){
		var szStuIds = $('#divUsers').find('input[name=stuIds]').val();
		var objName  = $('#divUsers').find('input[name=objName]').val();
		if(szStuIds == ''){
			var sel = $("#divUsers").find("select[name=gwId]");
			$('#searchForm').find("input[name=" + objName + "_id]").val( $(sel).val() );
			if( $(sel).val() == '')
				$('#searchForm').find("input[name=" + objName + "_name]").val('');
			else	
				$('#searchForm').find("input[name=" + objName + "_name]").val( $(sel).find("option:selected").text() );
				
		 	$('#divUsers').dialog('close');
		}
		else{
			doAllocate();
		}	
	}
	
	function jsDelete(){
		var selRow = $('#grid').datagrid("getSelected");
		if( null == selRow ){
			alert('请选择要操作的行');
			return;
		}
		
		$.messager.confirm('Confirm','确定删除?',function(r){
			if (!r)
				return;
				
			var szUrl = "delete.do";
			$.ajax({
				type: 'POST',
				url: szUrl ,
				data: {id:selRow.id},
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show( {title: '提示信息',msg:'删除成功'} ); 
						jsQuery();
					}else{
						$.messager.alert('错误提醒','删除失败-' + json.error);
					}
				}
			});	
		}); 
	}
	
	function custAllocate(){
		var selRows = $('#grid').datagrid("getSelections");
		if( null == selRows || 0 == selRows.length){
			alert('请选择要操作的行');
			return;
		}
		
		var stuIds ;
		for(var i=0; i < selRows.length; i++){
			if(i>0)
				stuIds+=","+ selRows[i].id;
			else 
				stuIds= selRows[i].id;
		}
		
		$('#divUsers').find("input[name=stuIds]").val(stuIds);
		openUsersDiv();
	}
	
	function doAllocate(){
		var params={};
		params.stuIds = $("#divUsers").find("input[name=stuIds]").val();
		params.zxgwId = $("#divUsers").find("select[name=gwId]").val();
		if( params.zxgwId == ''){
			alert('请选择顾问');
			return;
		}
		
		if( !confirm("确认分配?")) return;
		
		var szUrl = "assign/allocate.do";
		$.ajax({
		  type: 'POST',
		  url: szUrl ,
		  data: params,
		  dataType: "json",
		  error:function(){ alert('系统异常'); },
		  success: function(json){
		  	
		  	if(json.errorCode == 200){
		  		alert('分配成功');
		  		jsQuery();
		  	}else{
		  		alert('分配失败-' + json.error);
		  	}
		  	$('#divUsers').dialog('close');
		  }
		});
	}	
	</script>
	
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

	<div id="toolbar" style="display: none">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>资源管理</td></tr>
</table>
	<form name="searchForm" id="searchForm" method="post" >
	<table width="100%" border="0">
		<tr> 
			<td style="width: 80px;text-align:right">公司</td>
			<td style="width: 150px">
				<select name='company_id' id="company_id" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 80px;text-align:right">姓名</td>
			<td style="width: 200px">
				<input name="name" id="name" type="text" maxlength="50" style="width:80px;" />
				<select name="gender">
					<option value="">请选择</option>
					<option value="M" >男</option>
					<option value="F" >女</option>
				</select>	
			</td>
			<td style="width: 80px;text-align:right">联系电话</td>
			<td style="width: 210px"><input name="phone" type="text" maxlength="20" style="width:105px;" /></td>					
			<td style="width: 80px;text-align:right">咨询顾问</td>
			<td>
				<input type='text' name='zxgw_name' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('zxgw')">...</a>
				<input type="hidden" name="zxgw_id" >
			</td>
		</tr>
		<tr>
			<td align="right">签约状态</td>
			<td>
				<select name="sign_status" style="width:105px;">
					<option value="">全部</option>
					<option value="2" >已签约</option>
					<option value="1" >未签约</option>
				</select>
			</td>		
			<td align="right">录入日期</td>
			<td>
			  <input name="created_from" id="created_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="created_to"   id="created_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td align="right">首次咨询</td>
			<td>
				<input name="inquire_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="inquire_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="text-align:right">录入人</td>
			<td>
				<input type='text' name='creator_name' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('creator')">...</a>
				<input type="hidden" name="creator_id" >
			</td>	 	    
		</tr>
		<tr>
			<td align="right">毕业/在读学校</td>
			<td>
				<input name="curr_school" type="text" style="width:105px;" />
			</td>
			<td style="text-align:right">咨询渠道</td>
			<td >
				<input type='text' name="stuFromName" style='width:150px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromCode" value="">
			</td>
			<td align="right">分配时间</td> 
			<td>
				<input name="assign_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="assign_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
		</tr>
		<tr>	
			<td style="text-align:right">意向国家</td>
			<td>
			  <input name="plan_country" style="width:105px;" />
			</td>
			<td style="text-align:right">申请学历</td>
			<td>
				<select id="plan_xl" name="plan_xl" class="easyui-combotree" panelWidth="200" style="width:160px;" multiple="multiple"></select>				
			</td>
			<td align="right">最后回访</td>
			<td >
				<input name="last_contact_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="last_contact_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;"/>
			</td>
		</tr>		
		<tr>
			<td align="right">顾问评级</td>
			<td colspan='7'>
				<select id="stu_level" name="stu_level" class="easyui-combotree" panelWidth="480" style="width:450px;" multiple="multiple"></select>
				&nbsp;
			
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
				
				<a href="add.do" class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				style="margin-left: 0px;" >新建</a> 
				
				<a href="javascript:;" onclick="custAllocate()" class="easyui-linkbutton" data-options="iconCls:'icon-crm-allocate'" 
				style="margin-left: 0px;" >分配</a> 
				<a href="javascript:;" onclick="custRecycle()" class="easyui-linkbutton" data-options="iconCls:'icon-crm-recycle'" 
				style="margin-left: 0px;" >回收</a> 
				<a href='javascript:;' onclick='jsDelete()'>删除</a>
				<input type='checkbox' name='fuzzySearch' checked='true'/>模糊查询
			</td>
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>
	
<div id='divUsers' class="easyui-dialog" title="选择顾问" data-options="closed:true" style="width:400px;height:200px;padding:10px">
	公司<select name='companyId' onchange="searchUsers()">
		</select>
	姓名<input type='text' name='name' style='width:50px' />
	Enabled
	<select name='enabled'>
		<option value=''>全部</option>
		<option value='true'>Y</option>
		<option value='false'>N</option>
	</select>
	<a href='javascript:void(0)' onclick='searchUsers()'>查询</a>
	
	<br>
	<label class="label-top">顾问:</label>
    <select name='gwId' style="width:120px;height:20px">
    </select>
    <input type='hidden' name='stuIds'>
    <input type='hidden' name='objName'>
        
    <input type='button' name='userOK' value='确定' onclick='userSelected()'>
</div>
<div id='divFromTree' class="easyui-dialog" title="咨询渠道选择" data-options="closed:true" style="width:500px;height:360px;padding:10px">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>
</body>
</html>