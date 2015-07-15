<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="../css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DiplomaCY - Gameboard</title>
</head>

<body onLoad="initGameboard()">
	<!-- style="margin: 0px; padding: 0px;"-->
	<div id="header">
		<%@ include file="header.jsp"%>
	</div>
	<div id="gameList">
		<center>
			<select id="gameSelector"></select>
		</center>
	</div>
	<div id="canvasLocation"></div>
	<script src="../scripts/GList.js"></script>
	<script src="../scripts/OriginalDiplomacyMap.js"></script>
	<script src="../scripts/NorthAmericaDiplomacyMap.js"></script>
	<script src="../scripts/GButton.js"></script>
	<script src="../scripts/GArrow.js"></script>
	<script src="../scripts/GUnit.js"></script>
	<script src="../scripts/GameBoard.js"></script>
	<script>
		var curGameId;

		//spring/fall order writing phase etc.
		var curGameState;

		//diplomacyMap definition (original map)
		var diplomacyMap;

		//gameboard
		var gameBoard;

		// user logged in 
		var user;

		//current map state (downloaded from the server)
		var map;

		//list of users in the game;
		var userList;

		//indicates if the user has made orders but not submitted them. 
		var needToSubmit = false;

		//true if the current user has displaced units
		var displacedUnits = false;

		function clearCanvasLocation()
		{
			var div = document.getElementById("canvasLocation");
			while (div.firstChild)
				div.removeChild(div.firstChild);
		}

		function clearOrderList()
		{
			while (orderList.length > 0)
				orderList.splice(0, 1);
		};
		
		
		var pollEnabled = true;
	
		function poll()
		{
			if(pollEnabled)
			{
				getCurrentGameState(curGameId, function(msg)
				{
					try
					{
						/*
						
							var obj = JSON.parse(msg);
						curGameState = obj.stateNumber;
						boardType = obj.boardType;
						nextTrigger = -1;
						if(obj.nextTrigger)
							nextTrigger = obj.nextTrigger;
						*/
						var obj = JSON.parse(msg);
						if(curGameState != obj.stateNumber)
							reloadGame();
					}
					catch (err)
					{
						curGameState = -1;
						poll=false;
					}
				});	
				
				getSubmitStatus(curGameId, function(msg)
				{
					var obj = JSON.parse(msg);
					
					submitStatus = new GList();
					//submitStatus.setComparator(function(a,b){return a.localeCompare(b);});
					for(var i=0;i<obj.countries.length;i++)
						submitStatus.add(obj.countries[i]);

					addUserListButtons(submitStatus);
					gameBoard.redraw();
				});
			}
		}
		var submitStatus;
		var nextTrigger;

		function loadGameboard(gameId, callback)
		{
			if (gameBoard)
			{
				clearOrderList();
				//for some reason buttons are persisting when gameId changes...
				resetClickOrderState();
				//TODO i don't why have to do this

				var curCanvas = gameBoard.canvas;
				var div = document.getElementById("canvasLocation");
				div.removeChild(curCanvas);
				delete curCanvas;
				delete gameBoard.buttonList;
				delete gameBoard;

			}

			getGameMap(gameId, function(jsonMap)
			{
				map = JSON.parse(jsonMap);

				if (diplomacyMap)
					delete diplomacyMap;

				curGameState = -1;

				getCurrentGameState(gameId, function(msg)
				{
					try
					{
						var obj = JSON.parse(msg);
						curGameState = obj.stateNumber;
						boardType = obj.boardType;
						nextTrigger = -1;
						if(obj.nextTrigger)
							nextTrigger = obj.nextTrigger;
					}
					catch (err)
					{
						curGameState = -1;
					}
					
					var load =function()
					{ 	//create gameBoard
						gameBoard = new GameBoard(diplomacyMap, 1, 1);
						gameBoard.setDisplaySupplyCenters(true);
						gameBoard.setDisplayUnits(true);
						getUserList(gameId, function(list)
						{
							userList = JSON.parse(list);
							callback();
						});
					};
					
					if(boardType.toUpperCase()=="EUROPE")
						diplomacyMap = new OriginalDiplomacyMap(load);
					else if(boardType.toUpperCase()=="NORTHAMERICA")
						diplomacyMap = new NorthAmericaDiplomacyMap(load);

				});

			});
		}

		function setupGameBoard()
		{
			//create gameBoard
			gameBoard = new GameBoard(diplomacyMap, 1, 1);
			gameBoard.setDisplaySupplyCenters(true);
			gameBoard.setDisplayUnits(true);
			getUserList(gameId, function(list)
			{
				userList = JSON.parse(list);
				callback();
			});
		}
		
		function populateGameSelector()
		{
			var desiredIndex = getCookie("selectedGame");
			var gameSelector = document.getElementById("gameSelector");

		    for(var i=gameSelector.options.length-1;i>=0;i--)
		        gameSelector.remove(i);

			gameSelector.onchange = gameSelectorChanged;

			for (var i = 0; i < user.gameIds.length; i++)
			{
				var option = document.createElement("option");
				option.text = "Game " + user.gameIds[i] + " playing as " + user["country" + user.gameIds[i]];
				gameSelector.add(option);
			}
			
			if(desiredIndex)
			{
				gameSelector.selectedIndex = desiredIndex;
				gameSelectorChanged();
				curGameId = user.gameIds[gameSelector.selectedIndex];
			}
			else
				gameSelector.selectedIndex = 0;
		}

		function gameSelectorChanged()
		{
			var gameSelector = document.getElementById("gameSelector");
			curGameId = user.gameIds[gameSelector.selectedIndex];
			loadGameboard(curGameId, documentIsReadyAndUserIsReadyAndMapIsReady);
			setCookie("selectedGame", gameSelector.selectedIndex, 3, "/");
		}

		function initGameboard()
		{
			//first get user
			getCurrentUser(function(jsonUser)
			{
				user = JSON.parse(jsonUser);
				if (user.jsontype == "user")
				{
					curGameId = user.gameIds[0];
					populateGameSelector();
					//load first game
					loadGameboard(curGameId, documentIsReadyAndUserIsReadyAndMapIsReady);
					populateGameSelector();
				}
				else
				{
					//send them to the main page
					window.location = getBaseURL() + DIPLOMACY_BASE;
				}
			});

		}

		// orderState is the state of the current user has clicked
		//0 = no clicks (reset)
		//1 = begin order, unit was clicked, order buttons drawn (waiting to cancel or click order)
		//2 = move clicked, begin move order
		//3 = support clicked, begin support order
		//4 = continue support, (unit to support has been clicked)
		//5 = support a move
		//6 = begin convoy order
		//7 = provinceClick and unitClick are called simultaneously state7 ignores the first provinceClick
		//8 = end convoy order

		var clickOrderState = 0;
		var curClickedUnit;
		var supportUnit;
		var convoyUnit;
		var beginConvoyProvince;
		
		var debugBtnArr=new GList();
		
		function debugBoard(msgArr, time)
		{
			for(btn in debugBtnArr.list)
				gameBoard.removeButton(btn);

			debugBtnArr.removeAllElements();
			
			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			for(var i=0;i<msgArr.length;i++)
			{
				var debugBtn = new GButton(msgArr[i]);
				debugBtn.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
				debugBtn.setAlpha(0.8);
				debugBtn.setBackgroundColor("yellow");
				gameBoard.addButton(debugBtn);
				debugBtnArr.add(debugBtn);
				gameBoard.redraw();	
				
				if(time)
				{
					
					window.setTimeout(function()
					{
						gameBoard.removeButton(debugBtn);
						gameBoard.redraw();
					},time);					
				}		
			}
		};
		
		function unitClicked(unit)
		{
			var uCountry = user["country" + curGameId].toUpperCase();
			var cCountry = unit.country.toUpperCase();

			if (clickOrderState == 3)
			{
				continueSupportOrder(unit);
			}
			else if (clickOrderState == 0 || clickOrderState == 1)
			{
				if (uCountry == cCountry)
				{
					var phase = curGameState % 5;

					if ((curClickedUnit) && curClickedUnit.province.toUpperCase() == unit.province.toUpperCase())
					{
						curClickedUnit = null;
						resetClickOrderState();
					}
					else if (phase == 1 || phase == 3)
					{
						if (unit.isDisplaced)
						{
							clickOrderState = 1;
							curClickedUnit = unit;
							addUnitActionButtons(curClickedUnit);
						}
						else
							debugBoard(["Only orders can be made for displaced units"],1000);
					}
					else if (phase == 0 || phase == 2)
					{
						clickOrderState = 1;
						curClickedUnit = unit;
						addUnitActionButtons(curClickedUnit);
					}
					else
					{
						var arr = ["Disband Orders can be made on the right side."];
						debugBoard(	arr,1000);
					}
				}
				else
				{
					debugBoard(["Orders can only be made for you country " + uCountry],1000);
				}

			}
			else if (clickOrderState == 6)
			{
				convoyUnit = unit;
				endConvoyOrder();
			}
			else
				debugBoard([describeObject(unit)],1000);

		};

		var btnMove;
		var btnSupport;
		var btnHold;
		var btnConvoy;
		/**
		 * displays action buttons under the active unit
		 */
		function addUnitActionButtons(unit)
		{
			gameBoard.removeButton(btnMove);
			gameBoard.removeButton(btnSupport);
			gameBoard.removeButton(btnHold);
			gameBoard.removeButton(btnConvoy);

			var x = unit.absoluteX - 5 * unit.height;
			var y = unit.absoluteY + unit.height;
			var width = 2.5 * unit.height;
			var height = unit.height;
			var bgColor = "white";
			var fgColor = "black";
			var font = "Impact 15 pt;";

			btnSupport = createGameButton("Support", x, y, width, height, bgColor, fgColor, font, beginSupportOrder);

			x += 3 * unit.height;
			btnMove = createGameButton("   Move", x, y, width, height, bgColor, fgColor, font, null);

			x += 3 * unit.height;
			btnHold = createGameButton("   Hold", x, y, width, height, bgColor, fgColor, font, null);

			if (clickOrderState == 4)
			{
				btnMove.addEventListener(endSupportMoveOrder);
				btnHold.addEventListener(appendSupportHoldOrder);
			}
			else
			{
				btnMove.addEventListener(beginMoveOrder);
				btnHold.addEventListener(appendHoldOrder);
			}

			x += 3 * unit.height;
			btnConvoy = createGameButton("Convoy", x, y, width, height, bgColor, fgColor, font, beginConvoyOrder);

			gameBoard.addButton(btnMove);
			var phase = curGameState % 5;
			if (phase == 0 || phase == 2)
			{
				gameBoard.addButton(btnHold);
				if (clickOrderState != 4)
					gameBoard.addButton(btnSupport);
				if (clickOrderState == 1 && unit.unitType == "F")
					gameBoard.addButton(btnConvoy);
			}
			gameBoard.redraw();
		}

		var orderList = [];

		
		var userListButtons = new GList();

		
		function addUserListButtons(submitStatus)
		{
		
			if(submitStatus)
			for(var i=0;i<submitStatus.size();i++)
				console.debug("'"+submitStatus.get(i)+"'");
			
			for(var i=0;i<userListButtons.size();i++)
				gameBoard.removeButton(userListButtons.get(i));
			userListButtons.removeAllElements();
			
			var width = 100;
			var height = 25;
			var pos = gameBoard.getMapPos();
			var curX = pos.x - width;
			var curY = pos.y;
			var bgColor = "Black";
			var fgColor = "White";
			var font = "Impact 16 pt";

			var supplyCenterCount = countSupplyCenters();

			for (country in userList)
			{
				
				fgColor = "White";
				if(submitStatus && submitStatus.contains(country))
					fgColor = "Green";
					
				var caption1 = "[" + userList[country] + " " + country  + " " + "]";
				var caption2 = "     "+ supplyCenterCount[country] + " supply centers"
				var button = addGameButton(caption1, curX, curY, width, height, bgColor, fgColor, font, userButtonPressed);
				curY += 25;
				button.setActionId(country + "=" + userList[country]);
				userListButtons.add(button);
				
				fgColor = "White";
				button = addGameButton(caption2, curX, curY, width, height, bgColor, fgColor, font, userButtonPressed);
				curY += 25;
				button.setActionId(country + "=" + userList[country]);
				userListButtons.add(button);
			}

			
			updateCountdown();
			
		}

		var timeButton;
		function updateCountdown()
		{
			if(timeButton)
				gameBoard.removeButton(timeButton);

			if(nextTrigger!=-1)
			{
				var curTime =((new Date).getTime()); 
				var timeDiff = nextTrigger-curTime;
				
				var days = Math.floor(timeDiff/86400000);
				timeDiff = timeDiff % 86400000;
				
				var hours = Math.floor(timeDiff/3600000);
				timeDiff = timeDiff % 3600000;
				
				var minutes = Math.floor(timeDiff/60000);
				timeDiff = timeDiff % 60000;
				
				var seconds = Math.floor(timeDiff/1000);

				var caption = twoDigit(days) +  ":" + twoDigit(hours) + ":" + twoDigit(minutes) +":" + twoDigit(seconds) ;
				
				var width = 100;
				var height = 25;
				var pos = gameBoard.getMapPos();
				var x = pos.x - width;
				var y = pos.y+gameBoard.getMapSize().height - height;
				var bgColor = "Black";
				var fgColor = "White";
				var font = "Impact 16 pt";
				timeButton = addGameButton(caption, x, y, width, height, bgColor, fgColor, font, null);
				gameBoard.redraw();
			}
		}
		
		function twoDigit(i)
		{
			var ret = "" + i;
			
			if(ret.length==1)
				ret = "0" + ret;
			
			return ret;
		}
		
		function userButtonPressed(actionId)
		{
			var pair = actionId.split("=");
			var requestedChatUser = pair[1];
			setCookie("requestedUser", pair[1], 1, "/");
			window.location = getBaseURL() + "/DiplomaCY/pages/friends.jsp";
		}

		function addOrderSideButtons()
		{
			var pos = gameBoard.getMapPos();
			var curX = pos.x + gameBoard.getMapSize().width;
			var curY = pos.y;
			var width = 300;
			var height = 25;
			var bgColor = "Black";
			var fgColor = "White";
			var font = "Impact 16 pt";
			if (curGameState >= 0)
			{
				addGameButton(getStateName(curGameState), curX, curY, width, height, bgColor, fgColor, font, getMyMoves);
				curY += height;
			}

			if (displacedUnits)
			{
				fgColor = "Red";
				addGameButton("You must resolve your displaced Units!", curX, curY, width, height, bgColor, fgColor, font);
				curY += height;
			}

			fgColor = "Green";
			if (needToSubmit)
				fgColor = "Red";

			font = "Impact 16 pt";
			for (var i = 0; i < orderList.length; i++)
			{
				addGameButton(orderList[i], curX, curY, width, height, bgColor, fgColor, font, null);
				curY += height;
			}

			bgColor = "DarkGoldenRod";
			fgColor = "DarkBlue";
			font = "Impact 16 pt";

			if (needToSubmit)
			{
				addGameButton("Submit Orders", curX, curY, width, height, bgColor, fgColor, font, submitOrders);
				curY += height;
			}
			

			var userIsOwner=false;
			for (var i = 0; i < user.ownedGameIds.length; i++)
			{
				if(curGameId==user.ownedGameIds[i])
					userIsOwner = true;
			}
			if(userIsOwner)
			{
				addGameButton("Advance Turn", curX, curY, width, height, bgColor, fgColor, font, advanceClock);
				curY += height;
			}

			if (curGameState >= 0)
			{
				addGameButton("Reload moves ", curX, curY, 300, height, bgColor, fgColor, font, getMyMoves);
				curY += height;
			}

			curY += height;
			addGameButton("Toggle Units ", curX, curY, width, height, bgColor, fgColor, font, toggleDisplayUnits);

			curY += height;
			addGameButton("Toggle Supply Centers ", curX, curY, width, height, bgColor, fgColor, font, toggleSupplyCenters);
			curY += height;

			addGameButton("Toggle Move Arrows ", curX, curY, width, height, bgColor, fgColor, font, toggleMoveArrows);
			curY += height;
			
			
			var phase = curGameState % 5;
			if (phase == 1 || phase == 3)
			{
				if(lastRoundMoves)
				{
					addGameButton("View Last Round Moves",curX, curY, width, height, bgColor, fgColor, font, viewLastRoundMoves);
					curY += height;
				}
				
				if (displacedUnits)
				{
					bgColor = "red";
					fgColor = "black";
					addGameButton("You have displaced units!!!", curX, curY, width, height, bgColor, fgColor, font, null);
					curY += height;
				}
				
				gameBoard.redraw();

			}
			else if (phase == 4)
			{
				var msg = resolveSupplyCenters().trim();

				curY += height;
				bgColor = "yellow";
				var msgArr = msg.split("\n");
				for (var i = 0; i < msgArr.length; i++)
				{
					addGameButton(msgArr[i], curX, curY, width, height, bgColor, fgColor, font, null);
					curY += height;
				}

				bgColor = "#55FF55";
				curY += height;
				addGameButton("Resolve Supply Centers", curX, curY, width, height, bgColor, fgColor, font, null);
				curY += height;

				for (var i = 0; i < validBuildStations.size(); i++)
				{
					var btn = addGameButton("Build Army In " + validBuildStations.get(i), curX, curY, width, height, bgColor, fgColor, font, buildDisbandAction);

					btn.setActionId("B A " + validBuildStations.get(i));
					curY += height;
					btn = addGameButton("Build Fleet In " + validBuildStations.get(i), curX, curY, width, height, bgColor, fgColor, font, buildDisbandAction);
					btn.setActionId("B F " + validBuildStations.get(i));
					curY += height;
				}
				for (var i = 0; i < removeUnitList.size(); i++)
				{
					var uCountry = getCurrentUserCountry();

					var unitType = map[uCountry][removeUnitList.get(i)];

					var btn = addGameButton("Disband Unit In " + removeUnitList.get(i), curX, curY, width, height, bgColor, fgColor, font, buildDisbandAction);
					btn.setActionId("D " + unitType + " " + removeUnitList.get(i));
					curY += height;
				}
			}
			
		}
		
		function viewLastRoundMoves()
		{
			var msg = "";
			for(var i=0;i<lastRoundMoves.lastMoves.length;i++)
			{
				msg += lastRoundMoves.lastMoves[i] + "\n";				
			}
			alert(msg);
		}

		function buildDisbandAction(actionId)
		{
			//TODO...
			appendOrder(actionId);
		}

		function getCurrentUserCountry()
		{
			return (user["country" + curGameId]);
		};

		var validBuildStations = new GList();
		var removeUnitList = new GList();
		var stationDifference = 0;

		function countSupplyCenters()
		{
			var ret = new Object();
			for (country in userList)
			{
				ret[country] = 0;
			}

			for (province in map.supply)
			{
				ret[map.supply[province]] += 1;
			}
			return ret;
		}

		function resolveSupplyCenters()
		{
			var supplyList = new GList();
			var uCountry = getCurrentUserCountry();

			if (validBuildStations)
				validBuildStations.removeAllElements();

			if (removeUnitList)
				removeUnitList.removeAllElements();

			for (province in map.supply)
			{
				if (map.supply[province] == uCountry)
					supplyList.add(province);
			}

			var userUnits = new GList();
			var curUnits = gameBoard.getUnits();
			for (var i = 0; i < curUnits.length; i++)
				if (curUnits[i].country == uCountry)
					userUnits.add(curUnits[i].province);

			var curCount = userUnits.length();
			var targetCount = supplyList.length();

			var out = "You have " + curCount + " units.\n";
			stationDifference = targetCount - curCount;
			out += "You are alowed " + targetCount + " units\n";

			if (stationDifference > 0)
			{
				out += "You could build " + stationDifference + " unit(s)\n";
				var buildStations = diplomacyMap.getHomeCountrySupplyCenters(uCountry);

				for (var i = 0; i < buildStations.length; i++)
				{
					var provinceIsOccupied = gameBoard.getUnit(buildStations[i]);
					if (!provinceIsOccupied)
						validBuildStations.add(buildStations[i]);
				}
				out += "You may build in " + validBuildStations.list + " \n";
			}
			else if (stationDifference < 0)
			{
				out += "You must disband " + (0 - stationDifference) + " unit(s)\n";
				for (var i = 0; i < userUnits.size(); i++)
				{
					removeUnitList.add(userUnits.get(i));
				}

			}
			return out;
		}

		function toggleMoveArrows()
		{
			if (gameBoard.displayArrows)
				gameBoard.setDiplayArrows(false);
			else
				gameBoard.setDiplayArrows(true);

			gameBoard.redraw();
		}

		function toggleDisplayUnits()
		{
			if (gameBoard.displayUnits)
				gameBoard.setDisplayUnits(false);
			else
				gameBoard.setDisplayUnits(true);

			gameBoard.redraw();
		}

		function toggleSupplyCenters()
		{
			if (gameBoard.displaySupplyCenters)
				gameBoard.setDisplaySupplyCenters(false);
			else
				gameBoard.setDisplaySupplyCenters(true);

			gameBoard.redraw();
		}

		function getStateName(state)
		{
			var year = 1900 + Math.floor(state / 5);

			var phase = state % 5;
			var strSeason = "Spring ";
			if (phase > 1)
				strSeason = "Fall ";
			strSeason += year;

			if (phase == 0 || phase == 2)
				strSeason += " (Order Writing Phase)"
			else if (phase == 1 || phase == 3)
				strSeason += " (Order Resolution Phase)";
			else if (phase == 4)
				strSeason += " (Build and Disband Phase)";

			return strSeason + " [" + state + "]";
		}

		function addGameButton(caption, x, y, w, h, bgColor, fgColor, font, eventListener)
		{
			var button = createGameButton(caption, x, y, w, h, bgColor, fgColor, font, eventListener);
			gameBoard.addButton(button);
			return button;
		}

		function createGameButton(caption, x, y, w, h, bgColor, fgColor, font, eventListener)
		{
			var button = new GButton(caption);
			button.setDimmensions(x, y, w, h);
			button.setBackgroundColor(bgColor);
			button.setForegroundColor(fgColor);
			button.setFont(font);
			if (eventListener)
				button.addEventListener(eventListener);

			return button;
		}

		/**
		 * downloads the current moves for the curUser for the curGame and displays them on the board.
		 */
		function getMyMoves(callback)
		{
			getPendingMoves(curGameId, function(msg)
			{
				try
				{
					var moveList = JSON.parse(msg);
					if (!moveList.pendingMoves)
						return

					clearOrderList();
					for (var i = 0; i < moveList.pendingMoves.length; i++)
						appendOrder(moveList.pendingMoves[i]);
					needToSubmit = false;

					refreshGameArrowsAndButtons();
					callback();
				}
				catch (err)
				{
				}
			});

		}
		/*
		 * <pre>
		 * commandSets[]
		 * ------------
		 * HOLD - A BER H
		 * MOVE - A BER M KIE
		 * SUPPORT - A BER S A KIE H or A BER S A KIE M RUH
		 * CONVOY - F ENG C A WAL M PIC
		 * BUILD - B A BER
		 * </pre>
		 */

		function modifyArrow(arrow, fgColor, width, srcHead, targetHead, alpha)
		{
			arrow.setForegroundColor(fgColor);
			arrow.setWidth(width);
			arrow.setSourceArrowHeadVisible(srcHead);
			arrow.setTargetArrowHeadVisible(targetHead);
			arrow.setAlpha(0.5);
		}
		
		var lastRoundMoves;
		function addLastRoundArrows(jsonListMoves)
		{
			var moves = JSON.parse(jsonListMoves);
			lastRoundMoves = moves;
			
			for(var i=0;i<moves.lastMoves.length;i++)
			{
				var move = moves.lastMoves[i][0];
				var eval = moves.lastMoves[i][1];
				addLastMoveArrow(move, eval);
			}
		}
		
		function addLastMoveArrow(movestr, eval)
		{
			var ret;
			var moves = movestr.split(" ");
			if (moves.length < 3)
				return;

			var color = "red";
			if(eval=="success")
				color = "green";
			else if(eval == "failure")
				color = "red";
			else
				return;
			
			
							
			if (moves.length == 4 && moves[2] == "M")
			{
				var moveArrow = gameBoard.createMoveArrow(moves[1], moves[3]);
				if (moveArrow)
				{
					modifyArrow(moveArrow, color, 1, false, true, 1.0);
					gameBoard.addArrow(moveArrow);
				}
			}
			gameBoard.redraw();
		}
		
		function addMoveArrows()
		{
			for (var i = 0; i < orderList.length; i++)
			{
				var moves = orderList[i].split(" ");
				if (moves.length < 3)
					continue;

				if (moves.length == 3 && moves[2] == "H")
				{
					var holdButton = gameBoard.createHoldButton(moves[1]);
					if (!holdButton)
						continue;
					gameBoard.addButton(holdButton);
				}
				else if (moves.length == 4 && moves[2] == "M")
				{
					var moveArrow = gameBoard.createMoveArrow(moves[1], moves[3]);
					if (!moveArrow)
						continue;
					modifyArrow(moveArrow, "red", 3, false, true, 1.0);
					gameBoard.addArrow(moveArrow);
				}
				else if (moves.length > 5 && moves[2] == "S")
				{
					var moveArrow = gameBoard.createMoveArrow(moves[1], moves[4]);
					if (!moveArrow)
						continue;
					modifyArrow(moveArrow, "yellow", 3, false, false, 0.75);

					if (moves[5] == "H")
						gameBoard.addArrow(moveArrow);
					else if (moves[5] == "M" && moves.length > 6)
					{
						var supportArrow = gameBoard.createMoveArrow(moves[1], moves[6]);
						modifyArrow(supportArrow, "yellow", 7, false, true, 0.75);
						gameBoard.addArrow(supportArrow);
						gameBoard.addArrow(moveArrow);
					}
				}
				else if (moves.length > 5 && moves[2] == "C")
				{
					var moveArrow = gameBoard.createMoveArrow(moves[1], moves[4]);
					modifyArrow(moveArrow, "Blue", 7, false, false, 0.5);
					var convoyArrow = gameBoard.createMoveArrow(moves[1], moves[6]);
					modifyArrow(convoyArrow, "Blue", 7, false, false, 0.5);
					gameBoard.addArrow(moveArrow);
					gameBoard.addArrow(convoyArrow);
				}
			}
		}
		
		

		function refreshGameArrowsAndButtons()
		{
			
			var phase = curGameState%5;
			if(phase==1 || phase==3)
				getLastMoves(curGameId, curGameState, addLastRoundArrows);
			
			gameBoard.removeAllButtons();
			gameBoard.removeAllArrows();
			addOrderSideButtons();
			addUserListButtons();
			addMoveArrows();

			gameBoard.redraw();
		}

		function resetClickOrderState()
		{
			curClickedUnit = null;
			supportUnit = null;
			clickOrderState = 0;
			refreshGameArrowsAndButtons();
			gameBoard.setProvinceHover(false);
		}

		function reloadGame()
		{

			var gameSelector = document.getElementById("gameSelector");
			curGameId = user.gameIds[gameSelector.selectedIndex];
			loadGameboard(curGameId, documentIsReadyAndUserIsReadyAndMapIsReady);
			
			
		}
		
		function advanceClock()
		{
			var allSubmitted = true;
			
			for (country in userList)
				allSubmitted = allSubmitted && (submitStatus && submitStatus.contains(country));

			var phase= curGameState%5;
			
			var confirmRes = true;
			if(!allSubmitted && (phase==0 || phase==2))
				confirmRes = confirm("It appears not all players have submitted move orders. Are you sure you want to trigger the next turn?"); 
			
			if(confirmRes)
				triggerClock(curGameId,reloadGame);
		}
		
		
		
		function submitOrders()
		{
			submitMoves(curGameId, orderList, function()
			{
				clearOrderList();
				resetClickOrderState();
				getMyMoves();
				refreshGameArrowsAndButtons();
			});
		}

		function appendSupportMoveOrder(targetProvince)
		{
			if (!curClickedUnit)
				return;
			if (!supportUnit)
				return;

			var strOrder = curClickedUnit.unitType + " " + curClickedUnit.province + " S " + supportUnit.unitType + " " + supportUnit.province + " M " + targetProvince;
			appendOrder(strOrder);

		}

		function appendSupportHoldOrder()
		{
			if (!curClickedUnit)
				return;
			if (!supportUnit)
				return;

			var strOrder = curClickedUnit.unitType + " " + curClickedUnit.province + " S " + supportUnit.unitType + " " + supportUnit.province + " H";
			appendOrder(strOrder);
		}

		function appendMoveOrder(targetProvince)
		{
			if (!curClickedUnit)
				return;

			var strOrder = curClickedUnit.unitType + " " + curClickedUnit.province + " M " + targetProvince;
			appendOrder(strOrder);
		}

		function appendConvoyOrder(endConvoyProvince)
		{
			if (clickOrderState != 8)
				return;
			var strOrder = curClickedUnit.unitType + " " + curClickedUnit.province + " C " + convoyUnit.unitType + " " + convoyUnit.province + " M " + endConvoyProvince;
			appendOrder(strOrder);
		}

		function appendHoldOrder()
		{
			if (!curClickedUnit)
				return;

			var strOrder = curClickedUnit.unitType + " " + curClickedUnit.province + " H";
			appendOrder(strOrder);
		}

		function appendOrder(strOrder)
		{
			needToSubmit = true;
			var orderingUnit = strOrder.substring(0, 5);
			//remove any orders this unit has made already from the list
			var found = false;

			var action = strOrder.substring(0, 1);
			for (var i = 0; i < orderList.length && !found; i++)
			{
				var listUnit = orderList[i].substring(0, 5);
				if (orderingUnit == listUnit)
				{
					orderList.splice(i, 1);
					found = true;
				}

				if ((action == "B" || action == "D"))
				{
					listUnit = orderList[i].substring(4, 7);
					var actualOrderingUnit = strOrder.substring(4, 7);
					if (listUnit == actualOrderingUnit)
					{
						orderList.splice(i, 1);
						found = true;
					}
				}
			}

			//disband and build orders can only submit x order where x = the magnitude of stationDifference

			if ((action == "B" || action == "D") && orderList.length == Math.abs(stationDifference))
				orderList.splice(0, 1);

			orderList.push(strOrder);
			resetClickOrderState();
		}

		function provinceClicked(province)
		{
			if (clickOrderState == 2)
			{
				//move order
				appendMoveOrder(province);
			}
			else if (clickOrderState == 5)
			{
				appendSupportMoveOrder(province);
			}
			else if (clickOrderState == 7)
			{
				//provinceClicked will get called whenever a unit is clicked as well. 
				//we ignore the first click
				clickOrderState = 8;
			}
			else if (clickOrderState == 8)
			{
				//and proccess the second click
				appendConvoyOrder(province);
			}
		}

		function populateGameBoardWithMap()
		{
			var country = diplomacyMap.getCountries();
			displacedUnits = false;

			for (var i = 0; i < country.length; i++)
				for ( var province in map[country[i]])
				{
					var unitType = map[country[i]][province];

					if (unitType == "f" || unitType == "a")
					{
						gameBoard.addDisplacedUnit(country[i], province, unitType);
						if (country[i] == user["country" + curGameId])
							displacedUnits = true;
					}
					else
						gameBoard.addUnit(country[i], province, unitType);
				}

			for (province in map.supply)
			{
				gameBoard.addSupplyCenter(province, map.supply[province]);
			}
		}

		function documentIsReadyAndUserIsReadyAndMapIsReady()
		{
			getMyMoves(function()
			{
				gameBoard.addUnitListener(unitClicked);
				gameBoard.addProvinceListener(provinceClicked);
			});

			gameBoard.setViewPortVisibility(false);
			populateGameBoardWithMap();

			//clearCanvasLocation();
			var div = document.getElementById("canvasLocation");
			if (div)
				div.appendChild(gameBoard.canvas);

			div.appendChild(gameBoard.canvas);
			resizeAndRedraw();
			window.onresize = resizeAndRedraw;
			resetClickOrderState();
			
			//poll();
			window.setInterval(poll, 500);

		}
		

		function beginConvoyOrder()
		{
			if (clickOrderState != 1)
				return;

			gameBoard.removeAllButtons();

			var btnInstrunctions1 = new GButton("Click the unit you wish to convoy.");
			var btnInstrunctions2 = new GButton("");

			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			btnInstrunctions1.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
			btnInstrunctions2.setDimmensions(pos.x, pos.y + size.height + 25, size.width, 25);

			btnInstrunctions1.setAlpha(0.8);
			btnInstrunctions2.setAlpha(0.8);
			btnInstrunctions1.setBackgroundColor("yellow");
			btnInstrunctions2.setBackgroundColor("yellow");

			gameBoard.addButton(btnInstrunctions1);
			gameBoard.addButton(btnInstrunctions2);

			gameBoard.redraw();

			clickOrderState = 6;

		}

		function endConvoyOrder()
		{
			if (clickOrderState != 6)
				return;

			gameBoard.removeAllButtons();

			var btnInstrunctions1 = new GButton("      Click the province you want to convoy to.");
			var btnInstrunctions2 = new GButton("Make sure the mouse hover text is the same province you want.");

			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			btnInstrunctions1.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
			btnInstrunctions2.setDimmensions(pos.x, pos.y + size.height + 25, size.width, 25);

			btnInstrunctions1.setAlpha(0.8);
			btnInstrunctions2.setAlpha(0.8);
			btnInstrunctions1.setBackgroundColor("yellow");
			btnInstrunctions2.setBackgroundColor("yellow");

			gameBoard.addButton(btnInstrunctions1);
			gameBoard.addButton(btnInstrunctions2);
			gameBoard.setProvinceHover(true);
			gameBoard.redraw();
			clickOrderState = 7;

		}

		function beginMoveOrder()
		{
			if (clickOrderState == 4)
				supportMove();

			if (clickOrderState != 1)
				return;

			gameBoard.removeAllButtons();
			clickOrderState = 2;
			var btnInstrunctions1 = new GButton("      Click the province you want to move to.");
			var btnInstrunctions2 = new GButton("Make sure the mouse hover text is the same province you want to move to.");

			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			btnInstrunctions1.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
			btnInstrunctions2.setDimmensions(pos.x, pos.y + size.height + 25, size.width, 25);

			btnInstrunctions1.setAlpha(0.8);
			btnInstrunctions2.setAlpha(0.8);
			btnInstrunctions1.setBackgroundColor("yellow");
			btnInstrunctions2.setBackgroundColor("yellow");

			gameBoard.addButton(btnInstrunctions1);
			gameBoard.addButton(btnInstrunctions2);

			gameBoard.setProvinceHover(true);
			gameBoard.redraw();

		};

		function beginSupportOrder()
		{
			gameBoard.removeAllButtons();
			clickOrderState = 3;

			var btnInstrunctions1 = new GButton("Click on a the unit you wish to support.");
			var btnInstrunctions2 = new GButton("");

			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			btnInstrunctions1.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
			btnInstrunctions2.setDimmensions(pos.x, pos.y + size.height + 25, size.width, 25);

			btnInstrunctions1.setBackgroundColor("yellow");
			btnInstrunctions2.setBackgroundColor("yellow");

			gameBoard.addButton(btnInstrunctions1);
			gameBoard.addButton(btnInstrunctions2);

			gameBoard.redraw();

		};

		/**
		 *  
		 */
		function continueSupportOrder(unitToSuport)
		{
			supportUnit = unitToSuport;

			if (!supportUnit)
				return;
			if (clickOrderState != 3)
				return;

			clickOrderState = 4;
			//gameBoard.removeAllButtons();

			addUnitActionButtons(supportUnit);

			var btnInstrunctions1 = new GButton("Click on a the UNIT to support you wish to support.");
			var btnInstrunctions2 = new GButton("");

			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			btnInstrunctions1.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
			btnInstrunctions2.setDimmensions(pos.x, pos.y + size.height + 25, size.width, 25);

			btnInstrunctions1.setBackgroundColor("yellow");
			btnInstrunctions2.setBackgroundColor("yellow");

			gameBoard.addButton(btnInstrunctions1);
			gameBoard.addButton(btnInstrunctions2);

			//gameBoard.setProvinceHover(true);
			gameBoard.redraw();

		};

		function endSupportMoveOrder()
		{
			if (!supportUnit)
				return;
			if (clickOrderState != 4)
				return;

			clickOrderState = 5;
			gameBoard.removeAllButtons();

			var btnInstrunctions1 = new GButton("Click on a the province you wish to help " + supportUnit.country + " support.");
			var btnInstrunctions2 = new GButton("Make sure mouse hover text is the province you want to choose.");

			var pos = gameBoard.getMapPos();
			var size = gameBoard.getMapSize();

			btnInstrunctions1.setDimmensions(pos.x, pos.y + size.height, size.width, 25);
			btnInstrunctions2.setDimmensions(pos.x, pos.y + size.height + 25, size.width, 25);

			btnInstrunctions1.setBackgroundColor("yellow");
			btnInstrunctions2.setBackgroundColor("yellow");

			gameBoard.addButton(btnInstrunctions1);
			gameBoard.addButton(btnInstrunctions2);

			gameBoard.setProvinceHover(true);
			gameBoard.redraw();
		}

		function resizeAndRedraw()
		{

			var w = window.innerWidth;
			var h = window.innerHeight;
			gameBoard.setCanvasSize(w, h);
			//regardles of width and height the map will maintain its aspect ration
			gameBoard.setMapSize(w - 50, h - 150);
			//we want to recenter 		
			var centerX = (w - gameBoard.getMapSize().width) / 2 - 50;
			gameBoard.setMapPos(centerX, 0);

			refreshGameArrowsAndButtons();
			gameBoard.redraw();
			//update btn widths and heights
			if (clickOrderState == 1)
			{
				//update the curClickedUnit info
				curClickedUnit = gameBoard.getUnit(curClickedUnit.province);

				addUnitActionButtons(curClickedUnit);
				gameBoard.redraw();
			}
		}
		
		
	</script>
</body>
</html>