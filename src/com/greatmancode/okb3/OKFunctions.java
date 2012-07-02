package com.greatmancode.okb3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OKFunctions
{
    public static boolean accountExist(String username, String password)
    {
        return OKB.sync.accountExist(username,password);
    }
    
    /**
     * Get all forum groups ID from a user.
     * @param playerName The player Name
     * @return A List of forum group ID or null if the user never synced.
     */
    public static List<Integer> getGroupList(String playerName)
    {
        List<Integer> returnList = null;
        if (OKB.OKBDb.existUser(playerName))
        {
           returnList = OKB.sync.getGroup(OKB.OKBDb.getUser(playerName));
        }
        return returnList;
    }
    
    public static synchronized boolean hasAccount(String playerName)
    {
        return OKB.OKBDb.existUser(playerName); 
    }
    
    public static synchronized void banUser(String playerName, String reason)
    {
    	if (OKB.OKBDb.existUser(playerName))
    	{
    		OKB.OKBDb.banUser(playerName, reason);
    		OKB.sync.changeRank(OKB.OKBDb.getUser(playerName), OKConfig.bannedGroupID);
    		
    		Player p = Bukkit.getPlayer(playerName);
    		
    		if (p != null)
    		{
    			p.kickPlayer(OKConfig.bannedMsg + " : " + reason);
    		}
    	}
    }

    public static synchronized void unbanUser(String playerName)
    {
    	if (OKB.OKBDb.existUser(playerName) && OKB.OKBDb.isBannedUser(playerName))
    	{
    		OKB.OKBDb.unbanUser(playerName);
    		OKB.sync.changeRank(OKB.OKBDb.getUser(playerName), OKConfig.unbannedGroupID);
    	}
    }
    
    public static synchronized void setPlayerRank(String playerName, int rankID)
    {
    	if (hasAccount(playerName))
    	{
    		OKB.sync.changeRank(OKB.OKBDb.getUser(playerName), rankID);
    	}
    }
    /**
     * Sync a player
     * @param playerName The player name
     * @param worldName The world to sync to
     * @return True if the user synced, false if the user doesn't have a account.
     */
    public static synchronized boolean syncPlayer(String playerName, String worldName)
    {
        if (!OKConfig.groupList.containsKey(worldName))
        {
            worldName = "default";
        }
        if (hasAccount(playerName))
        {
            List<Integer> groupList = OKB.sync.getGroup(OKB.OKBDb.getUser(playerName));
            Iterator<Integer> groupIterator = groupList.iterator();
            //We reset groups
            String[] permGroupList = OKB.perms.getPlayerGroups(OKB.p.getServer().getPlayer(playerName));
            for (int i = 0; i < permGroupList.length; i++)
            {
        	modifyGroup(true, playerName, permGroupList[i], worldName);
            }
            HashMap<Integer, String> configurationGroup = OKConfig.groupList.get(worldName);
            
            while (groupIterator.hasNext())
            {
                String groupName = configurationGroup.get(groupIterator.next());
                
                if (groupName != null)
                {
                    modifyGroup(false, playerName, groupName, worldName);
                }
            }
            return true;
        }
        return false;
    }
    
    public static synchronized void modifyGroup(boolean remove, String playerName, String groupName, String worldName)
    {
    	if (groupName != null)
    	{
    		if (remove)
    		{
    				if (worldName.equals("default"))
    			    {
    			        OKB.perms.playerRemoveGroup(OKB.p.getServer().getPlayer(playerName).getWorld(), OKB.p.getServer().getPlayer(playerName).getName(), groupName);
    			    }
    			    else
    			    {
    			        OKB.perms.playerRemoveGroup(worldName, OKB.p.getServer().getPlayer(playerName).getName(), groupName);
    			    }
    		    
    		}
    		else
    		{
    		    if (worldName.equals("default"))
    		    {
    		    	OKB.perms.playerAddGroup(OKB.p.getServer().getPlayer(playerName), groupName);
    		    }
    		    else
    		    {
    		    	OKB.perms.playerAddGroup(worldName, playerName, groupName);
    		    }
    		}
    	}
    }
}
