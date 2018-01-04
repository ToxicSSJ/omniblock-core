package net.omniblock.core.database.data.match;

import net.omniblock.core.database.table.TableType;

public enum ItemType {

	NETWORK_BOOSTER_6H(TableType.BANK_DATA, "NB6H_01"),

	GIF_MINI_COW_PET(TableType.BANK_DATA, "MINI_COW"),

	;

	private String hashid;
	private TableType table;

	ItemType(TableType table, String hashid) {

		this.table = table;
		this.hashid = hashid;

	}

	public String getHashid() {
		return hashid;
	}

	public TableType getTable() {
		return table;
	}

}
