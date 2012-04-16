package com.greatmancode.okb3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class OKConfig
{
    public static String 	linkName = "",
                            tablePrefix = "",
    						databaseHost = "",
    						databaseUser = "", 
    						databasePassword = "", 
    						databaseDB = "", 
    						databasePort = "",
    						whitelistKickMsg = "";
    
    public static int		bannedGroupID;
    
    public static boolean   isWhitelist = false,
                            useSecondaryGroups = false;
    
    public static HashMap<String, HashMap<Integer, String>> groupList = null;
    
    public static List<Integer> whitelist = null;
    
    public OKConfig(OKB plugin)
    {
        File file = new File(plugin.getDataFolder() + "config.yml");
        

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
        
        List<String> typeList = plugin.getConfig().getStringList("group-mapping");
        Iterator<String> iterator = typeList.iterator();
        
        while (iterator.hasNext())
        {
        	//We get the ranks
            String type = iterator.next();
            
            List<String> groupID = plugin.getConfig().getStringList("group-mapping." + type);
            Iterator<String> groupIDIterator = groupID.iterator();
            
            HashMap<Integer,String> groups = new HashMap<Integer,String>();
            
            while (groupIDIterator.hasNext())
            {
            	int groupforumid = Integer.parseInt(groupIDIterator.next());
            	groups.put(groupforumid, plugin.getConfig().getString("group-mapping." + type + "." + groupforumid));
            }
            
            groupList.put(type, groups);
        }
        
        //Whitelist manager
        whitelist = new ArrayList<Integer>();
        whitelistKickMsg = plugin.getConfig().getString("extras.whitelist.kick-message");
        
        typeList = plugin.getConfig().getStringList("extras.whitelist.groups");
        iterator = typeList.iterator();
        while(iterator.hasNext())
        {
        	whitelist.add(Integer.parseInt(iterator.next()));
        }
        
        
        //Ban feature
        bannedGroupID = plugin.getConfig().getInt("extras.synced-banning.banned-user-forum-rank-id");
        
        
        //TODO: Post count, promotion-track
    }
}
