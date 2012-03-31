package me.kalmanolah.forumlistener;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.kalmanolah.extras.Tools;
import me.kalmanolah.okb3.OKBSync;
import me.kalmanolah.okb3.OKConfig;
import me.kalmanolah.okb3.OKDatabase;

public class IPB implements OKBSync
{

	public IPB()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accountExist(String username, String password)
	{
		boolean exist = false;
		String encpass = "nope";
		try
        {
            ResultSet rs = OKDatabase.dbm.query("SELECT members_pass_hash,members_pass_salt FROM " + OKConfig.config.get("db.prefix") + "members WHERE members_l_username = '" + username + "'");
            if (rs.next())
            {
                do
                {
                    encpass = Tools.md5(Tools.md5(rs.getString("members_pass_salt")) + Tools.md5(password));
                    if (encpass.equals(rs.getString("members_pass_hash")))
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
		// TODO Auto-generated method stub
		OKDatabase.dbm.query("UPDATE " + OKConfig.config.get("db.prefix") + "members SET member_group_id=" + forumGroupId + " WHERE members_l_username = '" + username + "'");
	}

	@Override
	public void ban(String username, int forumGroupId)
	{
		// TODO Auto-generated method stub
		changeRank(username,forumGroupId);
	}

	@Override
	public void unban(String username, int forumGroupId)
	{
		// TODO Auto-generated method stub
		changeRank(username,forumGroupId);
	}

	@Override
	public List<Integer> getGroup(String username)
	{
		List<Integer> group = new ArrayList<Integer>();
		try
		{
			ResultSet rs = OKDatabase.dbm.query("SELECT member_group_id FROM " + OKConfig.config.get("db.prefix") + "members WHERE members_l_username = '" + username + "'");
			if (rs.next())
			{
				do
				{
					group.add(rs.getInt("member_group_id"));
				}
				while(rs.next());
			}
		}
		catch (SQLException e)
        {
            e.printStackTrace();
        }
		
		return group;
	}

}
