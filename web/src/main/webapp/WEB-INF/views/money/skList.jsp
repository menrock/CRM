<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>收款管理</title>
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
	var itemOptions=[
	<c:forEach var="item" varStatus="status" items="${feeItemList}">
	 <c:if test="${status.index >0}">,</c:if>{id:${item.id},name:'${item.dictName}'}
	</c:forEach>];
	
	var grid;  
	$(function() {
			
	  grid = $('#grid').datagrid({
				title : '',
				url : 'listData.do',
				striped:true,
				showFooter: true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100,200],
				idField : 'id',
				queryParams: {footer:true},
				frozenColumns : [ [ 
				{
					field : 'id',
					checkbox: true
				},{
					width : 85,
					title : '款项公司',
					align:'center',
					field : 'sk.company_id',
					formatter: function(value,row,index){
						return row.companyName;
					} 
				},{
					width : 110,
					title : '合同号',
					align:'center',
					field : 'c.con_no',
					formatter: function(value,row,index){
						return row.conNo; 
					} 
				},{
					width : 85,
					title : '姓名',
					align:'center',
					field : 'a.name',
					formatter: function(value,row,index){
						var szName = row.cstmName;
						if(szName == '') szName = '未录入';
						return "<a href='javascript:void(0)' onclick=\"jsEdit('" +row.id + "')\">" + szName + "</a>"; 
					} 
				},{
					width : 85,
					title : '客户来源1',
					align:'center',
					field : 'stu_from_id1',
					formatter: function(value,row,index){
						return row.stuFromName1; 
					} 
				},{
					width : 85,
					title : '客户来源',
					align:'center',
					field : 'stu.stu_from_id',
					formatter: function(value,row,index){
						return row.stuFromName; 
					} 
				},{
					width : 85,
					title : '收款方式',
					align:'center',
					field : 'sk.pay_type',
					formatter: function(value,row,index){
					  return row.payTypeName; 
					} 
				},{
					width : 80,
					title : '收款日期',
					align:'center',
					field : 'sk.sk_date',
					formatter: function(value,row,index){
						if(!row.skDate) return "";
					    var d = new Date(row.skDate);
					    return d.format('yyyy-MM-dd'); 
					} 
				},{
					width : 130,
					title : '收款项目',
					align : 'center',
					halign: 'center',
					field : 'itemId',
					formatter: function(value,row,index){
						return row.itemName;
					}
				},{
					width : 85,
					title : '合同类型1',
					align:'center',
					field : 'con_type1',
					formatter: function(value,row,index){
						return row.conTypeName1; 
					} 
				},{
					width : 85,
					title : '合同类型',
					align:'center',
					field : 'con_type',
					formatter: function(value,row,index){
						return row.conTypeName; 
					} 
				},{
					width : 85,
					title : '收款额',
					align:'right',
					halign:'center',
					field : 'sk_value',
					formatter:function(value,row,index){
						if(row.skValue !=null)
							return row.skValue.toFixed(2);	
					}
				},{
					width : 85,
					title : '业绩',
					align:'right',
					halign:'center',
					field : 'achivement',
					formatter:function(value,row,index){
						if(row.achivement !=null)
							return row.achivement.toFixed(2);
					}
				}] ],
				columns : [ [ 
				{
					width : 130,
					title : '签约国家',
					halign:'center',
					field : 'countryCodes',
					formatter: function(value,row,index){return row.countryNames; } 
				},{
					width : 75,
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
						if(!row.id) return '';
						var szGwName1 = (row.applyGwName==null?'':row.applyGwName);
						var szGwName2 = (row.writeGwName==null?'':row.writeGwName);
						return szGwName1 + '/' + szGwName2;
					}
				},{
					width : 85,
					title : '课程顾问',
					halign:'center',
					field : 'sk.kcgw_name',
					formatter: function(value,row,index){
						return row.kcgwName ;
					}
				},{
					width : 175,
					title : '备注',
					halign:'center',
					field : 'sk.memo',
					formatter: function(value,row,index){
						return row.memo ;
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
				width : 225,
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
				title : '收款金额',
				align:'right',
				field : 'skValue',
				formatter: function(value,row,index){
					return row.skValue; 
				},
				editor:'numberbox' 
			},{
				width : 300,
				title : '备注',
				field : 'memo',
				editor:'textbox' 
			}] ],
			onClickCell: onClickCell,
			onEndEdit: onEndEdit
		});
	});
	  
	function jsQuery() {
		grid.datagrid('clearSelections');
	/*
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		}); */
		var params =  $.serializeObject($('#searchForm'));
		params.footer = true;
		//params.[[${_csrf.parameterName}]] = '[[${_csrf.token}]]';
		grid.datagrid('load', params); 
	};	
    
	
	function jsExport() {
		$("#searchForm")[0].submit();
	};
	
    function selectUser(objName){
		$('#divUsers').find("input[name=objName]").val(objName);
		openUsersDiv();
	}
	
	function openUsersDiv(){
		var szCompanyId = $('#searchForm').find("select[name=companyId]").val();
		
		$('#divUsers').find('select[name=companyId]').empty();
		$("#companyId option").each(function(){
			var optionHtml = '<option value="' + $(this).val() + '">' + $(this).html() + '</option>';
			$('#divUsers').find('select[name=companyId]').append(optionHtml);
		});
		
		$('#divUsers').find('input[name=companyId]').val();
		
		var params = {companyId:szCompanyId, "size":6, "pageSize":5,"page":1};
		$('#divUsers').find("select[name=enabled]").val('');
			
		$('#divUsers').dialog('open');	
		
	}
	
	function jsDelete(){		
		var rows = $('#grid').datagrid('getSelections');
		if(rows.length == 0){
			$.messager.alert('提醒','请选择要删除的信息');
			return; 
		}
		var params = {id:rows[0].id};
		if(rows[0].status == 'AUDIT'){
			$.messager.alert('提醒','已删除数据不能删除');
			return;
		}
		
		$.messager.confirm('Confirm','确定删除?',function(r){
			if (!r)
				return;
			
			$.ajax({
				type: 'POST',
		 		url: 'delete.do',
		 		data: params,
		 		dataType: "json",
		 		error:function(){ alert('系统异常'); },
		 		success: function(json){
		 			if(json.errorCode == 200){
		 				$.messager.show({title:'提示信息', msg:'删除成功'});
		 				jsQuery();
		 			}else{
		 				$.messager.show({title:'提示信息', msg:json.moreInfo});		 			
		 			}
		 		}
		 	});	
		});
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
		$('#searchForm').find("input[name=" + objName + "Id]").val( $(sel).val() );
		if( $(sel).val() == '')
			$('#searchForm').find("input[name=" + objName + "Name]").val('');
		else	
			$('#searchForm').find("input[name=" + objName + "Name]").val( $(sel).find("option:selected").text() );
			
		$('#divUsers').dialog('close');
	}
    
    function selConType(){
		var roots = $('#conTypeTree').tree('getRoots');
		if(roots == null || roots.length ==0){	
			$('#conTypeTree').tree({
				lines:true,
				url : '/dict/treeData?rootCode=custcontract.type',
				checkbox:function(node){ return true; }
			});
		}	
		$('#divConTypeTree').dialog('open');
    }
    function setConType(){
    	var nodes = $('#conTypeTree').tree('getChecked');
        var sNames = '', sCodes='';
        for(var i=0; i<nodes.length; i++){
           if(i >0){
              sCodes += ',';
              sNames += ',';
           }  
           sCodes += nodes[i].code;
           sNames += nodes[i].text;
        }
            
    	$('input[name=conTypeCodes]').val(sCodes);
		$('input[name=conTypeNames]').val(sNames);
		$('#divConTypeTree').dialog('close');
    	
    }
    function clearConType(){
		$('input[name=conTypeCodes]').val('');
		$('input[name=conTypeNames]').val('');
		$('#divConTypeTree').dialog('close');
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
		$('#divFromTree').dialog('open');
    }
    function clearStuFrom(){
		$('input[name=stuFromId]').val('');
		$('input[name=stuFromName]').val('');
		$('#divFromTree').dialog('close');
    }
    
