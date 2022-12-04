<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>合同管理</title>
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
	var grid;	
		
	$(function() {	
		$('#countryCodes').combotree('loadData', [
		<c:forEach var="item" varStatus="status" items="${countryList}">
			<c:if test="${status.index >0}">,</c:if>
			{id:'${item.code}', text:'${item.name}'}
		</c:forEach>	
		]);
		$('#countryCodes').combotree('setValues', 
		[
		<c:forEach var="item" varStatus="status" items="${lsCountry}"><c:if test="${status.index >0}">,</c:if>'${item.countryCode}'</c:forEach>	
		]
		);
		
	  grid = $('#grid').datagrid({
				title : '',
				url : 'stuContractData.do',
				queryParams:{stuId:${stu.id}},
				striped:true,
				rownumbers : true,
				idField : 'id',
				frozenColumns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : '85',
					title : '签约公司',
					align:'center',
					field : 'c.company_id',
					formatter: function(value,row,index){
						return row.companyName;
					} 
				},{
					width : '85',
					title : '姓名',
					align:'center',
					field : 'a.name',
					formatter: function(value,row,index){
						var szName = row.cstmName;
						if(szName == '') szName = '未录入';
					  return "<a href='javascript:void(0)' onclick=\"edit('" +row.id + "')\">" + szName + "</a>"; 
					} 
				},{
					width : 95,
					title : '合同号',
					align:'center',
					field : 'c.con_no',
					formatter: function(value,row,index){
						return row.conNo; 
					} 
				},{
					width : 80,
					title : '合同类型',
					align:'center',
					field : 'c.con_type',
					formatter: function(value,row,index){
						return row.conTypeName; 
					} 
				},{
					width : 80,
					title : '状态',
					align:'center',
					field : 'c.status',
					formatter: function(value,row,index){
					  return row.status; 
					} 
				},{
					width : 75,
					title : '标准金额',
					align:'right',
					halign:'center',
					field : 'conValue'
				},{
					width : 75,
					title : '优惠金额',
					align:'right',
					halign:'center',
					field : 'conDiscount'
				},{
					width : 75,
					title : '应收金额',
					align:'right',
					halign:'center',
					field : 'conInv',
					formatter: function(value,row,index){
						return row.conValue - row.conDiscount;
					}
				},{
					width : 75,
					title : '收款额',
					align:'right',
					halign:'center',
					field : 'skValue'
				}] ],
				columns : [ [ 
				{
					width : 100,
					title : '首次收款',
					align:'center',
					field : 'c.first_sk_date',
					formatter: function(value,row,index){
						if(!row.firstSkDate) return "";
					    var d = new Date(row.firstSkDate);
					    return d.format('yyyy-MM-dd');
					} 
				},{
					width : 130,
					title : '签约国家',
					halign:'center',
					field : 'countryCodes',
					formatter: function(value,row,index){return row.countryNames; } 
				},{
					width : 80,
					title : '签约顾问',
					halign:'center',
					field : 'a.sign_gw_id',
					formatter: function(value,row,index){
						return row.signGwName;
					}
				},{
					width : 80,
					title : '规划顾问',
					halign:'center',
					field : 'c.plan_gw_id',
					formatter: function(value,row,index){
						return row.planGwName;
					}
				},{
					width : '120',
					title : '申请/写作顾问',
					halign:'center',
					field : 'c.apply_gw_id',
					formatter: function(value,row,index){
						var szGwName1 = (row.applyGwName==null?'':row.applyGwName);
						var szGwName2 = (row.writeGwName==null?'':row.writeGwName);
						return szGwName1 + '/' + szGwName2;
					}
				},{
					width : 75,
					title : '客服顾问',
					halign:'center',
					field : 'c.service_gw_id',
					formatter: function(value,row,index){
						return row.serviceGwName ;
					}
				},{
					width : 80,
					title : '签约日期',
					align:'center',
					field : 'sign_date',
					formatter: function(value,row,index){
						if(!row.signDate) return "";
					    var d = new Date(row.signDate);
					    return d.format('yyyy-MM-dd'); 
					} 
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
					width : 100,
					title : '标准金额',
					align:'right',
					field : 'itemValue',
					formatter: function(value,row,index){
						return row.itemValue; 
					},
					editor:'numberbox' 
				},{
					width : 100,
					title : '优惠金额',
					align:'right',
					field : 'itemDiscount',
					formatter: function(value,row,index){
						return row.itemDiscount; 
					},
					editor:'numberbox' 
				},{
					width : 100,
					title : '应收金额',
					align:'right',
					field : 'itemInv',
					formatter: function(value,row,index){
						return row.itemValue - row.itemDiscount; 
					} 
				}] ],
				onClickCell: onClickCell,
                onEndEdit: onEndEdit,
				toolbar : '#feeToolbar'
		});
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
            	$('#contractForm').form('clear');
            	$('#contractForm').form('load', result.data);
            	
            	if(result.data.signDate){
            		var d = new Date(result.data.signDate);
            		$('#contractForm').find('input[name=signDate]').val( d.format('yyyy-MM-dd') );
            	}else{
            		$('#contractForm').find('input[name=signDate]').val('');
            	}
            	
            	$('#feeItemGrid').datagrid('loadData', { total: 0, rows: [] });
            	if( result.data && result.data.feeLines){
            		$.each(result.data.feeLines, function(index, item){
            			addFeeLine(item);
            		});
            	}
            	
            	$('#ccCompanyId').combobox("loadData",[{id:result.data.companyId,name:result.data.companyName}]);
            	$('#ccCompanyId').combobox("setValue",result.data.companyId);
            	
            	$('#divContract').dialog('center');
            	$('#divContract').dialog('open');
            	$('#feeItemGrid').datagrid('resize', {width:620});
        	}
		});
	}	
	  
	function jsCreate(){
		$('#contractForm').form('clear');
		
		var arrData = $('#ccCompanyId').combobox("getData");
		if(arrData.length == 0){
			$('#ccCompanyId').combobox("loadData",[{id:'${stu.companyId}',name:'${stu.companyName}'}]);
		}
		$('#ccCompanyId').combobox("setValue",'${stu.companyId}');
		$('#divContract').dialog('center');
		$('#divContract').dialog('open');
	}
	  
	function jsHisAdd(){
		$('#contractForm').form('clear');
		$('#contractForm').find("input[name=isHisAdd]").val(1);
		$('#contractForm').find("input[name=conNo]").attr("readonly",false);
		
		var arrData = $('#ccCompanyId').combobox("getData");
		if(arrData.length == 0){
			$('#ccCompanyId').combobox("loadData",[{id:'${stu.companyId}',name:'${stu.companyName}'}]);
		}
		$('#ccCompanyId').combobox("setValue",'${stu.companyId}');
		$('#divContract').dialog('center');
		$('#divContract').dialog('open');
	}
	
	function fetchCompany(){
		var arrData = $('#ccCompanyId').combobox('getData');
		
		//不重复加载
		if( arrData.length >1)
			return;
		
		$.ajax({
        	type:'post',
        	url:'/company/listData.do',
        	data:{},
        	error:function(){alert('系统异常');},
        	success: function(result){
            	if (result.errorCode != 200){
            		$.messager.show({
                    	title: 'Error',
                        msg: result.error
                    });
                } else {
                	$('#ccCompanyId').combobox('loadData', result.data);
                }
        	}
        });	
	}
	
	function tranSubmit(){
		var params = $.serializeObject($('#contractForm'));
	
		$.messager.confirm('确认', '确认信息无误，转后期？', function(r){
			if (!r)
				return;
				
			$.ajax({
        		type:'post',
        		url:'tranSubmit.do',
        		data:params,
        		error:function(){
        			$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            	},
            	success: function(result){
            		if (result.errorCode != 200){
            			$.messager.show({
                    		title: 'Error',   msg: result.error
                    	});
                    } else {
                		$.messager.show( {title: '提醒消息',msg:'转后期成功提交'} ); 
                	}
        		}
			});	
		});
	}
	
	function saveContract(){ 
		saveSubmitContract(false);
	}
	function submitContract(){
		saveSubmitContract(true);
	}
	function saveSubmitContract(isSubmit){        
		accept();
		
		var params =  $.serializeObject($('#contractForm'));
		params.cstmId = "${stu.cstmId}";
		
		if(isSubmit){
			if(params.signDate == ''){
				$.messager.show( {title: '提醒',msg:'请选择签约日期'} );
				return;
			}
		}
		params.planEnterYear = $.trim(params.planEnterYear);
		
		if(params.planEnterYear !=''){
			re = /^2\d{3}$/
			if (!re.test(params.planEnterYear)) {
				$.messager.alert( {title: '提醒',msg:'申请年份格式为2YYY'} );
				$('#planEnterYear').focus();
				return;
			}
		}
		
		if( params.isHisAdd == '1' && params.conNo == ''){
        	$.messager.alert( {title: '提醒',msg:'请录入合同号'} ); 
			return;
		}
		
		if( !params.conType || params.conType == ''){
        	$.messager.alert( {title: '提醒',msg:'请选择合同类型'} ); 
			return;
		}
		if( !params.countryCodes || params.countryCodes == ''){
        	$.messager.alert( {title: '提醒',msg:'请选择签约国家'} ); 
			return;
		}
		if( params.signGwId == ''){
        	$.messager.alert( {title: '提醒',msg:'请选择签约顾问'} ); 
			return;
		}
		
		if( !$('#contractForm').form('validate') ){
			$.messager.alert( {title: '提醒',msg:'请录入必须的数据'} ); 
			return;
		}	
		
		params.stuId=${stu.id};
	
		var feeLines = $('#feeItemGrid').datagrid('getRows');
		if(feeLines.length ==0){
        	$.messager.show( {title: '提醒',msg:'请录入合同费用信息'} ); 
			return;
		}
		
		for(var i=0; feeLines != null && i < feeLines.length; i++){
			params["feeLines[" + i + "].id"] = feeLines[i].id;
			params["feeLines[" + i + "].itemId"] = feeLines[i].itemId;
			params["feeLines[" + i + "].itemValue"] = feeLines[i].itemValue;
			params["feeLines[" + i + "].itemDiscount"] = feeLines[i].itemDiscount;
		}
		if(isSubmit)
			params.status = 'SUBMIT';
		 
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
                        msg: result.error
                    });
                } else {
                	$('#grid').datagrid('reload');
                	$('#divContract').dialog('close');     // close the dialog
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
		if (editIndex == undefined){return}
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
			$.messager.alert( {title: '提醒',msg:'请选择要删除的合同'} );
			return;
		}
		var ids = '';
		for(i=0; i < selRows.length; i++){
			if(i ==0)
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
						$.messager.show( {title: '提示信息',msg:'删除' + json.data + '个合同'} ); 
						$('#grid').datagrid('load', {stuId:${stu.id}}); 
					}else{
						$.messager.alert('错误提醒','删除失败-' + json.error);
					}
				}
			});	
		}); 
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
	<a href='javascript:void(0)' onclick='jsCreate();' class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				style="margin-left: 0px;" >新建</a> 
	<a href='javascript:void(0)' onclick='jsDelete();' class="easyui-linkbutton" data-options="iconCls:'icon-remove'" 
				style="margin-left: 0px;" >删除</a> 
	<a href='javascript:void(0)' onclick='jsHisAdd();' class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				style="margin-left: 0px;" >补录</a> 
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>

