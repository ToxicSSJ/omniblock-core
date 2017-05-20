package net.omniblock.core.protocol.manager.network.object;

import java.util.HashMap;
import java.util.Map;

import net.omniblock.core.protocol.manager.network.types.GameAttribute;
import net.omniblock.core.protocol.manager.network.types.GamePreset;
import net.omniblock.core.protocol.manager.network.types.GameStat;
import net.omniblock.lib.json.JSONObject;

/**
 * 
 * Clase que será el constructor de la estructura de un juego
 * la cual es leida por medio JSON.
 * 
 * @author zlToxicNetherlz
 *
 */
public class GameStructure {

	private int PACKET_ID = -1;
	
	protected JSONObject information;
	protected GameStructureHandler gamestructurehandler;
	
	protected Map<GameAttribute, Boolean> attributes = new HashMap<GameAttribute, Boolean>();
	protected Map<GameStat, Object> stats = new HashMap<GameStat, Object>();
	
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
	public GameStructure(JSONObject information, int packetid) {
		
		if(information == null) {
			throw new NullPointerException("La información del paquete #" + packetid + " no contiene ningun valor para ser leido.");
		}
		
		gamestructurehandler = new GameStructureHandler(information);
		
		attributes = gamestructurehandler.getAttributes();
		stats = gamestructurehandler.getStats();
		
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado EXTRA_INFO.
	 * 
	 * @return La información extra del juego.
	 */
	public String getExtraInfo() {
		
		if(stats.containsKey(GameStat.EXTRA_INFO)) {
			return (String) stats.get(GameStat.EXTRA_INFO);
		}
		
		return "none";
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado SERVER_NAME.
	 * 
	 * @return El nombre del servidor ya registrado.
	 */
	public String getServerName() {
		
		if(stats.containsKey(GameStat.SERVER_NAME)) {
			return (String) stats.get(GameStat.SERVER_NAME);
		}
		
		return "UNKNOW";
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado GAME_PRESET.
	 * 
	 * @return El GamePreset encontrado en el paquete.
	 */
	public GamePreset getGamePreset() {
		if(stats.containsKey(GameStat.GAME_PRESET)) {
			return GamePreset.valueOf((String) stats.get(GameStat.GAME_PRESET));
		}
		
		return GamePreset.NONE;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado MAP_NAME.
	 * 
	 * @return El nombre del mapa usado en el juego.
	 */
	public String getMapName() {
		if(stats.containsKey(GameStat.MAP_NAME)) {
			return (String) stats.get(GameStat.MAP_NAME);
		}
		
		return "world";
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado ONLINE_PLAYERS.
	 * 
	 * @return El numero de jugadores que están en el juego.
	 */
	public int getOnlinePlayers() {
		if(stats.containsKey(GameStat.ONLINE_PLAYERS)) {
			return (int) stats.get(GameStat.ONLINE_PLAYERS);
		}
		
		return 0;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado MAXIMIUM_PLAYERS.
	 * 
	 * @return El numero maximo de jugadores que pueden entrar al juego.
	 */
	public int getMaximiumPlayers() {
		if(stats.containsKey(GameStat.MINIMIUM_PLAYERS)) {
			return (int) stats.get(GameStat.MINIMIUM_PLAYERS);
		}
		
		return 0;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado MINIMIUM_PLAYERS.
	 * 
	 * @return El numero maximo de jugadores para iniciar el juego.
	 */
	public int getMinimiumPlayers() {
		if(stats.containsKey(GameStat.MINIMIUM_PLAYERS)) {
			return (int) stats.get(GameStat.MINIMIUM_PLAYERS);
		}
		
		return 0;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Atributo denominado STARTED.
	 * 
	 * @return true si el juego ha sido iniciado y no se deberia de seguir usando
	 * para referirse al siguiente en la lista de su categoria.
	 */
	public boolean isStarted() {
		if(attributes.containsKey(GameAttribute.STARTED)) {
			return attributes.get(GameAttribute.STARTED);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Atributo denominado NEXT.
	 * 
	 * @return true si el juego es el siguiente en la lista de su categoria.
	 */
	public boolean isNext() {
		if(attributes.containsKey(GameAttribute.NEXT)) {
			return attributes.get(GameAttribute.NEXT);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado RELOAD.
	 * 
	 * @return true si el juego se encuentra en el estado reload (reseteo).
	 */
	public boolean isReload() {
		if(attributes.containsKey(GameAttribute.RELOAD)) {
			return attributes.get(GameAttribute.RELOAD);
		}
		
		return false;
	}
	
	/**
	 * 
	 * Se usa para recibir la información del Stat denominado LOCKED.
	 * 
	 * @return true si el juego se encuentra en el estado de partida (cerrado).
	 */
	public boolean isLocked() {
		if(attributes.containsKey(GameAttribute.LOCKED)) {
			return attributes.get(GameAttribute.LOCKED);
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
	 * Cambia de valor cualquier atributo.
	 * 
	 * @param attribute El atributo al cual le cambiarás el valor.
	 * @param value El valor que se le dará al atributo.
	 */
	public void setAttribute(GameAttribute attribute, boolean value) {
		attributes.put(attribute, value);
	}
	
	/**
	 * 
	 * Cambia de valor cualquier stat.
	 * 
	 * @param stat El stat al cual le cambiarás el valor.
	 * @param value El valor que se le dará al atributo.
	 */
	public void setStat(GameStat stat, Object value, Class<?> clazz) {
		stats.put(stat, stat.getClazz().isAssignableFrom(clazz) ? value : null);
	}
	
	/**
	 * 
	 * Clase tipo Handler que tiene metodos para la redacción del JSON con la información base de
	 * la estructura general de un juego.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public static class GameStructureHandler {
		
		protected JSONObject object;
		
		/**
		 * 
		 * Constructor principal de la clase.
		 * 
		 * @param object JSON con la información de la estructura del juego.
		 */
		public GameStructureHandler(JSONObject object) {
			
			this.object = object;
			
		}

		/**
		 * 
		 * Genera un mapa con todos los stats encontrados en el JSON
		 * con los siguientes argumentos:
		 * <br> </br>
		 * <code> KEY = GameStat.class | Value = Object.class </code>
		 * 
		 * @return Devuelve el mapa con los Stats encontrados.
		 */
		public Map<GameStat, Object> getStats() {
			
			Map<GameStat, Object> stats = new HashMap<GameStat, Object>();
			
			if(object.has("stats")) {
				
				JSONObject json_stats = (JSONObject) object.get("stats");
				
				for(GameStat stat : GameStat.values()) {
					
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
			
			for(GameStat stat : GameStat.values()) {
				
				if(!stats.containsKey(stat)) {
					
					if(stat.getClass().isAssignableFrom(String.class)) {
						
						String value = "notvalue";
						stats.put(stat, value);
						
					} else if(stat.getClass().isAssignableFrom(Integer.class)) {
						
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
		 * <code> KEY = GameAttribute.class | Value = Boolean.class </code>
		 * 
		 * @return Devuelve el mapa con los Atributos encontrados.
		 */
		public Map<GameAttribute, Boolean> getAttributes() {
			
			Map<GameAttribute, Boolean> attributes = new HashMap<GameAttribute, Boolean>();
			
			if(object.has("attributes")) {
				
				JSONObject json_attributes = (JSONObject) object.get("attributes");
				
				for(GameAttribute attribute : GameAttribute.values()) {
					
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
		 * Metodo que devolverá la información de la estructura del juego.
		 * 
		 * @return JSON de la información de la estructura del juego.
		 */
		protected JSONObject getObject() {
			return object;
		}
		
	}
	
}
