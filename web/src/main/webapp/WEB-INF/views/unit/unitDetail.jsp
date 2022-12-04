<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
<title>部门信息</title>
<script language='javascript'>
  function jsSave() {
		$(':text').each(function(){
			$(this).val($.trim($(this).val()));
		});
		
		var params =  $.serializeObject($('#formUnit'));
		var szAlias = $.trim(params.alias);
				
		var reg=/^[a-zA-z][a-zA-Z0-9-_]{1,14}$/;
		
		if(szAlias == '' || !reg.test(szAlias) ){
			alert('部门代码不符合规则');
			return;
		}
		if( $.trim(params.name) == ''){
			alert('部门名称不能为空');
			return;
		}
		
		//params.id = $('#id').val();
		$.ajax({
		  type: 'POST',
		  url: "save.do" ,
		  data: params,
		  dataType: "json",
		  error:function(){ alert('系统异常'); },
		  success: function(json){
		  	if(json.errorCode == 200){
		  		$('#id').val(json.data.id);
		  		alert('保存成功');
		  	}else{
		  		alert('保存失败-' + json.error);
		  	}
		  }
		});
	}
	
	function jsNew(){
		$('#newUnitForm').submit();
	}
</script>
</head>
<body>
  <TABLE style="width:800px" border=0 cellspacing=0 cellpadding=0>
    <tr><td height=10></td></tr>
  	<tr>
  		<td width=100% align='center' height=30 valign='middle' class='title1'>
  			
  		</td>
  	</tr>
  </TABLE>
      
	<form method="post" id="formUnit">	
	<TABLE border="0">
		<tr>
	    	<td style="width:80px;text-align:right">ID</td>
	    	<TD>${unit.id}
	             <input type='hidden' id='id' name='id' value='${unit.id}' />
	             <input type='hidden' id='parentId' name='parentId' value='${pUnit.id}' />
	           </TD>
	         </tr>
	         <tr>
	           <td class='detailTitle' style="width:80px;text-align:right">序号</td>
	           <td>
	             <input type='text' name='showIndex' value='${unit.showIndex}' maxlength=10 />
	           </td>
	         </tr>
	         <tr>
	           <td class='detailTitle' style="text-align:right">部门代码</td>
	           <td>
	             <input type='text' name='alias' value='${unit.alias}' maxlength=40 style="width:200px">
	             (部门代码只能由字母数字及-_组成,且必须由字母开头,不能带。)
	           </td>
	         </tr>
	         <tr>
	           <td class='detailTitle' style="text-align:right">部门名称</td>
	           <td>
	             <input type='text' style="width:260px" name='name' value='${unit.name}' maxlength=250>
	           </td>
	         </tr>  
	    <tr>
	       <td class='detailTitle' style="text-align:right">上级部门</td>
	       <td>${pUnit.name}(${pUnit.code})</td>
		</tr>
		<tr>
	    	<td colspan='2' style='text-align:center'>
	    		<input type="button" name="saveUnit" id="saveUnit" value="保存" onclick="jsSave()" />
	    	<c:if test="${unit !=null}">	
	    		<input type="button" name="newUnit" id="newUnit" value="新建下级" onclick="jsNew()" />
	    	</c:if>	
		   </td>
		</tr>
	</TABLE>
</form>
<form action="create.do" method='post' id='newUnitForm'>
<input type='hidden' name='parentId' value='${unit.id}' />
<form>    
</body>
</HTML>