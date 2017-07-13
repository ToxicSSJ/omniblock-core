package net.omniblock.core.protocol.manager.network.object.packet;

import net.omniblock.core.protocol.manager.network.reader.MessageReader;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class MessagePacket {
	
	private MessageType type;
	
	public MessagePacket(MessageType type){
		
		this.type = type;
		MessageReader.insert(this);
		
	}

	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException {
		return ReaderStatus.NOT_VALID;
	}
	
	public MessageType getType() {
		return type;
	}
	
}
