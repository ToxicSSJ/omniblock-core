package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.packet.RequestPlayerGameLobbyServersPacket;
import net.omniblock.packets.network.tool.object.PacketReader;

public class RequestReader {

	public static void start(){
		
		Packets.READER.registerReader(new PacketReader<RequestPlayerGameLobbyServersPacket>(){

			@Override
			public void readPacket(PacketSocketData<RequestPlayerGameLobbyServersPacket> packetsocketdata) {
				
				NetworkManager.sendLobbyServers(packetsocketdata);
				return;
				
			}

			@Override
			public Class<RequestPlayerGameLobbyServersPacket> getAttachedPacketClass() {
				return RequestPlayerGameLobbyServersPacket.class;
			}
			
		});
		
	}
	
}
