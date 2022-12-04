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
	var isZxgw = ${isZxgw};
	$(function(){        
		$('#planCountry').combotree('loadData', [
		<c:forEach var="item" varStatus="status" items="${countryList}"><c:if test="${status.index >0}">,</c:if>
			{id:'${item.code}', text:'${item.name}'}
		</c:forEach>	
		]);
		$('#planCountry').combotree('setValues', 
		[
		<c:forEach var="item" varStatus="status" items="${lsPlanCountry}"><c:if test="${status.index >0}">,</c:if>'${item.countryCode}'</c:forEach>	
		]
		);
		
		$('#zxgwGrid').datagrid({
			url:'listStuZxgw.do',
			queryParams: {stuId:'${stu.id}'},
			columns : [ [ 
				{
					field : 'id',
					checkbox: true
				},{
					width : 110,
					title : '咨询顾问',
					align:'center',
					field : 'zxgwName'
				},{
					width : 130,
					title : '分配时间',
					align:'center',
					field : 'assignDate',
					formatter: function(value,row,index){
						if(!row.assignDate) return "";
					    var d = new Date(row.assignDate);
					    return d.format('yyyy-MM-dd HH:mm'); 
					} 
				},{
					width : 450,
					title : '顾问评级',
					align:'center',
					field : 'stuLevelName'
				}
			]],
			toolbar : '#zxgwToolbar'	
		});
		
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
	
		loadContactRecord('${stu.id}');
				
		$(".chackTextarea-area").autoTextarea({
			maxHeight:220,//文本框是否自动撑高，默认：null，不自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
		});
		
		{
			var obj = document.getElementById("areaBasicInfo"); 
			$(obj).height(obj.scrollHeight);
		}
    });
    
    function cancelShare(obj,id){
    	var span = obj.parentNode;
    	while(span){
    		if(span.tagName == 'SPAN') break;
    		span = span.parentNode;
    	}
    	$.messager.confirm('Confirm', '确认收回共享?', function(r){
    		if (!r) return;

	    	$.ajax({
			  type: 'POST',
			  url: "/stushare/cancelSingle.do" ,
			  data: {id:id},
			  dataType: "json",
			  error:function(){ alert('系统异常'); },
			  success: function(json){
			  	if(json.errorCode == 200){
			  		$(span).remove();
			  		$.messager.show( {title: 'Error',msg:'已取消共享'} ); 
			  	}
			  }
			});
		});	
    }
    
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
	
	function jsSave() {
		if( $('#frmInquireRecord').find("input[name=savingFlag]").val() ==1){
			alert('请不要重复点击');
			return;
		}
		
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
		
		var params =  $.serializeObject($('#frmInquireRecord'));
		
		params.lxCust =0;
		params.pxCust =0; 
		params.cpCust =0;
		
		if( $('#lxCust').prop('checked')){
			params.lxCust =1;
		}
		if( $('#pxCust').prop('checked')){
			params.pxCust =1;
		}
		if( $('#cpCust').prop('checked')){
			params.cpCust =1;
		}
		if(params.lxCust + params.pxCust + params.cpCust ==0){
			$.messager.alert('提醒信息','业务类型至少选一个');
			return;
		}
		
		if(params.unitId == ''){
			$.messager.alert('提醒信息','请选择归属部门');
			return;
		}
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
		
		if(!(/^1[3456789]\d{9}$/.test(params.mobile))){ 
			$.messager.alert('提醒信息','请录入正确手机号码');
			return;
		}
		
		//stu_level:重要、紧急、犹豫待定、普通、长期潜在(29,35,309,310, 311)

		if( !params.planCountry || params.planCountry == ''){
			$.messager.alert('提醒信息','请录入意向国家');
			return;
		}
		if(params.planXl == ''){
			$.messager.alert('提醒信息','请录入申请学历');
			return;
		}
		
		for(i=0; i < custPhones.length; i++){
		  params['custPhones[' + i + '].isMain'] = false;
		  params['custPhones[' + i + '].showPhone'] = custPhones[i].showPhone;
		  params['custPhones[' + i + '].memo'] = custPhones[i].memo;
		}
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
		  		
		  		$.messager.show({title: '提醒信息', msg: '保存成功'});
		  	}else{
		  		$.messager.show({title: '提醒信息', msg: '保存失败-' + json.moreInfo});
		  	}
		  }
		});
	}
    
    function selStuFrom(){
    	fromTree = $('#fromTree').tree({
			lines:true,
			url : '/dict/treeData?rootCode=stufrom',
			onClick : function(node) {				
				if( !$('#fromTree').tree('isLeaf',node.target) )
					return;
					
				var pNode = $('#fromTree').tree('getParent',node.target);
				var szTxt = node.text;
					
				$('input[name=stuFromId]').val(node.id);
				$('input[name=stuFromName]').val(szTxt);
				$('#divFromTree').dialog('close');
			}
		});
		$('#divFromTree').dialog('open');
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
    
    function loadContactRecord(stuId){
    	if(stuId == '') return;
    	
    	$('#contactGrid').datagrid({
        	url: '/lx/contactRecord/listByStu?stuId=' + stuId,        	
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
					title : '回访类型',
					align:'center',
					field : 'callbackType'
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
				},{
					width : 125,
					title : '下次跟进时间',
					halign:'center',
					align :'center',
					field : 'nextDate',
					formatter: function(value,row,index){
						if(!row.nextDate) return "";
					    var d = new Date(row.nextDate);
					    return d.format('yyyy-MM-dd HH:mm');
					} 
				}
			]],
			nowrap:false,
			toolbar : '#contactToolbar'
        });
    }
    
    function selContactType(val){
    	$('#contactForm').find("input[name=contactType]").val(val);
    }
    
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
    //取我对此客户的评级
    function getStuLevel(){
    	var stuLevel = '';
        var zxgwRows = $('#zxgwGrid').datagrid('getRows');
        for(i=0; i < zxgwRows.length; i++){
          if(zxgwRows[i].zxgwId == '${user.id}')
          	stuLevel = zxgwRows[i].stuLevel;
        } 
        return stuLevel;
    }
      
    function newContactRecord(){
    	$('#contactForm').form('clear');
    	$('#contactForm').find("input[name=stuId]").val('${stu.id}');
        $('#contactForm').find("input[name=gwId]").val('${user.id}');
        $('#contactForm').find("input[name=gwName]").val('${user.name}');
        
        var stuLevel = getStuLevel();
        
        $('#contactForm').find("select[name=contactStatus]").val('');
        $('#contactForm').find("input[name=oldStuLevel]").val(stuLevel);
        
        if( $('#contactForm').find("select[name=stuLevel] option").size() ==0){
          $("#stuLevel option").each(function(){
        	$('#contactForm').find("select[name=stuLevel]").append("<option value='" + $(this).val() + "'>" + $(this).text()  + "</option>");
          });
        }
        $('#contactForm').find("select[name=stuLevel]").val(stuLevel);
        
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
    
	function removeContactRecord(){
    	var row = $('#contactGrid').datagrid('getSelected');
        if (row){
        }else{
        	alert('请选择要删除的记录');
        }
    }
        
	function saveContactRecord(){
		if( !$('#contactForm').form('validate') )
        	return;        		
        var params =  $.serializeObject($('#contactForm'));
        params.callbackType = $('#contactForm').find("select[name=callbackType]").val();
        params.contactStatus = $('#contactForm').find("select[name=contactStatus]").val();
        
        if( params.id != ''){
        	$.messager.alert({title: '提醒', msg: '不能修改'});
        	return;
        }
        if( !params.contactType || params.contactType == '' ){
        	$.messager.alert({title: '提醒', msg: '请选择联系方式'});
        	return;
        }
        if( !params.contactStatus || params.contactStatus == '' ){
        	$.messager.alert({title: '提醒', msg: '请选择接听状态'});
        	return;
        }
        if( !params.callbackType || params.callbackType == '' ){
        	$.messager.alert({title: '提醒', msg: '请选择回访类型'});
        	return;
        }
        
        if( !params.nextDateH ) params.nextDateH ='';
        if( !params.nextDateM ) params.nextDateM ='';
        
        if( params.contactStatus =='Y'){
        	//重要，紧急(29,35), 字数不能低于20
        	if( params.contactStatus =='Y'&& params.contactText.length <20 && 
            	params.oldStuLevel != '29' && params.oldStuLevel != '25'){
        		if( params.stuLevel == '29' || params.stuLevel == '35'){
        			$.messager.alert({title: '提醒', msg: '跟进情况不能少于20个字'});
        			return;
        		}	
       		}
        	//普通，重要，紧急(310,29,35)
        	if(params.stuLevel == '310' || params.stuLevel == '29' || params.stuLevel == '35'){
        		if($.trim(params.caseText) == ''){
        			$.messager.alert({title: '提醒', msg: '请录入顾问方案'});
        			return;
        		}
        		if($.trim(params.nextDate) == ''){
        			$.messager.alert({title: '提醒', msg: '请选择下次跟进时间'});
        			return;
        		}
        	} 
        }
        if( params.nextDate != '' ){
        	if( params.nextDateH == '' || params.nextDateM == '' ){
        		$.messager.alert({title: '提醒', msg: '请选择完整的下次跟进时间'});
        		return;
        	}
        	params.nextDate = params.nextDate + " " + params.nextDateH + ":" + params.nextDateM;
        }
        	  
        $.ajax({
        	type:'post',
        	url:'/lx/contactRecord/save',
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
                        
                    if(params.stuLevel != getStuLevel())
                    	$('#zxgwGrid').datagrid('reload');
                }
            }
		});
	} 
        
	function selectUnit(){
    	unitTree = $('#unitTree').tree({
			lines:true,
			url : '/unit/treeData?rootCode=root',
			onClick : function(node) {	
				$('#frmInquireRecord').find("input[name=unitId]").val(node.id);
				$('#frmInquireRecord').find("input[name=unitName]").val(node.text);
				
				$('#divUnitTree').dialog('close');
			}
		});
		$('#divUnitTree').dialog('open');
    }
    
    function stuAssignLog(){
    	var stuId = $('#frmInquireRecord').find("input[name=id]").val();
    	$.ajax({
        	type:'post',
        	url:'stuAssignLog.do',
        	data:{stuId:stuId},
        	error:function(){
                 $.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if(result.length == 0){
                	$.messager.alert({title: '', msg: '没有历史记录'});
                    return;
                }   
                var szMsg = '';
                for(i=0; i < result.length; i++){
                	if(i >0) szMsg += "<br/>";
                	var d = new Date(result[i].createdAt);
                	szMsg += result[i].zxgwName + "(" + d.format('yyyy-MM-dd HH:mm');
                	if( result[i].creatorName != null && result[i].creatorName != '')
                		szMsg += " 操作人:" + result[i].creatorName
                	szMsg += ")";
                }
                $.messager.alert({title: '', msg: szMsg});
            }
        });
    }
    function stuLevelLog(){
    	var stuId = $('#frmInquireRecord').find("input[name=id]").val();
    	$.ajax({
        	type:'post',
        	url:'stuLevelLog.do',
        	data:{stuId:stuId},
        	error:function(){
                 $.messager.show( {title: 'Error',msg:'系统异常'} ); 
            },
            success: function(result){
            	if(result.length == 0){
                	$.messager.alert({title: '', msg: '没有历史记录'});
                    return;
                }   
                var szMsg = '';
                for(i=0; i < result.length; i++){
                	if(i >0) szMsg += "<br/>";
                	
                	var d = new Date(result[i].createdAt);
                	szMsg += result[i].levelName + "(" + result[i].creatorName + "," + d.format('yyyy-MM-dd HH:mm') + ")";
                }
                $.messager.alert({title: '', msg: szMsg});
            }
        });
    }
