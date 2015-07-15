<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="stylesheet" href="../css/main.css">
<link rel="stylesheet" href="../css/home.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
</head>
<style>
</style>
<body>
	<div id="header">
		<%@ include file="header.jsp"%>
	</div>
	<br>
	<div class="jumbotron"><!-- img src='http://www.wizards.com/global/images/ah_prod_diplomacy_pic3_en.jpg'--></div>
	<br>
	<div id="play"><a class="playButton" href="login.jsp">PLAY</a></div>
</body>

<script>
	$(document).ready(function()
	{
		getCurrentUser(function(response){
			var json = JSON.parse(response);
			if(json.jsontype=='user')
			{
				//if user is logged in change the button to goto the gameboard 
				$('#play').html('<a class="playButton" href="gameboard.jsp">PLAY</a>');
			}
				
		});
	});
</script>
</html>
