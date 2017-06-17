package net.omniblock.core.protocol.manager.network.types;

public enum ServerType {

	MAIN_AUTH_SERVER("AUTH", false),
	MAIN_LOBBY_SERVER("LOBBY", false),
	
	SURVIVAL("SURVIVAL", false),
	
	SKYWARS_LOBBY_SERVER("SKWLB", false),
	SKYWARS_GAME_SERVER("SKWGS", true),
	
	;
	
	private String servertypekey;
	private boolean usemask = false;
	
	ServerType(String servertypekey, boolean mask){
		this.servertypekey = servertypekey;
	}

	public String getServertypekey() {
		return servertypekey;
	}

	public boolean UseMask() {
		return usemask;
	}
	
}
