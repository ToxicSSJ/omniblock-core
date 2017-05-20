package net.omniblock.core.protocol.manager.network.types;

public enum GameStat {
	
	SERVER_NAME("servername", String.class),
	GAME_PRESET("gamepreset", String.class),
	
	ONLINE_PLAYERS("onlineplayers", Integer.class),
	
	MINIMIUM_PLAYERS("minimiumplayers", Integer.class),
	MAXIMIUM_PLAYERS("maximiumplayers", Integer.class),
	
	MAP_NAME("mapname", String.class),
	EXTRA_INFO("extrainfo", String.class),
	
	;
	
	private String statkey;
	private Class<?> clazz;
	
	GameStat(String statkey, Class<?> clazz){
		
		this.statkey = statkey;
		this.clazz = clazz;
		
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getStatkey() {
		return statkey;
	}

	public void setStatkey(String statkey) {
		this.statkey = statkey;
	}
	
}
