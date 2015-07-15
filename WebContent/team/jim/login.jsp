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
<div id="header"><a href="http://stackoverflow.com/questions/10788872/insert-image-dynamically-with-jquery">http://stackoverflow.com/questions/10788872/insert-image-dynamically-with-jquery</a></div>
	Username:
	<input type="text" id="username">
	<br> Password:
	<input type="password" id="password">
	<br>
	<input type="button" value="Login" onclick="doLogin();">
	<br>
	<div id="output"></div>
	<input type="button" value="ImageThingy" onclick="doImagePlacement();">
	<input type="button" value="ImageIds" onclick="listImageIds();">
</body>
<script src="session.js"></script>
<script src="jquery-2.1.1.js"></script>
<script>

	var usersServlet = "../../usersServlet";

	function callBack(response)
	{
		try
		{
			var json = JSON.parse(response);
			var s = "";
			var keys = [];
			for(var key in json)
		   	{
		    	keys.push(key);
			}

			for(var i=0;i<keys.length;i++)
			{
				s += keys[i] + "=" + json[keys[i]] +"\n"; 
			}
			alert(s);

			if(json.jsontype == "user")
			{
				startSession(json);
			}
			else if(json.jsontype == "error")
			{
				out = "Error Encountered:<br>Error Message:'" + json.message +"'<br>";

				out+= "<pre>" + json.stackTrace + "</pre>";
				
				setInnerHtml("output",out);
			}
		}
		catch(err)
		{
			setInnerHtml("output","Unexpected Server Response:<br>" + response + "<br>Error:<br>" + err);
		}
	}
	
	function finishDocument()
	{
		loadUserInfo(function(user){

			var out ="";
			if(user.username!="")
			{
				out =  user.username + " Hello and WELCOME back " + user.firstName + "!";
				out += "<br>you can login again and reset your sesion id or click <a href=\'homePage.jsp\'>here</a>!"; 				
				setInnerHtml("output",out);				
			}

		});
	}
	
	function doLogin()
	{
		var username = document.getElementById("username").value;
		var password = document.getElementById("password").value;
		var requestArr = [ "<%=UsersServlet.ACTION%>", "<%=UsersServlet.ACTION_LOGIN%>","<%=UsersServlet.USERNAME%>", username,"<%=UsersServlet.PASSWORD%>", password];
		var request = createUrlencodedRequest(requestArr);
		sendAjaxQuery("POST", usersServlet, request, callBack);
	}

	function startSession(user)
	{
		//LOGIN redirect to Homepage
		setCookie("s", user.sessionId, 1, "/");
		window.location = "homePage.jsp";
	}
	
	var x = 0;
	var y=50;
	function doImagePlacement()
	{
		x+=150;
		y+=50;
		var yourImg = $('<img/>', {
           // "class" :"inline", // or className: "inline"
             src:"../../images/army_austria.png",
             style:"position: absolute; left: " + x + "px; top: " + y + "px",
             onclick:"alert(\"hello world\");",
             id:"myImg"+x
          });
		$("#header").prepend(yourImg);
		
	}
	
	function listImageIds()
	{
		var s="";
		var obj=$('#myImg300');
		
		obj.attr('onclick','alert("Im speaciul!!!")');
		obj.attr('style',"position: absolute; left:600px;");
		for(var key in  obj)
		{
			s+=key + " ";
		}
		alert(obj.attr('id'));
	}
</script>
</html>