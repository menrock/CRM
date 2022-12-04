<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>合同信息</title>
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
	
<script type="text/javascript">
/*<![CDATA[*/
	var mainTabs;
	
	$(function() {
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
<body id="mainLayout" class="easyui-layout" >
	<div data-options="region:'center'" style="overflow: hidden; ">
		<div id="mainTabs">
			<div title="合同信息" data-options="iconCls:'ext_house'">
				<iframe src="/lx/contract/detail.do?id=${id}" allowTransparency="true" style="border: 0; width: 100%; height: 99%;" frameBorder="0"></iframe>
			</div>
			<div title="附件信息" data-options="iconCls:'ext_house'">
				<iframe src="/cust/attach/listByContract.do?conId=${id}" allowTransparency="true" style="border: 0; width: 100%; height: 99%;" frameBorder="0"></iframe>
			</div>
			<div title="录取签证附件" data-options="iconCls:'ext_house'">
				<iframe src="/lx/offerVisa/resultAttach/listByContract.do?conId=${id}" allowTransparency="true" style="border: 0; width: 100%; height: 99%;" frameBorder="0"></iframe>				
			</div>
			<div title="宣传配合附件" data-options="iconCls:'ext_house'">
				<iframe src="/lx/contract/xcclAttach/listByContract.do?conId=${id}" allowTransparency="true" style="border: 0; width: 100%; height: 99%;" frameBorder="0"></iframe>
			</div>
		</div>
	</div>  		
</body>

</html>