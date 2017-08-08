package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.PlayerSendToGamePacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToNamedServerPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.tool.object.PacketReader;
import net.omniblock.packets.object.external.GamePreset;
import net.omniblock.packets.object.external.ServerType;

public class PlayersReader {

	static {
		
		/*
		 * 
		 * El siguiente reader es el encargado de
		 * ejecutar los metodos para el envio de jugadores
		 * a un juego en especifico basandose en los
		 * servidores tipo juegos registrados por el
		 * sistema.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<PlayerSendToGamePacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendToGamePacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				GamePreset gamepreset = GamePreset.valueOf(structure.get(DataType.STRINGS, "gamepreset"));
				
				Boolean party = structure.get(DataType.BOOLEANS, structure.get(DataType.BOOLEANS, "party"));
				
				GameManager.sendPlayerToGame(playername, party, gamepreset);
				return;
				
			}

			@Override
			public Class<PlayerSendToGamePacket> getAttachedPacketClass() {
				return PlayerSendToGamePacket.class;
			}
			
		});
		
		/*
		 * 
		 * El siguiente reader es el encargado de
		 * ejecutar los metodos para el envio de jugadores
		 * a un tipo de servidor en especifico basandose 
		 * en los servidores abiertos del mismo tipo y sus
		 * atributos.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<PlayerSendToServerPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendToServerPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				ServerType type = ServerType.valueOf(structure.get(DataType.STRINGS, "servertype"));
				
				NetworkManager.sendPlayerToServer(playername, type);
				return;
				
			}

			@Override
			public Class<PlayerSendToServerPacket> getAttachedPacketClass() {
				return PlayerSendToServerPacket.class;
			}
			
		});
		
		/*
		 * 
		 * El siguiente reader es el encargado de
		 * ejecutar los metodos para el envio de jugadores
		 * a un servidor en especifico basandose en el
		 * nombre del servidor.
		 * 
		 */
		Packets.READER.registerReader(new PacketReader<PlayerSendToNamedServerPacket>(){

			@Override
			public void readPacket(PacketSocketData<PlayerSendToNamedServerPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String playername = structure.get(DataType.STRINGS, "playername");
				String servername = structure.get(DataType.STRINGS, "servername");
				
				NetworkManager.sendPlayerToServer(playername, servername);
				return;
				
			}

			@Override
			public Class<PlayerSendToNamedServerPacket> getAttachedPacketClass() {
				return PlayerSendToNamedServerPacket.class;
			}
			
		});
		
	}
	
}
