<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>合同信息</title>
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
	var skGrid = null;
	$(function() {
		$('#feeItemGrid').datagrid({
			title : '',
			striped:true,
			url : '/lx/contract/feeLineData.do',
			queryParams:{lxConId:${contract.id}},
			rownumbers : true,
			idField : 'id',
			columns : [ [ {
				field : 'id',
				hidden: true
			},{
				width : 185,
				title : '费用项目',
				align:'center',
				field: 'itemId',
				formatter: function(value,row,index){
					return row.itemName;
				}
			},{
				width : 100,
				title : '标准金额',
				align:'right',
				field : 'itemValue',
				formatter: function(value,row,index){
					return row.itemValue.toFixed(2);
				}
			},{
				width : 100,
				title : '优惠金额',
				align:'right',
				field : 'itemDiscount',
				formatter: function(value,row,index){
					return row.itemDiscount.toFixed(2);
				}
			},{
				width : 100,
				title : '应收金额',
				align:'right',
				field : 'itemInv',
				formatter: function(value,row,index){
					return (row.itemValue - row.itemDiscount).toFixed(2); 
				} 
			}] ]
		});
		
		skGrid = $('#skGrid').datagrid({
			title : '收款信息',
			striped:true,
			url : 'skData.do',
			queryParams:{conId:${contract.conId}},
			rownumbers : true,
			idField : 'id',
			columns : [ [ 
			{
				width : 100,
				title : '收款日期',
				align:'center',
				field : 'skDate',
				formatter:function(value,row,index){
				    var d = new Date(row.skDate);
				    return d.format('yyyy-MM-dd');
				}
			},{
				width : 120,
				title : '收款项目',
				align:'center',
				field : 'itemName'
			},{
				width : 100,
				title : '收款金额',
				align : 'right',
				field : 'skValue',
				formatter:function(value,row,index){
				    return row.skValue.toFixed(2);
				}
			}]]
		});
		
		$('#tranGrid').datagrid({
			url : '/lx/contract/tranApply/listByLxConId.do',
			queryParams:{lxConId:${contract.id}},
			rownumbers : true,
			idField : 'id',
			columns : [ [ 
			{
				width : 60,
				title : '类型',
				align:'center',
				field : 'tran_type',
				formatter:function(value,row,index){
				    if(row.tranType ==2)
				    	return '转后期';
				    else
				    	return '转顾问';
				}
			},{
				width : 100,
				title : '录入日期',
				align:'center',
				field : 'createdAt',
				formatter:function(value,row,index){
				    var d = new Date(row.createdAt);
				    return d.format('yyyy-MM-dd');
				}
			},{
				width : 120,
				title : '提交时间',
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
			}]],
			onDblClickRow:function(index,row){
				tranApply(row.id, null);
			}	
		});
	});	

	function newTranApply(tranType){
		tranApply(null, tranType);
	}
	function tranApply(id, tranType){
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
            	
            	doTranApply(id, tranType);
            }
		});
	}
	
	function doTranApply(id, tranType){
		$('#tranApplyForm').form('clear');
		$('#tranApplyForm').find("[name=conId]").val(${conId});
		
		var buttons = [
			{text:'保存', handler:function(){saveTranApply();}},
			{text:'提交', handler:function(){submitTranApply();}},
			{text:'关闭', handler:function(){$('#tranApplyDlg').dialog('close');}}
		];
		
		var szUrl = null;
		var queryParams = {conId:${contract.id}};
		if(id !=null){
			queryParams.id = id;
			szUrl = '/lx/contract/tranApply/applyInfo.do';
		}else{
			queryParams.lxConId = ${contract.id};
			szUrl = '/lx/contract/tranApply/newApply.do';
		}
		
		$.ajax({
			type:'post',
			url:szUrl,
			data:queryParams,
			error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	var data = result.data;
            	if( id == null)
            		data.tranType = tranType;
            	
            	$('#tranApplyForm').form('load',data);
            	if(data.id != null && 'DRAFT' != result.data.status)
            	   buttons = [{text:'关闭', handler:function(){$('#tranApplyDlg').dialog('close');}}	];
            	   
            	$('#tranApplyDlg').dialog({buttons:buttons});
            	$('#tranApplyDlg').dialog('open').dialog('center');
            }
        });
	}
	
	function submitTranApply(){
		if( !$('#tranApplyForm').form('validate') )
        	return;        		
        var params =  $.serializeObject($('#tranApplyForm'));
        
        $.messager.confirm('Confirm','确认信息无误?',function(r){
        	if (r)
        		doSubmitTranApply(params);
        });
    }    	
        
    function doSubmitTranApply(params){    
        $.ajax({
        	type:'post',
        	url:'/lx/contract/tranApply/submit.do',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
			},
			success: function(result){
				if (result.errorCode != 200){
					$.messager.show({title: 'Error', msg: result.error});
                } else {
                	$.messager.show({title: '消息', msg: '转案提交成功'});
                	$('#tranGrid').datagrid('load');
                	$('#tranApplyDlg').dialog('close');
                }
            }
		});
	}
	
	function saveTranApply(){
		if( !$('#tranApplyForm').form('validate') )
        	return;        		
        var params =  $.serializeObject($('#tranApplyForm'));
        
        $.ajax({
        	type:'post',
        	url:'/lx/contract/tranApply/save.do',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
			},
			success: function(result){
				if (result.errorCode != 200){
					$.messager.show({title: 'Error', msg: result.moreInfo});
                } else {
                	$.messager.show({title: '消息', msg: '保存成功'});
                	$('#tranGrid').datagrid('load');
                }
            }
		});
	}
	
	function selectUser(objName){
		$('#divUsers').find("[name=objName]").val(objName);
				
		$('#divUsers').find("[name=companyId]").val( ${contract.companyId} );
		$('#divUsers').find("[name=enabled]").val('true');
		$('#divUsers').find("[name=gwId]").val('');
		
		$('#divUsers').dialog('open').dialog('center');	
	}
	
	function searchUsers(){
		var params = {};
		params.companyId = $('#divUsers').find("[name=companyId]").val();
		params.enabled = $('#divUsers').find("[name=enabled]").val();
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
		var objName  = $('#divUsers').find('input[name=objName]').val();
		
		var sel = $("#divUsers").find("select[name=gwId]");
		$('#tranApplyForm').find("input[name=" + objName + "Id]").val( $(sel).val() );
		if( $(sel).val() == '')
			$('#tranApplyForm').find("input[name=" + objName + "Name]").val('');
		else	
			$('#tranApplyForm').find("input[name=" + objName + "Name]").val( $(sel).find("option:selected").text() );
			
		$('#divUsers').dialog('close');	
	}
