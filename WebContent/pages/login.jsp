<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../css/main.css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<script src="../scripts/session.js"></script>
<script src="../scripts/jquery-2.1.1.js"></script>

<body>
	<div>
		<%@ include file="header.jsp"%>
	</div>
<div id="controls">
	Username:
	<input type="text" id="username">
	<br> Password:
	<input type="password" id="password">
	<input type="button" id="btnLogin" value="Login" onclick="doLogin();">
	<br> -- OR --
	<input type="button" value="Create an Account" onclick="window.location='createAccount.jsp';">
</div>
	<div id="message"></div>
</body>

<script>
	function doLogin()
	{
		var username = $("#username").val();
		var password = $("#password").val();
		
		$("#controls").html("loading...");

		userLogin(username, password, function(response)
		{


			var json = JSON.parse(response);
			if (json.jsontype == "user" && json.username == username && json.password == password)
				startSession(json);
			else if (json.jsontype == "error")
			{
				var msg = json.message;

				if (json.stacktrace != "undefined")
				{
					msg += "<br>Stack trace<pre>" + json.stacktrace + "</pre><br>";
				}

				$('#message').html(msg);
			}
		});
	}

	function startSession(user)
	{
		//set sessionId
		setCookie("s", user.sessionId, 1, "/");
		//LOGIN redirect to Homepage
		window.location = "gameboard.jsp";
	}

	function loadPreviousSession()
	{
		//TODO
	}

	$(document).ready(function(){
		$('#username').focus();
		$("#password").keypress(function(e)
				{
					if (e.which == 13)
					{
						doLogin();
					}
				});
	});
</script>
</html>