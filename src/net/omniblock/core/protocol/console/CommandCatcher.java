package net.omniblock.core.protocol.console;

import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.packets.object.external.ServerType;

public class CommandCatcher {

	public static final String NOT_RECOGNIZED_COMMAND = "El comando '%s' no ha sido reconocido por el sistema!";
	
	public static final String NOT_ENOUGHT_ARGUMENTS = "No hay suficientes argumentos para el comando '%s' por favor rectifique la sintaxis!";
	public static final String NOT_RECOGNIZED_ARGUMENT = "No se ha reconocido el argumento '%s' en el comando '%s' por favor rectifique la sintaxis!";
	
	public CommandCatcher() { }
	
	public static boolean catchCommand(String command, String[] args) {
		
		if(command.equalsIgnoreCase("reload")) {
			
			if(args.length >= 1) {
				
				if(args[0].equalsIgnoreCase("all")) {
					
					Console.WRITTER.printLine("Se hará reload a todos los servidores de la Network, Esto puede generar lag. Recuerde haber activado el modo mantenimiento previamente para mayor rendimiento...");
					
					for(ServerType st : ServerType.values()){
						NetworkManager.reloadServers(st);
					}
					
					Console.WRITTER.printInfo("Se ha enviado el paquete de relodeo a los servidores correctamente!");
					return true;
					
				}
				
				if(args[0].equalsIgnoreCase("mainlobby")) {
					
					Console.WRITTER.printLine("Se hará reload a todos los servidores tipo MainLobby de la Network, Esto puede generar lag. Recuerde haber activado el modo mantenimiento previamente para mayor rendimiento...");
					
					NetworkManager.reloadServers(ServerType.MAIN_LOBBY_SERVER);
					
					Console.WRITTER.printInfo("Se ha enviado el paquete de relodeo a los servidores correctamente!");
					return true;
					
				}
				
				if(args[0].equalsIgnoreCase("survival")) {
					
					Console.WRITTER.printLine("Se hará reload al servidor Survival de la Network...");
					
					NetworkManager.reloadServers(ServerType.SURVIVAL);
					
					Console.WRITTER.printInfo("Se ha enviado el paquete de relodeo al servidor correctamente!");
					return true;
					
				}
				
				if(args[0].equalsIgnoreCase("skywars")){
					
					Console.WRITTER.printError(NOT_ENOUGHT_ARGUMENTS.replaceFirst("%s", command));
					return true;
					
				}
				
				Console.WRITTER.printError(NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[0]).replaceFirst("%s", command));
				return true;
				
			}
			
			if(args.length >= 2){
				
				if(args[0].equalsIgnoreCase("skywars")) {
					
					if(args[1].equalsIgnoreCase("lobby")){
						
						Console.WRITTER.printLine("Se hará reload a todos los servidores tipo Lobby de la modalidad de Skywars, Esto puede generar lag. Recuerde haber activado el modo mantenimiento previamente para mayor rendimiento...");
						
						NetworkManager.reloadServers(ServerType.SKYWARS_LOBBY_SERVER);
						
						Console.WRITTER.printInfo("Se ha enviado el paquete de relodeo a los servidores correctamente!");
						return true;
						
					}
					
					if(args[1].equalsIgnoreCase("game")){
						
						Console.WRITTER.printLine("Se hará reload a todos los servidores tipo Game de la modalidad de Skywars, Esto puede generar lag. Recuerde haber activado el modo mantenimiento previamente para mayor rendimiento...");
						
						NetworkManager.reloadServers(ServerType.SKYWARS_GAME_SERVER);
						
						Console.WRITTER.printInfo("Se ha enviado el paquete de relodeo a los servidores correctamente!");
						return true;
						
					}
					
					Console.WRITTER.printError(NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[1]).replaceFirst("%s", command));
					return true;
					
				}
				
				Console.WRITTER.printError(NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[1]).replaceFirst("%s", command));
				return true;
				
			}
			
			Console.WRITTER.printError(NOT_ENOUGHT_ARGUMENTS.replaceFirst("%s", command));
			return true;
			
		} else if(command.equalsIgnoreCase("shop")) {
			
			return true;
			
		} else if(command.equalsIgnoreCase("stop")) {
			
			Console.WRITTER.printInfo("Adios! :)");
			System.exit(0);
			return true;
			
		}
		
		return false;
		
	}
	
}