</script>
</head>
<body style='margin-top:10px;margin-bottom:20px'>

<div align="left">
<div class="title" style="width:920px;font-size:14px;text-align: center;"><strong>学生咨询评估表</strong></div>

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
			
			<a href='javascript:void(0)' onclick='jsSave();' class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href='javascript:void(0)' onclick='chkRepeat(true);' class="easyui-linkbutton" data-options="iconCls:'icon-search'">查重</a>

		<c:if test="${stu.id !=null }">
			<a href='javascript:void(0)' onclick="openAlarmDialog()" class="easyui-linkbutton" data-options="iconCls:'ext_bell'">提醒</a>
			<a href='javascript:void(0)' onclick="openSmsDialog()" class="easyui-linkbutton" data-options="iconCls:'ext_transmit'">短信</a>
			<a href='javascript:void(0)' onclick="stuLevelLog()">顾问评级记录</a>
			<a href='javascript:void(0)' onclick="stuAssignLog()">顾问分配记录</a>
		</c:if>
			<input type='hidden' id='id'     name='id' value="${stu.id}" />
			<input type='hidden' id='cstmId' name='cstmId' value="${stu.cstmId}" />
			<input type='hidden' id='savingFlag' name='savingFlag'/> <!-- 是否正在保存 -->
		</td>
	</tr>	
	<tr> 
		<td style="text-align:right">*归属部门</td>
		<td colspan='3'>
			<input type='text' name='unitName' style="width:330px;" value='${stu.unitName}' readonly/>
			<a href='javascript:void(0)' onclick='selectUnit()'>...</a>
			<input type='hidden' name='unitId' value='${stu.unitId}' readonly/>	
		</td>  
		<td style="text-align:right">*咨询渠道</td>
		<td colspan='3'>
			<input type='text' name="stuFromName" style='width:200px' value="${stu.stuFromName}" readonly data-options="required:true" />
		<c:if test="${canModifyStuFrom || stu.id == null}">
			<a href='javascript:;' onclick='selStuFrom()'>...</a>
		</c:if>
			<input type='hidden' name="stuFromId" value="${stu.stuFromId}">
		</td>
	</tr>
	<tr>	
		<td style="text-align:right">首次咨询</td>
		<td>
			<input name="inquireDate" value="<fmt:formatDate value='${stu.inquireDate}' pattern='yyyy-MM-dd'/>" type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'});" style='width:120px' readonly/>
		</td>
		<td style="text-align:right">首次面访</td>
		<td ><input name="visitDate" value="<fmt:formatDate value='${stu.visitDate}' pattern='yyyy-MM-dd'/>" type="text" onclick="WdatePicker({maxDate:'%y-%M-%d'})" style='width:120px;' readonly /></td>
		
		<td style="text-align:right">业务类型</td>
		<td >
			<input type='checkbox' id='lxCust' name='lxCust' value="1" <c:if test="${cust.lxCust}">checked</c:if> />留学
			<input type='checkbox' id='pxCust' name='pxCust' value="1" <c:if test="${cust.pxCust}">checked</c:if> />培训
			<input type='checkbox' id='cpCust' name='cpCust' value="1" <c:if test="${cust.cpCust}">checked</c:if> />产品
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
		<td style='text-align:right'>身份证号</td>
			<input type='hidden' name='idCertType' value='${cust.idCertType}' />
		</td>
		<td ><input type="text" onfocus="txtOnfocus(this)"  
			name="idCertNo" value="${cust.idCertNo}" style='width:180px'/>
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
	<tr id="trLang">
		<td align="right">外语程度</td>
		<td colspan="5">
		<table>
		  <tr>
			<td>Toefl</td><td style='width:80px'><input type="text" name="flToefl" id="flToefl" style="width:60px" value="${stu.flToefl}" ></td>
			<td style='text-align:right'>ToeflJunior</td><td style='width:80px'><input type="text" name="flToeflJunior" id="flToeflJunior" style="width:60px" value="${stu.flToeflJunior}" ></td>
			<td style='text-align:right'>IELTS</td><td style='width:80px'><input type="text" name="flIelts" id="flIelts" style="width:60px" value="${stu.flIelts}" ></td>
			<td style='text-align:right'>GRE</td><td style='width:80px'><input type="text" name="flGre"   id="flGre"  style="width:60px" value="${stu.flGre}" ></td>
			<td style='text-align:right'>GMAT</td><td style='width:80px'><input type="text" name="flGmat"  id="flGmat" style="width:60px" value="${stu.flGmat}" ></td>			
		</tr>
		<tr>
		  <td style='text-align:right'>SAT</td><td><input type="text" name="flSat"   id="flSat"  style="width:60px" value="${stu.flSat}" ></td>
		  <td style='text-align:right'>SAT2</td><td><input type="text" name="flSat2"  id="flSat2" style="width:60px" value="${stu.flSat2}" ></td>
		  <td style='text-align:right'>SSAT</td><td><input type="text" name="flSsat"  id="flSsat" style="width:60px" value="${stu.flSsat}" ></td>
		  <td style='text-align:right'>ACT</td><td><input type="text" name="flAct"   id="flAct"  style="width:60px" value="${stu.flAct}" ></td>
		</tr>
		</table>	
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
		<td style="text-align:right">投放城市</td>
		<td >
			<input type="text" name="toCity" value="${stu.toCity}" style="width:120px" readonly="readonly" />
			<input type="hidden" name="toCityId" value="${stu.toCityId}" />
			<a href="javascript:void(0)" onclick="jsSelArea()">...</a>
		</td>
		<td style="text-align:right">培训需求</td>
		<td colspan="3"><input type='text' name="pxRequire" value="${stu.pxRequire}" style='width:400px'></td>		
	</tr>
	
	<tr>
		<td style="text-align:right">Call Center</td>
		<td >
			<select name="callCenter" >
				<option value=''>请选择</option>
			<c:forEach var="item" items="${ccLevelList}">
				<option value='${item.dictName}' <c:if test="${stu.callCenter==item.dictName}">selected</c:if>>${item.dictName}</option>
			</c:forEach>
			</select>
		</td>
		<td style="text-align:right">资源属主</td>
		<td colspan='3'>
			<input type="text" name="ownerName" value="${stu.ownerName}" style='width:80px' readonly/>
			<input type="hidden" name="ownerId" value="${stu.ownerId}" />
		<c:if test="${user.id==1 || user.companyId == 14 || user.companyId == 115}"> <!-- 暂时只对资源管理中心/TMK 开放 -->
			<a href='javascript:void(0)' onclick="openUserDialog('owner')" >...</a>
		</c:if>
			(数据归属人可以查看此客户信息)
		</td>	
	</tr>		
