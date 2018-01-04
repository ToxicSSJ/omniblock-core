package net.omniblock.core.database.bases;

import java.sql.SQLException;

import net.omniblock.core.database.data.make.MakeAdvancedSQLQuery;
import net.omniblock.core.database.table.TableType;

public class SkywarsBase {

	protected static TableType tabletype = TableType.TOP_STATS_WEEKPRIZE_SKYWARS;
	
	protected static String map_inserter_sql = "INSERT INTO top_maps_skywars (map_name, votes) SELECT * FROM (SELECT VAR_P_SKYWARS_MAP a, VAR_P_SKYWARS_MAP_INITIAL b) AS tmp WHERE NOT EXISTS (SELECT 1 FROM top_maps_skywars WHERE map_name = VAR_P_SKYWARS_MAP );";
	protected static String top_weekprize_pos_sql = "SELECT id, p_id, p_points, FIND_IN_SET( p_points, ( SELECT GROUP_CONCAT( p_points ORDER BY p_points DESC ) FROM skywars_weekprize ) ) AS rank FROM skywars_weekprize WHERE p_id =  'VAR_P_ID'";
	protected static String top_weekprize_delete_sql = "TRUNCATE skywars_weekprize;";
	
	public static void giveAll() {
		
		try {
			
			new MakeAdvancedSQLQuery(TableType.TOP_STATS_WEEKPRIZE_SKYWARS)
			.append(top_weekprize_delete_sql)
			.execute();
			
			System.out.println("xdddd");
			
		} catch (SQLException e) { e.printStackTrace(); }
		
	}
	
}
