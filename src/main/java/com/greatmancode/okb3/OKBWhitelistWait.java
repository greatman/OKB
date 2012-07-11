package com.greatmancode.okb3;

import org.bukkit.entity.Player;

public class OKBWhitelistWait implements Runnable
{
    Player player;
    public OKBWhitelistWait(Player player)
    {
        this.player = player;
    }
    @Override
    public void run()
    {
    	OKB.p.getLogger().info("We waited 30s!");
        // TODO Auto-generated method stub
        if (!OKFunctions.hasAccount(player.getName()))
        {
            player.kickPlayer("You went over the 30 secs limit to sync. Sync faster!");
        }
    }

}
