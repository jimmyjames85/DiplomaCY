package edu.iastate.cs309.r16.diplomacy.moves;

import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;

public class Build extends Action {

	private String[] buildCommandSet;
	
	public Build(String armyType, String home, String country, String[] commandSet) {
		super(armyType, home, country);
		buildCommandSet = commandSet;
	}
	
	@Override
	public boolean isPossible(GameMap map) {
		Territory homeTerritory = map.getTerritory(home);
		return homeTerritory != null && super.isPossible(map) && homeTerritory.isSupply();
	}
	
	public String[] getCommandSet() {
		return buildCommandSet;
	}
	
	public String getUnitType(){
		return buildCommandSet[1];
	}
}
