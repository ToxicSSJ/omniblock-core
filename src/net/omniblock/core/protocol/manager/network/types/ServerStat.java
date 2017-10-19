package net.omniblock.core.protocol.manager.network.types;

import net.omniblock.packets.network.structure.data.PacketStructure.DataType;

public enum ServerStat {

	ONLINE_PLAYERS("onlineplayers", DataType.INTEGERS),
	MAX_ONLINE_PLAYERS("maxplayers", DataType.INTEGERS),
	
	SERVER_PORT("serverport", DataType.INTEGERS),
	SOCKET_PORT("socketport", DataType.INTEGERS),
	SERVER_NAME("servername", DataType.STRINGS),
	SERVER_TYPE("servertype", DataType.STRINGS),
	
	MAP_NAME("mapname", DataType.STRINGS),
	EXTRA_INFO("info", DataType.STRINGS),
	
	;
	
	private String statkey;
	private DataType datatype;
	
	ServerStat(String statkey, DataType datatype){
		this.statkey = statkey;
		this.setDatatype(datatype);
	}

	public String getStatkey() {
		return statkey;
	}

	public void setStatkey(String statkey) {
		this.statkey = statkey;
	}

	public DataType getDatatype() {
		return datatype;
	}

	public void setDatatype(DataType datatype) {
		this.datatype = datatype;
	}
	
}
