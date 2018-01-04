package net.omniblock.core.protocol.manager.network;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.omniblock.core.OmniCore;
import net.omniblock.core.protocol.console.Console;
import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.core.protocol.manager.network.object.handler.GameGetter;
import net.omniblock.core.protocol.manager.network.object.handler.GameGetterHandler;
import net.omniblock.core.protocol.manager.network.types.GameAttribute;
import net.omniblock.core.protocol.manager.network.types.GameStat;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.socket.type.DataSenderStatus;
import net.omniblock.packets.network.structure.packet.GameInitializerInfoPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendMessagePacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToNamedServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.GamePreset;

public class GameManager {
	
	public static GameGetterHandler GAME_GETTER_HANDLER = new GameGetterHandler();
	public static Thread NEXT_GAME_COMPARATOR = null;
	
	public static void start() {
		
		NEXT_GAME_COMPARATOR = new Thread((new Runnable(){
			
            public void run(){
            	
            	K : while(true) {
                	
                	if(NetworkManager.NETWORK_GAMES.size() <= 0) {
    					sleep(250); 
    					continue K;
    				}
                	
                	try {
                		
                		E : for(GamePreset preset : GamePreset.values()) {
                    		
                			if(preset == GamePreset.NONE || preset.isMask())
                				continue E;
                			
                    		if(!GAME_GETTER_HANDLER.hasNext(preset)) {
                    			
                    			Entry<String, GameStructure> search = NetworkManager.NETWORK_GAMES.entrySet().stream()
                    					.filter(entry -> NetworkManager.NETWORK_SERVERS.containsKey(entry.getKey()))
                    					.filter(entry -> entry.getValue().getGamePreset().getServertype() == preset.getServertype())
                    					.filter(entry -> entry.getValue().getGamePreset() == preset)
                    					.filter(entry -> !entry.getValue().isLocked())
                    					.filter(entry -> !entry.getValue().isNext())
                    					.filter(entry -> !entry.getValue().isStarted())
                    					.findAny().orElse(null);
                    			
                    			if(search != null && OmniCore.DEBUG)
                    				Console.WRITTER.printInfo("DEBUG : Se añadió una nueva preset " + preset.name() + " | " + search.getValue().getServerName());
                    			
                    			if(search != null)
                    				search.getValue().setAttribute(GameAttribute.NEXT, true);
                    			
                    			if(search != null)
                    				GAME_GETTER_HANDLER.addGetter(new GameGetter(search.getValue(), preset));
                    			
                    			continue E;
                    			
                    		}
                    		
                    		GameGetter getter = GAME_GETTER_HANDLER.next(preset);
                    		
                    		Entry<String, GameStructure> search = NetworkManager.NETWORK_GAMES.entrySet().stream()
                    				.filter(entry -> NetworkManager.NETWORK_SERVERS.containsKey(entry.getKey()))
                					.filter(entry -> entry.getValue().getServerName().equals(getter.getStructure().getServerName()))
                					.filter(entry -> !entry.getValue().isLocked())
                					.filter(entry -> entry.getValue().isNext())
                					.filter(entry -> !entry.getValue().isStarted())
                					.findAny().orElse(null);
                    		
                    		if(search != null) {
                    			
                    			// if(OmniCore.DEBUG)
                    				// Console.WRITTER.printWarning("DEBUG : Analisis de estructura " + search.getValue().getServerName() + " | PRESET -> " + search.getValue().getGamePreset().name() + " | LOCKED -> " + search.getValue().isLocked());
                    			
                    			if(!NetworkManager.NETWORK_SERVERS.containsKey(search.getValue().getServerName())) {
                    				
                    				NetworkManager.NETWORK_GAMES.remove(search.getValue().getServerName());
                    				GAME_GETTER_HANDLER.removeGetter(getter);
                    				
                    				if(OmniCore.DEBUG)
                    					Console.WRITTER.printInfo("DEBUG : #1 : Se removió el GameGetter " + search.getValue().getServerName());
                    				
                    				continue E;
                    				
                    			}
                    			
                    		}
                    		
                    		if(search != null && !NetworkManager.NETWORK_SERVERS.containsKey(search.getKey())) {
                				
                				GAME_GETTER_HANDLER.removeGetter(getter);
                				
                				if(OmniCore.DEBUG)
                					Console.WRITTER.printInfo("DEBUG : #2 : Se removió el GameGetter " + search.getValue().getServerName());
                				
                				continue E;
                				
                			}
                    		
                    		if(search == null) {
                    			
                    			GAME_GETTER_HANDLER.removeGetter(getter);
                    			
                    			if(OmniCore.DEBUG)
                					Console.WRITTER.printInfo("DEBUG : #3 : Se removió el GameGetter " + getter.getStructure().getServerName());
                    			
                    			continue E;
                    			
                    		}
                    		
                    		continue E;
                    		
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
            
        }));
		
		NEXT_GAME_COMPARATOR.start();
		
	}
	
	public static void sendPlayerToGame(String name, boolean party, GamePreset preset) {
		
		if(!GAME_GETTER_HANDLER.hasNext(preset)) {
			
			Entry<DataSenderStatus, GameStructure> entry = startNewGame(preset);
			
			if(entry.getKey() == DataSenderStatus.ERROR || entry.getValue() == null) {
				
				Packets.STREAMER.streamPacket(new PlayerSendMessagePacket()
						
						.setPlayername(name)
						.setMessage("[center]&cNo hay servidores disponibles :([/center][br]"
								 + "&8&m-&r &7En OmniNetwork contamos con más de &a20&7 servidores por cada una de nuestras modalidades, este mensaje quiere decir "
								 + "que nuestros fondos no son suficientes para cubrir tantos usuarios. De parte de todo el equipo agradecemos cualquier "
								 + "donación por nuestra tienda oficial, La cual usaremos para mejorar nuestros sistemas y podrás recibir grandes "
								 + "cosas a cambio! Gracias por jugar en OmniNetwork!")
						
						.build().setReceiver(PacketSenderType.OMNICORD));
				return;
				
			}
			
			Packets.STREAMER.streamPacket(new PlayerSendToNamedServerPacket()
					
					.setPlayername(name)
					.setServername(entry.getValue().getServerName())
					.setParty(party)
					
					.build().setReceiver(PacketSenderType.OMNICORD));
			return;
			
		}
		
		Packets.STREAMER.streamPacket(new PlayerSendToNamedServerPacket()
				
				.setPlayername(name)
				.setServername(GAME_GETTER_HANDLER.next(preset).getStructure().getServerName())
				.setParty(party)
				
				.build().setReceiver(PacketSenderType.OMNICORD));
		return;
		
	}
	
	public static Map.Entry<DataSenderStatus, GameStructure> startNewGame(GamePreset preset) {
		
		if(!GAME_GETTER_HANDLER.hasNext(preset)) {
			
			GameStructure search = NetworkManager.NETWORK_GAMES.values().stream()
					.filter(entry -> entry.getGamePreset().getServertype() == preset.getServertype())
					.filter(entry -> !entry.isLocked() && !entry.isStarted())
					.filter(entry -> !entry.getGamePreset().isMask())
					.filter(entry -> entry.getGamePreset() == preset)
					.findAny().orElse(null);
			
			if(search == null){
				
				search = NetworkManager.NETWORK_GAMES.values().stream()
						.filter(entry -> entry.getGamePreset().getServertype() == preset.getServertype())
						.filter(entry -> !entry.isLocked() && !entry.isStarted())
						.filter(entry -> entry.getGamePreset().isMask())
						.findAny().orElse(null);
				
			}
			
			return new AbstractMap.SimpleEntry<DataSenderStatus, GameStructure>(initializeGame(search, preset), search);
			
		}
		
		return new AbstractMap.SimpleEntry<DataSenderStatus, GameStructure>(DataSenderStatus.SENDED, GAME_GETTER_HANDLER.next(preset).getStructure());
		
	}
	
	public static void setGameOpened(String servername, boolean opened) {
		
		if(NetworkManager.NETWORK_GAMES.containsKey(servername))
			NetworkManager.NETWORK_GAMES.get(servername).setAttribute(GameAttribute.LOCKED, !opened);
		
		return;
		
	}
	
	public static void setGameStarted(String servername, boolean started) {
		
		if(NetworkManager.NETWORK_GAMES.containsKey(servername))
			NetworkManager.NETWORK_GAMES.get(servername).setAttribute(GameAttribute.STARTED, started);
		
		return;
		
	}
	
	public static void setGameReload(String servername, boolean reload) {
	
		if(NetworkManager.NETWORK_GAMES.containsKey(servername))
			NetworkManager.NETWORK_GAMES.get(servername).setAttribute(GameAttribute.RELOAD, reload);
		
		return;
		
	}
	
	public static void setGameOnlineINFO(String servername, String mapname, int online, int max) {
		
		if(NetworkManager.NETWORK_GAMES.containsKey(servername)) {
			
			NetworkManager.NETWORK_GAMES.get(servername).setStat(GameStat.MAP_NAME, mapname);
			NetworkManager.NETWORK_GAMES.get(servername).setStat(GameStat.MAXIMIUM_PLAYERS, max);
			NetworkManager.NETWORK_GAMES.get(servername).setStat(GameStat.ONLINE_PLAYERS, online);
			
		}
		
		return;
		
	}
	
	private static DataSenderStatus initializeGame(GameStructure structure, GamePreset preset) {
		
		if(structure == null) return DataSenderStatus.ERROR;
		if(GAME_GETTER_HANDLER.hasNext(preset)) return DataSenderStatus.ERROR;
		
		if(!structure.isLocked() && !structure.isStarted()) {
			if(structure.getGamePreset().getServertype() == preset.getServertype()) {
				
				if(OmniCore.DEBUG)
    				Console.WRITTER.printInfo("DEBUG : Se enviará la inicialización de juego a la estructura: " + structure.getServerName() + " | " + preset.name());
				
				GameStructure cache = structure;
				
				DataSenderStatus status = Packets.STREAMER.streamPacket(new GameInitializerInfoPacket()
						
						.setData("")
						.setServername(structure.getServerName())
						.setGamepreset(preset)
						.setSocketport(NetworkManager.NETWORK_SERVERS.get(structure.getServerName()).getSocketPort())
						
						.build().setReceiver(NetworkManager.NETWORK_SERVERS.get(structure.getServerName()).getSocketPort()));
				
				cache.setAttribute(GameAttribute.NEXT, true);
				cache.setStat(GameStat.GAME_PRESET, preset.toString());
				
				if(OmniCore.DEBUG)
    				Console.WRITTER.printInfo("DEBUG : El status del envio de la estructura es: " + status.name());
				
				GAME_GETTER_HANDLER.addGetter(new GameGetter(cache, preset));
				return status;
				
			}
		}
		
		return DataSenderStatus.ERROR;
		
	}
	
}
