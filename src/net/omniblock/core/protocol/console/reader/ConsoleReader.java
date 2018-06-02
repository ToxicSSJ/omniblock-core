package net.omniblock.core.protocol.console.reader;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.Arrays;

import net.omniblock.core.protocol.console.CommandCatcher;
import net.omniblock.core.protocol.console.Console;

@SuppressWarnings("deprecation")
public class ConsoleReader implements Runnable, KeyListener {

	protected CommandCatcher catcher = new CommandCatcher();
	protected Boolean read = true;
	
	public void keyPressed(KeyEvent k) {
		
		if (k.getKeyCode() == KeyEvent.VK_UP) {
			
			if(CommandCatcher.commandHistory.isEmpty())
				return;
			
			if(!(CommandCatcher.commandPos + 1 >= CommandCatcher.commandHistory.size())) {
				
				CommandCatcher.commandPos++;
				
				StringBufferInputStream str = new StringBufferInputStream(CommandCatcher.commandHistory.get(CommandCatcher.commandPos));
				System.setIn(str);
				return;
				
			}
			
			return;
			
		} else if (k.getKeyCode() == KeyEvent.VK_DOWN) {
			   
			if(CommandCatcher.commandHistory.isEmpty())
				return;
			
			if(!(CommandCatcher.commandPos - 1 < 0)) {
				
				CommandCatcher.commandPos--;
				
				StringBufferInputStream str = new StringBufferInputStream(CommandCatcher.commandHistory.get(CommandCatcher.commandPos));
				System.setIn(str);
				return;
				
			}
			
			return;
			
		}
		
	}
	
	@Override
	public void run() {
		
		InputStreamReader instream = new InputStreamReader(System.in);
		
		while(read) {
			
			try (BufferedReader buffer = new BufferedReader(instream)) {
				
				String writed = "", original = "";
				String[] args = new String[0];
				
			    while ((original = buffer.readLine()) != null) {
			        
			    	if (original.isEmpty()) {
			            break;
			        }
			    	
			    	if(!original.contains(" "))
			    		writed = original;
			    	
			    	if(original.contains(" ")) {
	                	
	                	String[] cacheargs = Arrays.copyOfRange(original.split(" "), 1, original.split(" ").length);
	                	args = cacheargs;
	                	
	                	writed = original.split(" ")[0];
	                	
	                }
	                
	                if(!CommandCatcher.catchCommand(original, writed, args))
	                	Console.WRITTER.printError(CommandCatcher.NOT_RECOGNIZED_COMMAND.replaceFirst("%s", original));
	                
	                run();
	            	return;
			    	
			    }
			    
			} catch (Exception e) {}
            
        }
		
	}

	public boolean isRead() {
		return read;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
