package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.protocol.manager.network.SystemHandler;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.packet.RegisterSystemServerPacket;
import net.omniblock.packets.network.structure.packet.RemoveSystemServerPacket;
import net.omniblock.packets.network.tool.console.Console;
import net.omniblock.packets.network.tool.object.PacketReader;

import java.util.Map;


public final class SystemServerReader {

	public static void start(){

		Packets.READER.registerReader(new PacketReader<RegisterSystemServerPacket>() {
			@Override
			public void readPacket(PacketSocketData<RegisterSystemServerPacket> packetSocketData) {

				PacketStructure structure = packetSocketData.getStructure();

				String servertype = structure.get(PacketStructure.DataType.STRINGS, "servertype");
				int serversocket = structure.get(PacketStructure.DataType.INTEGERS, "serversocket");

				if(!SystemHandler.SYSTEM_REGISTER.containsKey(servertype)){

					SystemHandler.SYSTEM_REGISTER.put(servertype, serversocket);

					return;
				}
			}

			@Override
			public Class<RegisterSystemServerPacket> getAttachedPacketClass() {
				return RegisterSystemServerPacket.class;
			}
		});

		Packets.READER.registerReader(new PacketReader<RemoveSystemServerPacket>() {
			@Override
			public void readPacket(PacketSocketData<RemoveSystemServerPacket> packetSocketData) {

				PacketStructure structure = packetSocketData.getStructure();
				String servertype =  structure.get(PacketStructure.DataType.STRINGS, "servertype");

				for(Map.Entry<String, Integer> entry : SystemHandler.SYSTEM_REGISTER.entrySet())
					if(entry.getKey().equals(servertype))
						SystemHandler.SYSTEM_REGISTER.remove(entry.getKey());

			}

			@Override
			public Class<RemoveSystemServerPacket> getAttachedPacketClass() {
				return RemoveSystemServerPacket.class;
			}
		});
	}
}
