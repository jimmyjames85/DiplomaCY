package edu.iastate.cs309.r16.diplomacy.moves;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import edu.iastate.cs309.r16.diplomacy.map.GameMap;

public class MoveTest {
	

	public static void main(String[] args) {
		GameMap gameMap = new GameMap(null);
		
		String jsonInput = "{\"ENG\":{\"LON\":\"A\",\"EDI\":\"F\",\"LIV\":\"A\",\"ENG\":\"F\",\"MID\":\"F\",\"POR\":\"F\"},"+
				"\"FRA\":{\"PAR\":\"A\",\"MAR\":\"A\",\"BRE\":\"F\"},"+
				"\"AUS\":{\"VIE\":\"A\",\"BUD\":\"A\",\"TRI\":\"F\"},"+
				"\"ITA\":{\"ROM\":\"A\",\"VEN\":\"A\",\"NAP\":\"F\"},"+
				"\"GER\":{\"BER\":\"A\",\"RUH\":\"A\",\"MUN\":\"A\",\"KIE\":\"F\",\"NAT\":\"F\",\"IRI\":\"F\"},"+
				"\"RUS\":{\"MOS\":\"A\",\"SEV\":\"F\",\"WAR\":\"A\",\"STP\":\"F\"},"+
				"\"TUR\":{\"ANK\":\"F\",\"CON\":\"A\",\"SMY\":\"A\"}}";
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Map<String,String>> peices = null;
		try {
			peices = mapper.readValue(jsonInput, Map.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String key: peices.keySet())
		{
			gameMap.populateMap(key, peices.get(key));
		}
		ArrayList<String> orders1 = new ArrayList<String>();
		ArrayList<String> orders2 = new ArrayList<String>();
		ArrayList<String> orders3 = new ArrayList<String>();
		
		//orders1.add("A BER M KIE");
		//orders1.add("A MUN S A BER M KIE");
		//orders1.add("F KIE H");
		//orders1.add("A RUH S F KIE H");
		orders2.add("A LON M GAS");
		orders2.add("F ENG C A LON M GAS");
		orders2.add("F MID C A LON M GAS");
		//orders3.add("A MAR M GAS");
		orders1.add("F NAT M MID");
		orders1.add("F IRI S F NAT M MID");
		orders2.add("F POR S F MID H");


		ActionReader read = new ActionReader(gameMap);
		read.addOrders("GER", orders1);
		read.addOrders("ENG", orders2);
		read.addOrders("FRA", orders3);
		
		read.evaluateMoves();
		gameMap = read.getMap();
		for(int i = 0; i <= read.getSuccessful().size() - 1; i++) {
			System.out.println("Successful:");
			System.out.print(read.getSuccessful().get(i).getArmyType() + " ");
			System.out.print(read.getSuccessful().get(i).getHome() + " ");
			System.out.print(read.getSuccessful().get(i).getCommandSet()[2] + " ");
			System.out.println(read.getSuccessful().get(i).getDestination());
		}
		for(int i = 0; i <= read.getUnsuccessful().size() - 1; i++) {
			System.out.println("Unsuccesful:");
			System.out.print(read.getUnsuccessful().get(i).getArmyType() + " ");
			System.out.print(read.getUnsuccessful().get(i).getHome() + " ");
			System.out.print(read.getUnsuccessful().get(i).getCommandSet()[2] + " ");
			System.out.println(read.getUnsuccessful().get(i).getDestination());
		}
		for(int i = 0; i <= read.getInvalid().size() - 1; i++) {
			System.out.println("Invalid:");
			System.out.print(read.getInvalid().get(i).getArmyType() + " ");
			System.out.print(read.getInvalid().get(i).getHome() + " ");
			System.out.print(read.getInvalid().get(i).getCommandSet()[2] + " ");
			System.out.println(read.getInvalid().get(i).getDestination());
		}
//		System.out.println(read.getSuccessful().get(0).getHome());
//		System.out.println(read.getSuccessful().get(1).getHome());
//		System.out.println(read.getSuccessful().get(2).getHome());
//		System.out.println(read.getSuccessful().get(3).getHome());
//		System.out.println(read.getSuccessful().get(4).getHome());
//		System.out.println(read.getUnsuccessful().get(0).getHome());
		
	}
}
