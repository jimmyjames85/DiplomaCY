package edu.iastate.cs309.r16.diplomacy.games;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs309.r16.diplomacy.enumeration.Countries;
import edu.iastate.cs309.r16.diplomacy.enumeration.Units;
import edu.iastate.cs309.r16.diplomacy.jobs.HelloJob;
import edu.iastate.cs309.r16.diplomacy.jobs.ProcessTurnJob;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.GameMapSerializer;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.TerritorySerializer;
import edu.iastate.cs309.r16.diplomacy.map.Unit;
import edu.iastate.cs309.r16.diplomacy.moves.Action;
import edu.iastate.cs309.r16.diplomacy.moves.ActionReader;
import edu.iastate.cs309.r16.diplomacy.moves.Build;
import edu.iastate.cs309.r16.diplomacy.moves.Disband;
import edu.iastate.cs309.r16.diplomacy.moves.Move;
import edu.iastate.cs309.r16.diplomacy.users.User;
import edu.iastate.cs309.r16.diplomacy.users.session.SessionId;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;

public class GameManager
{
	
	public static final String GAMES_TABLE = "games";
	public static final String GAMES_TABLE_GAME_ID = "id";

	public static final String MOVES_TABLE = "moves";
	public static final String MOVES_TABLE_GAME_ID = "gameId";
	
	public static final int PROCESS_AT_MIDNIGHT = -1;

	private static Logger LOG = LoggerFactory.getLogger(GameManager.class);
	