</script>
</head>
<body style='margin-left:10px;'>
<table>
	<tr>
		<td style='width:70px;text-align:right'>签约公司：</td>
		<td style='width:220px;'>${contract.companyName}</td>
		<td style='width:70px;text-align:right'>学生姓名：</td>
		<td style='width:220px;'>${contract.cstmName}</td>
		<td style='text-align:right'>签约日期：</td>
		<td><fmt:formatDate value="${contract.signDate}" pattern="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td style='text-align:right'>合同号：</td>
		<td>${contract.conNo}</td>
		<td style='text-align:right'>状态：</td><td>${contract.statusName}</td>
		<td style='text-align:right'>归档日期：</td>
		<td>
			<fmt:formatDate value="${contract.archiveDate}" pattern="yyyy-MM-dd"/>
		</td>
	</tr>	
	<tr>
		<td style='text-align:right'>合同应收：</td>
		<td>${contract.conValue.subtract(contract.conDiscount) }</td>
		<td style='text-align:right'>合同金额：</td>
		<td>${contract.conValue}</td>
		<td style='text-align:right'>优惠金额：</td>
		<td>${contract.conDiscount}</td>
	</tr>		
	<tr>
		<td style='text-align:right'>收款金额：</td>
		<td>${contract.skValue}</td>
		<td style='text-align:right'>收款差额：</td>
		<td>${contract.conValue.subtract(contract.conDiscount) - contract.skValue}</td>
	</tr>		
	<tr>
		<td style='text-align:right'>签约顾问：</td><td>${contract.signGwName}</td>
		<td style='text-align:right'>规划顾问：</td><td>${contract.planGwName}</td>
	</tr>		
	<tr>
		<td style='text-align:right'>申请顾问：</td><td>${contract.applyGwName}</td>
		<td style='text-align:right'>写作顾问：</td><td>${contract.writeGwName}</td>
		<td style='text-align:right'>客服顾问：</td><td>${contract.serviceGwName}</td>
	</tr>	
	<tr>
		<td style='text-align:right'>录入人：</td>
		<td>${contract.creatorName}</td>
		<td style='text-align:right'>录入时间：</td>
		<td><fmt:formatDate value="${contract.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
	</tr>	
