package edu.iastate.cs309.r16.diplomacy.moves;

import edu.iastate.cs309.r16.diplomacy.enumeration.Units;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;

public class Convoy extends Action {

	private String convoyHome;
	private String convoyDestination;
	private String[] convoyCommandSet;
	private String[] convoyedMoveCommandSet;
	
	public Convoy(String armyType, String home, String country, String convoyHome, String convoyDestination, String[] commandSet) {
		super(armyType, home, country);
		setConvoyHome(convoyHome);
		setConvoyDestination(convoyDestination);
		String[] convoyedMoveCommandSet = {"A", convoyHome, "M", convoyDestination};
		this.convoyedMoveCommandSet = convoyedMoveCommandSet;
		convoyCommandSet = commandSet;
	}
	
	@Override
	public boolean isPossible(GameMap map) {
			return super.isPossible(map) && this.armyType.equals(Units.FLEET);
	}
	
	public void setConvoyHome(String convoyHome) {
		this.convoyHome = convoyHome;
	}
	
	public void setConvoyDestination(String destination) {
		this.convoyDestination = destination;
	}
	
	@Override
	public String getDestination() {
		return home;
	}
	
	public String getConvoyHome() {
		return convoyHome;
	}
	
	public String getConvoyDestination() {
		return convoyDestination;
	}
	
	public String[] getCommandSet() {
		return convoyCommandSet;
	}
	
	public String[] getConvoyedMoveCommandSet() {
		return convoyedMoveCommandSet;
	}
}
