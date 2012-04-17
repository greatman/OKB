package com.greatmancode.okb3.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.greatmancode.extras.TextUtil;
import com.greatmancode.okb3.OKFunctions;

public class FBanCommand extends BaseCommand
{

    public FBanCommand()
    {
        this.command.add("fban");
        this.permFlag = "bbb.ban";
        this.helpDescription = "Ban someone ingame and on the forum";
        this.requiredParameters.add("Player Name");
        this.optionalParameters.add("Reason");
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
    	Player player = Bukkit.getPlayer(this.parameters.get(0));
		if (player != null || OKFunctions.hasAccount(this.parameters.get(0)))
		{
			String playerName = this.parameters.get(0);
			if (player != null)
			{
				playerName = player.getName();
			}
			
			String reason = "Banned";
			
			if (this.parameters.size() > 1)
			{
				reason = TextUtil.merge(this.parameters, 1);
			}
			
			OKFunctions.banUser(playerName, reason);
			sendMessage("Player banned!");
		}
		else
		{
			sendMessage(ChatColor.RED + "No online player found or he didin't sync!");
		}
    }
}
