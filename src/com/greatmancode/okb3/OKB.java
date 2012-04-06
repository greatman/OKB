package com.greatmancode.okb3;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import com.greatmancode.extras.Metrics;

public class OKB extends JavaPlugin
{
    public static String name;
    public static String version;
    public static List<String> authors;
    public static OKBSync sync;
    public static OKBInternalDB OKBDb;
    public void onEnable()
    {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        authors = this.getDescription().getAuthors();
        PluginLoader pm = this.getPluginLoader();
        
        //Load the configuration
        new OKConfig(this);
        
        //Loading databases
        OKBDb = new OKBInternalDB();
        
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
        
    }
    
    public void onDisable()
    {
        
    }
}
