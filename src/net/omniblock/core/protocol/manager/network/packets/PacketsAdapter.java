package net.omniblock.core.protocol.manager.network.packets;

import net.omniblock.core.protocol.manager.network.packets.readers.ActionerReader;
import net.omniblock.core.protocol.manager.network.packets.readers.GamesReader;
import net.omniblock.core.protocol.manager.network.packets.readers.PlayersReader;
import net.omniblock.core.protocol.manager.network.packets.readers.RequestReader;
import net.omniblock.core.protocol.manager.network.packets.readers.ServersReader;
import net.omniblock.core.protocol.manager.network.packets.readers.StructuresReader;

public class PacketsAdapter {

	public static void registerReaders(){
		
		ActionerReader.start();
		GamesReader.start();
		PlayersReader.start();
		ServersReader.start();
		StructuresReader.start();
		RequestReader.start();
		
	}
	
}
