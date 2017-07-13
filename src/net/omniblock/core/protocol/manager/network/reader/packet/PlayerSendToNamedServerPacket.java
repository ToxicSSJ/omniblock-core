package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class PlayerSendToNamedServerPacket extends MessagePacket {

	public PlayerSendToNamedServerPacket(MessageType type) {
		super(type);
	}
	
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TO_NAMED_SERVER.getKey());
		
		String playername = obj.getString("playername");
		String servername = obj.getString("servername");
		
		NetworkManager.sendPlayerToServer(playername, servername);
		return ReaderStatus.SUCESS;
		
	}
	
}
