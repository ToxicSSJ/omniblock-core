package net.omniblock.core.protocol.manager.network.types;

import net.omniblock.packets.network.structure.data.PacketStructure.DataType;

public enum GameStat {
	
	SERVER_NAME("servername", DataType.STRINGS),
	GAME_PRESET("gamepreset", DataType.STRINGS),
	
	MINIMIUM_PLAYERS("minimiumplayers", DataType.INTEGERS),
	MAXIMIUM_PLAYERS("maximiumplayers", DataType.INTEGERS),
	
	ONLINE_PLAYERS("onlineplayers", DataType.INTEGERS),
	
	MAP_NAME("mapname", DataType.STRINGS),
	EXTRA_INFO("extrainfo", DataType.STRINGS),
	
	;
	
	private String statkey;
	private DataType datatype;
	
	GameStat(String statkey, DataType datatype){
		
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
