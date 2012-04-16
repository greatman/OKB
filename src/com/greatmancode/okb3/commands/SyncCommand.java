package com.greatmancode.okb3.commands;

import org.bukkit.ChatColor;

import com.greatmancode.okb3.OKB;
import com.greatmancode.okb3.OKFunctions;

public class SyncCommand extends BaseCommand
{
	public SyncCommand()
	{
		this.command.add("sync");
		this.helpDescription = "Sync your account with the forum";
		this.requiredParameters.add("Username");
		this.requiredParameters.add("Password");
	}
	
	public void perform()
	{
        if (OKFunctions.accountExist(this.parameters.get(0), this.parameters.get(1)))
        {
            OKB.OKBDb.addUser(player.getName(), this.parameters.get(0));
            OKB.playerList.put(player.getName(), this.parameters.get(0));
            OKFunctions.syncPlayer(player.getName(), player.getWorld().getName());
            sendMessage("You are now synced with your website account! Each time you connect your group will be automaticly imported.");
        }
        else
        {
            sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "Incorrect username or password.");
        }
	}

}
