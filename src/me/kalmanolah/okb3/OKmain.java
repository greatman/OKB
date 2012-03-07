package me.kalmanolah.okb3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class OKmain extends JavaPlugin {
	public static String name;
	public static String version;
	public static List<String> authors;
	private final OKPlayerListener playerListener = new OKPlayerListener(this);
	private final OKCommandManager commandManager = new OKCommandManager(this);
	public static List<Player> kicks = new ArrayList<Player>();
	public static List<Player> portals = new ArrayList<Player>();
	public static HashMap<Player, String> cachedjoinmsgs = new HashMap<Player, String>();
	public static Permission perms;
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	public void onEnable() {
		name = getDescription().getName();
		version = getDescription().getVersion();
		authors = getDescription().getAuthors();
		OKLogger.initialize(Logger.getLogger("Minecraft"));
		OKLogger.info("Attempting to enable " + name + " v" + version + " by " + authors.get(0) + "...");
		PluginManager pm = getServer().getPluginManager();
		if (!setupPermissions()) {
			OKLogger.info("Permissions plugin not found, shutting down...");
			pm.disablePlugin(this);
		} else {
			new OKConfig();
			OKDatabase.initialize(this);
			OKDB.initialize(this);
			new OKFunctions(this);
			pm.registerEvents(playerListener, this);
			setupCommands();
			OKLogger.info(name + " v" + version + " enabled successfully.");
		}
	}

	private void setupCommands() {
	    CommandExecutor cmd = new OKCmd(this);
		addCommand("bbb", cmd);
		addCommand("sync", cmd);
		addCommand("resync", cmd);
		addCommand("fsync", cmd);
		addCommand("fsyncall", cmd);
		addCommand("fban", cmd);
		addCommand("funban", cmd);
		addCommand("fpromote", cmd);
		addCommand("fdemote", cmd);
		addCommand("frank", cmd);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return commandManager.dispatch(sender, cmd, label, args);
	}

	private void addCommand(String command, CommandExecutor executor) {
		getCommand(command).setExecutor(executor);
		commandManager.addCommand(command, executor);
	}

	@SuppressWarnings("unchecked")
	public void changeGroup(String player, String group, String world, Boolean mode) {
		String groupname = null;
		if (mode) {
			world = getServer().getPlayer(player).getWorld().getName();
		}
		HashMap<String, String> worldgroups = (HashMap<String, String>) OKFunctions.getConfig("groups." + world);
		if (worldgroups == null) {
			worldgroups = (HashMap<String, String>) OKFunctions.getConfig("groups");
		}
		if (worldgroups.containsKey(group)) {
			groupname = worldgroups.get(group);
		}
		if (groupname == null) {
			worldgroups = (HashMap<String, String>) OKFunctions.getConfig("groups");
			groupname = worldgroups.get(group);
		}
		if (groupname != null) {
			perms.playerRemoveGroup(getServer().getPlayer(player), "*");
			perms.playerAddGroup(getServer().getPlayer(player), groupname);
			
		}
	}

	public static boolean CheckPermission(Player player, String string) {
			return player.hasPermission(string);
	}

	public static void kickPlayer(Player plr, String string) {
		plr.kickPlayer(string);
	}

	public void onDisable() {
		OKLogger.info("Attempting to disable " + name + "...");
		OKDatabase.disable();
		OKDB.disable();
		getServer().getScheduler().cancelTasks(this);
		OKLogger.info(name + " disabled successfully.");
	}
}