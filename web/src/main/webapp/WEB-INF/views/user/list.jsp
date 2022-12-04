<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
  out.clear();
  String tblWidth="950px";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>用户管理</title>
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
	  grid = $('#grid').datagrid({
				title : '',
				url : '/user/listData.do',
				striped:true,
				rownumbers : true,
				pagination : true,
				pageSize : 15,
				pageList: [15,20,30,50,100],
				idField : 'id',
				columns : [ [ {
					field : 'id',
					checkbox: true
				},{
					width : 85,
					title : '姓名',
					align : 'center',
					field : 'name' 
				},{
					width : 50,
					title : '性别',
					align : 'center',
					field : 'gender' 
				},{
					width : 100,
					title : '账号',
					align : 'center',
					field : 'account'
				},{
					width : 150,
					title : '公司',
					halign: 'center',
					field : 'companyName',
					formatter: function(value,row,index){
						return row.companyName;
					} 
				},{
					width : 150,
					title : '部门',
					halign: 'center',
					field : 'unitName',
					formatter: function(value,row,index){
						if(row.unit)
							return row.unit.name;
						else
							return '';	 
					} 
				},{
					width : 150,
					title : '联系电话',
					halign:'center',
					field : 'phone'
				},{
					width : 150,
					title : '微信号',
					halign:'center',
					field : 'weixinId'
				},{
					width : '80',
					title : 'enabled',
					halign:'center',
					field : 'enabled'
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
				},{
					width : '60',
					title : '修改密码',
					align:'center',
					field : 'changePwd',
					formatter: function(value,row,index){						
						var szUrl = '<a href="javascript:void(0)" onclick="changePwd(this,' + row.id + ')">修改密码</a>'
					    return szUrl;
					} 
				},{
					width : '60',
					title : '权限设置',
					align:'center',
					field : 'acl',
					formatter: function(value,row,index){
						var szUrl = '<a href="/func/userFuncSet.do?userId=' + row.id + '" target="_blank">权限设置</a>'
					    return szUrl;
					} 
				}
				] ],
				toolbar : '#toolbar'
			});
	  });
	  
	function jsQuery() {
		grid.datagrid('clearSelections');
		
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
		
		
		var params =  $.serializeObject($('#searchForm'));
		grid.datagrid('load', params); 
	};
	
	function changePwd(obj,id){		
		var tr = null;
		while(true){
			if(obj.tagName == 'TR'){
				tr = obj;
				break;
			}
			obj = obj.parentNode;	
		}
		if(!tr) return;
		
		var rowIndex = $('#grid').datagrid('getRowIndex', id); 
		var rowData = $('#grid').datagrid('getRows')[rowIndex]; 
		
		
		$('#divPwd').find('input[name=id]').val(rowData.id);
		$('#divPwd').find('input[name=name]').val(rowData.name);
		$('#divPwd').find('input[name=account]').val(rowData.account);
		$('#divPwd').find('input[name=newPasswd]').val('');
		$('#divPwd').find('input[name=newPasswd1]').val('');
		
		$('#divPwd').dialog('open');
	}	
	function doChangePwd(){			
		var reg = /^(?=.{6,})(?=.*[a-zA-Z])(?=.*[0-9]).*$/;
		var params = {};
		
		var szAccount = $('#divPwd').find('input[name=account]').val();
		var szPasswd1 = $('#divPwd').find('input[name=newPasswd]').val();
		var szPasswd2 = $('#divPwd').find('input[name=newPasswd1]').val();
		if(szPasswd1 != szPasswd2 ){
			alert('新密码两次输入不一致');
			return;
		}
		if( !szPasswd1.match(reg) ){
			alert('密码必须6位以上，且须包含字母 数字');
			return;
		}
		
		params.account   = szAccount;			
		params.newPasswd = szPasswd1;

		$.ajax({
		  url: "/user/doChangePwd.do", 
		  type:'post',
		  data: params,
		  dataType: "json",
		  error: function(){alert('系统异常');},
		  success: function(json){
		  	if(json.errorCode == 200){
		  		$.messager.show( {title: '提示信息',msg:'修改成功'} ); 
		  		$('#divPwd').dialog('close');
		  	}else{
		  		$.messager.show( {title: '错误信息',msg:'修改失败-' + json.error} ); 
		  	}
		  }
		});
	}
	
	function selectUnit(szTarget){
    	unitTree = $('#unitTree').tree({
			lines:true,
			url : '/unit/treeData?rootCode=root',
			onClick : function(node) {	
				if( szTarget == 'search'){
					$('#searchForm').find("input[name=unitId]").val(node.id);
					$('#searchForm').find("input[name=unitName]").val(node.text);
				}
				else{
				/*
					if( !$('#unitTree').tree('isLeaf',node.target) )
						return; */
						
					$('#userForm').find("input[name=unitId]").val(node.id);
					$('#userForm').find("input[name=unitName]").val(node.text);
				}	
				$('#divUnitTree').dialog('close');
			}
		});
		$('#divUnitTree').dialog('open');
    }  
    
	function newUser(){
		$('#userForm').form('clear');
		$('#userDlg').dialog('open').dialog('center').dialog('setTitle','新建用户');
    }
    function editUser(){
		var rows = $('#grid').datagrid('getSelections');
		if(rows.length ==0){
			alert('请选择要修改的记录');
			return;
		} 
		if(rows.length >1){
			alert('请只选择要修改的行');
			return;
		} 
		row = rows[0];
		
			//id, name 无论如何先清空
            $('#userForm').find("input[name=id]").val('');
            $('#userForm').find("input[name=name]").val('');
                        
            $('#userForm').form('load',row);
            if(row.enabled)
            	$('#userForm').find("select[name=enabled]").val(1);
            else
            	$('#userForm').find("select[name=enabled]").val(0);
            
            $('#userForm').find("input[name=unitId]").val(row.unit.id);
            
            if(row.unit.id == row.unit.companyId)
            	$('#userForm').find("input[name=unitName]").val(row.unit.name);
            else
            	$('#userForm').find("input[name=unitName]").val(row.companyName + '-' +row.unit.name);	

			$('#userDlg').dialog('open').dialog('center').dialog('setTitle','修改用户信息');
		
	}
	function removeUser(){
    	var row = $('#contactDg').datagrid('getSelected');
        if (row){
        	
        }else{
        	alert('请选择要删除的记录');
        }
    }
        
	function saveUser(){
		if( !$('#userForm').form('validate') )
        	return;
        var params =  $.serializeObject($('#userForm'));
        $.ajax({
        	type:'post',
        	url:'/user/save.do',
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
                	$('#userDlg').dialog('close');     // close the dialog
					$('#userDg').datagrid('reload');   // reload the user data
				}
			}
		});
	} 
	
	function editAcl(){
		var row = $('#grid').datagrid('getSelected');
		if (row){
            

			$('#aclDlg').dialog('open').dialog('center').dialog('setTitle','权限设置');
		}else{
			alert('请选择要修改的记录');
		}
	}
