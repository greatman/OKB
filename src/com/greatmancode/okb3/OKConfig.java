package com.greatmancode.okb3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.configuration.MemorySection;

public class OKConfig
{
    public static String 	linkName = "",
                            tablePrefix = "",
    						databaseHost = "",
    						databaseUser = "", 
    						databasePassword = "", 
    						databaseDB = "", 
    						databasePort = "",
    						whitelistKickMsg = "",
    						bannedMsg = "";
    
    public static int		bannedGroupID,
    						unbannedGroupID;
    
    public static boolean   isWhitelist = false,
                            useSecondaryGroups = false;
    
    public static int[]	promotionList;
    
    public static HashMap<String, HashMap<Integer, String>> groupList = null;
    
    public static List<Integer> whitelist = null;
    
    public static HashMap<Integer, String> rankIdentifier = null;

    
    public OKConfig(OKB plugin)
    {
        File file = new File(plugin.getDataFolder() + File.separator + "config.yml");
        

        if (!file.exists())
        {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
        }
        
        linkName = plugin.getConfig().getString("configuration.forum");
        tablePrefix = plugin.getConfig().getString("configuration.prefix");
        useSecondaryGroups = plugin.getConfig().getBoolean("configuration.use-secondary-group");
        
        databaseHost = plugin.getConfig().getString("mysql-connection.mysql-host");
        databaseUser = plugin.getConfig().getString("mysql-connection.mysql-user");
        databasePassword = plugin.getConfig().getString("mysql-connection.mysql-password");
        databaseDB = plugin.getConfig().getString("mysql-connection.mysql-database-name");
        databasePort = plugin.getConfig().getString("mysql-connection.mysql-port");
        
        //We get the types (World + default)
        groupList = new HashMap<String, HashMap<Integer, String>>();
        
        //We get the worlds and load it to be able to iterate.
        Map<String,Object> typeList = plugin.getConfig().getConfigurationSection("group-mapping").getValues(false);
        Set<Entry<String,Object>> entryset = typeList.entrySet();
        Iterator<Entry<String, Object>> iterator = entryset.iterator();
        
        //The group list (Made outside to not redeclare all the time
        HashMap<Integer,String> groups = null;
        
        //Iterate through all worlds
        while (iterator.hasNext())
        {
        	//We get the Worlds
            Entry<String, Object> type = iterator.next();
            
            //We load the ranks to be able to iterate
            Map<String, Object> groupID = ((MemorySection) type.getValue()).getValues(false);
            Set<Entry<String,Object>> groupIDentryset = groupID.entrySet();
            Iterator<Entry<String, Object>> iterator2 = groupIDentryset.iterator();
            
            //We clear the hashmap of groups
            groups =  new HashMap<Integer,String>();
            
            //We iterate through the ranks
            while(iterator2.hasNext())
            {
                //We put the groups into the hashmap
                Entry<String, Object> groupidentry = iterator2.next();
                groups.put(Integer.parseInt(groupidentry.getKey()), (String) groupidentry.getValue());
            }
            
            //We put the world into the final hashmap
            groupList.put(type.getKey(), groups);
        }
        
        //Whitelist manager
        isWhitelist = plugin.getConfig().getBoolean("general.enable-whitelist");
        whitelist = new ArrayList<Integer>();
        whitelistKickMsg = plugin.getConfig().getString("extras.whitelist.kick-message");
        
        
        //We load the group list
        List<Integer> typeListWhitelist = plugin.getConfig().getIntegerList("extras.whitelist.groups");
        Iterator<Integer> iteratorwhitelist = typeListWhitelist.iterator();
        while (iteratorwhitelist.hasNext())
        {
            //We get the groups
            whitelist.add(iteratorwhitelist.next());
        }
        
        
        //Ban feature
        bannedGroupID = plugin.getConfig().getInt("extras.synced-banning.banned-user-forum-rank-id");
        unbannedGroupID = plugin.getConfig().getInt("extras.synced-banning.unbanned-user-forum-rank-id");
        bannedMsg = plugin.getConfig().getString("extras.synced-banning.ban-message");
        
        //TODO: Post count
        
        
        //Promotion track
        String[] track = plugin.getConfig().getString("extras.promotion-track.track").split("\\|");
        promotionList = new int[track.length];
        for (int i = 0; i < track.length; i++)
        {
        	promotionList[i] = Integer.parseInt(track[i]);
        }
        
        //Rank identifier
        //We load the group list
        rankIdentifier = new HashMap<Integer,String>();
        typeList = plugin.getConfig().getConfigurationSection("extras.rank-changing.identifiers").getValues(false);
        entryset = typeList.entrySet();
        iterator = entryset.iterator();
        while (iterator.hasNext())
        {
            //We get the groups
            Entry<String, Object> type = iterator.next();
            rankIdentifier.put(Integer.parseInt(type.getKey()), (String) type.getValue());
        }
    }
}
