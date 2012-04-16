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
    						databasePort = "";
    
    public static boolean   isWhitelist = false,
                            useSecondaryGroups = false;
    
    public static ArrayList<Integer> whiteListID = null;
    
    public static HashMap<String, HashMap<Integer, String>> groupList = null;
    
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
        
        List<String> typeList = plugin.getConfig().getStringList("group-mapping");
        Iterator<String> iterator = typeList.iterator();
        while (iterator.hasNext())
        {
            String type = iterator.next();
            List<Integer> groupID = plugin.getConfig().getStringList("group-mapping." + type);
        }
    }
}
