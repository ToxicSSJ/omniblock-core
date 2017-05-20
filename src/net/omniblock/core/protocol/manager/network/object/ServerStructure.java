package net.omniblock.core.protocol.manager.network.object;

import java.util.HashMap;
import java.util.Map;

import net.omniblock.core.protocol.manager.network.types.ServerAttribute;
import net.omniblock.core.protocol.manager.network.types.ServerStat;
import net.omniblock.core.protocol.manager.network.types.ServerType;
import net.omniblock.lib.json.JSONObject;

/**
 * 
 * Clase que será el constructor de la estructura de un servidor
 * la cual es leida por medio JSON.
 * 
 * @author zlToxicNetherlz
 *
 */
public class ServerStructure {

	private int PACKET_ID = -1;
	
	protected JSONObject information;
	protected ServerStructureHandler serverstructurehandler;
	
	protected Map<ServerAttribute, Boolean> attributes = new HashMap<ServerAttribute, Boolean>();
	protected Map<ServerStat, Object> stats = new HashMap<ServerStat, Object>();
	
	/**
	 * 
	 * Constructor de la clase, En este constructor se inicializa el handler que a su vez
	 * se encarga de leer todas las etiquetas en el JSON de información impartido como un
	 * parametro, Luego de ser leidas las etiquetas son pegadas en 2 mapas y pueden ser
	 * obtenidos los valores por separado por medio de los metodos que se encuentrane en
	 * la clase.
	 * 
	 * @param information JSON con las etiquetas que leerá el handler.
	 * @param packetid Identidad del paquete (se usa para identificar la entrada de la estructura).
	 */
	public ServerStructure(JSONObject information, int packetid) {
		
		if(information == null) {
			throw new NullPointerException("La información del paquete #" + packetid + " no contiene ningun valor para ser leido.");
		}
		
		serverstructurehandler = new ServerStructureHandler(information);
		
		attributes = serverstructurehandler.getAttributes();
		stats = serverstructurehandler.getStats();
		
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado EXTRA_INFO.
	 * 
	 * @return La información extra del servidor.
	 */
	public String getExtraInfo() {
		
		if(stats.containsKey(ServerStat.EXTRA_INFO)) {
			return (String) stats.get(ServerStat.EXTRA_INFO);
		}
		
		return "none";
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado MAP_NAME.
	 * 
	 * @return El nombre del mapa proveido por el servidor.
	 */
	public String getMapName() {
		if(stats.containsKey(ServerStat.MAP_NAME)) {
			return (String) stats.get(ServerStat.MAP_NAME);
		}
		
		return "world";
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado SERVER_NAME.
	 * 
	 * @return El nombre del servidor registrado en el <code> Bukkit.getServername(); </code>
	 */
	public String getServerName() {
		if(stats.containsKey(ServerStat.SERVER_NAME)) {
			return (String) stats.get(ServerStat.SERVER_NAME);
		}
		
		return "UNKNOW";
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado SERVER_TYPE.
	 * 
	 * @return El tipo de servidor proveido.
	 */
	public ServerType getServerType() {
		if(stats.containsKey(ServerStat.SERVER_TYPE)) {
			return ServerType.valueOf((String) stats.get(ServerStat.SERVER_TYPE));
		}
		
		return ServerType.MAIN_LOBBY_SERVER;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado SOCKET_PORT.
	 * 
	 * @return El puerto socket donde recibe información el servidor.
	 */
	public int getSocketPort() {
		if(stats.containsKey(ServerStat.SOCKET_PORT)) {
			return (int) stats.get(ServerStat.SOCKET_PORT);
		}
		
		return -1;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado SERVER_PORT.
	 * 
	 * @return El puerto donde está corriendo el servidor.
	 */
	public int getServerPort() {
		if(stats.containsKey(ServerStat.SERVER_PORT)) {
			return (int) stats.get(ServerStat.SERVER_PORT);
		}
		
		return 25565;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado MAX_ONLINE_PLAYERS.
	 * 
	 * @return El numero maximo de jugadores que puede albergar el servidor.
	 */
	public int getMaxPlayers() {
		if(stats.containsKey(ServerStat.MAX_ONLINE_PLAYERS)) {
			return (int) stats.get(ServerStat.MAX_ONLINE_PLAYERS);
		}
		
		return 100;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado ONLINE_PLAYERS.
	 * 
	 * @return El numero de jugadores onlines del servidor desde la ultima actualización de la estructura.
	 */
	public int getOnlinePlayers() {
		if(stats.containsKey(ServerStat.ONLINE_PLAYERS)) {
			return (int) stats.get(ServerStat.ONLINE_PLAYERS);
		}
		
		return 0;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado FULL.
	 * 
	 * @return true si el servidor esta lleno.
	 */
	public boolean isFull() {
		if(attributes.containsKey(ServerAttribute.FULL)) {
			return attributes.get(ServerAttribute.FULL);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado OTHER_SERVER.
	 * 
	 * @return true si el servidor pertenece a la categoria de Otros servidores.
	 */
	public boolean isOtherServer() {
		if(attributes.containsKey(ServerAttribute.OTHER_SERVER)) {
			return attributes.get(ServerAttribute.OTHER_SERVER);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado STAFF_SERVER.
	 * 
	 * @return true si el servidor pertenece a la categoria de Servidores para el Staff.
	 */
	public boolean isStaffServer() {
		if(attributes.containsKey(ServerAttribute.STAFF_SERVER)) {
			return attributes.get(ServerAttribute.STAFF_SERVER);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado GAME_LOBBY_SERVER.
	 * 
	 * @return true si el servidor pertenece a la categoria de Servidores para lobbies de juegos.
	 */
	public boolean isGameLobbyServer() {
		if(attributes.containsKey(ServerAttribute.GAME_LOBBY_SERVER)) {
			return attributes.get(ServerAttribute.GAME_LOBBY_SERVER);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado GAME_SERVER.
	 * 
	 * @return true si el servidor pertenece a la categoria de Servidores para juegos.
	 */
	public boolean isGameServer() {
		if(attributes.containsKey(ServerAttribute.GAME_SERVER)) {
			return attributes.get(ServerAttribute.GAME_SERVER);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado LOBBY_SERVER.
	 * 
	 * @return true si el servidor pertenece a la categoria de Servidores para la lobbie principal.
	 */
	public boolean isLobbyServer() {
		if(attributes.containsKey(ServerAttribute.LOBBY_SERVER)) {
			return attributes.get(ServerAttribute.LOBBY_SERVER);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Devuelve la identidad del paquete que se inserto
	 * en la estructura.
	 * 
	 * @return La identidad del paquete insertada en la estructura.
	 */
	public int getPacketID() {
		return PACKET_ID;
	}

	/**
	 * 
	 * Clase tipo Handler que tiene metodos para la redacción del JSON con la información base de
	 * la estructura general del servidor.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public static class ServerStructureHandler {
		
		protected JSONObject object;
		
		/**
		 * 
		 * Constructor principal de la clase.
		 * 
		 * @param object JSON con la información de la estructura del servidor.
		 */
		public ServerStructureHandler(JSONObject object) {
			
			this.object = object;
			
		}

		/**
		 * 
		 * Genera un mapa con todos los stats encontrados en el JSON
		 * con los siguientes argumentos:
		 * <br> </br>
		 * <code> KEY = ServerStat.class | Value = Object.class </code>
		 * 
		 * @return Devuelve el mapa con los Stats encontrados.
		 */
		public Map<ServerStat, Object> getStats() {
			
			Map<ServerStat, Object> stats = new HashMap<ServerStat, Object>();
			
			if(object.has("stats")) {
				
				JSONObject json_stats = (JSONObject) object.get("stats");
				
				for(ServerStat stat : ServerStat.values()) {
					
					if(json_stats.has(stat.getStatkey())) {
						
						if(stat.getClazz().isAssignableFrom(String.class)) {
							
							String value = json_stats.getString(stat.getStatkey());
							stats.put(stat, value);
							
						} else if(stat.getClazz().isAssignableFrom(Integer.class)) {
							
							Integer value = json_stats.getInt(stat.getStatkey());
							stats.put(stat, value);
							
						}
						
					}
					
				}
	            
			}
			
			for(ServerStat stat : ServerStat.values()) {
				
				if(!stats.containsKey(stat)) {
					
					if(stat.getClazz().isAssignableFrom(String.class)) {
						
						String value = "notvalue";
						stats.put(stat, value);
						
					} else if(stat.getClazz().isAssignableFrom(Integer.class)) {
						
						Integer value = 0;
						stats.put(stat, value);
						
					}
					
				}
				
			}
			
			return stats;
		}
		
		/**
		 * 
		 * Genera un mapa con todos los atributos encontrados en el JSON
		 * con los siguientes argumentos:
		 * <br> </br>
		 * <code> KEY = ServerAttribute.class | Value = Boolean.class </code>
		 * 
		 * @return Devuelve el mapa con los Atributos encontrados.
		 */
		public Map<ServerAttribute, Boolean> getAttributes() {
			
			Map<ServerAttribute, Boolean> attributes = new HashMap<ServerAttribute, Boolean>();
			
			if(object.has("attributes")) {
				
				JSONObject json_attributes = (JSONObject) object.get("attributes");
				
				for(ServerAttribute attribute : ServerAttribute.values()) {
					
					if(json_attributes.has(attribute.getAttribute())) {
						attributes.put(attribute, json_attributes.getBoolean(attribute.getAttribute()));
						continue;
					}
					
					attributes.put(attribute, false);
					
				}
	            
			}
			
			return attributes;
			
		}
		
		/**
		 * 
		 * Metodo que devolverá la información de la estructura del servidor.
		 * 
		 * @return JSON de la información de la estructura del servidor.
		 */
		protected JSONObject getObject() {
			return object;
		}
		
	}
	
}
