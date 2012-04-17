package com.greatmancode.okb3.commands;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.greatmancode.extras.TextUtil;

public class BaseCommand
{

    public List<String> command, requiredParameters, optionalParameters;

    public String permFlag, helpDescription, helpNameAndParams;
    public CommandSender sender;
    public boolean senderMustBePlayer, commandOnly;
    public Player player;

    public List<String> parameters;

    public BaseCommand()
    {
        command = new ArrayList<String>();
        requiredParameters = new ArrayList<String>();
        optionalParameters = new ArrayList<String>();

        senderMustBePlayer = true;
        permFlag = "";
        helpNameAndParams = "fail!";
        helpDescription = "no description";
        commandOnly = true;
    }

    public void execute(CommandSender sender, List<String> parameters)
    {
        this.sender = sender;
        if (!commandOnly && parameters.size() > 0)
        {
            if (this.getCommands().contains(parameters.get(0)))
            {
                parameters.remove(0);
            }
        }
        this.parameters = parameters;
        if (!validateCall())
        {
            return;
        }
        if (sender instanceof Player)
        {
            this.player = (Player) sender;
        }
        perform();
    }

    public boolean validateCall()
    {
        if (this.senderMustBePlayer && !(sender instanceof Player))
        {
            sender.sendMessage("This command can only be used by ingame players.");
            return false;
        }

        if (!hasPermission(sender))
        {
            sender.sendMessage("You lack the permissions to " + this.helpDescription.toLowerCase() + ".");
            return false;
        }

        if (parameters.size() < requiredParameters.size())
        {
            sender.sendMessage("Usage: " + this.getUseageTemplate(false));
            return false;
        }

        return true;
    }

    public void perform()
    {
    }

    public String getUseageTemplate(boolean withDescription)
    {
        String ret = "";

        ret += ChatColor.AQUA;
        ret += "/";
        ret += TextUtil.implode(this.getCommands(), ",") + " ";

        List<String> parts = new ArrayList<String>();

        for (String requiredParameter : this.requiredParameters)
        {
            parts.add("[" + requiredParameter + "]");
        }

        for (String optionalParameter : this.optionalParameters)
        {
            parts.add("*<" + optionalParameter + ">");
        }

        ret += ChatColor.DARK_AQUA;

        ret += TextUtil.implode(parts, " ");

        if (withDescription)
        {
            ret += "  " + ChatColor.YELLOW + this.helpDescription;
        }
        return ret;
    }

    public List<String> getCommands()
    {
        return this.command;
    }

    public boolean hasPermission(CommandSender sender)
    {
        boolean result = true;
        if (!this.permFlag.equalsIgnoreCase(""))
        {
            result = sender.hasPermission(this.permFlag);
        }

        return result;
    }

    public void sendMessage(Player player, String message)
    {
        if (player != null)
            player.sendMessage(ChatColor.GREEN + "[" + ChatColor.YELLOW + "OKB3" + ChatColor.GREEN + "] " + message);
    }

    public void sendMessage(String message)
    {
        sender.sendMessage(ChatColor.GREEN + "[" + ChatColor.YELLOW + "OKB3" + ChatColor.GREEN + "] " + message);
    }

    public String colorizeText(String text, ChatColor color)
    {
        return color + text + ChatColor.WHITE;
    }
}
