package net.omniblock.core.protocol.manager.network.types;

public enum MessagePreset {
	
	LOCK_SERVER("lockserver"),
	UNLOCK_SERVER("unlockserver"),
	
	;
	
	private String preset;
	
	MessagePreset(String preset){
		
		this.preset = preset;
		
	}

	public String getPreset() {
		return preset;
	}

	public void setPreset(String preset) {
		this.preset = preset;
	}
	
}
