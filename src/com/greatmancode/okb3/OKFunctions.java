package com.greatmancode.okb3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    
    public static boolean hasAccount(String playerName)
    {
        return OKB.OKBDb.existUser(playerName); 
    }
    
    //TODO: THIS FUNCTION
    public static void banUser(String playerName, String reason)
    {
        
    }

    
    public static void setPlayerRank(String playerName, int rankID)
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
    public static boolean syncPlayer(String playerName, String worldName)
    {
        // TODO Auto-generated method stub
        if (!OKConfig.groupList.containsKey(worldName))
        {
            worldName = "default";
        }
        if (hasAccount(playerName))
        {
            List<Integer> groupList = OKB.sync.getGroup(playerName);
            Iterator<Integer> iterator = groupList.iterator();
            
            
            //We reset groups
            String[] permGroupList = OKB.perms.getPlayerGroups(worldName, OKB.p.getServer().getPlayer(playerName).getName());
            for (int i = 0; i < permGroupList.length; i++)
            {
                OKB.perms.playerRemoveGroup(OKB.p.getServer().getPlayer(playerName), permGroupList[i]);
            }
            
            HashMap<Integer, String> configurationGroup = OKConfig.groupList.get(worldName);
            while (iterator.hasNext())
            {
                String groupName = configurationGroup.get(iterator);
                
                if (groupName != null)
                {
                    if (worldName == "default")
                    {
                        OKB.perms.playerAddGroup(OKB.p.getServer().getPlayer(playerName), groupName);
                    }
                    else
                    {
                        OKB.perms.playerAddGroup(worldName, playerName, groupName);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
