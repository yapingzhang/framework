<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "contextPath" value="${pageContext.request.contextPath}" scope="request"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>必联bid版框架demo示例</title>
</head>
<body>
<h1>必联bid版框架demo示例</h1>
<h2>请点击下面链接进入CRUD例子</h2>
<a href="${contextPath}/user/list.htm">用户列表CRUD示例</a>
</body>
</html>