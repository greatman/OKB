package com.greatmancode.okb3.commands;

import org.bukkit.entity.Player;

import com.greatmancode.okb3.OKB;
import com.greatmancode.okb3.OKFunctions;

public class FsyncAllCommand extends BaseCommand
{

    public FsyncAllCommand()
    {
        this.command.add("fsyncall");
        this.helpDescription = "Force sync all accounts";
        this.permFlag = "bbb.forceall";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
        Player[] players = OKB.p.getServer().getOnlinePlayers();
        for (Player p : players)
        {
        	if (OKFunctions.hasAccount(p.getName()))
        	{
        		OKFunctions.syncPlayer(p.getName(), p.getWorld().getName());
        	}
        }
        sendMessage("All players with saved accounts has been synced!");
    }

}