<div id='divContract' class="easyui-dialog" style="width:650px;height:480px;padding:10px"
	 data-options="closed:true,title:'合同详情',modal:true,
			buttons:[{
				text:'保存',
				handler:saveContract
			},{
				text:'提交',
				handler:submitContract
			},{
				text:'转后期',
				handler:tranSubmit
			},{
				text:'关闭',
				handler:function(){$('#divContract').dialog('close');}
			}]">
	<form name='contractForm' id='contractForm'>	
		<input type='hidden' name='isHisAdd'>
	<table>
		<tr>
			<td style='width:60px;text-align:right'>签约公司</td>
			<td style='width:140px;'>
				<select id='ccCompanyId' name='companyId' class="easyui-combobox" style='width:100px'
				   data-options="valueField: 'id',textField: 'name'"></select>
				<a href='javascript:void(0)' onclick='fetchCompany()'>...</a>
				<input type='hidden' name='id' />
			</td>
			<td style='width:60px;text-align:right'>合同类型</td>
			<td colspan='3'>
				<input type='text' name="conTypeName" style='width:160px' readonly>
				<a href='javascript:;' onclick='selConType()'>...</a>
				<input type='hidden' name="conType" value="">
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>合同号</td>
			<td><input class="easyui-validatebox" type='text' name='conNo' readonly style='width:120px'/></td>
			<td style='text-align:right'>签约日期</td>
			<td>
				<input class="easyui-validatebox" name="signDate" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;"/>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>办理国家</td>
			<td colspan='5'>
				<select id="countryCodes" name="countryCodes" class="easyui-combotree" multiple="true"
				  style="width:280px;" panelWidth="290"></select>
			</td>
		</tr>
		<tr>
			<td align="right">申请季度</td>
			<td >
				<input type="text" id="planEnterYear" name="planEnterYear" style="width:40px">年
				<select name="planEnterSeason" >
					<option value=''></option>
					<option value='春季' >春季</option>
					<option value='秋季' >秋季</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>签约顾问</td>
			<td><input type="text" name="signGwName" style="width:80px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="selectUser('signGw')">...</a>
				<input type="hidden" name="signGwId" >
			</td>
			<td style='text-align:right'>规划顾问</td>
			<td><input type="text" name="planGwName" style="width:80px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="selectUser('planGw')">...</a>
				<input type="hidden" name="planGwId" >
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>申请顾问</td>
			<td><input type="text" name="applyGwName" style="width:80px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="selectUser('applyGw')">...</a>
				<input type="hidden" name="applyGwId" >
			</td>
			<td style='text-align:right'>写作顾问</td>
			<td><input type="text" name="writeGwName" style="width:80px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="selectUser('writeGw')">...</a>
				<input type="hidden" name="writeGwId" >
			</td>
			<td style='width:70px;text-align:right'>客服顾问</td>
			<td style='width:120px;'><input type="text" name="serviceGwName" style="width:80px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="selectUser('serviceGw')">...</a>
				<input type="hidden" name="serviceGwId" >
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


