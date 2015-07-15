<link rel="stylesheet" href="../css/header.css">
<script src="../scripts/session.js"></script>
<script src="../scripts/jquery-2.1.1.js"></script>
<br>
<div id="headerContainer">
	<div id="headerRow">
		<div id="headerHomePlay">
			<a href="home.jsp">Home</a>
		</div>
		<div>
			<a id="headerCreateGame" href="about.jsp">About</a>
		</div>
		<div>
			<a href="https://www.wizards.com/avalonhill/rules/diplomacy.pdf">Rules</a>
		</div>
		<div>
			<a href="profile.jsp">Profile</a>
		</div>
		<div>
			<a href="friends.jsp">Friends</a>
		</div>
		<div id="headerLoginHTML">
			<a href="login.jsp">Login</a>
		</div>
	</div>
</div>
<br>
<script>
	$(document).ready(checkCurrentUser());

	function checkCurrentUser()
	{
		getCurrentUser(function(jsonStr)
		{
			var obj = JSON.parse(jsonStr);
			if (obj.jsontype == "user")
			{
				$('#headerHomePlay').html('<a href="gameboard.jsp">Play</a>');
				$('#headerCreateGame').html('<a href="gameCreation.jsp">Create Game</a>');
				$('#headerLoginHTML').html('<a onclick="headerLogout();" href="#">Logout (' + obj.username + ')</a>');
				try
				{
					curUserCountry = obj.country;
				}
				catch (err)
				{
					/*maynotbedefined*/
				}
			}
		});
	}

	function headerLogout()
	{
		//this calls the userLogout in session.js		
		userLogout(function(msg)
		{
			window.location = getBaseURL() + "/DiplomaCY/pages/home.jsp";
		});
	}
</script>