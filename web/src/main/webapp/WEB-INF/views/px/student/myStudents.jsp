<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>我的客户</title>
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
		        {id: '<c:out value="${item.id}" />',  text: '<c:out value="${item.dictName}(${item.dictDesc})" />'}
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
				url : 'myStudentsData.do',
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
					field : 'name',
					sortable: true,
					formatter: function(value,row,index){
						var szName = row.customer.name;
						if(szName == '') szName = '未录入';
					  return "<a href='main.do?id=" + row.student.id +"' target='_blank'>" + szName + "</a>"; 
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
					width : 80,
					title : '咨询日期',
					halign:'center',
					field : 'inquire_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.student.inquireDate) return "";
					    var d = new Date(row.student.inquireDate);
					    return d.format('yyyy-MM-dd'); 
					} 
				},{
					width : '80',
					title : '咨询顾问',
					halign:'center',
					field : 'zxgw.zxgw_id',
					sortable: true,
					formatter: function(value,row,index){
						return row.zxgwName;
					}
				},{
					width : '120',
					title : '分配时间',
					halign:'center',
					field : 'zxgw.assign_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.assignDate) return "";
					    var d = new Date(row.assignDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					} 
				},{
					width : 100,
					title : '顾问评级',
					align:'center',
					field : 'zxgw.stu_level',
					sortable: true,
					formatter: function(value,row,index){return row.stuLevelName; } 
				},{
					width : 210,
					title : '顾问最近回访内容',
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
					width : 120,
					title : '最近回访时间',
					align:'left',
					field : 'zxgw.last_contact_date',
					sortable: true,
					formatter: function(value,row,index){
						if(!row.lastContactDate) return "";
					    var d = new Date(row.lastContactDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					}
				},{
					width : 70,
					title : '回访次数',
					align:'right',
					field : 'zxgw.contact_count',
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
						if(row.student.stuCity)
							return row.student.stuCity;
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
						return row.student.ownerName;
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
		var params =  $.serializeObject($('#searchForm'));
		//params.[[${_csrf.parameterName}]] = '[[${_csrf.token}]]';
		grid.datagrid('load', params); 
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
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div id="toolbar" style="display: none">
<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>我的客户管理</td></tr>
</table>
</c:if>

	<form name="searchForm" id="searchForm" method="post" >
	<table width="100%" border="0">
		<tr> 
			<td style="width: 85px;text-align:right">公司</td>
			<td style="width: 120px">
				<select name='company_id' id="company_id" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 70px;text-align:right">姓名</td>
			<td style="width: 200px">
				<input name="name" id="name" type="text" maxlength="50" style="width:80px;" />
				<select name="gender">
					<option value="">请选择</option>
					<option value="M" >男</option>
					<option value="F" >女</option>
				</select>	
			</td>
			<td style="width: 70px;text-align:right">联系电话</td>
			<td style="width: 205px"><input name="phone" type="text" maxlength="20" style="width:105px;" /></td>	
			<td style="width: 70px;text-align:right">入学时间</td>
			<td >
				<input type="text" name="planEnterYear" style="width:40px">年
				<select name="planEnterSeason" >
					<option value=''></option>
					<option value='春季' >春季</option>
					<option value='秋季' >秋季</option>
				</select>
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
			<td align="right">首次面访</td>
			<td>
				<input name="visit_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="visit_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>			    
		</tr>
		<tr>
			<td align="right">毕业/在读学校</td>
			<td>
				<input name="curr_school" type="text" maxlength="50" style="width:105px;" />
			</td>	
			<td style="text-align:right">咨询渠道</td>
			<td >
				<input type='text' name="stuFromName" style='width:150px' value="" readonly>
				<a href='javascript:;' onclick='selStuFrom()'>...</a>
				<input type='hidden' name="stuFromCode" value="">
			</td>
			<td align="right">分配时间</td> 
			<td>
				<input name="assign_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="assign_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
		</tr>
		<tr>	
			<td style="text-align:right">意向国家</td>
			<td>
			  <select id="country_codes" name="country_codes" class="easyui-combotree" panelWidth="150" style="width:140px;" multiple="multiple"></select>
			</td>
			<td style="text-align:right">申请学历</td>
			<td>
				<select id="plan_xl" name="plan_xl" class="easyui-combotree" panelWidth="200" multiple="multiple" style="width:160px;"></select>				
			</td>
			<td align="right">最后回访</td>
			<td >
				<input name="last_contact_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
				<input name="last_contact_to" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;"/>
			</td>
			<td align="right">回访次数</td>
			<td >
				<input type="text" name="contactCountMin" style="width:40px;" />到
				<input type="text" name="contactCountMax" style="width:40px;"/>
			</td>
		</tr>		
		<tr>
			<td align="right">顾问评级</td>
			<td colspan='7'>
				<select id="stu_level" name="stu_level" class="easyui-combotree" panelWidth="480" style="width:450px;" multiple="multiple"></select>
				&nbsp;
			
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
				
				<a href="add.do" class="easyui-linkbutton" data-options="iconCls:'icon-add'" 
				style="margin-left: 0px;" >新建</a> 
				<input type='checkbox' name='fuzzySearch' checked='true'/>模糊查询
			</td>
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>
<div id='divFromTree' class="easyui-dialog" data-options="closed:true,title:'咨询渠道选择',buttons:[{
				text:'clear',
				handler:clearStuFrom
			}]" style="width:500px;height:360px;padding:10px">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>	
</body>
</html>