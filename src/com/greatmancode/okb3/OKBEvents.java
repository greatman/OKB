package com.greatmancode.okb3;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OKBEvents implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
        OKB.p.getServer().getScheduler().scheduleAsyncDelayedTask(OKB.p, new OKRunnable(OKB.p, event), 0);
    }
}
