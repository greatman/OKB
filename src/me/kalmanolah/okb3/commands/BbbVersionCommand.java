package me.kalmanolah.okb3.commands;

import me.kalmanolah.okb3.OKmain;

import org.bukkit.ChatColor;

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
        sendMessage(colorizeText("--Bulletin Board Bridge by " + OKmain.authors.get(0) + "--", ChatColor.AQUA));
        sendMessage("This server is using " + colorizeText(OKmain.name, ChatColor.GREEN) + " version " + colorizeText(OKmain.version, ChatColor.GREEN) + ".");

    }
}
