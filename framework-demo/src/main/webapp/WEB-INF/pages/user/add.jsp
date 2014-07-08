<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "contextPath" value="${pageContext.request.contextPath}" scope="request"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>必联bid版框架demo示例</title>
</head>
<body>
<h1>必联bid版框架demo示例</h1>
<form action="${contextPath}/user/save.htm" method="post">
<table>
	<tr>
		<td align="right">
			用户名：
		</td>
		<td align="left">
			<input name="username" value="${user.username}"/>
		</td>
		<td align="right">
			密码：
		</td>
		<td align="left">
			<input name=password value="${user.password}"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			性别：
		</td>
		<td align="left">
			<input name="gender" value="${user.gender}"/>
		</td>
		<td align="right">
			备注：
		</td>
		<td align="left">
			<input name=remark value="${user.remark}"/>
		</td>
	</tr>
	<tr>
		<td colspan="4"><input type="submit" value="提交"/></td>
	</tr>
</table>
</form>
<a href="${contextPath}/user/list.htm">返回列表</a>
</body>
</html>