package com.greatmancode.okb3;

import me.kalmanolah.okb3.OKmain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OKBEvents implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        OKmain.cachedjoinmsgs.put(event.getPlayer(), event.getJoinMessage());
        event.setJoinMessage(null);
        OKB.p.getServer().getScheduler().scheduleAsyncDelayedTask(OKB.p, new OKRunnable(OKB.p, event), 0);
    }
}
