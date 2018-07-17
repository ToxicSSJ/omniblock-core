package net.omniblock.core.protocol.manager.network.packets.readers;

import net.omniblock.core.database.bases.ShopBase;
import net.omniblock.core.protocol.manager.network.SystemHandler;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.packet.GameShopInfoPacket;
import net.omniblock.packets.network.structure.packet.SendDataToServerPacket;
import net.omniblock.packets.network.structure.packet.SendInfoDataPacket;
import net.omniblock.packets.network.tool.object.PacketReader;

public final class StructureSystemShopReader {

	public static void start(){

		Packets.READER.registerReader(new PacketReader<GameShopInfoPacket>() {
			@Override
			public void readPacket(PacketSocketData<GameShopInfoPacket> packetSocketData) {

				PacketStructure structure = packetSocketData.getStructure();

				String servertype = structure.get(PacketStructure.DataType.STRINGS, "servertype");
				String section = structure.get(PacketStructure.DataType.STRINGS, "section");

				String jsonData = ShopBase.getShopData(section);

				Packets.STREAMER.streamPacket(new SendDataToServerPacket()
					.setData(jsonData)
					.build().setReceiver(SystemHandler.SYSTEM_REGISTER.get(servertype)));

			}

			@Override
			public Class<GameShopInfoPacket> getAttachedPacketClass() {
				return GameShopInfoPacket.class;
			}
		});


		Packets.READER.registerReader(new PacketReader<SendInfoDataPacket>() {
			@Override
			public void readPacket(PacketSocketData<SendInfoDataPacket> packetSocketData) {

				PacketStructure structure = packetSocketData.getStructure();

				String servertype = structure.get(PacketStructure.DataType.STRINGS, "servertype");
				String data = structure.get(PacketStructure.DataType.STRINGS, "data");

				ShopBase.setChangeData(servertype, data);

			}

			@Override
			public Class<SendInfoDataPacket> getAttachedPacketClass() {
				return SendInfoDataPacket.class;
			}
		});
	}
}
