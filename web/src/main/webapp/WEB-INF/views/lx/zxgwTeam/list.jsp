<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>咨询顾问分组</title>
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
				title : '组长',
				align:'center',
				field : 'leaderName',
				sortable: true,
				formatter: function(value,row,index){
					var szVal = row.leaderName;
					return "<a href='javascript:void(0)' onclick=\"editTeam('" + row.leaderId + "')\">" + szVal + "</a>";
				}
			},{
				width : 820,
				title : '组员',
				align : 'left',
				field : 'memeberNames'
			}
			] ],
			toolbar : '#toolbar'
		});
	});
	
	function editTeam(leaderId){
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
            	doEditTeam(leaderId);
            }
		});
	};
    
    function doEditTeam(leaderId){
		$('#teamForm').form('clear');
		
		$.ajax({
			type:'post',
			url :'teamInfo.do',
			data:{leaderId:leaderId},
			error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	$('#teamForm').form('load', result.data);
            	
            	$("#divMembers").html('');
            	for(i=0; i < result.data.members.length; i++){
            		member = result.data.members[i];
            		addMember(member);
            	}
            }
		});
		if(leaderId == null)
			$('#teamDlg').dialog('open').dialog('center').dialog('setTitle','新建顾问组');
		else
			$('#teamDlg').dialog('open').dialog('center').dialog('setTitle','修改顾问组');
	}
	
	function addMember(props){
		if(props == null)
			props = {zxgwId:'', zxgwName:''};
			
		var liHtml = null;
		liHtml = "<td style='width:100px;float:left;'>"
				+ "  <input type='hidden' name='zxgwId' value='" + props.zxgwId + "' />"
				+ "  <input type='text' name='zxgwName' value='" + props.zxgwName + "' style='width:80px' readonly />"
            	+ "</td>"
            	+ "<td>"
            	+ "  <a href='javascript:void(0)' onclick='selMember(this)'>选择</a> &nbsp; "
            	+ "  <a href='javascript:void(0)' onclick='delMember(this)'>删除</a>"
            	+ "</td>";
		$("#divMembers").append("<tr>" + liHtml +"</tr>");
	}
	function delMember(obj){
		var pNode = obj.parentNode;
		var tr;
		while(pNode){
			if(pNode.tagName == 'TR'){
				tr = pNode;
				break;
			}	
			pNode = pNode.parentNode;
		}
		if(tr !=null)
			$(tr).remove();
	}
	
	//当前操作的行
	var gTR = null;
	var selUserType = null; //Leader/Member
	
	function selMember(obj){
		selUserType = 'Member';
		var tr;
		var pNode = obj.parentNode;
		while(pNode){
			if(pNode.tagName == 'TR'){
				tr = pNode;
				break;
			}	
			pNode = pNode.parentNode;
		}
		if(tr !=null){
			gTR = tr;
			selectUser();
		}
	}
	function selLeader(){
		selUserType = 'Leader';
		selectUser();
	}

	function saveTeam(){
		if( !$('#teamForm').form('validate') )
        	return;
        var data = $.serializeObject($('#teamForm'));
        
        var params = {leaderId : data.leaderId, memberIds:data.zxgwId };
        
        $.ajax({
        	type:'post',
        	url:'save.do',
        	data:params,
        	error:function(){
            	$.messager.show( {title: 'Error',msg:'系统异常'} ); 
			},
            success: function(result){
            	if (result.errorCode != 200){
                	$.messager.show({
                    	title: 'Error',
                        msg: result.moreInfo
					});
				} else {
                	$('#teamDlg').dialog('close');
				}
			}
		});
	}
	
	
	function selectUser(){
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
			
		$('#divUsers').dialog('open').dialog('center');
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
		var sel = $("#divUsers").find("select[name=gwId]");
		var zxgwId   = $(sel).val();
		var zxgwName = $(sel).find("option:selected").text();
		
		if(selUserType == 'Leader'){
			$('#teamForm').find('input[name=leaderId]').val(zxgwId);
			$('#teamForm').find('input[name=leaderName]').val(zxgwName);
		}else if(gTR !=null){
			$(gTR).find('input[name=zxgwId]').val(zxgwId);
			$(gTR).find('input[name=zxgwName]').val(zxgwName);
		}	
		$('#divUsers').dialog('close');
	}
	
</script>	
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<c:if test="${singlePage}">
<table width='800px'>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>留学咨询顾问分组信息</td></tr>
</table>
</c:if>

<div id="toolbar" style="display: none">
<form name="searchForm" id="searchForm" method="post" >
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="editTeam()">New</a>		
</form>
</div>
	
<table id="grid" data-options="fit:true,border:false">
</table>
	
	<div id="teamDlg" class="easyui-dialog" style="width:500px;height:320px;padding:5px 5px"
            closed="true" buttons="#teamDlg-buttons">        
        <form id="teamForm" method="post" >
        <table>
        	<tr>
              <td style='text-align:right'>组长</td>
              <td style='width:360px'>
                <input name="leaderName" style='width:80px' required="true" readonly/> 
                <input type='hidden' name="leaderId" />
                <a href='javascript:void(0)' onclick="selLeader()">选择</a>
              </td>  
            </tr>
        	<tr>
              <td style='text-align:right'>组员<br/>
              	<a href='javascript:void(0)' onclick='addMember()'>add</a>
              </td>
              <td >
              	<table id='divMembers' style='list-style:none;'>
              	</table>
              </td>
            </tr>  
        </table>  
        </form>
    </div>
    <div id="teamDlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveTeam()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#teamDlg').dialog('close')" style="width:90px">Cancel</a>
    </div>
	
<div id='divUsers' class="easyui-dialog" title="选择顾问" data-options="closed:true,buttons:[{
				text:'确定',
				handler:userSelected
			},{
				text:'close',
				handler:function(){$('#divUsers').dialog('close');}
			}]" style="width:420px;height:200px;padding:10px">
	公司<select name='companyId' onchange="searchUsers()">
		</select>
	姓名<input type='text' name='name' style='width:60px' />
	
	<a href='javascript:void(0)' onclick='searchUsers()'>查询</a>
		<input type='hidden' name='enabled' value='true' />
	<br>
	<label class="label-top">顾问:</label>
    <select name='gwId' style="width:120px;height:20px">
    </select>
</div>
	
</body>
</html>