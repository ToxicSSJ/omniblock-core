package net.omniblock.core.protocol.manager.network.types;

public enum GameAttribute {

	LOCKED("islocked"),
	NEXT("isnext"),
	
	STARTED("isstarted"),
	RELOAD("isreload"),
	
	;
	
	private String attribute;
	
	GameAttribute(String attribute){
		this.attribute = attribute;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public static GameAttribute getAttribute(String attributekey) {
		
		for(GameAttribute attribute : GameAttribute.values()) {
			if(attribute.getAttribute().equalsIgnoreCase(attributekey)) {
				return attribute;
			}
		}
		
		return null;
	}
	
	public static boolean isAttribute(String attributekey) {
		
		for(GameAttribute attribute : GameAttribute.values()) {
			if(attribute.getAttribute().equalsIgnoreCase(attributekey)) {
				return true;
			}
		}
		
		return false;
	}
	
}
