/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package net.omniblock.core.database.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.omniblock.core.database.Database;
import net.omniblock.core.database.OrderByType;
import net.omniblock.core.database.SQLQueryResult;
import net.omniblock.core.database.table.TableType;

public class MakeSQLQuery {

	private List<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
	private TableType table;
	private List<String> selectRows = new ArrayList<String>();
	private HashMap<String, String> where = new HashMap<String, String>();
	private int limit = -1;
	private OrderByType orderByDirection = null;
	private String orderByRow = null;
	private boolean paginationLimit = false;
	private int paginationLimitFirstBound = 0;
	private int paginationLimitSecondBound = 0;
	
	public MakeSQLQuery(TableType table){
		this.table = table;
	}
	
	public void setResultLimit(int limit){
		this.limit = limit;
	}
	
	public void setPagination(int startAtRow, int rowCount){
		paginationLimit = true;
		paginationLimitFirstBound = startAtRow;
		paginationLimitSecondBound = startAtRow + rowCount;
	}
	
	public void setOrderBy(String row, OrderByType direction){
		this.orderByRow = row;
		this.orderByDirection = direction;
	}
	
	public void addWhereClause(String row, Object value){
		where.put(row, value.toString());
	}
	
	public void addRowToSelect(String row){
		this.selectRows.add(row);
	}
	
	public SQLQueryResult execute() throws SQLException, IllegalArgumentException {
		if(table == null){
			throw new IllegalArgumentException("La tabla no puede ser nula!");
		}
		if(selectRows.isEmpty()){
			selectRows.add("*");
		}
		
		String sql = "SELECT %s FROM %s";
		
		Iterator<String> it = selectRows.iterator();
		String selectPart = "";
		while(it.hasNext()){
			String row = it.next();
			boolean isEnd = !it.hasNext();
			
			if(isEnd){
				selectPart += row;
			}else{
				selectPart += row + ", ";
			}
			
		}
		
		sql = String.format(sql, selectPart, table.getTableName());
		
		if(!where.isEmpty()){
			
			Iterator<Entry<String, String>> it2 = where.entrySet().iterator();
			String wherePart = "";
			while(it2.hasNext()){
				Entry<String, String> entry = it2.next();
				boolean isEnd = !it2.hasNext();
				
				if(isEnd){
					wherePart += entry.getKey() + " = '" + entry.getValue() + "'";
				}else{
					wherePart += entry.getKey() + " = '" + entry.getValue() + "' AND ";
				}
			}
			
			sql += " WHERE " + wherePart;
		}
		
		if(orderByRow != null && orderByDirection != null){
			String orderByPart = "ORDER BY %s %s";
			orderByPart = String.format("%s", orderByRow, orderByDirection.toString());
			sql += " " + orderByPart;
		}
		
		if(!paginationLimit){
			if(limit > 0){
				sql += " LIMIT " + limit;
			}
		}else{
			sql += " LIMIT " + paginationLimitFirstBound + ", " + paginationLimitSecondBound;
		}
		
		Statement stm = Database.getConnection().createStatement();
		
		ResultSet res = stm.executeQuery(sql);
		
		HashMap<Integer, String> columns = new HashMap<Integer, String>();
		ResultSetMetaData rsmd = res.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		for (int i = 1; i <= columnCount; i++ ) {
		  String name = rsmd.getColumnName(i);
		  columns.put(i, name);
		}
		
		results.clear();
		
		while(res.next()){
			HashMap<String, Object> armResult = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++ ) {
				armResult.put(columns.get(i), res.getObject(i));
			}
			results.add(armResult);
		}
		
		stm.close();
		
		return new SQLQueryResult(results, table);
	}
}