</table>
<br/>
<table id="feeItemGrid" class="easyui-datagrid">

</table>
<br />
<a href='javascript:void(0)' onclick='newTranApply(1)'>转后期</a>
<a href='javascript:void(0)' onclick='newTranApply(2)'>转顾问</a>
<table id="tranGrid" title='转案记录' class="easyui-datagrid" style="height:auto;">
</table>
<br/>
<table id="skGrid" class="easyui-datagrid" style="height:auto;">
</table>

<div id="tranApplyDlg" title='转顾问' class="easyui-dialog" style="width:500px;height:260px;padding:5px 5px"
            closed="true">        
	<form id="tranApplyForm" method="post">
    	<input type='hidden' name="id" />
        <input type='hidden' name="lxConId" value="${contract.id}"/>
        <input type='hidden' name="tranType" />
    <table>
    	<tr>
    		<td style='width:70px;text-align:right'>规划顾问：</td>
    		<td style='width:310px'>
    			<input type='text' name='toPlanGwName' style='width:85px'>
    			<input type='hidden' name='toPlanGwId'>
    			<a href='javascript:void(0)' onclick="selectUser('toPlanGw')">选择...</a>
    		</td>
    	</tr>
    	<tr>
    		<td style='text-align:right'>申请顾问：</td>
    		<td>
    			<input type='text' name='toApplyGwName' style='width:85px'>
    			<input type='hidden' name='toApplyGwId'>
    			<a href='javascript:void(0)' onclick="selectUser('toApplyGw')">选择...</a>
    		</td>
    	</tr>
    	<tr>
    		<td style='text-align:right'>写作顾问：</td>
    		<td>
    			<input type='text' name='toWriteGwName' style='width:85px'>
    			<input type='hidden' name='toWriteGwId'>
    			<a href='javascript:void(0)' onclick="selectUser('toWriteGw')">选择...</a>
    		</td>
    	</tr>
    	<tr>
    		<td style='text-align:right'>客服顾问：</td>
    		<td>
    			<input type='text' name='toServiceGwName' style='width:85px'>
    			<input type='hidden' name='toServiceGwId'>
    			<a href='javascript:void(0)' onclick="selectUser('toServiceGw')">选择...</a>
    		</td>
    	</tr>
    	<tr>
    		<td style='text-align:right'>备注：</td>
    		<td>
    			<textarea name='applyMemo' style='width:300px;height:50px'></textarea>
    		</td>
    	</tr>
    </table>
</div>


<div id='divUsers' class="easyui-dialog" title="选择顾问" data-options="closed:true,buttons:[{
				text:'确定',
				handler:userSelected
			},{
				text:'close',
				handler:function(){$('#divUsers').dialog('close');}
			}]" style="width:420px;height:260px;padding:10px">
	<table>
		<tr>
			<td style='width:60px;text-align:right'>公司</td>
			<td style='width:120px'>
				<select name='companyId' onchange="searchUsers()">
					<option value="">All</option>
					<option value="${contract.companyId}">${contract.companyName}</option>
				</select>
			</td>
			<td style='width:60px;text-align:right'>Enabled</td>
			<td>
				<select name='enabled'>
					<option value=''>全部</option>
					<option value='true'>Y</option>
					<option value='false'>N</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>姓名</td>
			<td><input type='text' name='name' style='width:80px' /></td>
			<td></td><td>
				<a href='javascript:void(0)' onclick='searchUsers()'>查询</a>
				<input type='hidden' name='objName'>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>顾问</td>
			<td><select name='gwId' style="width:120px;height:20px"></select></td>
		</tr>
	</table>
</div>
</body>
</html>