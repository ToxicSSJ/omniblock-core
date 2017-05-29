/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package net.omniblock.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.omniblock.core.config.ConfigHandler;
import net.omniblock.core.protocol.console.Console;

public class Database {
	
	public static Connection conn = null;

	public static boolean makeConnection(){
		
		String host = ConfigHandler.DATABASE_CONFIG.getData("host");
		String port = ConfigHandler.DATABASE_CONFIG.getData("port");
		String user = ConfigHandler.DATABASE_CONFIG.getData("user");
		String pass = ConfigHandler.DATABASE_CONFIG.getData("pass");
		String database = ConfigHandler.DATABASE_CONFIG.getData("database");
		
		if(host == null) Console.WRITTER.printError("&c[!] &fEl host no pudo ser obtenido (null)");
		if(port == null) Console.WRITTER.printError("&c[!] &fEl puerto no pudo ser obtenido (null)");
		if(user == null) Console.WRITTER.printError("&c[!] &fEl usuario no pudo ser obtenido (null)");
		if(pass == null) Console.WRITTER.printError("&c[!] &fLa contrasena no pudo ser obtenida (null)");
		if(database == null) Console.WRITTER.printError("&c[!] &fLa base de datos no pudo ser obtenida (null)");
		
		if(host != null && port != null && user != null && pass != null && database != null){
			
			Console.WRITTER.printLines(
					"Host: " + host,
					"Puerto: " + port,
					"Base de Datos: " + database,
					"Usuario: " + user,
					"Pass: " + "******"
					);
			
			Console.WRITTER.printWarning("&eEstableciendo conexion...");
			
			String URL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";
			
			try {
				
				conn = (Connection) DriverManager.getConnection(URL, user, pass);
				
				Console.WRITTER.printLines(
						"&aConexion establecida!",
						"&eCreando tablas, en caso de ser necesario...",
						"&8---------------------------------------------");
				
				return true;
				
			} catch (SQLException e) {
				
				Console.WRITTER.printError("&c[!] &fError al establecer conexion:");
				e.printStackTrace();
				return false;
				
			}
			
		}else{
			
			Console.WRITTER.printError("Ha ocurrido un problema con los datos para conectar a la base de datos. Por favor revisa que todo este correctamente configurado.");
			return false;
			
		}
	}
	
	public static Connection getConnection(){
		return conn;
	}
}
