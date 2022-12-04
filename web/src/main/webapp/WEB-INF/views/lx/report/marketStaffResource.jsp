<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>资源统计报表</title>
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
	var grid =null;
	$(function() {
		queryParams =  $.serializeObject($('#searchForm'));
		
		grid = $('#grid').datagrid({
			title : '',
			striped:true,
			showFooter:true,
			rownumbers : true,
			pagination : true,
			pageSize : 15,
			pageList: [15,30,50,100,200],
			idField : 'id',
			columns : [ [ 
			{
				width : 120,
				title : '分公司',
				align:'center',
				field : 'a.company_id',
				sortable: true,
				formatter:function(value,row,index){
					return row.companyName;
				}
			},{
				width : 100,
				title : '资源属主',
				halign:'center',
				field : 'a.owner_id',
				sortable: true,
				formatter:function(value,row,index){
					return row.ownerName;
				}
			},{
				width : 100,
				title : '客户姓名',
				halign:'center',
				field : 'cstmName'
			},{
				width : 90,
				title : '创建日期',
				halign:'center',
				field : 'a.created_at',
				formatter:function(value,row,index){
				    var d = new Date(row.createdAt);
				    return d.format('yyyy-MM-dd');
				}
			},{
				width : 150,
				title : '客户来源',
				halign:'center',
				field : 'a.stu_from_id',
				formatter:function(value,row,index){
					return row.stuFromName;
				}
			},{
				width : 180,
				title : '跟进顾问及评级',
				halign:'center',
				align:'center',
				field : 'zxgw_ids',
				formatter:function(value,row,index){
					var records = row.zxgwList;
					if( !records )
						return "";
						  
					szText = '';  
					for( idx=0; idx < records.length; idx++){  
						record = records[idx];
						if(idx >0)
						     szText = szText + '<br>------------------<br>';
						     
						szText += '[' + record.zxgwName + ']';
						if( record.stuLevel !=null )
							szText += record.stuLevelName;
					}  
					return szText;					
				}
			},{
				width : 280,
				title : '顾问回访记录',
				halign:'center',
				field : 'contactRecords',
				formatter:function(value,row,index){
					var records = row.contactRecords;
					if( !records )
						return "";
						  
					szContacts = '';  
					for( idx=0; idx < records.length; idx++){  
					   record = records[idx];
					   if(idx >0)
						     szContacts = szContacts + '<br>------------------<br>';
						     
					   record.contactText;
					   szContacts = szContacts + '[' + record.gwName + ']'
					                + record.contactText;
					}  
					return szContacts;
				}
			},{
				width : 160,
				title : '无效时间',
				halign:'center',
				align:'right',
				field : 'invalidDates',
				formatter:function(value,row,index){
				}
			}
			] ],
			toolbar : '#toolbar'
		});
	});
	  
	function jsQuery() {
        grid.datagrid('clearSelections');
        var params =  $.serializeObject($('#searchForm'));
        if(params.createdFrom == ''){
        	$.messager.show( {title: '信息',msg:'请选择录入日期'} ); 
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
            	grid.datagrid({url : 'marketStaffResourceData.do', queryParams:params});
            }
		});
	};	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
	<table width=800px>
		<tr><td height=10></td></tr>
		<tr><td width=100% align='center' height=30 class='title'>市场人员资源统计报表</td></tr>
	</table>
	<form name="searchForm" id="searchForm" method="post">
	<table border="0">
		<tr> 
			<td style="width:80px;text-align:right">分公司</td>
			<td style="width:120px">
				<select name='companyId' id="company_id" style="width:115px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width:80px;text-align:right">录入日期</td>
			<td style="width:220px">
			  <input name="createdFrom" id="createdFrom" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="createdTo"   id="createdTo"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
			<td >
				<a href="javascript:void(0);" class="easyui-linkbutton" style="margin-left: 20px;" 
				  onclick="jsQuery()" >统计</a> 
			</td>
		</tr>
	</table>
</form>	
</div>	

	<table id="grid" data-options="fit:true,border:false">
	</table>

</body>
</html>