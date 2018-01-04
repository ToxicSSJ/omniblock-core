package net.omniblock.core.protocol.manager.network.object.handler;

import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.packets.object.external.GamePreset;
import net.omniblock.packets.object.external.ServerType;

public class GameGetter {

	protected GameStructure structure;
	protected GamePreset preset;
	
	protected ServerType type;
	
	public GameGetter(GameStructure structure, GamePreset preset) {
		
		this.structure = structure;
		this.preset = preset;
		
		this.type = preset.getServertype();
		
	}
	
	public GameStructure getStructure() {
		return structure;
	}
	
	public GamePreset getPreset() {
		return preset;
	}
	
}
