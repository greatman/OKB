package com.greatmancode.okb3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OKRunnable implements Runnable
{
    public static OKB plugin;
    Event event;
    
    public OKRunnable(OKB instance, Event event)
    {
        plugin = instance;
        this.event = event;
    }
    public void run()
    {
        //Somebody is connecting
        if (this.event instanceof PlayerJoinEvent)
        {
            PlayerJoinEvent joinevent = (PlayerJoinEvent) event;
            Player thePlayer = joinevent.getPlayer();
            if (!OKB.OKBDb.isBannedUser(thePlayer.getName()))
            {
                if (OKConfig.isWhitelist)
                {
                    if (OKFunctions.hasAccount(thePlayer.getName()))
                    {
                        List<Integer> groupList = OKFunctions.getGroupList(thePlayer.getName());
                        Iterator<Integer> iterator = groupList.iterator();
                        
                        boolean isWhitelist = false;
                        
                        while(iterator.hasNext())
                        {
                            if (OKConfig.whitelist.contains(iterator.next()))
                            {
                                isWhitelist = true;
                            }
                        }
                        if (!isWhitelist)
                        {
                            thePlayer.kickPlayer(OKConfig.whitelistKickMsg);;
                        }
                    }
                    else
                    {
                        //We give a grace period of 30 secs to sync
                    	OKB.p.getLogger().info("Let's wait 30 seconds");
                        OKB.p.getServer().getScheduler().scheduleAsyncDelayedTask(OKB.p, new OKBWhitelistWait(thePlayer), 600L);
                    }
                }
                //We alert that the player connected
                
                //Is the player login stealth enabled?
                if (!thePlayer.hasPermission("bbb.hide"))
                {
                    OKB.p.getServer().broadcastMessage(ChatColor.YELLOW + thePlayer.getDisplayName() + ChatColor.YELLOW + " joined the game!");
                    joinevent.setJoinMessage(ChatColor.YELLOW + thePlayer.getDisplayName() + " joined the game!");
                }
                OKB.worldUpdate.put(thePlayer.getName(), thePlayer.getWorld().getName());
                //Everything done, we sync the player!
                OKFunctions.syncPlayer(thePlayer.getName(), thePlayer.getWorld().getName());
            }
            else
            {
                thePlayer.kickPlayer(OKConfig.bannedMsg + " : " + OKB.OKBDb.getBanReason(thePlayer.getName()));
            }
        }
        else if (this.event instanceof PlayerTeleportEvent)
        {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            Player thePlayer = ((PlayerTeleportEvent)this.event).getPlayer();
            boolean update = false;
            if (OKB.worldUpdate.containsKey(thePlayer.getName()))
            {
            	if (!OKB.worldUpdate.get(thePlayer.getName()).equals(thePlayer.getWorld().getName())) {
            		update = true;
            	}
            } else {
            	update = true;
            }
            
            if (update) {
            	OKB.worldUpdate.put(thePlayer.getName(), thePlayer.getWorld().getName());
        		OKFunctions.syncPlayer(thePlayer.getName(), thePlayer.getWorld().getName());
            }
        }
    }
}
