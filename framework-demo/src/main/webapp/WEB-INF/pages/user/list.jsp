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
<a href="${contextPath}/user/add.htm">增加</a>
<table>
	<thead>
		<tr>
			<th>ID</th>
			<th>用户名</th>
			<th>密码</th>
			<th>性别</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${userList}" var="user">
		<tr>
			<td>${user.id}</td>
			<td>${user.username}</td>
			<td>${user.password}</td>
			<td>${user.gender}</td>
			<td>${user.remark}</td>
			<td>
				<a href="${contextPath}/user/edit.htm?id=${user.id}">修改</a>
				<a href="${contextPath}/user/delete.htm?id=${user.id}">删除</a>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>

<a href="${contextPath}/user/index.htm">返回首页</a>
</body>
</html>