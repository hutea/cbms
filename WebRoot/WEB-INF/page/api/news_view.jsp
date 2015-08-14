<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
      
    <title>新闻查看</title>
    
		<link href="${pageContext.request.contextPath}/resource/chain/css/bootstrap.min.css" rel="stylesheet">
		<script src="${pageContext.request.contextPath}/resource/chain/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resource/chain/js/bootstrap.min.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <h3>${news.title}</h3>
    <div><fmt:formatDate value="${news.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /> </div>
    <div style="border-top: 1px dashed ddd;">
    	${news.content}
    </div>
  </body>
</html>
