package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.core.protocol.manager.network.types.GamePreset;
import net.omniblock.lib.json.JSONObject;

public class PlayerSendToGamePacket extends MessagePacket {

	public PlayerSendToGamePacket(MessageType type) {
		super(type);
	}

	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.PLAYER_SEND_TO_GAME.getKey());
		
		String playername = obj.getString("playername");
		Boolean party = obj.getBoolean("party");
		GamePreset preset = GamePreset.valueOf(obj.getString("gamepreset"));
		
		GameManager.sendPlayerToGame(playername, party, preset);
		return ReaderStatus.SUCESS;
		
	}
	
}
