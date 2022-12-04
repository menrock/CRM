<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,com.niu.crm.model.UserFunc" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%!
	private boolean hasFunc(List<UserFunc> lsFunc, String funcCode){
		if(lsFunc == null || lsFunc.size() ==0)
			return false;
			
		for(UserFunc uf:lsFunc){
			if(uf.getFuncCode().equals(funcCode))
				return true;
		}
		return false;
	}
%>
<%
  out.clear();
  List<UserFunc> lsFunc = (List<UserFunc>)request.getAttribute("lsFunc");
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>导航树</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link rel="stylesheet" type="text/css" href="/_resources/css/default/newTree.css" /> 
<script type="text/javascript" src="/_resources/js/newTree.js"></script>

<script language="javascript">
	function winOpen(szUrl, szTarget, szFace) {
		var win = window.open(szUrl, szTarget, szFace);
		win.focus();
	}
</script>
</head>
<body>
<div class="dtree">
<script type="text/javascript">
<!--
var tree = new dTree('tree','');

tree.add( 'root', -1, 'crm', '', 'mainFrame');
tree.add( 'lx_mis', 'root', '留学管理系统', '',  'mainFrame' );	

tree.add( 'lx_mis01', 'lx_mis', '我的客户', '/lx/student/myStudents.do',  '_blank' );
tree.add( 'lx_mis05', 'lx_mis', '我录入的客户', '/lx/student/myCreatedStudents.do',  '_blank' );

<%if( hasFunc(lsFunc, "custquery") ){ %>
tree.add( 'lx_mis003', 'lx_mis', '客户池', '/lx/student/pool.do',  '_blank' );
<%} %>
<%if( hasFunc(lsFunc, "youngquery") ){ %>
tree.add( 'lx_mis005', 'lx_mis', '低龄客户', '/lx/student/youngList.do',  '_blank' );
<%} %>
tree.add( 'lx_mis008', 'lx_mis', '客户管理', '/lx/student/list.do',  '_blank' );
tree.add( 'lx_mis011', 'lx_mis', '客户分配信息', '/lx/student/stuZxgwList.do',  '_blank' );

tree.add( 'lx_mis015', 'lx_mis', '我的回访提醒', '/callback/myRemindList.do',  '_blank' );
<%if( hasFunc(lsFunc, "custquery") ){ %>
tree.add( 'lx_mis016', 'lx_mis', '回访提醒管理', '/callback/remindList.do',  '_blank' );
<%} %>

//tree.add( 'lx_mis020', 'lx_mis', '客户导入', '/lx/student/importCust.do',  '_blank' );

<%if( hasFunc(lsFunc, "lx_invite_visit_manager") ){ %>
tree.add( 'lx_mis041', 'lx_mis', '邀约到访', '/lx/visitInvite/list.do?singlePage=true',  '_blank' );
<%} %>

tree.add( 'lx_mis052', 'lx_mis', '合同管理', '/lx/contract/list.do',  '_blank' );
tree.add( 'lx_mis053', 'lx_mis', '院校管理', '/college/listPage.do',  '_blank' );
tree.add( 'lx_mis054', 'lx_mis', '合作机构管理', '/coopOrg/listPage.do',  '_blank' );
<%if( hasFunc(lsFunc, "comm_inv_input") || hasFunc(lsFunc, "comm_inv_qry") ){ %>
tree.add( 'lx_mis055', 'lx_mis', '佣金账单', '/commission/invList.do?singlePage=true',  '_blank' );
<%} %>

<%if( hasFunc(lsFunc, "lx_resource_report") ){ %>
tree.add( 'lx_mis081', 'lx_mis', '资源统计', '/lx/report/resource.do?singlePage=true',  '_blank' );
tree.add( 'lx_mis083', 'lx_mis', '回访检查报表', '/lx/report/callbackCheck.do?singlePage=true',  '_blank' );
tree.add( 'lx_mis086', 'lx_mis', '市场人员报表', '/lx/report/marketStaffResource.do',  '_blank' );
<%} %>


