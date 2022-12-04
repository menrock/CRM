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
	
	var itemOptions=[
	<c:forEach var="item" varStatus="status" items="${feeItemList}">
	 <c:if test="${status.index >0}">,</c:if>{id:${item.id},name:'${item.dictName}'}
	</c:forEach>];
	var payTypes=[
	<c:forEach var="item" varStatus="status" items="${paytypeList}">
	 <c:if test="${status.index >0}">,</c:if>{id:${item.id},name:'${item.dictName}'}
	</c:forEach>];
	var payBanks=[
	<c:forEach var="item" varStatus="status" items="${paybankList}">
	 <c:if test="${status.index >0}">,</c:if>{id:${item.id},name:'${item.dictName}'}
	</c:forEach>];
	
	var grid;	
	
	var cstmId = ${student.cstmId};
	var cstmName = '${cstmName}';
		
	$(function() {			
	  grid = $('#grid').datagrid({
				title : '',
				url : 'stuPaymentData.do',
				queryParams:{cstmId:cstmId},
				striped:true,
				rownumbers : true,
				idField : 'id',
				frozenColumns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : '125',
					title : 'payNo',
					align:'center',
					field : 'p.pay_no',
					formatter: function(value,row,index){
						return row.payNo;
					} 
				},{
					width : '85',
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
					align:'center',
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
					width : '120',
					title : '录入时间',
					align:'center',
					field : 'p.created_at',
					formatter: function(value,row,index){
					    var d = new Date(row.createdAt);
					    return d.format('yyyy-MM-dd hh:mm');
					} 
				},{
					width : 100,
					title : '录入人',
					align:'center',
					field : 'p.creator_id',
					formatter: function(value,row,index){
					    return row.creatorName;
					} 
				}
				] ],
				onClickCell: onClickCell,
                onEndEdit: onEndEdit,
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
					width : 120,
					title : '合同号',
					align:'center',
					field: 'conNo',
                    editor:'text',
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
					},
                    editor:{
                    	type:'combobox',
                        options:{
                        	valueField:'id',
                            textField:'name',
                            data:itemOptions,
                            required:true
                        }
                    } 
				},{
					width : 80,
					title : '金额',
					align:'right',
					field : 'itemValue',
					editor:'numberbox' ,
					formatter: function(value,row,index){
						return row.itemValue ; 
					} 
				},{
					width : 80,
					title : '课程顾问',
					align : 'center',
					field : 'kcgwName',
                    editor: 'text' ,
					formatter: function(value,row,index){
						return row.kcgwName ; 
					} 
				},{
					width : 200,
					title : '备注',
					align : 'center',
					field : 'memo',
                    editor: 'text' ,
					formatter: function(value,row,index){
						return row.memo ; 
					} 
				}] ],
				onClickCell: onClickCell,
                onEndEdit: onEndEdit,
				toolbar : '#feeToolbar'
		});
		
		$('#payType').combobox({'data':payTypes,valueField: 'id',	textField: 'name'});
		$('#bankName').combobox({'data':payBanks,valueField: 'name',	textField: 'name'});
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
            	
            	$('#feeItemGrid').datagrid('resize', {width:750,height:300});
        	}
		});
	}	
	  
	function jsCreate(){
		$('#paymentForm').form('clear');
		$('#paymentForm').find("input[name=cstmId]").val(cstmId);
		$('#paymentForm').find("input[name=cstmName]").val(cstmName);
		
		$('#feeItemGrid').datagrid('loadData', { total: 0, rows: [] });
		editIndex = undefined;
		
		$('#divPayment').dialog('center');
		$('#divPayment').dialog('open');
	}
	
	function savePayment(){        
		accept();
		if( !$('#paymentForm').form('validate') ){
			$.messager.show( {title: '提醒',msg:'请录入必须的数据'} ); 
			return;
		}	
		var params =  $.serializeObject($('#paymentForm'));
		/*
		if( params.paidAt == ''){
        	$.messager.show( {title: '提醒',msg:'请选择合同类型'} ); 
			return;
		}
		if( !params.countryCodes || params.countryCodes == ''){
        	$.messager.show( {title: '提醒',msg:'请选择签约国家'} ); 
			return;
		}
		if( params.signGwId == ''){
        	$.messager.show( {title: '提醒',msg:'请选择签约顾问'} ); 
			return;
		} */
	
		var feeLines = $('#feeItemGrid').datagrid('getRows');
		if(feeLines.length ==0){
        	$.messager.show( {title: '提醒',msg:'请录入合同费用信息'} ); 
			return;
		}
		
		for(var i=0; feeLines != null && i < feeLines.length; i++){
			params["lines[" + i + "].id"] = feeLines[i].id;
			params["lines[" + i + "].conNo"] = feeLines[i].conNo;
			params["lines[" + i + "].itemId"] = feeLines[i].itemId;
			params["lines[" + i + "].itemValue"] = feeLines[i].itemValue;
			params["lines[" + i + "].kcgwName"] = feeLines[i].kcgwName;
			params["lines[" + i + "].memo"] = feeLines[i].memo;
		}
		 
        $.ajax({
        	type:'post',
        	url:'save.do',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if (result.errorCode != 200){
            		$.messager.show({
                    	title: 'Error',
                        msg: result.moreInfo
                    });
                } else {
                	$('#grid').datagrid('reload');
                	$('#divPayment').dialog('close');     // close the dialog
                }
        	}
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
	
	function onEndEdit(index, row){
    	var ed = $(this).datagrid('getEditor', {
                index: index,
                field: 'itemId'
        });
        row.itemName = $(ed.target).combobox('getText');
    }
	
	function removeit(){
		if (editIndex == undefined)
			return;
		$('#feeItemGrid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
		editIndex = undefined;
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
	
	function jsDelete(){
		var selRows = $('#grid').datagrid("getSelections");
		if( selRows.length == 0 ){
			$.messager.alert( {title: '提醒',msg:'请选择要删除的付款信息'} );
			return;
		}
		var ids = '';
		for(i=0; i < selRows.length; i++){
			if(ids =='')
				ids = selRows[i].id;
			else
				ids += "," + selRows[i].id;	
		}
		
		$.messager.confirm('Confirm','确定删除?',function(r){
			if (!r)
				return;
				
			var szUrl = "delete.do";
			$.ajax({
				type: 'POST',
				url: szUrl ,
				data: {ids:ids},
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show( {title: '提示信息',msg:'删除' + json.data + '条付款信息'} ); 
						$('#grid').datagrid('load', {cstmId:cstmId}); 
					}else{
						$.messager.alert('错误提醒','删除失败-' + json.data.moreInfo);
					}
				}
			});	
		}); 
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="jsCreate();">新建</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="jsDelete();">删除</a>
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>

<div id='divPayment' class="easyui-dialog" style="width:780px;height:420px;padding:5px"
	 data-options="closed:true,title:'付款信息',modal:true,
			buttons:[{
				text:'保存',
				handler:savePayment
			},{
				text:'关闭',
				handler:function(){$('#divPayment').dialog('close');}
			}]">
	<form name='paymentForm' id='paymentForm'>		
	<table>
		<tr>
			<td style='width:60px;text-align:right'>学生姓名</td>
			<td style='width:140px;'>
				<input type='text' name='cstmName' style='width:120px' readonly='readonly' />
				<input type='hidden' name='cstmId' />
				<input type='hidden' name='id' />
			</td>
			<td style='width:60px;text-align:right'>付款时间</td>
			<td style='width:130px;'>
				<input class="easyui-validatebox" name="paidAt" data-options="required:true" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;"/>
			</td>
			<td style='width:60px;text-align:right'>payNo</td>
			<td><input type='text' name='payNo' readonly style='width:140px'/></td>
		</tr>
		<tr>
			<td style='text-align:right'>收款方式</td>
			<td>
				<input type='text' id='payType' name='payType' class="easyui-combobox" data-options="required:true" style='width:100px'/>
			</td>
			<td style='text-align:right'>收款银行</td>
			<td>
				<input type='text' id='bankName' name='bankName' class="easyui-combobox" style='width:120px'/>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>付款人</td>
			<td >
				<input type='text' name='payerName' class="easyui-textbox" data-options="required:true" style='width:120px'/>
			</td>
			<td style='text-align:right'>付款金额</td> 
			<td >
				<input type='text' name='invMoney' class="easyui-textbox" data-options="required:true" style='width:100px'/>元
			</td>
		</tr>
	</table>
	<div id="feeToolbar" style="height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addFeeLine({})">Append</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>        
    </div>	
    <table class="easyui-datagrid" id="feeItemGrid" data-options="fit:true,border:false">
    </table>
	</form>
</div>	
</body>
</html>