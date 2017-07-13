package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.handler.cord.ProxyServer;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class PlayerSendTexturepackPacket extends MessagePacket {

	public PlayerSendTexturepackPacket(MessageType type) {
		super(type);
	}

	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TEXTUREPACK.getKey());
		
		PacketModifier modifier = new PacketModifier()
				.addString(MessageType.PLAYER_SEND_TEXTUREPACK.getKey())
				.addString(obj.getString("playername"))
				.addString(obj.getString("hash"));
		
		ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
		return ReaderStatus.SUCESS;
		
	}
	
}
