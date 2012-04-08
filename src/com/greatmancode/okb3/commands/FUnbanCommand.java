package com.greatmancode.okb3.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kalmanolah.okb3.OKDB;
import me.kalmanolah.okb3.OKDatabase;
import me.kalmanolah.okb3.OKFunctions;

import org.bukkit.ChatColor;

import com.greatmancode.okb3.OKLogger;

public class FUnbanCommand extends BaseCommand
{

    public FUnbanCommand()
    {
        this.command.add("funban");
        this.permFlag = "bbb.unban";
        this.helpDescription = "Unban someone from the forum and ingame";
        this.senderMustBePlayer = false;
    }

    public void perform()
    {
        String name = this.parameters.get(0);
        ResultSet test = null;
        test = OKDB.dbm.query("SELECT player FROM bans WHERE player = '" + name + "'");
        try
        {
            if (!test.next())
            {
                sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "'" + ChatColor.WHITE + name + ChatColor.GRAY + "' is not banned.");
            }
            else
            {
                OKDB.dbm.query("DELETE FROM bans WHERE player = '" + name + "'");
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
                        String unbanrank = (String) OKFunctions.getConfig("bans.unbanrank");
                        String table1 = (String) OKFunctions.getConfig("modes.table1");
                        String field1 = (String) OKFunctions.getConfig("modes.field1");
                        String field3 = (String) OKFunctions.getConfig("modes.field3");
                        if ((enctype == 1) || (enctype == 2) || (enctype == 4))
                        {
                            OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field3 + "='" + unbanrank + "' WHERE " + field1 + "='" + user + "'");
                        }
                        else if (enctype == 3)
                        {
                            String table2 = (String) OKFunctions.getConfig("modes.table2");
                            OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field3 + "='" + unbanrank + "' WHERE " + table1 + "." + field1 + "='" + user
                                    + "' AND " + table1 + "." + (String) OKFunctions.getConfig("modes.field4") + "=" + table2 + "."
                                    + (String) OKFunctions.getConfig("modes.field5"));
                        }
                    }
                }
                else
                {
                    String unbanrank = (String) OKFunctions.getConfig("bans.unbanrank");
                    String table1 = (String) OKFunctions.getConfig("modes.table1");
                    String field1 = (String) OKFunctions.getConfig("modes.field1");
                    String field2 = (String) OKFunctions.getConfig("modes.field2");
                    if ((Boolean) OKFunctions.getConfig("modes.multitable"))
                    {
                        String table2 = (String) OKFunctions.getConfig("modes.table2");
                        OKDatabase.dbm.query("UPDATE " + table1 + "," + table2 + " SET " + field2 + "='" + unbanrank + "' WHERE " + table2 + "." + field1 + "='" + name + "' AND "
                                + table1 + "." + (String) OKFunctions.getConfig("modes.field3") + "=" + table2 + "." + (String) OKFunctions.getConfig("modes.field4"));
                    }
                    else
                    {
                        OKDatabase.dbm.query("UPDATE " + table1 + " SET " + field2 + "='" + unbanrank + "' WHERE " + field1 + "='" + name + "'");
                    }
                }
                OKLogger.info("[BANS] " + name + " has been unbanned by " + sender.getName() + ".");
                sendMessage(ChatColor.GOLD + "Notice: " + ChatColor.GRAY + "Unbanned player '" + ChatColor.WHITE + name + ChatColor.GRAY + "'.");
            }
            test.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
