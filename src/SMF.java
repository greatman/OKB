import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.greatmancode.extras.Tools;
import com.greatmancode.okb3.OKBSync;
import com.greatmancode.okb3.OKBWebsiteDB;
import com.greatmancode.okb3.OKConfig;

public class SMF implements OKBSync
{

    @Override
    public boolean accountExist(String username, String password)
    {
        boolean exist = false;
        try
        {
            PreparedStatement query = OKBWebsiteDB.dbm.prepare("SELECT passwd FROM " + OKConfig.tablePrefix + "members WHERE member_name='" + username + "'");
            ResultSet result = query.executeQuery();
            if (result != null)
            {
                if (result.next())
                {
                    if (result.getString("passwd").equals(Tools.SHA1(username.toLowerCase() + password)))
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
        catch (NoSuchAlgorithmException e)
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
            OKBWebsiteDB.dbm.prepare("UPDATE " + OKConfig.tablePrefix + "members SET id_group=" + forumGroupId + " WHERE member_name='" + username + "'").executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void ban(String username, int forumGroupId)
    {
        // TODO Use official SMF ban
        changeRank(username, forumGroupId);
    }

    @Override
    public void unban(String username, int forumGroupId)
    {
        // TODO Use official SMF ban
        changeRank(username, forumGroupId);
    }

    @Override
    public List<Integer> getGroup(String username)
    {
        //TODO Find how SMF multigroup works
        List<Integer> list = new ArrayList<Integer>();
        try
        {
            ResultSet result = OKBWebsiteDB.dbm.prepare("SELECT id_group FROM " + OKConfig.tablePrefix + "members WHERE member_name='" + username + "'").executeQuery();
            if (result != null)
            {
                if (result.next())
                {
                    list.add(result.getInt("id_group"));
                }
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
        return list;
    }

}
