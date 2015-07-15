package edu.iastate.cs309.r16.diplomacy.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.iastate.cs309.r16.diplomacy.enumeration.Territories;
import edu.iastate.cs309.r16.diplomacy.enumeration.Units;

public class GameMap {
	Map<String,Territory> territories;
	Map<String,Unit> units;
	Map<String,Unit> displaced;
	List<String> countries;
	
	private String resource = "../resources/territories.xml";
	
	private static final Logger LOG = LoggerFactory.getLogger(GameMap.class);
	
	public GameMap(String resourcePath)
	{
		//TODO check this is what we want to do
		if(resourcePath != null)
			resource = resourcePath;
		this.territories = new HashMap<String, Territory>();
		this.units = new HashMap<String, Unit>();
		this.displaced = new HashMap<String, Unit>();
		this.countries = new LinkedList<String>();
		Document document = getDocument();
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList territoryList = getTerritories(document, xPath);
		for(int i = 0; i<territoryList.getLength();i++)
		{
			evaluateAndAddTerritory(territoryList.item(i), xPath);
		}
		for(int i=0; i<territoryList.getLength();i++)
		{
			linkTerritoryInMap(territoryList.item(i), xPath);
		}
	}
	
	private Document getDocument()
	{
		InputStream inputStream = GameMap.class.getClassLoader().getResourceAsStream(resource);
		Document ret = null;
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			ret = builder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	private NodeList getTerritories(Document document, XPath xPath)
	{
		NodeList ret = null;
		try
		{
			XPathExpression expression = xPath.compile("/territories/territory");
			ret = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
		}
		catch(XPathExpressionException e)
		{
			//TODO something meaningful
			e.printStackTrace();
		}
		return ret;
	}
	
	private void evaluateAndAddTerritory(Node node, XPath xPath)
	{
		Territory cur = new Territory(); 
		try
		{
			XPathExpression namePath = xPath.compile("@name");
			String name = (String) namePath.evaluate(node, XPathConstants.STRING);
			cur.setName(name);
			
			XPathExpression countryPath = xPath.compile("country");
			String country = (String) countryPath.evaluate(node, XPathConstants.STRING);
			cur.setCountry(country);
			
			XPathExpression supplyPath = xPath.compile("supply");
			String supply = (String) supplyPath.evaluate(node, XPathConstants.STRING);
			cur.setSupply(Boolean.parseBoolean(supply));
			
			XPathExpression typePath = xPath.compile("type");
			String type = (String) typePath.evaluate(node, XPathConstants.STRING);
			cur.setType(type);
			
			XPathExpression passableArmyPath = xPath.compile("passable/army");
			String passableArmy = (String) passableArmyPath.evaluate(node, XPathConstants.STRING);
			cur.setPassableArmy(Boolean.parseBoolean(passableArmy));

			XPathExpression passableFleetPath = xPath.compile("passable/fleet");
			String passableFleet = (String) passableFleetPath.evaluate(node, XPathConstants.STRING);
			cur.setPassableFleet(Boolean.parseBoolean(passableFleet));
			
			this.territories.put(name, cur);
		}
		catch(XPathExpressionException e)
		{
			//TODO something meaningful
			e.printStackTrace();
		}
	}
	
	private void linkTerritoryInMap(Node node, XPath xPath)
	{
		Territory cur = null;
		try
		{
			XPathExpression namePath = xPath.compile("@name");
			String name = (String) namePath.evaluate(node, XPathConstants.STRING);
			cur = territories.get(name);
			
			XPathExpression armyNeighborsPath = xPath.compile("neighbors/neighbor[@army='true']");
			NodeList armyNeighbors = (NodeList) armyNeighborsPath.evaluate(node, XPathConstants.NODESET);
			List<Territory> aNeighbors =  new LinkedList<Territory>();
			for(int i=0; i<armyNeighbors.getLength(); i++)
			{
				Node armyNeighbor = armyNeighbors.item(i);
				aNeighbors.add(territories.get(armyNeighbor.getTextContent()));
			}
			cur.setArmyNeighbors(aNeighbors);
			
			XPathExpression fleetNeighborsPath = xPath.compile("neighbors/neighbor[@fleet='true']");
			NodeList fleetNeighbors = (NodeList) fleetNeighborsPath.evaluate(node, XPathConstants.NODESET);
			List<Territory> fNeighbors = new LinkedList<Territory>();
			for(int i=0; i<fleetNeighbors.getLength(); i++)
			{
				Node fleetNeighbor = fleetNeighbors.item(i);
				fNeighbors.add(territories.get(fleetNeighbor.getTextContent()));
			}
			cur.setFleetNeighbors(fNeighbors);
			
			XPathExpression exclusivesPath = xPath.compile("exclusives/exclusive");
			NodeList exclusives = (NodeList) exclusivesPath.evaluate(node, XPathConstants.NODESET);
			List<Territory> exclusiveList =  new LinkedList<Territory>();
			for(int i=0; i<exclusives.getLength(); i++)
			{
				Node exclusive = exclusives.item(i);
				exclusiveList.add(territories.get(exclusive.getTextContent()));
			}
			cur.setExclusives(exclusiveList);
		}
		catch(XPathExpressionException e)
		{
			//TODO something meaningful
			e.printStackTrace();
		}		
	}

	public void createFromBoard(String jsonBoard)
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Map<String,String>> boardValues = null;
		try {
			boardValues = mapper.readValue(jsonBoard, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		for(String key : boardValues.keySet())
		{
			if("supply".equals(key))
			{
				this.setOwnership(boardValues.get(key));
			}
			else
			{
				this.populateMap(key, boardValues.get(key));
			}
		}
	}
	
	public void populateMap(String country, Map<String,String> units)
	{
		this.countries.add(country);
		for(String curKey: units.keySet())
		{
			Unit curUnit = new Unit();
			curUnit.setCountry(country);
			curUnit.setTerritory(curKey);
			curUnit.setType(units.get(curKey));
			if(Units.ARMY.equals(curUnit.getType()) || Units.FLEET.equals(curUnit.getType()))
				this.units.put(curKey, curUnit);
			else
				this.displaced.put(curKey, curUnit);
		}
	}
	
	public void setOwnership(Map<String,String> supplyCenters)
	{
		LOG.debug(supplyCenters.toString());
		for(String curKey: supplyCenters.keySet())
		{
			Territory territory = this.territories.get(curKey);
			LOG.debug("look for " + curKey);
			if(territory.isSupply())
				territory.setCountry(supplyCenters.get(curKey));
		}
	}
	
	public void updateOwnership()
	{
		for(String curKey: this.territories.keySet())
		{
			Territory territory = this.territories.get(curKey);
			Unit unit = this.units.get(curKey);
			if(unit == null)
			{
				for(Territory exclusive: territory.getExclusives())
				{
					unit = this.units.get(exclusive.getName());
					if(unit != null)
						break;
				}
			}
			if(territory.isSupply() && unit != null)
			{
				territory.setCountry(unit.getCountry());
			}
		}
	}
	
	public Map<String,String> getOwnership()
	{
		Map<String, String> ret = new HashMap<String, String>();
		for(String curKey: this.territories.keySet())
		{
			Territory territory = this.territories.get(curKey);
			if(territory.isSupply())
				ret.put(curKey,territory.getCountry());
		}
		return ret;
	}
	
	public Territory getTerritory(String name) {
		Territory ret = null;
		ret = territories.get(name);
		return ret;
	}
	
	public Unit getUnit(String territory){
		Unit ret = null;
		ret = units.get(territory);
		return ret;
	}
	
	public String toString(){
		String ret = "territories size: " + territories.size();
		ret += "\n";
		ret += "units size: " + units.size();
		return ret;
	}
	
	public Map<String, Territory> getTerritories()
	{
		return this.territories;
	}
	
	public Map<String, Unit> getUnits()
	{
		return this.units;
	}
	
	public Map<String,Unit> getDisplaced()
	{
		return this.displaced;
	}
	
	public List<String> getCountries()
	{
		return this.countries;
	}
}
