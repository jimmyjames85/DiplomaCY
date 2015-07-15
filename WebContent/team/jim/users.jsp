<%@page import="edu.iastate.cs309.r16.diplomacy.servlets.UsersServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>

<body onload="finishDocument();">


	<style>
.textBox {
	border-style: solid;
	border-color: #000000;
	border-width: 1px;
	background-color: #999999;
	color: #FFFFFF;
}

#debug {
	background-color: black;
	color: red;
	font-size: large;
}

#cookieData {
	background-color: #888800;
	color: black;
}

#userData {
	background-color: #FF4444;
	color: black;
}
</style>

	<input type="hidden" name="sessionid" value="IE60012219">
	username:
	<input type="text" id="username" class="textBox">
	<br> password:
	<input type="password" id="password" onkeypress="keyPressed(event);"
		class="textBox">
	<div id="newPasswordField"></div>
	<br>
	<br>
	<h2>User Info</h2>
	<br> First Name:
	<input type="text" id="firstName" class="textBox">
	<br> Last Name:
	<input type="text" id="lastName" class="textBox">
	<br> E-mail:
	<input type="text" id="email" class="textBox">
	<br>
	<input type="radio" name="action" id="action1"
		value="<%=UsersServlet.ACTION_ADD%>"
		onclick="updateNewPasswordField();"> Add
	<input type="radio" name="action" id="action2"
		value="<%=UsersServlet.ACTION_GET%>"
		onclick="updateNewPasswordField();"> Get
	<input type="radio" name="action" id="action3"
		value="<%=UsersServlet.ACTION_UPDATE%>"
		onclick="updateNewPasswordField();"> Update
	<input type="radio" name="action" id="action4"
		value="<%=UsersServlet.ACTION_CHANGE_PASSWORD%>"
		onclick="updateNewPasswordField();">Change Password
	<br>
	<input type="button" value="Submit" id="button1"
		onclick="parseUserData();">
</body>
<div id="userData"></div>
<div id="debug"></div>
<div id="cookieData"></div>
<script src="session.js"></script>
<script>
	var usersServlet = "../../usersServlet";

	function keyPressed(e)
	{
		 if (typeof e == 'undefined' && window.event)
		 {
			 e = window.event; 
		 }
		 if(e.keyCode ==13)
		 {
			 document.getElementById("action2").checked=true;
			 parseUserData();
		 }
		setInnerHtml("debug", "you pressed " + e.keyCode);
	}
	function updateNewPasswordField()
	{
		if(document.getElementById("action4").checked)
			setInnerHtml("newPasswordField", "New Password: <input type=\"password\" id=\"newPassword\">");
		else
			setInnerHtml("newPasswordField", "");
	}
	
	function finishDocument()
	{

		var data = "";
		
		var cookie = getCookie("username");
		if (cookie != "")
			data += cookie + "<br>";
		else
			data += "No username cookie<br>";

		cookie = getCookie("password");
		if (cookie != "")
			data += cookie + "<br>";
		else
			data += "No password cookie<br>";

			data += "<br>" + document.cookie;
		setInnerHtml("cookieData", "<h3>CookieData</h3><br>" + data);
	}

	function parseUserData()
	{

		var username = document.getElementById("username").value;
		var password = document.getElementById("password").value;

		var firstName = document.getElementById("firstName").value;
		var lastName = document.getElementById("lastName").value;
		var email = document.getElementById("email").value;
		var actions = document.getElementsByName("action");


		var action;
		for (var i = 0; i < actions.length; i++)
		{
			var elem = document.getElementById(actions[i].id);
			if (elem.checked)
				action = elem.value;
		}

		if (action == "<%=UsersServlet.ACTION_ADD%>")
			addUser(username, password, firstName, lastName, email);
		else if (action == "<%=UsersServlet.ACTION_GET%>")
			getUser(username, password);
		else if (action == "<%=UsersServlet.ACTION_UPDATE%>")
			updateUser(username, password, firstName, lastName, email);
		else if (action == "<%=UsersServlet.ACTION_CHANGE_PASSWORD%>")
		{
			var newpassword = document.getElementById("newPassword").value;
			changePassword(username, password, newpassword)
		}
	}

	function ajaxCallback(response)
	{
		var out = "callback: <br>";
		try
		{
			var json = JSON.parse(response);
			
			if(json.jsontype == "user")
			{
				out += json.username +"<br>";
				out += json.password +"<br>";
				out += json.firstName +"<br>";
				out += json.lastName +"<br>";
				out += json.email +"<br>";
			}
			else if(json.jsontype == "error")
			{
				out += "Error Encountered:<br>Error Message:<br>" + json.message;
			}
			
		}
		catch(err)
		{
			
			out += response + "<br>" + err;			
		}
		
		setInnerHtml("userData", out);

		
		
	}
	function changePassword(username, oldPassword, newPassword)
	{
		var requestArr = [ "action","<%=UsersServlet.ACTION_CHANGE_PASSWORD%>", "username", username, "password", oldPassword, "newPassword", newPassword ];
		var request = createUrlencodedRequest(requestArr);
		sendAjaxQuery("POST", usersServlet, request, ajaxCallback);
		setInnerHtml("userData", "changing password");
	}
	function updateUser(username, password, firstName, lastName, email)
	{
		var requestArr = [ "action", "UPDATE", "username", username, "password", password, "email", email, "firstName", firstName, "lastName", lastName ]
		var request = createUrlencodedRequest(requestArr);
		sendAjaxQuery("POST", usersServlet, request, ajaxCallback);
		xmlhttp.send(request);
	}
	function getUser(username, password)
	{
		var requestArr = [ "action", "GET", "username", username, "password", password ];
		var request = createUrlencodedRequest(requestArr);
		sendAjaxQuery("POST", usersServlet, request, ajaxCallback);
		setInnerHtml("userData", "Request for GET user '" + username + "'sent...");
	}

	function addUser(username, password, firstName, lastName, email)
	{
		requestArr = [ "action", "ADD", "username", username, "password", password, "email", email, "firstName", firstName, "lastName", lastName ];
		var request = createUrlencodedRequest(requestArr);
		sendAjaxQuery("POST", usersServlet, request, ajaxCallback);
		setInnerHtml("userData", "query sent...");
	}
	function debug(msg)
	{
		document.getElementById("debug").innerHTML += "" + msg + "<br>";
	}
</script>
</html>