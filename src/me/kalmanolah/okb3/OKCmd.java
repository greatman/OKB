package me.kalmanolah.okb3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OKCmd implements CommandExecutor {
	private static OKmain plugin;

	public OKCmd(OKmain instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean handled = false;
		if (is(label, "bbb")) {
			if (args.length == 0 || (is(args[0], "help") && args.length == 1) || (is(args[0], "?") && args.length == 1)) {
				if (isPlayer(sender)) {
					handled = true;
					sendMessage(sender, ChatColor.GRAY + "--" + ChatColor.GOLD + "B" + ChatColor.WHITE + "ulletin " + ChatColor.GOLD + "B" + ChatColor.WHITE + "oard " + ChatColor.GOLD + "B" + ChatColor.WHITE + "ridge Help Menu" + ChatColor.GRAY
							+ "--");
					if ((Integer) OKFunctions.getConfig("mode") == 1) {
						sendMessage(sender, colorizeText("- /sync <username> <password>", ChatColor.YELLOW));
						sendMessage(sender, ChatColor.GRAY + "---- Syncs you with the forum.");
						sendMessage(sender, colorizeText("- /resync", ChatColor.YELLOW));
						sendMessage(sender, ChatColor.GRAY + "---- Resyncs you with the forum.");
					} else {
						sendMessage(sender, colorizeText("- /sync", ChatColor.YELLOW));
						sendMessage(sender, ChatColor.GRAY + "---- Syncs you with the forum.");
					}
					sendMessage(sender, colorizeText("- /bbb, /bbb ? or /bbb help", ChatColor.YELLOW));
					sendMessage(sender, ChatColor.GRAY + "---- Shows this help menu.");
					sendMessage(sender, colorizeText("- /bbb ver or /bbb v", ChatColor.YELLOW));
					sendMessage(sender, ChatColor.GRAY + "---- Shows version information.");
					if (OKmain.CheckPermission(getPlayer(sender), "bbb.force") || OKmain.CheckPermission(getPlayer(sender), "bbb.forceall") || OKmain.CheckPermission(getPlayer(sender), "bbb.ban")
							|| OKmain.CheckPermission(getPlayer(sender), "bbb.unban") || OKmain.CheckPermission(getPlayer(sender), "bbb.promote") || OKmain.CheckPermission(getPlayer(sender), "bbb.demote")
							|| OKmain.CheckPermission(getPlayer(sender), "bbb.reload") || OKmain.CheckPermission(getPlayer(sender), "bbb.rank")) {
						sendMessage(sender, ChatColor.GRAY + "--" + ChatColor.GOLD + "B" + ChatColor.WHITE + "ulletin " + ChatColor.GOLD + "B" + ChatColor.WHITE + "oard " + ChatColor.GOLD + "B" + ChatColor.WHITE + "ridge Moderation Menu"
								+ ChatColor.GRAY + "--");
					}
					if (OKmain.CheckPermission(getPlayer(sender), "bbb.force")) {
						sendMessage(sender, colorizeText("- /fsync <name>", ChatColor.YELLOW));
					}
					if (OKmain.CheckPermission(getPlayer(sender), "bbb.forceall")) {
						sendMessage(sender, colorizeText("- /fsyncall", ChatColor.YELLOW));
					}
					if ((Boolean) OKFunctions.getConfig("gen.bans")) {
						if (OKmain.CheckPermission(getPlayer(sender), "bbb.ban")) {
							sendMessage(sender, colorizeText("- /fban <name> <reason>", ChatColor.YELLOW));
						}
						if (OKmain.CheckPermission(getPlayer(sender), "bbb.unban")) {
							sendMessage(sender, colorizeText("- /funban <name>", ChatColor.YELLOW));
						}
					}
					if (!(Boolean) OKFunctions.getConfig("modes.multitable") && (Boolean) OKFunctions.getConfig("gen.track")) {
						if (OKmain.CheckPermission(getPlayer(sender), "bbb.promote")) {
							sendMessage(sender, colorizeText("- /fpromote <name>", ChatColor.YELLOW));
						}
						if (OKmain.CheckPermission(getPlayer(sender), "bbb.demote")) {
							sendMessage(sender, colorizeText("- /fdemote <name>", ChatColor.YELLOW));
						}
					}
					if ((Boolean) OKFunctions.getConfig("gen.ranks") && OKmain.CheckPermission(getPlayer(sender), "bbb.rank")) {
						sendMessage(sender, colorizeText("- /frank <name> <rank>", ChatColor.YELLOW));
					}
					if (OKmain.CheckPermission(getPlayer(sender), "bbb.reload")) {
						sendMessage(sender, colorizeText("- /bbb reload", ChatColor.YELLOW));
					}
				}
			} else if ((((args.length == 1) && is(args[0], "ver")) || is(args[0], "v"))) {
				handled = true;
				sendMessage(sender, colorizeText("--Bulletin Board Bridge by " + OKmain.authors.get(0) + "--", ChatColor.AQUA));
				sendMessage(sender, "This server is using " + colorizeText(OKmain.name, ChatColor.GREEN) + " version " + colorizeText(OKmain.version, ChatColor.GREEN) + ".");
			} else if (((args.length == 1) && is(args[0], "reload"))) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.reload")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				OKConfig.loadkeys();
				sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "Bulletin Board Bridge configuration file reloaded.");
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "sync")) {
			if (((Integer) OKFunctions.getConfig("mode") == 1) && (args.length >= 2)) {
				handled = true;
				if (isPlayer(sender)) {
				    int i = 1;
				    String user = "";
				    String pass = "";
				    for(String s:args){
				        if(i < (args.length-1)){
				            user = user + s + " ";
				        }else if(i == (args.length-1)){
				            user = user + s;
				        }else if(i == args.length){
				            pass = s;
				        }
                        i++;
				    }
					String encpass = OKFunctions.getEncPass(user, pass);
					if (encpass.equals("nope")) {
						sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "Incorrect username or password.");
					} else {
						OKFunctions.updateSecure(sender, getPlayer(sender), getName(sender), user, encpass, false);
					}
				}
			} else if (((Integer) OKFunctions.getConfig("mode") == 0) && (args.length == 0)) {
				handled = true;
				if (isPlayer(sender)) {
					OKFunctions.updateNormal(sender, getPlayer(sender), getPlayer(sender).getName(), false);
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "resync") && ((Integer) OKFunctions.getConfig("mode") == 1)) {
			if (args.length == 0) {
				handled = true;
				if (isPlayer(sender)) {
					String name = getPlayer(sender).getName();
					String user = null;
					String pass = null;
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
					if (user == null) {
						sendMessage(sender, ChatColor.RED + "Error:" + ChatColor.GRAY + " You need to have used " + ChatColor.WHITE + "/sync" + ChatColor.GRAY + " first.");
						sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
					} else {
						OKFunctions.updateSecure(sender, getPlayer(sender), getName(sender), user, pass, false);
					}
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "fsync")) {
			if (args.length == 1) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.force")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				String name = args[0];
				if ((Integer) OKFunctions.getConfig("mode") == 1) {
					String user = null;
					String pass = null;
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
					Player target = plugin.getServer().getPlayer(name);
					if (target != null) {
						if (target.isOnline()) {
							if (user == null) {
								sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "This user's login details aren't saved yet.");
							} else {
								OKFunctions.updateSecure(sender, target, name, user, pass, true);
							}
						}
					}
				} else {
					Player target = plugin.getServer().getPlayer(name);
					if (target != null) {
						if (target.isOnline()) {
							OKFunctions.updateNormal(sender, target, name, true);
						}
					}
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "fsyncall")) {
			if ((args.length == 0)) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.forceall")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				Player[] players = plugin.getServer().getOnlinePlayers();
				for (Player p : players) {
					String name = p.getName();
					if ((Integer) OKFunctions.getConfig("mode") == 1) {
						String user = null;
						String pass = null;
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
						if (user == null) {
							sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "Player '" + ChatColor.WHITE + name + ChatColor.GRAY + "' has no saved login details.");
						} else {
							OKFunctions.updateSecure(sender, plugin.getServer().getPlayer(name), name, user, pass, true);
						}
					} else {
						OKFunctions.updateNormal(sender, plugin.getServer().getPlayer(name), name, true);
					}
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "fban")) {
			if (((args.length == 1) || (args.length > 1)) && (Boolean) OKFunctions.getConfig("gen.bans")) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.ban")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				String name = args[0];
				ResultSet test = null;
				test = OKDB.dbm.query("SELECT player FROM bans WHERE player = '" + name + "'");
				try {
					if (test.next()) {
						sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' is already banned.");
					} else {
						if (args.length == 1) {
							OKDB.dbm.query("INSERT into bans (player,reason) VALUES ('" + name + "','" + (String) OKFunctions.getConfig("bans.message") + "')");
						} else {
							OKDB.dbm.query("INSERT into bans (player,reason) VALUES ('" + name + "','" + join(args, 1) + "')");
						}
						if ((Integer) OKFunctions.getConfig("mode") == 1) {
							String user = null;
							ResultSet test2 = null;
							test2 = OKDB.dbm.query("SELECT user FROM players WHERE player = '" + name + "'");
							try {
								if (test2.next()) {
									do {
										user = test2.getString("user");
									} while (test2.next());
								}
								test2.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							if (user != null) {
								Integer enctype = (Integer) OKFunctions.getConfig("enctype");
								String banrank = (String) OKFunctions.getConfig("bans.banrank");
								String table1 = (String) OKFunctions.getConfig("modes.table1");
								String field1 = (String) OKFunctions.getConfig("modes.field1");
								String field3 = (String) OKFunctions.getConfig("modes.field3");
								if ((enctype == 1) || (enctype == 2) || (enctype == 4)) {
									OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + banrank + "' WHERE " + field1 + "='" + user + "'");
								} else if (enctype == 3) {
									String table2 = (String) OKFunctions.getConfig("modes.table2");
									OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + banrank + "' WHERE " + table1 + "." + field1 + "='" + user + "' AND " + table1 + "."
											+ (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field5"));
								}
							}
						} else {
							String banrank = (String) OKFunctions.getConfig("bans.banrank");
							String table1 = (String) OKFunctions.getConfig("modes.table1");
							String field1 = (String) OKFunctions.getConfig("modes.field1");
							String field2 = (String) OKFunctions.getConfig("modes.field2");
							if ((Boolean) OKFunctions.getConfig("modes.multitable")) {
								String table2 = (String) OKFunctions.getConfig("modes.table2");
								OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + banrank + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND " + table1 + "."
										+ (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
							} else {
								OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + banrank + "' WHERE " + field1 + "='" + name + "'");
							}
						}
						OKLogger.info("[BANS] " + name + " has been banned by " + getName(sender) + ".");
						sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "Banned player '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
					}
					test.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (plugin.getServer().getPlayer(name) != null) {
					if (plugin.getServer().getPlayer(name).isOnline()) {
						if (args.length == 1) {
							plugin.getServer().getPlayer(name).kickPlayer((String) OKFunctions.getConfig("bans.message"));
						} else {
							plugin.getServer().getPlayer(name).kickPlayer(join(args, 1));
						}
					}
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "funban")) {
			if ((args.length == 1) && (Boolean) OKFunctions.getConfig("gen.bans")) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.unban")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				String name = args[0];
				ResultSet test = null;
				test = OKDB.dbm.query("SELECT player FROM bans WHERE player = '" + name + "'");
				try {
					if (!test.next()) {
						sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' is not banned.");
					} else {
						OKDB.dbm.query("DELETE FROM bans WHERE player = '" + name + "'");
						if ((Integer) OKFunctions.getConfig("mode") == 1) {
							String user = null;
							ResultSet test2 = null;
							test2 = OKDB.dbm.query("SELECT user FROM players WHERE player = '" + name + "'");
							try {
								if (test2.next()) {
									do {
										user = test2.getString("user");
									} while (test2.next());
								}
								test2.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							if (user != null) {
								Integer enctype = (Integer) OKFunctions.getConfig("enctype");
								String unbanrank = (String) OKFunctions.getConfig("bans.unbanrank");
								String table1 = (String) OKFunctions.getConfig("modes.table1");
								String field1 = (String) OKFunctions.getConfig("modes.field1");
								String field3 = (String) OKFunctions.getConfig("modes.field3");
								if ((enctype == 1) || (enctype == 2) || (enctype == 4)) {
									OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + unbanrank + "' WHERE " + field1 + "='" + user + "'");
								} else if (enctype == 3) {
									String table2 = (String) OKFunctions.getConfig("modes.table2");
									OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + unbanrank + "' WHERE " + table1 + "." + field1 + "='" + user + "' AND " + table1 + "."
											+ (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field5"));
								}
							}
						} else {
							String unbanrank = (String) OKFunctions.getConfig("bans.unbanrank");
							String table1 = (String) OKFunctions.getConfig("modes.table1");
							String field1 = (String) OKFunctions.getConfig("modes.field1");
							String field2 = (String) OKFunctions.getConfig("modes.field2");
							if ((Boolean) OKFunctions.getConfig("modes.multitable")) {
								String table2 = (String) OKFunctions.getConfig("modes.table2");
								OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + unbanrank + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND " + table1 + "."
										+ (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
							} else {
								OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + unbanrank + "' WHERE " + field1 + "='" + name + "'");
							}
						}
						OKLogger.info("[BANS] " + name + " has been unbanned by " + getName(sender) + ".");
						sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "Unbanned player '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
					}
					test.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "fpromote")) {
			if ((args.length == 1) && (Boolean) OKFunctions.getConfig("gen.track")) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.promote")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				String name = args[0];
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
						sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "No promotion tracks found.");
					} else {
						int newrankid = counter2 + 1;
						if (!(newrankid > (track.size() - 1))) {
							if (track.get(newrankid) != null) {
								String newrank = track.get(newrankid);
								if (isPlayer(sender)) {
									if (!OKmain.CheckPermission(getPlayer(sender), "bbb.promote." + newrank)) {
										sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
										return handled;
									}
								}
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
								Player target = plugin.getServer().getPlayer(name);
								if (target != null) {
									if (target.isOnline()) {
										if ((Integer) OKFunctions.getConfig("mode") == 1) {
											if (user == null) {
												sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "This user's login details aren't saved yet.");
											} else {
												OKFunctions.updateSecure(sender, target, name, user, pass, true);
											}
										} else {
											OKFunctions.updateNormal(sender, target, name, true);
										}
									}
								}
								@SuppressWarnings("unchecked")
								HashMap<String, String> rankidentifiers = (HashMap<String, String>) OKFunctions.getConfig("ranks.identifierstwo");
								if (rankidentifiers.containsKey((newrank).toLowerCase())) {
									OKLogger.info("[PROMO-TRACK]" + getName(sender) + " promoted " + name + " to " + rankidentifiers.get(newrank) + ".");
									sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was promoted to '" + ChatColor.WHITE + rankidentifiers.get(newrank) + ChatColor.GRAY + "'.");
								} else {
									OKLogger.info("[PROMO-TRACK] " + getName(sender) + " promoted " + name + ".");
									sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was promoted.");
								}
							} else {
								sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't promote this player any more.");
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't promote this player any more.");
						}
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "No rank found for '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "fdemote")) {
			if ((args.length == 1) && (Boolean) OKFunctions.getConfig("gen.track")) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.demote")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				String name = args[0];
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
						sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "No promotion tracks found.");
					} else {
						int newrankid = counter2 - 1;
						if (!(newrankid < 0)) {
							if (track.get(newrankid) != null) {
								String newrank = track.get(newrankid);
								if (isPlayer(sender)) {
									if (!OKmain.CheckPermission(getPlayer(sender), "bbb.promote." + newrank)) {
										sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
										return handled;
									}
								}
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
								Player target = plugin.getServer().getPlayer(name);
								if (target != null) {
									if (target.isOnline()) {
										if ((Integer) OKFunctions.getConfig("mode") == 1) {
											if (user == null) {
												sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "This user's login details aren't saved yet.");
											} else {
												OKFunctions.updateSecure(sender, target, name, user, pass, true);
											}
										} else {
											OKFunctions.updateNormal(sender, target, name, true);
										}
									}
								}
								@SuppressWarnings("unchecked")
								HashMap<String, String> rankidentifiers = (HashMap<String, String>) OKFunctions.getConfig("ranks.identifierstwo");
								if (rankidentifiers.containsKey(newrank.toLowerCase())) {
									OKLogger.info("[PROMO-TRACK]" + getName(sender) + " demoted " + name + " to " + rankidentifiers.get(newrank) + ".");
									sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was demoted to '" + ChatColor.WHITE + rankidentifiers.get(newrank) + ChatColor.GRAY + "'.");
								} else {
									OKLogger.info("[PROMO-TRACK] " + getName(sender) + " demoted " + name + ".");
									sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was demoted.");
								}
							} else {
								sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't demote this player any more.");
							}
						} else {
							sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't demote this player any more.");
						}
					}
				} else {
					sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "No rank found for '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		if (is(label, "frank")) {
			if (args.length == 2) {
				handled = true;
				if (isPlayer(sender)) {
					if (!OKmain.CheckPermission(getPlayer(sender), "bbb.rank")) {
						sendMessage(sender, colorizeText("You cannot use this command.", ChatColor.LIGHT_PURPLE));
						return handled;
					}
				}
				String name = args[0];
				String id = args[1];
				String user = null;
				String pass = null;
				@SuppressWarnings("unchecked")
				HashMap<String, String> rankidentifiers = (HashMap<String, String>) OKFunctions.getConfig("ranks.identifiers");
				String newrankid = rankidentifiers.get(id.toLowerCase());
				if (newrankid != null) {
					if ((Integer) OKFunctions.getConfig("mode") == 1) {
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
							Integer enctype = (Integer) OKFunctions.getConfig("enctype");
							String table1 = (String) OKFunctions.getConfig("modes.table1");
							String field1 = (String) OKFunctions.getConfig("modes.field1");
							String field3 = (String) OKFunctions.getConfig("modes.field3");
							if ((enctype == 1) || (enctype == 2) || (enctype == 4)) {
								OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + newrankid + "' WHERE " + field1 + "='" + user + "'");
							} else if (enctype == 3) {
								String table2 = (String) OKFunctions.getConfig("modes.table2");
								OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + newrankid + "' WHERE " + table1 + "." + field1 + "='" + user + "' AND " + table1 + "."
										+ (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field5"));
							}
						}
					} else {
						String table1 = (String) OKFunctions.getConfig("modes.table1");
						String field1 = (String) OKFunctions.getConfig("modes.field1");
						String field2 = (String) OKFunctions.getConfig("modes.field2");
						if ((Boolean) OKFunctions.getConfig("modes.multitable")) {
							String table2 = (String) OKFunctions.getConfig("modes.table2");
							OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + newrankid + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND " + table1 + "."
									+ (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
						} else {
							OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + newrankid + "' WHERE " + field1 + "='" + name + "'");
						}
					}
					Player target = plugin.getServer().getPlayer(name);
					if (target != null) {
						if (target.isOnline()) {
							if ((Integer) OKFunctions.getConfig("mode") == 1) {
								if (user == null) {
									sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "This user's login details aren't saved yet.");
								} else {
									OKFunctions.updateSecure(sender, target, name, user, pass, true);
								}
							} else {
								OKFunctions.updateNormal(sender, target, name, true);
							}
						}
					}
					OKLogger.info("[RANK-CHANGING] " + getName(sender) + " changed " + name + "'s rank to " + id.toLowerCase() + ".");
					sendMessage(sender, ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' had their rank changed to '" + ChatColor.WHITE + id + ChatColor.GRAY + "'.");
				} else {
					sendMessage(sender, ChatColor.RED + "Error: " + ChatColor.GRAY + "No rank found matching '" + ChatColor.WHITE + id + ChatColor.GRAY + "'.");
				}
			} else {
				handled = true;
				sendMessage(sender, "Incorrect command usage: /" + label + " " + join(args, 0));
				sendMessage(sender, ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
			}
		}
		return handled;
	}

	private boolean is(String entered, String label) {
		return entered.equalsIgnoreCase(label);
	}

	private boolean sendMessage(CommandSender sender, String message) {
		boolean sent = false;
		if (isPlayer(sender)) {
			Player player = (Player) sender;
			player.sendMessage(message);
			sent = true;
		} else {
			OKLogger.info(message);
			sent = true;
		}
		return sent;
	}

	private String getName(CommandSender sender) {
		String name = "";
		if (isPlayer(sender)) {
			Player player = (Player) sender;
			name = player.getName();
		} else {
			name = "CONSOLE";
		}
		return name;
	}

	private boolean isPlayer(CommandSender sender) {
		return sender instanceof Player;
	}

	private Player getPlayer(CommandSender sender) {
		Player player = null;
		if (isPlayer(sender)) {
			player = (Player) sender;
		}
		return player;
	}

	private String colorizeText(String text, ChatColor color) {
		return color + text + ChatColor.WHITE;
	}

	private String join(String[] split, int delimiter) {
		String joined = "";
		int length = split.length;
		int i = delimiter;
		while (i < (length - 1)) {
			joined += split[i] + " ";
			i++;
		}
		while (i == (length - 1)) {
			joined += split[i];
			i++;
		}
		return joined;
	}
}