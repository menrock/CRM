<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>回访统计报表</title>
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
	var grid,detailGrid;
	
	$(function() {
		detailGrid = $('#detailGrid').datagrid({
			title : '',
			striped:true,
			rownumbers : true,
			idField : 'id',
			columns : [ [{
				title : 'id',
				align:'center',
				field : 'id',
				hidden:true
			},{
				width : 120,
				title : '姓名',
				align:'center',
				field : 'c.name',
				formatter: function(value,row,index){
					return "<a href='/lx/student/main.do?id=" + row.stuId + "' target=_blank>" + row.cstmName + "</a>";
				} 
			},{
				width : 120,
				title : '截止时间',
				halign:'center',
				field : 'r.latest_contact_time',
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
				formatter: function(value,row,index){
					if(row.contactTime){
						var d = new Date(row.contactTime);
						return d.format('yyyy-MM-dd HH:mm');
					}	
				}
			}] ]
		});
		grid = $('#grid').datagrid({
			title : '',
			striped:true,
			rownumbers : true,
			idField : 'zxgwId',
			columns : [ [{
				width : 120,
				title : '分公司',
				align:'center',
				field : 'companyId',
				formatter: function(value,row,index){
					return row.companyName;
				} 
			},{
				width : 90,
				title : '顾问/组长',
				align:'center',
				field : 'zxgwId',
				formatter: function(value,row,index){
					return row.zxgwName;
				}	
			},{
				width : 120,
				title : '回访类型',
				align:'center',
				field : 'callbackTypeName'
			},{
				width : 120,
				title : '应完成数量',
				halign:'center',
				field : 'totalCount',
				align : 'right',
				formatter:function(value,row,index){
				    return "<a href='javascript:void(0)' onclick=\"showDetail(" + index + ",'" + this.field+ "')\">" + row.totalCount + "</a>";
				}
			},{
				width : 120,
				title : '已完成数量',
				halign:'center',
				field : 'finishCount',
				align : 'right',
				formatter:function(value,row,index){
				    return "<a href='javascript:void(0)' onclick=\"showDetail(" + index + ",'" + this.field+ "')\">" + row.finishCount + "</a>";
				}
			},{
				width : 100,
				title : '未完成数量',
				align:'center',
				field : 'noFinishCount',
				formatter: function(value,row,index){
				    var count = row.totalCount - row.finishCount;
				    return "<a href='javascript:void(0)' onclick=\"showDetail(" + index + ",'" + this.field+ "')\">" + count +"</a>";
				} 
			},{
				width : 100,
				title : '完成率',
				align:'center',
				field : 'finishRatio'
			},{
				width : '120',
				title : '逾期完成数量',
				align:'center',
				field : 'overtimeCount',
				formatter: function(value,row,index){
				    var count = row.finishCount - row.ontimeCount;
				    return "<a href='javascript:void(0)' onclick=\"showDetail(" + index + ",'" + this.field+ "')\">" + count + "</a>";
				} 
			}
			] ],
			toolbar : '#toolbar'
		});
	});
	
	function showDetail(rowIndex, field){
		var row = $(grid).datagrid('getRows')[rowIndex];
		var fldTitle = $(grid).datagrid('getColumnOption', field).title;
		var szTitle = row.zxgwName + '的' + fldTitle + '明细数据';
		
		var params = {};
		params.callbackType = searchParams.callbackType;
		$.extend(params, searchParams);
		params.zxgwId = row.zxgwId;
		if(field == 'finishCount'){
			params.callbackStatus = 'FINISH';
		}
		else if(field == 'noFinishCount'){
			params.callbackStatus = 'NOFINISH';
		}
		else if(field == 'overtimeCount'){
			params.callbackStatus = 'OVERTIME_FINISH';
		}
		
		$('#detailGrid').datagrid({
			url:'callbackCheckDetailData.do',
			queryParams:params
		});
		
		$('#detail-dialog').dialog('open').dialog('center').dialog('setTitle', szTitle);
	}
	
	var searchParams = null;
	
	function jsStat(){
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
        var params =  $.serializeObject($('#statForm'));
        searchParams = params;
        
        if(params.callbackType == ''){
        	$.messager.show( {title: '提示信息',msg:'请选择回访类型'} ); 
        	return;
        }
		
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
            	grid.datagrid({url:'callbackCheckData.do',queryParams:params});
            }
		});  
	}
	
	function jsExport(){
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
					
					$('input[name=stuFromId]').val(node.id);
					$('input[name=stuFromName]').val(szTxt);
					$('#divFromTree').dialog('close');
				}
			});
		}	
		$('#divFromTree').dialog('open').dialog("center");
    }
    function clearStuFrom(){
		$('input[name=stuFromId]').val('');
		$('input[name=stuFromName]').val('');
		$('#divFromTree').dialog('close');
    }
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false" style='margin:5px'>

<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>回访统计报表</td></tr>
</table>
</c:if>

<div id="toolbar" style="display: none">
<form name="statForm" id="statForm" method="post" action="callbackCheckExcel">
	<table border="0">
		<tr> 
			<td style="width:60px;text-align:right">分公司</td>
			<td style="width:120px">
				<select name='companyId' id="companyId" style="width:115px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width:70px;text-align:right">回访类型</td>
			<td style="width: 120px">
				<select name='callbackType'>
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${callbackTypes}">
					<option value='${item}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width:70px;text-align:right">统计周期</td>
			<td style="width:200px">
			  <input name="dateFrom" id="dateFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="dateTo"   id="dateTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="width:80px;text-align:right">客户录入时间</td>
			<td style="width:200px">
			  <input name="stuCreatedFrom" id="stuCreatedFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="stuCreatedTo"   id="stuCreatedTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
		</tr>
		<tr>
			<td>客户来源</td>
			<td colspan='3'>
				<input type='text' name="stuFromName" style='width:250px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromId" >
			</td>	
			<td colspan='3'>
				<a href="javascript:void(0);" class="easyui-linkbutton" style="margin-left: 10px;" 
				  onclick="jsStat()" >统计</a> 
				
				<a href="javascript:void(0);" class="easyui-linkbutton" style="margin-left: 0px;" 
				  onclick="jsExport()" >导出</a> 
			</td>
		</tr>
	</table>
</form>
</div>

	<table id="grid" data-options="fit:true,border:false">
	</table>
	
<div id='divFromTree' class="easyui-dialog"  style="width:500px;height:360px;padding:10px"
	 data-options="closed:true,title:'客户来源选择',buttons:[{
				text:'clear',
				handler:clearStuFrom
			}]">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>

<div id='detail-dialog' class="easyui-dialog"  style="width:720px;height:420px;padding:5px"
	 data-options="closed:true,title:'回访明细',buttons:[{
				text:'close',
				handler:function(){
					$('#detail-dialog').dialog('close')
				}
			}]">
	<table id="detailGrid" data-options="fit:true,border:false">
	</table>
</div>
	
</body>
</html>