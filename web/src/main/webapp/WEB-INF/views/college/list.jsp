<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="gb312">
<title>院校管理</title>
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
	<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
	<script type="text/javascript" src="/_resources/js/date.js"></script>
	<script type="text/javascript" src="/_resources/My97DatePicker/WdatePicker.js"></script>
	
	<script type="text/javascript">
	var grid;
	$(function(){
		grid = $('#grid').datagrid({
				title : '',
				url : 'listData.do',
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100],
				idField : 'id',
				columns : [ [ 
				{
					field : 'id',
					checkbox: true
				},{
					width : 80,
					title : 'Index',	
					align : 'center',
					field : 'show_index',
					formatter: function(value,row,index){return row.showIndex; } 
				},{
					width : 110,
					title : '国家',	
					align:'center',
					field : 'country_code',
					formatter: function(value,row,index){return row.countryCode; } 
				},{
					width : 110,
					title : '院校类型',	
					align:'center',
					field : 'col_type',
					formatter: function(value,row,index){return row.colType; } 
				},{
					width : 320,
					title : '英文名称',	
					align : 'center',
					field : 'en_name',
					formatter: function(value,row,index){
						return "<a href='javascript:void(0)' onclick='editCollege(" + row.id + ")'>" + row.enName + "</a>";	 
					} 
				},{
					width : 320,
					title : '中文名称',	
					align:'center',
					field : 'cn_name',
					formatter: function(value,row,index){return row.cnName; } 
				}]],
				toolbar : '#toolbar'
			});	
        });
	
	function jsQuery() {
        grid.datagrid('clearSelections');
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
            	var params =  $.serializeObject($('#searchForm'));
            	grid.datagrid({queryParams:params});
            }
		});
	};	
	
	function editCollege(collegeId){
		$('#collegeEditForm').form('clear');
		if(collegeId ==null){
    	    $('#collegeDlg').dialog('open').dialog('center').dialog('setTitle','增加院校');
    	    return;
    	} 
    	   
		$.ajax({
			type:'post',
			url:'collegeInfo.do',
			data:{id:collegeId},
			error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
           	},
           	success: function(result){
           		$('#collegeEditForm').form('load', result.data);
           	}
		});
        $('#collegeDlg').dialog('open').dialog('center').dialog('setTitle','修改院校');
	}
	
	function saveCollege(){
    	if( !$('#collegeEditForm').form('validate') )
        	return; 
        	
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});	
        var params =  $.serializeObject($('#collegeEditForm'));
        
        if(params.enName == ''){
        	$.messager.alert('Warning','英文名称不能为空');
        	return;
        }
        if(params.colType == '' || params.countryCode == '' ){
        	$.messager.alert('Warning','国家和院校类型不能为空');
        	return;
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
					$.messager.show({title: 'Error', msg: result.error});
                } else {
                	$.messager.show({title: '消息', msg: '保存成功'});
                }
            }
		});
    }                
</script>	
</head>
<body >


<c:if test="${singlePage}">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>资源管理</td></tr>
</table>
</c:if>
	
<div id="toolbar" style="display: none">
	<form name="searchForm" id="searchForm" method="post" >
	<table border="0">
		<tr> 
			<td style="width: 70px;text-align:right">国家</td>
			<td style="width: 120px">
				<select name='countryCode' id="countryCode" >
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${countryList}">
					<option value='${item.code}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 80px;text-align:right">院校类型</td>                
			<td style="width: 120px">
				<select name="colType" >
					<option value=''>请选择</option>
					<option value='低龄'>低龄</option>
					<option value='高中'>高中</option>
					<option value='大学'>大学</option>
					<option value='非美'>非美</option>
		   		</select>
			</td>
			<td style="width: 70px;text-align:right">英文名称</td>
			<td style="width: 150px">
				<input type='text' name='enName' style='width:130px'>
			</td>
			<td>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				  onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-add'"
				  onclick="editCollege()">增加</a>
			</td>
		</tr>	
	</table>
</div>
    
    <table id="grid" data-options="fit:true,border:false">
	</table>
    
    <div id="collegeDlg" class="easyui-dialog" style="width:600px;height:380px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons">
        <div class="ftitle">院校属性</div>
        <form id="collegeEditForm" method="post" novalidate>
            <div class="fitem">
                <label>国家:</label>
                <select name="countryCode" >
                	<option value=''>请选择</option>
                <c:forEach var="item" items="${countryList}" varStatus="status" >
		   			<option value='${item.code}'>${item.name}</option>
		   		</c:forEach>
		   		</select>
            </div>
            <div class="fitem">
                <label>英文名称:</label>
                <input name="enName" class="easyui-textbox">
                <input type='hidden' name="id" >
            </div>
            <div class="fitem">
                <label>中文名称:</label>
                <input name="cnName" class="easyui-textbox">
            </div>
            <div class="fitem">
                <label>Level:</label>
                <select name="level" >
                	<option value=''>请选择</option>
                	<option value='主推'>主推</option>
                	<option value='重推'>重推</option>
		   		</select>
            </div>
            <div class="fitem">
                <label>院校类型:</label>
                <select name="colType" >
					<option value=''>请选择</option>
					<option value='低龄'>低龄</option>
					<option value='高中'>高中</option>
					<option value='大学'>大学</option>
					<option value='非美'>非美</option>
		   		</select>
            </div>
            <div class="fitem">
                <label>index:</label>
                <input name="showIndex" class="easyui-textbox" >
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveCollege()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#collegeDlg').dialog('close')" style="width:90px">Cancel</a>
    </div>
    
</body>

</html>