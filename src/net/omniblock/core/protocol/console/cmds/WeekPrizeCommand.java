package net.omniblock.core.protocol.console.cmds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import net.omniblock.core.config.ConfigHandler;
import net.omniblock.core.protocol.console.CommandCatcher;
import net.omniblock.core.protocol.console.Console;
import net.omniblock.core.protocol.console.CommandCatcher.Command;

public class WeekPrizeCommand implements Command {

	public static final Calendar calendar = Calendar.getInstance();
    public static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@Override
	public boolean execute(String command, String[] args) {
		
		if(command.equalsIgnoreCase("weekprize")) {
			
			if(args.length >= 4) {
				
				if(args[0].equalsIgnoreCase("add")) {
					
					String ddmmyy = args[2];
					String hhmmss = args[3];
					
					String datestr = StringUtils.join(new String[] {
							ddmmyy,
							hhmmss
					}, " ");
					
					if(StringUtils.countMatches(datestr, "/") == 2 && 
							StringUtils.countMatches(datestr, ":") == 2) {
						
						FileConfiguration config = ConfigHandler.getWeekPrizeConfig().getConfiguration();
						String setter = args[1];
						
						if(setter.equals("skywars")) {
							
							List<String> dates = config.isSet("weekprizes.skywars") ?
									config.getStringList("weekprizes.skywars") != null ?
											config.getStringList("weekprizes.skywars") : new ArrayList<String>() :
									new ArrayList<String>();
							
							dates.add(datestr);
							config.set("weekprizes.skywars", dates);
							
							ConfigHandler.getWeekPrizeConfig().save();
							
							Console.WRITTER.printInfo("Se ha añadido la fecha '" + datestr + "' como un WeekPrize para Skywars!");
							return true;
							
						}
						
						Console.WRITTER.printError("Usted no ha puesto un weekprize valido, recuerde que solo esta disponible: skywars.");
						return true;
						
					}
					
					Console.WRITTER.printError("El formato dado no es valido para una fecha.");
					return true;
					
				}
				
				Console.WRITTER.printError(CommandCatcher.NOT_RECOGNIZED_ARGUMENT.replaceFirst("%s", args[0]).replaceFirst("%s", command));
				return true;
				
			}
			
			Console.WRITTER.printError(CommandCatcher.NOT_ENOUGHT_ARGUMENTS.replaceFirst("%s", command));
			return true;
			
		}
		
		return false;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public SimpleDateFormat getFormat() {
		return format;
	}
	
}
