package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKFunctions;

public class ResyncCommand extends BaseCommand
{
    public ResyncCommand()
    {
        this.command.add("resync");
        this.helpDescription = "Resync with the forum";
    }

    public void perform()
    {
        String user = null, pass = null;
        ResultSet test = OKDB.dbm.query("SELECT user,encpass FROM players WHERE player = '" + player.getName() + "'");
        try
        {
            if (test.next())
            {
                do
                {
                    user = test.getString("user");
                    pass = test.getString("encpass");
                }
                while (test.next());
            }
            test.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if (user == null)
        {
            sendMessage(ChatColor.RED + "Error:" + ChatColor.GRAY + " You need to have used " + ChatColor.WHITE + "/sync" + ChatColor.GRAY + " first.");
            sendMessage(ChatColor.GOLD + "Notice:" + ChatColor.GRAY + " Please type " + ChatColor.WHITE + "/bbb" + ChatColor.GRAY + " for a help menu.");
        }
        else
        {
            OKFunctions.updateSecure(sender, player, player.getName(), user, pass, false);
        }
    }
}