<c:if test="${stu.id !=null}">
	<tr>
		<td style="text-align:right">录入人</td>
		<td colspan='5'>
			<input name="creatorName" value="${stu.creatorName}" type="text" style='width:80px' readonly/>
			&nbsp;录入时间
			<input name="createdAt" value="<fmt:formatDate value='${stu.createdAt}' pattern='yyyy-MM-dd HH:mm'/>" type="text" style='width:110px' readonly/>
		</td>
	</tr>
</c:if>
</table>
</form>
</div>

<c:if test="${stu.id !=null}">
<div style='padding-top:10px'>
	<table id="zxgwGrid" title="咨询顾问列表" style="width:780px;"
          idField="id"
            rownumbers="true" singleSelect="true">
    </table>
    <div id="zxgwToolbar">
    <!--
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openUserDialog('newZxgw')">增加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeZxgw()">删除</a>
        -->
      <c:if test="${stu.id !=null && isZxgw}">  
        <a href="javascript:void(0)" onclick="openStuLevelDialog()">修改顾问评级</a>
      </c:if>  
    </div>
</div>

<div style='padding-top:10px'>
	<table id="contactGrid" title="顾问回访及联系信息" style="width:1080px;height:420px"
           pagination="true" idField="id"
            rownumbers="true" singleSelect="true">
    </table>
    <div id="contactToolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newContactRecord()">New</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editContactRecord()">Edit</a>
    </div>
