package edu.iastate.cs309.r16.diplomacy.map;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class GameMapSerializer extends JsonSerializer<GameMap> {

	@Override
	public void serialize(GameMap gMap, JsonGenerator jGen, SerializerProvider serializer) 
			throws IOException, JsonProcessingException {
		jGen.writeStartObject();
		for(String country: gMap.countries)
		{
			Map<String, String> countryUnits = new HashMap<String, String>();
			for(String territory: gMap.units.keySet())
			{
				Unit tempUnit = gMap.units.get(territory);
				if(country.equals(tempUnit.getCountry()))
					countryUnits.put(territory, tempUnit.getType());
			}
			for(String territory: gMap.displaced.keySet())
			{

				Unit tempUnit = gMap.displaced.get(territory);
				if(country.equals(tempUnit.getCountry()))
					countryUnits.put(territory, tempUnit.getType());
			}
			jGen.writeObjectField(country, countryUnits);
		}
		Map<String,String> supplyOwnership = new HashMap<String, String>();
		for(String key: gMap.territories.keySet())
		{
			Territory tempTerritory = gMap.getTerritory(key);
			if(tempTerritory.isSupply())
				supplyOwnership.put(key, tempTerritory.getCountry());
		}
		jGen.writeObjectField("supply", supplyOwnership);
		jGen.writeEndObject();
	}

}
