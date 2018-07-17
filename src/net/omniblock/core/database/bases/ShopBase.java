package net.omniblock.core.database.bases;

import com.google.gson.JsonSyntaxException;
import net.omniblock.core.database.data.make.MakeSQLQuery;
import net.omniblock.core.database.data.make.MakeSQLUpdate;
import net.omniblock.core.database.data.util.SQLResultSet;
import net.omniblock.core.database.table.TableType;
import net.omniblock.packets.network.tool.console.Console;

import java.sql.SQLException;

public class ShopBase {

	public static String getShopData(String servertype){

		MakeSQLQuery msq = new MakeSQLQuery(TableType.SHOP)
				.select("s_shop")
				.where("s_id", servertype);

		try {

			SQLResultSet sqr = msq.execute();

			if(sqr.next())
				return sqr.get("s_shop");
			else
				Console.WRITTER.printError("La tienda de este servidor no esta registrada.");

		} catch (SQLException e) {
			e.printStackTrace();
		}


		return "";
	}

	public static void setChangeData(String servertype, String jsonData){

		try {
			new MakeSQLUpdate(TableType.SHOP, MakeSQLUpdate.TableOperation.UPDATE)
					.whereOperation("s_id", servertype)
					.rowOperation("s_shop", jsonData)
					.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setData(String servertype, String jsonData){

		MakeSQLQuery msq = new MakeSQLQuery(TableType.SHOP)
				.select("s_shop")
				.where("s_id", servertype);

		try {

			SQLResultSet res = msq.execute();

			if(!res.next()) {
				new MakeSQLUpdate(TableType.SHOP, MakeSQLUpdate.TableOperation.INSERT)
						.rowOperation("s_id", servertype)
						.rowOperation("s_shop", jsonData)
						.execute();


			}
		} catch (SQLException | JsonSyntaxException e) {
			e.printStackTrace();
		}
	}
}
