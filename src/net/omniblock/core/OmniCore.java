package net.omniblock.core;

import java.util.concurrent.TimeUnit;

import net.omniblock.core.protocol.console.Console;
import net.omniblock.core.protocol.manager.network.GameManager;
import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.core.protocol.manager.network.handler.cord.ProxyServer;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.reader.type.MessageType;
import net.omniblock.lib.socket.ServerSocketAdapter.DataSenderStatus;

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

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static void main(String[] args) {
		
		if(isUnix()) {
			
			Console.DRAWER.printDraw("logon.draw");
			Console.WRITTER.printInfo("Inicializando librerias...");
			
			sleep(TimeUnit.SECONDS, 3);
			
			Console.WRITTER.printInfo("Verificando la connexión con el proxy...");
			
			if(ProxyServer.getSocketAdapter().isLocalPortInUse(25565)) {
				
				PacketModifier modifier = new PacketModifier()
						.addString(MessageType.WELCOME_PROXY.getKey());
				
				DataSenderStatus status = ProxyServer.getSocketAdapter().sendData(ProxyServer.getPort(), modifier);
				
				if(status == DataSenderStatus.SENDED) {
					
					Console.WRITTER.printInfo("Conectado! Iniciando sistema de procesamiento de paquetes...");
					
					ProxyServer.start();
					
					NetworkManager.start();
					GameManager.start();
					
					Console.WRITTER.printInfo("Sistemas Iniciados! OmniCore iniciado correctamente! :)");
					
					return;
					
				}
				
				Console.WRITTER.printError("Error! Parece ser que OmniCord está desactualizado, OmniCore se desactivará...");
				System.exit(0);
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
