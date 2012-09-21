import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.greatmancode.extras.Tools;
import com.greatmancode.okb3.OKBSync;
import com.greatmancode.okb3.OKBWebsiteDB;
import com.greatmancode.okb3.OKConfig;

public class XenForo implements OKBSync
{
	public XenForo()
	{
	}
    @Override
    public boolean accountExist(String username, String password)
    {
        boolean exist = false;
        try
        {
            ResultSet rs = OKBWebsiteDB.dbm.prepare("SELECT data FROM " + (String) OKConfig.tablePrefix + "xf_user_authenticate," + (String) OKConfig.tablePrefix
                    + "xf_user WHERE " + OKConfig.tablePrefix + "xf_user.username = '" + username + "' AND " + OKConfig.tablePrefix + "xf_user.user_id = " + OKConfig.tablePrefix + "xf_user_authenticate.user_id").executeQuery();
            if (rs.next())
            {
                do
                {
                    if (Tools.SHA256(Tools.SHA256(password) + Tools.regmatch("\"salt\";.:..:\"(.*)\";.:.:\"hashFunc\"", rs.getString("data"))).equals(Tools.regmatch("\"hash\";.:..:\"(.*)\";.:.:\"salt\"", rs.getString("data"))))
                    {
                        exist = true;
                    }
                }
                while (rs.next());
            }
            rs.close();
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
            OKBWebsiteDB.dbm.prepare("UPDATE " + OKConfig.tablePrefix + "xf_user," + OKConfig.tablePrefix+ "xf_user_authenticate SET user_group_id='" + forumGroupId + "' WHERE " + OKConfig.tablePrefix + "xf_user.username='" + username + "' AND " + OKConfig.tablePrefix + "xf_user.user_id=xf_user_authenticate.user_id").executeUpdate();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void ban(String username, int forumGroupId)
    {
        changeRank(username,forumGroupId); 
    }

    @Override
    public void unban(String username, int forumGroupId)
    {
        changeRank(username,forumGroupId);
    }

    @Override
    public List<Integer> getGroup(String username)
    {
    	//first one
        List<Integer> group = new ArrayList<Integer>(); 
        String query1 = "SELECT user_group_id,data FROM " + OKConfig.tablePrefix + "xf_user," + OKConfig.tablePrefix + "xf_user_authenticate WHERE " + OKConfig.tablePrefix + "xf_user.username = '" + username + "'  AND " + OKConfig.tablePrefix + "xf_user.user_id = " + OKConfig.tablePrefix + "xf_user_authenticate.user_id";
        try
        {
            ResultSet rs = OKBWebsiteDB.dbm.prepare(query1).executeQuery();
            if (rs.next())
            {
                group.add(rs.getInt("user_group_id"));
            }
            rs.close();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (OKConfig.useSecondaryGroups)
		{
        	//second one
            String query2 = "SELECT secondary_group_ids,data FROM " + OKConfig.tablePrefix + "xf_user," + OKConfig.tablePrefix + "xf_user_authenticate WHERE " + OKConfig.tablePrefix + "xf_user.username = '" + username + "'  AND " + OKConfig.tablePrefix + "xf_user.user_id = " + OKConfig.tablePrefix + "xf_user_authenticate.user_id";
            try
            {
                ResultSet rs2 = OKBWebsiteDB.dbm.prepare(query2).executeQuery();
                String secondarygroups = rs2.getString("secondary_group_ids");
                String[] splitgroups = secondarygroups.split(",");
                for (int i = 0; i < splitgroups.length; i++)
                {
                    group.add(Integer.parseInt(splitgroups[i]));
                }
                rs2.close();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
        
        
        return group;
    }

}