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

/**
 * 
 * Esta clase cumple la función de adaptador de sockets
 * para el sistema core con el fin de manejar los paquetes
 * tanto de salida como de entrada los cuales funcionan de
 * la siguiente manera:
 * 
 * <center>
 * 		<li>Recepción desde el OmniNetwork. [OmniNetwork -> Core]</li>
 * 		<li>Envio al OmniCord. [Core -> OmniCord]</li>
 * </center>
 * 
 * <br>
 * Estos sistemas son la pieza fundamental del sistema
 * de información del servidor que hasta el momento no
 * ha demostrado un mal desempeño en cuanto a los timings
 * debido a que la información pasa por hasta 3 servidores
 * antes de recibir la respuesta.
 * <br>
 * 
 * @author zlToxicNetherlz
 *
 */
public class ServerSocketAdapter {

	public static ServerSocket serverSocket = null;
	public static Thread thread = null;
	
	/**
	 * 
	 * Inicia el adaptador del servidor por medio
	 * de un puerto definido con el fin de recibir
	 * lo respectivos paquetes en proceso aparte
	 * del general.
	 * 
	 * @param port El puerto que se desea utilizar, 0 para
	 * abrir uno al azar.
	 */
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
	
	/**
	 * 
	 * Con este metodo se pueden enviar datos a un socket server
	 * especificando su puerto y el respectivo PacketModifier con
	 * la información necesaria para que el procesador remoto
	 * sea capaz de procesarla correctamente.
	 * 
	 * @param port El puerto del socket server al cual se le enviará
	 * la información.
	 * @param packet La información almacenada en el constructor PacketModifier.
	 * @return Se devolverá el estado del envio.
	 */
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
	
	/**
	 * 
	 * Con este metodo se puede verificar si un puerto
	 * local esta o no en uso.
	 * 
	 * @param port El puerto que se desea verificar.
	 * @return <strong>true</strong> si está en uso.
	 */
	public boolean isLocalPortInUse(int port) {
	    try {
	    	
	        new ServerSocket(port).close();
	        return false;
	        
	    } catch(IOException e) {
	    	
	        return true;
	        
	    }
	}
	
	/**
	 * 
	 * Con este metodo se puede refrescar un puerto
	 * que este en uso o si no lo está se reabrirá
	 * para poder ser usado.
	 * 
	 * @param port El puerto el cual se desea refrescar.
	 */
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
	
	/**
	 * 
	 * Esta clase es la responsable de preprocesar
	 * la información recibida de los clientes sockets
	 * pero en su generalidad es creada y llamada
	 * por el ThreadPool para que su procesamiento sea
	 * más rapido y eficaz, Luego de esto simplemente
	 * prosigue a leerla y ejecutar la acción fundamental
	 * de procesamiento de sockets.
	 * 
	 * @author zlToxicNetherlz
	 *
	 */
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
        
        /**
         * 
         * Con este metodo se verifica que la data
         * leida sea valida y se prosigue a enviarla
         * al PacketHandler, el cual continuara el
         * proceso.
         * 
         * @param data La data en su formato String puro.
         */
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