</div>

<div id="contactDlg" class="easyui-dialog" style="width:600px;height:380px;padding:5px 5px"
            closed="true" buttons="#contactDlg-buttons">        
        <form id="contactForm" method="post">
                <input type='hidden' name="id" />
                <input type='hidden' name="stuId" value="${stu.id}" />
                <input type='hidden' name="oldStuLevel" />
        <table>
        	<tr>
              <td style='width:90px'></td>
              <td style='width:160px'></td>
              <td style='width:70px'></td>
              <td style='width:200px'></td>
            </tr>  
        <!--
        	<tr>
              <td style='text-align:right'>联系时间</td>
              <td>
                <input name="contactDate" onclick="WdatePicker({maxDate:'%y-%M-%d'});" readonly style='width:80px'/>
                <select name="contactDateH">
              <%
                for(int ih=0; ih <24; ih++){
                	int h=(ih +8)%24;
                	java.text.DecimalFormat df = new java.text.DecimalFormat("00");
                	out.println("<option value='" + df.format(h) + "'>" + df.format(h) + "</option>"); 
                }	
              %>
                </select>:
                <select name="contactDateM">
                	<option value='00'>00</option>
                	<option value='15'>15</option>
                	<option value='30'>30</option>
                	<option value='45'>45</option>	
                </select>
              </td>  
            </tr> -->
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
              <td style='text-align:right'>回访类型</td>
              <td >
              	<select name='callbackType' class="easyui-validatebox" data-options="required:true">
              	<c:forEach var="item" varStatus="status" items="${callbackTypes}">
              	 <option value='${item}'>${item.name}</option>
              	 </c:forEach>
              	</select>
              </td>
              <td style='text-align:right'>接听状态</td>
              <td >
              	<select name='contactStatus' >
              		<option value=''>请选择</option>
              		<option value='Y'>接听</option>
              		<option value='N'>未接听</option>
              	</select>
              </td>
            </tr>
            <tr>  
              <td style='text-align:right'>顾问评级</td>
              <td >
              	<select name='stuLevel' ></select>
              </td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>跟进情况</td>
              <td colspan='3'><textarea name="contactText" style='height:90px;width:400px'></textarea></td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>顾问方案</td>
              <td colspan='3'><textarea name="caseText" style='height:90px;width:400px'></textarea></td>
            </tr>
            <tr>
              <td style='text-align:right;vertical-align:top'>下次跟进时间</td>
              <td colspan='3'>
              	<input name="nextDate" onclick="WdatePicker({minDate:'%y-%M-%d'});" readonly style='width:80px'/>
              	<select name="nextDateH" style='width:40px'>
              <%
                for(int ih=0; ih <24; ih++){
                	int h=(ih +8)%24;
                	java.text.DecimalFormat df = new java.text.DecimalFormat("00");
                	out.println("<option value='" + df.format(h) + "'>" + df.format(h) + "</option>"); 
                }	
              %>
                </select>:
                <select name="nextDateM" style='width:40px'>
                	<option value='00'>00</option>
                	<option value='15'>15</option>
                	<option value='30'>30</option>
                	<option value='45'>45</option>	
                </select>
              </td>
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

