<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="../stylesheets/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<br>
	<div id="header">
		<%@ include file="header.jsp"%> 
	</div>
		This is the login page
	<form>
		Username: <input type="text" name="Username"><br>
		Password: <input type="text" name="Password"><br>
		<button type="submit">Submit</button>
	</form>
</body>
</html>