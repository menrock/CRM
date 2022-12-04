<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>合同转案审核</title>
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
			url : 'auditListData.do',
			striped:true,
			rownumbers : true,
			pagination : true,
			pageSize : 15,
			pageList: [15,30,50,100,200],
			idField : 'id',
			queryParams: {tranFlag:1},
			frozenColumns : [ [ 
			{
				field : 'id',
				checkbox: true
			},{
				width : 100,
				title : '归属分公司',
				align:'center',
				field : 'c.company_id',
				sortable: true,
				formatter: function(value,row,index){
					return row.companyName;
				} 
			},{
				width : 110,
				title : '合同号',
				align:'center',
				field : 'c.con_no',
				sortable: true,
				formatter: function(value,row,index){
					return "<a href='javascript:void(0)' onclick=\"tranApplyApprove('" +row.id + "')\">" + row.conNo + "</a>";
				} 
			},{
				width : 90,
				title : '学生姓名',
				align:'center',
				field : 'c.cstm_id',
				sortable: true,
				formatter: function(value,row,index){
					return row.cstmName;
				} 
			},{
				width : 120,
				title : '申请时间',
				align:'center',
				field : 'submitTime',
				formatter:function(value,row,index){
					if(row.submitTime !=null){
				    	var d = new Date(row.submitTime);
				    	return d.format('yyyy-MM-dd HH:mm');
				    }
				}
			},{
				width : 100,
				title : '申请人',
				align:'center',
				field : 'creator_id',
				formatter:function(value,row,index){
				    return row.creatorName;
				}
			},{
				width : 280,
				title : '备注',
				align:'center',
				field : 'apply_memo',
				formatter:function(value,row,index){
				    return row.applyMemo;
				}
			},{
				width : 115,
				title : '审批时间',
				align:'center',
				field : 'approvedTime',
				formatter:function(value,row,index){
					if(row.approvedTime !=null){
				    	var d = new Date(row.approvedTime);
				    	return d.format('yyyy-MM-dd HH:mm');
				    }
				}
			},{
				width : 80,
				title : '审批人',
				align:'center',
				field : 'approver_id',
				formatter:function(value,row,index){
				    return row.approverName;
				}
			},{
				width : 80,
				title : '状态',
				align:'center',
				field : 'status',
				formatter:function(value,row,index){
				    return row.statusName;
				}
			}
			] ],
			toolbar : '#toolbar'
		});
	});
	  
	function jsQuery() {
		$.ajax({
			type:'post',
			url:'/hasLogin',
			error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if(result.errorCode !=200){
            		alert('系统异常');
            		return;
            	}
            	if( !result.data ){
            		$.messager.show( {title: 'Error',msg:'登录过期，请重新<a href="/" target=_top>登录</a>'} );
            		return;
            	}
            	grid.datagrid('clearSelections');
            	var params =  $.serializeObject($('#searchForm'));
            	//grid.datagrid({queryParams:params});
            	grid.datagrid('load', params); 
            }
		});  
	}	
	
	
	function jsApprove(agreeFlag){
		var params =  $.serializeObject($('#applyForm'));
		params.agreeFlag = agreeFlag;
		
		$.ajax({
			type:'post',
			url:'approve.do',
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
               		if(agreeFlag)
               			$.messager.show( {title: '提醒消息',msg:'通过转案申请'} );
               		else
               			$.messager.show( {title: '提醒消息',msg:'驳回转案申请'} );	 
               	}
        	}
		});	
	}
	
	function tranApplyApprove(id){
		$.ajax({
        	type:'post',
        	url:'applyInfo.do',
        	data:{id:id},
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	$('#applyForm').form('clear');
            	$('#applyForm').form('load', result.data);
            	
            	/*
            	if(result.data.submitTime){
            		var d = new Date(result.data.signDate);
            		$('#contractForm').find('input[name=signDate]').val( d.format('yyyy-MM-dd') );
            	}else{
            		$('#contractForm').find('input[name=signDate]').val('');
            	}*/
            	
            	$('#dlgApply').dialog('center').dialog('open');
        	}
		});
	}
</script>	
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>合同信息</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" >
	<table border="0">
		<tr> 
			<td style="width: 75px;text-align:right">公司</td>
			<td style="width: 140px">
				<select name='companyId' id="companyId" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 70px;text-align:right">姓名</td>
			<td style="width: 150px">
				<input name="cstmName" id="cstmName" type="text" maxlength="30" style="width:100px;" />
			</td>
			<td style="width: 70px;text-align:right">申请时间</td>
			<td style="width:200px">
				<input name="submitFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="submitTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td></td>
		</tr>
		<tr>
			<td style="text-align:right">审核状态</td>
			<td >
				<select name='statusList'>
					<option value=''>All</option>
					<option value='SUBMIT' selected>待审核</option>
					<option value='PASS'>通过</option>
					<option value='REJECTED'>驳回</option>
				</select>
			</td>
			<td style="text-align:right">合同号</td>
			<td >
				<input name="conNo" id="conNo" type="text" maxlength="50" style="width:100px;" />
			</td>	
			<td align="right">审批时间</td>
			<td>
			  <input name="approvedFrom" id="createdFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="approvedTo"   id="createdTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td> 
			<td >
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" >查询</a> 
			</td>
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>

<div id='dlgApply' class="easyui-dialog" style="width:650px;height:320px;padding:10px"
	 data-options="closed:true,title:'转案审核',modal:true,
			buttons:[{
				text:'通过审核',
				handler:function(){ jsApprove(true);}
			},{
				text:'驳回',
				handler:function(){ jsApprove(false);}
			},{
				text:'关闭',
				handler:function(){$('#dlgApply').dialog('close');}
			}]">
	<form name='applyForm' id='applyForm'>	
		<input type='hidden' name='id'>
		<input type='hidden' name='tranType'>
	<table>
		<tr>
			<td style='width:60px;text-align:right'>合同号</td>
			<td style='width:140px;'><input type='text' name='conNo' readonly style='width:110px'/></td>
			<td style='width:60px;text-align:right'>学生姓名</td>
			<td>
				<input name="cstmName" readonly type="text" style="width:110px;"/>
			</td>
		</tr>
		<tr>		
			<td style='text-align:right'>规划顾问</td>
			<td><input type="text" name="toPlanGwName" style="width:110px;" readonly="readonly">
				<input type="hidden" name="toPlanGwId" >
			</td>
			<td style='text-align:right'>申请顾问</td>
			<td><input type="text" name="applyGwName" style="width:110px;" readonly="readonly">
				<input type="hidden" name="applyGwId" >
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>写作顾问</td>
			<td><input type="text" name="toWriteGwName" style="width:110px;" readonly="readonly">
				<input type="hidden" name="toWriteGwId" >
			</td>
			<td style='text-align:right'>后续顾问</td>
			<td><input type="text" name="toServiceGwName" style="width:110px;" readonly="readonly">
				<input type="hidden" name="toServiceGwId" >
			</td>
		</tr>
	</table>
	</form>
</div>	
</body>
</html>