package com.greatmancode.okb3.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.greatmancode.okb3.OKLogger;

public class FRankCommand extends BaseCommand
{

    public FRankCommand()
    {
        this.command.add("frank");
        this.requiredParameters.add("Username");
        this.requiredParameters.add("Rank ID");
        this.helpDescription = "Set the rank of a player both ingame and on the forum";
        this.permFlag = "bbb.rank";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
        String name = this.parameters.get(0);
        String id = this.parameters.get(1);
        
        @SuppressWarnings("unchecked")
        HashMap<String, String> rankidentifiers = (HashMap<String, String>) OKFunctions.getConfig("ranks.identifiers");
        String newrankid = rankidentifiers.get(id.toLowerCase());
        if (newrankid != null)
        {
        	if (Bukkit.getPlayer(name) != null)
        	{
        		List<Integer> ranklist = new ArrayList<Integer>();
        		ranklist.add(Integer.parseInt(newrankid));
        		OKmain.p.changeGroup(name, ranklist, "nope", true);
            	OKmain.sync.changeRank(name, ranklist.get(0));
            	OKLogger.info("[RANK-CHANGING] " + player.getName() + " changed " + name + "'s rank to " + id.toLowerCase() + ".");
                sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' had their rank changed to '" + ChatColor.WHITE + id
                        + ChatColor.GRAY + "'.");
        	}
        	else
        	{
        		sendMessage(ChatColor.RED + "Error: User not found");
        	}
        }
        else
        {
        	sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "No rank found matching '" + ChatColor.WHITE + id + ChatColor.GRAY + "'.");    
        }
    }
}
