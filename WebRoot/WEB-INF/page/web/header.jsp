<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!--上部开始-->
<div class="top">
	<div class="top_header box0">
		<div class="logo"><a href="<%=basePath%>"><img src="<%=basePath %>resource/page/images/top_logo.png" alt="logo" /></a></div>
		<ul class="top_right"><%-- class="b1" b2 b3 b4 --%>
			<li class="first"><a href="#">[<span>登录</span>|<span>注册</span>]</a><b></b></li>
			<li class="second"><a href="<%=basePath%>web/news/list">新闻动态</a><b class="b2"></b></li>
			<li class="third"><a href="<%=basePath%>manage/server/detail?id=1">服务范围</a><b class="b3"></b></li>
			<li class="last"><a href="<%=basePath%>manage/server/appdown">APP下载</a><b class="b4"></b></li>
		</ul>
	</div>
	<!--box banner start-->
	<!--box banner end-->
</div>
<!--上部结束-->
