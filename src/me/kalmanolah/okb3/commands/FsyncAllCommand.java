package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FsyncAllCommand extends BaseCommand
{

    public FsyncAllCommand()
    {
        this.command.add("fsyncall");
        this.helpDescription = "Force sync all accounts";
        this.permFlag = "bbb.forceall";
    }

    public void perform()
    {
        Player[] players = OKmain.p.getServer().getOnlinePlayers();
        for (Player p : players)
        {
            String name = p.getName();
            if ((Integer) OKFunctions.getConfig("mode") == 1)
            {
                String user = null;
                String pass = null;
                ResultSet test = null;
                test = OKDB.dbm.query("SELECT user,encpass FROM players WHERE player = '" + name + "'");
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
                    sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "Player '" + ChatColor.WHITE + name + ChatColor.GRAY + "' has no saved login details.");
                }
                else
                {
                    OKFunctions.updateSecure(sender, p, name, user, pass, true);
                }
            }
            else
            {
                OKFunctions.updateNormal(sender, p, name, true);
            }
        }
    }

}
