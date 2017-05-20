package net.omniblock.core.protocol.manager.network.handler;

import java.util.Map;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.NetworkManager.InjectorStatus;
import net.omniblock.core.protocol.manager.network.handler.Packet.PacketType;
import net.omniblock.core.protocol.manager.network.handler.exception.PacketHandlerException;
import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.core.protocol.manager.network.object.ServerStructure;
import net.omniblock.core.protocol.manager.network.reader.Reader;
import net.omniblock.core.protocol.manager.network.reader.type.ReaderStatus;
import net.omniblock.lib.json.JSONObject;

public class PacketHandler {

	public static int SUCESSFULLY_HANDLED_PACKETS = 0;
	public static int ERROR_HANDLED_PACKETS = 0;
	
	public static void unpackPackets(Map<PacketType, JSONObject> packets) {
		
		packets.forEach(((k, v) -> {
			{
				unpackPacket(k, v);
			}
		}));
		
	}
	
	protected static void unpackPacket(PacketType type, JSONObject object) {
		
		if(type == PacketType.GAME_STRUCTURE) {
			
			GameStructure structure = wrapGameStructure(object);
			InjectorStatus status = NetworkManager.registerGame(structure);
			
			if(status == InjectorStatus.SUCESS) {
				SUCESSFULLY_HANDLED_PACKETS++;
				return;
			}
			
			ERROR_HANDLED_PACKETS++;
			throw new PacketHandlerException(
												"\n" + "Un paquete ha lanzado un error!" +
												"\n" + "   Información del paquete:" +
											    "\n" +
												"\n" + "            Identidad = " + Packet.PACKET_ID +
												"\n" + "            Tipo = " + type.toString().toLowerCase() +
												"\n" + "            Status = " + status.getStatusMSG() + 
												"\n" + 
												"\n");
			
		} else if(type == PacketType.SERVER_STRUCTURE) {
			
			ServerStructure structure = wrapServerStructure(object);
			InjectorStatus status = NetworkManager.registerServer(structure);
			
			if(status == InjectorStatus.SUCESS) {
				SUCESSFULLY_HANDLED_PACKETS++;
				return;
			}
			
			ERROR_HANDLED_PACKETS++;
			throw new PacketHandlerException(
												"\n" + "Un paquete ha lanzado un error!" +
												"\n" + "   Información del paquete:" +
											    "\n" +
												"\n" + "            Identidad = " + Packet.PACKET_ID +
												"\n" + "            Tipo = " + type.toString().toLowerCase() +
												"\n" + "            Status = " + status.getStatusMSG() + 
												"\n" + 
												"\n");
			
		} else if(type == PacketType.MESSAGE_ACTIONER) {
			
			ReaderStatus status = Reader.MESSAGE_READER.isValidReader(object) ?
				
				Reader.MESSAGE_READER.read(object) : 
				Reader.EXTRAINFO_READER.read(object);
			
			if(status == ReaderStatus.SUCESS) {
				SUCESSFULLY_HANDLED_PACKETS++;
				return;
			}
			
			ERROR_HANDLED_PACKETS++;
			throw new PacketHandlerException(
												"\n" + "Un paquete ha lanzado un error!" +
												"\n" + "   Información del paquete:" +
											    "\n" +
												"\n" + "            Identidad = " + Packet.PACKET_ID +
												"\n" + "            Tipo = " + type.toString().toLowerCase() +
												"\n" + "            Status = " + status.getStatusMSG() + 
												"\n" + 
												"\n");
			
		}
		
	}
	
	protected static GameStructure wrapGameStructure(JSONObject object) {
		
		return new GameStructure(object, Packet.PACKET_ID);
		
	}
	
	protected static ServerStructure wrapServerStructure(JSONObject object) {
		
		return new ServerStructure(object, Packet.PACKET_ID);
		
	}

}
