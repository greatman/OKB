package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKDatabase;
import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKLogger;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FPromoteCommand extends BaseCommand
{
	public FPromoteCommand()
	{
		this.command.add("fpromote");
		this.requiredParameters.add("Username");
		permFlag = "bbb.promote";
		this.helpDescription = "Promote a player both ingame and in the forum";
	}
	
	public void perform()
	{
		String name = this.parameters.get(0);
		String user = null;
		String pass = null;
		String rank = null;
		if ((Integer) OKFunctions.getConfig("mode") == 0) {
			rank = OKFunctions.getRankNormal(name);
		} else {
			ResultSet test = null;
            test = OKDB.dbm.query("SELECT user,encpass FROM players WHERE player = '" + name + "'");
			try {
				if (test.next()) {
					do {
						user = test.getString("user");
						pass = test.getString("encpass");
					} while (test.next());
				}
				test.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (user != null) {
				rank = OKFunctions.getRankSecurePass(user, pass);
			}
		}
		@SuppressWarnings("unchecked")
		List<String> track = (List<String>) OKFunctions.getConfig("track.track");
		if (rank != null) {
			Iterator<String> rankit = track.iterator();
			int counter = 0;
			int counter2 = -1;
			while (rankit.hasNext()) {
				if (rankit.next().equalsIgnoreCase(rank)) {
					counter2 = counter;
				}
				counter++;
			}
			if (counter2 == -1) {
				sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "No promotion tracks found.");
			} else {
				int newrankid = counter2 + 1;
				if (!(newrankid > (track.size() - 1))) {
					if (track.get(newrankid) != null) {
						String newrank = track.get(newrankid);
						if ((Integer) OKFunctions.getConfig("mode") == 1) {
							if (user != null) {
								Integer enctype = (Integer) OKFunctions.getConfig("enctype");
								String table1 = (String) OKFunctions.getConfig("modes.table1");
								String field1 = (String) OKFunctions.getConfig("modes.field1");
								String field3 = (String) OKFunctions.getConfig("modes.field3");
								if ((enctype == 1) || (enctype == 2) || (enctype == 4)) {
									OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + newrank + "' WHERE " + field1 + "='" + user + "'");
								} else if (enctype == 3) {
									String table2 = (String) OKFunctions.getConfig("modes.table2");
									OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + newrank + "' WHERE " + table1 + "." + field1 + "='" + user + "' AND " + table1 + "."
											+ (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field5"));
								}
							}
						} else {
							String table1 = (String) OKFunctions.getConfig("modes.table1");
							String field1 = (String) OKFunctions.getConfig("modes.field1");
							String field2 = (String) OKFunctions.getConfig("modes.field2");
							if ((Boolean) OKFunctions.getConfig("modes.multitable")) {
								String table2 = (String) OKFunctions.getConfig("modes.table2");
								OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + newrank + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND " + table1 + "."
										+ (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
							} else {
								OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + newrank + "' WHERE " + field1 + "='" + name + "'");
							}
						}
						Player target = OKmain.p.getServer().getPlayer(name);
						if (target != null) {
							if (target.isOnline()) {
									if (user == null) {
										sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "This user's login details aren't saved yet.");
									} else {
										OKFunctions.updateSecure(sender, target, name, user, pass, true);
									}
							}
						}
						@SuppressWarnings("unchecked")
						HashMap<String, String> rankidentifiers = (HashMap<String, String>) OKFunctions.getConfig("ranks.identifierstwo");
						if (rankidentifiers.containsKey((newrank).toLowerCase())) {
							OKLogger.info("[PROMO-TRACK]" + player.getName() + " promoted " + name + " to " + rankidentifiers.get(newrank) + ".");
							sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was promoted to '" + ChatColor.WHITE + rankidentifiers.get(newrank) + ChatColor.GRAY + "'.");
						} else {
							OKLogger.info("[PROMO-TRACK] " + player.getName() + " promoted " + name + ".");
							sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was promoted.");
						}
					} else {
						sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't promote this player any more.");
					}
				} else {
					sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't promote this player any more.");
				}
			}
		} else {
			sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "No rank found for '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
		}
	}
}
