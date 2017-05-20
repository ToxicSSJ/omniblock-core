package net.omniblock.core.protocol.console;

import net.omniblock.core.protocol.console.drawer.ConsoleDrawer;
import net.omniblock.core.protocol.console.reader.ConsoleReader;
import net.omniblock.core.protocol.console.writter.ConsoleWritter;

public class Console {

	public static final ConsoleDrawer DRAWER = new ConsoleDrawer();
	public static final ConsoleWritter WRITTER = new ConsoleWritter();
	public static final Thread READER = new Thread(new ConsoleReader());
	
	static {
		
		READER.start();
		
	}
	
}
