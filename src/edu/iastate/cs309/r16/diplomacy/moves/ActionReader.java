package edu.iastate.cs309.r16.diplomacy.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.iastate.cs309.r16.diplomacy.enumeration.Units;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.Unit;

public class ActionReader {
	//creates variables to separate the actions as they are evaluated
	private ArrayList<Action> actions;
	private ArrayList<Action> validActions;
	private ArrayList<Action> successful;               
	private ArrayList<Action> unsuccessful;
	private ArrayList<Action> invalidActions;
	private ArrayList<Support> support;
	private ArrayList<Action> pending;
	private ArrayList<Unit> tempUnits;
	
	private GameMap map;
	
	/**
	 * Creates a new ActionReader. It will not do anything unless 
	 * the Action Reader calls the methods addOrders() and evaluateMoves()
	 * @param map
	 */
	public ActionReader(GameMap map) {
		actions = new ArrayList<Action>();
		this.map = map;	
	}
	
	/**
	 * Creates a new ActionReader that gets a list of actions, evaluates them
	 * against each other, and then updates the game map.
	 * @param map
	 * @param actionList
	 */
	public ActionReader(GameMap map, ArrayList<Action> actionList) {
		actions = new ArrayList<Action>();
		this.map = map;
		for(int i = 0; i <= actionList.size() - 1; i++) {
			actions.add(actionList.get(i));
		}
		evaluateMoves();	
	}
	
	/**
	 * Takes the ArrayList of actions created by ActionReader() 
	 * and separates it into validActions and invalidActions.
	 * Invalid actions do not affect the game map or other actions.
	 */
	public void validDestinations() {
		validActions = new ArrayList<Action>();
		invalidActions = new ArrayList<Action>();
		for(int i = 0; i <= actions.size() - 1; i++) {
			if(actions.get(i).isPossible(map)) {
				validActions.add(actions.get(i));
			}
			else {
				invalidActions.add(actions.get(i));
			}
		}
	}
	/**
	 * Evaluates the actions in the validActions ArrayList. First, the actions
	 * that are not affected by other actions and do not affect other actions are
	 * immediately placed into the successful ArrayList. Support actions are then
	 * evaluated to check if they successfully support another action, or if support
	 * is cut. If support is cut, it is unsuccessful and it must defend itself. Each 
	 * action is then evaluated against all other actions it affects. The action with 
	 * the highest power is successful, and all other moves are unsuccessful. The map
	 * is then updated.  
	 * 
	 */
	public void evaluateMoves() {
		validDestinations();
		defaultToHold(map, validActions);
		
		successful = new ArrayList<Action>();
		support = new ArrayList<Support>();
		pending = new ArrayList<Action>();
		tempUnits = new ArrayList<Unit>();
		unsuccessful = new ArrayList<Action>();
		Action temp = null;
		Territory tempTerritory = null;
		boolean chained = false;
		boolean flag = false;
		
		//see method definitions below
		separateOrders(temp, flag);
		evaluateSupports(temp, flag);
		evaluatePower(temp, flag);
		evaluateConvoys(tempTerritory, chained);
		removeAdditions(unsuccessful);
		removeAdditions(successful);
		updateMap();
	}
	
	/**
	 * 
	 * @return successful actions
	 */
	public ArrayList<Action> getSuccessful() {
		return successful;
	}
	
	/**
	 * 
	 * @return unsuccessful actions
	 */
	public ArrayList<Action> getUnsuccessful() {
		return unsuccessful;
	}
	
	/**
	 * 
	 * @return invalid actions
	 */
	public ArrayList<Action> getInvalid() {
		return invalidActions;
	}
	
	/**
	 * Adds an order for a given country.
	 * 
	 * @param country
	 * @param orders
	 */
	public void addOrders(String country, ArrayList<String> orders) {
		for(int i = 0; i <= orders.size() - 1; i++) {
			actions.add(Action.readOrder(country, orders.get(i)));
		}
	}
	
