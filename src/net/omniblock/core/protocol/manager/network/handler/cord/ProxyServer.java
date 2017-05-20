package net.omniblock.core.protocol.manager.network.handler.cord;

import net.omniblock.lib.socket.ServerSocketAdapter;

public class ProxyServer {

	protected static final int PROXYSOCKET_PORT = 8005;
	protected static final ServerSocketAdapter SOCKET_ADAPTER = new ServerSocketAdapter();
	
	public static void start() {
		
		SOCKET_ADAPTER.startServer(8000);
		
	}
	
	public static int getPort() {
		return PROXYSOCKET_PORT;
	}
	
	public static ServerSocketAdapter getSocketAdapter() {
		return SOCKET_ADAPTER;
	}
	
}
