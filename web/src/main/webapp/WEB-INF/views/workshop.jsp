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
%>
<!DOCTYPE html>
<html >
<head>
<title>CRM</title>
	<meta charset="UTF-8" />
	<link rel="shortcut icon" href="/favicon.ico" />
	<link id="easyuiTheme" rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/extIcon.css" />
	
	<!-- script -->
	<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="/_resources/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="/_resources/js/jqueryUtils.js"></script>
	
<script type="text/javascript"> 
/*<![CDATA[*/

var au = document.createElement("audio");
au.preload="auto";
au.src = "/_resources/sound/message.wav";
function playSound() {
    au.play();
} 
	 
var msgDlgId=window.setInterval(msgDlg,10000);
function msgDlg(){
	$.ajax({
		type: 'POST',
		url: '/sysMessage/myPromptData.do',
		data: {},
		dataType: "json",
		error:function(){ },
		success: function(json){
			if(json.length ==0)
				return;
			
			playSound();
			
			var msg = '';
			for(i=0; i < json.length; i++){
				if(i >0) msg += "<br/>";
				if(json[i].url){
					msg += "<a href='" + json[i].url + "' target=_blank>" + json[i].content + "</a>";
				}else{
					msg += json[i].content;
				}
			}			
			if(false){
				$('#divMsgDlg').html(msg);
				$('#divMsgDlg').dialog('open');	
			}else{
				$.messager.show({
					showSpeed:1200,
					timeout:0,
					width:320,
					height:120,
					title:'系统消息',
					draggable:true,
					msg: msg
				});
			}			 	
		}
	});	
} 

	
		var mainMenu;
		var mainTabs;
	
		$(function() {
			var treeData = [ {
				text : '留学管理系统',
				iconCls : 'ext_computer',
				children : [ {
					text : '我的客户',
					iconCls : 'ext_folder_wrench',
					attributes : {
						url : "/lx/student/myStudents.do"
					}
				},{
					text : '我录入的客户',
					iconCls : 'ext_folder_wrench',
					attributes : {
						url : "/lx/student/myCreatedStudents.do"
					}
				}
				<%if( hasFunc(lsFunc, "custquery") ){ %>
				,{
					text:'客户池', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/student/pool.do'
					}
				}<%} %>
				<%if( hasFunc(lsFunc, "youngquery") ){ %>
				,{
					text:'低龄客户', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/student/youngList.do'
					}
				}<%} %>
				,{
					text:'客户管理', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/student/list.do'
					}
				},{
					text:'客户分配信息', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/student/stuZxgwList.do'
					}
				},{
					text:'我的回访提醒', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/callback/myRemindList.do'
					}
				}
				<%if( hasFunc(lsFunc, "custquery") ){ %>
				,{
					text:'回访提醒管理', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/callback/remindList.do'
					}
				}<%} %>
				<%if( hasFunc(lsFunc, "lx_invite_visit_manager") ){ %>
				,{
					text:'邀约到访', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/visitInvite/list.do'
					}
				}<%} %>				
				<%if( 1==2 ){ %>
				,{
					text:'客户导入', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/student/importCust.do'
					}
				}<%} %>
				,{
					text:'合同管理', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/contract/list.do'
					}
				},{
					text : '院校管理',
					attributes : {
						url : "/college/listPage.do",
						target:'_blank'
					}
				},{
					text : '合作机构管理',
					attributes : {
						url : "/coopOrg/listPage.do",
						target:'_blank'
					}
				}
				<%if( hasFunc(lsFunc, "comm_inv_input") || hasFunc(lsFunc, "comm_inv_qry") ){ %>
				,{
					text:'佣金账单', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/commission/invList.do'
					}
				}<%} %>
				,{
					text : '报表',
					iconCls : 'ext_bullet_wrench',
					state:'closed',
					children : [ {
						text:'资源统计', iconCls : 'ext_folder_wrench',
						attributes : {
							url:'/lx/report/resource.do'
						}
					},{
						text:'市场人员报表', iconCls : 'ext_folder_wrench',
						attributes : {
							url:'/lx/report/marketStaffResource.do'
						}
					},{
						text:'回访检查统计', iconCls : 'ext_folder_wrench',
						attributes : {
							url:'/lx/report/callbackCheck.do'
						}
					}]
				},{
					text : '后期',
					iconCls : 'ext_bullet_wrench',
					state:'closed',
					children : [ {
						text:'我的客户', iconCls : 'ext_folder_wrench',
						attributes : {
							url:'/lx/contract/myCases.do'
						}
					}<%if( hasFunc(lsFunc, "tran_audit") ){ %>
					,{
						text:'转案审核', iconCls : 'ext_folder_wrench',
						attributes : {
							url:'/lx/contract/tranApply/auditList.do'
						}
					}<%} %>
					]
				}
				]
			},
			<%if( hasFunc(lsFunc, "lx_precust_query") ){ %>
			{
				text : '种子库',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [ {
					text:'留学种子库', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/precust/list.do'
					}
				},{
					text:'留学种子库导入', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/lx/precust/importCust.do'
					}
				}]
			},
			<%} %>
			{
				text : '培训管理系统',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [ {
					text:'我的客户(培训)', 
					iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/px/student/myStudents.do'
					}
				},{
					text:'我录入的客户(培训)', 
					iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/px/student/myCreatedStudents.do'
					}
				}<%if( hasFunc(lsFunc, "px_custquery") ){ %>
				,{
					text:'客户池(培训)', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/px/student/pool.do'
					}
				}
				<%} %>
				]
			},
			{
				text : '产品管理系统',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [ {
					text:'我的客户(产品)', 
					iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/cp/student/myStudents.do'
					}
				},{
					text:'我录入的客户(产品)', 
					iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/cp/student/myCreatedStudents.do'
					}
				}<%if( hasFunc(lsFunc, "cp_custquery") ){ %>
				,{
					text:'客户池(产品)', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/cp/student/pool.do'
					}
				}
				<%} %>
				]
			},
			{
				text : '收银系统',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [ 
				<%if( hasFunc(lsFunc, "sk_fz_confirm") ){ %>
				{
					text:'分总确认', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/payment/payments4FzConfirm.do'
					}
				},<%}%>
				<%if( hasFunc(lsFunc, "sk_confirm") ){ %>
				{
					text:'财务确认', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/payment/payments4FinanceConfirm.do'
					}
				},<%}%>
				{
					text:'收款信息', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/money/list.do'
					}
				}]
			},{
				text : '提醒消息',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [ {
					text:'收件箱', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/sysMessage/myMessage.do'
					}
				},{
					text:'发件箱', iconCls : 'ext_folder_wrench',
					attributes : {
						url:'/alarm/myAlarm.do'
					}
				}
				<%if( hasFunc(lsFunc, "admin") ){ %>,
				{
					text:'短信', iconCls : 'ext_transmit',
					attributes : {
						url:'/sms/list.do'
					}
				}<%} %>
				]
			},
			<%if( hasFunc(lsFunc, "useradmin") || hasFunc(lsFunc, "admin") ){ %>{
				text : '设置',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [
				<%if( hasFunc(lsFunc, "useradmin") ){ %>{
					text : '部门管理',
					iconCls : 'ext_user',
					attributes : {
						url : "/unit/frame.do",
						target:'_blank'
					}
				},{
					text : '用户管理',
					iconCls : 'ext_user',
					attributes : {
						url : "/user/list.do?singlePage=true",
						target:'_blank'
					}
				},{
					text : '留学顾问分组',
					iconCls : 'ext_user',
					attributes : {
						url : "/lx/zxgwTeam/list.do?singlePage=true",
						target:'_blank'
					}
				},
				<%} %>
				<%if( hasFunc(lsFunc, "admin") ){ %>{
					text : '字典维护',
					iconCls : 'ext_user',
					attributes : {
						url : "/dict/main.do",
						target:'_blank'
					}
				},{
					text : '功能点管理',
					iconCls : 'ext_user',
					attributes : {
						url : "/func/list.do"
					}
				}
				<%} %>]
			},	
			<%} %>
			{
				text : '个人',
				iconCls : 'ext_bullet_wrench',
				state:'closed',
				children : [ {
					text : '客户共享',
					iconCls : 'ext_user',
					attributes : {
						url : "/stushare/myList.do"
					}
				},{
					text : '微信企业号',
					iconCls : 'ext_user',
					attributes : {
						url : "/_resources/images/meishi_qyh_qrcode.jpg"
					}
				},{
					text : '修改密码',
					iconCls : 'ext_user',
					attributes : {
						url : "user/changePwd.do"
					}
				},{
					text : '退出系统',
					iconCls : 'ext_plugin',
					attributes : {
					  onClick:function(){jsLogout();}
					}
				}]
			}
			];
			mainMenu = $('#mainMenu').tree({
				lines:true,
				data : treeData,
				//parentField : 'pid',
				onClick : function(node) {
					if(node.attributes.onClick) {
						var o={};
						o.fn = node.attributes.onClick;
						o.fn();
					}
					else if (node.attributes.url) {
						var src = node.attributes.url;
						var szTarget = node.attributes.target;
						if(szTarget){
							window.open(src,'');
							return;
						}
								
						var tabs = $('#mainTabs');
						var opts = {
							title : node.text,
							closable : true,
							iconCls : node.iconCls,
							content : '<iframe src="' + src + '" allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"/>',
							border : false,
							fit : true
						};
								
						if (tabs.tabs('exists', opts.title)) {
							tabs.tabs('select', opts.title);
						} else {
							tabs.tabs('add', opts);
						}
					}
				}
			});
	
			/**
			 * 设置iframe高度
			 */
			setIframeHeight = function(iframe, height) {
				iframe.height = height;
			}
	
			$('#mainLayout').layout('panel', 'center').panel({
				onResize : function(width, height) {
					setIframeHeight('centerIframe', $('#mainLayout').layout('panel', 'center').panel('options').height - 5);
				}
			});
	
			mainTabs = $('#mainTabs').tabs({
				fit : true,
				border : false,
				tools : [{
					text : '刷新',
					iconCls : 'icon-reload',
					handler : function() {
						var panel = mainTabs.tabs('getSelected').panel('panel');
						var frame = panel.find('iframe');
						try {
							if (frame.length > 0) {
								for (var i = 0; i < frame.length; i++) {
									frame[i].contentWindow.document.write('');
									frame[i].contentWindow.close();
									frame[i].src = frame[i].src;
								}							
								if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
								try {
										CollectGarbage();
									} catch (e) {
									}
								}
							}
						} catch (e) {
							$.messager.alert('提示', '[' + tab.panel('options').title + ']不可以刷新！', 'error');
						}
					}
				}, {
					//text : '关闭',
					iconCls : 'icon-cancel',
					handler : function() {
						var index = mainTabs.tabs('getTabIndex', mainTabs.tabs('getSelected'));
						var tab = mainTabs.tabs('getTab', index);
						if (tab.panel('options').closable) {
							mainTabs.tabs('close', index);
						} else {
							$.messager.show({
								title:'提示',
								msg:'不可以关闭'
							});
						}
					}
				}, {
					iconCls : 'ext_arrow_up',
					handler : function() {
						mainTabs.tabs({
							tabPosition : 'top'
						});
					}
				}, {
					iconCls : 'ext_arrow_down',
					handler : function() {
						mainTabs.tabs({
							tabPosition : 'bottom'
						});
					}
				} ]
			});
	
		});
		/*]]>*/
	</script>
</head> 

<body id="mainLayout" class="easyui-layout" layout:fragment="content">
	<div data-options="region:'north',href:'layout/north'" style="height: 40px; overflow: hidden;" class="logo"></div>
	<div data-options="region:'west',split:true" title="CRM" style="width: 180px; padding: 10px;">
		<ul id="mainMenu"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden; ">
		<div id="mainTabs">
		</div>
	</div>
	width:320,height:120,openAnimation:'show',openDuration:600,shadow:false
<div id='divMsgDlg' class="easyui-dialog" title="系统消息" data-options="closed:true,width:320,height:120,openAnimation:'show',closeAnimation:'hide',openDuration:1200,closeDuration:1200,shadow:false" style="padding:10px">
</div>  		
</body>

</html>