tree.add( 'houqi', 'root', '后期', '',  '' );	
tree.add( 'houqi01', 'houqi', '后期', '/lx/contract/myCases.do',  '_blank' );	
<%if( hasFunc(lsFunc, "tran_audit") ){ %>
tree.add( 'houqi02', 'houqi', '转案审核', '/lx/contract/tranApply/auditList.do',  '_blank' );	
<%} %>

<%if( hasFunc(lsFunc, "lx_precust_query") ){ %>
tree.add( 'precust', 'root', '种子库', '',  'mainFrame' );	
tree.add( 'lx01', 'precust', '留学种子库', '/lx/precust/list.do',  '_blank' );	
tree.add( 'lx02', 'precust', '留学种子库导入', '/lx/precust/importCust.do',  '_blank' );	
<%} %>


tree.add( 'px_mis', 'root', '培训管理系统', '',  'mainFrame' );	

tree.add( 'px_mis01', 'px_mis', '我的客户', '/px/student/myStudents.do',  '_blank' );
tree.add( 'px_mis05', 'px_mis', '我录入的客户', '/px/student/myCreatedStudents.do',  '_blank' );
<%if( hasFunc(lsFunc, "px_custquery") ){ %>
tree.add( 'px_mis003', 'px_mis', '客户池(产品)', '/cp/student/pool.do',  '_blank' );
<%} %>


tree.add( 'cp_mis', 'root', '产品管理系统', '',  'mainFrame' );	

tree.add( 'cp_mis01', 'cp_mis', '我的客户', '/cp/student/myStudents.do',  '_blank' );
tree.add( 'cp_mis05', 'cp_mis', '我录入的客户', '/cp/student/myCreatedStudents.do',  '_blank' );
<%if( hasFunc(lsFunc, "cp_custquery") ){ %>
tree.add( 'cp_mis003', 'cp_mis', '客户池(产品)', '/cp/student/pool.do',  '_blank' );
<%} %>


tree.add( 'money', 'root', '收银系统', '',  'mainFrame' );	
<%if( hasFunc(lsFunc, "sk_fz_confirm") ){ %>
tree.add( 'sk01', 'money', '分总确认', '/payment/payments4FzConfirm.do',  '_blank' );	
<%}%>
<%if( hasFunc(lsFunc, "sk_confirm") ){ %>
tree.add( 'sk01', 'money', '财务确认', '/payment/payments4FinanceConfirm.do',  '_blank' );		
<%}%>
tree.add( 'sk02', 'money', '收款信息', '/money/list.do',  '_blank' );	

tree.add( 'alarmMsg', 'root', '提醒消息', '',  '' );	
tree.add( 'recbox', 'alarmMsg', '收件箱', '/sysMessage/myMessage.do',  '_blank' );	
tree.add( 'sendbox', 'alarmMsg', '发件箱', '/alarm/myAlarm.do',  '_blank' );
<%if( hasFunc(lsFunc, "admin") ){ %>
tree.add( 'sms', 'alarmMsg', '短信', '/sms/list.do',  '_blank' );
<%} %>

tree.add( 'base', 'root', '设置', '',  'mainFrame' );	
<%if( hasFunc(lsFunc, "useradmin") ){ %>
tree.add( 'base01', 'base', '部门管理', '/unit/frame.do',  '_blank' );	
tree.add( 'base02', 'base', '用户管理', '/user/list.do',  '_blank' );
tree.add( 'base03', 'base', '留学顾问分组', '/lx/zxgwTeam/list.do?singlePage=true',  '_blank' );
<%} %>
<%if( hasFunc(lsFunc, "admin") ){ %>
tree.add( 'base13', 'base', '字典维护', '/dict/main.do',  '_blank' );
tree.add( 'base14', 'base', '功能点管理', '/func/list.do',  '_blank' );
<%} %>

tree.add( 'personal', 'root', '个人中心', '',  '' );		
tree.add( 'p01', 'personal', '客户共享', '/stushare/myList.do',  '_blank' );
tree.add( 'p02', 'personal', '微信企业号', '/_resources/images/meishi_qyh_qrcode.jpg',  '_blank' );
document.write(tree);
//-->
</script>
</div>
<br /><br />
</body>
</html>