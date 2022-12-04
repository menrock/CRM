<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>录取签证信息</title>
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
	
	var cstmId = '${student.cstmId}';
	var cstmName = '${cstmName}';
	
	function jsCreate(isOffer, isRefuse){
		$('#attachDesc').val('');
		$('#uploadForm').find("input[name=offer]" ).val(isOffer);
		$('#uploadForm').find("input[name=refuse]").val(isRefuse);
		
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
				url : 'attachData.do',
				queryParams:{cstmId:cstmId},
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
					title : '附件类型',
					align:'center',
					field : 'offer',
					formatter: function(value,row,index){
						if(row.offer){
							if(row.refuse)
								return '拒录信息';
							else
								return 'offer';
						}else{
							if(row.refuse)
								return '拒签信息';
							else
								return '签证页';
						}
					} 
				},{
					width : 200,
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
	    	    	var filepath = $("input[name='file']").val();
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
	    	        if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
	    	        	$.messager.alert('提示','上传图片限于bmp,png,gif,jpeg,jpg格式!','error');
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
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" onclick="jsCreate(1,0);">上传offer</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" onclick="jsCreate(1,1);">上传学校拒录信息</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" onclick="jsCreate(0,0);">上传签证页</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" onclick="jsCreate(0,1);">上传拒签信息</a>
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>

	<div id='divUpload' class="easyui-dialog" title="附件上传" data-options="closed:true" >
    		<form id="uploadForm" method="post" enctype="multipart/form-data" action="upload.do">
		        <div style="margin:20px;">		
		  <table>
		  <tr><td>      
		        合同
		        <select name='conId' id='conId'>
		        	<option value=''>请选择</option>
		        <c:forEach var="item" items="${contracts}" varStatus="status">
		        <option value='${item.conId}'>${item.conNo}</option>
		        </c:forEach> 	
		        </select>      </td></tr>
		   <tr><td>附件          
		            <input type="file" name="file" style="width:100%;" />
		            <input type="hidden" name="cstmId" value="${student.cstmId}" />
		            <input type="hidden" name="offer" />
		            <input type="hidden" name="refuse" />
		     </td></tr>
		     <tr><td>       
		           备注<textarea id='attachDesc' name="desc" style='width:400px;height:50px'></textarea>
		           </td></tr></table>
		        </div>
	        </form>
	        <div style="text-align:center;margin-bottom:20px;">
	            <a href="javascript:void(0)" onclick="submitUploadForm();" class="easyui-linkbutton" >上传</a>
	        </div>
	</div>			

</body>
</html>