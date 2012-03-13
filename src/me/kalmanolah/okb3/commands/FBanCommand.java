package me.kalmanolah.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kalmanolah.extras.TextUtil;
import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKDatabase;
import me.kalmanolah.okb3.OKFunctions;
import me.kalmanolah.okb3.OKLogger;
import me.kalmanolah.okb3.OKmain;

import org.bukkit.ChatColor;

public class FBanCommand extends BaseCommand
{

    public FBanCommand()
    {
        this.command.add("fban");
        this.permFlag = "bbb.ban";
        this.helpDescription = "Ban someone ingame and on the forum";
        this.requiredParameters.add("Player Name");
        this.optionalParameters.add("Reason");
    }

    public void perform()
    {
        String name = this.parameters.get(0);
        ResultSet test = null;
        test = OKDB.dbm.query("SELECT player FROM bans WHERE player = '" + name + "'");
        try
        {
            if (test.next())
            {
                sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' is already banned.");
            }
            else
            {
                if (this.parameters.size() == 1)
                {
                    OKDB.dbm.query("INSERT into bans (player,reason) VALUES ('" + name + "','" + (String) OKFunctions.getConfig("bans.message") + "')");
                }
                else
                {
                    OKDB.dbm.query("INSERT into bans (player,reason) VALUES ('" + name + "','" + TextUtil.merge(this.parameters, 1) + "')");
                }
                if ((Integer) OKFunctions.getConfig("mode") == 1)
                {
                    String user = null;
                    ResultSet test2 = null;
                    test2 = OKDB.dbm.query("SELECT user FROM players WHERE player = '" + name + "'");
                    try
                    {
                        if (test2.next())
                        {
                            do
                            {
                                user = test2.getString("user");
                            }
                            while (test2.next());
                        }
                        test2.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                    if (user != null)
                    {
                        Integer enctype = (Integer) OKFunctions.getConfig("enctype");
                        String banrank = (String) OKFunctions.getConfig("bans.banrank");
                        String table1 = (String) OKFunctions.getConfig("modes.table1");
                        String field1 = (String) OKFunctions.getConfig("modes.field1");
                        String field3 = (String) OKFunctions.getConfig("modes.field3");
                        if ((enctype == 1) || (enctype == 2) || (enctype == 4))
                        {
                            OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + banrank + "' WHERE " + field1 + "='" + user + "'");
                        }
                        else if (enctype == 3)
                        {
                            String table2 = (String) OKFunctions.getConfig("modes.table2");
                            OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + banrank + "' WHERE " + table1 + "." + field1 + "='" + user
                                    + "' AND " + table1 + "." + (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "."
                                    + (String) OKFunctions.getConfig("modes.field5"));
                        }
                    }
                }
                else
                {
                    String banrank = (String) OKFunctions.getConfig("bans.banrank");
                    String table1 = (String) OKFunctions.getConfig("modes.table1");
                    String field1 = (String) OKFunctions.getConfig("modes.field1");
                    String field2 = (String) OKFunctions.getConfig("modes.field2");
                    if ((Boolean) OKFunctions.getConfig("modes.multitable"))
                    {
                        String table2 = (String) OKFunctions.getConfig("modes.table2");
                        OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + banrank + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND "
                                + table1 + "." + (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
                    }
                    else
                    {
                        OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + banrank + "' WHERE " + field1 + "='" + name + "'");
                    }
                }
                OKLogger.info("[BANS] " + name + " has been banned by " + player.getName() + ".");
                sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "Banned player '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
            }
            test.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if (OKmain.p.getServer().getPlayer(name) != null)
        {
            if (OKmain.p.getServer().getPlayer(name).isOnline())
            {
                if (this.parameters.size() == 1)
                {
                    OKmain.p.getServer().getPlayer(name).kickPlayer((String) OKFunctions.getConfig("bans.message"));
                }
                else
                {
                    OKmain.p.getServer().getPlayer(name).kickPlayer(TextUtil.merge(this.parameters, 1));
                }
            }
        }
    }
}
