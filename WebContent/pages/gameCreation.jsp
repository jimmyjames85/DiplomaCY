<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../css/main.css">
<title>Game Creation</title>
</head>
<body>
	<div id="header">
		<%@ include file="header.jsp"%> 
	</div><br>
	<div id="gameCreation">
		<br>Map Type<br>
		<select id="mapType" name="mapType">
		  <option value="EUROPE">Diplomacy Europe</option>
		  <option value="NORTHAMERICA">Diplomacy North America</option>
		</select><br>
		<br>Number of Players<br>
		<select id="numPlayers" name="numPlayers">
		  <option value="2">2 Players</option>
		  <option value="3">3 Players</option>
		  <option value="4">4 Players</option>
		  <option value="5">5 Players</option>
		  <option value="6">6 Players</option>
		  <option value="7">7 Players</option>
		</select><br>
		<br>Country Select<br>
		<div id="countrySelect">
		</div>
		<br>Turn Length<br>
		<select id="turnLength" name="turnLength">
		  <option value="15">15 Min</option>
		  <option value="30">30 Min</option>
		  <option value="60">1 Hour</option>
		  <option value="-1">Evaluate at Midnight</option>  
		</select><br>
		<br>Grace Length<br>
		<select id="graceLength" name="graceLength">
		  <option value="15">15 Min</option>
		  <option value="30">30 Min</option>
		  <option value="60">1 Hour</option>
		  <option value="120">2 Hours</option>
		  <option value="1440">1 Day</option>  
		</select><br><br><br>
		<input type="button"  onclick="postNewGame()" value="CreateGame" />
	</div>
</body>
<script>
/**
 * Load the intial game creation based on the following preselected values:
	 numPlayers = 2;
	 turnLength = 15;
	 graceLength = 15;
	 mapType = Europe;
 */
var arr=["ENG", "FRA", "AUS", "ITA", "GER", "RUS", "TUR"];
var input;
var numPlayers = 2;
var mapType = "Europe";
$(document).ready(function() {
	  input = document.getElementById("countrySelect");
  	  for(var a=0; a<numPlayers; a++){
	  	  input.innerHTML += "Input Player";
	  	  var newSelect = $("<select id=\"selectId" + a + "\" name=\"selectName\" />");
		  for(var b = 0; b<arr.length; b++) {
			  if(b == a){
				 var selectOption = $("<option />", {value: arr[b], text: arr[b]});
				 selectOption.attr('selected','selected');
				 selectOption.appendTo(newSelect);
			  }
			  else{
				  $("<option />", {value: arr[b], text: arr[b]}).appendTo(newSelect);
			  }
		  }
		  newSelect.appendTo(input);
	  	  var newInput = document.createElement("input");
	  	  newInput.type="text";
	  	  newInput.name="player" + a;
	  	  newInput.id= "player" + a;
	  	  input.appendChild(newInput);
	  	  input.innerHTML += "<br>";
	  }
/**
 * On Map change, (i.e. Europe to North America or vice versa) countries and players are changed to accomodate
 new map countries and sizes.
 Ex: North America can support up to 10 players, and has 10 new countries
 */
	  $('#mapType').change(function() {
		mapType = $('#mapType').val();
		input.innerHTML = "";
	  	numPlayers=$('#numPlayers').val();
	  	if(mapType == "EUROPE"){
	  		if(numPlayers > 7){
	  			numPlayers = 7;
	  		}
	  		arr = ["ENG", "FRA", "AUS", "ITA", "GER", "RUS", "TUR"];
	  		document.getElementById("numPlayers").innerHTML = "";
	  		for(var b = 2; b<=arr.length; b++) {
			  	  $("<option />", {value: b, text: b + " Players"}).appendTo($("#numPlayers"));
			}
	  	}
	  	else if(mapType == "NORTHAMERICA"){
	  		arr = ["BRC", "CAL", "MEX", "FLO", "HTL", "NEW", "QUE", "PER", "TEX", "CUB"];
	  		document.getElementById("numPlayers").innerHTML = "";
	  		for(var b = 2; b<=arr.length; b++) {
			  	  $("<option />", {value: b, text: b + " Players"}).appendTo($("#numPlayers"));
			}
	  	}
	  	for(var a=0; a<numPlayers; a++){
	  	  input.innerHTML += "Input Player";// + arr[a] + " Player";
	  	  selectId = "selectId" + a;
	  	  var newSelect = $("<select id=\"" + selectId + "\" name=\"selectName\" />");
		  for(var b = 0; b<arr.length; b++) {
			  if(b == a){
				 var selectOption = $("<option />", {value: arr[b], text: arr[b]});
				 selectOption.attr('selected','selected');
				 selectOption.appendTo(newSelect);
			  }
			  else{
				  $("<option />", {value: arr[b], text: arr[b]}).appendTo(newSelect);
			  }
		  }
		  newSelect.appendTo(input);
	  	  var newInput = document.createElement("input");
	  	  newInput.type="text";
	  	  newInput.name="player" + a;
	  	  newInput.id= "player" + a;
	  	  input.appendChild(newInput);
	  	  input.innerHTML += "<br>";
	  	}
	  });
/**
 * On change of number of players, the adequate number of select options are generated
 */
	  $('#numPlayers').change(function() {
		input.innerHTML = "";
	  	numPlayers=$('#numPlayers').val();
	  	if(mapType == "EUROPE"){
	  		arr = ["ENG", "FRA", "AUS", "ITA", "GER", "RUS", "TUR"];
	  	}
	  	else if(mapType == "NORTHAMERICA"){
	  		arr = ["BRC", "CAL", "MEX", "FLO", "HTL", "NEW", "QUE", "PER", "TEX", "CUB"];
	  	}
	  	for(var a=0; a<numPlayers; a++){
	  	  input.innerHTML += "Input Player";// + arr[a] + " Player";
	  	  selectId = "selectId" + a;
	  	  var newSelect = $("<select id=\"" + selectId + "\" name=\"selectName\" />");
		  for(var b = 0; b<arr.length; b++) {
			  if(b == a){
				 var selectOption = $("<option />", {value: arr[b], text: arr[b]});
				 selectOption.attr('selected','selected');
				 selectOption.appendTo(newSelect);
			  }
			  else{
				  $("<option />", {value: arr[b], text: arr[b]}).appendTo(newSelect);
			  }
		  }
		  newSelect.appendTo(input);
	  	  var newInput = document.createElement("input");
	  	  newInput.type="text";
	  	  newInput.name="player" + a;
	  	  newInput.id= "player" + a;
	  	  input.appendChild(newInput);
	  	  input.innerHTML += "<br>";
	  	}
	  });
});
/**
Validates players and countries to make sure they are unique
*/
function validate(pArray, cArray, headers){
	for(var a = 0; a < headers.numPlayers; a++){
		for(var b = 0; b < headers.numPlayers; b++){
			if(a != b){
				console.debug("player A: " + pArray[a] + " Player B: " + pArray[b]);
			}
			console.debug(pArray[0] + " " + cArray[0]);
			if(pArray[a]==pArray[b] && a != b){
				return false;
			}
			if(cArray[a]==cArray[b] && a != b){
				return false;
			}
		}
	}
	return true;
}
/**
 * Posts a new game by writing the game and gamestate to the database.
 Values written to the database are based on the values of the various game creation selects
 */
