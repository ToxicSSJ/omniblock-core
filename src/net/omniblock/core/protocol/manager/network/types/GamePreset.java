package net.omniblock.core.protocol.manager.network.types;

public enum GamePreset {

	NONE,
	
	SKYWARS_MASK(ServerType.SKYWARS_GAME_SERVER, "mask"),
	
	SKYWARS_SOLO_NORMAL(ServerType.SKYWARS_GAME_SERVER, "solo", "normal"),
	SKYWARS_SOLO_INSANE(ServerType.SKYWARS_GAME_SERVER, "solo", "insane"),
	SKYWARS_SOLO_Z(ServerType.SKYWARS_GAME_SERVER, "solo" , "z"),
	
	SKYWARS_TEAM_NORMAL(ServerType.SKYWARS_GAME_SERVER, "team", "normal"),
	SKYWARS_TEAM_INSANE(ServerType.SKYWARS_GAME_SERVER, "team", "insane"),
	SKYWARS_TEAM_Z(ServerType.SKYWARS_GAME_SERVER, "team", "z"),
	
	;
	
	private String[] initializeargs;
	private ServerType servertype = ServerType.MAIN_LOBBY_SERVER;
	
	GamePreset() { }
	
	GamePreset(ServerType servertype, String...args) {
		
		this.servertype = servertype;
		this.initializeargs = args;
		
	}

	public ServerType getServertype() {
		return servertype;
	}

	public void setServertype(ServerType servertype) {
		this.servertype = servertype;
	}
	
	public static GamePreset getGamePreset(String gamepresetkey) {
		
		for(GamePreset gp : GamePreset.values()) {
			if(gp.toString() == gamepresetkey) {
				return gp;
			}
		}
		
		return GamePreset.NONE;
		
	}

	public String[] getInitializeArgs() {
		return initializeargs;
	}

	public String getInitializeArgsSTR() {
		return String.join("#", initializeargs);
	}
	
	public void setInitializeArgs(String[] initializeargs) {
		this.initializeargs = initializeargs;
	}
	
}
