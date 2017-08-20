package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.ServerRemoveInfoPacket;
import net.omniblock.packets.network.tool.object.PacketReader;

public class ServersReader {

	public static void start() {
		
		/*
		 * 
		 * Este reader es el encargado de remover
		 * del registro un servidor en base a la
		 * información contenida en el paquete para
		 * removerlo.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<ServerRemoveInfoPacket>(){

			@Override
			public void readPacket(PacketSocketData<ServerRemoveInfoPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String servername = structure.get(DataType.STRINGS, "servername");
				
				NetworkManager.unregisterServer(servername);
				return;
				
			}

			@Override
			public Class<ServerRemoveInfoPacket> getAttachedPacketClass() {
				return ServerRemoveInfoPacket.class;
			}
			
		});
		
	}
	
}
