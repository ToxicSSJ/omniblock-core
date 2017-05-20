package net.omniblock.core.protocol.manager.network.types;

public enum ServerAttribute {

	FULL("isfull"),
	LOBBY_SERVER("islobbyserver"),
	
	GAME_SERVER("isgameserver"),
	GAME_LOBBY_SERVER("isgamelobbyserver"),
	
	STAFF_SERVER("isstaffserver"),
	OTHER_SERVER("isotherserver"),
	
	;
	
	private String attribute;
	
	ServerAttribute(String attribute){
		
		this.attribute = attribute;
		
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	public static ServerAttribute getAttribute(String attributekey) {
		
		for(ServerAttribute attribute : ServerAttribute.values()) {
			if(attribute.getAttribute().equalsIgnoreCase(attributekey)) {
				return attribute;
			}
		}
		
		return null;
	}
	
	public static boolean isAttribute(String attributekey) {
		
		for(ServerAttribute attribute : ServerAttribute.values()) {
			if(attribute.getAttribute().equalsIgnoreCase(attributekey)) {
				return true;
			}
		}
		
		return false;
	}
	
}
