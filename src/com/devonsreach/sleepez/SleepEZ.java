package com.devonsreach.sleepez;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.devonsreach.sleepez.event.player.PlayerEnterBed;
import com.devonsreach.sleepez.event.player.PlayerLeaveBed;


public class SleepEZ extends JavaPlugin {

    public void onEnable() {
        PluginDescriptionFile pdFile = getDescription();
        Logger logger = getLogger();

        registerCommands();
        registerEvents();
        registerConfig();

        logger.info(pdFile.getName() + " has been enabled " + pdFile.getVersion());
        logger.info("[SleepEZ] Wakey, wakey! Eggs and bakey!");
    }

    public void onDisable() {
        PluginDescriptionFile pdFile = getDescription();
        Logger logger = getLogger();
        logger.info(pdFile.getName() + " has been disabled " + pdFile.getVersion());
        logger.info("It's night night time. It's sleepy time. It's time to go to bed.");
    }

    private void registerCommands() {

    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerEnterBed(this), this);
        pm.registerEvents(new PlayerLeaveBed(), this);
    }

    private void registerConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists())
            saveDefaultConfig();
    }
//End of Class
}
