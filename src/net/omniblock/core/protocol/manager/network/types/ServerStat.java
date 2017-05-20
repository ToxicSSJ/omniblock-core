package net.omniblock.core.protocol.manager.network.types;

public enum ServerStat {

	ONLINE_PLAYERS("onlineplayers", Integer.class),
	MAX_ONLINE_PLAYERS("maxplayers", Integer.class),
	
	SERVER_PORT("serverport", Integer.class),
	SOCKET_PORT("socketport", Integer.class),
	SERVER_NAME("servername", String.class),
	SERVER_TYPE("servertype", String.class),
	
	MAP_NAME("mapname", String.class),
	EXTRA_INFO("info", String.class),
	
	;
	
	private String statkey;
	private Class<?> clazz;
	
	ServerStat(String statkey, Class<?> clazz){
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
