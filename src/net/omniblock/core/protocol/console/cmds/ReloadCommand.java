package net.omniblock.core.protocol.console.cmds;

import net.omniblock.core.protocol.console.Console;
import net.omniblock.core.protocol.console.CommandCatcher;
import net.omniblock.core.protocol.console.CommandCatcher.Command;
import net.omniblock.core.protocol.manager.network.NetworkManager;
import net.omniblock.packets.object.external.ServerType;

public class ReloadCommand implements Command {

	@Override
	public boolean execute(String command, String[] args) {
		
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
					
					Console.WRITTER.printError(CommandCatcher.NOT_ENOUGHT_ARGUMENTS.replaceFirst("%s", command));
					return true;
					
				}
				
				Console.WRITTER.printError(CommandCatcher.NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[0]).replaceFirst("%s", command));
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
					
					Console.WRITTER.printError(CommandCatcher.NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[1]).replaceFirst("%s", command));
					return true;
					
				}
				
				Console.WRITTER.printError(CommandCatcher.NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[1]).replaceFirst("%s", command));
				return true;
				
			}
			
			Console.WRITTER.printError(CommandCatcher.NOT_ENOUGHT_ARGUMENTS.replaceFirst("%s", command));
			return true;
			
		}
		
		return false;

	}
	
}
