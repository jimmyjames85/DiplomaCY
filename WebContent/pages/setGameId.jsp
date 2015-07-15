<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../css/main.css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>setGameId</title>
</head>
<script src="../scripts/session.js"></script>
<script src="../scripts/jquery-2.1.1.js"></script>

<body>

	GameId:
	<input type="text" id="username">
	<input type="button" value="setGameId" onclick="doSet();">
</body>

<script>
	function doSet()
	{
		var gameId = $("#gameId").val();
		//set gameId
		setCookie("gameId", gameId, 1, "/");
	}
	
	$(document).ready(function(){
		$('#gameId').focus();
	});
</script>
</html>