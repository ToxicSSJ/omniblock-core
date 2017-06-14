package net.omniblock.core.protocol.manager.network.handler.cord;

import net.omniblock.lib.socket.ServerSocketAdapter;

/**
 * 
 * Con esta clase se pueden manejar los sitemas del proxy
 * para el envio y la recepción de los paquetes (sockets)
 * por medio de un adaptador simple de sockets.
 * 
 * @author zlToxicNetherlz
 *
 */
public class ProxyServer {

	protected static final int PROXYSOCKET_PORT = 8005;
	protected static final ServerSocketAdapter SOCKET_ADAPTER = new ServerSocketAdapter();
	
	/**
	 * 
	 * Inicializa la conexión del core en el puerto
	 * asignado.
	 * 
	 */
	public static void start() {
		
		SOCKET_ADAPTER.startServer(8000);
		
	}
	
	/**
	 * 
	 * Con este metodo podrás recibir el puerto del
	 * proxy para el envio y la recepción de datos.
	 * 
	 * @return El puerto de recepción de sockets del Proxy (BungeeCord).
	 */
	public static int getPort() {
		return PROXYSOCKET_PORT;
	}
	
	/**
	 * 
	 * Con este metodo se puede accesar al SocketAdapter
	 * que está definido como protegido en la sintaxis.
	 * 
	 * @return El socket adapater en su estado base.
	 * @see net.omniblock.lib.socket.ServerSocketAdapter
	 */
	public static ServerSocketAdapter getSocketAdapter() {
		return SOCKET_ADAPTER;
	}
	
}
