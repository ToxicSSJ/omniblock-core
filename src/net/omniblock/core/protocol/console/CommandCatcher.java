package net.omniblock.core.protocol.console;

import java.util.LinkedList;

import net.omniblock.core.protocol.console.cmds.ReloadCommand;
import net.omniblock.core.protocol.console.cmds.ShopCommand;
import net.omniblock.core.protocol.console.cmds.StopCommand;
import net.omniblock.core.protocol.console.cmds.WeekPrizeCommand;

public class CommandCatcher {

	public static final String ARROW_UP = "\u001B[A";
	public static final String ARROW_DOWN = "\u001B[B";
	
	public static final String NOT_RECOGNIZED_COMMAND = "El comando '%s' no ha sido reconocido por el sistema!";
	
	public static final String NOT_ENOUGHT_ARGUMENTS = "No hay suficientes argumentos para el comando '%s' por favor rectifique la sintaxis!";
	public static final String NOT_RECOGNIZED_ARGUMENT = "No se ha reconocido el argumento '%s' en el comando '%s' por favor rectifique la sintaxis!";
	
	private static final Command[] COMMANDS = new Command[] {
			
			new ReloadCommand(),
			new StopCommand(),
			
			new ShopCommand(),
			new WeekPrizeCommand(),
			
	};
	
	public static LinkedList<String> commandHistory;
	public static int commandPos;
	
	public CommandCatcher() {
		
		commandPos = -1;
		commandHistory = new LinkedList<String>();
		
	}
	
	public static boolean catchCommand(String line, String command, String[] args) {
		
		if(commandHistory.size() + 1 > 5)
			commandHistory.remove(commandHistory.get(4));
			
		commandHistory.add(line);
		commandPos = -1;
		
		for(Command cmd : COMMANDS) {
			
			if(cmd.execute(command, args)) 
				return true;
			
		}
		
		return false;
		
	}
	
	/**
	 * 
	 * Clase general encargada de tener instancias
	 * para el manejo de comandos del sistema.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
	public interface Command {
		
		/**
		 * 
		 * Este metodo será ejecutado una vez un comando
		 * que aún no ha sido identificado pase por el
		 * lector como proceso general. Si este metodo
		 * devuelve true, el lector parará y no se seguirá
		 * tratando de identificar el manager de dicho
		 * comando. Caso contrario el lector seguirá con
		 * las demas instancias de comandos.
		 * 
		 * @param cmd El comando en su formato habitual.
		 * @param args Los argumentos extras del comando.
		 * @return <strong>true</strong> si el procesador
		 * del comando fue identificado.
		 */
		public boolean execute(String command, String[] args);
		
	}
	
}
