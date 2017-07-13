package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.core.protocol.manager.network.types.ServerType;
import net.omniblock.lib.json.JSONObject;

public class PlayerSendToServerPacket extends MessagePacket {

	public PlayerSendToServerPacket(MessageType type) {
		super(type);
	}
	
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TO_SERVER.getKey());
		
		String playername = obj.getString("playername");
		ServerType type = ServerType.valueOf(obj.getString("servertype"));
		
		NetworkManager.sendPlayerToServer(playername, type);
		return ReaderStatus.SUCESS;
		
	}
	
}
