<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.niu.crm.model.type.MessageType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>学生信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<!-- easyui -->
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/extIcon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.edatagrid.js"></script>
<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
<script type="text/javascript" src="/_resources/js/date.js"></script>
<script type="text/javascript" src="/_resources/My97DatePicker/WdatePicker.js"></script>
<script language='javascript'>
	//是否是当前学生的咨询顾问
	$(function(){        
		$('#planCountry').combotree('loadData', [
		<c:forEach var="item" varStatus="status" items="${countryList}">
		<c:if test="${status.index >0}">,</c:if>{id:'${item.code}', text:'${item.name}'}
		</c:forEach>	
		]);
		$('#planCountry').combotree('setValues', 
		[
		<c:forEach var="item" varStatus="status" items="${lsPlanCountry}"><c:if test="${status.index >0}">,</c:if>'${item.code}'</c:forEach>	
		]
		);
		
		
		$('#custPhoneGrid').datagrid({
			queryParams: {cstmId:'${stu.cstmId}', isMain:false},
			columns : [ [ 
				{
					field : 'id',
					checkbox: true
				},{
					width : 150,
					title : '电话',
					align : 'left',
                    editor: 'text',
					field : 'showPhone'
				},{
					width : 300,
					title : '备注',
					align : 'left',
                    editor:'text',
					field : 'memo'
				}
			]],
			onClickCell: phoneOnClickCell,
            onEndEdit: phoneOnEndEdit,
			toolbar : '#custPhoneToolbar'	
		});
		
		loadContactRecord('${stu.cstmId}');
				
		$(".chackTextarea-area").autoTextarea({
			maxHeight:220,//文本框是否自动撑高，默认：null，不自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
		});
		
	});	
	
	//不包括 主联系电话
	var custPhones = new Array();
    <c:forEach var="item" varStatus="status" items="${cust.custPhones}">
	custPhones.push({showPhone:'${item.showPhone}', memo:'${item.memo}'});</c:forEach>
    
    /*--------------------------------------------*/
    var editPhoneIndex = 0;
    function endPhoneEditing(){
    	if (editPhoneIndex == undefined)
    		return true;
        if ($('#custPhoneGrid').datagrid('validateRow', editPhoneIndex)){
        	$('#custPhoneGrid').datagrid('endEdit', editPhoneIndex);
            editPhoneIndex = undefined;
            return true;
        } else {
            return false;
        }
    }
    
    function phoneOnClickCell(index, field){
		if (editPhoneIndex != index){
			if (endPhoneEditing()){
            	$('#custPhoneGrid').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                var ed = $('#custPhoneGrid').datagrid('getEditor', {index:index,field:field});
                if (ed){
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editPhoneIndex = index;
            } else {
            	setTimeout(function(){
            		$('#custPhoneGrid').datagrid('selectRow', editPhoneIndex);
                },0);
            }
    	}
	}
	function phoneOnEndEdit(index, row){
		
    }
    
    function acceptPhone(){
		if (endPhoneEditing())
			$('#feeItemGrid').datagrid('acceptChanges');            
	}
    
    function custPhoneDialog(){
    	while($('#custPhoneGrid').datagrid('getRows').length >0){
    		$('#custPhoneGrid').datagrid('deleteRow', 0);
    	}
    	for(i=0; i < custPhones.length; i++){
    		addCustPhone(custPhones[i]);
    	}
    	$('#custPhoneDlg').dialog('open').dialog('center');
    }
    
    function addCustPhone(data){
		if (endPhoneEditing()){
			$('#custPhoneGrid').datagrid('appendRow',data);
			editPhoneIndex = $('#custPhoneGrid').datagrid('getRows').length-1;
			$('#custPhoneGrid').datagrid('selectRow', editPhoneIndex).datagrid('beginEdit', editPhoneIndex);   
		}
    }
    function removeCustPhone(){
    	acceptPhone();
		var rows = $('#custPhoneGrid').datagrid('getSelections');
		for(i=0; i < rows.length; i++){
			var idx = $('#custPhoneGrid').datagrid('getRowIndex', rows[i]);
			$('#custPhoneGrid').datagrid('cancelEdit', idx).datagrid('deleteRow', idx);
		}	
		editPhoneIndex = undefined;
    }
    
    function saveCustPhone(){
    	acceptPhone();
    	
    	var szPhones = '';
    	custPhones = new Array();
    	var rows = $('#custPhoneGrid').datagrid('getRows');    	
    	for(i=0; i < rows.length; i++){
    		var phoneData={};
    		phoneData.showPhone = $.trim(rows[i].showPhone);
    		phoneData.memo  = rows[i].memo;
			if(phoneData.showPhone !=''){
				custPhones.push(phoneData);
				if( szPhones == '')
					szPhones += phoneData.showPhone;
				else
					szPhones += ',' + phoneData.showPhone;	 
			}
		}	
		
		$('#frmInquireRecord').find("input[name=phone]").val(szPhones);
    	
    	$('#custPhoneDlg').dialog('close');
    }
    /*---------------------------------------------*/
    
    /**
     *byManual 是否是手动查重
     */
	function chkRepeat(byManual){
		var params = {};
		
		var szMobile = $.trim( $('#frmInquireRecord').find("input[name=mobile]").val());
  		if(params.mobile == ''){
			$.messager.alert({title:'提醒',msg:请录入手机号码, width:650});	
			return;
		}
		for(i=0; i < custPhones.length; i++){
			if(custPhones[i] == '')
				continue;
			szMobile += "," + custPhones[i].showPhone;
		}
		
		params.id =  $('#frmInquireRecord').find("input[name=id]").val();
		params.cstmId = $('#frmInquireRecord').find("input[name=cstmId]").val();
		params.phones = szMobile;
		
		$.ajax({
		  type: 'POST',
		  url: "checkRepeat.do" ,
		  data: params,
		  dataType: "json",
		  error:function(){ alert('系统异常'); },
		  success: function(json){
		  	if(json.errorCode == 200){
		  		if(json.data.length == 0){
                    $.messager.alert('查重提醒','没发现重复客户');
		  			return;
		  		}	
		  		szHtml = '';
		  		for(i=0; i < json.data.length; i++){
		  			var data=json.data[i];
		  			if(i >0)
		  				szHtml += '<br/>';
		  			var szCreated = new Date(data.createdAt).format('yyyy-MM-dd HH:mm');
						  			
		  			szHtml += '公司:' + data.companyName + ' 姓名:' + data.cstmName + "<br/>"
		  			        + ' 手机号:' + data.mobile ;
		  			if(data.precust)
		  			   szHtml += " [种子库]<br/>";
		  			else
		  			   szHtml += " [资源库]<br/>";   
		  			   
		  			szHtml += '录入时间:' + szCreated + '渠道:' + data.stuFromName;
		  		}
		  		$.messager.alert({title:'重复提醒',msg:szHtml, width:550});	
		  	}else{
		  		alert('查重失败-' + json.moreInfo);
		  	}
		  }
		});
	}
	
	function jsSave(opCmd){
		if( $('#frmInquireRecord').find("input[name=savingFlag]").val() ==1){
			alert('请不要重复点击');
			return;
		}
		
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
		
		var params =  $.serializeObject($('#frmInquireRecord'));
		params.status = opCmd;

		if(params.stuFromId == ''){
			$.messager.alert('提醒信息','请选择咨询渠道');
			return;
		}
		if(params.planEnterYear !=''){
			re = /^20\d{2}$/
			if (!re.test(params.planEnterYear)) {
				$.messager.alert( {title: '提醒',msg:'申请年份格式为2YYY'} );
				$('#planEnterYear').focus();
				return;
			}
		}
		
		if( opCmd == 'MOVED' ){
			if(!(/^1[34578]\d{9}$/.test(params.mobile))){ 
				$.messager.alert('提醒信息','请录入正确手机号码');
				return;
			}
	
			if( !params.planCountry || params.planCountry == ''){
				$.messager.alert('提醒信息','请录入意向国家');
				return;
			}
			if(params.planXl == ''){
				$.messager.alert('提醒信息','请录入申请学历');
				return;
			}
		}
		
		{
		  params['custPhones[0].isMain'] = true;
		  params['custPhones[0].showPhone'] = params.mobile;
		  params['custPhones[0].memo'] = '';
		}
		
		for(i=1; i <= custPhones.length; i++){
		  params['custPhones[' + i + '].isMain'] = false;
		  params['custPhones[' + i + '].showPhone'] = custPhones[i -1].showPhone;
		  params['custPhones[' + i + '].memo'] = custPhones[i -1].memo;
		}
		
		var confirmMsg = '确认信息无误?';
		if(opCmd == 'INVALID')
			confirmMsg = '确认设置为无效?';
		else if(opCmd == 'MOVED')
			confirmMsg = '确认转正式库?';	
		
		$.messager.confirm('操作确认', confirmMsg, function(r){
			if (!r) return;
			
			$('#frmInquireRecord').find("input[name=savingFlag]").val(1);
			$.ajax({
				type: 'POST',
				url: "save.do" ,
				data: params,
				dataType: "json",
				error:function(){
					$('#frmInquireRecord').find("input[name=savingFlag]").val('');
					alert('系统异常');
				},
				success: function(json){
					$('#frmInquireRecord').find("input[name=savingFlag]").val('');
										
					if(json.errorCode == 200){
						$('#id').val(json.data.id);
						$('#cstmId').val(json.data.cstmId);
						
						var showMsg = '保存成功';
						if(opCmd == 'INVALID')
							confirmMsg = '确认设置为无效?';
						else if(opCmd == 'MOVED')
							confirmMsg = '确认转正式库?';
							
						$.messager.show({title: '提醒信息', msg: showMsg});
					}else{
						$.messager.show({title: '提醒信息', msg: '保存失败-' + json.moreInfo});
					}
				}
			});
		});
	}
	
	function selPlanXl(){
    	fromTree = $('#planXlTree').tree({
			lines:true,
			url : '/dict/treeData?rootCode=xl',
			onClick : function(node) {	
				$('input[name=planXl]').val(node.text);
				$('#divPlanXlTree').dialog('close');
			}
		});
		$('#divPlanXlTree').dialog('open');
    }
    
    
    function loadContactRecord(cstmId){
    	if(cstmId == '') return;
    	
    	$('#contactGrid').datagrid({
        	url: '/pre/contactRecord/listByCstm?cstmId=' + cstmId,        	
			onDblClickRow:function(index,row){editContactRecord();},
        	columns : [ [ 
				{
					field : 'id',
					checkbox: true
				},{
					hidden:true,
					field : 'gwId',
					formatter: function(value,row,index){
					    var d = new Date(row.contactDate);
					    row.contactDateH = d.format('HH');
					    row.contactDateM = d.format('mm');
					} 					
				},{
					width : 80,
					title : '联系顾问',
					align:'center',
					field : 'gwName'
				},{
					width : 125,
					title : '联系时间',
					align:'center',
					field : 'contactDate',
					formatter: function(value){
						if(!value) return "";
					    var d = new Date(value);
					    return d.format('yyyy-MM-dd HH:mm');
					} 
				},{
					width : '100',
					title : '联系方式',
					align:'center',
					field : 'contactType'
				},{
					width : 290,
					title : '联系内容',
					halign:'center',
					align:'left',
					field : 'contactText'
				},{
					width : 280,
					title : '顾问方案',
					halign:'center',
					align:'left',
					field : 'caseText'
				}
			]],
			nowrap:false,
			toolbar : '#contactToolbar'
        });
    }
    
    function selContactType(val){
    	$('#contactForm').find("[name=contactType]").val(val);
    }
    
    function newContactRecord(){
    	$('#contactForm').form('clear');
    	$('#contactForm').find("input[name=cstmId]").val('${stu.cstmId}');
        $('#contactForm').find("input[name=gwId]").val('${user.id}');
        $('#contactForm').find("input[name=gwName]").val('${user.name}');
        
        
        $('#contactForm').find("select[name=contactStatus]").val('');
        
        
        $('#contactDlg').dialog('open').dialog('center').dialog('setTitle','回访联系信息');
    }
    
    function editContactRecord(){
    	var row = $('#contactGrid').datagrid('getSelected');
        if (row){
            $('#contactDlg').dialog('open').dialog('center').dialog('setTitle','修改联系信息');
            $('#contactForm').form('clear');
            $('#contactForm').form('load',row);
            
            if(row.nextDate){
            	d = new Date(row.nextDate);
            	$('#contactForm').find("input[name=nextDate]").val( d.format('yyyy-MM-dd') );
			}		    
        
            
            if( $('#contactForm').find("select[name=stuLevel] option").size() ==0){
            	$("#stuLevel option").each(function(){
            		$('#contactForm').find("select[name=stuLevel]").append("<option value='" + $(this).val() + "'>" + $(this).text()  + "</option>");
            	});
            }
                
            var szContactDate = (new Date(row.contactDate)).format('yyyy-MM-dd'); 				    
            $('#contactForm').find("input[name=contactDate]").val(szContactDate);
        }else{
        	alert('请选择要修改的记录');
        }
    }
    
    function saveContactRecord(){
		if( !$('#contactForm').form('validate') )
        	return;        		
        var params =  $.serializeObject($('#contactForm'));
        params.contactStatus = $('#contactForm').find("[name=contactStatus]").val();
        
        if( params.id != ''){
        	$.messager.alert({title: '提醒', msg: '不能修改'});
        	return;
        }
        if( !params.contactType || params.contactType == '' ){
        	$.messager.alert({title: '提醒', msg: '请选择联系方式'});
        	return;
        }
        if( !params.status || params.status == '' ){
        	$.messager.alert({title: '提醒', msg: '请选择接听状态'});
        	return;
        }
        	  
        $.ajax({
        	type:'post',
        	url:'/pre/contactRecord/save',
        	data:params,
        	error:function(){
        		$.messager.show( {title: 'Error',msg:'系统异常'} ); 
			},
			success: function(result){
				if (result.errorCode != 200){
					$.messager.show({title: 'Error', msg: result.moreInfo});
                } else {
                	$('#contactDlg').dialog('close');     // close the dialog
                    $('#contactGrid').datagrid('reload');   // reload the user data
                }
            }
		});
	} 
</script>		
</head>
<body style='margin-top:10px;margin-bottom:20px'>

<form method="post" name="frmInquireRecord" id="frmInquireRecord">
<table>
	<tr>
		<td style="width:80px"></td>
		<td style="width:160px"></td>
		<td style="width:80px"></td>
		<td style="width:160px"></td>
		<td style="width:80px"></td>
		<td style="width:200px"></td>
		<td style="width:80px"></td>
		<td style="width:140px"></td>
	</tr>
	<tr>
		<td colspan='4' style='text-align:center'>			
			<a href='javascript:void(0)' onclick='chkRepeat(true);' class="easyui-linkbutton" data-options="iconCls:'icon-search'">查重</a>
			<a href='javascript:void(0)' onclick="jsSave('PENDING');" class="easyui-linkbutton" data-options="iconCls:'icon-save'">待定</a>
			<a href='javascript:void(0)' onclick="jsSave('INVALID');" class="easyui-linkbutton" data-options="iconCls:'icon-save'">无效</a>
			<a href='javascript:void(0)' onclick="jsSave('MOVED');" class="easyui-linkbutton" data-options="iconCls:'icon-save'">转正式库</a>
			
			<input type='hidden' name='id' value="${stu.id}" />
			<input type='hidden' name='cstmId' value="${cust.id}" />
			<input type='hidden' name='savingFlag' value="" /> <!-- 是否正在保存 -->
		</td>
	</tr>	
	<tr> 
		<td style="text-align:right">归属公司</td>
		<td colspan='3'>
			<input type='text' name='companyName' style="width:330px;" value='${stu.companyName}' readonly/>
			<input type='hidden' name='companyId' value='${stu.companyId}' readonly/>	
		</td>  
		<td style="text-align:right">*咨询渠道</td>
		<td colspan='3'>
			<input type='text' name="stuFromName" style='width:200px' value="${stu.stuFromName}" readonly data-options="required:true" />
			<input type='hidden' name="stuFromId" value="${stu.stuFromId}">
		</td>
	</tr>
	<tr>
		<td style="text-align:right">*学生姓名</td>
		<td >
		  <input type="text" value="${cust.name}"
		     name="name" maxlength="20" style="width:120px" data-options="required:true" >
		</td>
		<td style="text-align:right">身份</td>
		<td>
			<select name="visitorType">
				<option value="">选择</option>
			<c:forEach var="item" items="${visitorTypeList}">
				<option value='${item.dictName}' <c:if test="${stu.visitorType==item.dictName}">selected</c:if>>${item.dictName}</option>
			　</c:forEach>
			</select>
			
			性别<select name="gender">
				<option value="">选择</option>
				<option value="M" <c:if test="${cust.gender=='M'}">selected</c:if>>男</option>
				<option value="F" <c:if test="${cust.gender=='F'}">selected</c:if>>女</option>
			</select>
		</td>
	</tr>
	<tr>	
		<td style="text-align:right">*手机</td>
		<td >
			<input type="text" name="mobile" value="${cust.mobile}" style="width:120px" data-options="required:true" />
		</td>
		<td style="text-align:right">备用电话</td>
		<td colspan='3'>
			<input type="text" name="phone" value="${cust.phone}" readonly style="width:350px"/>
			<a href='javascript:;' onclick='custPhoneDialog()'>编辑</a>
		</td>
	</tr>	
	<tr>	
		<td style="text-align:right">Email</td>
		<td >
			<input type="text" name="email" value="${cust.email}" style="width:130px"/>
		</td>
		<td style="text-align:right">QQ</td>
		<td >
			<input type="text" name="QQ" value="${cust.QQ}" style="width:130px"/>
		</td>
		<td style="text-align:right">微信</td>
		<td >
			<input type="text" name="wechat" value="${cust.wechat}" style="width:130px"/>
		</td>
	</tr>	
	<tr>
		<td align='right'>所在城市</td>
		<td>
			<input type="text" name="stuCity" value="${stu.stuCity}" style="width:130px" >
		</td>	
		<td style="text-align:right">国内毕业<br>在读学校</td>
		<td >
		<input type="text" name="currSchool"
					 style="width:130px" value="${stu.currSchool}">
		</td>
		<td align="right">在读年级</td>
		<td>
			<select name="currGrade">
				<option value="">选择</option>
			<c:forEach var="item" items="${gradeList}">
				<option value='${item.dictName}' <c:if test="${stu.currGrade==item.dictName}">selected</c:if>>${item.dictName}</option>
			　</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td style="text-align:right" >当前专业</td>
		<td>
		  <input type="text" name="currentSpecialty" value="${stu.currentSpecialty}" style="width:130px"></td>
		<td align="right" >意向专业</td>
		<td>
		  <input type="text" name="hopeSpecialty" value="${stu.hopeSpecialty}" style="width:130px">
		</td>
		<td align="right">GPA</td>
		<td>
			<input type="text" name="gpa" id="gpa" style="width:100px" value="${stu.gpa}" >
		</td>
	</tr>
	<tr>
		<td align='right'>*意向国家</td>
		<td >
			<select id="planCountry" name="planCountry" class="easyui-combotree" multiple="true" style="width:140px;" panelWidth="180"></select>
		</td>
		<td style="text-align:right">*申请学历</td>
		<td >
			<input type='text' name="planXl" value="${stu.planXl}" readonly style='width:100px' readonly />
			<a href='javascript:void(0)' onclick='selPlanXl()'>...</a>
		</td>
		<td align="right">入学时间</td>
		<td >
			<input type="text" id="planEnterYear" name="planEnterYear" value="${stu.planEnterYear}" style="width:40px">年
			<select name="planEnterSeason" >
				<option value=''></option>
				<option value='春季' <c:if test="${stu.planEnterSeason=='春季'}">selected</c:if>>春季</option>
				<option value='秋季' <c:if test="${stu.planEnterSeason=='秋季'}">selected</c:if>>秋季</option>
			</select>
		</td>
	</tr>	
	<tr align="right">
		<td valign="top">基本情况</td>
		<td align="left" colspan="7"><textarea id='areaBasicInfo' name="basicInfo" class="chackTextarea-area">${stu.basicInfo}</textarea></td>
	</tr>
	<tr align="right">
		<td valign="top">备注</td>
		<td align="left" colspan="7"><textarea name="memo" id="memo" class="chackTextarea-area">${stu.memo}</textarea></td>
	</tr>
	<tr>
		<td style="text-align:right">培训需求</td>
		<td colspan="3"><input type='text' name="pxRequire" value="${stu.pxRequire}" style='width:400px'></td>		
	</tr>
</table>
</form>

<c:if test="${stu.id !=null}">
<div style='padding-top:10px'>
	<table id="contactGrid" title="顾问回访及联系信息" style="width:1080px;height:420px"
           pagination="true" idField="id"
            rownumbers="true" singleSelect="true">
    </table>
    <div id="contactToolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newContactRecord()">New</a>
    </div>
</div>
<div id="contactDlg" class="easyui-dialog" style="width:600px;height:330px;padding:5px 5px"
            closed="true" buttons="#contactDlg-buttons">        
        <form id="contactForm" method="post">
                <input type='hidden' name="id" />
                <input type='hidden' name="cstmId" value="${stu.cstmId}" />
        <table>
        	<tr>
              <td style='width:90px'></td>
              <td style='width:160px'></td>
              <td style='width:70px'></td>
              <td style='width:200px'></td>
            </tr>  
            <tr>
              <td style='text-align:right'>联系方式</td>
              <td>
              	<input type='text' name="contactType" required="true" style='width:90px' readonly/>
              	<a href="javascript:void(0)" id="mb" class="easyui-menubutton" 
              		data-options="menu:'#mm'">...</a>
            	<div id="mm" style="width:150px;">
            	<c:forEach var="item" items="${contactTypeList}"><div onclick="selContactType('${item.dictName}')">${item.dictName}</div></c:forEach>
            	</div>
              </td>
              <td style='text-align:right'>联系顾问</td>
              <td>
                <input name="gwName" value="" readonly style='width:100px' />
                <input type='hidden' name="gwId" value="" />
              </td>  
            </tr>
            <tr>
              <td style='text-align:right'>接听状态</td>
              <td >
              	<select name='status' >
              		<option value=''>请选择</option>
              		<option value='Y'>接听</option>
              		<option value='N'>未接听</option>
              	</select>
              </td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>跟进情况</td>
              <td colspan='3'><textarea name="contactText" style='height:120px;width:400px'></textarea></td>
            </tr>
        </table>    
        </form>
    </div>
    <div id="contactDlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveContactRecord()" style="width:90px">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#contactDlg').dialog('close')" style="width:90px">关闭</a>
    </div>
</c:if>
<br><br>

<div id='divPlanXlTree' class="easyui-dialog" title="申请学历" data-options="closed:true" style="width:500px;height:360px;padding:10px">
	<ul id="planXlTree" class="easyui-tree" ></ul>
</div>
<div id='custPhoneDlg' class="easyui-dialog" title="备用电话" data-options="closed:true,buttons:[{
				text:'确定',
				handler:saveCustPhone
			}]" style="width:600px;height:320px;padding:10px">
	<div id="custPhoneToolbar" style="height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addCustPhone({})">Append</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeCustPhone()">Remove</a>  
    </div>	
    <table class="easyui-datagrid" id="custPhoneGrid" data-options="fit:true,border:false">
    </table>	
