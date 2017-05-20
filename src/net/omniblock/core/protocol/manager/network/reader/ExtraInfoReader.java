package net.omniblock.core.protocol.manager.network.reader;

import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class ExtraInfoReader implements Reader {

	@Override
	public ReaderStatus read(JSONObject object) {
		
		if(object.has(InfoType.STARTED.getKey())) {
			
			JSONObject obj = object.getJSONObject(InfoType.STARTED.getKey());
			
			String servername = obj.getString("servername");
			Boolean started = obj.getBoolean("isstarted");
			
			GameManager.setGameStarted(servername, started);
			return ReaderStatus.SUCESS;
			
		} else if(object.has(InfoType.RELOAD.getKey())) {
			
			JSONObject obj = object.getJSONObject(InfoType.RELOAD.getKey());
			
			String servername = obj.getString("servername");
			Boolean started = obj.getBoolean("isreload");
			
			GameManager.setGameStarted(servername, started);
			return ReaderStatus.SUCESS;
			
		}
		
		return ReaderStatus.CANNOT_EXECUTE;
		
	}

	@Override
	public boolean isValidReader(JSONObject object) {
		
		String str = object.toString();
		
		for(InfoType it : InfoType.values()) {
			if(str.contains(it.getKey())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static enum InfoType {
		
		STARTED("STARTED"),
		RELOAD("RELOAD"),
		
		;
		
		private String key;
		
		InfoType(String key){
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
