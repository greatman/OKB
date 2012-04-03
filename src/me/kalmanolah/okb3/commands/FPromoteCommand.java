package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.kalmanolah.okb3.OKConfig;
import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class FPromoteCommand extends BaseCommand
{
	public FPromoteCommand()
	{
		this.command.add("fpromote");
		this.requiredParameters.add("Username");
		permFlag = "bbb.promote";
		this.helpDescription = "Promote a player both ingame and in the forum";
		this.senderMustBePlayer = false;
	}
	
	@SuppressWarnings("unchecked")
	public void perform()
	{
		
		String name = this.parameters.get(0);
		String user = null;
		List<Integer> rank = new ArrayList<Integer>();
		ResultSet test = null;
		if (Bukkit.getPlayer(name) != null)
		{
			test = OKDB.dbm.query("SELECT user FROM players WHERE player = '" + name + "'");
			try {
				if (test.next()) {
					do {
						user = test.getString("user");
					} while (test.next());
				}
				test.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (user != null) {
				rank = OKmain.sync.getGroup(user);
			}
			List<Integer> track = (List<Integer>) OKFunctions.getConfig("track.track");
			if (rank.size() >= 1) {
				Iterator<Integer> rankit = track.iterator();
				int counter = 0;
				int highest = -1;
				while (rankit.hasNext()) 
				{
					if (rank.contains(rankit.next())) 
					{
						highest = counter;
					}
					counter++;
				}
				if (highest == -1) 
				{
					sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "No promotion tracks found.");
				} 
				else 
				{
					if (track.size() < (highest + 1))
					{
						OKmain.sync.changeRank(name, track.get(highest + 1));
						OKmain.p.changeGroup(name, rank, "nope", true);
						sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' was promoted to '" + ChatColor.WHITE + ((HashMap<Integer,String>) OKConfig.config.get("groups")).get(track.get(highest + 1)) + ChatColor.GRAY + "'.");
					}
					else
					{
						sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "You can't promote this player any more.");
					}
					
				}
			}
			else
			{
				sendMessage(ChatColor.RED + "Error: User never synced.");
			}
		}
		else
		{
			sendMessage(ChatColor.RED + "Error: User not found");
		}
        
	}
}
