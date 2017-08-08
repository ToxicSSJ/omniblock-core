package net.omniblock.core.protocol.manager.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.omniblock.core.protocol.manager.network.object.GameStructure;
import net.omniblock.core.protocol.manager.network.types.GameAttribute;
import net.omniblock.core.protocol.manager.network.types.GameStat;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.GameInitializerInfoPacket;
import net.omniblock.packets.network.structure.packet.PlayerSendMessagePacket;
import net.omniblock.packets.network.structure.packet.PlayerSendToNamedServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.GamePreset;

public class GameManager {

	public static Map<GamePreset, GameStructure> NEXT_GAME_SERVER = new HashMap<GamePreset, GameStructure>();
	public static ExecutorService NEXT_GAME_COMPARATOR = null;
	
	public static void start() {
		
		for(GamePreset gp : GamePreset.values()) {
			
			NEXT_GAME_SERVER.put(gp, null);
			
		}
		
		NEXT_GAME_COMPARATOR = Executors.newFixedThreadPool(1);
		
		NEXT_GAME_COMPARATOR.execute((new Runnable(){
			
            public void run(){
            	
            	K : while(true) {
                	
                	if(NetworkManager.NETWORK_GAMES.size() <= 0) {
    					sleep(250); 
    					continue K;
    				}
                	
                	Iterator<Map.Entry<GamePreset, GameStructure>> iterate = NEXT_GAME_SERVER.entrySet().iterator();
                	
                	E : while(iterate.hasNext()) {
                		
                		Map.Entry<GamePreset, GameStructure> next = iterate.next();
                		
                		if(next.getValue() == null) {
                			
                			Entry<String, GameStructure> search = NetworkManager.NETWORK_GAMES.entrySet().stream()
                					.filter(entry -> entry.getValue().getGamePreset() == next.getKey())
                					.filter(entry -> !entry.getValue().isLocked())
                					.filter(entry -> !entry.getValue().isNext())
                					.filter(entry -> !entry.getValue().isStarted())
                					.findAny().orElse(null);
                			
                			if(search != null) {
                				NEXT_GAME_SERVER.put(next.getKey(), search.getValue());
                			}
                			
                			continue E;
                			
                		} else {
                			
                			GameStructure linked_structure = next.getValue();
                			
                			Entry<String, GameStructure> first_search = NetworkManager.NETWORK_GAMES.entrySet().stream()
                					.filter(entry -> entry.getValue().getServerName() == linked_structure.getServerName())
                					.findAny().orElse(null);
                			
                			if(first_search != null) {
                				
                				GameStructure structure = first_search.getValue();
                				
                    			if(structure.isLocked() || structure.isStarted() || structure.isReload()) {
                    				
                    				structure.setAttribute(GameAttribute.NEXT, false);
                    				
                    				NEXT_GAME_SERVER.put(next.getKey(), null);
                    				
                    				Entry<String, GameStructure> second_search = NetworkManager.NETWORK_GAMES.entrySet().stream()
                    						.filter(entry -> NetworkManager.NETWORK_SERVERS.containsKey(entry.getValue().getServerName()))
                        					.filter(entry -> entry.getValue().getGamePreset() == next.getKey())
                        					.filter(entry -> !entry.getValue().isLocked())
                        					.filter(entry -> !entry.getValue().isNext())
                        					.filter(entry -> !entry.getValue().isReload())
                        					.findAny().orElse(null);
                        			
                        			if(second_search != null) {
                        				
                        				NEXT_GAME_SERVER.put(next.getKey(), second_search.getValue());
                        				continue E;
                        				
                        			}
                    				
                        			NEXT_GAME_SERVER.put(next.getKey(), null);
                        			continue E;
                        			
                    			}
                				
                    			if(!structure.isNext()) structure.setAttribute(GameAttribute.NEXT, true);
                    			continue E;
                    			
                			} else {
                				
                				NEXT_GAME_SERVER.put(next.getKey(), null);
                    			continue E;
                				
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
            
        }));
		
	}
	
	public static void sendPlayerToGame(String name, boolean party, GamePreset preset) {
		
		if(NEXT_GAME_SERVER.get(preset) == null) {
			
			GameStructure structure = startNewGame(preset);
			
			if(structure == null) {
				
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
					.setServername(structure.getServerName())
					.setParty(party)
					
					.build().setReceiver(PacketSenderType.OMNICORD));
			return;
			
		} else {
			
			if(NEXT_GAME_SERVER.get(preset).isLocked() || !NEXT_GAME_SERVER.get(preset).isStarted()
					|| !NetworkManager.NETWORK_SERVERS.containsKey(NEXT_GAME_SERVER.get(preset).getServerName())){
				
				NEXT_GAME_SERVER.put(preset, null);
				
				GameStructure structure = startNewGame(preset);
				
				if(structure == null) {
					
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
						.setServername(structure.getServerName())
						.setParty(party)
						
						.build().setReceiver(PacketSenderType.OMNICORD));
				return;
				
			}
			
			Packets.STREAMER.streamPacket(new PlayerSendToNamedServerPacket()
					
					.setPlayername(name)
					.setServername(NEXT_GAME_SERVER.get(preset).getServerName())
					.setParty(party)
					
					.build().setReceiver(PacketSenderType.OMNICORD));
			return;
			
		}
		
	}
	
	public static GameStructure startNewGame(GamePreset preset) {
		
		for(Map.Entry<String, GameStructure> entry : NetworkManager.NETWORK_GAMES.entrySet()) {
			if(NEXT_GAME_SERVER.get(preset) == null) {
				if(entry.getValue().getGamePreset().getServertype() == preset.getServertype()) {
					if(!entry.getValue().isLocked() && !entry.getValue().isStarted()) {
						initializeGame(entry.getValue(), preset);
						return entry.getValue();
					}
				}
			}
		}
		
		return null;
		
	}
	
	public static void setGameStarted(String servername, boolean started) {
		
		if(NetworkManager.NETWORK_GAMES.containsKey(servername)) {
			NetworkManager.NETWORK_GAMES.get(servername).setAttribute(GameAttribute.STARTED, started);
		}
		
		return;
		
	}
	
	public static void setGameReload(String servername, boolean reload) {
	
		if(NetworkManager.NETWORK_GAMES.containsKey(servername)) {
			NetworkManager.NETWORK_GAMES.get(servername).setAttribute(GameAttribute.RELOAD, reload);
		}
		
		return;
		
	}
	
	public static void setGameOnlineINFO(String servername, String mapname, int online, int max) {
		
		if(NetworkManager.NETWORK_GAMES.containsKey(servername)) {
			
			NetworkManager.NETWORK_GAMES.get(servername).setStat(GameStat.MAP_NAME, mapname, String.class);
			
			NetworkManager.NETWORK_GAMES.get(servername).setStat(GameStat.ONLINE_PLAYERS, online, Integer.class);
			NetworkManager.NETWORK_GAMES.get(servername).setStat(GameStat.MAXIMIUM_PLAYERS, max, Integer.class);
			
		}
		
		return;
		
	}
	
	private static void initializeGame(GameStructure structure, GamePreset preset) {
		
		if(!structure.isLocked() && !structure.isStarted()) {
			if(structure.getGamePreset().getServertype() == preset.getServertype()) {
				
				GameStructure cache = structure;
				
				Packets.STREAMER.streamPacket(new GameInitializerInfoPacket()
						
						.setServername(structure.getServerName())
						.setGamepreset(preset)
						.setSocketport(NetworkManager.NETWORK_SERVERS.get(structure.getServerName()).getSocketPort())
						
						.build().setReceiver(PacketSenderType.OMNICORD));
				
				cache.setAttribute(GameAttribute.NEXT, true);
				cache.setStat(GameStat.GAME_PRESET, preset.toString(), String.class);
				
				GameManager.NEXT_GAME_SERVER.put(preset, cache);
				return;
				
			}
		}
		
	}
	
}
