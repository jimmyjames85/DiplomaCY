package edu.iastate.cs309.r16.diplomacy.moves;
/*
 * moveId
 * unique Long for Identification
 * 
 * moveText
 * the move in textual representation
 * "A RUH H" : Army in Ruhr Hold
 * "F LON M ENG" : Fleet in London move to English Channel
 * "A GRE S F BUS H" : Army in Greece support Fleet in Bulgaria South Cost Hold
 * "F ION C TUN M GRE" : Fleet in Ionian Sea convoy Tunis to Greece
 * 
 * evaluation
 * enum for how the move was evaluated, not evaluated, successful, failed, invalid
 * 
 * postedDate
 * the date the move was posted to the server
 */
import java.util.Arrays;

import edu.iastate.cs309.r16.diplomacy.enumeration.*;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.Unit;
/**
 * 
 * @author Ian Abbott
 * Stores unit data and determines which actions are valid. 
 */
public abstract class Action {
	protected String[] unitData;
	protected String armyType;
	protected String home;
	protected static String[] commandSet;
	private String country;
	private int power = 1;
	private Long moveId;
	
	/**
	 * Creates a new empty action
	 */
	public Action() {
		setArmyType("");
		setHome("");
		setCountry("");
		setUnitData("", "", "");
	}
	
	/** 
	 * Creates a new action
	 * @param armyType
	 * @param home
	 * @param country
	 */
	public Action(String armyType, String home, String country) {
		setArmyType(armyType);
		setHome(home);
		setCountry(country);
		setUnitData(armyType, home, country);
	}
	
	/*
	 * commandSets[]
	 * ------------
	 * HOLD - A BER H
	 * MOVE - A BER M KIE
	 * SUPPORT - A BER S A KIE H or A BER S A KIE M RUH
	 * CONVOY - F ENG C A WAL M PIC
	 * BUILD - B A BER
	 * DISBAND - D A BER
	 */
	
	/**
	 * Reads a string creates the appropriate action
	 * @param country
	 * @param order
	 * @return the action corresponding to the order
	 */
	public static Action readOrder(String country, String order) {
		commandSet = order.split(" ");
		if("H".equals(commandSet[2])) {
			Hold hold = new Hold(commandSet[0], commandSet[1], country, commandSet);
			return hold;
		}
		else if("M".equals(commandSet[2])) {
			Move move = new Move(commandSet[0], commandSet[1], country, commandSet[3], commandSet);
			return move;
		}
		else if("C".equals(commandSet[2])) {
			Convoy convoy = new Convoy(commandSet[0], commandSet[1], country, commandSet[4], commandSet[6], commandSet);
			return convoy;
		}
		else if("S".equals(commandSet[2]) && "H".equals(commandSet[5])) {
			Support support = new Support(commandSet[0], commandSet[1], country, commandSet[4], commandSet[3], commandSet);
			return support;
		}
		else if("S".equals(commandSet[2]) && "M".equals(commandSet[5])) {
			Support support = new Support(commandSet[0], commandSet[1], country, commandSet[6], commandSet[3], commandSet[4], commandSet);
			return support;
		}
		else if("B".equals(commandSet[0])) {
			Build build = new Build(commandSet[1], commandSet[2], country, commandSet);
			return build;
		}
		else if("D".equals(commandSet[0])) {
			Disband disband = new Disband(commandSet[1], commandSet[2], country, commandSet);
			return disband;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Sets the army type of an action
	 * @param armyType
	 */
	public void setArmyType(String armyType) {
		this.armyType = armyType;
	}
	
	/**
	 * Sets the home territory of an action
	 * @param home
	 */
	public void setHome(String home) {
		this.home = home;
	}
	
	/**
	 * Sets the country that gave the action
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Stores the unit information in a string array
	 * @param armyType
	 * @param home
	 * @param country
	 */
	public void setUnitData(String armyType, String home, String country) {
		unitData = new String[10];
		unitData[0] = armyType;
		unitData[1] = home;
		unitData[2] = country;
	}
	
	/**
	 * 
	 * @return the army type doing the action
	 */
	public String getArmyType() {
		return armyType;
	}
	
	/**
	 * 
	 * @return the home territory of the action
	 */
	public String getHome() {
		return home;
	}
	
	/**
	 * 
	 * @return the country giving the action
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * 
	 * @return the destination of the action
	 */
	public String getDestination() {
		return home;
	}
	
	/**
	 * 
	 * @return a string array of the unit information
	 */
	public String getUnitData() {
		return "Army Type: " + unitData[0] + " Home: " + unitData[1] + " Country: " + unitData[2];
	}
	
	/**
	 * 
	 * @return the attacking/defending/supporting power of the action
	 */
	public int getPower() {
		return power;
	}
	
	/**
	 * 
	 * @return a string array of the action information
	 */
	public String[] getCommandSet() {
		return commandSet;
	}
	/**
	 * Checks if it is possible for an action to have a successful outcome
	 * @param map
	 * @return whether the action is possible or not
	 */
	public boolean isPossible(GameMap map) {
		Unit possibleUnit = map.getUnit(this.home);
		return possibleUnit != null &&
			this.country.equals(possibleUnit.getCountry()) && 
			this.armyType.equals(possibleUnit.getType());
	}
	
	/**
	 * Increases an action's attacking/defending/supporting power
	 */
	public void increasePower() {
		power += 1;
	}
	
	/**
	 * Checks if one action is equal to another
	 * @param actionOne
	 * @param actionTwo
	 * @return whether the given actions are equal
	 */
	public static boolean isEqualAction(Action actionOne, Action actionTwo ) {
		return Arrays.equals(actionOne.getCommandSet(), actionTwo.getCommandSet());
	}
	
	/**
	 * 
	 * @return the identification number of the action
	 */
	public Long getMoveId(){
		return this.moveId;
	}
	
	/**
	 * Sets the identification number of the action
	 * @param moveId
	 */
	public void setMoveId(Long moveId){
		this.moveId = moveId;
	}
}
