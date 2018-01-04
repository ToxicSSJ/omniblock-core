package net.omniblock.core;

import java.util.concurrent.TimeUnit;

import net.omniblock.core.config.ConfigHandler;
import net.omniblock.core.protocol.console.Console;
import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.packets.PacketsAdapter;
import net.omniblock.packets.OmniPackets;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.socket.Sockets;
import net.omniblock.packets.network.socket.helper.SocketHelper;
import net.omniblock.packets.network.structure.packet.WelcomeProxyPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.SystemType;

/**
 * 
 * Clase encargada de inicializar el sistema OmniCore de Omniblock Network,
 * Se añade que este sistema es sumamente necesario para la ejecución
 * de la network y va ligado con el plugin OmniNetwork que se debe encontrar
 * en cada servidor de la Network para el correcto funcionamiento de la
 * conexión cliente - servidor.
 * 
 * @author zlToxicNetherlz
 *
 */
public class OmniCore {

	private static final String OS = System.getProperty("os.name").toLowerCase();
	public static boolean DEBUG = true;
	
	public static void main(String[] args) {
		
		if(isUnix()) {
			
			Console.DRAWER.printDraw("logon.draw");
			Console.WRITTER.printInfo("Inicializando librerias...");
			
			ConfigHandler.create();
			
			sleep(TimeUnit.SECONDS, 3);
			
			Console.WRITTER.printInfo("Verificando la connexión con el proxy...");
			
			if(SocketHelper.isLocalPortInUse(25565)) {
				
				OmniPackets.setupSystem(SystemType.OMNICORE);
				OmniPackets.setDebug(false);
				
				PacketsAdapter.registerReaders();
				
				Sockets.SERVER.startServer(SocketHelper.OMNICORE_SOCKET_PORT);
				
				Packets.STREAMER.streamPacket(new WelcomeProxyPacket()
						.build().setReceiver(PacketSenderType.OMNICORD));
				
				Console.WRITTER.printInfo("Conectado! Iniciando sistema de procesamiento de paquetes...");
				
				// Database.makeConnection();
				
				NetworkManager.start();
				GameManager.start();
				
				Console.WRITTER.printInfo("Sistemas Iniciados! OmniCore iniciado correctamente! :)");
				
				return;
				
			}
			
			Console.WRITTER.printError("No se ha logrado conectar con el proxy, OmniCore se desactivará...");
			System.exit(0);
			return;
			
		}
		
		Console.WRITTER.printLine("OmniCore solo puede ser ejecutado en LINUX! (Por favor rectifique la Wiki o contacte con administración general) ", false, false);
		System.exit(0);
		return;
		
	}
	
	public static void sleep(TimeUnit unit, int time) {
		
		try {
			
			unit.sleep(time);
			return;
			
		} catch (InterruptedException e) {
			
			return;
			
		}
		
	}
	
	public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }
	
}
