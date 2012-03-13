package me.kalmanolah.okb3;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OKPlayerListener implements Listener
{
    private static OKmain plugin;

    public OKPlayerListener(OKmain instance)
    {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if ((event.getFrom().getWorld().getName() != event.getTo().getWorld().getName()) || (OKmain.portals.contains(event.getPlayer())))
        {
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new OKRunnable(plugin, event), 0);
            OKmain.portals.remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        OKmain.cachedjoinmsgs.put(event.getPlayer(), event.getJoinMessage());
        event.setJoinMessage(null);
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new OKRunnable(plugin, event), 0);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPortal(PlayerPortalEvent event)
    {
        OKmain.portals.add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (OKmain.kicks.contains(event.getPlayer()))
        {
            event.setQuitMessage(null);
            OKmain.kicks.remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKick(PlayerKickEvent event)
    {
        if (OKmain.kicks.contains(event.getPlayer()))
        {
            event.setLeaveMessage(null);
            OKmain.kicks.remove(event.getPlayer());
        }
    }
}
