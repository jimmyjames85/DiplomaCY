<%@page import="edu.iastate.cs309.r16.diplomacy.servlets.UsersServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<style>
#batman{
position: absolute;
left: 450px;
top: 50px;
float: left;

}

#batman2{
position: relative;
left: 0px;
top: 50px;
width: 20%;
float: left;

</style>
<body onload="finishDocument();">
	<div id="cookieData"></div>
	<div id="clickMe">ClickMe</div>
	<img id="batman" src="sharkRepellantBatSpray.jpg">
		<img id="batman2" src="sharkRepellantBatSpray.jpg">
</body>
<script src="jquery-2.1.1.js"></script>
<script src="session.js"></script>
<script>
var usersServlet = "../usersServlet";


	function finishDocument()
	{
		loadUserInfo(function(user){
			var username = user.username;
			var firstName = user.firstName;
			var sessionId = user.sessionId;
			
			if(username=="")
			{
				out = "Please login <a href=\"login.jsp\">here</a>";
			}
			else
			{
				out = "Hello " + firstName +",<br>Your username='" + username + "' and we are ready to play!<br>";
				out += "SID: " + sessionId +"<br>";
				out += "<br><input type=\"button\" value=\"Log_out\" onclick=\"logout();\">";
				out += "<br><a href=\"users.jsp\">Click here</a> and see how cookies persist.";
			}
			setInnerHtml("cookieData",out);
			
		});
	}


	function logout()
	{
		var username = getCookie("username");
		setInnerHtml("cookieData", username);
		
		var requestArr = [ "<%=UsersServlet.ACTION%>", "<%=UsersServlet.ACTION_LOGOUT%>","<%=UsersServlet.USERNAME%>", username ];
		var request = createUrlencodedRequest(requestArr);
		sendAjaxQuery("POST", usersServlet, request, function(response)
		{
		});//empty callback

		//delete cookie
		setCookie("username", "", -1, "/");
		setCookie("firstName", "", -1, "/");
		setCookie("s", "", -1, "/");
		window.location = "homePage.jsp";//reload
	}

	


	//JQUERY!!
	
	//global variable
	var x = 1;
	var left = 0;
	
	$(document).ready(function()
	{


		$("#clickMe").click(function()
		{
			if (x == 1)
			{
				$("#batman").slideUp(10);
				$("#batman2").animate({left:left + "px"},10);
				x = 0;
				left += 200;
				if(left > 1080)
					left = 0;
			}
			else if (x == 0)
			{
				$("#batman").fadeIn(1000);
				x = 1;
			}
		});

	});
</script>
</html>