	/**
	 * 
	 * @return game map
	 */
	public GameMap getMap() {
		return map;
	}
	
	/**
	 * If a unit is not given an order, it automatically defaults to a hold action
	 * for the purpose of evaluating it against a move that is attacking it
	 * @param map
	 * @param validActions
	 */
	public void defaultToHold(GameMap map, ArrayList<Action> validActions) {
		boolean isPresent = false;
		for(String curKey : map.getUnits().keySet()) {
			for(int i = 0; i <= validActions.size() - 1; i++) {
				if(curKey.equals(validActions.get(i).getHome())) {
					isPresent = true;
					break;
				}
			}
			if(!isPresent) {
				String order = map.getUnits().get(curKey).getType() + " " + map.getUnits().get(curKey).getTerritory() + " " + "H";
				this.validActions.add(Action.readOrder(map.getUnits().get(curKey).getCountry(), order));
			}
			isPresent = false;
		}
	}
	
	/**
	 * Do an initial evaluation of all the moves, separating them into moves we know are
	 * successful, moves that are still pending, and support moves. The method iterates 
	 * through the valid actions array list and finds the actions that don't interact with 
	 * anything, marking them automatically successful, and the actions that interact with
	 * another actions, marking them as pending.
	 * 
	 * @param temp
	 * @param flag
	 */
	public void separateOrders(Action temp, boolean flag) {
		while(validActions.size() > 0) {
			temp = validActions.get(0);
			validActions.remove(0);
			for(int i = validActions.size() - 1; i >= 0; i--) { //place all conflicting actions into pending
				if((temp.getDestination().equals(validActions.get(i).getDestination())) || 
					(temp.getDestination().equals(validActions.get(i).getHome()) && 
					validActions.get(i).getDestination().equals(temp.getHome())) ||
					(map.getTerritory(temp.getDestination()).getExclusives().contains(map.getTerritory(validActions.get(i).getDestination())))) {
					
					if(validActions.get(i).getClass().equals(Support.class)) {
						support.add((Support) validActions.get(i));
					}
					else {
						pending.add(validActions.get(i));
					}
					validActions.remove(i);
					flag = true;
				}
			}
			if(!flag) {
				if(!temp.getClass().equals(Support.class)) {
					successful.add(temp);
				}
				else {
					unsuccessful.add(temp);
				}
			}
			else {
				if(!temp.getClass().equals(Support.class)) {
					pending.add(temp);
				}
				else {
					support.add((Support) temp);
				}
			}
			flag = false;
		}
	}
	
	/**
	 * This method iterates once through the support array list. On each pass,
	 * it will iterate separately through the successful actions array list, and 
	 * the pending actions array list to find if any other moves could interfere
	 * with the supporting action. If an interference, the support is unsuccessful
	 * and a flag is set. If the flag is not set at the end of each iteration, 
	 * the support action is successful, and the supported action's power is
	 * increased.
	 * 
	 * @param temp
	 * @param flag
	 */
	public void evaluateSupports(Action temp, boolean flag) {
		for(int i = support.size() - 1; i >= 0; i--) { //check if support actions are successful
			for(int j = 0; j <= successful.size() - 1; j++) { //support is cut by a move action
				if(support.get(i).getHome().equals(successful.get(j).getDestination())) {
					pending.add(new Hold(support.get(i).getArmyType(), support.get(i).getHome(), support.get(i).getCountry(), support.get(i).getCutCommandSet()));
					pending.add(successful.get(j));
					successful.remove(j);
					unsuccessful.add(support.get(i));
					flag = true;
					break;
				}
			}
			
			for(int h = 0; h <= pending.size() - 1; h++) { //support is cut by a move action
				if(support.get(i).getHome().equals(pending.get(h).getDestination())) {
					pending.add(new Hold(support.get(i).getArmyType(), support.get(i).getHome(), support.get(i).getCountry(), support.get(i).getCutCommandSet()));
					unsuccessful.add(support.get(i));
					flag = true;
					break;
				}
			}
			
			if(!flag) { //support is successful, increase the supported action's power
				temp = ((Support) support.get(i)).getSupportedMove();
				for(int k = 0; k <= pending.size() - 1; k++) {
					if(Action.isEqualAction(temp, pending.get(k)) || (pending.get(k).getClass().equals(Support.class) && 
					pending.get(k).getDestination().equals(support.get(i).getDestination()))) {
						pending.get(k).increasePower();
						successful.add(support.get(i));
						break;
					}
				}
			}
			flag = false;
		}
	}
	
