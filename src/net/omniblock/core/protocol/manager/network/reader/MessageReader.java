package net.omniblock.core.protocol.manager.network.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.packet.GameOnlineInfoPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerLoginEvaluatePacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerLoginSucessPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendBanPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendKickPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendMessagePacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendTexturepackPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendToGamePacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendToNamedServerPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.PlayerSendToServerPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.RequestPlayerGameLobbyServersPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.ServerReloadInfoPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.ServerRemoveInfoPacket;
import net.omniblock.core.protocol.manager.network.reader.packet.ServerSocketInfoPacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class MessageReader implements Reader {
	
	private static Map<MessageType, List<MessagePacket>> packets = new HashMap<MessageType, List<MessagePacket>>();
	
	static {
		
		for(MessageType type : MessageType.values()){
			
			packets.put(type, new ArrayList<MessagePacket>());
			continue;
			
		}
		
		packets.get(MessageType.GAME_ONLINE_INFO).add(new GameOnlineInfoPacket(MessageType.GAME_ONLINE_INFO));
		packets.get(MessageType.PLAYER_LOGIN_EVALUATE).add(new PlayerLoginEvaluatePacket(MessageType.PLAYER_LOGIN_EVALUATE));
		packets.get(MessageType.PLAYER_LOGIN_SUCESS).add(new PlayerLoginSucessPacket(MessageType.PLAYER_LOGIN_SUCESS));
		packets.get(MessageType.PLAYER_SEND_BAN).add(new PlayerSendBanPacket(MessageType.PLAYER_SEND_BAN));
		packets.get(MessageType.PLAYER_SEND_KICK).add(new PlayerSendKickPacket(MessageType.PLAYER_SEND_KICK));
		packets.get(MessageType.PLAYER_SEND_MESSAGE).add(new PlayerSendMessagePacket(MessageType.PLAYER_SEND_MESSAGE));
		packets.get(MessageType.PLAYER_SEND_TEXTUREPACK).add(new PlayerSendTexturepackPacket(MessageType.PLAYER_SEND_TEXTUREPACK));
		packets.get(MessageType.PLAYER_SEND_TO_GAME).add(new PlayerSendToGamePacket(MessageType.PLAYER_SEND_TO_GAME));
		packets.get(MessageType.PLAYER_SEND_TO_NAMED_SERVER).add(new PlayerSendToNamedServerPacket(MessageType.PLAYER_SEND_TO_NAMED_SERVER));
		packets.get(MessageType.PLAYER_SEND_TO_SERVER).add(new PlayerSendToServerPacket(MessageType.PLAYER_SEND_TO_SERVER));
		packets.get(MessageType.REQUEST_PLAYER_GAME_LOBBY_SERVERS).add(new RequestPlayerGameLobbyServersPacket(MessageType.REQUEST_PLAYER_GAME_LOBBY_SERVERS));
		packets.get(MessageType.SERVER_RELOAD_INFO).add(new ServerReloadInfoPacket(MessageType.SERVER_RELOAD_INFO));
		packets.get(MessageType.SERVER_REMOVE_INFO).add(new ServerRemoveInfoPacket(MessageType.SERVER_REMOVE_INFO));
		packets.get(MessageType.SERVER_SOCKET_INFO).add(new ServerSocketInfoPacket(MessageType.SERVER_SOCKET_INFO));
		
	}
	
	public static void insert(MessagePacket messagepacket){
		
		if(packets.containsKey(messagepacket.getType())){
			
			packets.get(messagepacket.getType()).add(messagepacket);
			return;
			
		}
		
	}
	
	@Override
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		ReaderStatus status = ReaderStatus.SUCESS;
		
		for(Map.Entry<MessageType, List<MessagePacket>> packet : packets.entrySet()){
			
			if(packet.getValue().size() > 0 && object.has(packet.getKey().getKey())){
					
				for(MessagePacket function : packet.getValue()){
						
					ReaderStatus cache = function.read(object);
					if(cache != ReaderStatus.SUCESS) status = cache;
					continue;
						
				}
					
				return status;
				
			}
			
		}
		
		return ReaderStatus.CANNOT_EXECUTE;
	}
	
	@Override
	public boolean isValidReader(JSONObject object) {
		
		String str = object.toString();
		
		for(MessageType mt : MessageType.values()) {
			if(str.contains(mt.getKey())) {
				return true;
			}
		}
		
		return false;
	}
	
}
