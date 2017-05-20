package net.omniblock.core.protocol.manager.network.reader;

import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.handler.cord.ProxyServer;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.core.protocol.manager.network.types.GamePreset;
import net.omniblock.core.protocol.manager.network.types.ServerType;
import net.omniblock.lib.json.JSONObject;

public class MessageReader implements Reader {
	
	@Override
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		if(object.has(MessageType.PLAYER_SEND_TO_GAME.getKey())) {
			
			System.out.println("wtf??");
			
			JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TO_GAME.getKey());
			
			String playername = obj.getString("playername");
			Boolean party = obj.getBoolean("party");
			GamePreset preset = GamePreset.valueOf(obj.getString("gamepreset"));
			
			GameManager.sendPlayerToGame(playername, party, preset);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.PLAYER_SEND_TO_SERVER.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TO_SERVER.getKey());
			
			String playername = obj.getString("playername");
			ServerType type = ServerType.valueOf(obj.getString("servertype"));
			
			NetworkManager.sendPlayerToServer(playername, type);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.PLAYER_SEND_BAN.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_BAN.getKey());
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.PLAYER_SEND_BAN.getKey())
					.addString(obj.getString("playername"));
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.PLAYER_SEND_KICK.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_KICK.getKey());
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.PLAYER_SEND_KICK.getKey())
					.addString(obj.getString("playername"))
					.addString(obj.getString("moderator"))
					.addString(obj.getString("reason"));
			
			System.out.println("CATCHED KICK!");
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.SERVER_SOCKET_INFO.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.SERVER_SOCKET_INFO.getKey());
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.SERVER_SOCKET_INFO.getKey())
					.addString(obj.getString("servername"))
					.addInteger(obj.getInt("socketport"));
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.SERVER_REMOVE_INFO.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.SERVER_REMOVE_INFO.getKey());
			
			String servername = obj.getString("servername");
			
			NetworkManager.unregisterServer(servername);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.PLAYER_SEND_MESSAGE.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_MESSAGE.getKey());
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.PLAYER_SEND_MESSAGE.getKey())
					.addString(obj.getString("playername"))
					.addString(obj.getString("message"));
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.PLAYER_SEND_TEXTUREPACK.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TEXTUREPACK.getKey());
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.PLAYER_SEND_TEXTUREPACK.getKey())
					.addString(obj.getString("playername"))
					.addString(obj.getString("hash"));
			
			System.out.println("sending texture pack...");
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.SERVER_RELOAD_INFO.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.SERVER_RELOAD_INFO.getKey());
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.SERVER_RELOAD_INFO.getKey())
					.addString(obj.getString("servername"))
					.addInteger(obj.getInt("serversocket"));
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(MessageType.GAME_ONLINE_INFO.getKey())) {
			
			JSONObject obj = object.getJSONObject(MessageType.GAME_ONLINE_INFO.getKey());
			
			String servername = obj.getString("servername");
			String mapname = obj.getString("mapname");
			
			Integer onlineplayers = obj.getInt("onlineplayers");
			Integer maxplayers = obj.getInt("maximiumplayers");
			
			GameManager.setGameOnlineINFO(servername, mapname, onlineplayers, maxplayers);
			return ReaderStatus.SUCESS;
			
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
	
	public static enum MessageType {
		
		WELCOME_PROXY("WELCOME"),
		BYE_PROXY("BYE"),
		
		SERVER_SOCKET_INFO("SSOCK"),
		SERVER_REMOVE_INFO("SSSI"),
		SERVER_RELOAD_INFO("SRI"),
		
		PLAYER_SEND_TO_SERVER("PSTS"),
		PLAYER_SEND_TO_GAME("PSTG"),
		PLAYER_SEND_MESSAGE("PSMSG"),
		PLAYER_SEND_TEXTUREPACK("PSTP"),
		
		PLAYER_SEND_KICK("PSKICK"),
		PLAYER_SEND_BAN("PSBAN"),

		GAME_LOCK_STATUS("GLS"),
		SERVER_FULL_STATUS("SFS"),
		
		GAME_ONLINE_INFO("GSPSI"),
		
		INITIALIZE_GAME("IG"),
		
		;
		
		private String key;
		
		MessageType(String key){
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
		
		
	}
	
}
