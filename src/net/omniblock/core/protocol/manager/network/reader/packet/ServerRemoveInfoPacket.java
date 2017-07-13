package net.omniblock.core.protocol.manager.network.reader.packet;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.object.packet.MessagePacket;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class ServerRemoveInfoPacket extends MessagePacket {

	public ServerRemoveInfoPacket(MessageType type) {
		super(type);
	}
	
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		
		JSONObject obj = object.getJSONObject(MessageType.SERVER_REMOVE_INFO.getKey());
		
		String servername = obj.getString("servername");
		
		NetworkManager.unregisterServer(servername);
		return ReaderStatus.SUCESS;
		
	}

}
