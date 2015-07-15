package edu.iastate.cs309.r16.diplomacy.servlets;

import java.io.Console;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.cs309.r16.diplomacy.games.GameManager;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;
import edu.iastate.cs309.r16.diplomacy.map.GameMapSerializer;
import edu.iastate.cs309.r16.diplomacy.map.Territory;
import edu.iastate.cs309.r16.diplomacy.map.TerritorySerializer;
import edu.iastate.cs309.r16.diplomacy.util.DBAccessor;

import edu.iastate.cs309.r16.diplomacy.games.GameManager;

public class GameCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
/**
 * Get not supported
 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.getOutputStream().println("Get not supported");
	}
/**
 * @param request
 * http request to send to servlet
 * @param response
 * http response to send to server
 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String sessionId = request.getHeader("sessionId"); //gets the sessionId for the current user
	    String players = request.getHeader("players"); // gets the array of players playing the game
	    String countries = request.getHeader("countries"); //gets the array of players
	    String turnLength = request.getHeader("turnLength"); //the set turn length of the current game
	    String graceLength = request.getHeader("graceLength"); //the grace length of the current game
	    String mapType = request.getHeader("mapType"); //the game map being used in current game
	    String owner = GameManager.getUserName(sessionId); //the owner of the game based on sessionId cookie
	    String userInfo = createUserInfo(players, countries); // function call that creates json of users and their countries
	    
	    //Check to see if users and countries are all selected
	    if(userInfo.isEmpty() || userInfo.length() < 1){
	    	System.out.println("User not in database");
	    	return;
	    }
	    //Set Game State
	    GameManager.setGameParameters(owner, userInfo, mapType);
	    //Get Game id based on set game state
	    long id = GameManager.getGameId(owner, userInfo, mapType);
	    
	    GameManager.registerGameTrigger(id,Integer.parseInt(turnLength),Integer.parseInt(graceLength));
	   
	    if(mapType.equalsIgnoreCase("EUROPE")){
	    	String board = "{\"ENG\":{\"LON\":\"F\",\"EDI\":\"F\",\"LVP\":\"A\"}," + 
				"\"FRA\":{\"PAR\":\"A\",\"MAR\":\"A\",\"BRE\":\"F\"}," + 
				"\"AUS\":{\"VIE\":\"A\",\"BUD\":\"A\",\"TRI\":\"F\"}," + 
				"\"ITA\":{\"ROM\":\"A\",\"VEN\":\"A\",\"NAP\":\"F\"}," + 
				"\"GER\":{\"BER\":\"A\",\"MUN\":\"A\",\"KIE\":\"F\"}," + 
				"\"RUS\":{\"MOS\":\"A\",\"SEV\":\"F\",\"WAR\":\"A\",\"STPS\":\"F\"}," + 
				"\"TUR\":{\"ANK\":\"F\",\"CON\":\"A\",\"SMY\":\"A\"}," + 
				"\"supply\":{\"LON\":\"ENG\",\"EDI\":\"ENG\",\"LVP\":\"ENG\",\"PAR\":\"FRA\",\"MAR\":\"FRA\",\"BRE\":\"FRA\"," + 
				"\"VIE\":\"AUS\",\"BUD\":\"AUS\",\"TRI\":\"AUS\",\"ROM\":\"ITA\",\"VEN\":\"ITA\",\"NAP\":\"ITA\"," + 
				"\"BER\":\"GER\",\"MUN\":\"GER\",\"KIE\":\"GER\",\"MOS\":\"RUS\",\"SEV\":\"RUS\",\"WAR\":\"RUS\",\"STP\":\"RUS\"," + 
				"\"ANK\":\"TUR\",\"CON\":\"TUR\",\"SMY\":\"TUR\"," + 
				"\"POR\":\"NON\",\"SPA\":\"NON\",\"TUN\":\"NON\",\"BEL\":\"NON\",\"HOL\":\"NON\",\"DEN\":\"NON\",\"NWY\":" +
				"\"NON\",\"SWE\":\"NON\",\"SER\":\"NON\",\"BUL\":\"NON\",\"RUM\":\"NON\",\"GRE\":\"NON\"}}";
	    	GameManager.setGameState(id, board);
	    }
	    else if(mapType.equalsIgnoreCase("NORTHAMERICA")){
	    	String board = "{\"BRC\":{\"ANC\":\"F\",\"VAN\":\"A\",\"CGY\":\"A\"}," +
	    		"\"CAL\":{\"SFR\":\"A\",\"LOS\":\"A\",\"SDI\":\"F\"}," + 
	    		"\"MEX\":{\"VER\":\"A\",\"MEX\":\"A\",\"GUA\":\"F\"}," + 
	    		"\"FLO\":{\"TAM\":\"A\",\"JAC\":\"A\",\"MIA\":\"F\"}," + 
	    		"\"HTL\":{\"CHI\":\"A\",\"MIL\":\"A\",\"MIN\":\"A\"}," + 
	    		"\"NEW\":{\"NYC\":\"F\",\"PHI\":\"A\",\"NJE\":\"A\"}," + 
	    		"\"QUE\":{\"UNG\":\"F\",\"QUE\":\"F\",\"MON\":\"A\"}," + 
	    		"\"PER\":{\"LIM\":\"F\",\"BOG\":\"A\",\"CAL\":\"F\"}," + 
	    		"\"TEX\":{\"SAN\":\"A\",\"HOU\":\"F\",\"DAL\":\"A\"}," + 
	    		"\"CUB\":{\"HAV\":\"F\",\"KIN\":\"F\",\"HOL\":\"A\"}," + 
	    		"\"supply\":{\"ANC\":\"BRC\",\"VAN\":\"BRC\",\"CGY\":\"BRC\",\"SFR\":\"CAL\",\"LOS\":\"CAL\",\"SDI\":\"CAL\"," +
	    		"\"VER\":\"MEX\",\"MEX\":\"MEX\",\"GUA\":\"MEX\",\"TAM\":\"FLO\",\"JAC\":\"FLO\",\"MIA\":\"FLO\"," + 
	    		"\"CHI\":\"HTL\",\"MIL\":\"HTL\",\"MIN\":\"HTL\",\"NYC\":\"NEW\",\"PHI\":\"NEW\",\"NJE\":\"NEW\"," +
	    		"\"UNG\":\"QUE\",\"QUE\":\"QUE\",\"MON\":\"QUE\",\"LIM\":\"PER\",\"BOG\":\"PER\",\"CAL\":\"PER\"," +
	    		"\"SAN\":\"TEX\",\"HOU\":\"TEX\",\"DAL\":\"TEX\",\"HAV\":\"CUB\",\"KIN\":\"CUB\",\"HOL\":\"CUB\"," +
	    		"\"MAN\":\"NON\",\"GRE\":\"NON\",\"ONT\":\"NON\",\"NSC\":\"NON\",\"MAS\":\"NON\",\"HAW\":\"NON\"," +
	    		"\"VEN\":\"NON\",\"PAN\":\"NON\",\"NIC\":\"NON\",\"NIN\":\"NON\",\"NIS\":\"NON\"," +
	    		"\"GUT\":\"NON\",\"GUE\":\"NON\",\"GUTS\":\"NON\",\"YUC\":\"NON\",\"DUR\":\"NON\",\"NLE\":\"NON\"," +
	    		"\"CHH\":\"NON\",\"LOU\":\"NON\",\"GEO\":\"NON\",\"NCA\":\"NON\",\"OHI\":\"NON\",\"TEN\":\"NON\"," +
	    		"\"MIS\":\"NON\",\"KAN\":\"NON\",\"COL\":\"NON\",\"ARI\":\"NON\",\"WAS\":\"NON\",\"ORE\":\"NON\"," +
	    		"\"DOM\":\"NON\",\"MIC\":\"NON\",\"WDC\":\"NON\"}}";
	    	GameManager.setGameState(id, board);
	    }
	    else{
	    	System.out.println("Invalid Map Type: Maptype \"" + mapType + "\" does not exist");
	    }
	    return;
	}
	/**
	 * creates a JSON of players and their countries
	 * @param players
	 * comma delimited string of all players
	 * @param countries
	 * comma delimited string of all countries
	 * @return
	 */
	private String createUserInfo(String players, String countries){
		String userInfo = "";
		String JSONstr = "";
		String[] playersArr = players.split(",");
		String[] countriesArr = countries.split(",");
		for(int a = 0; a<playersArr.length; a++){
			if(GameManager.authenticateUser(playersArr[a]) == false){
				return "";
			}
		}
		userInfo+= "{";
		for(int a = 0; a<playersArr.length; a++){
			userInfo += "\"" + countriesArr[a] + "\"" + ":" + "\"" + playersArr[a] + "\"";
			if(a <playersArr.length-1){
				userInfo += ",";
			}
		}
		userInfo+= "}";
		System.out.println(userInfo);
		return userInfo;
	}
}
