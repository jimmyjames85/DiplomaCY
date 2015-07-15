<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friends</title>
</head>
<body>
	<div id="header">
		<%@ include file="header.jsp"%>
	</div>


	<div id="chatTabs"></div>
	<div id="chatText" style="height: 500px; overflow-y: scroll;"></div>
	<div id="chatControls">
		<input type="text" id="tbMessage">
		<button id="btnSend" type=button>Send</button>
	</div>
	<div id="DEBUG"></div>

</body>
<script src="../scripts/chat.js"></script>

<script>
	var theUser;
	var curChatRoom;
	var pollChatRoom;

	function sendMessage(msg, callback)
	{
		//append message
		//var curTmpChat = $('#tmpChatMessageArea').html();
		//$('#tmpChatMessageArea').html(curTmpChat + "<br>" + msg);

		//send
		var usersArr = curChatRoom.split(";");
		sendChatMessage(theUser.username, usersArr, msg, function(cb)
		{
			callback(cb);
			sendAjaxQuery("POST", getChatServlet(), parms, setMessageAreaText);

			/*
			 if (cb && !(cb === "empty"))
			 {
			 try
			 {
			 var err = JSON.parse(cb);
			 alert(err.message);
			 //this is to reset for gamemanger commands
			 $("#tmpChatMessageArea").html("");
			 $("#tbMessage").val("");
			 $("#btnSend").prop("disabled", false);
			 $("#tbMessage").attr("disabled", false);
			 }
			 catch (err)
			 {
			 //This err is from parsing
			 //cb is 'empty' or is empty or is "" I dunno
			 //but I can't catch it
			 //we need this here
			 openChatRoom(curChatRoom)
			 }
			 }
			 else
			 openChatRoom(curChatRoom);*/
		});
	}

	function setMessageAreaText(txt)
	{
		//setInnerHtml('DEBUG', "setMessageAreaText=" + txt);
		$('#chatText').html(txt);

		var log = JSON.parse(txt);

		if (log.jsontype == "chatlog")
		{
			$('#chatControls').show();
			var msgArr = log.log;

			var out = "<br><b>Chat Room Users:</b> " + curChatRoom.toLowerCase().replace(/;/g, " , ");

			out += "<br><br><table>";
			out += "<div id=\"chatTable\">";
			var lastUser = "";

			for (var i = 0; i < msgArr.length; i++)
			{
				var curMsg = msgArr[i];

				out += "<tr>";

				var sender = curMsg.sender;
				if (sender == lastUser)
					sender = "";
				else
					lastUser = sender;

				out += "<td>" + sender + "</td><td></td><td></td><td></td></tr><tr><td></td>";

				if (curMsg.sender == theUser.username)
					out += "<td>" + curMsg.message + "</td><td></td>";
				else
					out += "<td></td><td>" + curMsg.message + "</td>";
				out += "<td>" + formatTime(curMsg.timestamp) + "</td>";
				out += "</tr>";
			}
			out += "<div id=\"chatTable\">";
			out += "</table>";
			out += "<div id=\"tmpChatMessageArea\"></div>"

			//$("textarea#test").val(replace($("textarea#test").val(), "<br>", "&#10;")));

			/*out += "<input type=\"text\" id=\"tbMessage\">";

			//currentUser.firstName = $('#tbFirstName').val();
			out += "<button id=\"btnSend\" type=\"button\">send</button>";*/

			$('#chatText').html(out);

			$("#btnSend").click(function()
			{
				sendMessage($("#tbMessage").val());
				$("#tbMessage").val("");
				//sendAjaxQuery("POST", getChatServlet(), parms, setMessageAreaText);
				//$("#btnSend").prop("disabled", true);
				//$("#tbMessage").attr("disabled", "disabled");

			});

			$("#tbMessage").keypress(function(e)
			{
				if (e.which == 13)
				{
					sendMessage($("#tbMessage").val());
					$("#tbMessage").val("");
					//sendAjaxQuery("POST", getChatServlet(), parms, setMessageAreaText);
					//$("#btnSend").prop("disabled", true);
					//$("#tbMessage").attr("disabled", "disabled");
				}
			});

			//$("#btnSend").prop("disabled", false);
			//$("#tbMessage").attr("disabled", false);

			$("#tbMessage").focus();
		} else
			$('#chatText').html(txt);

		$('#chatText').scrollTop(1E10);//scroll to bottom
	}

	function initiateChat(chatRoomName)
	{
		curChatRoom = chatRoomName;
		$('#chatText').html("initiating...");
		sendMessage("I'd like to chat with you!", function(cb)
		{
			loadChatRooms();
			$('#chatText').html("Chat Initialized!!<br>Open new chat room above.");
		});
	}

	function startNewChat()
	{
		$('#chatControls').hide();
		var out = "";
		out += "Enter user(s) seperated by a semi-colon (;)<br>";
		out += "<input type=\"text\" id=\"tbUserList\">";
		out += "<button type=\"button\" id=\"btnNewChat\" onClick='initiateChat($(\"#tbUserList\").val())'>Start New Chat</button>";
		$('#chatText').html(out);
		//currentUser.firstName = $('#tbFirstName').val();
	}

	function openChatRoom(chatRoomName)
	{
		window.clearInterval(pollChatRoom);
		$("#chatText").html("loading...");

		if (chatRoomName == "startNewChat" && curChatRoom != "startNewChat")
		{
			startNewChat();
		} else
		{
			loadChatRooms();
			curChatRoom = chatRoomName;
			var parms = "action=GETL&username=" + theUser.username;
			var arr = chatRoomName.split(";");
			for (var i = 0; i < arr.length; i++)
				parms += "&user=" + arr[i];

			sendAjaxQuery("POST", getChatServlet(), parms, setMessageAreaText);

			pollChatRoom = setInterval(function()
			{
				sendAjaxQuery("POST", getChatServlet(), parms, setMessageAreaText);
			}, 5000);
		}
	}

	function defineChatOption(chatRoomName)
	{
		var caption = chatRoomName;

		var arr = chatRoomName.split(";");
		if (arr.length == 2)
		{
			caption = arr[0];
			if (caption.toUpperCase() == theUser.username.toUpperCase())
				caption = arr[1];
		}

		//var html = "<button type=\"button\" onClick='openChatRoom(\""
		//+ chatRoomName + "\")'>" + caption + "</button>";
		var html = "<option value=\"" + chatRoomName+"\">" + caption + "</option>";
		return html;
	}

	function drawChatRooms(arr)
	{
		var out = "<select id=\"chatList\" onchange=\"openChatRoom(this.value);\">";

		for (var i = 0; i < arr.length; i++)
		{
			out += defineChatOption(arr[i]);
		}
		out += "<option value=\"startNewChat\">Start New Chat</option>"
		out += "<option value=\"GM\">Game Manager</option>"
		out += "<option>Select Chat Room</option>"
		out += "</select>";
		out += "<br>";
		$('#chatTabs').html(out);
		$('#chatList').val("Select Chat Room");
	}

	function loadChatRooms()
	{
		getRooms(theUser.username, function(user)
		{
			var chatRoomArr;
			var obj = JSON.parse(user);
			if (obj.jsontype == "chatRoomList")
			{
				chatRoomArr = obj.chatRooms;
				drawChatRooms(chatRoomArr);
			} else
				alert("Bad object type: " + describeObject(obj));
		});
	}

	$(document).ready(function()
	{
		$('#chatTabs').html("loading chatRooms...");
		$('#chatControls').hide();
		getCurrentUser(function(json)
		{
			var user = JSON.parse(json);
			if (user.jsontype == "user")
			{
				theUser = user;
				loadChatRooms();
			} else
				setMessageAreaText(json);
		});
	});
</script>

</html>