	/**
	 * gets the country string for the given player username and gameId
	 * @param player the username string of the player in question
	 * @param gameId the integer of the gameId
	 * @return the country string the player is playing in the given game
	 */
	public static String getCountry(String player, long gameId)
	{
		String jsonUsers = (String) DBAccessor.sendQuery("select users from games "
				+ "where id=?","" + gameId).get(0).get("users");
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> users;
		try {
			users = mapper.readValue(jsonUsers, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String ret = "";
		for(String key: users.keySet())
		{
			if(player.equals(users.get(key)))
			{
				ret = key;
				break;
			}
		}
		return ret;
	}

	/**
	 * gets the username associated with the session id
	 * @param sessionId the given sessionId
	 * @return a string of the username associated with the sessionId or an empty string if it doesn't exist
	 */
	public static String getUserName(String sessionId)
	{
		List<Map<String,Object>> user = 
				DBAccessor.sendQuery("select username from sessions where sessionId=?;", sessionId);
		if(user == null || user.size() < 1)
			return "";
		return (String) user.get(0).get("username");
	}
	
	/**
	 * puts an action on the moves table, it does not validate the action 
	 * as to whether it is possible or a valid action string
	 * @param country the country string of the player submitting the action
	 * @param move the action string
	 * @param gameId the integer identifying the game
	 */
	public static void submitMoveOrder(String country, String move, long gameId, int stateNumber)
	{
		DBAccessor.sendQuery("insert into moves "
			+ "(gameId,country,evaluation,moveText,stateNumber,date)"
			+ "values (?,?,?,?,?,?);", 
			"" + gameId, country,"pending",move, "" + stateNumber,"" + System.currentTimeMillis());
	}
	
	/**
	 * gets the largest state number for the gamestate table, or the current state number
	 * @param gameId the unique integer id of the game
	 * @return the current state integer
	 */
	public static int getCurrentStateNumber(long gameId)
	{
		return (int) DBAccessor.sendQuery("select "
				+ "max(stateNumber) from gamestate where "
				+ "gameId=?;", "" + gameId).get(0).get("max(stateNumber)");
	}
	/**
	 * gets the json representation of the given gamesate that is stored on the table
	 * @param gameId the integer identifying the game
	 * @param stateNumber the integer identifying the state
	 * @return the json string of the map
	 */
	public static String getJsonGameMapForStateId(long gameId, int stateNumber)
	{
		return (String) DBAccessor.sendQuery("select board from gamestate "
				+ "where gameId=? and stateNumber=?;", "" + gameId, "" + stateNumber).get(0).get("board");
	}
	
	/**
	 * creates a gameMap object for the given state number
	 * @param gameId the integer identifying the game
	 * @param stateNumber the integer identifying the state
	 * @return a gamestate object representing the given state
	 */
	public static GameMap getGameMapForStateId(long gameId, int stateNumber)
	{
		String boardType = getBoardType(gameId);
		GameMap ret = null;
		if("EUROPE".equalsIgnoreCase(boardType))
		{
			ret = new GameMap("../resources/territories.xml");
		}
		else if("NORTHAMERICA".equalsIgnoreCase(boardType))
		{
			ret = new GameMap("../resources/territories_northamerica.xml");
		}
		else
		{
			return null;
		}
		String board = getJsonGameMapForStateId(gameId, stateNumber);
		ret.createFromBoard(board);
		return ret;
	}
	
	/**
	 * gets the type of board for the game
	 * @param gameId the integer identifying the game
	 * @return a string representing the board type
	 */
	public static String getBoardType(long gameId)
	{
		//TODO check this against the new db
		return (String) DBAccessor.sendQuery("select boardType from games "
				+ "where id=?;", "" + gameId).get(0).get("boardType");
	}
	
	/**
	 * puts the given gameMap on the state table as the given state
	 * @param gMap the gameMap to put on the table
	 * @param gameId the integer identifying the game
	 * @param stateNumber the integer identifying the state
	 */
	public static void putGameMapForStateId(GameMap gMap, long gameId, int stateNumber)
	{
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleModule module = new SimpleModule("GameMap", new Version(1, 0, 0, null));
		module.addSerializer(GameMap.class, new GameMapSerializer());
		mapper.registerModule(module);
		String map;
		try {
			map = mapper.writeValueAsString(gMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		DBAccessor.sendQuery("insert into gamestate (gameId,stateNumber,board) "
				+ "value (?,?,?);", "" + gameId, "" + stateNumber, map);
	}
	
	/**
	 * gets all of the pending actions from the moves table for the given game
	 * @param gameId the integer identifying the game
	 * @return a list of strings
	 */
	public static List<Map<String,Object>> getPendingMoves(int gameId)
	{
		//front end assumes the list is ascending
		List<Map<String,Object>> moves = DBAccessor.sendQuery("select * from moves "
				+ "where gameId=? and evaluation=? "
				+ "order by date asc;","" + gameId,"pending");
		return moves;
	}
	
	public static List<Map<String,Object>> getSubmittedMovesForStatenumber(int gameId, long stateNumber)
	{
		//front end assumes the list is ascending
		List<Map<String,Object>> moves = DBAccessor.sendQuery("select * from moves "
				+ "where gameId=? and stateNumber=? "
				+ "order by date asc;","" + gameId,"" + stateNumber);
		return moves;
	}	
	
	public static List<Map<String,Object>> getSubmitMoveStatus(int gameId)
	{
		List<Map<String,Object>> moves = DBAccessor.sendQuery("select distinct country from moves "
				+ "where gameId=? and evaluation=? "
				+ "order by date asc;","" + gameId,"pending");
		return moves;		
	}
	
	/**
	 * Registers a job to process a turn with the quartz scheduler
	 * @param gameId the gameId of the game to process
	 * @param repeatMinutes the interval (in minutes) between turn processing
	 * 	set this to PROCESS_AT_MIDNIGHT to have the turn process at midnight
	 * @param graceMinutes the interval (in minutes) until the first turn process
	 */
	public static void registerGameTrigger(long gameId, int repeatMinutes, int graceMinutes)
	{
		LOG.debug("starting " + gameId);
		JobDetail job = newJob(ProcessTurnJob.class)
			    .withIdentity("" + gameId , "GameJobs")
			    .usingJobData("gameId", gameId)
			    .build();
		
		Trigger trigger = null;
		
		if(repeatMinutes == PROCESS_AT_MIDNIGHT)
		{
			trigger = newTrigger()
				    .withIdentity("" + gameId, "GameJobs")
				    .withSchedule(cronSchedule("0 0 0 * * ?") //fire every midnight forever
				    		.withMisfireHandlingInstructionFireAndProceed())
				    .build();
		}
		else
		{
		trigger = newTrigger()
			    .withIdentity("" + gameId, "GameJobs")
			    .startAt(new Date(System.currentTimeMillis() + (60 * 1000 * graceMinutes)))
			    .withSchedule(simpleSchedule().withIntervalInMinutes(repeatMinutes)
			    		.withMisfireHandlingInstructionFireNow().repeatForever())                   
			    .build();
		}
		try {
			new StdSchedulerFactory().getScheduler()
				.scheduleJob(job , trigger);
		} catch (SchedulerException e) {
			LOG.error("scheduler error when setting game trigger", e);
		}
	}
	
	/**
	 * deletes the game job from the Quartz scheduler
	 * @param gameId the id of the game to be removed from the scheduler
	 */
	public static void deregisterGameTrigger(long gameId)
	{

		LOG.debug("destroying " + gameId);
		try {
			new StdSchedulerFactory().getScheduler()
				.deleteJob(new JobKey("" + gameId, "GameJobs"));
		} catch (SchedulerException e) {
			LOG.error("scheculer error when deleting game trigger", e);
		}
	}
	
	/**
	 * triggers a process of the turn, preserving the Quartz triggers
	 * @param gameId id of the game to be processed
	 */
	public static void triggerProcessTurn(long gameId)
	{
		LOG.debug("triggering " + gameId);
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			Trigger trigger = scheduler.getTrigger(new TriggerKey("" + gameId, "GameJobs"));
			Trigger newTrigger = trigger.getTriggerBuilder().startNow().build();
			scheduler.rescheduleJob(trigger.getKey(), newTrigger);
		} catch (SchedulerException e) {
			LOG.error("scheculer error when deleting game trigger", e);
		}
	}
	
	/**
	 * gets the next fire time for the process turns job from Quartz
	 * @param gameId the id of the game
	 * @return the date of the next process turns trigger, null if an error occurs
	 */
	public static Date getNextTrigger(long gameId)
	{
		Date ret = null;
		try {
			ret = new StdSchedulerFactory().getScheduler()
				.getTrigger((new TriggerKey("" + gameId, "GameJobs"))).getNextFireTime();
		} catch (SchedulerException e) {
			LOG.error("scheculer error when looking for next trigger", e);
		}
		catch (Exception e){}
		return ret;
	}
	
	/**
	 * does the logic for processing a turn
	 * @param gameId the game to process a turn for
	 */
	public static void proccessTurn(long gameId)
	{
		List<Map<String,Object>> moves = DBAccessor.sendQuery("select * from moves "
				+ "where gameId=? and evaluation=? "
				+ "order by date desc;","" + gameId,"pending");

		ArrayList<Action> actions = new ArrayList<Action>();
		ArrayList<Action> invalidActions = new ArrayList<Action>();
		ArrayList<Action> successfulActions = new ArrayList<Action>();
		ArrayList<Action> failedActions = new ArrayList<Action>();
		
		if(moves != null)
		{
			moves.remove(moves.size() - 1); //remove the resultset
			for(Map<String,Object> moveValue : moves)
			{
				Action tempAction = Action.readOrder((String)moveValue.get("country"),
						(String)moveValue.get("moveText"));
				tempAction.setMoveId((long) moveValue.get("id"));
				String home = tempAction.getHome();
				boolean toAdd = true;
				for(Action a : actions)
				{
					if(home.equals(a.getHome()))
					{
						invalidActions.add(tempAction);
						toAdd = false;
					}
				}
				if(toAdd)
					actions.add(tempAction);
			}
		}
		
		int currentStateNumber = getCurrentStateNumber(gameId);
		
		GameMap gMap = getGameMapForStateId(gameId,currentStateNumber);
		
		if(someoneWon(gMap))
		{
			deregisterGameTrigger(gameId);
			return;
		}
		switch(currentStateNumber % 5){
		case 0: // spring move
			ActionReader actionReaderSpring = new ActionReader(gMap,actions);
			invalidActions.addAll(actionReaderSpring.getInvalid());
			successfulActions.addAll(actionReaderSpring.getSuccessful());
			failedActions.addAll(actionReaderSpring.getUnsuccessful());
			break;
		case 1: // spring retreat
			for(Action action: actions)
			{
				if(Move.class.equals(action.getClass()))
				{
					if(evaluateRetrete(gMap,action))
						successfulActions.add(action);
					else
						failedActions.add(action);
				}
				else
				{
					invalidActions.add(action);
				}
			}
			gMap.getDisplaced().clear();
			break;
		case 2: // fall move
			ActionReader actionReaderFall = new ActionReader(gMap,actions);
			invalidActions.addAll(actionReaderFall.getInvalid());
			successfulActions.addAll(actionReaderFall.getSuccessful());
			failedActions.addAll(actionReaderFall.getUnsuccessful());
			gMap.updateOwnership();
			break;
		case 3: // fall retreat
			for(Action action: actions)
			{
				if(Move.class.equals(action.getClass()))
				{
					if(evaluateRetrete(gMap,action))
						successfulActions.add(action);
					else
						failedActions.add(action);
				}
			}
			gMap.getDisplaced().clear();
			break;
		case 4: //build_disband
			List<Action> tempActions = new ArrayList(actions); // needed for alter in place error
			for(Action action: actions)
			{
				if(Disband.class.equals(action.getClass()))
				{
					if(evaluateDisband(gMap, action))
						successfulActions.add(action);
					else
						failedActions.add(action);
					tempActions.remove(action);
				}
			}
			for(Action action: actions)
			{
				if(Build.class.equals(action.getClass()))
				{
					if(evaluateBuild(gMap, action))
						successfulActions.add(action);
					else
						failedActions.add(action);
					tempActions.remove(action);
				}
			}
			for(String country : gMap.getCountries())
			{
				while(countCurrentSupply(gMap, country) < countCurrentUnits(gMap, country))
				{
					String keyToRemove = null;
					for(String key : gMap.getUnits().keySet())
					{
						Unit unit = gMap.getUnits().get(key);
						if(unit.getCountry().equals(country))
						{
							keyToRemove = key;
							break;
						}
					}
					if(keyToRemove != null)
						gMap.getUnits().remove(keyToRemove);
					else
						break;
				}
			}
			invalidActions.addAll(tempActions);
			gMap.getDisplaced().clear();
		default:
			break;
		}

		currentStateNumber++;
		updateMoves(invalidActions,"invalid");
		updateMoves(successfulActions,"success");
		updateMoves(failedActions,"failure");
		putGameMapForStateId(gMap, gameId, currentStateNumber);
	}

	/**
	 * updtates the action on the moves table with the given evalutation and at the given stateNumber
	 * @param moves the move to update
	 * @param evaluation the evaluation of the move
	 * @param stateNumber the state number associated with the move
	 */
	private static void updateMoves(List<Action> moves, String evaluation)
	{
		for(Action a : moves)
		{
			System.out.println(evaluation + " " + a.getMoveId() + " " + a.toString());
			if(a.getMoveId() != null)
				DBAccessor.sendQuery("update moves set evaluation=? where id=?",
						evaluation,"" + a.getMoveId());
		}
	}
	
	/**
	 * evaluates a retreat move on the given gameMap
	 * @param gMap the map the action is working on
	 * @param retreat the retreat action to evaluate
	 * @return whether or not the action was sucessful
	 */
	private static boolean evaluateRetrete(GameMap gMap, Action retreat)
	{
		Territory home = gMap.getTerritory(retreat.getHome());
		Territory dest = gMap.getTerritory(retreat.getDestination());
		Unit displaced = gMap.getDisplaced().remove(retreat.getHome());
		if(home == null || dest == null || displaced == null)
			return false;
		if(((Units.ARMY_DISPLACED.equals(displaced.getType()) && home.getArmyNeighbors().contains(dest)) ||
				(Units.FLEET_DISPLACED.equals(displaced.getType())) && home.getFleetNeighbors().contains(dest)) &&
				gMap.getUnit(dest.getName()) == null)
		{
			if(Units.ARMY_DISPLACED.equals(displaced.getType()))
				displaced.setType(Units.ARMY);
			if(Units.FLEET_DISPLACED.equals(displaced.getType()))
				displaced.setType(Units.FLEET);
			gMap.getUnits().put(dest.getName(), displaced);
			return true;
		}
		return false;
	}
	
	/**
	 * Evaluates a build move against the given gameMap
	 * @param gMap the map to evaluate the action against
	 * @param build the build action to evaluate
	 * @return whether or not the action was successful
	 */
	private static boolean evaluateBuild(GameMap gMap, Action build)
	{
		String country = build.getCountry();
		String type = ((Build)build).getArmyType();
		int supplyCount = countCurrentSupply(gMap, country);
		int unitCount = countCurrentUnits(gMap, country);
		Territory dest = gMap.getTerritory(build.getDestination());
		if(unitCount<supplyCount && dest != null && dest.isSupply() && 
				dest.getCountry().equals(country) && gMap.getUnit(dest.getName()) == null &&
				(Units.ARMY.equals(type) && dest.isPassableArmy()) || 
				(Units.FLEET.equals(type) && dest.isPassableFleet()))
		{
			Unit newUnit = new Unit();
			newUnit.setCountry(country);
			newUnit.setTerritory(dest.getName());
			newUnit.setType(type);
			gMap.getUnits().put(dest.getName(), newUnit);
			return true;
		}
		return false;
	}
	
	/**
	 * evaluates a disband move against the given map
	 * @param gMap the map to evaluate against
	 * @param disband the disband move to evaluate
	 * @return whether or not the move was successful
	 */
	private static boolean evaluateDisband(GameMap gMap, Action disband)
	{
		String country = disband.getCountry();
		Unit derelect = gMap.getUnits().get(disband.getHome());
		if(derelect != null && country.equals(derelect.getCountry()))
		{
			gMap.getUnits().remove(derelect.getTerritory());
			return true;
		}
		return false;
	}

	private static boolean someoneWon(GameMap gMap)
	{
		int totalSupply = 0;
		for(String key : gMap.getTerritories().keySet())
		{
			if(gMap.getTerritories().get(key).isSupply())
				totalSupply++;
		}
		for(String country : gMap.getCountries())
		{
			if(countCurrentSupply(gMap, country) > totalSupply/2)
				return true;
		}
		return false;
	}
	
	/**
	 * counts the number of owned supply centers by the given country on the map
	 * @param gMap the map to work on
	 * @param country the country 
	 * @return integer of the owned supply center
	 */
	private static int countCurrentSupply(GameMap gMap, String country)
	{
		int supplyCount = 0;
		for(String key: gMap.getOwnership().keySet())
		{
			if(country.equals(gMap.getOwnership().get(key)))
				supplyCount++;
		}
		return supplyCount;
	}
	
	/**
	 * counts the current number of units owned by the country on the map
	 * @param gMap the map to work on
	 * @param country the country
	 * @return integer of the owned units on the board
	 */
	private static int countCurrentUnits(GameMap gMap, String country)
	{
		int unitCount = 0;
		for(String key: gMap.getUnits().keySet())
		{
			if(country.equals(gMap.getUnits().get(key)))
				unitCount++;
		}
		return unitCount;
	}
	
	/**
	 * puts a game on the gamesTable
	 * @param owner owner of the game 
	 * @param userInfo json string of users and their countries
	 * @param boardType the type of board to create the game for
	 */
	public static void setGameParameters(String owner, String userInfo, String boardType)
	{
		DBAccessor.sendQuery("insert into games "
				+ "(owner,users,boardType) "
				+ "values (?,?,?);", owner,userInfo,boardType);
	}
	
	/**
	 * gets the list of users from the games table
	 * @param gameId integer of the game
	 * @return json string of users and their countries
	 */
	public static String getUserList(Integer gameId)
	{
		try
		{
			return DBAccessor.sendQuery("select * from games where id=?" , gameId.toString()).get(0).get("users").toString();			
		}
		catch(Exception e)
		{
			
		}
		return "";
	}
	
	/**
	 * checks if a user exists
	 * @param user the username to check
	 * @return if the user exists
	 */
	public static boolean authenticateUser(String user){
		List<Map<String, Object>> check = DBAccessor.sendQuery("select username from users where username=?;", user);
		if(check == null || check.size() < 1){
			return false;
		}
		return true;
	}
	
	/**
	 * gets the game Id for the given game
	 * @param owner the owner of the game
	 * @param userInfo the users json string
	 * @param boardType what type of board is part of this game
	 * @return the integer id of the game
	 */
	public static long getGameId(String owner, String userInfo, String boardType){
		return (long) DBAccessor.sendQuery("select max(id) from games "
				+ "where owner=? and users=? and boardType=?;", owner, userInfo, boardType)
				.get(0).get("max(id)");
	}
	/**
	 * Sets the initial gamestate for the current game
	 * @param id 
	 * the id of the game to set
	 * @param board 
	 * a string containing the initial starting positions of all game pieces
	 */
	public static void setGameState(long id, String board){
		String gameId = "" + id;
		DBAccessor.sendQuery("insert into gamestate "
				+ "(gameId,board,stateNumber) "
				+ "values (?,?,?);",gameId,board,"0");
	}
}
