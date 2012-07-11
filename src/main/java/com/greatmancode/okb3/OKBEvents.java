package com.greatmancode.okb3;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OKBEvents implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
        OKB.p.getServer().getScheduler().scheduleAsyncDelayedTask(OKB.p, new OKRunnable(OKB.p, event), 0);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
	OKB.p.getServer().getScheduler().scheduleAsyncDelayedTask(OKB.p, new OKRunnable(OKB.p, event), 0);
    }
}
