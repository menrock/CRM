<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>邀约面访</title>
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
			url : 'listData.do',
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
				width : 120,
				title : '公司',
				align:'center',
				field : 'b.company_id',
				sortable: true,
				formatter: function(value,row,index){
					return row.companyName;
				} 
			},{
				width : 85,
				title : '客户姓名',
				align:'center',
				field : 'cstm_id',
				sortable: true,
				formatter: function(value,row,index){
					var szName = row.cstmName;
					if(szName == '') szName = '未录入';
					return szName; 
				} 
			},{
				width : 90,
				title : '预约顾问',
				align:'center',
				field : 'gw_id',
				sortable: true,
				formatter: function(value,row,index){
					return row.gwName; 
				} 
			},{
				width : 120,
				title : '预约时间',
				halign:'center',
				align : 'center',
				field : 'appointment_time',
				sortable: true,
				formatter: function(value,row,index){
					if( row.appointmentTime != null ){
						var d = new Date( row.appointmentTime );
					    return d.format('yyyy-MM-dd hh:mm');
					}    
				} 
			},{
				width : 280,
				title : '预约备注',
				halign:'center',
				align : 'center',
				field : 'appointment_memo',
				formatter: function(value,row,index){
					if( row.appointmentMemo != null )
					    return row.appointmentMemo;
				} 
			},{
				width : 120,
				title : '到访时间',
				halign:'center',
				align : 'center',
				field : 'visit_time',
				sortable: true,
				formatter: function(value,row,index){				
					if( row.visitTime != null ){
						var d = new Date( row.visitTime );
					    return d.format('yyyy-MM-dd hh:mm');
					}    
				} 
			},{
				width : 280,
				title : '到访备注',
				halign:'center',
				align : 'center',
				field : 'visit_memo',
				formatter: function(value,row,index){
					if( row.visitMemo != null )
					    return row.visitMemo;
				} 
			}] ],
			onDblClickRow	:function(index,row){
				editVisit(row);
			},
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
	
	function editVisit(row){
		$('#visitForm').form('clear');
		
		var props = row;
		
		var d = new Date( row.appointmentTime );
		props.appointmentTime = d.format('yyyy-MM-dd hh:mm');
		
		if( row.visitTime !=null ){
			d = new Date( row.visitTime );
			props.visitTime = d.format('yyyy-MM-dd hh:mm');
		}
		
		$('#visitForm').form('load',props);
		$('#visitDlg').dialog('open').dialog('center').dialog('setTitle','到访信息');
	}
	
	function saveVisit(){
        var params =  $.serializeObject($('#visitForm'));
        
        
        $.ajax({
        	type:'post',
        	url:'saveVisit.do',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
			},
			success: function(result){
				if (result.errorCode != 200){
					$.messager.show({title: 'Error', msg: result.error});
                } else {
                	$('#inviteForm').find("[name=id]").val(result.data.id);
                	jsQuery();
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
  <tr><td width=100% align='center' height=30 class='title'>邀约到访管理</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" action="exportData.do">
	<table border="0">
		<tr> 
			<td style="width: 70px;text-align:right">公司</td>
			<td style="width: 130px">
				<select name='companyId' id="companyId" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 70px;text-align:right">预约顾问</td>
			<td style="width: 120px">
				<input name="gwName" type="text" maxlength="20" style="width:85px;" />
			</td>					
			<td style="width: 70px;text-align:right">预约时间</td>
			<td style="width:200px">
			  <input name="appointmentFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="appointmentTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="width: 70px;text-align:right">到访时间</td>
			<td style="width:200px">
				<input name="visitFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="visitTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>	 	    
		</tr>
		<tr>	
			<td style="text-align:right">客户姓名</td>
			<td>
				<input name="cstmName" id="cstmName" type="text" style="width:85px;" />
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
	
	<div id="visitDlg" class="easyui-dialog" style="width:500px;height:330px;padding:5px 5px"
            closed="true" buttons="#visitDlg-buttons">        
        <form id="visitForm" method="post">
             <input type='hidden' name="id" />
             <input type='hidden' name="stuId" />
        <table>
            <tr>
            	<td style='width:65px;text-align:right'>学生姓名</td>
            	<td style='width:150px'>
            		<input type='text' name='cstmName' style='width:130px' readonly />
            	</td>
            	<td style='width:65px;text-align:right'>预约顾问</td>
            	<td>
            		<input type='text' name='gwName' style='width:100px' readonly />
            		<input type='hidden' name='gwId'/>
            	</td>
            </tr>
            <tr>
            	<td style='text-align:right'>预约时间</td>
            	<td colspan='3'>
            		<input type="text" name='appointmentTime' style='width:130px' readonly />
            	</td>
            </tr>  
            <tr>  
            	<td style='text-align:right'>预约备注</td>
            	<td colspan='3'>
            		<textarea name='appointmentMemo' style='width:330px;height:40px' readonly></textarea>
            	</td>
            </tr> 
            <tr>
            	<td style='text-align:right'>到访时间</td>
            	<td colspan='3'>
            		<input type="text" name='visitTime' style='width:130px'
            			readonly onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm', minDate:'%y-%M-{%d -7}'})"
            		>
            	</td>
            </tr>  
            <tr>  
            	<td style='text-align:right'>到访备注</td>
            	<td colspan='3'>
            		<textarea name='visitMemo' style='width:330px;height:40px'></textarea>
            	</td>
            </tr> 
        </table>    
        </form>	
    </div>
    <div id="visitDlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveVisit()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#visitDlg').dialog('close')" style="width:90px">关闭</a>
    </div>
	
</body>
</html>