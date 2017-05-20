package net.omniblock.core.protocol.manager.network.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.omniblock.lib.json.JSONObject;

public class Packet {

	public static int PACKET_ID = -1;
	
	private Set<PacketType> PACKETS = new HashSet<PacketType>();
	
	protected JSONObject JSON_MESSAGE_ACTIONER = new JSONObject();
	protected JSONObject JSON_SERVER_STRUCTURE = new JSONObject();
	protected JSONObject JSON_GAME_STRUCTURE = new JSONObject();
	
	public Packet() {
		
		PACKET_ID++;
		
	}
	
	public Packet readJSONInformation(String JSON) {
		
		if(validateJSON(JSON)) {
			
			JSONObject object = new JSONObject(JSON);
			
			if(object.has("message_actioner")) {
				
				JSONObject actioner = object.getJSONObject("message_actioner");
				JSON_MESSAGE_ACTIONER = actioner;
				
				PACKETS.add(PacketType.MESSAGE_ACTIONER);
				
			}
			
			if(object.has("server_structure")) {
				
				JSONObject server_structure = object.getJSONObject("server_structure");
				JSON_SERVER_STRUCTURE = server_structure;
				
				PACKETS.add(PacketType.SERVER_STRUCTURE);
				
			}
			 
			if(object.has("game_structure")) {
				
				JSONObject game_structure = object.getJSONObject("game_structure");
				JSON_GAME_STRUCTURE = game_structure;
				
				PACKETS.add(PacketType.GAME_STRUCTURE);
				
			}
			
			return this;
			
		} else {
			
			throw new IllegalArgumentException("La información propiciada es corrupta.");
			
		}
		
	}
	
	public Map<PacketType, JSONObject> getJSONInformation() {
		
		Map<PacketType, JSONObject> JSONInformation = new HashMap<PacketType, JSONObject>();
		
		for(PacketType packet : PACKETS) {
			
			switch(packet) {
			
				case MESSAGE_ACTIONER: JSONInformation.put(packet, JSON_MESSAGE_ACTIONER); break;
				case SERVER_STRUCTURE: JSONInformation.put(packet, JSON_SERVER_STRUCTURE); break;
				case GAME_STRUCTURE: JSONInformation.put(packet, JSON_GAME_STRUCTURE); break;
				default: break;
			
			}
			
		}
		
		return JSONInformation;
		
	}
	
	private boolean validateJSON(String JSON) {
		
		try {
			
			new JSONObject(JSON);
			return true;
			
		} catch(Exception e) {
			
			return false;
			
		}
		
	}
	
	public static enum PacketType {
		
		NONE,
		
		MESSAGE_ACTIONER,
		
		SERVER_STRUCTURE,
		GAME_STRUCTURE,
		
		;
		
	}
	
}
