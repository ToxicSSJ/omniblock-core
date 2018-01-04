/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package net.omniblock.core.database.data.util;

import org.bukkit.ChatColor;

public class TextUtil {

	public static String format(String s) {
		return s.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
	}

	public static String replaceAllTextWith(String text, char replace) {
		char chArray[] = text.toCharArray();
		for (int i = 0; i < chArray.length; i++) {
			chArray[i] = replace;
		}

		return String.valueOf(chArray);
	}

}