</script>	
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>收款信息</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" action="exportData.do">
	<table width="100%" border="0">
		<tr> 
			<td style="width: 65px;text-align:right">公司</td>
			<td style="width: 140px">
				<select name='companyId' id="companyId" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width:60px;text-align:right">姓名</td>
			<td style="width:130px">
				<input name="cstmName" id="cstmName" type="text" maxlength="50" style="width:85px;" />
			</td>
			<td style="width:60px;text-align:right">收款日期</td>
			<td style="width:200px">
				<input name="skDateFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="skDateTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>	
			<td style="width:60px;text-align:right">收款项目</td>
			<td style="width:140px">
				<select name='itemId' style="width:135px">
				<option value=''>请选择</option>
			<c:forEach var="item" items="${feeItemList}">
				<option value='${item.id}' >${item.dictName}</option>
			</c:forEach>
				</select>
			</td>
			<td style="width:60px;text-align:right">联系电话</td>
			<td ><input name="phone" type="text" maxlength="20" style="width:100px;" /></td>
		</tr>
		<tr>
			<td style="text-align:right">签约顾问</td>
			<td>
				<input type='text' name='signGwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('signGw')">...</a>
				<input type="hidden" name="signGwId" >
			</td>					
			<td style="text-align:right">咨询顾问</td>
			<td>
				<input type='text' name='zxgwName' readonly style="width:85px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('zxgw')">...</a>
				<input type="hidden" name="zxgwId" >
			</td>	
			<td align="right">录入日期</td>
			<td>
			  <input name="createdFrom" id="createdFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="createdTo"   id="createdTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="text-align:right">合同号</td>
			<td>
				<input type='text' name='conNo' style="width:110px" >
			</td>	 
			<td style="text-align:right">录入人</td>
			<td>
				<input type='text' name='creatorName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('creator')">...</a>
				<input type="hidden" name="creatorId" >
			</td>	   
		</tr>
		<tr>
			<td style="text-align:right">规划顾问</td>
			<td>
				<input type='text' name='planGwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('planGw')">...</a>
				<input type="hidden" name="planGwId" >
			</td>
			<td style="text-align:right">申请顾问</td>
			<td>
				<input type='text' name='applyGwName' readonly style="width:85px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('applyGw')">...</a>
				<input type="hidden" name="applyGwId" >
			</td>
			<td style="width:60px;text-align:right">合同类型</td>
			<td colspan='5'>
				<input type='text' name="conTypeNames" style='width:250px' readonly>
				<a href='javascript:;' onclick='selConType()'>...</a>
				<input type='hidden' name="conTypeCodes" >
			</td>
		</tr>
		<tr>
			<tr>
			<td style="text-align:right">客户来源</td>
			<td colspan='3'>
				<input type='text' name="stuFromName" style='width:292px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromId" value="">
			</td>	
			<td colspan='3'>		
				&nbsp;&nbsp; 
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" >查询</a>
			<c:if test="${canSkInput}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				onclick="jsAdd()" >收款</a>  
			</c:if>		
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" 
				onclick="jsDelete()" >删除</a>  
			<c:if test="${canExport}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext_page_excel'" 
				onclick="jsExport()" style="margin-left: 0px;" >导出</a> 
			</c:if>	
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

			
<div id='skDialog' class="easyui-dialog" style="width:750px;height:400px;padding:5px"
	  data-options="closed:true,title:'收款编辑',modal:true,
			buttons:[{
				text:'Save',
				handler:saveSkData
			},{
				text:'Close',
				handler:function(){$('#skDialog').dialog('close');}
			}]">
	<form name='skForm' id='skForm'>		
	<table>
		<tr>
			<td style='width:65px;text-align:right'><a href="javascript:void(0)" onclick="jsGetContract()">合同号</a></td>
			<td style='width:115px;'>
				<input type='text' name='conNo' style='width:110px' onblur="jsGetContract()"/>
				<input type='hidden' name='conId'/>
				<input type='hidden' name='cstmId'/>
				<input type='hidden' name='fetchedConNo'/> <!-- 上次获取过合同信息的 合同号 -->
			</td>
			<td style='width:65px;text-align:right'>款项公司</td>
			<td style='width:110px;'>
				<input type='text' name='companyName' style='width:100px' readonly='readonly' />
				<input type='hidden' name='companyId' />
			</td>
			<td style='width:65px;text-align:right'>姓名</td>
			<td style='width:100px;'><input type='text' name='cstmName' style='width:90px' readonly/></td>
			<td style='width:65px;text-align:right'>合同金额</td>
			<td >
				<input type='text' name='conValue' style='width:80px;text-align:right' readonly='readonly' />
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>签约日期</td>
			<td><input type='text' name='signDate' style='width:110px' readonly/></td>
			<td style='text-align:right'>签约顾问</td>
			<td >
				<input type='text' name='signGwName' style='width:100px' readonly='readonly' />
			</td>
			<td style='text-align:right'>规划顾问</td>
			<td >
				<input type='text' name='planGwName' style='width:90px' readonly='readonly' />
			</td>
			<td style='text-align:right'>申请顾问</td>
			<td >
				<input type='text' name='applyGwName' style='width:80px' readonly='readonly' />
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>收款日期</td>
			<td >
				<input type='text' name='skDate' style='width:110px' onclick="WdatePicker({maxDate:'%y-%M-%d'})" readonly='readonly' />
			</td>
		</tr>	
	</table>
	<div id="feeToolbar" style="height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addFeeLine()">Append</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">Remove</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">Accept</a>        
    </div>	
    </form>
    <table id="feeItemGrid" data-options="fit:true,border:false" style='height:auto'>
    </table>
