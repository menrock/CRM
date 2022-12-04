<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>资源管理</title>
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
		$('#plan_xl').combotree('loadData', [{
		        id: '',
		        text: '全部',
		        children: [
		   <c:forEach var="item" items="${xlList}" varStatus="status" >  
		   		<c:if test="${status.index >0}">,</c:if>
		        {
		                id: '<c:out value="${item.dictName}" />', 
		                text: '<c:out value="${item.dictName}" />'
		        }
		   </c:forEach>  
		        ]
			}]);
		
		$('#stu_level').combotree('loadData', [{
		        id: '',
		        text: '全部',
		        children: [
		   <c:forEach var="item" items="${levelList}" varStatus="status" >  
		   		<c:if test="${status.index >0}">,</c:if>
		   		{id: '<c:out value="${item.id}" />', text: '<c:out value="${item.dictName}(${item.dictDesc})" />'}
		   </c:forEach>  
		        ]
			}]);	
		
		$('#country_codes').combotree('loadData', [{
		        id: '',
		        text: '全部',
		        children: [
		   <c:forEach var="item" items="${countryList}" varStatus="status" >  
		   		<c:if test="${status.index >0}">,</c:if>
		        {id: '<c:out value="${item.code}" />', text: '<c:out value="${item.name}" />'}
		   </c:forEach>  
		        ]
			}]);
						
	  grid = $('#grid').datagrid({
				title : '',
				url : '/lx/student/listData.do',
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100,200],
				idField : 'id',
				frozenColumns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : '85',
					title : '公司',
					align:'center',
					field : 'a.company_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.companyName;
					} 
				},{
					width : '85',
					title : '姓名',
					align:'center',
					field : 'name',
					sortable: true,
					formatter: function(value,row,index){
						var szName = row.customer.name;
						if(szName == '') szName = '未录入';
					  return "<a href='main.do?id=" + row.id +"' target='_blank'>" + szName + "</a>"; 
					} 
				}] ],
				columns : [ [ 
				{
					width : 100,
					title : '最高评级',
					align:'center',
					field : 'stu_level',
					sortable: true,
					formatter: function(value,row,index){return row.stuLevelName; } 
				},{
					width : 180,
					title : '咨询渠道',
					halign:'center',
					field : 'stu_from_id',
					sortable: true,
					formatter: function(value,row,index){return row.stuFromName; } 
				},{
					width : 140,
					title : '意向国家',
					halign:'center',
					field : 'plan_country',
					sortable: true,
					formatter: function(value,row,index){return row.planCountryName; } 
				},{
					width : 80,
					title : '申请学历',
					halign:'center',
					field : 'plan_xl',
					sortable: true,
					formatter: function(value,row,index){return row.planXl; } 
				},{
					width : 80,
					title : '咨询日期',
					halign:'center',
					field : 'inquire_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.inquireDate) return "";
					    var d = new Date(row.inquireDate);
					    return d.format('yyyy-MM-dd'); 
					} 
				},{
					width : '80',
					title : '咨询顾问',
					halign:'center',
					field : 'zxgw_names',
					sortable: true,
					formatter: function(value,row,index){
						return row.zxgwNames;
					}
				},{
					width : '120',
					title : '首次分配',
					halign:'center',
					field : 'first_assign_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.firstAssignDate) return "";
					    var d = new Date(row.firstAssignDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					} 
				},{
					width : 120,
					title : '最后回访时间',
					align:'left',
					field : 'last_contact_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.lastContactDate) return "";
					    var d = new Date(row.lastContactDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					}
				},{
					width : 210,
					title : '最近回访内容',
					align:'left',
					field : 'lastContacts',
					formatter: function(value,row,index){
						if( !row.lastContactRecords )
						  return "";
						  
						szContacts = '';  
						for( idx=0; idx < row.lastContactRecords.length; idx++)
						{  
						   record = row.lastContactRecords[idx];
						   if(idx==0)
						     szContacts = record.contactText;
						   else
						     szContacts = szContacts + '<br>------------------<br>'
						                + record.contactText;
						}  
						return szContacts;
					} 
				},{
					width : 70,
					title : '回访次数',
					align:'right',
					field : 'contact_count',
					sortable: true,
					formatter:function(value,row,index){
						return row.contactCount;
					}
				},{
					width : 70,
					title : '所在城市',
					align:'right',
					field : 'stu_city',
					sortable: true,
					formatter:function(value,row,index){
						if(row.stuCity)
							return row.stuCity;
						else
							return '';
					}
				},{
					width : 80,
					title : '资源属主',
					align:'center',
					field : 'a.owner_id',
					sortable: true,
					formatter:function(value,row,index){
						return row.ownerName;
					}
				},{
					width : 80,
					title : '录入人',
					align:'center',
					field : 'a.creator_id',
					sortable: true,
					formatter:function(value,row,index){
						return row.creatorName;
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
	
	function jsExport() {
		$("#searchForm")[0].submit();
	};
	
	function custRecycle(){
		var szUrl = "assign/recycle.do";		
		var selRows = $('#grid').datagrid("getSelections");
		if( null == selRows || 0 == selRows.length){
			alert('请选择要操作的行');
			return;
		}
		if( !confirm("确认回收?")) return;
		
		var stuIds ;
		for(var i=0; i < selRows.length; i++){
			if(i>0)
				stuIds+=","+ selRows[i].id;
			else 
				stuIds= selRows[i].id;
		}	  
		var params = {stuIds: stuIds};
				
		$.ajax({
		  type: 'POST',
		  url: szUrl ,
		  data: params,
		  dataType: "json",
		  error:function(){ alert('系统异常'); },
		  success: function(json){
		  	if(json.errorCode == 200){
		  		alert('客户回收成功');
		  		jsQuery();
		  	}else{
		  		alert('回事失败-' + json.error);
		  	}
		  }
		});
	}
	
	var fromTree;
	function selStuFrom(){
	  if( !fromTree){
    	fromTree = $('#fromTree').tree({
			lines:true,
			url : '/dict/treeData?rootCode=stufrom'
		});
		$('#fromTree').tree({
			checkbox:function(node){ return true;}
		});
	  }
	  $('#divFromTree').dialog('open');
    } 
	
	function clearStuFrom(){
		$('input[name=stuFromIds]').val('');
		$('input[name=stuFromNames]').val('');
		$('#divFromTree').dialog('close');
    }
    function setStuFrom(){
    	var nodes = $('#fromTree').tree('getChecked');
        var sNames = '', sIds='';
        for(var i=0; i<nodes.length; i++){
           if(i >0){
              sIds += ',';
              sNames += ',';
           }  
           sIds += nodes[i].id;
           sNames += nodes[i].text;
        }
            
    	$('input[name=stuFromIds]').val(sIds);
		$('input[name=stuFromNames]').val(sNames);
		$('#divFromTree').dialog('close');
    }
    
	function selectUser(objName){
		$('#divUsers').find("input[name=stuIds]").val('');
		$('#divUsers').find("input[name=objName]").val(objName);
		openUsersDiv();
	}
	
	function openUsersDiv(){
		var szCompanyId = $('#searchForm').find("select[name=company_id]").val();
		
		$('#divUsers').find('select[name=companyId]').empty();
		$("#company_id option").each(function(){
			var optionHtml = '<option value="' + $(this).val() + '">' + $(this).html() + '</option>';
			$('#divUsers').find('select[name=companyId]').append(optionHtml);
		});
		
		var params = {companyId:szCompanyId, "size":6, "pageSize":5,"page":1};
		if($('#divUsers').find("input[name=stuIds]").val() != '')
			$('#divUsers').find("select[name=enabled]").val('true');
		else
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
		var szStuIds = $('#divUsers').find('input[name=stuIds]').val();
		var objName  = $('#divUsers').find('input[name=objName]').val();
		if(szStuIds == ''){
			var sel = $("#divUsers").find("select[name=gwId]");
			$('#searchForm').find("input[name=" + objName + "_id]").val( $(sel).val() );
			if( $(sel).val() == '')
				$('#searchForm').find("input[name=" + objName + "_name]").val('');
			else	
				$('#searchForm').find("input[name=" + objName + "_name]").val( $(sel).find("option:selected").text() );
				
		 	$('#divUsers').dialog('close');
		}
		else{
			doAllocate();
		}	
	}
	
	function jsDelete(){
		var selRow = $('#grid').datagrid("getSelected");
		if( null == selRow ){
			alert('请选择要操作的行');
			return;
		}
		
		$.messager.confirm('Confirm','确定删除?',function(r){
			if (!r)
				return;
				
			var szUrl = "delete.do";
			$.ajax({
				type: 'POST',
				url: szUrl ,
				data: {id:selRow.id},
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show( {title: '提示信息',msg:'删除成功'} ); 
						jsQuery();
					}else{
						$.messager.alert('错误提醒','删除失败-' + json.error);
					}
				}
			});	
		}); 
	}
	
	function custAllocate(){
		var selRows = $('#grid').datagrid("getSelections");
		if( null == selRows || 0 == selRows.length){
			alert('请选择要操作的行');
			return;
		}
		
		var stuIds ;
		for(var i=0; i < selRows.length; i++){
			if(i>0)
				stuIds+=","+ selRows[i].id;
			else 
				stuIds= selRows[i].id;
		}
		
		$('#divUsers').find("input[name=stuIds]").val(stuIds);
		openUsersDiv();
	}
	
	function doAllocate(){
		var params={};
		params.stuIds = $("#divUsers").find("input[name=stuIds]").val();
		params.zxgwId = $("#divUsers").find("select[name=gwId]").val();
		if( params.zxgwId == ''){
			alert('请选择顾问');
			return;
		}
		
		if( !confirm("确认分配?")) return;
		
		var szUrl = "assign/allocate.do";
		$.ajax({
		  type: 'POST',
		  url: szUrl ,
		  data: params,
		  dataType: "json",
		  error:function(){ alert('系统异常'); },
		  success: function(json){
		  	
		  	if(json.errorCode == 200){
		  		alert('分配成功');
		  		jsQuery();
		  	}else{
		  		alert('分配失败-' + json.error);
		  	}
		  	$('#divUsers').dialog('close');
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
  <tr><td width=100% align='center' height=30 class='title'>资源管理</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" action="exportData.do">
	<table width="100%" border="0">
		<tr> 
			<td style="width: 80px;text-align:right">公司</td>
			<td style="width: 150px">
				<select name='company_id' id="company_id" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 80px;text-align:right">姓名</td>
			<td style="width: 200px">
				<input name="name" id="name" type="text" maxlength="50" style="width:80px;" />
				<select name="gender">
					<option value="">请选择</option>
					<option value="M" >男</option>
					<option value="F" >女</option>
				</select>	
			</td>
			<td style="width: 80px;text-align:right">联系电话</td>
			<td style="width: 210px"><input name="phone" type="text" maxlength="20" style="width:105px;" /></td>					
			<td style="width: 80px;text-align:right">咨询顾问</td>
			<td>
				<input type='text' name='zxgw_name' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('zxgw')">...</a>
				<input type="hidden" name="zxgw_id" >
			</td>
		</tr>
		<tr>
			<td align="right">签约状态</td>
			<td>
				<select name="signFlag" style="width:105px;">
					<option value="">全部</option>
					<option value="true" >已签约</option>
					<option value="false" >未签约</option>
				</select>
			</td>		
			<td align="right">录入日期</td>
			<td>
			  <input name="created_from" id="created_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="created_to"   id="created_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td align="right">首次咨询</td>
			<td>
				<input name="inquire_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="inquire_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="text-align:right">录入人</td>
			<td>
				<input type='text' name='creator_name' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('creator')">...</a>
				<input type="hidden" name="creator_id" >
			</td>	 	    
		</tr>
		<tr>
			<td align="right">毕业/在读学校</td>
			<td>
				<input name="curr_school" type="text" style="width:105px;" />
			</td>
			<td style="text-align:right">咨询渠道</td>
			<td >
				<input type='text' name="stuFromNames" style='width:150px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromIds" value="">
			</td>
			<td align="right">首次面访</td>
			<td>
				<input name="visit_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="visit_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td style="text-align:right">资源属主</td>
			<td>
				<input type='text' name='owner_name' readonly style="width:90px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUser('owner')">...</a>
				<input type="hidden" name="owner_id" >
			</td>	 
		</tr>
		<tr>	
			<td style="text-align:right">意向国家</td>
			<td>
			  <select id="country_codes" name="country_codes" class="easyui-combotree" panelWidth="150" style="width:140px;" multiple="multiple"></select>			  
			</td>
			<td style="text-align:right">申请学历</td>
			<td>
				<select id="plan_xl" name="plan_xl" class="easyui-combotree" panelWidth="200" style="width:160px;" multiple="multiple"></select>				
			</td>
			<td align="right">分配时间</td> 
			<td>
				<input name="assign_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="assign_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td align="right">回访次数</td>
			<td >
				<input type="text" name="contactCountMin" style="width:40px;" />到
				<input type="text" name="contactCountMax" style="width:40px;"/>
			</td>
		</tr>		
		<tr>
			<td align="right">顾问评级</td>
			<td colspan='3'>
				<select id="stu_level" name="stu_level" class="easyui-combotree" panelWidth="450" style="width:440px;" multiple="multiple"></select>
			</td>
			<td align="right">最后回访</td>
			<td >
				<input name="last_contact_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="last_contact_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;"/>
			</td>	
			<td colspan='2'>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
			<c:if test="${canExport}">	
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext_page_excel'" 
				onclick="jsExport()" style="margin-left: 0px;" >导出</a> 
			</c:if>	
				
				<a href='javascript:;' onclick='jsDelete()'>删除</a>
				<input type='checkbox' name='fuzzySearch' checked='true'/>模糊查询
			</td>
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>
	
<div id='divUsers' class="easyui-dialog" title="选择顾问" data-options="closed:true,buttons:[{
				text:'确定',
				handler:userSelected
			},{
				text:'close',
				handler:function(){$('#divUsers').dialog('close');}
			}]" style="width:420px;height:200px;padding:10px">
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
</div>
<div id='divFromTree' class="easyui-dialog" data-options="closed:true,title:'咨询渠道选择',buttons:[{
				text:'确定',
				handler:setStuFrom
			},{
				text:'清空',
				handler:clearStuFrom
			}]" style="width:500px;height:360px;padding:10px">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>
</body>
</html>