package net.omniblock.core.database.bases;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import net.omniblock.core.database.data.make.MakeSQLQuery;
import net.omniblock.core.database.data.make.MakeSQLUpdate;
import net.omniblock.core.database.data.make.MakeSQLUpdate.TableOperation;
import net.omniblock.core.database.data.util.Resolver;
import net.omniblock.core.database.data.util.SQLResultSet;
import net.omniblock.core.database.table.TableType;

public class AuthBase {

	public static final String DEFAULT_PASS = "$ZPASS";

	public static void setPassword(Player player, String password) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.PLAYER_SETTINGS, TableOperation.UPDATE);

		msu.rowOperation("p_pass", password);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}

	public static String getPassword(Player player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.PLAYER_SETTINGS).select("p_pass").where("p_id",
				Resolver.getNetworkID(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {
				return sqr.get("p_pass");
			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return DEFAULT_PASS;

	}

	public static boolean isRegister(Player player) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.PLAYER_SETTINGS).select("p_pass").where("p_id",
				Resolver.getNetworkID(player));

		try {

			SQLResultSet sqr = msq.execute();

			if (sqr.next()) {

				String pass = sqr.get("p_pass");

				if (pass.equalsIgnoreCase(DEFAULT_PASS))
					return false;
				return true;

			}

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

}
