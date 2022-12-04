<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.niu.crm.model.ZxgwCallbackRemind" %>
<%@ page import="com.niu.crm.model.type.CallbackRemindType" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>回访提醒管理</title>
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
		
		var now = new Date();	
		var default_latest_contact_to = now.format('yyyy-MM-dd') + ' 23:59';
		$('#searchForm').find('input[name=latest_contact_to]').val(default_latest_contact_to);
			
		grid = $('#grid').datagrid({
				title : '',
				url : 'listRemindData.do',
				queryParams:{latest_contact_to: default_latest_contact_to},
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
						return row.student.companyName;
					} 
				},{
					width : '85',
					title : '姓名',
					align:'center',
					field : 'c.name',
					sortable: true,
					formatter: function(value,row,index){
						var szName = row.cstmName;
						if(szName == '') szName = '未录入';
					  return "<a href='/lx/student/main.do?id=" + row.stuId +"' target='_blank'>" + szName + "</a>"; 
					} 
				}] ],
				columns : [ [ 
				{
					width : 180,
					title : '咨询渠道',
					halign:'center',
					field : 'stu_from_id',
					sortable: true,
					formatter: function(value,row,index){return row.student.stuFromName; } 
				},{
					width : 140,
					title : '意向国家',
					halign:'center',
					field : 'plan_country',
					sortable: true,
					formatter: function(value,row,index){return row.student.planCountry; } 
				},{
					width : 80,
					title : '申请学历',
					halign:'center',
					field : 'plan_xl',
					sortable: true,
					formatter: function(value,row,index){return row.student.planXl; } 
				},{
					width : 80,
					title : '所在城市',
					align:'center',
					field : 'stu_city',
					sortable: true,
					formatter: function(value,row,index){return row.student.stuCity; } 
				},{
					width : 100,
					title : '顾问评级',
					align:'center',
					field : 'zxgw.stu_level',
					sortable: true,
					formatter: function(value,row,index){return row.stuZxgw.stuLevelName; } 
				},{
					width : '80',
					title : '提醒对象',
					halign:'center',
					field : 'zxgw.zxgw_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.stuZxgw.zxgwName;
					}
				},{
					width : 90,
					title : '提醒类型',
					halign:'center',
					field : 'r.remind_type',
					sortable: true,
					formatter: function(value,row,index){
					    return row.remindTypeName; 
					}
				},{
					width : 120,
					title : '截止时间',
					halign:'center',
					field : 'r.latest_contact_time',
					sortable: true,
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
					sortable: true,
					formatter: function(value,row,index){
						if(row.contactTime){
							var d = new Date(row.contactTime);
							return d.format('yyyy-MM-dd HH:mm');
						}	 
					}
				},{
					width : 120,
					title : '提醒时间',
					halign:'center',
					field : 'r.created_at',
					sortable: true,
					formatter: function(value,row,index){
						var d = new Date(row.createdAt);
					    return d.format('yyyy-MM-dd HH:mm'); 
					}
				}
				] ],
				toolbar : '#toolbar'
			});
			
			jsQuery();
	  });
	  
	function jsQuery() {
		grid.datagrid('clearSelections');
	
		var params =  $.serializeObject($('#searchForm'));
		//params.[[${_csrf.parameterName}]] = '[[${_csrf.token}]]';
		grid.datagrid({url : 'listRemindData.do','queryParams': params}); 
	};	
	
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
	function selectUnit(){
		var szCompanyId = $('#company_id').val();
		var szUrl = '/unit/treeData?rootCode=root';
		if( szCompanyId != '')
			szUrl = '/unit/treeData?rootId=' + szCompanyId;
			
    	$('#unitTree').tree({
			lines:true,
			url : szUrl,
			onClick : function(node) {	
				$('#searchForm').find("input[name=unitId]").val(node.id);
				$('#searchForm').find("input[name=unitName]").val(node.text);
				
				$('#divUnitTree').dialog('close');
			}
		});
		$('#divUnitTree').dialog('open');
    }  
    function clearUnit(){
		$('#searchForm').find("input[name=unitId]").val('');
		$('#searchForm').find("input[name=unitName]").val('');
		$('#divUnitTree').dialog('close');
    }
	
	function openUsersDiv(){
		var szCompanyId = $('#searchForm').find("select[name=company_id]").val();
		
		$('#divUsers').find('select[name=companyId]').empty();
		$("#company_id option").each(function(){
			var optionHtml = '<option value="' + $(this).val() + '">' + $(this).html() + '</option>';
			$('#divUsers').find('select[name=companyId]').append(optionHtml);
		});
		
		$('#divUsers').find('input[name=companyId]').val();
		
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
		var objName  = $('#divUsers').find('input[name=objName]').val();
		
		var sel = $("#divUsers").find("select[name=gwId]");
		$('#searchForm').find("input[name=" + objName + "_id]").val( $(sel).val() );
		if( $(sel).val() == '')
			$('#searchForm').find("input[name=" + objName + "_name]").val('');
		else	
			$('#searchForm').find("input[name=" + objName + "_name]").val( $(sel).find("option:selected").text() );
				
		$('#divUsers').dialog('close');			
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>回访提醒</td></tr>
</table>
</c:if>
	<form name="searchForm" id="searchForm" method="post" >
	<table width="100%" border="0">
		<tr> 
			<td style="width: 70px;text-align:right">公司</td>
			<td style="width: 120px">
				<select name='company_id' id="company_id" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 70px;text-align:right">咨询渠道</td>
			<td style="width: 200px">
				<input type='text' name="stuFromNames" style='width:150px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromIds" value="">
			</td>
			<td style="width: 70px;text-align:right">分配时间</td>
			<td style="width: 260px">
				<input name="assign_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d', dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />到
				<input name="assign_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d', dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />
			</td>					
			<td style="width: 70px;text-align:right">咨询顾问</td>
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
			<td style="text-align:right">意向国家</td>
			<td>
			  <select id="country_codes" name="country_codes" class="easyui-combotree" panelWidth="150" style="width:140px;" multiple="multiple"></select>			  
			</td>
			<td align="right">提醒时间</td>
			<td>
			  <input name="created_from" id="created_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d', dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />到
			  <input name="created_to"   id="created_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d', dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />
			</td>
			<td style="text-align:right">顾问部门</td>
			<td>
				<input type='text' name='unitName' readonly style="width:150px" onclick="selectUser()">
				<a href='javascript:;' onclick="selectUnit()">...</a>
				<input type="hidden" name="unitId" >
			</td>	 	    
		</tr>
		<tr>
			<td style="text-align:right">提醒类型</td>
			<td >
				<select name='callbackType'>
					<option value=''>全部</option>
				<%
				  CallbackRemindType[] remindTypes = CallbackRemindType.values();	
				  for(CallbackRemindType typeItem:  remindTypes){
					out.println("<option value='" + typeItem.name() + "'>" + typeItem.getName() + "</option>");  
				  }
				%>	
				</select>	
			</td>
			<td style="text-align:right">回访状态</td>
			<td >
				<select name='callback_status'>
					<option value=''>全部</option>
					<option value='none'>未回访</option>
					<option value='finish'>已回访</option>
					<option value='ontime'>及时回访</option>
					<option value='delayed'>逾期</option>
				</select>
			</td>
			<td align="right">最迟回访</td>
			<td>
			  <input name="latest_contact_from" id="latest_contact_from" readonly type="text" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />到
			  <input name="latest_contact_to"   id="latest_contact__to"  readonly type="text" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm'})" style="width:110px;" />
			</td>
			<td colspan='2'>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
				<input type='checkbox' name='fuzzySearch' checked='true'/>模糊查询
				<input type='checkbox' name='onlyShowCurrent' checked='true'/>仅显示当期提醒
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