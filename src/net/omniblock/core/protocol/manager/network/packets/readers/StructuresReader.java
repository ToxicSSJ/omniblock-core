package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.core.protocol.manager.network.object.ServerStructure;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.packet.GameStructureInfoPacket;
import net.omniblock.packets.network.structure.packet.ServerStructureInfoPacket;
import net.omniblock.packets.network.tool.annotation.PacketEvent;
import net.omniblock.packets.network.tool.annotation.type.PacketPriority;
import net.omniblock.packets.network.tool.object.PacketReader;

public class StructuresReader {

	static {
	
		/*
		 * 
		 * El siguiente reader permite accionar
		 * y guardar la estructura de los paquetes
		 * de estructura tipo ServerStructure.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<ServerStructureInfoPacket>(){

			@Override
			@PacketEvent(priority = PacketPriority.CONSOLE)
			public void readPacket(PacketSocketData<ServerStructureInfoPacket> packetsocketdata) {
				
				ServerStructure structure = new ServerStructure(packetsocketdata.getStructure());
				NetworkManager.registerServer(structure);
				
				return;
		
			}

			@Override
			public Class<ServerStructureInfoPacket> getAttachedPacketClass() {
				return ServerStructureInfoPacket.class;
			}

			
		});
		
		/*
		 * 
		 * El siguiente reader permite accionar
		 * y guardar la estructura de los paquetes
		 * de estructura tipo GameStructure.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<GameStructureInfoPacket>(){

			@Override
			@PacketEvent(priority = PacketPriority.CONSOLE)
			public void readPacket(PacketSocketData<GameStructureInfoPacket> packetsocketdata) {
				
				GameStructure structure = new GameStructure(packetsocketdata.getStructure());
				NetworkManager.registerGame(structure);
				
				return;
		
			}

			@Override
			public Class<GameStructureInfoPacket> getAttachedPacketClass() {
				return GameStructureInfoPacket.class;
			}

			
		});
		
	}
	
}
