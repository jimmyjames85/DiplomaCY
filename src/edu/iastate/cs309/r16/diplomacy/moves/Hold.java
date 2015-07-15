package edu.iastate.cs309.r16.diplomacy.moves;

import edu.iastate.cs309.r16.diplomacy.enumeration.Units;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.Unit;

public class Hold extends Action {
	
	private String[] holdCommandSet;
	
	/**
	 * Creates a new hold action
	 * @param armyType
	 * @param home
	 * @param country
	 * @param commandSet
	 */
	public Hold(String armyType, String home, String country, String[] commandSet) {
		super(armyType, home, country);
		holdCommandSet = commandSet;
	}
	
	@Override
	/**
	 * Checks if there is a unit in the territory that can hold
	 * @param map
	 * @return whether the hold action is possible
	 */
	public boolean isPossible(GameMap map) {
		return super.isPossible(map);
	}
	
	/**
	 * 
	 * @return a string array of the hold information
	 */
	public String[] getCommandSet() {
		return holdCommandSet;
	}
}
