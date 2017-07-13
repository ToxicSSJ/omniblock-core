package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class GameOnlineInfoPacket extends MessagePacket {

	public GameOnlineInfoPacket(MessageType type) {
		super(type);
	}
	
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.GAME_ONLINE_INFO.getKey());
		
		String servername = obj.getString("servername");
		String mapname = obj.getString("mapname");
		
		Integer onlineplayers = obj.getInt("onlineplayers");
		Integer maxplayers = obj.getInt("maximiumplayers");
		
		GameManager.setGameOnlineINFO(servername, mapname, onlineplayers, maxplayers);
		return ReaderStatus.SUCESS;
		
	}
	
}
