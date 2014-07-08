<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Error Page</title>
</head>
<body>
	<div id="content">
		<br>
		<button onclick="history.back();">Back</button>
		<br>
		<p>
			<a href="#" onclick="showDetail();">Administrator click here to get the detail.</a>
		</p>
		<div id="detail_error_msg">
			ErrorPage
			<pre>
			   ErrorMsg:${errorMsg}
 			<%--exception.printStackTrace(new java.io.PrintWriter(out));--%>  
			</pre>
		</div>
	</div>
</body>
</html>