</script>
	
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none">
<table width=800px>
  <tr><td height=10></td></tr>
  <tr><td width=100% align='center' height=30 class='title'>用户管理</td></tr>
</table>
	<form name="searchForm" id="searchForm" method="post" >
	<table width="100%" border="0">
		<tr> 
			<td style="width: 70px;text-align:right">公司</td>
			<td style="width: 120px">
				<select name='companyId' id="companyId" style="width:105px;">
					<option  value=''>请选择</option>
				<c:forEach var="item" items="${companyList}">
					<option value='${item.id}' >${item.name}</option>
				</c:forEach>
				</select>
			</td>
			<td style="width: 70px;text-align:right">部门</td>
			<td style="width: 165px">
				<input type="text" name="unitName" maxlength="50" style="width:135px;" readonly/>
				<input type="hidden" name="unitId" />
				<a href='javascript:void(0)' onclick="selectUnit('search')">...</a>
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
			<td style="width: 70px;text-align:right">录入日期</td>
			<td>
			  <input name="created_from" id="created_from" readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />到
			  <input name="created_to"   id="created_to"   readonly type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:80px;" />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">账号</td>
			<td ><input type="text" name="account" maxlength="50" style="width:105px;" /></td>					
			<td style="text-align:right">联系电话</td>
			<td ><input type="text" name="phone" maxlength="50" style="width:105px;" /></td>					
			<td style="text-align:right">Enabled</td>
			<td>
				<select name='enabled'>
					<option value=''>全部</option>
					<option value='1'>Enabled</option>
					<option value='0'>Disabled</option>
				</select>
			</td>
			<td></td>
			<td colspan='3' style='text-align:left'>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" 
				onclick="jsQuery()" style="margin-left: 20px;" >查询</a> 
				
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="newUser()">New</a>
				
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="editUser()">Edit</a>
				<!--
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="editAcl()">权限设置</a> -->
			</td>					    
		</tr>
	</table>
	</form>
	</div>
	
	<table id="grid" data-options="fit:true,border:false">
	</table>
	
	<div id="userDlg" class="easyui-dialog" style="width:500px;height:320px;padding:5px 5px"
            closed="true" buttons="#userDlg-buttons">        
        <form id="userForm" method="post" novalidate>
        <table>
        	<tr>
              <td style='text-align:right'>姓名:</td>
              <td>
                <input name="name" required="true"/>                
                <input type='hidden' name="id" />
              </td>  
            </tr>
        	<tr>
              <td style='text-align:right'>性别:</td>
              <td>
              	<select name="gender">
					<option value="">请选择</option>
					<option value="M" >男</option>
					<option value="F" >女</option>
				</select>	
              </td>  
            </tr>
            <tr>
              <td style='text-align:right'>部门:</td>
              <td>
              	<input type='text' name="unitName" style='width:260px' readonly required="true" />
              	<a href='javascript:;' onclick="selectUnit('user')">...</a>
              	<input type='hidden' name="unitId" value='xx' />
              </td>
            </tr>
        	<tr>
              <td style='text-align:right'>账号:</td>
              <td>
                <input type='text' name="account"/>
              </td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>联系电话:</td>
              <td><input type='text' name="phone" value="" /></td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>Email:</td>
              <td><input type='text' name="email" style='width:260px' /></td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>微信号:</td>
              <td><input type='text' name="weixinId" style='width:260px' /></td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>Enabled:</td>
              <td>
              	<select name='enabled'>
					<option value='1'>Enabled</option>
					<option value='0'>Disabled</option>
				</select>
              </td>
            </tr>
            
        </table>  
        </form>
    </div>
    <div id="userDlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveUser()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#userDlg').dialog('close')" style="width:90px">Cancel</a>
    </div>
    
