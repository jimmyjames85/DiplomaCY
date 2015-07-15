var GArrow = function(x1, y1, x2, y2)
{
	this.x1 = x1;
	this.y1 = y1;

	this.x2 = x2;
	this.y2 = y2;

	this.sourceHead = false;
	this.targetHead = true;

	this.fgColor = "#000000";
	this.alpha = 1.0;
	this.width = 10;//pixels

	this.setSourceArrowHeadVisible = function(bool)
	{
		this.sourceHead = false;
		if (bool)
			this.sourceHead = true;
	}
	this.setTargetArrowHeadVisible = function(bool)
	{
		this.targetHead = false;
		if (bool)
			this.targetHead = true;
	}

	this.setForegroundColor = function(color)
	{
		this.fgColor = color;
	};

	this.setAlpha = function(alpha)
	{
		this.alpha = alpha;
	};

	this.setWidth = function(w)
	{
		this.width = Math.abs(w);
	};

	this.paint = function(context2d)
	{
		var ctx = context2d;
		ctx.beginPath();
		ctx.globalAlpha = this.alpha;
		ctx.strokeStyle = this.fgColor;
		ctx.lineWidth = this.width;

		//draw line		
		ctx.moveTo(this.x1, this.y1);
		ctx.lineTo(this.x2, this.y2);
		ctx.stroke();

		var d = 5;
		if (this.sourceHead)
		{

			ctx.moveTo(this.x1 + d, this.y1 + d)
			ctx.lineTo(this.x1 - d, this.y1 - d)
			ctx.stroke();
			ctx.moveTo(this.x1 - d, this.y1 + d)
			ctx.lineTo(this.x1 + d, this.y1 - d)
			ctx.stroke();
		}
		if (this.targetHead)
		{
			/*var lineD =Math.sqrt( Math.pow((this.x1 - this.x2),2) + Math.pow((this.y1 - this.y2),2));
			var aSinAngle = (this.y1 - this.y2)/lineD;
			var angle = Math.asin( (this.y1 - this.y2)/lineD);
			var newHyp = 0.9*lineD;
			
			var opp = newHyp*Math.asin(angle);
			var newY =this.x1-opp;
			var adj = newHyp*Math.acos(angle);
			var newX = this.y1 - adj;
			
			//var newY = 
			
			ctx.strokeStyle = "#00FF00";
			ctx.moveTo(this.x2, this.y2);
			ctx.lineTo(newX, newY);//, this.y2);// (this.y1-this.y2)*0.9);
			
			ctx.stroke();*/
			ctx.moveTo(this.x2 + d, this.y2 + d)
			ctx.lineTo(this.x2 - d, this.y2 - d)
			ctx.stroke();

			ctx.moveTo(this.x2 - d, this.y2 + d)
			ctx.lineTo(this.x2 + d, this.y2 - d)
			ctx.stroke();
		}
	};

}
