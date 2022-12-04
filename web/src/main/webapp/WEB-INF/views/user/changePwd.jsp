<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>修改密码</title>
	<link rel="shortcut icon" href="/favicon.ico" />
<link href="/_resources/css/default/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/demo/demo.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript">
/*<![CDATA[*/
	function ajaxUpdate(){ 
		var params = {};
		
		var reg = /^(?=.{6,})(?=.*[a-zA-Z])(?=.*[0-9]).*$/;
		
		var szPasswd1 = $('input[name=newPasswd]').val();
		var szPasswd2 = $('input[name=newPasswd1]').val();
		if(szPasswd1 != szPasswd2 ){
			alert('新密码两次输入不一致');
			return;
		}
		if( !szPasswd1.match(reg) ){
			alert('密码必须6位以上，且须包含字母 数字');
			return;
		}
		
		params.account  = $('input[name=account]').val();	
		params.oldPasswd  = $('input[name=oldPasswd]').val();		
		params.newPasswd  = $('input[name=newPasswd]').val();

		$.ajax({
		  url: "/user/doChangePwd.do", 
		  type:'post',
		  data: params,
		  dataType: "json",
		  error: function(){alert('系统异常');},
		  success: function(json){
		  	if(json.errorCode == 200){
		  		alert('修改成功');
		  	}else{
		  		alert('修改失败-' + json.error);
		  	}
		  }
		});
	}
	function clearForm(){
		$('#ff').form('clear');
	}	 
	$(document).ready( function(){ $('input[name=oldPasswd]').focus(); } );
/*]]>*/
</script>
</head> 

<body >
	<div class="easyui-panel" title="修改密码" style="width:500px">
    <table>
        <tr>
        	<td> 用户名：</td>
            <td><input name="account" value="${user.account}" style="width: 200px;" readonly/></td>
        </tr> 
        <tr>
                    <td>   当前密码：</td>
                    <td>
                        <input name="oldPasswd" type="password" style="width: 200px;" />
                    </td>
        </tr>              
        <tr>
                    <td>   新密码：</td>
                    <td>
                        <input name="newPasswd" type="password" style="width: 200px;" />
                    </td>
                </tr>
               <tr id = 'id_nPwdP'>
                    <td>   确认新密码：</td>
                    <td>
                        <input name="newPasswd1" type="password" style="width: 200px;" />
                        <input name="txtPasswd" type="hidden" />
                    </td>
                </tr>                                             
    </table>
    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="ajaxUpdate()">Submit</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">Clear</a>
	</div>
</div>	
</body>
</html>