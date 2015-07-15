package edu.iastate.cs309.r16.diplomacy.map;

import java.util.LinkedList;
import java.util.List;

public class Territory {
	private String name;
	private String country;
	private boolean supply;
	private String type;
	private boolean passableArmy;
	private boolean passableFleet;
	private List<Territory> armyNeighbors;
	private List<Territory> fleetNeighbors;
	private List<Territory> exclusives;
	
	public Territory()
	{
		setName("");
		setCountry("");
		setSupply(false);
		setType("");
		setPassableArmy(false);
		setPassableFleet(false);
		setArmyNeighbors(new LinkedList<Territory>());
		setFleetNeighbors(new LinkedList<Territory>());
		setExclusives(new LinkedList<Territory>());
	}

	/**
	 * @return the exclusives
	 */
	public List<Territory> getExclusives() {
		return exclusives;
	}

	/**
	 * @param exclusives the exclusives to set
	 */
	public void setExclusives(List<Territory> exclusives) {
		this.exclusives = exclusives;
	}

	/**
	 * @return the passableFleet
	 */
	public boolean isPassableFleet() {
		return passableFleet;
	}

	/**
	 * @param passableFleet the passableFleet to set
	 */
	public void setPassableFleet(boolean passableFleet) {
		this.passableFleet = passableFleet;
	}

	/**
	 * @return the passableArmy
	 */
	public boolean isPassableArmy() {
		return passableArmy;
	}

	/**
	 * @param passableArmy the passableArmy to set
	 */
	public void setPassableArmy(boolean passableArmy) {
		this.passableArmy = passableArmy;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the supply
	 */
	public boolean isSupply() {
		return supply;
	}

	/**
	 * @param supply the supply to set
	 */
	public void setSupply(boolean supply) {
		this.supply = supply;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fleetNeighbors
	 */
	public List<Territory> getFleetNeighbors() {
		return fleetNeighbors;
	}

	/**
	 * @param fleetNeighbors the fleetNeighbors to set
	 */
	public void setFleetNeighbors(List<Territory> fleetNeighbors) {
		this.fleetNeighbors = fleetNeighbors;
	}

	/**
	 * @return the armyNeighbors
	 */
	public List<Territory> getArmyNeighbors() {
		return armyNeighbors;
	}

	/**
	 * @param armyNeighbors the armyNeighbors to set
	 */
	public void setArmyNeighbors(List<Territory> armyNeighbors) {
		this.armyNeighbors = armyNeighbors;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