<div id='divFromTree' class="easyui-dialog" title="咨询渠道选择" data-options="closed:true" style="width:500px;height:360px;padding:10px">
	<ul id="fromTree" class="easyui-tree" ></ul>
</div>
<div id='divPlanXlTree' class="easyui-dialog" title="申请学历" data-options="closed:true" style="width:500px;height:360px;padding:10px">
	<ul id="planXlTree" class="easyui-tree" ></ul>
</div>
<div id='divUnitTree' class="easyui-dialog" title="部门选择" data-options="closed:true" style="width:500px;height:420px;padding:10px">
	<ul id="unitTree" class="easyui-tree" ></ul>
</div> 
	
<div id='areaDialog' class="easyui-dialog" title="投放城市" data-options="closed:true,buttons:[{
				text:'确定',
				handler:areaSelected
			},{
				text:'close',
				handler:function(){$('#areaDialog').dialog('close');}
			}]" style="width:420px;height:180px;padding:10px">
	<table>
		<tr>
		  <td>
		  	省
		    <select id='areaProvinceId' name='provinceId' onchange="areaChanged(this)">
		    </select>
		</td>
		<td>
			市
			<select id='areaCityId' name='cityId' onchange="areaChanged(this)">
			</select>
		</td>		
		<td>
			区县
			<select id='areaCountyId' name='countyId' >
			</select>
		</td>
	  </tr>
	</table>
