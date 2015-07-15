package edu.iastate.cs309.r16.diplomacy.map;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class TerritorySerializer extends JsonSerializer<Territory> {
	/*
	private String name;
	private String country;
	private boolean supply;
	private String type;
	private boolean passableArmy;
	private boolean passableFleet;
	private List<Territory> armyNeighbors;
	private List<Territory> fleetNeighbors;
	private List<Territory> exclusives;
	 */

	@Override
	public void serialize(Territory value, JsonGenerator jgen,
			SerializerProvider serializer) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("name", value.getName());
		jgen.writeStringField("country", value.getCountry());
		jgen.writeBooleanField("supply", value.isSupply());
		jgen.writeStringField("type", value.getType());
		jgen.writeBooleanField("passableArmy", value.isPassableArmy());
		jgen.writeBooleanField("passableFleet", value.isPassableFleet());
		jgen.writeArrayFieldStart("armyNeighbors");
		for(Territory aTer: value.getArmyNeighbors()){
			jgen.writeString(aTer.getName());
		}
		jgen.writeEndArray();
		jgen.writeArrayFieldStart("fleetNeighbors");
		for(Territory fTer: value.getFleetNeighbors()){
			jgen.writeString(fTer.getName());
		}
		jgen.writeEndArray();
		jgen.writeArrayFieldStart("exclusives");
		for(Territory eTer: value.getExclusives()){
			jgen.writeString(eTer.getName());
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
		
	}
	

}
//public class ItemSerializer extends JsonSerializer<Item> {
//    @Override
//    public void serialize(Item value, JsonGenerator jgen, SerializerProvider provider)
//      throws IOException, JsonProcessingException {
//        jgen.writeStartObject();
//        jgen.writeNumberField("id", value.id);
//        jgen.writeStringField("itemName", value.itemName);
//        jgen.writeNumberField("owner", value.owner.id);
//        jgen.writeEndObject();
//    }
//}