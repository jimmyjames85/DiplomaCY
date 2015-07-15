package edu.iastate.cs309.r16.diplomacy.moves;

import edu.iastate.cs309.r16.diplomacy.enumeration.Units;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.Unit;

public class Move extends Action {

	private String destination;
	private String[] moveCommandSet;
	
	/**
	 * Creates a new move action 
	 * 
	 * @param armyType
	 * @param home
	 * @param country
	 * @param destination
	 * @param commandSet
	 */
	public Move(String armyType, String home, String country, String destination, String[] commandSet) {
		super(armyType, home, country);
		setDestination(destination);
		moveCommandSet = commandSet;
	}
	
	@Override
	/**
	 * Checks if the action destination is adjacent to the home territory
	 * @param map
	 * @return whether the move is possible or not
	 */
	public boolean isPossible(GameMap map) {
		Territory homeTerritory = map.getTerritory(home);
		return super.isPossible(map) &&
			((this.armyType.equals(Units.ARMY) && (homeTerritory.getArmyNeighbors().contains(map.getTerritory(destination)))) || 
			(this.armyType.equals(Units.FLEET) && homeTerritory.getFleetNeighbors().contains(map.getTerritory(destination))) || 
			(this.armyType.equals(Units.ARMY) && homeTerritory.getType().equals("coast") && map.getTerritory(destination).getType().equals("coast")));
	}
	
	/**
	 * Sets the destination for the move
	 * @param destination
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Override
	/**
	 * @return the move destination
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * @return a string array of the move information
	 */
	public String[] getCommandSet() {
		return moveCommandSet;
	}
}
