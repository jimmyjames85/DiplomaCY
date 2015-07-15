package edu.iastate.cs309.r16.diplomacy.moves;

import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;



public class Disband extends Action{
	
	private String[] disbandCommandSet;
	
	public Disband(String armyType, String home, String country, String[] commandSet) {
		super(armyType, home, country);
		disbandCommandSet = commandSet;
	}
	
	@Override
	public boolean isPossible(GameMap map) {
		Territory homeTerritory = map.getTerritory(home);
		return homeTerritory != null && super.isPossible(map);
	}
	
	public String[] getCommandSet() {
		return disbandCommandSet;
	}
	
	public String getUnitType(){
		return disbandCommandSet[1];
	}
}
