package edu.iastate.cs309.r16.diplomacy.moves;

import edu.iastate.cs309.r16.diplomacy.enumeration.Units;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;

public class Support extends Action {

	private String destination;
	private Action supportedMove;
	private Hold cutSupportAction;          //default hold action if the supporting territory is attacked
	private String[] supportCommandSet;
	private String[] cutCommandSet;
	
	/**
	 * Creates a new support action to support a hold action
	 * @param armyType
	 * @param home
	 * @param country
	 * @param destination
	 * @param supportedArmyType
	 * @param commandSet
	 */
	public Support(String armyType, String home, String country, String destination, String supportedArmyType, String[] commandSet) {
		super(armyType, home, country);
		setDestination(destination);
		String[] supportedMoveCommandSet = {supportedArmyType, destination, "H"};
		String[] cutCommandSet = {armyType, home, "H"};
		this.cutCommandSet = cutCommandSet;
		this.supportCommandSet = commandSet;
		cutSupportAction = new Hold(armyType, home, country, cutCommandSet);
		supportedMove = new Hold(supportedArmyType, destination, country, supportedMoveCommandSet);
	}
	
	/**
	 * Creates a new support action to support a move action
	 * @param armyType
	 * @param home
	 * @param country
	 * @param destination
	 * @param supportedArmyType
	 * @param supportedStart
	 * @param commandSet
	 */
	public Support(String armyType, String home, String country, String destination, String supportedArmyType, String supportedStart, String[] commandSet) {
		super(armyType, home, country);
		setDestination(destination);
		String[] supportedMoveCommandSet = {supportedArmyType, supportedStart, "M", destination};
		String[] cutCommandSet = {armyType, home, "H"};
		this.cutCommandSet = cutCommandSet;
		this.supportCommandSet = commandSet;
		cutSupportAction = new Hold(armyType, home, country, cutCommandSet);
		supportedMove = new Move(supportedArmyType, supportedStart, country, destination, supportedMoveCommandSet);
	}
	
	@Override
	/**
	 * Checks if the supported action's destination is adjacent to the support's home territory
	 * @param map
	 * @return whether the action is possible or not
	 */
	public boolean isPossible(GameMap map) {
		
		Territory homeTerritory = map.getTerritory(home);
		return super.isPossible(map) &&
			((this.armyType.equals(Units.ARMY) && (homeTerritory.getArmyNeighbors().contains(map.getTerritory(destination)))) || 
			(this.armyType.equals(Units.FLEET) && homeTerritory.getFleetNeighbors().contains(map.getTerritory(destination))));
	}
	
	/**
	 * Sets the destination of the support
	 * @param destination
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	/**
	 * 
	 * @return the support destination
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * 
	 * @return the action the move is supporting
	 */
	public Action getSupportedMove() {
		return supportedMove;
	}
	
	/**
	 * 
	 * @return the default action for the support action if it is attacked
	 */
	public Hold getCutSupportAction() {
		return cutSupportAction;
	}
	
	/**
	 * 
	 * @return a string array of the support information
	 */
	public String[] getCommandSet() {
		return supportCommandSet;
	}
	
	/**
	 * 
	 * @return a string array of the default action if the support action is attacked
	 */
	public String[] getCutCommandSet() {
		return cutCommandSet;
	}
}
