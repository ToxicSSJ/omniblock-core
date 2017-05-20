package net.omniblock.core.protocol.manager.network.reader.type;

public enum ReaderStatus {

	SUCESS("Se ha ejecutado correctamente!"),
	
	CANNOT_EXECUTE("No se puede ejecutar la acción porque la información contiene argumentos no validos."),
	CANNOT_REGISTER("No se ha podido registrar por alguna razón en particular."),
	
	NOT_VALID("La información no es valida, Por favor rectifiquela."),
	
	;
	
	private String statusmsg;
	
	ReaderStatus(String statusmsg){
		this.statusmsg = statusmsg;
	}

	public String getStatusMSG() {
		return statusmsg;
	}

	public void setStatusMSG(String statusmsg) {
		this.statusmsg = statusmsg;
	}
	
}
