package com.greatmancode.okb3;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.greatmancode.extras.Metrics;
import com.greatmancode.okb3.commands.*;

public class OKB extends JavaPlugin
{
    public static String name;
    public static String version;
    public static List<String> authors;
    public static OKBSync sync;
    public static OKBInternalDB OKBDb;
    public List<BaseCommand> commands = new ArrayList<BaseCommand>();
    public static OKB p;
    public static Permission perms;
    
    public void onEnable()
    {
    	p = this;
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        authors = this.getDescription().getAuthors();
        PluginLoader pm = this.getPluginLoader();
        if (!setupPermissions())
        {
            OKLogger.info("Permissions plugin not found, shutting down...");
            pm.disablePlugin(this);
        }
        else
        {
        	//Load the configuration
            new OKConfig(this);
            
            //Loading databases
            OKBDb = new OKBInternalDB(this);
            
            //MySQL connect
            try
    		{
    			new OKBWebsiteDB(this);
    		}
    		catch (SQLException e2)
    		{
    			OKLogger.error(e2.getMessage());
    			pm.disablePlugin(this);
    		}
            
            //Load the corresponding link file along with metrics
            try
            {
                File dir = new File(this.getDataFolder() + "/links");
                ClassLoader loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, OKBSync.class.getClassLoader());
                for (File file : dir.listFiles()) {
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (name.toLowerCase() == OKConfig.linkName.toLowerCase())
                    {
                        Class<?> clazz = loader.loadClass(name);
                        Object object = clazz.newInstance();
                        if (object instanceof OKBSync) {
                            sync = (OKBSync) object;
                            OKLogger.info("Website link " + name + " loaded!");
                        }
                        else
                        {
                            OKLogger.error("Website link " + name + " not found. Be sure it is located in the plugins/OKB3/links folder!");
                            pm.disablePlugin(this);
                        }
                    }
                }
                
                //Loading metrics
                Metrics metrics = new Metrics(this);
                metrics.start();
            
            }
            catch (MalformedURLException e)
            {
                OKLogger.info("A error occured while loading the forum link class. Error code 1.");
                pm.disablePlugin(this);
            }
            catch (InstantiationException e)
            {
                OKLogger.info("A error occured while loading the forum link class. Error code 2");
                pm.disablePlugin(this);
            }
            catch (IllegalAccessException e)
            {
                OKLogger.info("A error occured while loading the forum link class. Error code 3");
                pm.disablePlugin(this);
            }
            catch (ClassNotFoundException e1)
            {
                OKLogger.info("Forum link class not found, shutting down.... Check if the configuration.forum configuration node is configurated correctly.");
                pm.disablePlugin(this);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            setupCommands();
        }
             
    }
    
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public void onDisable()
    {
        OKLogger.info("Closing remote MySQL connection");
    	OKBWebsiteDB.dbm.close();
    	
    	OKLogger.info("Closing local DB connection");
    	OKBDb.close();
    	
    }
    
    public void setupCommands()
    {
    	commands.add(new BbbCommand());
        commands.add(new BbbVersionCommand());
        commands.add(new SyncCommand());
        commands.add(new ResyncCommand());
        commands.add(new FsyncCommand());
        commands.add(new FsyncAllCommand());
        commands.add(new FBanCommand());
        commands.add(new FUnbanCommand());
        commands.add(new FPromoteCommand());
        commands.add(new FDemoteCommand());
        commands.add(new FRankCommand());
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        List<String> parameters = new ArrayList<String>(Arrays.asList(args));
        String commandName = cmd.getName();
        for (BaseCommand OKBCommand : this.commands)
        {
            if (OKBCommand.getCommands().contains(commandName))
            {
                OKBCommand.execute(sender, parameters);
                return true;
            }
        }
        return false;
    }
    
}
