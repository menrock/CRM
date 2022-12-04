<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>佣金账单</title>
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
			url : 'invListData.do',
			striped:true,
			rownumbers : true,
			pagination : true,
			pageSize : 15,
			pageList: [15,30,50,100,200],
			idField : 'id',
			columns : [ [ 
			{
				field : 'id',
				checkbox: true
			},{
				width : 95,
				title : '账单编号',
				align:'center',
				field : 'inv_no',
				formatter: function(value,row,index){
					return row.invNo; 
				} 
			},{
				width : 220,
				title : '院校/机构名称',
				align:'center',
				field : 'object_name',
				formatter: function(value,row,index){
					return row.objectName;
				} 
			},{
				width : 85,
				title : '币种',
				align:'center',
				field : 'inv_currency',
				formatter: function(value,row,index){
					return row.invCurrency; 
				} 
			},{
				width : 85,
				title : '账单金额',
				align:'right',
				field : 'inv_amount',
				formatter: function(value,row,index){
					return row.invAmount.toFixed(2); 
				} 
			},{
				width : 85,
				title : '寄出日期',
				align:'center',
				field : 'sent_date',
				formatter: function(value,row,index){
					if(!row.sentDate) return "";
				    var d = new Date(row.sentDate);
				    return d.format('yyyy-MM-dd');
				} 
			},{
				width : 85,
				title : '到款日期',
				align:'center',
				field : 'received_date',
				formatter: function(value,row,index){
					if(!row.receivedDate) return "";
				    var d = new Date(row.receivedDate);
				    return d.format('yyyy-MM-dd'); 
				} 
			},{
				width : 90,
				title : '收款金额',
				align : 'center',
				halign: 'center',
				field : 'received_amount',
				formatter: function(value,row,index){
					if(row.receivedAmount !=null)
						return row.receivedAmount.toFixed(2);	
				}
			},{
				width : 90,
				title : '状态',
				align : 'center',
				halign: 'center',
				field : 'status',
				formatter: function(value,row,index){
					return row.statusName;	
				}
			},{
				width : 100,
				title : '录入时间',
				align:'center',
				field : 'created_at',
				formatter: function(value,row,index){
					if(!row.createdAt) return "";
				    var d = new Date(row.createdAt);
				    return d.format('yyyy-MM-dd HH:mm'); 
				} 
			}
			] ],
			toolbar : '#toolbar'
		});
	});	
	  
	function jsQuery() {
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
		
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
            	grid.datagrid({queryParams:params});
            }
		});  
	}
	
	function editInvoice(id){
		$('#invoiceForm').form('clear');
		if(id == null){
			$('#invoiceDlg').dialog('open').dialog('center').dialog('setTitle','新建佣金账单');
			return;
		}
		
		
		$.ajax({
			type:'post',
			url:'invoiceInfo.do',
			data:{id:id},
			error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	var data = result.data;
            	$('#invoiceForm').find("[name=id]").val(data.id);
            	$('#invoiceForm').find("[name=invNo]").val(data.invNo);
            	$('#invoiceForm').find("[name=collegeId]").val(data.collegeId);
            	$('#invoiceForm').find("[name=orgId]").val(data.orgId);
            	$('#invoiceForm').find("[name=objectName]").val(data.objectName);
            	
            	$('#invoiceDlg').dialog('open').dialog('center').dialog('setTitle','账单编辑');
            }
		});
        
    }

	function jsDelete(){
		alert('delete');
	}
	function jsExport(){
		alert('export');
	}
	
	function saveInvoice(){
    	if( !$('#invoiceForm').form('validate') )
        	return;        		
        var params =  $.serializeObject($('#invoiceForm'));
        if(params.collegeId == '' && params.orgId == ''){
        	$.messager.show( {title: '提醒信息',msg:'请选择院校/机构'} ); 
        	return;
        }
        if(params.invCurrency == '' ){
        	$.messager.show( {title: '提醒信息',msg:'请选择币种'} ); 
        	return;
        }
        
        $.ajax({
        	type:'post',
        	url:'saveInvoice.do',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
			},
			success: function(result){
				if (result.errorCode != 200){
					$.messager.show({title: 'Error', msg: result.error});
                } else {
                	$.messager.show({title: '消息', msg: '保存成功'});
                }
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
  <tr><td width=100% align='center' height=30 class='title'>留学佣金账单</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" action="exportData.do">
	<table border="0">
		<tr> 
			<td style="width:60px;text-align:right">寄出日期</td>
			<td style="width:200px">
				<input name="skDateFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="skDateTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>	
			<td style="width:70px;text-align:right">院校/机构</td>
			<td style="width:140px">
				<input type='text' name='objectName' style="width:120px">
			</td>
			<td style="width:70px;text-align:right">状态</td>
			<td style="width:140px">
				<select name='status' >
				<option value=''>请选择</option>
			<c:forEach var="item" items="${statusList}">
				<option value='${item}' >${item.name}</option>
			</c:forEach>
				</select>
			</td>
			<td style="width:70px;text-align:right"></td>
		</tr>
		<tr>
			<td style="width:60px;text-align:right">到款日期</td>
			<td style="width:200px">
				<input name="receivedDateFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="receivedDateTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>	
			<td></td>
			<td colspan='3'>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				  onclick="jsQuery()" >查询</a>
			<c:if test="${canInputInvoice}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				  onclick="editInvoice(null)" >新建</a>  
			</c:if>		
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" 
				  onclick="jsDelete()" >删除</a>  
			<c:if test="${canExport}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext_page_excel'" 
				  onclick="jsExport()" style="margin-left: 0px;" >导出</a> 
			</c:if>	
			</td>
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>
	
<div id='invoiceDlg' class="easyui-dialog" style="width:600px;height:360px;padding:5px 5px"
            closed="true" buttons="#invoiceDlg-buttons">        
    <form id="invoiceForm" method="post">
    	<input type='hidden' name="id" />
    <table>
        <tr>
        	<td style='width:80px;text-align:right'>账单编号</td>
        	<td style='width:150px;'>
        		<input type='text' style='width:120px' name='invNo' readonly'>
        	</td>
        	<td style='width:80px;text-align:right'>账单对象</td>
        	<td style='width:150px;'>
        		<select name='objectType'>
        			<option value=''>选择</option>
        			<option value='college'>院校</option>
        			<option value='coopOrg'>合作机构</option>
        		</select>
        	</td>
        </tr>	
        <tr>
        	<td style='width:80px;text-align:right'>院校</td>
        	<td>
        		<input type='text' name='collegeId' >
        	</td>
        	<td style='width:80px;text-align:right'>机构</td>
        	<td>
        		<input type='text' name='orgId'>
        	</td>
        </tr>	
        <tr>
        	<td style='width:80px;text-align:right'>币种</td>
        	<td>
        		<input type='text' name='invCurrency' >
        	</td>
        </tr>	
    </table>    
	</form>
</div>
<div id="invoiceDlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveInvoice()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#invoiceDlg').dialog('close')" style="width:90px">关闭</a>
</div>	
	
</body>
</html>