package me.kalmanolah.okb3.commands;

import org.bukkit.ChatColor;

import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKmain;

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
        if (OKmain.sync.accountExist(this.parameters.get(0), this.parameters.get(1)))
        {
            OKFunctions.updateSecure(sender, player, player.getName(), this.parameters.get(0), this.parameters.get(1), false);
        }
        else
        {
            sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "Incorrect username or password.");
        }
	}

}
