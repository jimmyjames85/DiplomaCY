var GButton = function(caption)
{
	this.caption = caption;
	this.x = 0;
	this.y = 0;
	this.width = 1;
	this.height = 1;
	this.bgColor = "#ffffff";
	this.fgColor = "#000000";
	this.font = '10 pt Calibri';
	this.alpha = 1.0;
	this.textOffsetX = 5;
	this.textOffsetY = 0;

	//like swing button actions
	this.actionId = "";

	this.setTextOffset = function(x, y)
	{
		this.textOffsetX = x;
		this.textOffsetY = y;
	};

	this.eventListeners = new GList();

	this.addEventListener = function(listener)
	{
		this.eventListeners.add(listener);//;push(listener);
	};

	this.setActionId = function(actionId)
	{
		this.actionId = actionId;
	};

	this.removeEventListener = function(listener)
	{
		return this.eventListeners.remove(listener);
	};

	this.setDimmensions = function(x, y, w, h)
	{
		this.setPos(x, y);
		this.setSize(w, h);
	};
	this.setPos = function(x, y)
	{
		this.x = x;
		this.y = y;
	};

	this.setSize = function(w, h)
	{
		this.width = w;
		this.height = h;
	};

	this.setCaption = function(caption)
	{
		this.caption = caption;
	};

	this.setBackgroundColor = function(color)
	{
		this.bgColor = color;
	};

	this.setForegroundColor = function(color)
	{
		this.fgColor = color;
	};

	this.setAlpha = function(alpha)
	{
		this.alpha = alpha;
	};

	this.setFont = function(font)
	{
		this.font = font;
	};

	this.paint = function(context2d)
	{
		var ctx = context2d;
		ctx.beginPath();
		ctx.globalAlpha = this.alpha;
		ctx.fillStyle = this.bgColor;
		ctx.rect(this.x, this.y, this.width, this.height);
		ctx.fill();
		ctx.font = this.font;// '10 pt Calibri';
		ctx.fillStyle = this.fgColor;

		ctx.fillText(this.caption, this.x + this.textOffsetX, this.y + (this.height / 2) + this.textOffsetY);

	}

	this.click = function()
	{
		for (var i = 0; i < this.eventListeners.length(); i++)
			this.eventListeners.get(i)(this.actionId);
	};

	// this.addButton(x,y,w,h,caption,fgColor,bgColor,btnCaption,eventListener)
}