package me.kalmanolah.okb3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class OKConfig {
	static HashMap<String, Object> config = new HashMap<String, Object>();
	static String directory = "plugins" + File.separator + OKmain.name;
	static File file = new File(directory + File.separator + "config.yml");

	public OKConfig() {
		configCheck();
	}

	public void configCheck() {
		new File(directory).mkdir();
		if (!file.exists()) {
			try {
				OKLogger.info("Creating configuration file...");
				file.createNewFile();
				addDefaults();
			} catch (Exception ex) {
				ex.printStackTrace();
				OKLogger.error("Error creating configuration file.");
			}
		} else {
			loadkeys();
			OKLogger.info("Configuration file loaded.");
		}
	}

	private static void write(String root, Object x) {
		YamlConfiguration config = load();
		config.set(root, x);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readString(String root) {
		YamlConfiguration config = load();
		return config.getString(root);
	}

	private static YamlConfiguration load() {
		try {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			return config;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<String> readStringList(String root) {
		YamlConfiguration config = load();
		List<String> list = new ArrayList<String>();
		ConfigurationSection section = config.getConfigurationSection(root);
		if(section == null){
		    return null;
		}else{
		    Set<String> keys = section.getKeys(false);
		    if(keys == null){
		        return null;
		    }else{
	            for (String key : config.getConfigurationSection(root).getKeys(false)) {
                    list.add(key);
                }
	            return list;
		    }
		}
	}

	private static void addDefaults() {
		write("general.mode", "secure");
		write("general.enable-whitelist", "false");
		write("general.enable-nickname-syncing", "false");
		write("general.enable-synced-banning", "false");
		write("general.enable-promotion-track", "false");
		write("general.enable-rank-changing", "false");
		write("general.enable-post-count-rewarding", "false");
		write("modes.normal.user-table", "user_table");
		write("modes.normal.minecraft-login-name-field", "minecraft_name");
		write("modes.normal.rank-id-field", "rank_group_field");
		write("modes.normal.multitable.enable-multiple-tables", "false");
		write("modes.normal.multitable.second-table", "second_table");
		write("modes.normal.multitable.user-id-field-in-user-table", "user_id_field_user_table");
		write("modes.normal.multitable.user-id-field-in-second-table", "user_id_field_second_table");
		write("modes.secure.forum", "phpbb");
		write("modes.secure.type1.user-table", "user_table");
		write("modes.secure.type1.username-field", "username_field");
		write("modes.secure.type1.password-field", "password_field");
		write("modes.secure.type1.rank-id-field", "rank_group_field");
		write("modes.secure.type2.user-table", "user_table");
		write("modes.secure.type2.username-field", "username_field");
		write("modes.secure.type2.password-field", "password_field");
		write("modes.secure.type2.rank-id-field", "rank_group_field");
		write("modes.secure.type2.location-of-passgen-php", "http://yoursite.com/passgen.php");
		write("modes.secure.type2.password-set-in-config-inc-php", "12345pass");
		write("modes.secure.type3.user-table", "user_table");
		write("modes.secure.type3.rank-id-field", "rank_group_field");
		write("modes.secure.type3.username-field", "username_field");
		write("modes.secure.type3.password-field", "password_field");
		write("modes.secure.type3.second-table", "second_table");
		write("modes.secure.type3.user-id-field-in-user-table", "user_id_field_user_table");
		write("modes.secure.type3.user-id-field-in-second-table", "user_id_field_second_table");
		write("modes.secure.type4.user-table", "user_table");
		write("modes.secure.type4.username-field", "username_field");
		write("modes.secure.type4.password-field", "password-field");
		write("modes.secure.type4.rank-id-field", "rank_group_field");
		write("modes.secure.type4.location-of-passgen-php", "http://yoursite.com/passgen.php");
		write("modes.secure.type4.password-set-in-config-inc-php", "12345pass");
		write("mysql-connection.mysql-host", "localhost");
		write("mysql-connection.mysql-user", "root");
		write("mysql-connection.mysql-password", "password");
		write("mysql-connection.mysql-database-name", "database");
		write("mysql-connection.mysql-port", "3306");
		write("group-mapping.default.1", "ExampleGroup1");
		write("group-mapping.default.2", "ExampleGroup2");
		write("group-mapping.worlds.exampleworld1.1", "ExampleGroup1");
		write("group-mapping.worlds.exampleworld1.2", "ExampleGroup2");
		write("extras.whitelist.kick-message", "This is the message a user will see if their group isn't whitelisted.");
		write("extras.whitelist.use-as-blacklist", "false");
		write("extras.whitelist.groups.1", "true");
		write("extras.whitelist.groups.2", "false");
		write("extras.nickname-syncing.nickname-field-in-usertable", "nickname_field_user_table");
		write("extras.synced-banning.banned-user-forum-rank-id", "0");
		write("extras.synced-banning.unbanned-user-forum-rank-id", "10");
		write("extras.synced-banning.ban-message", "This is the message a user will see if they are banned.");
		write("extras.promotion-track.track", "6|4|68|123|10");
		write("extras.rank-changing.identifiers.1", "NickNameforRank1");
		write("extras.rank-changing.identifiers.7", "NickNameforAnotherRank");
		write("extras.post-count-rewarding.post-count-field-in-user-table", "post-count-field-user-table");
		write("extras.post-count-rewarding.currency-reward-per-post", "40");
		loadkeys();
	}

	public static void loadkeys() {
		config.clear();
		if (readString("general.mode").equalsIgnoreCase("secure")) {
			config.put("mode", 1);
		} else {
			config.put("mode", 0);
		}
		config.put("gen.whitelist", Boolean.parseBoolean(readString("general.enable-whitelist")));
		config.put("gen.nicks", Boolean.parseBoolean(readString("general.enable-nickname-syncing")));
		config.put("gen.bans", Boolean.parseBoolean(readString("general.enable-synced-banning")));
		config.put("gen.track", Boolean.parseBoolean(readString("general.enable-promotion-track")));
		config.put("gen.ranks", Boolean.parseBoolean(readString("general.enable-rank-changing")));
		config.put("gen.posts", Boolean.parseBoolean(readString("general.enable-post-count-rewarding")));
		config.put("mysql.host", readString("mysql-connection.mysql-host"));
		config.put("mysql.user", readString("mysql-connection.mysql-user"));
		config.put("mysql.pass", readString("mysql-connection.mysql-password"));
		config.put("mysql.db", readString("mysql-connection.mysql-database-name"));
		config.put("mysql.port", readString("mysql-connection.mysql-port"));
		if ((Integer) config.get("mode") == 1) {
			config.put("secure.forum", readString("modes.secure.forum"));
			if (((String) config.get("secure.forum")).equalsIgnoreCase("smf") || ((String) config.get("secure.forum")).equalsIgnoreCase("vbulletin") || ((String) config.get("secure.forum")).equalsIgnoreCase("mybb")
					|| ((String) config.get("secure.forum")).equalsIgnoreCase("ipb") || ((String) config.get("secure.forum")).equalsIgnoreCase("wbb")) {
				config.put("enctype", 1);
			} else if (((String) config.get("secure.forum")).equalsIgnoreCase("bbpress") || ((String) config.get("secure.forum")).equalsIgnoreCase("phpbb") || ((String) config.get("secure.forum")).equalsIgnoreCase("vanilla")) {
				config.put("enctype", 2);
			} else if (((String) config.get("secure.forum")).equalsIgnoreCase("xenforo") || ((String) config.get("secure.forum")).equalsIgnoreCase("kunena")) {
				config.put("enctype", 3);
			} else if (((String) config.get("secure.forum")).equalsIgnoreCase("custom")) {
				config.put("enctype", 4);
			}
			if ((Integer) config.get("enctype") == 1) {
				config.put("modes.table1", readString("modes.secure.type1.user-table"));
				config.put("modes.field1", readString("modes.secure.type1.username-field"));
				config.put("modes.field2", readString("modes.secure.type1.password-field"));
				config.put("modes.field3", readString("modes.secure.type1.rank-id-field"));
			} else if ((Integer) config.get("enctype") == 2) {
				config.put("modes.table1", readString("modes.secure.type2.user-table"));
				config.put("modes.field1", readString("modes.secure.type2.username-field"));
				config.put("modes.field2", readString("modes.secure.type2.password-field"));
				config.put("modes.field3", readString("modes.secure.type2.rank-id-field"));
				config.put("modes.phploc", readString("modes.secure.type2.location-of-passgen-php"));
				config.put("modes.phppass", readString("modes.secure.type2.password-set-in-config-inc-php"));
			} else if ((Integer) config.get("enctype") == 3) {
				config.put("modes.table1", readString("modes.secure.type3.user-table"));
				config.put("modes.table2", readString("modes.secure.type3.second-table"));
				config.put("modes.field1", readString("modes.secure.type3.username-field"));
				config.put("modes.field2", readString("modes.secure.type3.password-field"));
				config.put("modes.field3", readString("modes.secure.type3.rank-id-field"));
				config.put("modes.field4", readString("modes.secure.type3.user-id-field-in-user-table"));
				config.put("modes.field5", readString("modes.secure.type3.user-id-field-in-second-table"));
			} else if ((Integer) config.get("enctype") == 4) {
				config.put("modes.table1", readString("modes.secure.type4.user-table"));
				config.put("modes.field1", readString("modes.secure.type4.username-field"));
				config.put("modes.field2", readString("modes.secure.type4.password-field"));
				config.put("modes.field3", readString("modes.secure.type4.rank-id-field"));
				config.put("modes.phploc", readString("modes.secure.type4.location-of-passgen-php"));
				config.put("modes.phppass", readString("modes.secure.type4.password-set-in-config-inc-php"));
			}
			config.put("modes.multitable", false);
		} else {
			config.put("modes.table1", readString("modes.normal.user-table"));
			config.put("modes.field1", readString("modes.normal.minecraft-login-name-field"));
			config.put("modes.field2", readString("modes.normal.rank-id-field"));
			config.put("modes.multitable", Boolean.parseBoolean(readString("modes.normal.multitable.enable-multiple-tables")));
			if ((Boolean) config.get("modes.multitable")) {
				config.put("modes.table2", readString("modes.normal.multitable.second-table"));
				config.put("modes.field3", readString("modes.normal.multitable.user-id-field-in-user-table"));
				config.put("modes.field4", readString("modes.normal.multitable.user-id-field-in-second-table"));
			}
		}
		HashMap<String, String> groupmap = new HashMap<String, String>();
		List<String> groups = readStringList("group-mapping.default");
		Iterator<String> group = groups.iterator();
		while (group.hasNext()) {
			String nextgroup = group.next();
			groupmap.put(nextgroup, readString("group-mapping.default." + nextgroup));
		}
		config.put("groups", groupmap);
		List<String> worlds = readStringList("group-mapping.worlds");
		if(worlds != null && !worlds.isEmpty()){
	      Iterator<String> world = worlds.iterator();
	        while (world.hasNext()) {
	            String nextworld = world.next();
	            HashMap<String, String> worldmap = new HashMap<String, String>();
	            List<String> worldgroups = readStringList("group-mapping.worlds." + nextworld);
	            Iterator<String> worldgroup = worldgroups.iterator();
	            while (worldgroup.hasNext()) {
	                String nextworldgroup = worldgroup.next();
	                worldmap.put(nextworldgroup, readString("group-mapping.worlds." + nextworld + "." + nextworldgroup));
	            }
	            config.put("groups." + nextworld, worldmap);
	        }
		}
		if ((Boolean) config.get("gen.whitelist")) {
			config.put("whitelist.message", readString("extras.whitelist.kick-message"));
			config.put("whitelist.blacklist", Boolean.parseBoolean(readString("extras.whitelist.use-as-blacklist")));
			HashMap<String, Boolean> whitelistmap = new HashMap<String, Boolean>();
			List<String> whitelistgroups = readStringList("extras.whitelist.groups");
			Iterator<String> whitelistgroup = whitelistgroups.iterator();
			while (whitelistgroup.hasNext()) {
				String nextgroup = whitelistgroup.next();
				whitelistmap.put(nextgroup, Boolean.parseBoolean(readString("extras.whitelist.groups." + nextgroup)));
			}
			config.put("whitelist.groups", whitelistmap);
		}
		if ((Boolean) config.get("gen.nicks")) {
			config.put("nicks.field", readString("extras.nickname-syncing.nickname-field-in-usertable"));
		}
		if ((Boolean) config.get("gen.bans")) {
			config.put("bans.banrank", readString("extras.synced-banning.banned-user-forum-rank-id"));
			config.put("bans.unbanrank", readString("extras.synced-banning.unbanned-user-forum-rank-id"));
			config.put("bans.message", readString("extras.synced-banning.ban-message"));
		}
		if ((Boolean) config.get("gen.track")) {
			String[] track = readString("extras.promotion-track.track").split("\\|");
			List<String> promotiontrack = new ArrayList<String>();
			for (String t : track) {
				promotiontrack.add(t);
			}
			config.put("track.track", promotiontrack);
			HashMap<String, String> identifiers = new HashMap<String, String>();
			HashMap<String, String> identifierstwo = new HashMap<String, String>();
			config.put("ranks.identifiers", identifiers);
			config.put("ranks.identifierstwo", identifierstwo);
		}
		if ((Boolean) config.get("gen.ranks")) {
			List<String> rankidentifiers = readStringList("extras.rank-changing.identifiers");
			Iterator<String> rankidentifier = rankidentifiers.iterator();
			HashMap<String, String> identifiers = new HashMap<String, String>();
			HashMap<String, String> identifierstwo = new HashMap<String, String>();
			while (rankidentifier.hasNext()) {
				String nextidentifier = rankidentifier.next();
				identifiers.put(readString("extras.rank-changing.identifiers." + nextidentifier).toLowerCase(), nextidentifier);
				identifierstwo.put(nextidentifier, readString("extras.rank-changing.identifiers." + nextidentifier).toLowerCase());
			}
			config.put("ranks.identifiers", identifiers);
			config.put("ranks.identifierstwo", identifierstwo);
		}
		if ((Boolean) config.get("gen.posts")) {
			config.put("posts.field", readString("extras.post-count-rewarding.post-count-field-in-user-table"));
			config.put("posts.reward", Double.parseDouble(readString("extras.post-count-rewarding.currency-reward-per-post")));
		}
	}
}