</div>

<div id='stuLevelDialog' class="easyui-dialog" title="顾问评级" data-options="closed:true,buttons:[{
				text:'确定',
				handler:setStuLevel
			},{
				text:'close',
				handler:function(){$('#stuLevelDialog').dialog('close');}
			}]" style="width:420px;height:180px;padding:10px">
		<select id='stuLevel' name='stuLevel' >
			<option value=''>请选择</option>
			<c:forEach var="item" varStatus="status" items="${levelList}">
				<option value='${item.id}'>${item.dictName}</option></c:forEach>
		</select>
</div>

<div id='divAlarm' class="easyui-dialog" style="width:550px;height:280px;padding:5px"
	 data-options="closed:true,title:'提醒信息',modal:true,
			buttons:[{
				text:'确定',
				handler:submitAlarm
			},{
				text:'关闭',
				handler:function(){$('#divAlarm').dialog('close');}
			}]">
	<form name='alarmForm' id='alarmForm'>		
	<table>
		<tr>
			<td style='width:60px;text-align:right'>提醒内容</td>
			<td style='width:440px;'>
				<textarea name='content' style='width:400px;height:80px'></textarea>
				<input type='hidden' name='id' />
			</td>
		</tr>
		<tr>	
			<td style='text-align:right'>提醒方式</td>
			<td >
				<input type='checkbox' name='alarm_way' value='1'><%= MessageType.getName(1) %>
				<input type='checkbox' name='alarm_way' value='4'><%= MessageType.getName(4) %>
				<!-- <input type='checkbox' name='alarm_way' value='2'><%= MessageType.getName(2) %> -->
			</td>
		</tr>
		<tr>	
			<td style='text-align:right'>提醒时间</td>
			<td >
				<input type='text' name='alarmDate' style='width:70px' readonly onclick="WdatePicker({minDate:'%y-%M-%d'})" />
				<select name='alarm_time_h' style='width:40px'>
					<option value=''></option>
				<c:forEach var="i" begin="5" end="23" step="1">  
					<option value='<fmt:formatNumber value="${i}" pattern="00"/>'><fmt:formatNumber value="${i}" pattern="00"/></option>
				</c:forEach>
				</select> 时
				<select name='alarm_time_m' style='width:40px'>
					<option value=''></option>
				<c:forEach var="i" begin="0" end="55" step="5">  
					<option value='<fmt:formatNumber value="${i}" pattern="00"/>'><fmt:formatNumber value="${i}" pattern="00"/></option>
				</c:forEach>
				</select>分
			</td>
		</tr>
		<tr>
			<td style='text-align:right'>提醒对象</td>
			<td>
				<input type="text" name="alarmUserNames" style="width:280px;" readonly="readonly">
				<a href='javascript:void(0)' onclick="openUserDialog('alarm')">...</a>
				<a href='javascript:void(0)' onclick="userSelected({purpose:'alarm',id:'${user.id}',name:'${user.name}'})">我</a>
				
				<input type="hidden" name="alarmUserIds" >
				<input type='hidden' name='repeatType' value='ONLY' />
			</td>
		</tr>
	</table>
	</form>
</div>

<div id='divSms' class="easyui-dialog" style="padding:10px" data-options="closed:true,buttons:[{
				text:'发送',
				handler:sendSms}]" >
	
