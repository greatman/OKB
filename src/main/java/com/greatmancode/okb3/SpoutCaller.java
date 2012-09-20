package com.greatmancode.okb3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.spout.api.chat.ChatArguments;
import org.spout.api.entity.Player;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.Server;

import com.greatmancode.okb3.commands.CommandManager;

/**
 * Server caller for Spout
 * 
 * @author greatman
 * 
 */
public class SpoutCaller implements Caller {

	private SpoutLoader loader;

	public SpoutCaller(Loader loader) {
		this.loader = (SpoutLoader) loader;
	}

	@Override
	public void disablePlugin() {
		loader.getPluginLoader().disablePlugin(loader);
	}

	@Override
	public boolean checkPermission(String playerName, String perm) {
		boolean result = false;
		Player p = ((Server) loader.getEngine()).getPlayer(playerName, true);
		if (p != null) {
			result = p.hasPermission(perm);
		} else {
			// It's the console
			result = true;
		}
		return result;

	}

	@Override
	public void sendMessage(String playerName, String message) {
		Player p = ((Server) loader.getEngine()).getPlayer(playerName, true);
		if (p != null) {
			p.sendMessage(ChatArguments.fromFormatString(CHAT_PREFIX + message));
		} else {
			Common.getInstance().getLogger().log(Level.INFO, CHAT_PREFIX + message);
		}
	}

	@Override
	public String getPlayerWorld(String playerName) {
		String worldName = "";
		Player p = ((Server) loader.getEngine()).getPlayer(playerName, true);
		if (p != null) {
			worldName = p.getWorld().getName();
		}
		return worldName;
	}

	@Override
	public boolean isOnline(String playerName) {
		return ((Server) loader.getEngine()).getPlayer(playerName, true) != null;
	}

	@Override
	public String addColor(String str) {
		// Useless with Spout
		return null;
	}

	@Override
	public boolean worldExist(String worldName) {
		return loader.getEngine().getWorld(worldName) != null;
	}

	@Override
	public String getDefaultWorld() {
		return loader.getEngine().getWorlds().iterator().next().getName();
	}

	@Override
	public File getDataFolder() {
		return loader.getDataFolder();
	}

	public void addMetricsGraph(String title, String value)
	{
		Graph graph = loader.getMetrics().createGraph(title);
		graph.addPlotter(new MetricsSpout.Plotter(value) {

			@Override
			public int getValue() {
				return 1;
			}
		});
	}
	
	public void addMetricsGraph(String title, boolean value)
	{
		String stringEnabled = "No";
		if (value) {
			stringEnabled = "Yes";
		}
		addMetricsGraph(title, stringEnabled);
	}



	@Override
	public void startMetrics() {
		loader.getMetrics().start();
	}

	@Override
	public int schedule(Runnable entry, long firstStart, long repeating) {
		return schedule(entry, firstStart, repeating, false);
	}

	@Override
	public int schedule(Runnable entry, long firstStart, long repeating, boolean async) {
		if (!async)
			return loader
					.getEngine()
					.getScheduler()
					.scheduleSyncRepeatingTask(loader, entry, TimeUnit.MILLISECONDS.convert(firstStart, TimeUnit.SECONDS),
							TimeUnit.MILLISECONDS.convert(repeating, TimeUnit.SECONDS), TaskPriority.NORMAL);
		else
			return loader
					.getEngine()
					.getScheduler()
					.scheduleAsyncRepeatingTask(loader, entry, TimeUnit.MILLISECONDS.convert(firstStart, TimeUnit.SECONDS),
							TimeUnit.MILLISECONDS.convert(repeating, TimeUnit.SECONDS), TaskPriority.NORMAL);
	}

	@Override
	public List<String> getOnlinePlayers() {
		List<String> list = new ArrayList<String>();
		Player[] pList = ((Server) loader.getEngine()).getOnlinePlayers();
		for (Player p : pList) {
			list.add(p.getName());
		}
		return list;
	}

	@Override
	public void cancelSchedule(int id) {
		loader.getEngine().getScheduler().cancelTask(id);
	}

	@Override
	public int delay(Runnable entry, long start) {
		return delay(entry, start, false);
	}

	@Override
	public int delay(Runnable entry, long start, boolean async) {
		if (!async)
			return loader.getEngine().getScheduler().scheduleSyncDelayedTask(loader, entry, TimeUnit.MILLISECONDS.convert(start, TimeUnit.SECONDS), TaskPriority.NORMAL);
		else
			return loader.getEngine().getScheduler().scheduleAsyncDelayedTask(loader, entry, TimeUnit.MILLISECONDS.convert(start, TimeUnit.SECONDS), TaskPriority.NORMAL);
	}

	@Override
	public void addCommand(String name, String help, CommandManager manager) {
		if (manager instanceof SpoutCommandManager) {
			loader.getEngine().getRootCommand().addSubCommand(loader, name).setHelp(help).setExecutor((SpoutCommandManager) manager);
		}

	}
}
