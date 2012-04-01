package me.kalmanolah.forumlistener;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import me.kalmanolah.extras.PHPBB3Password;
import me.kalmanolah.okb3.OKBSync;
import me.kalmanolah.okb3.OKConfig;
import me.kalmanolah.okb3.OKDatabase;

public class PHPBB3 implements OKBSync
{
    public boolean accountExist(String username, String password)
    {
        boolean exist = false;
        PHPBB3Password phpbb = new PHPBB3Password();
        try
        {
            PreparedStatement query = OKDatabase.dbm.prepare("SELECT user_password FROM " + OKConfig.config.get("db.prefix") + "users WHERE username='" + username + "'");
            ResultSet result = query.executeQuery();
            if (result != null)
            {
                if (result.next())
                {
                    if (phpbb.phpbb_check_hash(password,result.getString("user_password")))
                    {
                        exist = true;
                    }
                    
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return exist;
    }

    @Override
    public void changeRank(String username, int forumGroupId)
    {
        try
        {
            PreparedStatement query = OKDatabase.dbm.prepare("UPDATE " + OKConfig.config.get("db.prefix") + "users SET group_id=" + forumGroupId + " WHERE username='" + username + "'"); 
            query.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void ban(String username, int forumGroupId)
    {
        //TODO: Use official PHPBB ban system
        changeRank(username, forumGroupId);
    }

    @Override
    public void unban(String username, int forumGroupId)
    {
        //TODO: Use official PHPBB ban system
        changeRank(username, forumGroupId);
    }

    @Override
    public List<Integer> getGroup(String username)
    {
        PreparedStatement query;
        List<Integer> list = new ArrayList<Integer>();
        ResultSet result;
        
        try
        {
            if (((Boolean) OKConfig.config.get("use.secondary.group")).booleanValue() == true)
            {
                query = OKDatabase.dbm.prepare("SELECT group_id FROM " + OKConfig.config.get("db.prefix") + "user_group WHERE " + OKConfig.config.get("db.prefix") + "users.username=." + username + "' && " + OKConfig.config.get("db.prefix") + "users.user_id = " + OKConfig.config.get("db.prefix") + "user_group.user_id && " + OKConfig.config.get("db.prefix") + "user_group.user_pending = 0");
            }
            else
            {
                query = OKDatabase.dbm.prepare("SELECT group_id FROM " + OKConfig.config.get("db.prefix") + "users WHERE username='" + username + "'");
            }
            result = query.executeQuery();
            if (result != null)
            {
                if (result.next())
                {
                    do
                    {
                        list.add(result.getInt("group_id"));
                    }
                    while(result.next());
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
