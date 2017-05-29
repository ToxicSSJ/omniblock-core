package net.omniblock.core.database.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.omniblock.core.database.Database;
import net.omniblock.core.database.SQLQueryResult;
import net.omniblock.core.database.table.TableType;

public class MakeAdvancedSQLQuery {

	private List<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
	
	private StringBuffer buffer = new StringBuffer();
	private TableType table;
	
	public MakeAdvancedSQLQuery(TableType table){ this.table = table; }
	
	public MakeAdvancedSQLQuery append(String SQL){
		
		buffer.append(SQL);
		return this;
		
	}
	
	public SQLQueryResult execute() throws SQLException {
		
		Statement stm = Database.getConnection().createStatement();
		
		ResultSet res = stm.executeQuery(buffer.toString());
		
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
	
	public TableType getTable() {
		return table;
	}
	
}
