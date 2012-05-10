package com.greatmancode.okb3;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.greatmancode.extras.lib.PatPeter.SQLibrary.SQLite;

public class OKBInternalDB
{
	SQLite db;
	private OKB p;
    public OKBInternalDB(OKB thePlugin)
    {
    	p = thePlugin;
    	db = new SQLite(p.getLogger(), "OKB", "database", p.getDataFolder().toString());
    	if (!db.checkTable("players"))
        {
            OKLogger.dbinfo("Creating table 'players'...");
            String query = "CREATE TABLE players (id INT AUTO_INCREMENT PRIMARY_KEY, player VARCHAR(255), user VARCHAR(255));";
            db.createTable(query);
        }
        if (!db.checkTable("bans"))
        {
            OKLogger.dbinfo("Creating table 'bans'...");
            String query = "CREATE TABLE bans (id INT AUTO_INCREMENT PRIMARY_KEY, player VARCHAR(255), reason VARCHAR(255));";
            db.createTable(query);
        }
        //TODO: How posts works?
        //if (!db.checkTable("posts"))
        //{
        //   OKLogger.dbinfo("Creating table 'posts'...");
        //	String query = "CREATE TABLE posts (id INT AUTO_INCREMENT PRIMARY_KEY, name VARCHAR(255), postcount INT(10));";
        //    db.createTable(query);
        //}
    }
    
    public void addUser(String playerName, String websiteUser)
    {
        if (existUser(playerName))
        {
            deleteUser(playerName);
        }
    	db.query("INSERT INTO players(player, user) VALUES('" + playerName + "', '" + websiteUser + "')");
    }
    
    public boolean existUser(String playerName)
    {
    	boolean result = false;
    	try
		{
	    	ResultSet rs = db.query("SELECT * FROM players WHERE player='" + playerName + "'");
	    	
	    	if (rs != null)
	    	{
	    		
					if (rs.next())
					{
						if (rs.getString("player").equals(playerName))
						{
							result = true;
						}
					}
					rs.close();
	    	}
		}
    	catch (SQLException e)
		{
			e.printStackTrace();
		}
    	return result;
    }
    
    public void deleteUser(String playerName)
    {
		db.query("DELETE FROM players WHERE player='" + playerName + "'");
    }
    
    public String getUser(String playerName)
    {
        String user = "";
        if (existUser(playerName))
        {
            try
            {
                ResultSet rs = db.query("SELECT * FROM players WHERE player='" + playerName + "'");
                if (rs != null)
                {
                    if (rs.next())
                    {
                        user = rs.getString("user");
                    }
                    rs.close();
                }
                
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            
        }
        return user;
    }
    
    public void banUser(String playerName, String reason)
    {
    	db.query("INSERT INTO bans(player,reason) VALUES('" + playerName + "', '" + reason + "')");
    }
    
    public boolean isBannedUser(String playerName)
    {
    	boolean result = false;
    	try
    	{
    		ResultSet rs = db.query("SELECT * FROM bans WHERE player='" + playerName + "'");
    		if (rs != null)
    		{
    			if (rs.next())
    			{
    				if (rs.getString("player").equals(playerName))
    				{
    					result = true;
    				}
    			}
    			rs.close();
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return result;
    }
    
    public void unbanUser(String playerName)
    {
    	db.query("DELETE FROM bans WHERE player='" + playerName + "'");
    }
    public String getBanReason(String playerName)
    {
        String reason = "";
        if (isBannedUser(playerName))
        {
            try
            {
                ResultSet rs = db.query("SELECT * FROM bans WHERE player='" + playerName + "'");
                if (rs != null)
                {
                    if (rs.next())
                    {
                        reason = rs.getString("reason");
                    }
                    rs.close();
                }
                
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            
        }
        return reason;
    }
    public void close()
    {
    	db.close();
    }
}
