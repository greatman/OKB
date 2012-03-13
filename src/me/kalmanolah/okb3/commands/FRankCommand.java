package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKDatabase;
import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKLogger;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FRankCommand extends BaseCommand
{

    public FRankCommand()
    {
        this.command.add("frank");
        this.requiredParameters.add("Username");
        this.requiredParameters.add("Rank ID");
        this.helpDescription = "Set the rank of a player both ingame and on the forum";
        this.permFlag = "bbb.rank";
    }

    public void perform()
    {
        String name = this.parameters.get(0);
        String id = this.parameters.get(1);
        String user = null;
        String pass = null;
        @SuppressWarnings("unchecked")
        HashMap<String, String> rankidentifiers = (HashMap<String, String>) OKFunctions.getConfig("ranks.identifiers");
        String newrankid = rankidentifiers.get(id.toLowerCase());
        if (newrankid != null)
        {
            if ((Integer) OKFunctions.getConfig("mode") == 1)
            {
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
                if (user != null)
                {
                    Integer enctype = (Integer) OKFunctions.getConfig("enctype");
                    String table1 = (String) OKFunctions.getConfig("modes.table1");
                    String field1 = (String) OKFunctions.getConfig("modes.field1");
                    String field3 = (String) OKFunctions.getConfig("modes.field3");
                    if ((enctype == 1) || (enctype == 2) || (enctype == 4))
                    {
                        OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + newrankid + "' WHERE " + field1 + "='" + user + "'");
                    }
                    else if (enctype == 3)
                    {
                        String table2 = (String) OKFunctions.getConfig("modes.table2");
                        OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + newrankid + "' WHERE " + table1 + "." + field1 + "='" + user + "' AND "
                                + table1 + "." + (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field5"));
                    }
                }
            }
            else
            {
                String table1 = (String) OKFunctions.getConfig("modes.table1");
                String field1 = (String) OKFunctions.getConfig("modes.field1");
                String field2 = (String) OKFunctions.getConfig("modes.field2");
                if ((Boolean) OKFunctions.getConfig("modes.multitable"))
                {
                    String table2 = (String) OKFunctions.getConfig("modes.table2");
                    OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + newrankid + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND "
                            + table1 + "." + (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
                }
                else
                {
                    OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + newrankid + "' WHERE " + field1 + "='" + name + "'");
                }
            }
            Player target = OKmain.p.getServer().getPlayer(name);
            if (target != null)
            {
                if (target.isOnline())
                {
                    if ((Integer) OKFunctions.getConfig("mode") == 1)
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
                    else
                    {
                        OKFunctions.updateNormal(sender, target, name, true);
                    }
                }
            }
            OKLogger.info("[RANK-CHANGING] " + player.getName() + " changed " + name + "'s rank to " + id.toLowerCase() + ".");
            sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' had their rank changed to '" + ChatColor.WHITE + id
                    + ChatColor.GRAY + "'.");
        }
        else
        {
            sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "No rank found matching '" + ChatColor.WHITE + id + ChatColor.GRAY + "'.");
        }
    }
}
