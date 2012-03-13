package me.kalmanolah.okb3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OKRunnable implements Runnable
{
    public static OKmain plugin;
    Event event;

    public OKRunnable(OKmain instance, Event event)
    {
        plugin = instance;
        this.event = event;
    }

    public void run()
    {
        if (event instanceof PlayerJoinEvent)
        {
            PlayerJoinEvent joinevent = (PlayerJoinEvent) event;
            Player plr = joinevent.getPlayer();
            String name = plr.getName();
            boolean banned = false;
            boolean kicked = false;
            ResultSet test = null;
            test = OKDB.dbm.query("SELECT reason FROM bans WHERE player = '" + name + "'");
            try
            {
                if (test.next())
                {
                    do
                    {
                        OKmain.kicks.add(plr);
                        plr.kickPlayer(test.getString("reason"));
                        OKLogger.info("[BANS] " + name + " was kicked.");
                        banned = true;
                        kicked = true;
                    }
                    while (test.next());
                }
                test.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            if (banned == false)
            {
                String rank = null;
                if ((Integer) OKFunctions.getConfig("mode") == 0)
                {
                    rank = OKFunctions.getRankNormal(name);
                }
                else
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
                        rank = OKFunctions.getRankSecureNopass(user);
                    }
                }
                if (rank != null)
                {
                    if ((Boolean) OKFunctions.getConfig("gen.whitelist"))
                    {
                        String rankidcheck = null;
                        @SuppressWarnings("unchecked")
                        HashMap<String, Boolean> whitelistgroups = (HashMap<String, Boolean>) OKFunctions.getConfig("whitelist.groups");
                        try
                        {
                            rankidcheck = String.valueOf(whitelistgroups.get(rank));
                        }
                        catch (Exception e)
                        {
                        }
                        if (!(Boolean) OKFunctions.getConfig("whitelist.blacklist"))
                        {
                            if (rankidcheck == null)
                            {
                                OKmain.kicks.add(plr);
                                plr.kickPlayer((String) OKFunctions.getConfig("whitelist.message"));
                                OKLogger.info("[WHITELIST] " + name + " was kicked.");
                                kicked = true;
                            }
                            else if (rankidcheck.equals("false") || rankidcheck.equals("null"))
                            {
                                OKmain.kicks.add(plr);
                                plr.kickPlayer((String) OKFunctions.getConfig("whitelist.message"));
                                OKLogger.info("[WHITELIST] " + name + " was kicked.");
                                kicked = true;
                            }
                        }
                        else
                        {
                            if (rankidcheck != null)
                            {
                                if (rankidcheck.equals("false"))
                                {
                                    OKmain.kicks.add(plr);
                                    plr.kickPlayer((String) OKFunctions.getConfig("whitelist.message"));
                                    OKLogger.info("[WHITELIST] " + name + " was kicked.");
                                    kicked = true;
                                }
                            }
                        }
                    }
                    if (!kicked)
                    {
                        plugin.changeGroup(name, rank, plr.getWorld().getName(), false);
                        OKLogger.info("[SYNC] " + name + "'s ranks successfully updated.");
                    }
                }
                else
                {
                    if ((Boolean) OKFunctions.getConfig("gen.whitelist"))
                    {
                        if (!(Boolean) OKFunctions.getConfig("whitelist.blacklist"))
                        {
                            OKmain.kicks.add(plr);
                            plr.kickPlayer((String) OKFunctions.getConfig("whitelist.message"));
                            OKLogger.info("[WHITELIST] " + name + " was kicked.");
                            kicked = true;
                        }
                    }
                }
            }
            if (!kicked)
            {
                if (!OKmain.CheckPermission(plr, "bbb.hide"))
                {
                    try
                    {
                        if (OKmain.cachedjoinmsgs.containsKey(plr))
                        {
                            plugin.getServer().broadcastMessage(OKmain.cachedjoinmsgs.get(plr));
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
                if ((Boolean) OKFunctions.getConfig("gen.nicks"))
                {
                    OKFunctions.updateNick(plr);
                }
                if ((Boolean) OKFunctions.getConfig("gen.posts"))
                {
                    OKFunctions.updatePosts(plr);
                }
            }
            OKmain.cachedjoinmsgs.remove(plr);
        }
        if (event instanceof PlayerTeleportEvent)
        {
            PlayerTeleportEvent teleportevent = (PlayerTeleportEvent) event;
            Player plr = teleportevent.getPlayer();
            String name = plr.getName();
            String world = teleportevent.getTo().getWorld().getName();
            String rank = null;
            if ((Integer) OKFunctions.getConfig("mode") == 0)
            {
                rank = OKFunctions.getRankNormal(name);
            }
            else
            {
                String user = null;
                ResultSet test = null;
                test = OKDB.dbm.query("SELECT user FROM players WHERE player = '" + name + "'");
                try
                {
                    if (test.next())
                    {
                        do
                        {
                            user = test.getString("user");
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
                    rank = OKFunctions.getRankSecureNopass(user);
                }
            }
            if (rank != "nope")
            {
                plugin.changeGroup(name, rank, world, false);
                OKLogger.info("[BRIDGE] " + name + "'s ranks synced.");
            }
            if ((Boolean) OKFunctions.getConfig("gen.nicks"))
            {
                OKFunctions.updateNick(plr);
            }
        }
    }
}