<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>合同附件</title>
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
	
	var grid;	
	
	var conId = '${contract.id}';
	var cstmId = '${contract.cstmId}';
	var cstmName = '${contract.cstmName}';
	
	function jsCreate(attachType){
		$('#attachDesc').val('');
		$('#uploadForm').find("[name=file]").val('');
		$('#uploadForm').find("[name=attachType]").val(attachType);
		
		if(attachType == 'CONTRACT')
		  $('#divUpload').dialog({'title':'上传合同信息'});
		else if(attachType == 'BCHT')
		  $('#divUpload').dialog({'title':'上传补充协议'});
		else if(attachType == 'SHOUJU')
		  $('#divUpload').dialog({'title':'上传收据'});
		else if(attachType == 'PLAN')
		  $('#divUpload').dialog({'title':'规划书'});
		else if(attachType == 'RESOURCES')
		  $('#divUpload').dialog({'title':'学生素材'});
		else if(attachType == 'WSTNFB')
		  $('#divUpload').dialog({'title':'文书头脑风暴'});
		else if(attachType == 'WSOUTLINE')
		  $('#divUpload').dialog({'title':'文书Outline'});
		else if(attachType == 'WSFINAL')
		  $('#divUpload').dialog({'title':'文书终稿'});
		else if(attachType == 'DXD')
		  $('#divUpload').dialog({'title':'院校与专业申请确认单'});
		else if(attachType == 'EMAIL_PWD_EID')
		  $('#divUpload').dialog({'title':'Email&EID&PWD'});
		else if(attachType == 'WSQRY')
		  $('#divUpload').dialog({'title':'网申完成确认页'});
		else if(attachType == 'END_DELAY')
		  $('#divUpload').dialog({'title':'结案延期说明书'});
		else
		  $('#divUpload').dialog({'title':'上传附件'});
		
		$('#divUpload').dialog('center');
		$('#divUpload').dialog('open');
	}
	function jsDelete(){
		var selRow = $('#grid').datagrid("getSelected");
		if( null == selRow ){
			alert('请选择要删除的附件');
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
						$('#grid').datagrid('reload');
					}else{
						$.messager.alert('错误提醒','删除失败-' + json.error);
					}
				}
			});	
		}); 
	}
		
	$(function() {			
	  grid = $('#grid').datagrid({
				title : '',
				url : 'custAttachData.do',
				queryParams:{conId:conId},
				striped:true,
				rownumbers : true,
				idField : 'id',
				columns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : 215,
					title : '附件名称',
					align:'center',
					field : 'attachName',
					formatter: function(value,row,index){
						return '<a href="download.do?id=' + row.id + '">' + row.attachName + '</a>';
					} 
				},{
					width : 100,
					title : '类型',
					align:'center',
					field : 'attach_type',
					formatter: function(value,row,index){
						return row.attachTypeName;
					} 
				},{
					width : 300,
					title : '备注',
					align:'center',
					field : 'attachDesc'
				},{
					width : 120,
					title : '上传时间',
					align:'center',
					field : 'created_at',
					formatter: function(value,row,index){
						if(row.createdAt)
							return new Date(row.createdAt).format('yyyy-MM-dd HH:mm');
						else
							return '';
					} 
				}] ],
				toolbar : '#toolbar'
		});
			
	
		$('#uploadForm').form({
			onSubmit: function(){
	    	    	var filepath=$("input[name='file']").val();
	    	    	var szConId = $("#conId").val();
	    	    	if(szConId == ''){
	    	    		$.messager.alert('提示','请选择对应合同!','info');
		    	        return false;
	    	    	}
	    	    	if(filepath.length==0){
	    	    		$.messager.alert('提示','请选择所要上传的文件!','info');
		    	        return false;
	    	    	}
	    	        var extStart=filepath.lastIndexOf(".");
	    	        var ext=filepath.substring(extStart,filepath.length).toUpperCase();
	    	        if(ext!=".PDF"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
	    	        	$.messager.alert('提示','上传图片限于pdf,png,gif,jpeg,jpg格式!','error');
		    	        return false;
	    	        }
	    	        $.messager.progress({text:'上传中...'});
	    	        return true;
	    	    },
	    	    success:function(data){
	    	    	$.messager.progress("close");
	    	    	
	    	    	$('#divUpload').dialog('close');
	    	    	$('#grid').datagrid('reload');
	    	    }
			});
		}); 
	
	function submitUploadForm(){
		$('#uploadForm').submit();
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">	
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="jsDelete();">删除</a>
    
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('CONTRACT')">合同信息</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('BCHT')">补充协议</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('SHOUJU')">收据</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('PLAN')">规划书</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('RESOURCES')">学生素材</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('WSTNFB')">文书头脑风暴</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('WSOUTLINE')">文书Outline</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('WSFINAL')">文书终稿</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('DXD')">院校与专业申请确认单</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('EMAIL_PWD_EID')">Email&EID&PWD</a>  
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('GTJL')">沟通记录</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('BSTC')">博士套磁</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('XXSQ')">夏校申请</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('WSQRY')">网申完成确认页</a>
    <a href="javascript:void(0)" plain="true" onclick="jsCreate('END_DELAY')">结案延期说明书</a>
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>

	<div id='divUpload' class="easyui-dialog" title="附件上传"
		style="width:500px;height:200px;padding:5px" data-options="closed:true" >
	<form id="uploadForm" method="post" enctype="multipart/form-data" action="upload.do">
	<table>
		<tr>
			<td style='width:50px;text-align:right'>附件</td>
			<td>
				<input type="file" name="file" style="width:100%;" />
		        <input type="hidden" name="attachType" />
		        <input type="hidden" name="conId" value="${conId}" />
		        <input type="hidden" name="cstmId" />
		    </td>
		</tr>
		<tr>
			<td style='text-align:right'>备注</td>
			<td>
				<textarea id='attachDesc' name="desc" style='width:400px;height:50px'></textarea>
		    </td>
		</tr>
		<tr>
			<td colspan='2' style='text-align:center'>
				<a href="javascript:void(0)" onclick="submitUploadForm();" class="easyui-linkbutton" >上传</a>
			</td>
		</tr>	
		</table>
	    </form>
	</div>	
</body>
</html>