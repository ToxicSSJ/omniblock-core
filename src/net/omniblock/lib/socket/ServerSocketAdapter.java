package net.omniblock.lib.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.omniblock.core.protocol.manager.network.handler.Packet;
import net.omniblock.core.protocol.manager.network.handler.PacketHandler;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier;
import net.omniblock.core.protocol.manager.network.handler.modifier.PacketModifier.PacketModifierHandler;

public class ServerSocketAdapter {

	public static ServerSocket serverSocket = null;
	public static Thread thread = null;
	
	public void startServer(int port) {
		
        final ExecutorService clientprocessor = Executors
                .newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                	
                	refreshPort(port);
                    serverSocket = new ServerSocket(port);
                    
                    while (true) {
                    	
                        Socket clientSocket = serverSocket.accept();
                        
                        clientprocessor
                                .submit(new SocketAdapter(clientSocket));
                        
                    }
                    
                } catch (IOException e) {
                	
                    e.printStackTrace();
                    
                }
            }
        };
        
        thread = new Thread(serverTask);
        thread.start();
        
    }
	
	public DataSenderStatus sendData(int port, PacketModifier packet) {
		
		try {
			
			if(!isLocalPortInUse(port)) return DataSenderStatus.ERROR;
			
			Socket client = new Socket("localhost", port);
			
			DataOutputStream ds = new DataOutputStream(client.getOutputStream());
			ds.writeUTF(PacketModifierHandler.serialize(packet));
			ds.close();
			
			client.close();
			return DataSenderStatus.SENDED;
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
			
		}
		
		return DataSenderStatus.ERROR;
		
	}
	
	public boolean isLocalPortInUse(int port) {
	    try {
	    	
	        new ServerSocket(port).close();
	        return false;
	        
	    } catch(IOException e) {
	    	
	        return true;
	        
	    }
	}
	
	private void refreshPort(int port) {
		
		ServerSocket socket = null;
		
	    try {
	    	
	        socket = new ServerSocket(port);
	        
	    } catch (IOException e) {
	    } finally {
	    	
	        if (socket != null) {
	        	try { socket.close(); } 
	        	catch (IOException e) { }
	        }
	        
	    }
	    
	}
    
	public static enum DataSenderStatus {
		
		SENDED,
		ERROR,
		
		;
		
	}
	
    private class SocketAdapter implements Runnable {
    	
        private final Socket clientSocket;

        private SocketAdapter(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
        	
            try {
            	
            	DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                String clientData = dis.readUTF();
                
                handlePacket(clientData);
                
                clientSocket.close();
                return;
                
            } catch (IOException e) { return; }
            
        }
        
    	public void handlePacket(String data) {
    		
    		if(data != null && data != "") {
				
				PacketHandler.unpackPackets(
						new Packet()
						.readJSONInformation(data)
						.getJSONInformation());
				
			}
    		
    	}
        
    }
	
}
