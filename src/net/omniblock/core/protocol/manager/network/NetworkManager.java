package net.omniblock.core.protocol.manager.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import net.omniblock.core.OmniCore;
import net.omniblock.core.protocol.console.Console;
import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.core.protocol.manager.network.object.ServerStructure;
import net.omniblock.core.protocol.manager.network.object.handler.GameGetter;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.socket.helper.SocketHelper;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.PlayerSendMessagePacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToNamedServerPacket;
import net.omniblock.packets.network.structure.packet.RequestPlayerGameLobbyServersPacket;
import net.omniblock.packets.network.structure.packet.ResposePlayerGameLobbiesPacket;
import net.omniblock.packets.network.structure.packet.ResposeReloadInfoPacket;
import net.omniblock.packets.network.structure.packet.ServerSocketInfoPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;

public class NetworkManager {

	protected static Map<String, ServerStructure> NETWORK_SERVERS = new HashMap<String, ServerStructure>();
	protected static Map<String, GameStructure> NETWORK_GAMES = new HashMap<String, GameStructure>();
	
	public static Thread NETWORK_UPDATER = null;
	public static Boolean MAINTENANCE_MODE = false;
	
	public static void start() {
		
		NETWORK_UPDATER = new Thread(new Runnable() {

			@Override
			public void run() {
				
				K : while(true) {
					
					try {
						
						Iterator<Map.Entry<String, ServerStructure>> iterate = NETWORK_SERVERS.entrySet().iterator();
		            	
		            	E : while(iterate.hasNext()) {
		            		
		            		Map.Entry<String, ServerStructure> next = iterate.next();
		            		
		            		if(next.getValue() != null) {
		            			
		            			if(!SocketHelper.isLocalPortInUse(
		            					NetworkManager.NETWORK_SERVERS.get(next.getValue().getServerName()).getServerPort())) {
		            				
		            				GameStructure game = NETWORK_GAMES.containsKey(next.getValue().getServerName()) ? NETWORK_GAMES.get(next.getValue().getServerName()) : null;
		            				NetworkManager.NETWORK_SERVERS.remove(next.getValue().getServerName());
		            				
		            				if(OmniCore.DEBUG)
		            					Console.WRITTER.printInfo("DEBUG : Se removió la estructura de servidor " + next.getValue().getServerName());
		            				
		            				if(game != null) {
		            					
		            					Iterator<GameGetter> game_iterate = GameManager.GAME_GETTER_HANDLER.getGetters().iterator();
		            					
		            					A : while(game_iterate.hasNext()) {
		            		        		
		            						GameGetter nextgame = game_iterate.next();
		            						
		            		        		if(nextgame.getStructure() == null)
		            		        			continue A;
		            		        		
		            						if(nextgame.getStructure().getServerName().equals(game.getServerName())) {
		            							GameManager.GAME_GETTER_HANDLER.removeGetter(nextgame);
		            							break A;
		            						}
		            						
		            					}
		            					
		            					NETWORK_GAMES.remove(game.getServerName());
		            					continue E;
		            					
		            				}
		            				
								}
		            			
		            		}
		            	}
					
		            	sleep(250);
		            	continue K;
						
					} catch(Exception e) {
						
						sleep(1);
						continue K;
						
					}
					
				}
            	
			}
			
			private void sleep(long time) {
            	
            	try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) { }
            	
            }
			
		});
		
