<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="<%=basePath %>/login.do" method="post" id="loginForm">
	用户名：<input type="text" id="username" name="username" />
	用户名：<input type="text" id="username" name="username" />
	<input type="submit" id="subBtn" value="登录" />
	<input type="text" id="clientUrl" name="clientUrl" value="${clientUrl }" />
</form>
</body>
</html>