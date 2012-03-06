package me.kalmanolah.okb3;

import me.kalmanolah.extras.lib.PatPeter.SQLibrary.MySQL;

public class OKDatabase {
	private static OKmain plugin;
	public static MySQL dbm;

	public static void initialize(OKmain instance) {
		plugin = instance;
		dbm = new MySQL(OKLogger.getLog(), OKLogger.getPrefix(), (String) OKFunctions.getConfig("mysql.host"), (String) OKFunctions.getConfig("mysql.port"), (String) OKFunctions.getConfig("mysql.db"), (String) OKFunctions.getConfig("mysql.user"), (String) OKFunctions.getConfig("mysql.pass"));
		OKLogger.dbinfo("Initializing MySQL connection...");
		dbm.open();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				try {
					dbm.query("SELECT 1 FROM DUAL");
				} catch (Exception e) {
				}
			}
		}, 300, (30 * 20));
		if (dbm.checkConnection()) {
		} else {
			OKLogger.dbinfo("MySQL connection failed!");
		}
	}

	public static void disable() {
		try {
			dbm.close();
		} catch (Exception e) {
		}
	}
}