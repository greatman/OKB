package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FsyncCommand extends BaseCommand
{

    public FsyncCommand()
    {
        this.command.add("fsync");
        this.permFlag = "bbb.force";
        this.requiredParameters.add("Username");
        this.helpDescription = "Force syncing a player";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
        String name = this.parameters.get(0);
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
            Player target = OKmain.p.getServer().getPlayer(name);
            if (target != null)
            {
                if (target.isOnline())
                {
                    if (user == null)
                    {
                        sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "This user's login details aren't saved yet.");
                    }
                    else
                    {
                        OKFunctions.updateSecure(sender, target, name, user, pass, true);
                    }
                }
            }
    }
}
