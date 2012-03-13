package me.kalmanolah.okb3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class OKConfig
{
    static HashMap<String, Object> config = new HashMap<String, Object>();
    static String directory = "plugins" + File.separator + OKmain.name;
    static File file = new File(directory + File.separator + "config.yml");

    private static OKmain plugin;

    public OKConfig(OKmain thePlugin)
    {
        plugin = thePlugin;
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        if (plugin.getConfig().getString("general.mode").equalsIgnoreCase("secure"))
        {
            config.put("mode", 1);
        }
        else
        {
            config.put("mode", 0);
        }
        config.put("gen.whitelist", plugin.getConfig().getBoolean("general.enable-whitelist"));
        config.put("gen.nicks", plugin.getConfig().getBoolean("general.enable-nickname-syncing"));
        config.put("gen.bans", plugin.getConfig().getBoolean("general.enable-synced-banning"));
        config.put("gen.track", plugin.getConfig().getBoolean("general.enable-promotion-track"));
        config.put("gen.ranks", plugin.getConfig().getBoolean("general.enable-rank-changing"));
        config.put("gen.posts", plugin.getConfig().getBoolean("general.enable-post-count-rewarding"));
        config.put("mysql.host", plugin.getConfig().getString("mysql-connection.mysql-host"));
        config.put("mysql.user", plugin.getConfig().getString("mysql-connection.mysql-user"));
        config.put("mysql.pass", plugin.getConfig().getString("mysql-connection.mysql-password"));
        config.put("mysql.db", plugin.getConfig().getString("mysql-connection.mysql-database-name"));
        config.put("mysql.port", plugin.getConfig().getInt("mysql-connection.mysql-port"));
        if ((Integer) config.get("mode") == 1)
        {
            config.put("secure.forum", plugin.getConfig().getString("modes.secure.forum"));
            if (((String) config.get("secure.forum")).equalsIgnoreCase("smf") || ((String) config.get("secure.forum")).equalsIgnoreCase("vbulletin")
                    || ((String) config.get("secure.forum")).equalsIgnoreCase("mybb") || ((String) config.get("secure.forum")).equalsIgnoreCase("ipb")
                    || ((String) config.get("secure.forum")).equalsIgnoreCase("wbb"))
            {
                config.put("enctype", 1);
            }
            else if (((String) config.get("secure.forum")).equalsIgnoreCase("bbpress") || ((String) config.get("secure.forum")).equalsIgnoreCase("phpbb")
                    || ((String) config.get("secure.forum")).equalsIgnoreCase("vanilla"))
            {
                config.put("enctype", 2);
            }
            else if (((String) config.get("secure.forum")).equalsIgnoreCase("xenforo") || ((String) config.get("secure.forum")).equalsIgnoreCase("kunena"))
            {
                config.put("enctype", 3);
            }
            else if (((String) config.get("secure.forum")).equalsIgnoreCase("custom"))
            {
                config.put("enctype", 4);
            }
            if ((Integer) config.get("enctype") == 1)
            {
                config.put("modes.table1", plugin.getConfig().getString("modes.secure.type1.user-table"));
                config.put("modes.field1", plugin.getConfig().getString("modes.secure.type1.username-field"));
                config.put("modes.field2", plugin.getConfig().getString("modes.secure.type1.password-field"));
                config.put("modes.field3", plugin.getConfig().getString("modes.secure.type1.rank-id-field"));
            }
            else if ((Integer) config.get("enctype") == 2)
            {
                config.put("modes.table1", plugin.getConfig().getString("modes.secure.type2.user-table"));
                config.put("modes.field1", plugin.getConfig().getString("modes.secure.type2.username-field"));
                config.put("modes.field2", plugin.getConfig().getString("modes.secure.type2.password-field"));
                config.put("modes.field3", plugin.getConfig().getString("modes.secure.type2.rank-id-field"));
                config.put("modes.phploc", plugin.getConfig().getString("modes.secure.type2.location-of-passgen-php"));
                config.put("modes.phppass", plugin.getConfig().getString("modes.secure.type2.password-set-in-config-inc-php"));
            }
            else if ((Integer) config.get("enctype") == 3)
            {
                config.put("modes.table1", plugin.getConfig().getString("modes.secure.type3.user-table"));
                config.put("modes.table2", plugin.getConfig().getString("modes.secure.type3.second-table"));
                config.put("modes.field1", plugin.getConfig().getString("modes.secure.type3.username-field"));
                config.put("modes.field2", plugin.getConfig().getString("modes.secure.type3.password-field"));
                config.put("modes.field3", plugin.getConfig().getString("modes.secure.type3.rank-id-field"));
                config.put("modes.field4", plugin.getConfig().getString("modes.secure.type3.user-id-field-in-user-table"));
                config.put("modes.field5", plugin.getConfig().getString("modes.secure.type3.user-id-field-in-second-table"));
            }
            else if ((Integer) config.get("enctype") == 4)
            {
                config.put("modes.table1", plugin.getConfig().getString("modes.secure.type4.user-table"));
                config.put("modes.field1", plugin.getConfig().getString("modes.secure.type4.username-field"));
                config.put("modes.field2", plugin.getConfig().getString("modes.secure.type4.password-field"));
                config.put("modes.field3", plugin.getConfig().getString("modes.secure.type4.rank-id-field"));
                config.put("modes.phploc", plugin.getConfig().getString("modes.secure.type4.location-of-passgen-php"));
                config.put("modes.phppass", plugin.getConfig().getString("modes.secure.type4.password-set-in-config-inc-php"));
            }
            config.put("modes.multitable", false);
        }
        else
        {
            config.put("modes.table1", plugin.getConfig().getString("modes.normal.user-table"));
            config.put("modes.field1", plugin.getConfig().getString("modes.normal.minecraft-login-name-field"));
            config.put("modes.field2", plugin.getConfig().getString("modes.normal.rank-id-field"));
            config.put("modes.multitable", plugin.getConfig().getBoolean("modes.normal.multitable.enable-multiple-tables"));
            if ((Boolean) config.get("modes.multitable"))
            {
                config.put("modes.table2", plugin.getConfig().getString("modes.normal.multitable.second-table"));
                config.put("modes.field3", plugin.getConfig().getString("modes.normal.multitable.user-id-field-in-user-table"));
                config.put("modes.field4", plugin.getConfig().getString("modes.normal.multitable.user-id-field-in-second-table"));
            }
        }
        HashMap<String, String> groupmap = new HashMap<String, String>();
        List<String> groups = plugin.getConfig().getStringList("group-mapping.default");
        Iterator<String> group = groups.iterator();
        while (group.hasNext())
        {
            String nextgroup = group.next();
            groupmap.put(nextgroup, plugin.getConfig().getString("group-mapping.default." + nextgroup));
        }
        config.put("groups", groupmap);
        List<String> worlds = plugin.getConfig().getStringList("group-mapping.worlds");
        if (worlds != null && !worlds.isEmpty())
        {
            Iterator<String> world = worlds.iterator();
            while (world.hasNext())
            {
                String nextworld = world.next();
                HashMap<String, String> worldmap = new HashMap<String, String>();
                List<String> worldgroups = plugin.getConfig().getStringList("group-mapping.worlds." + nextworld);
                Iterator<String> worldgroup = worldgroups.iterator();
                while (worldgroup.hasNext())
                {
                    String nextworldgroup = worldgroup.next();
                    worldmap.put(nextworldgroup, plugin.getConfig().getString("group-mapping.worlds." + nextworld + "." + nextworldgroup));
                }
                config.put("groups." + nextworld, worldmap);
            }
        }
        if ((Boolean) config.get("gen.whitelist"))
        {
            config.put("whitelist.message", plugin.getConfig().getString("extras.whitelist.kick-message"));
            config.put("whitelist.blacklist", Boolean.parseBoolean(plugin.getConfig().getString("extras.whitelist.use-as-blacklist")));
            HashMap<String, Boolean> whitelistmap = new HashMap<String, Boolean>();
            List<String> whitelistgroups = plugin.getConfig().getStringList("extras.whitelist.groups");
            Iterator<String> whitelistgroup = whitelistgroups.iterator();
            while (whitelistgroup.hasNext())
            {
                String nextgroup = whitelistgroup.next();
                whitelistmap.put(nextgroup, Boolean.parseBoolean(plugin.getConfig().getString("extras.whitelist.groups." + nextgroup)));
            }
            config.put("whitelist.groups", whitelistmap);
        }
        if ((Boolean) config.get("gen.nicks"))
        {
            config.put("nicks.field", plugin.getConfig().getString("extras.nickname-syncing.nickname-field-in-usertable"));
        }
        if ((Boolean) config.get("gen.bans"))
        {
            config.put("bans.banrank", plugin.getConfig().getString("extras.synced-banning.banned-user-forum-rank-id"));
            config.put("bans.unbanrank", plugin.getConfig().getString("extras.synced-banning.unbanned-user-forum-rank-id"));
            config.put("bans.message", plugin.getConfig().getString("extras.synced-banning.ban-message"));
        }
        if ((Boolean) config.get("gen.track"))
        {
            String[] track = plugin.getConfig().getString("extras.promotion-track.track").split("\\|");
            List<String> promotiontrack = new ArrayList<String>();
            for (String t : track)
            {
                promotiontrack.add(t);
            }
            config.put("track.track", promotiontrack);
            HashMap<String, String> identifiers = new HashMap<String, String>();
            HashMap<String, String> identifierstwo = new HashMap<String, String>();
            config.put("ranks.identifiers", identifiers);
            config.put("ranks.identifierstwo", identifierstwo);
        }
        if ((Boolean) config.get("gen.ranks"))
        {
            List<String> rankidentifiers = plugin.getConfig().getStringList("extras.rank-changing.identifiers");
            Iterator<String> rankidentifier = rankidentifiers.iterator();
            HashMap<String, String> identifiers = new HashMap<String, String>();
            HashMap<String, String> identifierstwo = new HashMap<String, String>();
            while (rankidentifier.hasNext())
            {
                String nextidentifier = rankidentifier.next();
                identifiers.put(plugin.getConfig().getString("extras.rank-changing.identifiers." + nextidentifier).toLowerCase(), nextidentifier);
                identifierstwo.put(nextidentifier, plugin.getConfig().getString("extras.rank-changing.identifiers." + nextidentifier).toLowerCase());
            }
            config.put("ranks.identifiers", identifiers);
            config.put("ranks.identifierstwo", identifierstwo);
        }
        if ((Boolean) config.get("gen.posts"))
        {
            config.put("posts.field", plugin.getConfig().getString("extras.post-count-rewarding.post-count-field-in-user-table"));
            config.put("posts.reward", Double.parseDouble(plugin.getConfig().getString("extras.post-count-rewarding.currency-reward-per-post")));
        }
    }
}