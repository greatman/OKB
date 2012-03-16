package me.kalmanolah.forumlistener;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.kalmanolah.extras.Tools;
import me.kalmanolah.okb3.OKBSync;
import me.kalmanolah.okb3.OKConfig;
import me.kalmanolah.okb3.OKDatabase;

public class XenForo implements OKBSync
{

    @Override
    public boolean accountExist(String username, String password)
    {
        // TODO Auto-generated method stub
        String encpass = "nope";
        boolean exist = false;
        try
        {
            ResultSet rs = OKDatabase.dbm.query("SELECT data FROM " + (String) OKConfig.config.get("db.prefix") + "xf_user_authenticate," + (String) OKConfig.config.get("db.prefix")
                    + "xf_user WHERE " + OKConfig.config.get("db.prefix") + "xf_user.username = '" + username + "' AND " + OKConfig.config.get("db.prefix") + "xf_user.user_id = " + OKConfig.config.get("db.prefix") + "xf_user_authenticate.user_id");
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
        OKDatabase.dbm.query("UPDATE " + OKConfig.config.get("db.prefix") + "xf_user," + OKConfig.config.get("db.prefix")+ "xf_user_authenticate SET user_group_id='" + forumGroupId + "' WHERE " + OKConfig.config.get("db.prefix") + "xf_user.username='" + username + "' AND " + OKConfig.config.get("db.prefix") + "xf_user.user_id=xf_user_authenticate.user_id");
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
    public int getGroup(String username)
    {
        int group = -1;
        String query1 = "SELECT user_group_id,data FROM " + OKConfig.config.get("db.prefix") + "xf_user," + OKConfig.config.get("db.prefix") + "xf_user_authenticate WHERE " + OKConfig.config.get("db.prefix") + "xf_user.username = '" + username + "'  AND " + OKConfig.config.get("db.prefix") + "xf_user.user_id = " + OKConfig.config.get("db.prefix") + "xf_user_authenticate.user_id";
        ResultSet rs = OKDatabase.dbm.query(query1);
        try
        {
            if (rs.next())
            {
                group = rs.getInt("user_group_id");
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return group;
    }

}
