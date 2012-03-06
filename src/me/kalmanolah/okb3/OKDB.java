package me.kalmanolah.okb3;

import java.io.File;

import me.kalmanolah.extras.lib.PatPeter.SQLibrary.SQLite;

public class OKDB {
	public static SQLite dbm;

	public static void initialize(OKmain instance) {
		File dbfile = new File("plugins" + File.separator + OKmain.name);
		if (!dbfile.exists()) {
			dbfile.mkdir();
		}
		dbm = new SQLite(OKLogger.getLog(), OKLogger.getPrefix(), "database", dbfile.getPath());
		OKLogger.dbinfo("Loading database...");
		dbm.open();
		if (!dbm.checkTable("players")) {
			OKLogger.dbinfo("Creating table 'players'...");
			String query = "CREATE TABLE players (id INT AUTO_INCREMENT PRIMARY_KEY, player VARCHAR(255), user VARCHAR(255), encpass VARCHAR(255));";
			dbm.createTable(query);
		}
		if (!dbm.checkTable("bans")) {
			OKLogger.dbinfo("Creating table 'bans'...");
			String query = "CREATE TABLE bans (id INT AUTO_INCREMENT PRIMARY_KEY, player VARCHAR(255), reason VARCHAR(255));";
			dbm.createTable(query);
		}
		if (!dbm.checkTable("posts")) {
			OKLogger.dbinfo("Creating table 'posts'...");
			String query = "CREATE TABLE posts (id INT AUTO_INCREMENT PRIMARY_KEY, name VARCHAR(255), postcount INT(10));";
			dbm.createTable(query);
		}
	}

	public static void disable() {
		try {
			dbm.close();
		} catch (Exception e) {
		}
	}
}