package com.greatmancode.okb3.commands;

import org.bukkit.ChatColor;

import com.greatmancode.okb3.OKFunctions;


public class ResyncCommand extends BaseCommand
{
    public ResyncCommand()
    {
        this.command.add("resync");
        this.helpDescription = "Resync with the forum";
    }

    public void perform()
    {
        if (OKFunctions.syncPlayer(player.getName(), player.getWorld().getName()))
        {
            sendMessage("You are now synced!");
        }
        else
        {
            sendMessage(ChatColor.RED + "You need to setup your sync first! Type /sync for more information.");
        }
    }
}
