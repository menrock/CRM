<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" >
/*<![CDATA[*/


	/**
	 * 更换EasyUI主题的方法
	 * 
	 * @param themeName
	 *            主题名称
	 */
	function changeThemeFun(themeName) {
		if ($.cookie('easyuiThemeName')) {
			$('#layout_north_pfMenu').menu('setIcon', {
				target : $('#layout_north_pfMenu div[title=' + $.cookie('easyuiThemeName') + ']')[0],
				iconCls : 'emptyIcon'
			});
		}
		$('#layout_north_pfMenu').menu('setIcon', {
			target : $('#layout_north_pfMenu div[title=' + themeName + ']')[0],
			iconCls : 'tick'
		});

		var $easyuiTheme = $('#easyuiTheme');
		var url = $easyuiTheme.attr('href');
		var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
		$easyuiTheme.attr('href', href);

		var $iframe = $('iframe');
		if ($iframe.length > 0) {
			for ( var i = 0; i < $iframe.length; i++) {
				var ifr = $iframe[i];
				try {
					$(ifr).contents().find('#easyuiTheme').attr('href', href);
				} catch (e) {
					try {
						ifr.contentWindow.document.getElementById('easyuiTheme').href = href;
					} catch (e) {
					}
				}
			}
		}

		$.cookie('easyuiThemeName', themeName, {
			expires : 7
		});
	};
	
	function changePwd(){
		
		var tabs = $('#mainTabs');
		
		var opts = {
					title : '修改密码',
					closable : true,
					iconCls : 'ext_user',
					content : '<iframe src="/user/changePwd.do" allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"/>',
					border : false,
					fit : true
				};
		
		if (tabs.tabs('exists', opts.title)) {
			tabs.tabs('select', opts.title);
		} else {
			tabs.tabs('add', opts);
		}
	}
	
	function jsLogout(){
		$('#frmLogout').submit();
	}
	
	
	var onlineFlag = ${user.online};
	function switchOnline(){
		var szUrl = "/user/online.do";
		if(onlineFlag)
			szUrl = "/user/offline.do";
		$.ajax({
			type: 'POST',
			url: szUrl,
			data: {},
			dataType: "json",
			error:function(){ alert('系统异常'); },
			success: function(json){
				if(json.errorCode == 200){
			  		if(onlineFlag){
			  			$('#aOnline').text('[off]');
			  			onlineFlag = false;
			  		}else{
			  			$('#aOnline').text('[on]');
			  			onlineFlag = true;
			  		}		
			  	}
			}
		});
	}
/*]]>*/	
</script>
<div id="sessionInfoDiv" style="position: absolute; right: 0px; top: 0px;" class="alert alert-info">
	欢迎您！<strong><span sec:authentication="name">${user.name}</span></strong>
	<span id='spOnline'>
		<a href='javascript:void()' onclick='switchOnline()' id='aOnline'>
			<c:if test="${user.online}">[on]</c:if>
			<c:if test="${user.online==false}">[off]</c:if>
		</a>
	</span>
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_kzmbMenu',iconCls:'ext_cog'">控制面板</a>
</div>
<!-- 
<div style="position: absolute; right: 0px; bottom: 0px;">
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_pfMenu',iconCls:'ext_css'">更换皮肤</a> 
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_kzmbMenu',iconCls:'ext_cog'">控制面板</a> 
</div>
 -->
<!-- 
<div id="layout_north_pfMenu" style="width: 120px; display: none;">
	<div onclick="changeThemeFun('default');" title="default">default</div>
	<div onclick="changeThemeFun('gray');" title="gray">gray</div>
	<div onclick="changeThemeFun('metro');" title="metro">metro</div>
	<div onclick="changeThemeFun('bootstrap');" title="bootstrap">bootstrap</div>
	<div onclick="changeThemeFun('black');" title="black">black</div>
</div>
 -->
<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	<div onclick="changePwd();">修改密码</div>
	<!-- 
	<div class="menu-sep"></div>
	<div><a href="logout">锁定窗口</a></div>
	<div class="menu-sep"></div>
	 -->
	<div data-options="iconCls:'ext_door_out'" onclick="jsLogout()">退出系统</div>
</div>

	
<form id="frmLogout" action="/logout" method="post" th:action="@{/logout}">
</form>