		NETWORK_UPDATER.start();
		
	}
	
	public static void reloadServers(ServerType st) {
		
		NETWORK_SERVERS.entrySet().stream().filter(entry ->
			entry.getValue().getServerType() == st).forEach(entry ->
					{
						
						List<GameGetter> toRemoveGetters = Lists.newArrayList();
						List<String> toRemoveStructures = Lists.newArrayList();
						
						GameManager.GAME_GETTER_HANDLER.getGetters().forEach(getter -> {
							
							if(getter.getPreset().getServertype() == st)
								toRemoveGetters.add(getter);
							
						});
						
						NETWORK_GAMES.entrySet().forEach(k -> {
							
							if(!toRemoveStructures.contains(k.getKey()))
								if(k.getValue().getGamePreset().getServertype() == st)
									toRemoveStructures.add(k.getKey());
							
						});
						
						NETWORK_SERVERS.entrySet().forEach(k -> {
							
							if(!toRemoveStructures.contains(k.getKey()))
								if(k.getValue().getServerType() == st)
									toRemoveStructures.add(k.getKey());
							
						});
						
						toRemoveGetters.forEach(getter -> {
							GameManager.GAME_GETTER_HANDLER.removeGetter(getter);
						});
						
						toRemoveStructures.forEach(id -> {
							
							if(NETWORK_SERVERS.containsKey(id))
								NETWORK_SERVERS.remove(id);
							
							if(NETWORK_GAMES.containsKey(id))
								NETWORK_GAMES.remove(id);
							
						});
						
						Packets.STREAMER.streamPacket(new ResposeReloadInfoPacket()
								.build().setReceiver(entry.getValue().getSocketPort()));
						
					}
			);
		
	}
	
	public static InjectorStatus unregisterServer(String servername) {
		
		if(servername != null) {
			
			if(NETWORK_GAMES.containsKey(servername)) {
				NETWORK_GAMES.remove(servername);
			}
			
			Iterator<GameGetter> game_iterate = GameManager.GAME_GETTER_HANDLER.getGetters().iterator();
			
			while(game_iterate.hasNext()) {
        		
				GameGetter nextgame = game_iterate.next();
				
        		if(nextgame.getStructure() == null) continue;
        		
				if(nextgame.getStructure().getServerName().equals(servername)) {
					GameManager.GAME_GETTER_HANDLER.removeGetter(nextgame);
					break;
				}
				
			}
			
			if(NETWORK_SERVERS.containsKey(servername)) {
				NETWORK_SERVERS.remove(servername);
			}
			
			if(OmniCore.DEBUG)
				Console.WRITTER.printInfo("DEBUG : Se removió la estructura de servidor " + servername);
			
			return InjectorStatus.SUCESS;
			
		}
		
		return InjectorStatus.NOT_VALID;
		
	}
	
	public static InjectorStatus registerServer(ServerStructure serverstructure) {
		
		if(serverstructure != null) {
			
			if(NETWORK_GAMES.containsKey(serverstructure.getServerName()))
				NETWORK_GAMES.remove(serverstructure.getServerName());
			
			NETWORK_SERVERS.put(serverstructure.getServerName(), serverstructure);
			
			if(OmniCore.DEBUG)
				Console.WRITTER.printInfo("DEBUG : Se añadió la estructura de servidor " + serverstructure.getServerName());
			
			Packets.STREAMER.streamPacket(new ServerSocketInfoPacket()
					
					.setServername(serverstructure.getServerName())
					.setServersocket(serverstructure.getSocketPort())
					
					.build().setReceiver(PacketSenderType.OMNICORD));
			
			return InjectorStatus.SUCESS;
			
		}
		
		return InjectorStatus.NOT_VALID;
		
	}
	
	public static InjectorStatus registerGame(GameStructure gamestructure) {
		
		if(gamestructure != null) {
			
			if(!NETWORK_SERVERS.containsKey(gamestructure.getServerName())) {
				return InjectorStatus.CANNOT_REGISTER;
			}
			
			Iterator<GameGetter> game_iterate = GameManager.GAME_GETTER_HANDLER.getGetters().iterator();
			
			while(game_iterate.hasNext()) {
        		
				GameGetter nextgame = game_iterate.next();
				
        		if(nextgame.getStructure() == null) continue;
        		
				if(nextgame.getStructure().getGamePreset() == gamestructure.getGamePreset()) {
					GameManager.GAME_GETTER_HANDLER.removeGetter(nextgame);
					break;
				}
				
			}
        	
			NETWORK_GAMES.put(gamestructure.getServerName(), gamestructure);
			return InjectorStatus.SUCESS;
			
		}
		
		return InjectorStatus.NOT_VALID;
		
	}
	
	public static void sendPlayerToServer(String name, String servername){
		
		Entry<String, ServerStructure> search = NETWORK_SERVERS.entrySet().stream()
		.filter(entry -> entry.getValue().getServerName().equalsIgnoreCase(servername))
		.filter(entry -> !entry.getValue().isFull())
		.findAny().orElse(null);
		
		if(search == null) {
			
			Packets.STREAMER.streamPacket(new PlayerSendMessagePacket()
					
					.setPlayername(name)
					.setMessage("&cEl servidor al que estas intentando entrar está lleno o esta desactivado.")
					
					.build().setReceiver(PacketSenderType.OMNICORD));
			return;
			
		}
		
		Packets.STREAMER.streamPacket(new PlayerSendToNamedServerPacket()
				
				.setPlayername(name)
				.setServername(search.getValue().getServerName())
				.setParty(false)
				
				.build().setReceiver(PacketSenderType.OMNICORD));
		return;
		
	}
	
	public static void sendPlayerToServer(String name, ServerType type) {
		
		Entry<String, ServerStructure> search = NETWORK_SERVERS.entrySet().stream()
		.filter(entry -> entry.getValue().getServerType() == type)
		.filter(entry -> !entry.getValue().isFull())
		.findAny().orElse(null);
		
		if(search == null) {
			
			Packets.STREAMER.streamPacket(new PlayerSendMessagePacket()
					
					.setPlayername(name)
					.setMessage("[center]&cNo hay servidores disponibles :([/center][br]"
							 + "&8&m-&r &7Este mensaje quiere decir que nuestros fondos no son suficientes para cubrir tantos usuarios. De parte "
							 + "de todo el equipo agradecemos cualquier donación por nuestra tienda oficial, La cual usaremos para mejorar nuestros "
							 + "sistemas y podrás recibir grandes cosas a cambio! Gracias por jugar en OmniNetwork!")
					
					.build().setReceiver(PacketSenderType.OMNICORD));
			return;
			
		}
		
		Packets.STREAMER.streamPacket(new PlayerSendToNamedServerPacket()
				
				.setPlayername(name)
				.setServername(search.getValue().getServerName())
				.setParty(false)
				
				.build().setReceiver(PacketSenderType.OMNICORD));
		
		return;
		
	}
	
	public static void sendLobbyServers(PacketSocketData<RequestPlayerGameLobbyServersPacket> packetsocketdata){
		
		PacketStructure structure = packetsocketdata.getStructure();
		
		String playername = structure.get(DataType.STRINGS, "playername");
		String servername = structure.get(DataType.STRINGS, "servername");
		
		String servertype = structure.get(DataType.STRINGS, "servertype");
		
		Packets.STREAMER.streamPacket(new ResposePlayerGameLobbiesPacket()
    			.setPlayername(playername)
    			.setServers(getLobbyServers(ServerType.valueOf(servertype)))
    			.build()
    			
    			.setPacketUUID(packetsocketdata.getPacketUUID())
    			.setReceiver(getSocketServer(servername)));
		
	}
	
	public static String getLobbyServers(ServerType type){
		
		List<String> data = new ArrayList<String>();
		List<ServerStructure> structures = new ArrayList<ServerStructure>();
		
		for(Map.Entry<String, ServerStructure> k : NETWORK_SERVERS.entrySet()){
			if(k.getValue().getServerType() == type){
				structures.add(k.getValue());
			}
		}
		
		structures.stream().forEach(structure -> {
			
			data.add(structure.getServerName() + "*" + structure.getOnlinePlayers());
			
		});
		
		return String.join(",", data);
		
	}
	
	public static int getSocketServer(String servername){
		
		return NETWORK_SERVERS.get(servername).getSocketPort();
		
	}
	
	public static enum InjectorStatus {
		
		SUCESS("Se ha registrado correctamente!"),
		
		ALREADY_EXISTS("No se ha podido registrar porque este mismo servidor ya se encuentra en la lista."),
		PORT_OCCUPIED("No se ha podido registrar porque ya hay un servidor con el mismo puerto protocolario del Socket o del Server como tál."),
		
		CANNOT_DELETE("No se ha podido borrar porque se encuentra una tarea en ejecución."),
		CANNOT_REGISTER("No se ha podido registrar por alguna razón en particular."),
		
		NOT_VALID("La estructura no es valida, Por favor rectifiquela."),
		
		;
		
		private String statusmsg;
		
		InjectorStatus(String statusmsg){
			this.statusmsg = statusmsg;
		}

		public String getStatusMSG() {
			return statusmsg;
		}

		public void setStatusMSG(String statusmsg) {
			this.statusmsg = statusmsg;
		}
		
	}
	
}
