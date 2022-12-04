<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="gb312">	<title>字典数据维护</title>
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript">
 /*<![CDATA[*/
	var mainMenu;
	var mainTabs;
	
	$(function() {
		mainMenu = $('#mainMenu').tree({
			lines:true,
			url : './treeData',
			onClick : function(node) {
				var src = './edit?id=' + node.id;
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
	<div data-options="region:'west',split:true" title="导航" style="width: 220px; padding: 10px;">
		<ul id="mainMenu"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden; ">
		<div id="mainTabs">
			<div title="首页" data-options="iconCls:'ext_house'">
				<iframe src="" allowTransparency="true" style="border: 0; width: 100%; height: 99%;" frameBorder="0"></iframe>
			</div>
		</div>
	</div>
</body>

</html>