<div id='divUsers' class="easyui-dialog" title="选择顾问" data-options="closed:true" style="width:400px;height:200px;padding:10px">
	公司<select name='companyId' onchange="searchUsers()">
		<option value=''>全部</option>
	<c:forEach var="item" varStatus="status" items="${companyList}">
		<option value='${item.id}' <c:if test="${item.id == stu.companyId}"> selected</c:if> >${item.name}</option>
	</c:forEach>]
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
<div id='divConTypeTree' class="easyui-dialog"  style="width:500px;height:360px;padding:10px"
	 data-options="closed:true,title:'合同类型选择',buttons:[{
				text:'clear',
				handler:clearConType
			}]">
	<ul id="conTypeTree" class="easyui-tree" ></ul>
</div>
<script language='javascript'>
	function selectUser(objName){
		if( $('#divUsers').find("select[name=companyId]").val() == '')
			$('#divUsers').find("select[name=companyId]").val(${stu.companyId});
			
		$('#divUsers').find("input[name=objName]").val(objName);
		$('#divUsers').dialog('center');
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
		var objName  = $('#divUsers').find('input[name=objName]').val();
		var sel = $("#divUsers").find("select[name=gwId]");
		
		$('#contractForm').find("input[name=" + objName + "Id]").val( $(sel).val() );
		if( $(sel).val() == '')
			$('#contractForm').find("input[name=" + objName + "Name]").val('');
		else	
			$('#contractForm').find("input[name=" + objName + "Name]").val( $(sel).find("option:selected").text() );
				
		$('#divUsers').dialog('close');
	}
    
    function selConType(){
		var roots = $('#conTypeTree').tree('getRoots');
		if(roots == null || roots.length ==0){	
			$('#conTypeTree').tree({
				lines:true,
				url : '/dict/treeData?rootCode=custcontract.type',
				onClick : function(node) {
					if( !$('#conTypeTree').tree('isLeaf',node.target) )
						return;
				
					var pNode = $('#conTypeTree').tree('getParent',node.target);
					
					var szTxt = node.text;
					if(pNode) szTxt = pNode.text + "-" + szTxt;
					
					$('input[name=conType]').val(node.id);
					$('input[name=conTypeName]').val(szTxt);
					$('#divConTypeTree').dialog('close');
				}
			});
		}	
		$('#divConTypeTree').dialog('center');
		$('#divConTypeTree').dialog('open');
    }
    function clearConType(){
		$('input[name=conType]').val('');
		$('input[name=conTypeName]').val('');
		$('#divConTypeTree').dialog('close');
    }
</script>
</body>
</html>