	/**
	 * This method evaluates each pending action's power against all other actions
	 * that it interacts with. It iterates through the pending action array list and 
	 * checks each action in it against every other action it contains. If they interact,
	 * their powers are compared, and the one with the higher power is marked successful, 
	 * and the lower power is unsuccessful. Ties are broken depending on what the action 
	 * types are.
	 * 
	 * @param temp
	 * @param flag
	 */
	public void evaluatePower(Action temp, boolean flag) {
		while(pending.size() > 0) { 
			temp = pending.get(0);
			pending.remove(0);
			for(int i = pending.size() - 1; i >= 0; i--) {
				if(temp.getDestination().equals(pending.get(i).getDestination()) || 
					(temp.getDestination().equals(pending.get(i).getHome()) && 
					pending.get(i).getDestination().equals(temp.getHome())) ||
					(map.getTerritory(temp.getDestination()).getExclusives().contains(map.getTerritory(pending.get(i).getDestination())))){
					if(temp.getPower() > pending.get(i).getPower()) {
						unsuccessful.add(pending.get(i));
						pending.remove(i);
					}
					else if(temp.getPower() == pending.get(i).getPower()) {
						if(temp.getClass().equals(Hold.class) || temp.getClass().equals(Convoy.class)) {
							unsuccessful.add(pending.get(i));
							pending.remove(i);
							flag = false;
						}
						else if(pending.get(i).getClass().equals(Hold.class) || temp.getClass().equals(Convoy.class)) {
							unsuccessful.add(temp);
							temp = pending.get(i);
							flag = false;
						}
						else {
							unsuccessful.add(pending.get(i));
							unsuccessful.add(temp);
							pending.remove(i);
							flag = true;
						}
					}
					else {
						unsuccessful.add(temp);
						temp = pending.get(i);
						pending.remove(i);
						flag = false;
					}
				}
			}
			if(!flag) {
				successful.add(temp);
			}
			else {
				continue; //bounce
			}
			flag = false;
		}
	}
	
