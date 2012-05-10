package com.greatmancode.okb3.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.greatmancode.okb3.OKConfig;
import com.greatmancode.okb3.OKFunctions;

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
	
	public void perform()
	{
    	if (!OKConfig.useSecondaryGroups)
    	{
    		Player player = Bukkit.getPlayer(this.parameters.get(0));
			if (player != null || OKFunctions.hasAccount(this.parameters.get(0)))
    		{
				String playerName = this.parameters.get(0);
				if (player != null)
				{
					playerName = player.getName();
				}
    			List<Integer> groupList = OKFunctions.getGroupList(playerName);
    			int position = -1;
    			
    			for (int i = 0; i < OKConfig.promotionList.length; i++)
    			{
    				if (groupList.get(0) == OKConfig.promotionList[i])
    				{
    					position = i;
    				}
    			}
    			
    			if (position != -1)
    			{
    				if (position != (OKConfig.promotionList.length - 1))
    				{
    					OKFunctions.setPlayerRank(playerName, OKConfig.promotionList[position + 1]);
    					String rank = OKConfig.promotionList[position + 1] + "";
    					if (OKConfig.rankIdentifier.containsKey(OKConfig.promotionList[position + 1]))
    					{
    						rank = OKConfig.rankIdentifier.get(OKConfig.promotionList[position + 1]);
    					}
    					sendMessage(ChatColor.GREEN + "User promoted to rank " + rank + "!");
    				}
    				else
    				{
    					sendMessage(ChatColor.RED + "Unable to promote the player, he is already at the highest rank!");
    				}
    			}
    			else
    			{
    				sendMessage(ChatColor.RED + "Unable to promote the player, the forum rank is not in the promotion track!");
    			}
    		}
    		else
    		{
    			sendMessage(ChatColor.RED + "Unable to promote the player, he didin't sync!");
    		}
    	}
    	else
    	{
    		sendMessage(ChatColor.RED + "Promotion is not currently compatible with multi-group enabled.");
    	}
	}
}
