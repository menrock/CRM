<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="gb312">
<title>部门维护</title>
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/_resources/easyui/themes/icon.css">
<script type="text/javascript" src="/_resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/_resources/easyui/jquery.easyui.min.js"></script>
</head>
<body>
	<div class="easyui-panel" style="padding:5px">
		<ul class="easyui-tree" data-options="url:'treeData.do',
		onClick : function(node) {
			var src = 'edit.do?id=' + node.id;
			window.open(src,'UnitRightFrm');		
		},
		method:'get',animate:true,lines:true"></ul>
	</div>
</body>
</html>