	/**
	 * This method executes once all actions have been marked as either successful or
	 * unsuccessful. It iterates through the successful action array list to find all
	 * successful convoy actions. It then tracks from the convoy origin to the convoy 
	 * destination to see if the origin and destination are connected by convoying units.
	 * If they are, the convoy is successful and the convoyed unit moves to the destination.
	 * If the convoy chain is broken at any point and the origin and destination are not
	 * connected, then the convoy is unsuccessful and the convoyed unit stays where it was.
	 * 
	 * @param tempTerritory
	 * @param chained
	 */
	public void evaluateConvoys(Territory tempTerritory, boolean chained) {
		for(int i = successful.size() - 1; i >= 0; i--) { //check if convoyed actions were interrupted
			if(successful.get(i).getClass().equals(Move.class) && successful.get(i).getArmyType().equals(Units.ARMY) && //check for valid convoys
			!map.getTerritory(successful.get(i).getHome()).getArmyNeighbors().contains(map.getTerritory(successful.get(i).getDestination()))) {
				tempTerritory = map.getTerritory(successful.get(i).getHome());
				chained = true;
				while(chained) {//while the territories are chained together
					for(int j = successful.size() - 1; j >= 0; j--) {
						if(successful.get(j).getClass().equals(Convoy.class) &&
						tempTerritory.getFleetNeighbors().contains(map.getTerritory(successful.get(j).getHome())) && 
						Arrays.equals(successful.get(i).getCommandSet(), ((Convoy) successful.get(j)).getConvoyedMoveCommandSet())) {//territories are chained
							tempTerritory = map.getTerritory(successful.get(j).getHome());//move along the chain
							chained = true;
							break;
						}
						else {
							chained = false;
						}
					}
					if(tempTerritory.getFleetNeighbors().contains(map.getTerritory(successful.get(i).getDestination()))) {//destination reached
						break;
					}
				}
				if(chained) {
					continue;
				}
				else {
					unsuccessful.add(successful.get(i));//convoy was interrupted or not complete
					successful.remove(i);
				}
			}
		}
	}
	
	
	/**
	 * During the evaluation of actions in previous methods, there are
	 * several dummy support actions that get created. This method finds
	 * all those extra actions and removes them, returning to the original
	 * action count.
	 * 
	 * @param remove
	 */
	public void removeAdditions(ArrayList<Action> remove) {
		for(int i = remove.size() - 1; i >= 0; i--) { //remove temporary failed support actions that are displaced
			for(int k = 0; k <= support.size() - 1; k++) {
				if((Hold.class.equals(remove.get(i).getClass())) && (remove.get(i).getHome().equals(support.get(k).getHome()))) {
					remove.remove(i);
					support.remove(k);
					break;
				}
			}
			if(support.size() == 0) {
				break;
			}
		}
	}
	
	/**
	 * This method iterates through both the unsuccessful action array list
	 * and the successful action array list and updates the map accordingly
	 * depending on what type of action it is. Successful holds, supports, 
	 * and convoys remain in place where the unsuccessful ones are displaced.
	 * Successful moves are allowed to move where the unsuccessful ones are
	 * forced to remain where they are, or are displaced if their territory
	 * was conquered. All displaced actions are placed in the displaced action
	 * array list to be used for unit relocation.
	 */
	public void updateMap() {
		for(Action a : unsuccessful) { //update map for displaced hold, move, and convoy actions
			if(Hold.class.equals(a.getClass()) || Convoy.class.equals(a.getClass()))
			{
				Unit tempUnit = map.getUnits().remove(a.getHome());
				tempUnit.setType(tempUnit.getType().toLowerCase());
				map.getDisplaced().put(a.getDestination(),tempUnit);
			}
			else if (Move.class.equals(a.getClass())) {
				for(int i = 0; i <= successful.size() - 1; i++) {
					if(a.getHome().equals(successful.get(i).getDestination())) {
						Unit tempUnit = map.getUnits().remove(a.getHome());
						tempUnit.setType(tempUnit.getType().toLowerCase());
						map.getDisplaced().put(a.getDestination(),tempUnit);
					}
				}
			}
			else {
				for(int i = 0; i <= successful.size() - 1; i++) {
					if(Move.class.equals(successful.get(i).getClass()) && a.getHome().equals(successful.get(i).getDestination())) {
						Unit tempUnit = map.getUnits().remove(a.getHome()); 
						tempUnit.setType(tempUnit.getType().toLowerCase());
						map.getDisplaced().put(a.getDestination(),tempUnit);
					}
				}
			}
		}
		
		
		for(Action a : successful) { //update map for successful moves
			if(Move.class.equals(a.getClass())) {
				tempUnits.add(map.getUnits().remove(a.getHome()));
			}
		}
		int counter = 0;
		for(int i = 0; i <= successful.size() - 1; i++) {
			if(Move.class.equals(successful.get(i).getClass())) {
				map.getUnits().put(successful.get(i).getDestination(), tempUnits.get(counter));
				map.getUnits().get(successful.get(i).getDestination()).setTerritory(successful.get(i).getDestination());
				counter++;
			}
		}
	}
}