</div> 


  <div id='userDialog' class="easyui-dialog" title="选择共享顾问" data-options="closed:true" style="width:400px;height:200px;padding:10px">
  	<input type='hidden' name='purpose' /> <!-- 选择用户的目的 -->
	公司<select name='companyId'>
		<option  value=''>请选择</option>
		<c:forEach var="item" items="${companyList}">
			<option value='${item.id}' >${item.name}</option>
		</c:forEach>
		</select>
	姓名<input type='text' name='name' style='width:60px' />	
	<a href='javascript:void(0)' onclick='searchUsers()'>查询</a>
	
	<br>
	<table id='tblUser'>
	</table>
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
<script type="text/javascript">
	function openStuLevelDialog(){
		$('#stuLevelDialog').dialog('center');
		$('#stuLevelDialog').dialog('open');
	}
	function setStuLevel(){
		var levelId = $('#stuLevel').val();
		if(levelId == '')
			return;
		$.ajax({
				type: 'POST',
				url: 'updateLevel.do',
				data: {stuId:'${stu.id}',levelId:levelId},
				dataType: "json",
				error:function(){
					alert('系统异常'); 
				},
				success: function(json){
					$('#zxgwGrid').datagrid('load');
					$('#stuLevelDialog').dialog('close');
				}
			});
	}
	
	function openAlarmDialog(){
		$('#divAlarm').dialog('center');
		$('#divAlarm').dialog('open');
	}
	
	
	function submitAlarm(){	
		var params =  $.serializeObject($('#alarmForm'));	
		params.stuId = '${stu.id}';
		params.url = '/lx/student/inquireRecord.do?id=${stu.id}';
		params.alarm_time = '';
		if(params.alarm_time_h !='' && params.alarm_time_m ==''){
			$.messager.alert('Warning','请正确录入提醒时间(日期/小时/分都填或都不填)');
			return;
		}
		if(params.alarm_time_h !=''){
			params.alarm_time = params.alarm_time_h + ":" + params.alarm_time_m;
		}
		if( (params.alarmDate == '' && params.alarm_time !='') || (params.alarmDate != '' && params.alarm_time =='') ){
			$.messager.alert('Warning','请正确录入提醒时间(日期/小时/分都填或都不填)');
			return;
		}
		
		$.ajax({
        	type:'post',
        	url:'/alarm/submit.do',
        	data:params,
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
                	$('#grid').datagrid('reload');
                	$('#divAlarm').dialog('close');     // close the dialog
                }
        	}
		});
	}
	
	function openSmsDialog(){
		$('#divSms').dialog('center');
		$('#divSms').dialog('open');
	}
	function sendSms(){
		if( $('#smsForm').find('input[name=sendingFlag]').val() ==1)
			return;
		
		var params =  $.serializeObject($('#smsForm'));
		params.mobile = $.trim(params.mobile);
		params.content = $.trim(params.content);
		if( params.mobile == '' || params.content ==''){
			$.messager.alert({title:'提示信息',msg:'手机号和短信内容不能为空'});
			return;
		}
		if( params.content.length  >256){
			$.messager.alert({title:'提示信息',msg:'短信内容太长了'});
			return;
		}
		
		$.messager.confirm('操作确认', '确认信息无误,发送此短信?', function(r){
			if (!r) return;
			
			$('#smsForm').find('input[name=sendingFlag]').val('1');	
			$.ajax({
				type: 'POST',
				url: '/sms/submit',
				data: params,
				dataType: "json",
				error:function(){
					$('#smsForm').find('input[name=sendingFlag]').val('0');
					alert('系统异常'); 
				},
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show({title:'成功提醒',msg:'操作成功'});
					}else{
						$.messager.show({title:'操作失败',msg:'操作失败'});
					}
					$('#divSms').dialog('close');
				}
			});
		});		
	}
	
	function jsSelArea(){
		if( $('#provinceId option').length >1){
			$('#areaDialog').dialog('center');	
			$('#areaDialog').dialog('open');
			return;	
		}
		
		$.ajax({
			type: 'POST',
			url: '/area/1/children',
			data: {},
			dataType: "json",
			error:function(){ alert('系统异常'); },
			success: function(json){
				if(json.errorCode != 200){
					alert('系统异常'); 
					return;
				}
				var areaObjId = '#areaProvinceId';
				$(areaObjId).empty();
				$(areaObjId).append("<option value=''>请选择</option>");
				$.each(json.data, function(index, item){
					$(areaObjId).append("<option value='" + item.id +"'>" + item.abbr + "</option>");
				});
				$('#areaDialog').dialog('center');	
				$('#areaDialog').dialog('open');
			}
		});		
		
	}
	
	function areaChanged(obj){
		var childObjId;
		if(obj.name == 'provinceId'){
			$('#areaCityId').empty();
			childObjId = 'areaCityId';
		}
		else{
			childObjId = 'areaCountyId';
		}
		$('#areaCountyId').empty();
		if( obj.value == '')
			return;
		
		$.ajax({
			type: 'POST',
			url: '/area/' + obj.value + '/children',
			data: {},
			dataType: "json",
			error:function(){ alert('系统异常'); },
			success: function(json){
				if(json.errorCode != 200){
					alert('系统异常'); 
					return;
				}
				var areaObjId = '#' + childObjId;
				$(areaObjId).empty();
				$(areaObjId).append("<option value=''>请选择</option>");
				$.each(json.data, function(index, item){
					$(areaObjId).append("<option value='" + item.id +"'>" + item.abbr + "</option>");
				});
			}
		});	
	}
	
	function areaSelected(){
		var szAreaId = '', szAreaName='';
		var objId = 'areaCountyId';
		szAreaId = $('#' + objId).val();
		if(szAreaId == null || szAreaId == ''){
			objId = 'areaCityId';
			szAreaId = $('#' + objId).val();
		}
		if(szAreaId == null || szAreaId == ''){
			objId = 'areaProvinceId';
			szAreaId = $('#' + objId).val();
		}
		
		if(szAreaId == null || szAreaId == ''){
			$.messager.alert('提醒','请选择投放城市');
			return;
		}
		szAreaName =  $('#' + objId).find("option:selected").text(); 
		
		$('#frmInquireRecord').find('input[name=toCity]').val(szAreaName);
		$('#frmInquireRecord').find('input[name=toCityId]').val(szAreaId);
		$('#areaDialog').dialog('close');
	}
	
	function openUserDialog(szPurpose){
		$('#userDialog').find("input[name=purpose]").val(szPurpose);
		if(szPurpose == 'share')
			$('#userDialog').dialog({title:'选择共享顾问'});
		else if(szPurpose == 'owner')
			$('#userDialog').dialog({title:'选择资源属主'});
		else if(szPurpose == 'newZxgw')
			$('#userDialog').dialog({title:'选择咨询顾问'});
		else if(szPurpose == 'alarm')
			$('#userDialog').dialog({title:'选择提醒对象'});
			
		$('#userDialog').dialog('center');
		$('#userDialog').dialog('open');	
	}
	function userSelected(props){
		var szPurpose = $('#userDialog').find("input[name=purpose]").val();
		if(props.purpose)
			szPurpose = props.purpose;
		
		if(szPurpose == 'share'){
			doShare(props);
		}else if(szPurpose == 'owner'){
			$('#frmInquireRecord').find('input[name=ownerId]').val(props.id);
			$('#frmInquireRecord').find('input[name=ownerName]').val(props.name);
			$('#userDialog').dialog('close');
		}else if(szPurpose == 'alarm'){
			$('#divAlarm').find('input[name=alarmUserIds]').val(props.id);
			$('#divAlarm').find('input[name=alarmUserNames]').val(props.name);
			$('#userDialog').dialog('close');
		}else if(szPurpose == 'newZxgw'){
			$.ajax({
				type: 'POST',
				url: '/lx/custAssign/allocate.do',
				data: {stuIds:'${stu.id}', toZxgwId:props.id},
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					$('#zxgwGrid').datagrid('load');
					$('#userDialog').dialog('close');
				}	
			});
		}	
	}
	
	function doShare(props){		
		var params={};
		params.stuId = '${stu.id}';
		params.fromUserId = '${user.id}';
		params.toUserId   = props.id;
		params.toUserName = props.name;
		if( params.toUserId == ''){
			alert('请选择顾问');
			return;
		}
		if(params.fromUserId== params.toUserId){
			$.messager.alert({title:'提醒信息',msg:'不能共享给自己'});
			return;
		}
		
		$.messager.confirm('操作确认', '确认和' + params.toUserName + '共享此客户资源信息?', function(r){
			if (!r) return;
			
			$.ajax({
				type: 'POST',
				url: '/stushare/addSingle.do',
				data: params,
				dataType: "json",
				error:function(){ alert('系统异常'); },
				success: function(json){
					if(json.errorCode == 200){
						$.messager.show({title:'成功提醒',msg:'操作成功'});
						jsQuery();
					}else{
						$.messager.show({title:'操作失败',msg:'操作失败'});
					}
					$('#userDialog').dialog('close');
				}
			});
		});
	}
	
	function searchUsers(){
		var params = {};
		params.companyId = $('#userDialog').find("select[name=companyId]").val();
		params.enabled = true;
		params.name = $.trim( $('#userDialog').find("input[name=name]").val() );
		if( params.name == ''){
			$.messager.alert({title:'提醒',msg:'请录入姓名'});
			return;
		}
		
		$.ajax({
			type: 'POST',
		 	url: '/user/listData.do',
		 	data: params,
		 	dataType: "json",
		 	error:function(){ alert('系统异常'); },
		 	success: function(json){
		 		$('#tblUser').html('');
		 		if( json.rows.length == 0){
		 			$.messager.alert({title:'提醒',msg:'没找到顾问'});
		 			return;
		 		}
		 		for(i=0; i < json.rows.length; i++){
		 			var item = json.rows[i];
		 			if(i >5) break;
		 			var szName = item.name;
		 			
		 			trHtml = "<tr>"
		 			       + "<td>" + json.rows[i].companyName +"</td>"
		 			       + "<td>" + json.rows[i].unit.name +"</td>"
		 			       + "<td><a href='javascript:void(0)' onclick=\"userSelected({id:'" + item.id + "',name:'" + szName + "'})\">"
		 			       + item.name + "</td></tr>"
		 			$('#tblUser').append(trHtml); 
		 		}
		 	}
		});
	}
	
	function removeZxgw(){
		var rows = $('#zxgwGrid').datagrid('getSelections');
		if(rows.length ==0){
			return;
		}
		var zxgwIds='';
		for(i=0; i < rows.length; i++){
			if(i==0)
				zxgwIds = rows[i].zxgwId;
			else
				zxgwIds += "," + rows[i].zxgwId;	
		}
		$.ajax({
			type: 'POST',
			url: 'assign/revoke.do',
			data: {stuId:'${stu.id}', zxgwIds:zxgwIds},
			dataType: "json",
			error:function(){ alert('系统异常'); },
			success: function(json){
				$('#zxgwGrid').datagrid('load');
				$('#userDialog').dialog('close');
			}	
		});
	}

(function($){
	$('#divSms').dialog({
		title:"短信发送",
		width:480,
		height:300,
		href:'/sms/student?id=${stu.id}' 
		});
		
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
