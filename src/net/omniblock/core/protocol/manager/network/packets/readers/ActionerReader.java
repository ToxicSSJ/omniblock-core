package net.omniblock.core.protocol.manager.network.packets.readers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import net.omniblock.core.config.ConfigHandler;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.RequestActionExecutorPacket;
import net.omniblock.packets.network.structure.packet.ResposeActionExecutorPacket;
import net.omniblock.packets.network.tool.object.PacketReader;

public class ActionerReader {
	
	public static Map<String, String> data_types = new HashMap<String, String>();
	
	static {
		
		data_types.put(DataKey.SKYWARS_Z, "enabled");
		
	}
	
	public static void start() {
		
		Packets.READER.registerReader(new PacketReader<RequestActionExecutorPacket>(){

			@Override
			public void readPacket(PacketSocketData<RequestActionExecutorPacket> packetsocketdata) {
				
				PacketStructure structure = packetsocketdata.getStructure();
				
				String requestaction = structure.get(DataType.STRINGS, "requestaction");
				String[] args = ((String) structure.get(DataType.STRINGS, "args")).split(",");
				
				Integer requesterport = structure.get(DataType.INTEGERS, "requesterport");
				
				for(ActionExecutorType type : ActionExecutorType.values()) {
					
					if(type.getRequest().equalsIgnoreCase(requestaction)) {
						
						Packets.STREAMER.streamPacket(
								type.getExecutor().execute(args).build()
									.setPacketUUID(packetsocketdata.getPacketUUID())
									.setReceiver(requesterport));
						return;
						
					}
					
				}
				
				return;
				
			}

			@Override
			public Class<RequestActionExecutorPacket> getAttachedPacketClass() {
				return RequestActionExecutorPacket.class;
			}
			
		});
		
	}
	
	public static enum ActionExecutorType {
		
		CHECK_SKYWARS("skywarsrequest", 0, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				if(data_types.containsKey(DataKey.SKYWARS_Z))
					return new ResposeActionExecutorPacket()
							.setResponse(data_types.get(DataKey.SKYWARS_Z));
				
				return new ResposeActionExecutorPacket()
						.setResponse("enabled");
				
			}
			
		}),
		
		SET_SKYWARS("setskywarsrequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String type = args[0].toUpperCase();
				
				if(type.equalsIgnoreCase("z") || type.equalsIgnoreCase("normal"))
					data_types.put(DataKey.SKYWARS_Z, type.equalsIgnoreCase("z") ? "enabled" : "disabled");
				else
					return new ResposeActionExecutorPacket()
							.setResponse("¡" + type + " no es un modo de Skywars valido!");
				
				return new ResposeActionExecutorPacket()
						.setResponse("Se ha definido correctamente el modo de Skywars a " + type);
				
			}
			
		}),
		
		ADD_WEEKPRIZE("addweekprizerequest", 1, new ActionExecutor() {

			@Override
			public ResposeActionExecutorPacket execute(String[] args) {
				
				String setter = args[0].toLowerCase();
				
				String ddmmyy = args[1];
				String hhmmss = args[2];
				
				String datestr = StringUtils.join(new String[] {
						ddmmyy,
						hhmmss
				}, " ");
				
				if(StringUtils.countMatches(datestr, "/") == 2 && 
						StringUtils.countMatches(datestr, ":") == 2) {
					
					FileConfiguration config = ConfigHandler.getWeekPrizeConfig().getConfiguration();
					
					if(setter.equals("skywars")) {
						
						List<String> dates = config.isSet("weekprizes.skywars") ?
								config.getStringList("weekprizes.skywars") != null ?
										config.getStringList("weekprizes.skywars") : new ArrayList<String>() :
								new ArrayList<String>();
						
						dates.add(datestr);
						config.set("weekprizes.skywars", dates);
						
						ConfigHandler.getWeekPrizeConfig().save();
						
						return new ResposeActionExecutorPacket()
								.setResponse("Se ha añadido el registro correctamente!");
						
					}
					
					return new ResposeActionExecutorPacket()
							.setResponse("La modalidad " + args[0] + " no es valida!");
					
				}
				
				return new ResposeActionExecutorPacket()
						.setResponse("No se ha podido añadir un registro porque el formato es incorrecto!");
				
			}
			
		}),
		
		;
		
		private String request;
		private int argslength;
		
		private ActionExecutor executor;
		
		ActionExecutorType(String request, int argslength, ActionExecutor executor){
			
			this.request = request;
			
			this.argslength = argslength;
			this.executor = executor;
			
		}
		
		public ActionExecutor getExecutor() {
			return executor;
		}
		
		public String getRequest() {
			return request;
		}
		
		public int getArgsLength() {
			return argslength;
		}
		
		public static interface ActionExecutor {
			
			public ResposeActionExecutorPacket execute(String[] args);
			
		}
		
	}
	
	public static class DataKey {
		
		public static final String SKYWARS_Z = "skywars_z_data";
		
	}
	
}
