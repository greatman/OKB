package com.greatmancode.okb3.commands;

import org.bukkit.ChatColor;

import com.greatmancode.okb3.OKFunctions;

public class FUnbanCommand extends BaseCommand
{

    public FUnbanCommand()
    {
        this.command.add("funban");
        this.requiredParameters.add("Player Name");
        this.permFlag = "bbb.unban";
        this.helpDescription = "Unban someone from the forum and ingame";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
		if (OKFunctions.hasAccount(this.parameters.get(0)))
		{
			OKFunctions.unbanUser(this.parameters.get(0));
			sendMessage("Player unbanned!");
		}
		else
		{
			sendMessage(ChatColor.RED + "Player not found.");
		}
    }
}