</div>
<script language='javascript'>
(function($){
	$.fn.autoTextarea = function(options) {
		var defaults={
				maxHeight:null,//文本框是否自动撑高，默认：null，不自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
				minHeight:$(this).height() //默认最小高度，也就是文本框最初的高度，当内容高度小于这个高度的时候，文本以这个高度显示
		};
		var opts = $.extend({},defaults,options);
		return $(this).each(function() {
				$(this).bind("paste cut keydown keyup focus blur",function(){
						var height,style=this.style;
						this.style.height =  opts.minHeight + 'px';
						if (this.scrollHeight > opts.minHeight) {
								if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
										height = opts.maxHeight;
										style.overflowY = 'scroll';
								} else {
										height = this.scrollHeight;
										style.overflowY = 'hidden';
								}
								style.height = height  + 'px';
						}
				});
		});
};
})(jQuery);


</script>
<style type="text/css">
*{margin:0px; padding:0px;}
.dn{display:none;}
.chackTextarea-area{line-height:22px; font-size:14px; padding:1px 5px; border:1px solid #CDCDCD; width:630px; height:40px;}
.content{width:420px; margin:0 auto; padding-top:20px;}
.content h1{font-size:16px; font-family:微软雅黑; font-weight:normal;}
</style>
</body>
</html>
