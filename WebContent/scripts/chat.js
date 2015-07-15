// servlet information for CHAT
var CHAT_SERVLET = "/DiplomaCY/chatServlet";
var ACTION_GET_ROOMS = "GETRM";
var ACTION_SEND_MESSAGE = "SENDM";
var ACTION_KEY = "action";

function getRooms(username, callback)
{
	var request = new Object();
	request.username = username;
	request.action = ACTION_GET_ROOMS;
	request.username = username;
	sendAjaxQuery("POST", getChatServlet(), createUrlEncodedRequest(request), callback);
}

function getChatServlet()
{
	return getBaseURL() + CHAT_SERVLET;
}

function sendChatMessage(username, usersArr, message, callback)
{
	var request = new Object();

	request.username = username;
	request.action = ACTION_SEND_MESSAGE;
	request.username = username;
	request.message = message;

	var req = createUrlEncodedRequest(request);

	for (var i = 0; i < usersArr.length; i++)
		req += "&user=" + usersArr[i]
	sendAjaxQuery("POST", getChatServlet(), req, callback);
}

function formatTime(millis)
{

	var d = new Date(millis);
	var ret = "";
	ret = d.getMonth() + "/" + d.getDay() + "/";
	ret += d.getFullYear().toString().substring(2, 4);
	ret = " [" + ret + " " + d.getHours() + ":" + d.getMinutes() + "]";
	return ret;
}