/**
 * map - should have functions getImageMap and getImageUnits(country, unitType)
 * that return the correct images such as in OriginalDiplomacyMap.js It should
 * also have a mapping, positions, where the keys are the provinces and the
 * values are the pixel positions within the map, of where to put units.
 */
var GameBoard = function(map, canvasWidth, canvasHeight)
{
	/**
	 * Save the map that is passed in
	 */
	this.diplomacyMap = map;

	this.displaySupplyCenters = false;

	this.setDisplaySupplyCenters = function(bool)
	{
		this.displaySupplyCenters = false;
		if (bool)
			this.displaySupplyCenters = true;
	};

	this.displayUnits = true;
	this.setDisplayUnits = function(bool)
	{
		this.displayUnits = false;
		if (bool)
			this.displayUnits = true;
	};

	/**
	 * Set width and height to the passed in width and height
	 */
	this.width = Math.max(1, canvasWidth);
	this.height = Math.max(1, canvasHeight);

	/**
	 * This is used to keep track of the actual map drawn within the context
	 */
	this.cMap = new Object();
	this.cMap.x = 0;
	this.cMap.y = 0;

	// this.cMap.width = this.width;
	// this.cMap.height = this.height;
	// default init values

	/**
	 * Used to keep track of the viewPort
	 */
	this.cMapViewPort = new Object();

	/**
	 * turn on/off viewPort
	 */
	this.viewPortIsVisible = true;

	/**
	 * turn on/off whether to display the province the mouse is hovering over.
	 * Note: when this enabled, it's constantly searching for the closest
	 * province, which is resource intensive. Use sparingly
	 */
	this.displayProvinceHover = false;

	/**
	 * list of units on the board
	 */
	this.units = new GList();
	//units are searchable by privinces
	this.units.setComparator(function(unitA, unitB)
	{
		return unitA.province.localeCompare(unitB.province);
	});

	/**
	 * list of displaced Units
	 */
	this.displacedUnits = new GList();

	/**
	 * list of buttons on the board
	 */
	this.buttonList = [];

	/**
	 * list of arrows on the board
	 */
	this.arrowList = [];

	/**
	 * list of event listeners for when a unit on the board is clicked
	 */
	this.unitListeners = [];

	//supplyCenters are  2 element arrays where the first element is the province and the 2nd is the owner (country)
	this.supplyCenters = new GList();
	this.supplyCenters.setComparator(function(a, b)
	{
		return a.province.localeCompare(b.province);
	});

	/**
	 * Whenever a unit is clicked on the board a call is made to the function
	 * 'listener' passing it information of the unit clicked (i.e. country,
	 * province, and country)
	 */
	this.addUnitListener = function(listener)
	{
		this.unitListeners.push(listener);
	};
	/**
	 * remove the function 'listener' from the list of unitListeners and return
	 * true if listener was removed
	 */
	this.removeUnitListener = function(listener)
	{
		var found = false;
		for (var i = 0; !found && i < this.unitListeners.length; i++)
			if (this.unitListeners[i] == listener)
			{
				this.unitListeners.splice(i, 1);
				found = true;
			}

		return found;
	};

	/**
	 * removes all registered unit listeners from this gameboard
	 */
	this.removeAllUnitListeners = function()
	{
		while (this.unitListeners.length > 0)
			this.unitListeners.splice(0, 1);
	};

	/**
	 * list of event listeners for when a province is clicked
	 */

	this.provinceListeners = [];

	/**
	 * Whenever the gameboard is clicked a call is made to the function
	 * 'listener' passing it the nearest province clicked
	 */
	this.addProvinceListener = function(listener)
	{
		this.provinceListeners.push(listener);
	}

	/**
	 * remove the function 'listener' from the list of provinceListeners and
	 * return true if listener was removed
	 */
	this.removeProvinceListener = function(listener)
	{
		var found = false;
		for (var i = 0; !found && i < this.provinceListeners.length; i++)
			if (this.provinceListeners[i] == listener)
			{
				this.provinceListeners.splice(i, 1);
				found = true;
			}
		return found;
	};

	/**
	 * removes all registered unit listeners from this gameboard
	 */
	this.removeAllProvinceListeners = function()
	{
		while (this.provinceListeners.length > 0)
			this.provinceListeners.splice(0, 1);
	};

	/**
	 * Returns an array (clone) of the current units on the map
	 */
	this.getUnits = function()
	{
		var ret = [];
		for (var i = 0; i < this.units.length(); i++)
			ret.push(this.cloneUnit(this.units.get(i)));

		return ret;
	};

	/**
	 * Returns an array (clone) of the current displaced units on the map
	 */
	this.getDisplacedUnits = function()
	{
		var ret = [];
		for (var i = 0; i < this.displacedUnits.length(); i++)
			ret.push(this.cloneUnit(this.displacedUnits.get(i)));

		return ret;
	};

	this.getDisplacedUnit = function(province)
	{
		var index = this.displacedUnits.indexOf(new GUnit(province, "", ""));
		var ret = null;
		if (index >= 0)
			ret = this.displacedUnits.get(index);
		return ret;
	};

	/**
	 * Removes all units from the map
	 */
	this.removeAllUnits = function()
	{
		this.units.removeAllElements();
		this.redraw();
	};

	this.removeAllDisplacedUnits = function()
	{
		this.displacedUnits.removeAllElements();
		this.redraw();
	};

	/**
	 * Removes any unit currently occupying the province. Returns true if
	 * province was successfully removed.
	 */
	this.clearProvince = function(province)
	{
		return this.units.remove(new GUnit(province, "", ""));
	};

	/**
	 * Removes any unit currently occupying the province. Returns true if
	 * province was successfully removed.
	 */
	this.clearDisplacedProvince = function(province)
	{
		return this.displacedUnits.remove(new GUnit(province, "", ""));
	};

	/**
	 * turn on/off whether to display the viewPort
	 */
	this.setViewPortVisibility = function(bool)
	{
		this.viewPortIsVisible = false;
		if (bool)
			this.viewPortIsVisible = true;
	};
	/**
	 * turn on/off whether to display the province the mouse is hovering over.
	 * Note: when this enabled, it's constantly searching for the closest
	 * province, which is resource intensive. Use sparingly
	 */
	this.setProvinceHover = function(bool)
	{
		this.displayProvinceHover = false;
		if (bool)
			this.displayProvinceHover = true;
	};

	this.addButton = function(button)
	{
		this.buttonList.push(button);
	};

	this.addArrow = function(arrow)
	{
		this.arrowList.push(arrow);
	}

	this.removeButton = function(button)
	{
		var found = false;
		for (var i = 0; !found && i < this.buttonList.length; i++)
			if (this.buttonList[i] == button)
			{
				this.buttonList.splice(i, 1);
				found = true;
			}

		return found;
	};

	this.removeArrow = function(arrow)
	{
		var found = false;
		for (var i = 0; !found && i < this.arrowList.length; i++)
			if (this.arrowList[i] == arrow)
			{
				this.arrowList.splice(i, 1);
				found = true;
			}
		return found;
	}

	/**
	 * removes all registered buttons from the gameboard
	 */
	this.removeAllButtons = function()
	{
		while (this.buttonList.length > 0)
			this.buttonList.splice(0, 1);
	};

	/**
	 * removes all registered arrows from the gameboard
	 */
	this.removeAllArrows = function()
	{
		while (this.arrowList.length > 0)
			this.arrowList.splice(0, 1);
	};

	/**
	 * Updates where the unit should be relative to the cMap Assumes unit is
	 * valid within the map
	 */
	this.updateUnitSizeAndLocation = function(unit)
	{
		var positions = this.diplomacyMap.positions;
		var posX = positions[unit.province][0] * this.cMap.scale;
		var posY = positions[unit.province][1] * this.cMap.scale;

		var drawHeight = unit.image.height * this.cMap.scale * this.diplomacyMap.unitScaleFactor;
		var drawWidth = unit.image.width * this.cMap.scale * this.diplomacyMap.unitScaleFactor;

		posX = posX - drawWidth / 2;
		posY = posY - drawHeight / 2;
		unit.setDimensions(posX, posY, drawWidth, drawHeight);

		if (unit.isDisplaced)//shift to the right
			unit.setPosition(posX + drawWidth, posY + drawHeight);

	};

	this.addSupplyCenter = function(provinceLocation, countryOwner)
	{
		provinceLocation = provinceLocation.toUpperCase();
		countryOwner = countryOwner.toUpperCase();
		var positions = this.diplomacyMap.positions;
		// if province is not a registered province in the map
		if (!positions[provinceLocation])
			return;

		var supply = new GUnit(provinceLocation, countryOwner, "S");
		var img = this.diplomacyMap.getImageUnit(countryOwner, "S");
		if (!img)
			return;
		supply.image = img;
		this.supplyCenters.add(supply);
	};

	this.addDisplacedUnit = function(country, province, unitType)
	{
		province = province.toUpperCase();
		country = country.toUpperCase();
		unitType = unitType.toUpperCase();
		var positions = this.diplomacyMap.positions;
		// if province is not a registered province in the map
		if (!positions[province])
			return;

		var img = this.diplomacyMap.getImageUnit(country, unitType);
		if (!img)
			return;

		var unit = new GUnit(province, country, unitType);
		unit.image = img;
		unit.alpha = 0.75;// TODO
		unit.isDisplaced = true;
		this.clearDisplacedProvince(province);
		this.displacedUnits.add(unit);

		this.updateUnitSizeAndLocation(unit);
	}

	/**
	 * add's a new unit to the map replacing any existing unit occupying the
	 * same province 
	 * 
	 */
	this.addUnit = function(country, province, unitType)
	{

		province = province.toUpperCase();
		country = country.toUpperCase();
		unitType = unitType.toUpperCase();

		var positions = this.diplomacyMap.positions;
		// if province is not a registered province in the map
		if (!positions[province])
			return;

		var img = this.diplomacyMap.getImageUnit(country, unitType);
		if (!img)
			return;

		var unit = new GUnit(province, country, unitType);
		unit.image = img;
		unit.alpha = 0.75;// TODO
		unit.isDisplaced = false;
		this.clearProvince(province);
		this.units.add(unit);
		this.updateUnitSizeAndLocation(unit);
	};

	this.createHoldButton = function(province)
	{
		var unit;

		for (var i = 0; !unit && i < this.units.length(); i++)
			if (this.units.get(i).province == province)
				unit = this.units.get(i);

		if (!unit)
			return null;

		var holdButton = new GButton("Hold");
		holdButton.setPos(this.cMap.x + unit.minX, this.cMap.y + unit.maxY);
		holdButton.setSize(unit.width, unit.height * 0.75);
		holdButton.setForegroundColor("red");
		holdButton.setBackgroundColor("black");
		//holdButton.setTextOffset(-3, 0);

		return holdButton;
	};

	/**
	 * creates an arrow from provinceFrom to provinceTo
	 * returns the arrow
	 */
	this.createMoveArrow = function(provinceFrom, provinceTo)
	{
		try
		{
			var posFrom = this.diplomacyMap.positions[provinceFrom];
			var posTo = this.diplomacyMap.positions[provinceTo];

			var x1 = this.cMap.x + posFrom[0] * this.cMap.scale;
			var y1 = this.cMap.y + posFrom[1] * this.cMap.scale;

			var x2 = this.cMap.x + posTo[0] * this.cMap.scale;
			var y2 = this.cMap.y + posTo[1] * this.cMap.scale;

			var arrow = new GArrow(x1, y1, x2, y2);

			return arrow;

		}
		catch (err)
		{

			//provinces may not be defined
		}

		return null;

	}

	this.getMapPos = function()
	{
		var pos = new Object();
		pos.x = this.cMap.x;
		pos.y = this.cMap.y;
		return pos;
	};

	this.getMapSize = function()
	{
		var size = new Object();
		size.width = this.cMap.width;
		size.height = this.cMap.height;
		return size;
	};
	this.debug = function(data)
	{
		this.redraw();
		var ctx = this.canvas.getContext("2d");
		ctx.beginPath();
		ctx.globalAlpha = 0.5;
		ctx.fillStyle = 'white';
		ctx.rect(this.cMap.x, this.cMap.y, this.cMap.width, 50);
		ctx.fill();
		ctx.font = '11pt Calibri';
		ctx.fillStyle = 'red';
		ctx.fillText(data, this.cMap.x + 10, this.cMap.y + 10);
	};

	/**
	 * Returns the closet province to the coordinates (x,y) (relative to the
	 * cMap not the canvas)
	 */
	this.calculateClosestProvince = function(x, y)
	{
		//remove the scale effect
		x = x / this.cMap.scale;
		y = y / this.cMap.scale;

		var positions = this.diplomacyMap.positions;

		var shortestProvince = "none";
		var shortestDistance = -1;

		for ( var province in positions)
		{
			var posX = positions[province][0];
			var posY = positions[province][1];

			var a = posX - x;
			var b = posY - y;
			a = a * a;
			b = b * b;

			var distance = Math.sqrt(a + b);
			if (shortestDistance == -1 || distance < shortestDistance)
			{
				shortestDistance = distance;
				shortestProvince = province;
			}

		}
		return shortestProvince;
	};

	this.cloneUnit = function(unit)
	{
		var ret = new Object();
		ret.country = unit.country;
		ret.province = unit.province;
		ret.unitType = unit.unitType;
		ret.absoluteX = unit.x + this.cMap.x;
		ret.absoluteY = unit.y + this.cMap.y;
		ret.mapX = unit.x;
		ret.mapY = unit.y;
		ret.width = unit.width;
		ret.height = unit.height;
		ret.isDisplaced = unit.isDisplaced;
		return ret;
	};

	/**
	 * Firefox does not have offsetX and offsetY so we add it to the mouseEvent
	 * 
	 */
	this.resolveMouseEvent = function(mouseEvent)
	{
		var returnEvent = mouseEvent;
		if (!mouseEvent.offsetX)
		{
			returnEvent = new Object();
			for (key in mouseEvent)
			{
				returnEvent[key] = mouseEvent[key];
				returnEvent.offsetX = mouseEvent.layerX - this.canvas.offsetLeft;
				returnEvent.offsetY = mouseEvent.layerY - this.canvas.offsetTop;
			}
		}
		return returnEvent;
	};

	/**
	 * Event listener that makes calls to all the callbacks neccessary
	 * 
	 * GButtons are on top and take 1st priority.
	 * 
	 * Then unitsListeners
	 * 
	 * Then provinceListeners
	 * 
	 */
	this.canvasClick = function(event)
	{
		event = this.resolveMouseEvent(event);

		if (event.ctrlKey)
			this.setViewPortVisibility(!this.viewPortIsVisible);

		if (event.shiftKey)
			this.setProvinceHover(!this.displayProvinceHover);

		this.redraw();

		// TODO add callback array
		var ex = event.offsetX;
		var ey = event.offsetY;

		// x and y are relative within the map only now
		var x = ex - this.cMap.x;
		var y = ey - this.cMap.y;

		var buttonClicked = false;

		// browse throught the list of buttons and check for click boundries
		for (var i = 0; i < this.buttonList.length; i++)
		{
			var button = this.buttonList[i];
			if (ex >= button.x && ex <= (button.x + button.width) && ey >= button.y && ey <= (button.y + button.height))
			{
				this.buttonList[i].click();
				buttonClicked = true;
			}
		}

		var unitClicked = false;
		var clickedUnitProvince = "undefined";

		// browse through the list of units and check for click boundries
		for (var i = 0; i < this.units.length(); i++)
		{
			var unit = this.units.get(i);
			if (x >= unit.minX && x <= unit.maxX && y >= unit.minY && y <= unit.maxY)
			{
				clickedUnitProvince = unit.province;
				for (var u = 0; u < this.unitListeners.length && !buttonClicked; u++)
				{
					this.unitListeners[u](this.cloneUnit(unit));
					unitClicked = true;

				}
			}
		}

		var unitClicked = false;
		var clickedUnitProvince = "undefined";

		// browse through the list of displaced units and check for click boundries
		for (var i = 0; i < this.displacedUnits.length(); i++)
		{
			var unit = this.displacedUnits.get(i);
			if (x >= unit.minX && x <= unit.maxX && y >= unit.minY && y <= unit.maxY)
			{
				clickedUnitProvince = unit.province;
				for (var u = 0; u < this.unitListeners.length && !buttonClicked; u++)
				{
					this.unitListeners[u](this.cloneUnit(unit));
					unitClicked = true;
				}
			}
		}

		var closetProvince = this.calculateClosestProvince(x, y);
		// call back to all of our province listeners
		for (var i = 0; i < this.provinceListeners.length && !unitClicked && !buttonClicked; i++)
		{
			this.provinceListeners[i](closetProvince);
		}

	};

	this.canvasMouseMove = function(event)
	{

		if (!this.displayProvinceHover)
			return;

		event = this.resolveMouseEvent(event);
		var x = event.offsetX - this.cMap.x;
		var y = event.offsetY - this.cMap.y;

		/*
		 * for(var i=0;i<this.units.length;i++) { var curUnit =
		 * this.unitList[i]; if(x>=curUnit.minX && x<=curUnit.maxX &&
		 * y>=curUnit.minY && y<=curUnit.maxY) { curUnit.alpha = 0.5;
		 * this.redraw(); } else curUnit.alpha = 1.0; }
		 */

		if (!this.displayProvinceHover)
			return;
		// this.debug
		// var mousePos = getMousePos(canvas, evt);
		// var message = 'Mouse position: ' + mousePos.x + ',' + mousePos.y;
		// writeMessage(canvas, message);

		var shortestP = this.calculateClosestProvince(x, y);

		this.redraw();
		this.debug(event.offsetX + "," + event.offsetY + " closestP=" + shortestP);
		var ctx = this.canvas.getContext("2d");
		ctx.beginPath();
		ctx.globalAlpha = 1;
		ctx.fillStyle = 'white';
		ctx.font = 'bold 16pt Calibri';
		ctx.fillText(shortestP, event.offsetX + 10, event.offsetY + 10);

	};

	/**
	 * The main canvas for the GameBoard
	 */
	this.canvas = document.createElement('canvas');
	this.canvas.addEventListener('click', this.canvasClick.bind(this), true);
	this.canvas.addEventListener('mousemove', this.canvasMouseMove.bind(this), true);
	// bind is necessary so that when canvasClick refers to 'this' it refers
	// to this and not the default anonymous this

	this.clear = function()
	{
		// save unitList
		var oldUnitList = [];
		for (var i = 0; i < this.units.length(); i++)
		{
			var unit = new Object();
			unit.country = this.units.get(i).country;
			unit.province = this.units.get(i).province;
			unit.unitType = this.units.get(i).unitType;
			oldUnitList.push(unit);
		}
		this.units.removeAllElements();

		// clear canvas
		this.canvas.width = this.canvas.width;
		var ctx = this.canvas.getContext("2d");
		ctx.beginPath();
		ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

		for (var i = 0; i < oldUnitList.length; i++)
			this.addUnit(oldUnitList[i].country, oldUnitList[i].province, oldUnitList[i].unitType);

	};

	this.redraw = function()
	{
		this.drawMap(this.cMap.x, this.cMap.y, this.cMap.width, this.cMap.height);
		this.drawArrows();
		this.drawButtons();
	};

	this.drawButtons = function()
	{
		var ctx = this.canvas.getContext("2d");
		for (var i = 0; i < this.buttonList.length; i++)
			this.buttonList[i].paint(ctx);
	};

	this.displayArrows = true;

	this.setDiplayArrows = function(bool)
	{
		this.displayArrows = false;
		if(bool)
			this.displayArrows = true;
	};

	this.drawArrows = function()
	{
		if (this.displayArrows)
		{
			var ctx = this.canvas.getContext("2d");
			for (var i = 0; i < this.arrowList.length; i++)
				this.arrowList[i].paint(ctx);
		}
	};

	/**
	 * draws the map with top left corner at (x,y) and bottom right corner at
	 * (x+w,y+h) within the total canvas' width and height. It repsects the
	 * aspect ratio of the underlying diplomacy map image. This effectively sets
	 * the mapPos and mapSize as well.
	 */
	this.drawMap = function(x, y, w, h)
	{
		this.clear();
		var ctx = this.canvas.getContext("2d");

		var mapImg = this.diplomacyMap.getImageMap();

		var imgW = mapImg.width;// actual png width
		var imgH = mapImg.height;// actual png height

		
		
		
		
		/*
		 * var ratio = imgW / imgH; // cMap object used to keep track of the
		 * destination coordinates
		 * 
		 * this.cMap.x = x; this.cMap.y = y; if (w < h) { this.cMap.width = w;
		 * this.cMap.height = w / ratio; } else { this.cMap.width = h * ratio;
		 * this.cMap.height = h; } // this is a scaling percentage for image
		 * locations this.cMap.scale = (this.cMap.width) / imgW;
		 */
		
		//var xScale = */
		
		// draw main map
		ctx.drawImage(mapImg, 0, 0, imgW, imgH, this.cMap.x, this.cMap.y, this.cMap.width, this.cMap.height);
		
		if (this.displaySupplyCenters)
			for (var i = 0; i < this.supplyCenters.length(); i++)
			{
				var supply = this.supplyCenters.get(i);
				this.updateUnitSizeAndLocation(supply);
				ctx.globalAlpha = supply.alpha;
				ctx.drawImage(supply.image, 0, 0, supply.image.width, supply.image.height, this.cMap.x + supply.x, this.cMap.y + supply.y, supply.width / 2, supply.height / 2);
				ctx.globalAlpha = 1.0;
			}

		// //////////draw fleets and armys on main map

		if (this.displayUnits)
			for (var i = 0; i < this.units.length(); i++)
			{
				var unit = this.units.get(i);
				this.updateUnitSizeAndLocation(unit);
				ctx.globalAlpha = unit.alpha;
				ctx.drawImage(unit.image, 0, 0, unit.image.width, unit.image.height, this.cMap.x + unit.x, this.cMap.y + unit.y, unit.width, unit.height);
				ctx.globalAlpha = 1.0;
			}

		// //////////draw displaced units
		if (this.displayUnits)
			for (var i = 0; i < this.displacedUnits.length(); i++)
			{
				var unit = this.displacedUnits.get(i);
				this.updateUnitSizeAndLocation(unit);
				ctx.globalAlpha = unit.alpha;
				ctx.drawImage(unit.image, 0, 0, unit.image.width, unit.image.height, this.cMap.x + unit.x, this.cMap.y + unit.y, unit.width, unit.height);
				ctx.globalAlpha = 1.0;
			}

		// /////////////////draw view port
		this.cMapViewPort.scale = 0.25;
		this.cMapViewPort.width = this.cMapViewPort.scale * this.cMap.width;
		this.cMapViewPort.height = this.cMapViewPort.scale * this.cMap.height;
		this.cMapViewPort.x = this.cMap.x + this.cMap.width - this.cMapViewPort.width;
		this.cMapViewPort.y = this.cMap.y + this.cMap.height - this.cMapViewPort.height;

		// draw viewPort
		if (this.viewPortIsVisible)
		{
			ctx.drawImage(mapImg, 0, 0, imgW, imgH, this.cMapViewPort.x, this.cMapViewPort.y, this.cMapViewPort.width, this.cMapViewPort.height);
			// draw fleets and armys on view port map
			for (var i = 0; i < this.units.length(); i++)
			{
				var unit = this.units.get(i);
				var ux = this.cMapViewPort.x + unit.x * this.cMapViewPort.scale;
				var uy = this.cMapViewPort.y + unit.y * this.cMapViewPort.scale;
				var uw = unit.width * this.cMapViewPort.scale;
				var uh = unit.height * this.cMapViewPort.scale;
				ctx.drawImage(unit.image, 0, 0, unit.image.width, unit.image.height, ux, uy, uw, uh);
			}

			ctx.beginPath();
			ctx.lineWidth = 4;
			ctx.rect(this.cMapViewPort.x, this.cMapViewPort.y, this.cMapViewPort.width, this.cMapViewPort.height);
			ctx.stroke();

		}
	};

	/**
	 * Set size of the html canvas element
	 */
	this.setCanvasSize = function(w, h)
	{
		if (w <= 0 || h <= 0)
			return;

		var widthPercentage = w / this.canvas.width;
		var heightPercentage = h / this.canvas.height;

		this.width = w;
		this.height = h;
		this.canvas.width = w;
		this.canvas.height = h;

		this.redraw();
		// drawMap(0, 0, w, h);
	};

	/**
	 * Retrieves the unit on the province if such a unit exists
	 */
	this.getUnit = function(province)
	{
		var index = this.units.indexOf(new GUnit(province, "", ""));
		var ret = null;
		if (index >= 0)
			ret = this.cloneUnit(this.units.get(index));
		return ret;
	};

	/**
	 * Set size of the entire game board map. Maintains aspect ratio of the
	 * underlying map image. So it is only guaranteed that either width or
	 * height will be set to w of h respectively. The other will be set less
	 * than or equal to the incomming parameter depending on the aspect ratio.
	 */
	this.setMapSize = function(w, h)
	{
		this.setMapDimmensions(this.cMap.x, this.cMap.y, w, h);

		// this.drawMap(this.cMap.x, this.cMap.y, w, h);
	};

	this.setMapDimmensions = function(x, y, w, h)
	{
		if (w <= 0 || h <= 0)
			return;
		var mapImg = this.diplomacyMap.getImageMap();
		var imgW = mapImg.width;// actual png width
		var imgH = mapImg.height;// actual png height
		var ratio = imgW / imgH;

		// cMap object used to keep track of the destination coordinates

		this.cMap.x = x;
		this.cMap.y = y;
		if (w < h)
		{
			this.cMap.width = w;
			this.cMap.height = w / ratio;
		}
		else
		{
			this.cMap.width = h * ratio;
			this.cMap.height = h;
		}

		// this is a scaling percentage for unit image locations
		this.cMap.scale = (this.cMap.width) / imgW;
	};

	/**
	 * Sets the position of the map to be drawn relative to the entire canvas
	 */
	this.setMapPos = function(x, y)
	{
		this.setMapDimmensions(x, y, this.cMap.width, this.cMap.height);
		// this.drawMap(x, y, this.cMap.width, this.cMap.height);
	}

	this.setCanvasSize(this.width, this.height);
	this.setMapSize(this.width, this.height);
}