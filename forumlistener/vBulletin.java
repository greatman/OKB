import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.greatmancode.extras.Tools;
import com.greatmancode.okb3.OKBSync;
import com.greatmancode.okb3.OKBWebsiteDB;
import com.greatmancode.okb3.OKConfig;

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
                    OKBWebsiteDB.dbm.prepare(
                            "SELECT password,salt FROM " + OKConfig.tablePrefix
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
            OKBWebsiteDB.dbm.prepare("UPDATE " + OKConfig.tablePrefix + "user SET usergroupid=" + forumGroupId + " WHERE username = '" + username + "'").executeUpdate();
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
            ResultSet rs = OKBWebsiteDB.dbm.prepare("SELECT usergroupid FROM " + OKConfig.tablePrefix + "user WHERE username = '" + username + "'").executeQuery();
            if (rs.next())
            {
                do
                {
                    group.add(rs.getInt("usergroupid"));
                }
                while(rs.next());
            }
            rs.close();
            if (OKConfig.useSecondaryGroups)
            {
            	rs = OKBWebsiteDB.dbm.prepare("SELECT membergroupids FROM " + OKConfig.tablePrefix + "user WHERE username = '" + username + "'").executeQuery();
                if (rs.next())
                {
                    do
                    {
                        group.add(rs.getInt("membergroupids"));
                    }
                    while(rs.next());
                }
                rs.close();
            }
            
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return group;
    }

}
