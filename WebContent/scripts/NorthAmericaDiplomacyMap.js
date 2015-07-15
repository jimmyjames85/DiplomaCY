/**
 * This Object/Class loads the images and map associated with the original
 * Diplomacy Game Map and pieces. Once the images have been loaded, a call to
 * callback is made.
 */
var NorthAmericaDiplomacyMap = function(callback) {
	var totalImagesLoaded = 0;

	/**
	 * This counts the number of images loaded. Once the sum reaches the total
	 * number of images we then make a call to callback indicating all the
	 * images have been pre-loaded
	 */
	this.waitForLoad = function() {
		totalImagesLoaded++;
		if (totalImagesLoaded == 30) {
			callback();
		}
	};

	this.getImageMap = function() {
		return this.imageMap;
	};

	this.unitScaleFactor = 0.1;

	this.imageMap = new Image();
	this.imageMap.src = '../images/NorthAmericaMap/NorthAmericaGameMap.png';
	this.imageMap.addEventListener("load", this.waitForLoad, false);

	this.imageBCFleet = new Image();
	this.imageBCFleet.src = "../images/NorthAmericaMap/fleet_bc.png";
	this.imageBCFleet.addEventListener("load", this.waitForLoad, false);

	this.imageBCArmy = new Image();
	this.imageBCArmy.src = "../images/NorthAmericaMap/army_bc.png";
	this.imageBCArmy.addEventListener("load", this.waitForLoad, false);

	this.imageBCSupply = new Image();
	this.imageBCSupply.src = "../images/NorthAmericaMap/supply_bc.png";
	this.imageBCSupply.addEventListener("load", this.waitForLoad, false);

	this.imageCaliforniaFleet = new Image();
	this.imageCaliforniaFleet.src = "../images/NorthAmericaMap/fleet_california.png";
	this.imageCaliforniaFleet.addEventListener("load", this.waitForLoad, false);

	this.imageCaliforniaArmy = new Image();
	this.imageCaliforniaArmy.src = "../images/NorthAmericaMap/army_california.png";
	this.imageCaliforniaArmy.addEventListener("load", this.waitForLoad, false);

	this.imageCaliforniaSupply = new Image();
	this.imageCaliforniaSupply.src = "../images/NorthAmericaMap/supply_california.png";
	this.imageCaliforniaSupply
			.addEventListener("load", this.waitForLoad, false);

	this.imageMexicoFleet = new Image();
	this.imageMexicoFleet.src = "../images/NorthAmericaMap/fleet_mexico.png";
	this.imageMexicoFleet.addEventListener("load", this.waitForLoad, false);

	this.imageMexicoArmy = new Image();
	this.imageMexicoArmy.src = "../images/NorthAmericaMap/army_mexico.png";
	this.imageMexicoArmy.addEventListener("load", this.waitForLoad, false);

	this.imageMexicoSupply = new Image();
	this.imageMexicoSupply.src = "../images/NorthAmericaMap/supply_mexico.png";
	this.imageMexicoSupply.addEventListener("load", this.waitForLoad, false);

	this.imageFloridaFleet = new Image();
	this.imageFloridaFleet.src = "../images/NorthAmericaMap/fleet_florida.png";
	this.imageFloridaFleet.addEventListener("load", this.waitForLoad, false);

	this.imageFloridaArmy = new Image();
	this.imageFloridaArmy.src = "../images/NorthAmericaMap/army_florida.png";
	this.imageFloridaArmy.addEventListener("load", this.waitForLoad, false);

	this.imageFloridaSupply = new Image();
	this.imageFloridaSupply.src = "../images/NorthAmericaMap/supply_florida.png";
	this.imageFloridaSupply.addEventListener("load", this.waitForLoad, false);

	this.imageHeartlandFleet = new Image();
	this.imageHeartlandFleet.src = "../images/NorthAmericaMap/fleet_heartland.png";
	this.imageHeartlandFleet.addEventListener("load", this.waitForLoad, false);

	this.imageHeartlandArmy = new Image();
	this.imageHeartlandArmy.src = "../images/NorthAmericaMap/army_heartland.png";
	this.imageHeartlandArmy.addEventListener("load", this.waitForLoad, false);

	this.imageHeartlandSupply = new Image();
	this.imageHeartlandSupply.src = "../images/NorthAmericaMap/supply_heartland.png";
	this.imageHeartlandSupply.addEventListener("load", this.waitForLoad, false);

	this.imageNewYorkFleet = new Image();
	this.imageNewYorkFleet.src = "../images/NorthAmericaMap/fleet_newyork.png";
	this.imageNewYorkFleet.addEventListener("load", this.waitForLoad, false);

	this.imageNewYorkArmy = new Image();
	this.imageNewYorkArmy.src = "../images/NorthAmericaMap/army_newyork.png";
	this.imageNewYorkArmy.addEventListener("load", this.waitForLoad, false);

	this.imageNewYorkSupply = new Image();
	this.imageNewYorkSupply.src = "../images/NorthAmericaMap/supply_newyork.png";
	this.imageNewYorkSupply.addEventListener("load", this.waitForLoad, false);

	this.imageQuebecFleet = new Image();
	this.imageQuebecFleet.src = "../images/NorthAmericaMap/fleet_quebec.png";
	this.imageQuebecFleet.addEventListener("load", this.waitForLoad, false);

	this.imageQuebecArmy = new Image();
	this.imageQuebecArmy.src = "../images/NorthAmericaMap/army_quebec.png";
	this.imageQuebecArmy.addEventListener("load", this.waitForLoad, false);

	this.imageQuebecSupply = new Image();
	this.imageQuebecSupply.src = "../images/NorthAmericaMap/supply_quebec.png";
	this.imageQuebecSupply.addEventListener("load", this.waitForLoad, false);

	this.imagePeruFleet = new Image();
	this.imagePeruFleet.src = "../images/NorthAmericaMap/fleet_peru.png";
	this.imagePeruFleet.addEventListener("load", this.waitForLoad, false);

	this.imagePeruArmy = new Image();
	this.imagePeruArmy.src = "../images/NorthAmericaMap/army_peru.png";
	this.imagePeruArmy.addEventListener("load", this.waitForLoad, false);

	this.imagePeruSupply = new Image();
	this.imagePeruSupply.src = "../images/NorthAmericaMap/supply_peru.png";
	this.imagePeruSupply.addEventListener("load", this.waitForLoad, false);

	this.imageTexasFleet = new Image();
	this.imageTexasFleet.src = "../images/NorthAmericaMap/fleet_texas.png";
	this.imageTexasFleet.addEventListener("load", this.waitForLoad, false);

	this.imageTexasArmy = new Image();
	this.imageTexasArmy.src = "../images/NorthAmericaMap/army_texas.png";
	this.imageTexasArmy.addEventListener("load", this.waitForLoad, false);

	this.imageTexasSupply = new Image();
	this.imageTexasSupply.src = "../images/NorthAmericaMap/supply_texas.png";
	this.imageTexasSupply.addEventListener("load", this.waitForLoad, false);

	this.imageCubaFleet = new Image();
	this.imageCubaFleet.src = "../images/NorthAmericaMap/fleet_cuba.png";
	this.imageCubaFleet.addEventListener("load", this.waitForLoad, false);

	this.imageCubaArmy = new Image();
	this.imageCubaArmy.src = "../images/NorthAmericaMap/army_cuba.png";
	this.imageCubaArmy.addEventListener("load", this.waitForLoad, false);

	this.imageCubaSupply = new Image();
	this.imageCubaSupply.src = "../images/NorthAmericaMap/supply_cuba.png";
	this.imageCubaSupply.addEventListener("load", this.waitForLoad, false);

	this.positions = new Object();

	// returns the pixel position of countries in NorthAmericaGameMap.png
	// Austria

	// BRC
	this.positions.ANC = [ 140, 163 ];
	this.positions.YUK = [ 196, 179 ];
	this.positions.NBC = [ 209, 268 ];
	this.positions.VAN = [ 204, 310 ];
	this.positions.CGY = [ 262, 322 ];

	// QUE
	this.positions.UNG = [ 488, 277 ];
	this.positions.COT = [ 527, 298 ];
	this.positions.QUE = [ 521, 335 ];
	this.positions.ABI = [ 488, 332 ];
	this.positions.MON = [ 502, 359 ];
	this.positions.BEA = [ 540, 353 ];
	this.positions.GAS = [ 554, 314 ];

	// NYK
	this.positions.NYS = [ 524, 387 ];
	this.positions.NYC = [ 546, 396 ];
	this.positions.WPE = [ 506, 419 ];
	this.positions.PHI = [ 524, 409 ];
	this.positions.NJE = [ 543, 410 ];

	// HRT
	this.positions.MIN = [ 385, 396 ];
	this.positions.IOW = [ 396, 432 ];
	this.positions.MIL = [ 417, 407 ];
	this.positions.CHI = [ 429, 436 ];
	this.positions.IND = [ 456, 440 ];

	// FLA
	this.positions.FPA = [ 485, 528 ];
	this.positions.JAC = [ 518, 521 ];
	this.positions.TAM = [ 513, 532 ];
	this.positions.MIA = [ 532, 545 ];

	// CUB
	this.positions.HAV = [ 536, 580 ];
	this.positions.CAM = [ 568, 578 ];
	this.positions.HOL = [ 600, 579 ];
	this.positions.KIN = [ 592, 604 ];

	// PER
	this.positions.GUJ = [ 652, 654 ];
	this.positions.VIC = [ 688, 676 ];
	this.positions.ANT = [ 652, 681 ];
	this.positions.CAL = [ 645, 707 ];
	this.positions.BOG = [ 680, 712 ];
	this.positions.ECU = [ 631, 750 ];
	this.positions.LIM = [ 674, 775 ];

	// MEX
	this.positions.GUA = [ 315, 626 ];
	this.positions.POT = [ 352, 613 ];
	this.positions.MEX = [ 350, 638 ];
	this.positions.GUE = [ 340, 652 ];
	this.positions.OAX = [ 374, 661 ];
	this.positions.VER = [ 389, 642 ];

	// TEX
	this.positions.DAL = [ 366, 521 ];
	this.positions.WTE = [ 326, 539 ];
	this.positions.SAN = [ 367, 565 ];
	this.positions.HOU = [ 388, 545 ];

	// CAL
	this.positions.SFR = [ 179, 445 ];
	this.positions.LOS = [ 195, 481 ];
	this.positions.SDI = [ 212, 502 ];

	// NRTH AND EAST
	this.positions.GRE = [ 544, 100 ];
	this.positions.NUN = [ 363, 193 ];
	this.positions.NWT = [ 262, 210 ];
	this.positions.SAS = [ 310, 311 ];
	this.positions.MAN = [ 361, 353 ];
	this.positions.NON = [ 421, 323 ];
	this.positions.WON = [ 403, 353 ];
	this.positions.ONT = [ 492, 379 ];
	this.positions.LAB = [ 557, 252 ];
	this.positions.NFL = [ 614, 273 ];
	this.positions.NBR = [ 566, 329 ];
	this.positions.NSC = [ 586, 338 ];
	this.positions.MAI = [ 558, 349 ];
	this.positions.VEM = [ 543, 371 ];
	this.positions.MAS = [ 557, 384 ];
	this.positions.UPP = [ 431, 384 ];
	this.positions.MIC = [ 465, 409 ];
	this.positions.OHI = [ 481, 430 ];
	this.positions.WVI = [ 501, 444 ];
	this.positions.WDC = [ 535, 428 ];
	this.positions.VIR = [ 524, 445 ];
	this.positions.KEN = [ 470, 461 ];
	this.positions.TEN = [ 448, 484 ];
	this.positions.NCA = [ 523, 465 ];
	this.positions.SCA = [ 516, 487 ];
	this.positions.GEO = [ 494, 502 ];
	this.positions.DSO = [ 450, 510 ];

	// MID TO WEST
	this.positions.WAS = [ 203, 367 ];
	this.positions.ORE = [ 193, 397 ];
	this.positions.IDA = [ 242, 413 ];
	this.positions.MTA = [ 283, 386 ];
	this.positions.DAK = [ 344, 406 ];
	this.positions.WYO = [ 292, 424 ];
	this.positions.NEV = [ 216, 449 ];
	this.positions.UTA = [ 257, 459 ];
	this.positions.COL = [ 308, 459 ];
	this.positions.NEB = [ 352, 441 ];
	this.positions.KAN = [ 376, 471 ];
	this.positions.MIS = [ 415, 466 ];
	this.positions.ARI = [ 247, 511 ];
	this.positions.NME = [ 300, 509 ];
	this.positions.OKL = [ 376, 497 ];
	this.positions.ARK = [ 413, 498 ];
	this.positions.LOU = [ 434, 539 ];

	// SOUTH
	this.positions.BAJ = [ 218, 537 ];
	this.positions.CHH = [ 296, 570 ];
	this.positions.COA = [ 332, 575 ];
	this.positions.DUR = [ 301, 595 ];
	this.positions.NLE = [ 359, 586 ];
	this.positions.YUC = [ 469, 619 ];
	this.positions.TAB = [ 440, 652 ];
	this.positions.CHP = [ 422, 662 ];
	this.positions.GUT = [ 460, 668 ];
	this.positions.ELS = [ 482, 672 ];
	this.positions.HON = [ 502, 662 ];
	this.positions.NIC = [ 522, 679 ];
	this.positions.CRI = [ 545, 693 ];
	this.positions.PAN = [ 606, 677 ];
	this.positions.HAW = [ 122, 673 ];
	this.positions.VEN = [ 690, 628 ];
	this.positions.DOM = [ 652, 567 ];
	this.positions.HAI = [ 638, 578 ];

	// NORTH AND EAST WATER
	this.positions.BER = [ 35, 113 ];
	this.positions.ARO = [ 235, 61 ];
	this.positions.BEF = [ 270, 120 ];
	this.positions.BAB = [ 436, 98 ];
	this.positions.SOL = [ 552, 167 ];
	this.positions.HUB = [ 404, 242 ];
	this.positions.NAO = [ 649, 145 ];
	this.positions.GSL = [ 586, 302 ];
	this.positions.MAB = [ 610, 361 ];
	this.positions.MAO = [ 675, 369 ];
	this.positions.MAY = [ 596, 417 ];
	this.positions.CHB = [ 579, 450 ];
	this.positions.SOS = [ 681, 473 ];
	this.positions.BET = [ 629, 514 ];
	this.positions.ECO = [ 588, 484 ];
	this.positions.SOF = [ 555, 564 ];
	this.positions.APB = [ 481, 551 ];
	this.positions.GOM = [ 424, 578 ];
	this.positions.GOC = [ 414, 626 ];
	this.positions.SOY = [ 500, 598 ];
	this.positions.GOH = [ 503, 632 ];
	this.positions.WCS = [ 548, 627 ];
	this.positions.CAT = [ 608, 591 ];
	this.positions.ECS = [ 635, 600 ];
	this.positions.LES = [ 685, 584 ];
	this.positions.SCS = [ 629, 637 ];
	this.positions.GMO = [ 568, 672 ];

	// SOUTH AND WEST WATER
	this.positions.GOA = [ 78, 228 ];
	this.positions.QCS = [ 136, 288 ];
	this.positions.SJF = [ 153, 356 ];
	this.positions.WCO = [ 136, 443 ];
	this.positions.GSC = [ 175, 513 ];
	this.positions.NPO = [ 58, 518 ];
	this.positions.SWP = [ 38, 709 ];
	this.positions.MPO = [ 196, 622 ];
	this.positions.GCA = [ 256, 621 ];
	this.positions.SPO = [ 239, 738 ];
	this.positions.COM = [ 303, 688 ];
	this.positions.GOT = [ 422, 689 ];
	this.positions.GAL = [ 473, 789 ];
	this.positions.GOF = [ 487, 702 ];
	this.positions.COB = [ 533, 712 ];
	this.positions.GOP = [ 606, 705 ];
	this.positions.GOG = [ 580, 746 ];

	// GREAT LAKES
	this.positions.LSU = [ 430, 367 ];
	this.positions.LMI = [ 443, 412 ];
	this.positions.LHU = [ 468, 387 ];
	this.positions.LER = [ 486, 412 ];
	this.positions.LON = [ 504, 389 ];

	// COASTS
	this.positions.GUTE = [ 481, 646 ];
	this.positions.GUTS = [ 450, 672 ];
	this.positions.HONN = [ 518, 649 ];
	this.positions.HONS = [ 497, 671 ];
	this.positions.CRIN = [ 549, 685 ];
	this.positions.CRIS = [ 542, 695 ];

	this.getImageName = function(country, unitType) {
		unitType = unitType.toUpperCase();
		country = country.toUpperCase();
		var imageName = "image";
		if (country == "BRC") {
			imageName += "BC";
		} else if (country == "CAL") {
			imageName += "California";
		} else if (country == "MEX") {
			imageName += "Mexico";
		} else if (country == "FLO") {
			imageName += "Florida";
		} else if (country == "H") {
			imageName += "Heartland";
		} else if (country == "NEW") {
			imageName += "NewYork";
		} else if (country == "QUE") {
			imageName += "Quebec";
		} else if (country == "PER") {
			imageName += "Peru";
		} else if (country == "TEX") {
			imageName += "Texas";
		} else if (country == "CUB") {
			imageName += "Cuba";
		} else {
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

	this.getImageUnit = function(country, unitType) {
		var imageName = this.getImageName(country, unitType);
		return this[imageName];
	};

	/**
	 * Returns a list of countries (i.e. the keys used to produce the images);
	 */
	this.getCountries = function() {
		var ret = [ "BRC", "CAL", "MEX", "FLO", "HTL", "NEW", "QUE", "PER",
				"TEX", "CUB" ];
		return ret;
	};

	/**
	 * returns a list of provinces with supply centers for the given country
	 */
	this.getHomeCountrySupplyCenters = function(country) {
		var ret = new GList();

		if (country == "BRC") {
			ret.add("ANC");
			ret.add("VAN");
			ret.add("CGY");
		} else if (country == "CAL") {
			ret.add("SFR");
			ret.add("LOS");
			ret.add("SDI");
		} else if (country == "MEX") {
			ret.add("VER");
			ret.add("MEX");
			ret.add("GUA");
		} else if (country == "FLO") {
			ret.add("TAM");
			ret.add("JAC");
			ret.add("MIA");
		} else if (country == "HTL") {
			ret.add("CHI");
			ret.add("MIL");
			ret.add("MIN");
		} else if (country == "NEW") {
			ret.add("NYC");
			ret.add("PHI");
			ret.add("NJE");
		} else if (country == "QUE") {
			ret.add("UNG");
			ret.add("QUE");
			ret.add("MON");
		} else if (country == "PER") {
			ret.add("LIM");
			ret.add("BOG");
			ret.add("CAL");
		} else if (country == "TEX") {
			ret.add("SAN");
			ret.add("HOU");
			ret.add("DAL");
		} else if (country == "CUB") {
			ret.add("HAV");
			ret.add("KIN");
			ret.add("HOL");
		}
		return ret.list;
	};

};
/**
 * 
 */
