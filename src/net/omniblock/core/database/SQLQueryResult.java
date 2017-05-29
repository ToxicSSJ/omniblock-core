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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.omniblock.core.database.table.TableType;

public class SQLQueryResult {
	
	private Iterator<HashMap<String, Object>> iterator = null;
	private HashMap<String, Object> iteratorLast = null;

	private List<HashMap<String, Object>> data = null;
	private TableType table;
	
	public SQLQueryResult(List<HashMap<String, Object>> data, TableType table){
		this.data = data;
	}
	
	public TableType getTableData(){
		return table;
	}
	
	public boolean iterateNextResult(){
		if(iterator == null){
			iterator = data.iterator();
		}
		
		if(iterator.hasNext()){
			iteratorLast = iterator.next();
			return true;
		}
		
		return false;
	}
	
	public Object getResult(String column) throws IllegalArgumentException {
		if(iteratorLast == null){
			throw new IllegalArgumentException("iterateNextResult() debe ser usado primero antes de obtener un resultado. O bien, no hay resultados por procesar.");
		}
		
		return iteratorLast.get(column);
	}
	
	public boolean getBoolean(String column) throws ClassCastException {
		return (boolean) getResult(column);
	}
	
	public int getInt(String column) throws ClassCastException {
		return (int) getResult(column);
	}
	
	public String getString(String column) throws ClassCastException {
		return getResult(column).toString();
	}
	
	public double getDouble(String column) throws ClassCastException {
		return (double) getResult(column);
	}
	
	public float getFloat(String column) throws ClassCastException {
		return (float) getResult(column);
	}
	
	public HashMap<String, Object> getResultMap() throws IllegalArgumentException {
		if(iteratorLast == null){
			throw new IllegalArgumentException("iterateNextResult() debe ser usado primero antes de obtener un resultado. O bien, no hay resultados por procesar.");
		}
		
		return iteratorLast;
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
}
