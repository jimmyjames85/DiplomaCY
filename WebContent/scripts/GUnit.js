var GUnit = function(province, country, unitType)
{
	this.province = province;
	this.country = country;
	this.unitType = unitType;
	this.alpha = 1.0;
	this.image = null;
	this.x = 0;
	this.y = 0;
	this.width = 1;
	this.height = 1;

	// min's and max's are used for hitboxes
	this.minX = this.x;
	this.maxX = this.x;
	this.minY = this.y;
	this.maxY = this.y;

	this.isDisplaced = false;
	
	

	this.setDimensions = function(x, y, width, height)
	{
		this.setPosition(x, y);
		this.setSize(width, height);
	};

	this.setSize = function(width, height)
	{
		this.width = width;
		this.height = height;
		this.updateMinMax();
	};

	this.updateMinMax = function()
	{
		this.minX = this.x;
		this.maxX = this.x + this.width;
		this.minY = this.y;
		this.maxY = this.y + this.height;
	};

	this.setPosition = function(x, y)
	{
		this.x = x;
		this.y = y;
		this.updateMinMax();
	};

};