<div id='divUnitTree' class="easyui-dialog" title="部门选择" data-options="closed:true" style="width:500px;height:420px;padding:10px">
	<ul id="unitTree" class="easyui-tree" ></ul>
</div>

<div id="aclDlg" class="easyui-dialog" style="width:600px;height:380px;padding:10px 20px"
            closed="true" buttons="#aclDlg-buttons">    
</div>   
<div id="aclDlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveAcl()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#aclDlg').dialog('close')" style="width:90px">Cancel</a>
</div>         

<div id='divPwd' class="easyui-dialog" title="修改密码" style="width:320px;height:220px;padding:5px"
	data-options="closed:true,buttons:[{
				text:'Save',
				handler:function(){ doChangePwd(); }
			},{
				text:'Close',
				handler:function(){$('#divPwd').dialog('close');}
			}]"
>
	<table>
        <tr>
        	<td> 姓名：</td>
            <td>
            	<input name="name" value="system" style="width: 160px;" readonly/>
            </td>
        </tr>     
        <tr>
        	<td> 用户名：</td>
            <td>
            	<input name="account" style="width: 160px;" readonly/>
                <input name="id" type="hidden" />
            </td>
        </tr>       
        <tr>
           <td>   新密码：</td>
           <td>
           		<input name="newPasswd" type="password" style="width: 160px;" />
           </td>
        </tr>
        <tr>
			<td>   确认新密码：</td>
			<td>
           		<input name="newPasswd1" type="password" style="width: 160px;" />
            </td>
        </tr>                                             
    </table>
</div>
</body>
</html>