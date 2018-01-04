package net.omniblock.core.protocol.manager.network.object.handler;

import java.util.ArrayList;
import java.util.List;

import net.omniblock.packets.object.external.GamePreset;

public class GameGetterHandler {

	protected List<GameGetter> getters = new ArrayList<GameGetter>();
	
	public boolean hasNext(GamePreset preset) {
		
		for(GameGetter getter : getters)
			if(getter.getPreset().equals(preset))
				return true;
		
		return false;
		
	}
	
	public GameGetter next(GamePreset preset) {
		
		for(GameGetter getter : getters)
			if(getter.getPreset().equals(preset))
				return getter;
		
		return null;
		
	}
	
	public void addGetter(GameGetter getter) {
		
		for(GameGetter cacheGetter : getters)
			if(cacheGetter.getStructure().getServerName().equals(getter.getStructure().getServerName()))
				return;
		
		if(!getters.contains(getter))
			getters.add(getter);
		
		return;
		
	}
	
	public void removeGetter(GameGetter getter) {
		
		if(getters.contains(getter))
			getters.remove(getter);
		
		return;
		
	}
	
	public List<GameGetter> getGetters(){
		return getters;
	}
	
}
