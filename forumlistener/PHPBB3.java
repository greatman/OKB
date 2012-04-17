import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import com.greatmancode.extras.PHPBB3Password;
import com.greatmancode.okb3.OKBSync;
import com.greatmancode.okb3.OKBWebsiteDB;
import com.greatmancode.okb3.OKConfig;

public class PHPBB3 implements OKBSync
{
    public boolean accountExist(String username, String password)
    {
        boolean exist = false;
        PHPBB3Password phpbb = new PHPBB3Password();
        try
        {
            PreparedStatement query = OKBWebsiteDB.dbm.prepare("SELECT user_password FROM " + OKConfig.tablePrefix + "users WHERE username='" + username + "'");
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
            result.close();
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
            OKBWebsiteDB.dbm.prepare("UPDATE " + OKConfig.tablePrefix + "users SET group_id=" + forumGroupId + " WHERE username='" + username + "'").executeUpdate(); 
           
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
            if (OKConfig.useSecondaryGroups)
            {
                query = OKBWebsiteDB.dbm.prepare("SELECT group_id FROM " + OKConfig.tablePrefix + "user_group WHERE " + OKConfig.tablePrefix + "users.username=." + username + "' && " + OKConfig.tablePrefix + "users.user_id = " + OKConfig.tablePrefix + "user_group.user_id && " + OKConfig.tablePrefix + "user_group.user_pending = 0");
            }
            else
            {
                query = OKBWebsiteDB.dbm.prepare("SELECT group_id FROM " + OKConfig.tablePrefix + "users WHERE username='" + username + "'");
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
            result.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
