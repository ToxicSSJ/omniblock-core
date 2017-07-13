package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.handler.cord.ProxyServer;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.core.protocol.manager.network.types.ServerType;
import net.omniblock.lib.json.JSONObject;

public class RequestPlayerGameLobbyServersPacket extends MessagePacket {

	public RequestPlayerGameLobbyServersPacket(MessageType type) {
		super(type);
	}
	
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.REQUEST_PLAYER_GAME_LOBBY_SERVERS.getKey());
		
		PacketModifier modifier = new PacketModifier()
				.addString(MessageType.REQUEST_PLAYER_GAME_LOBBY_SERVERS.getKey())
				.addString(obj.getString("playername"))
				.addString(NetworkManager.getLobbyServers(ServerType.valueOf(obj.getString("servertype"))));
		
		ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
		return ReaderStatus.SUCESS;
		
	}

}
