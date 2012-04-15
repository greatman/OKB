package com.greatmancode.okb3;

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
        boolean result = false;
        if (OKB.OKBDb.existUser(playerName))
        {
            result = true;
        }
        return result;
    }
    
    //TODO: THIS FUNCTION
    public static void banUser(String playerName, String reason)
    {
        
    }

    public static void syncPlayer(String playerName, String worldName)
    {
        // TODO Auto-generated method stub
        if (hasAccount(playerName))
        {
            List<Integer> groupList = OKB.sync.getGroup(playerName);
            if (first)
            {
                String[] permGroupList = OKB.perms.getPlayerGroups(OKB.p.getServer().getPlayer(playerName));
                for (int i = 0; i < permGroupList.length; i++)
                {
                    OKB.p.perms.playerRemoveGroup(OKB.p.getServer().getPlayer(playerName), permGroupList[i]);
                }
                first = false;
            }
         
            perms.playerAddGroup(getServer().getPlayer(player), groupname);
        }
    }
}