function postNewGame(){
	console.debug("in post new game");
	var headers = new Object();
	headers.sessionId = getCookie("s");
	headers.numPlayers = $('#numPlayers').val();
	headers.turnLength = $('#turnLength').val();
	headers.mapType = $('#mapType').val();
	headers.graceLength = $('#graceLength').val();
	console.debug("turn length = " + headers.turnLength);
	console.debug("grace length = " + headers.graceLength);
	var countriesArr = {};
	var playersArr = {};
	var countriesStr = "";
	var playersStr = "";
	console.debug(headers.numPlayers + " " +  headers.turnLength + " " + headers.mapType + " " + headers.graceLength);
	/**
	Generates the array of players and countries they are playing as
	*/
  	for(var a=0; a<headers.numPlayers; a++){
  		var countryId = "selectId" + a;
  		var playerId = "player" + a;
  		console.debug("player Id= " + playerId);
  		playersArr[a] = $("#" + playerId).val();
  		countriesArr[a] = $("#" + countryId).val();
  		console.debug("player username: " + playersArr[a]);
  		console.debug("player Country: " + countriesArr[a]);
  	}
	/**
	Converts the array of Players and Countries to a comma delimited string 
	*/
  	for(var a=0; a<headers.numPlayers; a++){
  		countriesStr += countriesArr[a];
  		playersStr += playersArr[a];
  		if(a < headers.numPlayers-1){
  			countriesStr += ",";
  			playersStr += ",";
  		}
  	}
  	headers.countries = countriesStr;
  	headers.players = playersStr;
  	
  	console.debug(playersArr[0] + " " + countriesArr[0]);
  	if(validate(playersArr, countriesArr, headers) == true){
  		sendAjaxQueryWithHeaders("post", getBaseURL() + GAME_CREATION_SERVLET, "", headers, redirect);
  	}
  	else{
  		alert("Players and selected countries must be unique. Once players and countries are unique please try again.");
  	}
}
function redirect(){
	window.location.href = "gameboard.jsp";
}
</script>
</html>