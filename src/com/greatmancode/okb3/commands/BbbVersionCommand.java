package com.greatmancode.okb3.commands;

import org.bukkit.ChatColor;

import com.greatmancode.okb3.OKB;

public class BbbVersionCommand extends BaseCommand
{

    public BbbVersionCommand()
    {
        this.command.add("bbbversion");
        this.helpDescription = "Show the version of OKB";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
        sendMessage(colorizeText("--Bulletin Board Bridge by " + OKB.authors.get(0) + ". Original Author kalmanolah--", ChatColor.AQUA));
        sendMessage("This server is using " + colorizeText(OKB.name, ChatColor.GREEN) + " version " + colorizeText(OKB.version, ChatColor.GREEN) + ".");

    }
}