</div>  
<div id='divConTypeTree' class="easyui-dialog"  style="width:500px;height:360px;padding:10px"
	 data-options="closed:true,title:'合同类型选择',buttons:[
	 	{
			text:'确定',
			handler:setConType
		},{
			text:'清空',
			handler:clearConType
		}]">
	<ul id="conTypeTree" class="easyui-tree" ></ul>
</div>  
<div id='divFromTree' class="easyui-dialog" data-options="closed:true,title:'客户来源选择',buttons:[{
				text:'clear',
				handler:clearStuFrom
			}]" style="width:500px;height:360px;padding:10px">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>	

<script language='javascript'>		
	function jsGetContract(){
		var conNo = $.trim($('#skForm').find("input[name=conNo]").val().toUpperCase());
		var fetchedConNo = $('#skForm').find("input[name=fetchedConNo]").val();
		if(conNo == fetchedConNo) return;
		
		$.ajax({
        	type:'post',
        	url:'/lx/contract/detailData.do',
        	data:{conNo:conNo},
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if(result.errorCode !=200){
            		$.messager.show( {title: 'Error',msg:result.error} ); 
            		return;
            	}
            	$('#skForm').find("input[name=fetchedConNo]").val(conNo);
            	
            	$('#skForm').find("input[name=conId]").val(result.data.conId);
            	$('#skForm').find("input[name=companyId]").val(result.data.companyId);
            	$('#skForm').find("input[name=companyName]").val(result.data.companyName);
            	$('#skForm').find("input[name=cstmId]").val(result.data.cstmId);
            	$('#skForm').find("input[name=cstmName]").val(result.data.cstmName);
            	$('#skForm').find("input[name=conValue]").val(result.data.conValue);
            	
            	if( result.data.signDate && result.data.signDate >0){
            		var szDate = (new Date(result.data.signDate)).format('yyyy-MM-dd');
            		$('#skForm').find("input[name=signDate]").val(szDate);
				}else{
					$('#skForm').find("input[name=signDate]").val('');
				}
            	
            	$('#skForm').find("input[name=signGwName]").val(result.data.signGwName);
            	$('#skForm').find("input[name=planGwName]").val(result.data.planGwName);
            	$('#skForm').find("input[name=applyGwName]").val(result.data.applyGwName);
            }
        });    	
	}
	function jsEdit(id){
		editIndex = undefined;
		$('#skDialog').dialog({title:'收款信息编辑'});
		$.ajax({
        	type:'post',
        	url:'detailData.do',
        	data:{id:id},
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if(result.errorCode !=200){
            		$.messager.show( {title: '错误提醒',msg:result.error} ); 
            		return;
            	}
            	
            	$('#skForm').form('clear');
            	$('#feeItemGrid').datagrid('loadData', { total: 1, rows: [result.data.skLine] });
		
				var conProps = result.data.contract;
				var skProps  = result.data.skLine;
				if(conProps){
					$('#skForm').form('load', conProps);
					var d = new Date(conProps.signDate);
					$('#skForm').find("input[name=signDate]").val( d.format('yyyy-MM-dd') );
					$('#skForm').find("input[name=conId]").val( conProps.id );
				}else{
					$('#skForm').find("input[name=cstmId]").val(skProps.cstmId);
					$('#skForm').find("input[name=cstmName]").val(skProps.cstmName);
				}
					
            	$('#skForm').find("input[name=fetchedConNo]").val(conProps.conNo);
            	$('#skForm').find("input[name=skDate]").val( (new Date(skProps.skDate)).format('yyyy-MM-dd') );
            	            	
            	$('#feeItemGrid').datagrid('resize', {width:650,height:400});
            	if( result.data && result.data.feeLines){
            		$.each(result.data.feeLines, function(index, item){
            			addFeeLine(item);
            		});
            	}
            	$('#skDialog').dialog('center');
            	$('#skDialog').dialog('open');
        	}
		});
	}	
	  
	function jsAdd(){
        $('#feeItemGrid').datagrid('loadData', { total: 0, rows: [] });
		$('#skForm').form('clear');
		$('#skForm').find("input[name=companyId]").val('${stu.companyId}');
		$('#skForm').find("input[name=companyName]").val('${stu.companyName}');
		addFeeLine();
		
		$('#skDialog').dialog({title:'收款信息录入'});
		$('#skDialog').dialog('center');
		$('#skDialog').dialog('open');
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
		if(data == null){
			data = {};
		}
		if (endEditing()){
			$('#feeItemGrid').datagrid('appendRow',data);
			editIndex = $('#feeItemGrid').datagrid('getRows').length-1;
			$('#feeItemGrid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);     
			//if(data != null)  accept();     
		}
	}
	
	function saveSkData(){        
		accept();
		var params =  $.serializeObject($('#skForm'));
		if(params.conId == ''){
			$.messager.alert( {title: '提醒',msg:'请录入合同号'} ); 
			return;
		}
		if(params.skDate == ''){
			$.messager.alert( {title: '提醒',msg:'请录入收款日期'} ); 
			return;
		}
		
		var feeLines = $('#feeItemGrid').datagrid('getRows');
		if(feeLines == null || feeLines.length == 0){
			$.messager.alert( {title: '提醒',msg:'请录入收款信息'} ); 
			return;
		}
		
		for(var i=0; feeLines != null && i < feeLines.length; i++){
			params["skLines[" + i + "].conId"]   = params.conId;
			params["skLines[" + i + "].cstmId"]   = params.cstmId;
			params["skLines[" + i + "].companyId"]   = params.companyId;
			params["skLines[" + i + "].skDate"]  = params.skDate;
			params["skLines[" + i + "].id"]      = feeLines[i].id;
			params["skLines[" + i + "].itemId"]  = feeLines[i].itemId;
			params["skLines[" + i + "].skValue"] = feeLines[i].skValue;
			params["skLines[" + i + "].memo"] = feeLines[i].memo;
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
                	$('#skDialog').dialog('close');     // close the dialog
                }
        	}
		});
	}
</script>
</body>
</html>