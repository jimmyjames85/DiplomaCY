/**
 * This Object/Class loads the images and map associated with the original
 * Diplomacy Game Map and pieces. Once the images have been loaded, a call to
 * callback is made.
 */
var OriginalDiplomacyMap = function(callback)
{
	var totalImagesLoaded = 0;

	/**
	 * This counts the number of images loaded. Once the sum reaches the total
	 * number of images we then make a call to callback indicating all the
	 * images have been pre-loaded
	 */
	this.waitForLoad = function()
	{
		totalImagesLoaded++;
		if (totalImagesLoaded == 22)
		{
			callback();
		}
	};

	this.getImageMap = function()
	{
		return this.imageMap;
	};

	this.unitScaleFactor = 0.1;

	this.imageMap = new Image();
	this.imageMap.src = '../images/OriginalMap/diplomacyGameMap.png';
	this.imageMap.addEventListener("load", this.waitForLoad, false);

	this.imageEnglandFleet = new Image();
	this.imageEnglandFleet.src = "../images/OriginalMap/fleet_england.png";
	this.imageEnglandFleet.addEventListener("load", this.waitForLoad, false);

	this.imageEnglandArmy = new Image();
	this.imageEnglandArmy.src = "../images/OriginalMap/army_england.png";
	this.imageEnglandArmy.addEventListener("load", this.waitForLoad, false);

	this.imageEnglandSupply = new Image();
	this.imageEnglandSupply.src = "../images/OriginalMap/supply_england.png";
	this.imageEnglandSupply.addEventListener("load", this.waitForLoad, false);

	this.imageFranceFleet = new Image();
	this.imageFranceFleet.src = "../images/OriginalMap/fleet_france.png";
	this.imageFranceFleet.addEventListener("load", this.waitForLoad, false);

	this.imageFranceArmy = new Image();
	this.imageFranceArmy.src = "../images/OriginalMap/army_france.png";
	this.imageFranceArmy.addEventListener("load", this.waitForLoad, false);

	this.imageFranceSupply = new Image();
	this.imageFranceSupply.src = "../images/OriginalMap/supply_france.png";
	this.imageFranceSupply.addEventListener("load", this.waitForLoad, false);

	this.imageAustriaFleet = new Image();
	this.imageAustriaFleet.src = "../images/OriginalMap/fleet_austria.png";
	this.imageAustriaFleet.addEventListener("load", this.waitForLoad, false);

	this.imageAustriaArmy = new Image();
	this.imageAustriaArmy.src = "../images/OriginalMap/army_austria.png";
	this.imageAustriaArmy.addEventListener("load", this.waitForLoad, false);

	this.imageAustriaSupply = new Image();
	this.imageAustriaSupply.src = "../images/OriginalMap/supply_austria.png";
	this.imageAustriaSupply.addEventListener("load", this.waitForLoad, false);

	this.imageItalyFleet = new Image();
	this.imageItalyFleet.src = "../images/OriginalMap/fleet_italy.png";
	this.imageItalyFleet.addEventListener("load", this.waitForLoad, false);

	this.imageItalyArmy = new Image();
	this.imageItalyArmy.src = "../images/OriginalMap/army_italy.png";
	this.imageItalyArmy.addEventListener("load", this.waitForLoad, false);

	this.imageItalySupply = new Image();
	this.imageItalySupply.src = "../images/OriginalMap/supply_italy.png";
	this.imageItalySupply.addEventListener("load", this.waitForLoad, false);

	this.imageGermanyFleet = new Image();
	this.imageGermanyFleet.src = "../images/OriginalMap/fleet_germany.png";
	this.imageGermanyFleet.addEventListener("load", this.waitForLoad, false);

	this.imageGermanyArmy = new Image();
	this.imageGermanyArmy.src = "../images/OriginalMap/army_germany.png";
	this.imageGermanyArmy.addEventListener("load", this.waitForLoad, false);

	this.imageGermanySupply = new Image();
	this.imageGermanySupply.src = "../images/OriginalMap/supply_germany.png";
	this.imageGermanySupply.addEventListener("load", this.waitForLoad, false);

	this.imageRussiaFleet = new Image();
	this.imageRussiaFleet.src = "../images/OriginalMap/fleet_russia.png";
	this.imageRussiaFleet.addEventListener("load", this.waitForLoad, false);

	this.imageRussiaArmy = new Image();
	this.imageRussiaArmy.src = "../images/OriginalMap/army_russia.png";
	this.imageRussiaArmy.addEventListener("load", this.waitForLoad, false);

	this.imageRussiaSupply = new Image();
	this.imageRussiaSupply.src = "../images/OriginalMap/supply_russia.png";
	this.imageRussiaSupply.addEventListener("load", this.waitForLoad, false);

	this.imageTurkeyFleet = new Image();
	this.imageTurkeyFleet.src = "../images/OriginalMap/fleet_turkey.png";
	this.imageTurkeyFleet.addEventListener("load", this.waitForLoad, false);

	this.imageTurkeyArmy = new Image();
	this.imageTurkeyArmy.src = "../images/OriginalMap/army_turkey.png";
	this.imageTurkeyArmy.addEventListener("load", this.waitForLoad, false);

	this.imageTurkeySupply = new Image();
	this.imageTurkeySupply.src = "../images/OriginalMap/supply_turkey.png";
	this.imageTurkeySupply.addEventListener("load", this.waitForLoad, false);

	this.positions = new Object();

	// returns the pixel position of countries in diplomacyGameMap.png
	// Austria
	this.positions.BOH =
	[
			586, 576
	];
	this.positions.BUD =
	[
			698, 652
	];
	this.positions.GAL =
	[
			726, 585
	];
	this.positions.TRI =
	[
			616, 700
	];
	this.positions.TYR =
	[
			543, 642
	];
	this.positions.VIE =
	[
			613, 619
	];
	// England
	this.positions.CLY =
	[
			305, 342
	];
	this.positions.EDI =
	[
			354, 363
	];
	this.positions.LVP =
	[
			332, 414
	];
	this.positions.LON =
	[
			355, 485
	];
	this.positions.WAL =
	[
			298, 462
	];
	this.positions.YOR =
	[
			355, 430
	];
	// France
	this.positions.BRE =
	[
			282, 571
	];
	this.positions.BUR =
	[
			407, 616
	];
	this.positions.GAS =
	[
			320, 667
	];
	this.positions.MAR =
	[
			397, 709
	];
	this.positions.PAR =
	[
			363, 579
	];
	this.positions.PIC =
	[
			369, 542
	];
	// Germany
	this.positions.BER =
	[
			565, 490
	];
	this.positions.KIE =
	[
			506, 486
	];
	this.positions.MUN =
	[
			508, 582
	];
	this.positions.PRU =
	[
			634, 475
	];
	this.positions.RUH =
	[
			463, 542
	];
	this.positions.SIL =
	[
			611, 536
	];
	// Italy
	this.positions.APU =
	[
			580, 800
	];
	this.positions.NAP =
	[
			579, 845
	];
	this.positions.PIE =
	[
			466, 683
	];
	this.positions.ROM =
	[
			536, 778
	];
	this.positions.TUS =
	[
			507, 733
	];
	this.positions.VEN =
	[
			522, 701
	];
	// Russia
	this.positions.LVN =
	[
			749, 433
	];
	this.positions.MOS =
	[
			921, 406
	];
	this.positions.SEV =
	[
			1004, 572
	];
	this.positions.STP =
	[
			903, 245
	];
	this.positions.STPN =
	[
			850, 180
	];
	this.positions.STPS =
	[
			780, 310
	];
	this.positions.UKR =
	[
			820, 571
	];
	this.positions.WAR =
	[
			701, 525
	];
	// Turkey
	this.positions.ANK =
	[
			940, 804
	];
	this.positions.ARM =
	[
			1108, 799
	];
	this.positions.CON =
	[
			848, 831
	];
	this.positions.SMY =
	[
			920, 870
	];
	this.positions.SYR =
	[
			1078, 900
	];
	// Nuetrals
	this.positions.ALB =
	[
			660, 806
	];
	this.positions.BEL =
	[
			412, 532
	];
	this.positions.BUL =
	[
			745, 767
	];
	this.positions.BULE =
	[
			815, 745
	];
	this.positions.BULS =
	[
			770, 800
	];
	this.positions.FIN =
	[
			744, 219
	];
	this.positions.GRE =
	[
			705, 862
	];
	this.positions.HOL =
	[
			443, 496
	];
	this.positions.DEN =
	[
			518, 415
	];
	this.positions.NWY =
	[
			533, 285
	];
	this.positions.NAF =
	[
			217, 930
	];
	this.positions.POR =
	[
			119, 737
	];
	this.positions.RUM =
	[
			806, 699
	];
	this.positions.SER =
	[
			683, 728
	];
	this.positions.SPA =
	[
			226, 747
	];
	this.positions.SPAN =
	[
			226, 680
	];
	this.positions.SPAS =
	[
			226, 830
	];
	this.positions.SWE =
	[
			605, 338
	];
	this.positions.TUN =
	[
			460, 933
	];
	// Bodies of Water
	this.positions.ADR =
	[
			605, 769
	];
	this.positions.AEG =
	[
			782, 885
	];
	this.positions.BAL =
	[
			637, 418
	];
	this.positions.BAR =
	[
			850, 50
	];
	this.positions.BLA =
	[
			938, 719
	];
	this.positions.EAS =
	[
			881, 939
	];
	this.positions.ENG =
	[
			290, 517
	];
	this.positions.BOT =
	[
			680, 325
	];
	this.positions.GOL =
	[
			403, 771
	];
	this.positions.HEL =
	[
			462, 432
	];
	this.positions.ION =
	[
			642, 923
	];
	this.positions.IRI =
	[
			231, 466
	];
	this.positions.MID =
	[
			68, 612
	];
	this.positions.NAT =
	[
			111, 245
	];
	this.positions.NTH =
	[
			440, 377
	];
	this.positions.NRG =
	[
			470, 154
	];
	this.positions.SKA =
	[
			524, 357
	];
	this.positions.TYN =
	[
			513, 837
	];
	this.positions.WES =
	[
			352, 849
	];

	this.getImageName = function(country, unitType)
	{
		unitType = unitType.toUpperCase();
		country = country.toUpperCase();
		var imageName = "image";
		if (country == "ENG")
		{
			imageName += "England";
		}
		else if (country == "FRA")
		{
			imageName += "France";
		}
		else if (country == "AUS")
		{
			imageName += "Austria";
		}
		else if (country == "ITA")
		{
			imageName += "Italy";
		}
		else if (country == "GER")
		{
			imageName += "Germany";
		}
		else if (country == "RUS")
		{
			imageName += "Russia";
		}
		else if (country == "TUR")
		{
			imageName += "Turkey";
		}
		else
		{
			return;
		}

		if (unitType == "F")
			imageName += "Fleet";
		else if (unitType == "A")
			imageName += "Army";
		else if (unitType == "S")
			imageName += "Supply";
		else
			return;

		return imageName;
	};

	this.getImageUnit = function(country, unitType)
	{
		var imageName = this.getImageName(country, unitType);
		return this[imageName];
	};

	/**
	 * Returns a list of countries (i.e. the keys used to produce the images);
	 */
	this.getCountries = function()
	{
		var ret =
		[
				"TUR", "GER", "AUS", "ITA", "RUS", "ENG", "FRA"
		];
		return ret;
	};

	/**
	 * returns a list of provinces with supply centers for the given country
	 */
	this.getHomeCountrySupplyCenters = function(country)
	{
		var ret = new GList();

		if (country == "ENG")
		{
			ret.add("LON");
			ret.add("EDI");
			ret.add("LVP");
		}
		else if (country == "FRA")
		{
			ret.add("BRE");
			ret.add("PAR");
			ret.add("MAR");
		}
		else if (country == "AUS")
		{
			ret.add("BUD");
			ret.add("VIE");
			ret.add("TRI");
		}
		else if (country == "ITA")
		{
			ret.add("VEN");
			ret.add("ROM");
			ret.add("NAP");
		}
		else if (country == "GER")
		{
			ret.add("MUN");
			ret.add("KIE");
			ret.add("BER");
		}
		else if (country == "RUS")
		{
			ret.add("STP");
			ret.add("MOS");
			ret.add("WAR");
			ret.add("SEV");
		}
		else if (country == "TUR")
		{
			ret.add("ANK");
			ret.add("SMY");
			ret.add("CON");
		}
		return ret.list;
	};

};
