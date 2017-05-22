package net.omniblock.core.protocol.manager.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.omniblock.core.protocol.manager.network.handler.cord.ProxyServer;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.core.protocol.manager.network.object.ServerStructure;
import net.omniblock.core.protocol.manager.network.reader.MessageReader.MessageType;
import net.omniblock.core.protocol.manager.network.types.GamePreset;
import net.omniblock.core.protocol.manager.network.types.ServerType;

public class NetworkManager {

	protected static Map<String, ServerStructure> NETWORK_SERVERS = new HashMap<String, ServerStructure>();
	protected static Map<String, GameStructure> NETWORK_GAMES = new HashMap<String, GameStructure>();
	
	public static ExecutorService NETWORK_UPDATER = null;
	public static Boolean MAINTENANCE_MODE = false;
	
	public static void start() {
		
		NETWORK_UPDATER = Executors.newFixedThreadPool(1);
		
		NETWORK_UPDATER.execute(new Runnable() {

			@Override
			public void run() {
				
				K : while(true) {
					
					if(NETWORK_SERVERS.size() <= 0) {
						sleep(250);
						continue K;
					}
					
					Iterator<Map.Entry<String, ServerStructure>> iterate = NETWORK_SERVERS.entrySet().iterator();
	            	
	            	E : while(iterate.hasNext()) {
	            		
	            		Map.Entry<String, ServerStructure> next = iterate.next();
	            		
	            		if(next.getValue() != null) {
	            			
	            			if(!ProxyServer.getSocketAdapter().isLocalPortInUse(
	            					NetworkManager.NETWORK_SERVERS.get(next.getValue().getServerName()).getServerPort())) {
	            				
	            				GameStructure game = null;
	            				
	            				NetworkManager.NETWORK_SERVERS.remove(next.getValue().getServerName());
	            				
	            				if(NETWORK_GAMES.containsKey(next.getValue().getServerName())) game = NETWORK_GAMES.get(next.getValue().getServerName());
	            				
	            				if(game != null) {
	            					
	            					Iterator<Map.Entry<GamePreset, GameStructure>> game_iterate = GameManager.NEXT_GAME_SERVER.entrySet().iterator();
	            					
	            					A : while(game_iterate.hasNext()) {
	            		        		
	            		        		Map.Entry<GamePreset, GameStructure> nextgame = game_iterate.next();
	            						
	            		        		if(next.getValue() == null) continue A;
	            		        		
	            						if(next.getValue().getServerName() == game.getServerName()) {
	            							GameManager.NEXT_GAME_SERVER.put(nextgame.getKey(), null);
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
					
				}
            	
			}
			
			private void sleep(long time) {
            	
            	try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) { }
            	
            }
			
		});
		
	}
	
	public static void reloadServers(ServerType st) {
		
		NETWORK_SERVERS.entrySet().stream().filter(entry ->
			entry.getValue().getServerType() == st).forEach(entry ->
					{
						
						PacketModifier modifier = new PacketModifier()
								.addString(MessageType.SERVER_RELOAD_INFO.getKey())
								.addString(entry.getKey())
								.addInteger(entry.getValue().getSocketPort());
						
						ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
						
					}
			);
		
	}
	
	public static InjectorStatus unregisterServer(String servername) {
		
		if(servername != null) {
			
			if(NETWORK_GAMES.containsKey(servername)) {
				NETWORK_GAMES.remove(servername);
			}
			
			if(NETWORK_SERVERS.containsKey(servername)) {
				NETWORK_SERVERS.remove(servername);
			}
			
			return InjectorStatus.SUCESS;
			
		}
		
		return InjectorStatus.NOT_VALID;
		
	}
	
	public static InjectorStatus registerServer(ServerStructure serverstructure) {
		
		if(serverstructure != null) {
			
			if(NETWORK_GAMES.containsKey(serverstructure.getServerName())) {
				NETWORK_GAMES.remove(serverstructure.getServerName());
			}
			
			NETWORK_SERVERS.put(serverstructure.getServerName(), serverstructure);
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.SERVER_SOCKET_INFO.getKey())
					.addString(serverstructure.getServerName())
					.addInteger(serverstructure.getSocketPort());
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return InjectorStatus.SUCESS;
			
		}
		
		return InjectorStatus.NOT_VALID;
		
	}
	
	public static InjectorStatus registerGame(GameStructure gamestructure) {
		
		if(gamestructure != null) {
			
			if(!NETWORK_SERVERS.containsKey(gamestructure.getServerName())) {
				return InjectorStatus.CANNOT_REGISTER;
			}
			
			Iterator<Map.Entry<GamePreset, GameStructure>> iterate = GameManager.NEXT_GAME_SERVER.entrySet().iterator();
			
        	while(iterate.hasNext()) {
        		
        		Map.Entry<GamePreset, GameStructure> next = iterate.next();
				
        		if(next.getValue() == null) continue;
        		
				if(next.getValue().getServerName() == gamestructure.getServerName()) {
					GameManager.NEXT_GAME_SERVER.put(next.getKey(), null);
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
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.PLAYER_SEND_MESSAGE.getKey())
					.addString(name)
					.addString("&cEl servidor al que estas intentando entrar está lleno o esta desactivado.");
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return;
			
		}
		
		PacketModifier modifier = new PacketModifier()
				.addString(MessageType.PLAYER_SEND_TO_SERVER.getKey())
				.addString(name)
				.addString(search.getValue().getServerName())
				.addBoolean(false);
		
		ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
		return;
		
	}
	
	public static void sendPlayerToServer(String name, ServerType type) {
		
		Entry<String, ServerStructure> search = NETWORK_SERVERS.entrySet().stream()
		.filter(entry -> entry.getValue().getServerType() == type)
		.filter(entry -> !entry.getValue().isFull())
		.findAny().orElse(null);
		
		if(search == null) {
			
			PacketModifier modifier = new PacketModifier()
					.addString(MessageType.PLAYER_SEND_MESSAGE.getKey())
					.addString(name)
					.addString("[center]&cNo hay servidores disponibles :([/center][br]"
							 + "&8&m-&r &7Este mensaje quiere decir que nuestros fondos no son suficientes para cubrir tantos usuarios. De parte "
							 + "de todo el equipo agradecemos cualquier donación por nuestra tienda oficial, La cual usaremos para mejorar nuestros "
							 + "sistemas y podrás recibir grandes cosas a cambio! Gracias por jugar en OmniNetwork!");
			
			ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
			return;
			
		}
		
		PacketModifier modifier = new PacketModifier()
				.addString(MessageType.PLAYER_SEND_TO_SERVER.getKey())
				.addString(name)
				.addString(search.getValue().getServerName())
				.addBoolean(false);
		
		ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
		return;
		
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
