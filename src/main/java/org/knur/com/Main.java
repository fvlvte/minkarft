package org.knur.com;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("POKNURSKU.PL onEnable ok ok");

        if(Bukkit.getWorld("knurrena") == null)
        {
            WorldCreator wc = new WorldCreator("knurrena");

            wc.environment(World.Environment.THE_END);
            wc.generator("VoidGen");
            Bukkit.createWorld(wc);
        }

        if(Bukkit.getWorld("knurori69") == null)
        {
            WorldCreator wc = new WorldCreator("knurori69");

            wc.environment(World.Environment.NORMAL);
            Bukkit.createWorld(wc);
        }

        if(Bukkit.getWorld("login") == null)
        {
            WorldCreator wc = new WorldCreator("login");

            wc.environment(World.Environment.NORMAL);
            wc.generator("VoidGen");
            Bukkit.createWorld(wc);
        }

        PVPEvent.getInstance().bindServer(this);

        Bukkit.getScheduler().runTask(this, () -> {
            try {
                getServer().getPlayer("fvlvte").setGameMode(GameMode.CREATIVE);
            }
            catch(Exception e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        });

        Bukkit.getScheduler().runTask(this, () -> {
            try {
                getServer().getPlayer("FritteX").setGameMode(GameMode.CREATIVE);
            }
            catch(Exception e) {
                Bukkit.getLogger().info(e.getMessage());
            }
        });

        getServer().getPluginManager().registerEvents(new KnurSluchacz(this), this);
        getServer().getPluginManager().registerEvents(BosniakDrabinka.getInstance(), this);
        broadCoUsT("KNUR " + ChatColor.RED + ChatColor.BOLD + "WSTAŁ");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("POKNURSKU.PL onDisable ok ok");
        broadCoUsT("KNUR " + ChatColor.RED + ChatColor.BOLD + "ŚPI");

        Bukkit.getWorld("knurrena").save();
        Bukkit.getWorld("knurori69").save();
        Bukkit.getWorld("login").save();
    }

    public void doReloadSelf() {
        try {
            getServer().getPluginManager().disablePlugin(this);
            File f = new File("./plugins/knurplugin-release.jar");
            Plugin p = getServer().getPluginManager().loadPlugin(f);
            getServer().getPluginManager().enablePlugin(p);
        }
        catch(Exception e) {
            Bukkit.getLogger().info(e.getMessage());
        }
    }
    public void broadCoUsT(String message)
    {
        getServer().broadcastMessage("[" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "POKNURSKU.PL" + ChatColor.RESET + "]: " + message);
    }
}
