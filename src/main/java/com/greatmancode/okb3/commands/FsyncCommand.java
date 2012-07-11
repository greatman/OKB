package com.greatmancode.okb3.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.greatmancode.okb3.OKFunctions;

public class FsyncCommand extends BaseCommand
{

    public FsyncCommand()
    {
        this.command.add("fsync");
        this.permFlag = "bbb.force";
        this.requiredParameters.add("Username");
        this.helpDescription = "Force syncing a player";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
    	Player player = Bukkit.getPlayer(this.parameters.get(0));
		if (player != null || OKFunctions.hasAccount(this.parameters.get(0)))
		{
			if (player != null)
			{
				OKFunctions.syncPlayer(player.getName(), player.getWorld().getName());
			}
			else
			{
				OKFunctions.syncPlayer(this.parameters.get(0), "default");
			}
			sendMessage("Player force-synced!");
	    }
		else
		{
			sendMessage(ChatColor.RED + "No online player found or he didin't sync!");
		}
    }
}
