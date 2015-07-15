<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create An Account</title>
</head>
<body>
	<div id="header">
		<%@ include file="header.jsp"%>
	</div>
	<script src="../scripts/session.js"></script>
	<script src="../scripts/jquery-2.1.1.js"></script>

	<div>
		Username: <input type="text" id="username" onkeyup="checkAvailability(event);">
		<div id="userAvailability"></div>
		<br> Password: <input type="password" id="password1"> <br>Re-Enter Password: <input type="password" id="password2">
		<div id="passwordCheck"></div>
		<br>First Name: <input type="text" id="firstName"> <br>Last Name: <input type="text" id="lastName"> <br>Email: <input type="text" id="email"> <br> <input id="createButton" type="button" value="Create Account" onclick="doCreateAccount();">

	</div>
</body>
<script>
	function doCreateAccount()
	{
		var username = $('#username').val();
		var password1 = $('#password1').val();
		var password2 = $('#password2').val();
		var email = $('#email').val();
		var firstName = $('#firstName').val();
		var lastName = $('#lastName').val();

		if (password1 == password2)
		{
			$('#passwordCheck').html("");
			createUser(username, password1, email, firstName, lastName, function(response)
			{
				var json = JSON.parse(response);
				if (json.jsontype == "user")
				{
					userLogin(json.username, json.password, function(u)
					{
						var user = JSON.parse(u);
						//set sessionId
						setCookie("s", user.sessionId, 1, "/");
						//LOGIN redirect to Homepage
						window.location = "gameboard.jsp";//todo
					});

				}
				else
				{
					alert(response);
				}

			});
		}
		else
			$('#passwordCheck').html("Passwords don't match!!!");
	}

	function checkAvailability(e)
	{
		//wait for username to be set
		setTimeout(function()
		{
		}, 500);
		var username = $("#username").val();

		doesUserExist(username, function(response)
		{
			var json = JSON.parse(response);
			if (json.jsontype == "userExists" && json.exists)
			{
				$("#userAvailability").html(username + " is already taken.");
				$('#createButton').attr("disabled", true);
			}
			else
			{
				$("#userAvailability").html("");
				$('#createButton').attr("disabled", false);

			}
		});
	}
	$(document).ready(function(){
		$('#username').focus();
	});
</script>

</html>