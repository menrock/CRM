<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="gb312">
<title>院校合作机构维护</title>
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
	$(function(){
		$('#grid').datagrid({
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
				width : 360,
				title : '英文名称',	
				align : 'left',
				field : 'en_name',
				formatter: function(value,row,index){
				var szName = row.enName; 
				if( !row.enName || row.enName == '')
					szName = '-';
							
					return "<a href='javascript:void(0)' onclick='editOrg(" + row.id + ")'>" + szName + "</a>";	 
				} 
			},{
				width : 150,
				title : '简称',	
				align : 'left',
				field : 'abbr_name',
				formatter: function(value,row,index){
					if(row.abbrName != null && row.abbrName !='')
						return row.abbrName; 
				} 
			},{
				width : 260,
				title : '中文名称',	
				align : 'left',
				field : 'cn_name',
				formatter: function(value,row,index){
					if(row.cnName != null && row.cnName !='')
						return row.cnName; 
				} 
			},{
				width : 140,
				title : '录入时间',	
				align : 'center',
				field : 'created_at',
				formatter: function(value,row,index){
					if(row.createdAt == null) return "";
				    var d = new Date(row.createdAt);
				    return d.format('yyyy-MM-dd hh:mm');
				}    
			}]]
		});	
	});
	
	function editOrg(orgId){
		$('#orgEditForm').form('clear');
		if(orgId ==null){
    	    $('#orgDlg').dialog('open').dialog('center').dialog('setTitle','增加合作机构');
    	    return;
    	} 
    	
    	   
		$.ajax({
			type:'post',
			url:'orgInfo.do',
			data:{id:orgId},
			error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
           	},
           	success: function(result){
           		$('#orgEditForm').form('load', result.data);
           	}
		});
        $('#orgDlg').dialog('open').dialog('center').dialog('setTitle','修改合作机构');
	}
	
	function saveOrg(){
    	if( !$('#orgEditForm').form('validate') )
        	return;        		
        var params =  $.serializeObject($('#orgEditForm'));
        
        if(params.enName == ''){
        	$.messager.alert('Warning','英文名称不能为空');
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
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="editOrg(null)">New</a>
    </div>
    
    <table id="grid" data-options="fit:true,border:false">
	</table>
    
    <div id="orgDlg" class="easyui-dialog" style="width:600px;height:380px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons">
        <div class="ftitle">院校属性</div>
        <form id="orgEditForm" method="post" novalidate>
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
                <label>简称:</label>
                <input name="abbrName" class="easyui-textbox">
            </div>
            <div class="fitem">
                <label>index:</label>
                <input name="showIndex" class="easyui-textbox" >
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveOrg()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#orgDlg').dialog('close')" style="width:90px">Cancel</a>
    </div>
    
</body>

</html>