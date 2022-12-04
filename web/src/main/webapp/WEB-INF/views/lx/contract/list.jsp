<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>合同管理</title>
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
	var conTypes = new Array();
	<c:forEach var="item" items="${conTypeList}" varStatus="status" >  
		conTypes.push({id:'<c:out value="${item.id}" />', dictName:'<c:out value="${item.dictName}" />'});</c:forEach>
	
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
					width : '85',
					title : '签约公司',
					align:'center',
					field : 'c.company_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.companyName;
					} 
				},{
					width : 95,
					title : '合同号',
					align:'center',
					field : 'c.con_no',
					sortable: true,
					formatter: function(value,row,index){
						var szConNo = row.conNo;
						if(szConNo == '') szConNo = '--';
						return "<a href='main.do?id=" + row.id + "' target='_blank'>" + szConNo + "</a>"; 
					} 
				},{
					width : 80,
					title : '合同类型',
					align:'center',
					field : 'c.con_type',
					sortable: true,
					formatter: function(value,row,index){
						return row.conTypeName; 
					} 
				},{
					width : '85',
					title : '姓名',
					align:'center',
					field : 'a.name',
					sortable: true,
					formatter: function(value,row,index){
						var szName = row.cstmName;
						if(szName == '') szName = '未录入';
						return "<a href='/lx/student/main.do?id=" + row.stuId + "' target='_blank'>" + szName + "</a>"; 
					} 
				},{
					width : 70,
					title : '标准金额',
					align:'right',
					halign:'center',
					field : 'conValue'
				},{
					width : 70,
					title : '优惠金额',
					align:'right',
					halign:'center',
					field : 'conDiscount'
				},{
					width : 70,
					title : '应收金额',
					align:'right',
					halign:'center',
					field : 'conInv',
					formatter: function(value,row,index){
						return row.conValue - row.conDiscount;
					}
				},{
					width : 70,
					title : '收款额',
					align:'right',
					halign:'center',
					field : 'skValue'
				},{
					width : 100,
					title : '首次收款',
					align:'center',
					field : 'c.first_sk_date',
					formatter: function(value,row,index){
						if(!row.firstSkDate) return "";
					    var d = new Date(row.firstSkDate);
					    return d.format('yyyy-MM-dd');
					} 
				}] ],
				columns : [ [ 
				{
					width : 140,
					title : '签约国家',
					halign:'center',
					field : 'countryCodes',
					sortable: true,
					formatter: function(value,row,index){return row.countryNames; } 
				},{
					width : 80,
					title : '来源',
					halign:'center',
					field : 'b.stu_from_id',
					formatter: function(value,row,index){
						return row.stuFromName;
					}
				},{
					width : 80,
					title : '客户地区',
					halign:'center',
					field : 'b.stu_city',
					formatter: function(value,row,index){
						return row.stuCity;
					}
				},{
					width : '80',
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
					sortable: true,
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
					sortable: true,
					formatter: function(value,row,index){
						if(!row.signDate) return "";
					    var d = new Date(row.signDate);
					    return d.format('yyyy-MM-dd'); 
					} 
				},{
					width : 80,
					title : '归档日期',
					align:'center',
					field : 'archive_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.archiveDate) return "";
					    var d = new Date(row.archiveDate);
					    return d.format('yyyy-MM-dd'); 
					} 
				},{
					width : '120',
					title : '录入时间',
					align : 'center',
					field : 'c.created_at',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.createdAt) return "";
					    var d = new Date(row.createdAt);
					    return d.format('yyyy-MM-dd hh:mm');
					} 
				}
				] ],
				toolbar : '#toolbar'
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
					
					$('input[name=stuFromCode]').val(node.code);
					$('input[name=stuFromName]').val(szTxt);
					$('#divFromTree').dialog('close');
				}
			});
		}	
		$('#divFromTree').dialog('open');
    }
    function clearStuFrom(){
		$('input[name=stuFromCode]').val('');
		$('input[name=stuFromName]').val('');
		$('#divFromTree').dialog('close');
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
    
    function selectUser(objName){
		$('#divUsers').find("input[name=stuIds]").val('');
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
    
	function filterConTypes(value,name){
		if($.trim(value) == ''){
			reallyFilterConTypes(conTypes);
		}
			
		$.ajax({
        	type:'post',
        	url:'/dict/contractTypes',
        	data:{keywords:value},
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(json){
            	reallyFilterConTypes(json.rows);
            	/*
            	var arrData1 = $('#conType').combobox('getData');
            	
            	var values = $('#conType').combobox('getValues');
            	for(j=0; j < arrData1.length; j++){
            		var bShow = false;
            		for(i=0; i < values.length; i++){
            			if(arrData1[j].value == values[i]){
            				bShow = true;
            				arrData2.push(arrData1[j]);
            				break;
            			}	
            		}
            		if(bShow) continue;
            		for(i=0; i < json.rows.length; i++){
            			if(arrData1[j].value == json.rows[i].id){
            				bShow = true;
            				arrData2.push(arrData1[j]);
            				break;
            			}	
            		}
            	}
            
		 		$('#conType').combobox('loadData', arrData2);
		 		*/
            }
        });    	
	}
	
	function reallyFilterConTypes(arrConTypes){
        var arrData = new Array();
        var values = $('#conTypes').combobox('getValues');
        for(j=0; j < conTypes.length; j++){
        	var conType = conTypes[j];
        	var bShow = false;
            for(i=0; i < values.length; i++){
            	if(conType.id == values[i]){
            		bShow = true;
            		arrData.push(conType);
            		break;
            	}
            }
            if(bShow) continue;
            for(i=0; i < arrConTypes.length; i++){
            	arrData.push(arrConTypes[i]);
            }
		}
		$('#conTypes').combobox('loadData', arrData);
	}
	
	/* 合同归档 */
	function jsArchive(){
		var selRow = $('#grid').datagrid("getSelected");
		if( null == selRow ){
			alert('请选择要操作的行');
			return;
		}
		
		$.messager.confirm('Confirm','确定归档?',function(r){
			if (!r)
				return;
				
			$.ajax({
				type: 'POST',
				url: "/contract/archive.do" ,
				data: {id:selRow.conId},
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show( {title: '提示信息',msg:'归档成功'} ); 
						jsQuery();
					}else{
						$.messager.alert('错误提醒','归档失败-' + json.error);
					}
				}
			});	
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
	<form name="searchForm" id="searchForm" method="post" action="exportData.do">
	<table width="100%" border="0">
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
			<td style="width: 200px">
				<input name="cstmName" id="cstmName" type="text" maxlength="30" style="width:90px;" />
			</td>
			<td style="width: 70px;text-align:right">合同号</td>
			<td style="width:200px">
				<input name="conNo" id="conNo" type="text" maxlength="50" style="width:120px;" />
			</td>						
			<td style="width: 70px;text-align:right">客户来源</td>
			<td >
				<input type='text' name="stuFromName" style='width:150px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromCode" value="">
			</td>
		</tr>
		<tr>
			<td align="right">咨询顾问</td>
			<td>
				<input type='text' name='zxgwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('zxgw')">...</a>
				<input type="hidden" name="zxgwId" >
			</td>
			<td align="right">签约日期</td>
			<td>
				<input name="signFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="signTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>		
			<td align="right">录入日期</td>
			<td>
			  <input name="createdFrom" id="createdFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="createdTo"   id="createdTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="text-align:right">录入人</td>
			<td>
				<input type='text' name='creatorName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('creator')">...</a>
				<input type="hidden" name="creatorId" >
			</td>	    
		</tr>
		<tr>
			<td style="text-align:right">签约顾问</td>
			<td>
				<input type='text' name='signGwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('signGw')">...</a>
				<input type="hidden" name="signGwId" >
			</td>
			<td style="text-align:right">规划顾问</td>
			<td>
				<input type='text' name='planGwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('planGw')">...</a>
				<input type="hidden" name="planGwId" >
			</td>
			<td style="text-align:right">申请顾问</td>
			<td>
				<input type='text' name='applyGwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('applyGw')">...</a>
				<input type="hidden" name="applyGwId" >
			</td>	  
			<td style="text-align:right">写作顾问</td>
			<td>
				<input type='text' name='writeGwName' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('writeGw')">...</a>
				<input type="hidden" name="writeGwId" >
			</td>	 
		</tr>
		<tr>				
			<td style="text-align:right">合同类型</td>
			<td colspan='3'>
				<input type='text' name="conTypeNames" style='width:320px' readonly>
				<a href='javascript:;' onclick='selConType()'>...</a>
				<input type='hidden' name="conTypeCodes" >
			</td>
			<td align="right">归档日期</td>
			<td>
			  <input name="archiveFrom" id="archiveFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="archiveTo"   id="archiveTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td colspan='2'>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" >查询</a> 
			<c:if test="${canExport}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext_page_excel'" 
				onclick="jsExport()" style="margin-left: 0px;" >导出</a> 
			</c:if>	
			
			 
			<c:if test="${canArchive}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" onclick="jsArchive()" 
					style="margin-left: 10px;" >归档</a> 
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
<div id='divFromTree' class="easyui-dialog"  style="width:500px;height:360px;padding:10px"
	 data-options="closed:true,title:'客户来源选择',buttons:[{
				text:'clear',
				handler:clearStuFrom
			}]">
	<ul id="fromTree" class="easyui-tree" ></ul>
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
</body>
</html>