package net.omniblock.core.protocol.manager.network.packets;

import net.omniblock.core.protocol.manager.network.packets.readers.*;

public class PacketsAdapter {

	public static void registerReaders(){
		
		ActionerReader.start();
		GamesReader.start();
		PlayersReader.start();
		ServersReader.start();
		StructuresReader.start();
		RequestReader.start();
		StructureSystemShopReader.start();
		SystemServerReader.start();
		
	}
	
}
