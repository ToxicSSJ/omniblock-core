package net.omniblock.core.protocol.manager.network.reader;

import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public interface Reader {

	public static Reader MESSAGE_READER = new MessageReader();
	public static Reader EXTRAINFO_READER = new ExtraInfoReader();
	
	public ReaderStatus read(JSONObject object) throws NullPointerException, ClassCastException;
	
	public boolean isValidReader(JSONObject object);
	
}
