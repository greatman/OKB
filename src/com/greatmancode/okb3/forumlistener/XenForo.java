package com.greatmancode.okb3.forumlistener;

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

	String fieldName = "user_group_id";
	
	public XenForo()
	{
		if (OKConfig.useSecondaryGroups)
		{
			fieldName = "secondary_group_ids";
		}
	}
    @Override
    public boolean accountExist(String username, String password)
    {
        String encpass = "nope";
        boolean exist = false;
        try
        {
            ResultSet rs = OKBWebsiteDB.dbm.prepare("SELECT data FROM " + (String) OKConfig.tablePrefix + "xf_user_authenticate," + (String) OKConfig.tablePrefix
                    + "xf_user WHERE " + OKConfig.tablePrefix + "xf_user.username = '" + username + "' AND " + OKConfig.tablePrefix + "xf_user.user_id = " + OKConfig.tablePrefix + "xf_user_authenticate.user_id").executeQuery();
            if (rs.next())
            {
                do
                {
                    encpass = Tools.SHA256(Tools.SHA256(password) + Tools.regmatch("\"salt\";.:..:\"(.*)\";.:.:\"hashFunc\"", rs.getString("data")));
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
        if (!encpass.equals("nope"))
        {
            exist = true;
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
        List<Integer> group = new ArrayList<Integer>(); 
        String query1 = "SELECT " + fieldName + ",data FROM " + OKConfig.tablePrefix + "xf_user," + OKConfig.tablePrefix + "xf_user_authenticate WHERE " + OKConfig.tablePrefix + "xf_user.username = '" + username + "'  AND " + OKConfig.tablePrefix + "xf_user.user_id = " + OKConfig.tablePrefix + "xf_user_authenticate.user_id";
        try
        {
            ResultSet rs = OKBWebsiteDB.dbm.prepare(query1).executeQuery();
            if (rs.next())
            {
                group.add(rs.getInt(fieldName));
            }
            rs.close();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return group;
    }

}
