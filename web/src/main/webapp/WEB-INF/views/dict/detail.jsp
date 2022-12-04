<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="gb312">
<title>字典数据维护</title>
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
	<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
    <script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/_resources/easyui/jquery.edatagrid.js"></script>
<script type="text/javascript">
 /*<![CDATA[*/
	/*]]>*/
</script>
</head> 

<body >
	<span style='font-size:16pt;text-decoration:underline'>${dict.dictName}</span>
	<span>及下级节点维护</span>
	
	<table id="dg" title="下级节点" style="width:850px;height:420px"
            toolbar="#toolbar" pagination="true" 
            rownumbers="true" fitColumns="true" singleSelect="true">
    </table>
    <div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newDict()">New</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editDict()">Edit</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteDict()">Remove</a>
    </div>
    
    <div id="dlg" class="easyui-dialog" style="width:600px;height:380px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons">
        <div class="ftitle">${dict.dictName} 节点属性</div>
        <form id="fm" method="post" novalidate>
            <div class="fitem">
                <label>Code:</label>
                <input name="alias" class="easyui-textbox" >
                <input type='hidden' name="id" >
                <input type='hidden' name="parentId" value='${dict.id}'>
            </div>
            <div class="fitem">
                <label>名称:</label>
                <input name="dictName" class="easyui-textbox" required="true">
            </div>
            <div class="fitem">
                <label>描述:</label>
                <input name="dictDesc" class="easyui-textbox">
            </div>
            <div class="fitem">
                <label>关键字:</label>
                <input name="keywords" class="easyui-textbox">
            </div>
            <div class="fitem">
                <label>index:</label>
                <input name="showIndex" class="easyui-textbox" >
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveDict()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">Cancel</a>
    </div>
	<script type="text/javascript">
		$(function(){
			$('#dg').datagrid({
				title : '',
				url : '/dict/${dict.id}/children.do',
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,30,50,100],
				idField : 'id',
				columns : [ [ 
				{
					width : 80,
					title : 'id',
					align : 'left',
					field : 'id'
				},{
					width : 80,
					title : 'Index',	
					align : 'center',
					field : 'show_index',
					formatter: function(value,row,index){return row.showIndex; } 
				},{
					width : 100,
					title : '代码',
					align : 'left',
					field : 'dict_code',
					formatter: function(value,row,index){return row.dictCode; } 
				},{
					width : 100,
					title : '名称',	
					align:'center',
					field : 'dict_name',
					formatter: function(value,row,index){return row.dictName; } 
				},{
					width : 100,
					title : '描述',	
					align : 'center',
					field : 'dict_desc',
					formatter: function(value,row,index){return row.dictDesc; } 
				},{
					width : 100,
					title : '关键字',	
					align:'center',
					field : 'keywords',
					formatter: function(value,row,index){return row.keywords; } 
				}]]
			});	
        });
        
        function newDict(){
            $('#dlg').dialog('open').dialog('center').dialog('setTitle','New Dict');
            $('#fm').form('clear');
            $('#fm').find("input[name=parentId]").val('${dict.id}');
        }
        function editDict(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('center').dialog('setTitle','Edit Dict');
                $('#fm').form('load',row);
            }
        }
        
	function deleteDict(){
    	var row = $('#dg').datagrid('getSelected');
        if (!row){
        	alert('请选择要删除的行');
        	return;
        }	
        $.messager.confirm('删除确认', '确认要删除"' + row.dictName + '"吗？', function(r){
			if (!r)
				return;
				
			$.ajax({
            	type:'post',
            	data:{id:row.id},
                url: '/dict/delete',
                onSubmit: function(){
                    return $(this).form('validate');
                },
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
                        $('#dg').datagrid('reload');    // reload the user data
                    }
                }
            });   	
        });
	}
        
        function saveDict(){
        	var data = {};
        	data.id = $('#fm').find("input[name=id]").val();
        	data.parentId  = $('#fm').find("input[name=parentId]").val();
        	data.alias     = $('#fm').find("input[name=alias]").val();
        	data.dictName  = $('#fm').find("input[name=dictName]").val();
        	data.dictDesc  = $('#fm').find("input[name=dictDesc]").val();
        	data.keywords  = $('#fm').find("input[name=keywords]").val();
        	data.showIndex = $('#fm').find("input[name=showIndex]").val();
        	
            $.ajax({
            	type:'post',
            	data:data,
                url: '/dict/save',
                onSubmit: function(){
                    return $(this).form('validate');
                },
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
                        $('#dlg').dialog('close');      // close the dialog
                        $('#dg').datagrid('reload');    // reload the user data
                    }
                }
            });
        }
                
    </script>
</body>

</html>