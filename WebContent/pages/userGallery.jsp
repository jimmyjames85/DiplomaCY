<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DiplomaCY - Gallery</title>
</head>

<body onLoad="loadUserGames()">
	<!-- style="margin: 0px; padding: 0px;"-->
	<div id="header">
		<%@ include file="header.jsp"%>
	</div>

	<div id="gameList">loading...</div>

	<script>
		function loadUserGames()
		{
			getCurrentUser(function(jsonUser)
			{

				user = JSON.parse(jsonUser);
				if (user.jsontype == "user")
				{
					var arr = user.gameIds;
					
					var htmlOut;
					
					if (!arr || arr.length == 0)
					{
						var loc = getBaseURL() + "/DiplomaCY/pages/gameCreation.jsp";
						htmlOut = "You have not joined any games. Click <a href='"+loc+"'>here</a> to create a game!";
					}
					else
					{

						 htmlOut = "<table><tr><td>Game ID</td><td>Users</td><td></td></tr>"
						for (var i = 0; i < arr.length; i++)
							htmlOut += "<tr><td>" + arr[i] + "</td><td></td><td><button onclick='playGame(" + arr[i] + ");'>Play</button></td></tr>";

						htmlOut += "</table>";
					}

					$("#gameList").html(htmlOut);
				}
				else
				{
					//send them to the main page
					window.location = getBaseURL() + DIPLOMACY_BASE;
				}
			});

		};
		
		function playGame(gameId)
		{
			alert(gameId);
		};
		
	</script>
</body>
</html>