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

public class vBulletin implements OKBSync {

	public vBulletin() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accountExist(String username, String password) {
		String encpass = "";
		boolean exist = false;
		try {
			ResultSet rs =
					OKDatabase.dbm.prepare(
							"SELECT password,salt FROM " + OKConfig.config.get("db.prefix")
									+ "user WHERE username = '" + username + "'").executeQuery();
			if (rs.next()) {
				do {
					encpass = Tools.md5(Tools.md5(password) + rs.getString("salt"));
					if (encpass.equals(rs.getString("password"))) {
						exist = true;
					}
				} while (rs.next());
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return exist;
	}

	@Override
	public void changeRank(String username, int forumGroupId) {
		try {
            OKDatabase.dbm.prepare("UPDATE " + OKConfig.config.get("db.prefix") + "user SET usergroupid=" + forumGroupId + " WHERE username = '" + username + "'").executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void ban(String username, int forumGroupId) {
		// TODO use vBulletin ban system
		changeRank(username,forumGroupId);
	}

	@Override
	public void unban(String username, int forumGroupId) {
		// TODO use vBulletin ban system
		changeRank(username,forumGroupId);
	}

	@Override
	public List<Integer> getGroup(String username) {
		List<Integer> group = new ArrayList<Integer>();
		try
		{
			ResultSet rs = OKDatabase.dbm.prepare("SELECT usergroupid FROM " + OKConfig.config.get("db.prefix") + "user WHERE username = '" + username + "'").executeQuery();
			if (rs.next())
			{
				do
				{
					group.add(rs.getInt("usergroupid"));
				}
				while(rs.next());
			}
			rs.close();
			rs = OKDatabase.dbm.prepare("SELECT membergroupids FROM " + OKConfig.config.get("db.prefix") + "user WHERE username = '" + username + "'").executeQuery();
			if (rs.next())
			{
				do
				{
					group.add(rs.getInt("usergroupids"));
				}
				while(rs.next());
			}
			rs.close();
		}
		catch (SQLException e)
        {
            e.printStackTrace();
        }
		
		return group;
	}

}
