<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>客户付款信息</title>
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
	var payTypes=[
	<c:forEach var="item" varStatus="status" items="${paytypeList}">
	 <c:if test="${status.index >0}">,</c:if>{id:${item.id},name:'${item.dictName}'}
	</c:forEach>];
	
	var grid;	
	
	function jsQuery() {
		grid.datagrid('clearSelections');
		var params =  $.serializeObject($('#searchForm'));
		
		if(params.statusList == ''){
			params.statusList = "SUBMIT,FZ_CONFIRMED,CONFIRMED";
		}
		
		grid.datagrid('load', params); 
	};
			
	$(function() {			
	  grid = $('#grid').datagrid({
				title : '',
				url : 'paymentsData.do',
				queryParams:{'STATUS':'SUBMIT,CONFIRMED'},
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100],
				idField : 'id',
				frozenColumns : [ [ {
					field : 'id',
					hidden: true
				},{
					width : 100,
					title : '公司',
					align:'center',
					field : 'p.company_id',
					sortable:true,
					formatter: function(value,row,index){
						return row.companyName;
					} 
				},{
					width : 140,
					title : 'payNo',
					align:'center',
					field : 'p.pay_no',
					sortable:true,
					formatter: function(value,row,index){
						return row.payNo;
					} 
				},{
					width : 85,
					title : '学生姓名',
					align:'center',
					field : 'c.cstm_id',
					formatter: function(value,row,index){
						var szName = row.cstmName;
						if(szName == '') szName = '未录入';
					  return "<a href='javascript:void(0)' onclick=\"edit('" +row.id + "')\">" + szName + "</a>"; 
					} 
				},{
					width : 95,
					title : '付款日期',
					align:'center',
					field : 'p.paid_at',
					formatter: function(value,row,index){
						if(row.paidAt)
							return new Date(row.paidAt).format('yyyy-MM-dd');
						else
							return '';
					} 
				},{
					width : 80,
					title : '付款金额',
					align:'right',
					field : 'p.inv_money',
					formatter: function(value,row,index){
						return row.invMoney; 
					} 
				},{
					width : 125,
					title : '收款方式',
					align:'center',
					field : 'p.pay_type',
					formatter: function(value,row,index){
						return row.payTypeName;
					} 
				},{
					width : 125,
					title : '收款银行',
					align:'center',
					field : 'p.bank_name',
					formatter: function(value,row,index){
						return row.bankName;
					} 
				}] ],
				columns : [ [ 
				{
					width : 80,
					title : '状态',
					align : 'center',
					halign:'center',
					field : 'p.status',
					formatter: function(value,row,index){
					    return row.statusName;
					} 
				},{
					width : 80,
					title : '分总确认',
					align:'center',
					field : 'p.fz_confirmer_id',
					formatter: function(value,row,index){
					    return row.fzConfirmerName;
					} 
				},{
					width : 120,
					title : '分总确认时间',
					align:'center',
					field : 'p.confirmed_at',
					formatter: function(value,row,index){
						if(row.fzConfirmedAt){
							var d = new Date(row.fzConfirmedAt);
							return d.format('yyyy-MM-dd hh:mm');
					    }
					} 
				},{
					width : 80,
					title : '财务确认',
					align:'center',
					field : 'p.confirmer_id',
					formatter: function(value,row,index){
					    return row.confirmerName;
					} 
				},{
					width : 120,
					title : '财务确认时间',
					align:'center',
					field : 'p.confirmed_at',
					formatter: function(value,row,index){
						if(row.confirmedAt){
							var d = new Date(row.confirmedAt);
							return d.format('yyyy-MM-dd hh:mm');
					    }
					} 
				},{
					width : 80,
					title : '录入人',
					align : 'center',
					halign:'center',
					field : 'p.creator_id',
					formatter: function(value,row,index){
					    return row.creatorName;
					} 
				},{
					width : '120',
					title : '录入时间',
					align:'center',
					field : 'p.created_at',
					formatter: function(value,row,index){
					    var d = new Date(row.createdAt);
					    return d.format('yyyy-MM-dd hh:mm');
					} 
				}
				] ],
				onClickCell: onClickCell,
				toolbar : '#toolbar'
		});
		
		$('#feeItemGrid').datagrid({
				title : '',
				striped:true,
				rownumbers : true,
				idField : 'id',
				columns : [ [ {
					field : 'id',
					hidden: true
				},{
					width : 140,
					title : '款项公司',
					align:'center',
					field: 'companyId',
					formatter: function(value,row,index){
						return row.companyName;
					}
				},{
					width : 140,
					title : '合同号',
					align:'center',
					field: 'conNo',
					formatter: function(value,row,index){
						return row.conNo;
					}
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
					title : '金额',
					align:'right',
					field : 'itemValue',
					formatter: function(value,row,index){
						return row.itemValue ; 
					} 
				}] ],
				onClickCell: onClickCell
		});
		
		$('#payType').combobox({'data':payTypes,valueField: 'id',	textField: 'name'});
	}); 
	
	function edit(id){
		$.ajax({
        	type:'post',
        	url:'detailData.do',
        	data:{id:id},
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	$('#paymentForm').form('load', result.data);
            	
            	if( result.data.status == 'CONFIRMED'){
            		$('#divPayment').dialog({
            			buttons:[
            				{text:'撤回',handler:revokePayment},
            				{text:'关闭',	handler:function(){$('#divPayment').dialog('close');}
            			}]
            		});
            	}else{
            		$('#divPayment').dialog({
            			buttons:[
            				{text:'确认',handler:confirmPayment},
            				{text:'关闭',	handler:function(){$('#divPayment').dialog('close');}
            			}]
            		});
            	}
            	
            	
            	$('#paymentForm').find('input[name=status]').val( result.data.status );
            	var d = new Date(result.data.paidAt);
            	$('#paymentForm').find('input[name=paidAt]').val( d.format('yyyy-MM-dd') );
            	
            	$('#feeItemGrid').datagrid('loadData', { total: 0, rows: [] });
            	if( result.data && result.data.lines){
            		$.each(result.data.lines, function(index, item){
            			addFeeLine(item);
            		});
            	}
            	$('#divPayment').dialog('center');
            	$('#divPayment').dialog('open');
            	
            	$('#feeItemGrid').datagrid('resize', {width:650,height:300});
        	}
		});
	}
	
	function confirmPayment(){        
		accept();
		if( !$('#paymentForm').form('validate') ){
			$.messager.show( {title: '提醒',msg:'请录入必须的数据'} ); 
			return;
		}	
		var params =  $.serializeObject($('#paymentForm'));
		
		if( params.status == 'CONFIRMED'){
        	$.messager.alert( {title: '提醒',msg:'此收款已经确认，不能重复确认'} ); 
			return;
		}
	
		var feeLines = $('#feeItemGrid').datagrid('getRows');
		if(feeLines.length ==0){
        	$.messager.show( {title: '提醒',msg:'请录入合同费用信息'} ); 
			return;
		}
		
		for(var i=0; feeLines != null && i < feeLines.length; i++){
			params["lines[" + i + "].id"] = feeLines[i].id;
			params["lines[" + i + "].companyId"] = feeLines[i].companyId;
			params["lines[" + i + "].conNo"] = feeLines[i].conNo;
			params["lines[" + i + "].itemId"] = feeLines[i].itemId;
			params["lines[" + i + "].itemValue"] = feeLines[i].itemValue;
		}
		
		$.messager.confirm('确认', "确定信息无误?", function(r){
			if (!r)
				return ;
				
			$.ajax({
        		type:'post',
        		url:'confirm.do',
        		data:params,
        		error:function(){
        			$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            	},
            	success: function(result){
            		if (result.errorCode != 200){
            			$.messager.alert({
                    		title: 'Error',
                        	msg: result.moreInfo
                    	});
                	} else {
                		$('#grid').datagrid('reload');
                		$('#divPayment').dialog('close');     // close the dialog
                	}
                }	
        	});
		});
	}
	
	function revokePayment(){ 
		var params =  $.serializeObject($('#paymentForm'));
		if( params.status != 'CONFIRMED'){
        	$.messager.alert( {title: '提醒',msg:'此收款还没确认，不需要撤回'} ); 
			return;
		}
		
		$.messager.confirm('确认', "确定撤回?", function(r){
			if (!r)
				return ;
				
			$.ajax({
        		type:'post',
        		url:'revoke.do',
        		data:{id:params.id},
        		error:function(){
        			$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            	},
            	success: function(result){
            		if (result.errorCode != 200){
            			$.messager.alert({
                    		title: 'Error',
                        	msg: result.moreInfo
                    	});
                	} else {
                		$('#grid').datagrid('reload');
                		$('#divPayment').dialog('close');     // close the dialog
                	}
                }	
        	});
		});
	}
	
	var editIndex = undefined;
    function endEditing(){
    	if (editIndex == undefined){return true}
        if ($('#feeItemGrid').datagrid('validateRow', editIndex)){
                $('#feeItemGrid').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
        } else {
                return false;
        }
	}
	
	function onClickCell(index, field){
		if (editIndex != index){
                if (endEditing()){
                    $('#feeItemGrid').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    var ed = $('#feeItemGrid').datagrid('getEditor', {index:index,field:field});
                    if (ed){
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }
                    editIndex = index;
                } else {
                    setTimeout(function(){
                        $('#feeItemGrid').datagrid('selectRow', editIndex);
                    },0);
                }
    	}
	}
    
    function accept(){
		if (endEditing())
			$('#feeItemGrid').datagrid('acceptChanges');            
	}
	    
	function addFeeLine(data){
		if (endEditing()){
			$('#feeItemGrid').datagrid('appendRow',data);
			editIndex = $('#feeItemGrid').datagrid('getRows').length-1;
			$('#feeItemGrid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);     
			if(data != null)  accept();     
		}
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
	<table width=800px>
		<tr><td height=10></td></tr>
		<tr><td width=100% align='center' height=30 class='title'>客户付款信息</td></tr>
	</table>
	<form name="searchForm" id="searchForm" method="post" >
	<table width="100%" border="0">
		<tr> 
			<td style="width: 80px;text-align:right">公司</td>
			<td style="width: 100px">
				<select name='companyId' id="company_id" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 70px;text-align:right">学生姓名</td>
			<td style="width: 100px">
				<input name="cstmName" id="cstmName" type="text" maxlength="50" style="width:80px;" />
			</td>
			<td style="width: 70px;text-align:right">付款人</td>
			<td style="width: 100px"><input name="payerName" type="text" maxlength="20" style="width:85px;" /></td>	
			<td style="width: 70px;text-align:right">收款方式</td>
			<td style="width: 100px">
				<input type='text' id='payType' name='payType' class="easyui-combobox" data-options="required:true" style='width:100px'/>
				
			</td>	
			<td >
			
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
				
				<input type='checkbox' name='fuzzySearch' checked='true'/>模糊查询
			</td>
		</tr>
		<tr>
			<td style="text-align:right">录入人</td>
			<td>
				<input type='text' name="creatorName" style='width:80px'>
			</td>
			<td style="text-align:right">状态</td>
			<td>
				<select name="statusList">
					<option value="">全部</option>
					<option value="SUBMIT" >未确认</option>
					<option value="FZ_CONFIRMED" >分总确认</option>
					<option value="CONFIRMED" >财务确认</option>
				</select>
			</td>
		</tr>	
	</table>
	</form>
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>

<div id='divPayment' class="easyui-dialog" style="width:720px;height:480px;padding:10px"
	 data-options="closed:true,title:'付款信息',modal:true,
			buttons:[{
				text:'确认',
				handler:confirmPayment
			},{
				text:'关闭',
				handler:function(){$('#divPayment').dialog('close');}
			}]">
	<form name='paymentForm' id='paymentForm'>		
	<table>
		<tr>
			<td style='width:75px;text-align:right'>学生姓名</td>
			<td style='width:120px;'>
				<input type='text' name='cstmName' style='width:110px' readonly='readonly' />
				<input type='hidden' name='cstmId' />
				<input type='hidden' name='id' />
				<input type='hidden' name='status' />
			</td>
			<td style='width:60px;text-align:right'>付款时间</td>
			<td style='width:110px;'>
				<input class="easyui-validatebox" name="paidAt" data-options="required:true" readonly type="text" style="width:80px;"/>
			</td>
			<td style='width:60px;text-align:right'>payNo</td>
			<td><input type='text' name='payNo' readonly style='width:140px'/></td>
		</tr>
		<tr>
			<td style='text-align:right'>收款方式</td>
			<td>
				<input type='text' name='payTypeName' data-options="required:true" readonly style='width:110px'/>
				<input type='hidden' name='payType' />
			</td>
			<td style='text-align:right'>收款银行</td>
			<td>
				<input type='text' name='bankName' data-options="required:true" readonly style='width:120px'/>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>付款人</td>
			<td >
				<input type='text' name='payerName' readonly style='width:110px'/>
			</td>
			<td style='text-align:right'>付款金额</td>
			<td >
				<input type='text' name='invMoney' data-options="required:true" readonly style='width:100px'/>元
			</td>
		</tr>
	</table>
    <table class="easyui-datagrid" id="feeItemGrid" data-options="fit:true,border:false">
    </table>
	</form>
</div>
</body>
</html>