package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.GameOnlineInfoPacket;
import net.omniblock.packets.network.tool.object.PacketReader;

public class GamesReader {

	static {
		
		/*
		 * 
		 * Este reader es el encargado de actualizar
		 * la información de el estado de un juego en
		 * base al paquete que contiene dicha información.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<GameOnlineInfoPacket>(){

			@Override
			public void readPacket(PacketSocketData<GameOnlineInfoPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String servername = structure.get(DataType.STRINGS, "servername");
				String mapname = structure.get(DataType.STRINGS, "mapname");
				
				Integer onlineplayers = structure.get(DataType.INTEGERS, "onlineplayers");
				Integer maxplayers = structure.get(DataType.INTEGERS, "maxplayers");
				
				GameManager.setGameOnlineINFO(servername, mapname, onlineplayers, maxplayers);
				return;
				
			}

			@Override
			public Class<GameOnlineInfoPacket> getAttachedPacketClass() {
				return GameOnlineInfoPacket.class;
			}
			
		});
		
	}
	
}
