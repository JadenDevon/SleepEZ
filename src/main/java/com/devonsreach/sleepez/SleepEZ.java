package com.devonsreach.sleepez;

import java.io.File;
import java.util.logging.Logger;

import com.devonsreach.sleepez.commands.*;
import com.devonsreach.sleepez.event.player.CancelSleepTimeSkip;
import com.devonsreach.sleepez.misc.SleeperList;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.devonsreach.sleepez.event.player.PlayerEnterBed;
import com.devonsreach.sleepez.event.player.PlayerLeaveBed;


public class SleepEZ extends JavaPlugin {

    Logger logger = getLogger();

    public SleeperList sleeperList = new SleeperList(this);

    @Override
    public void onEnable() {
        PluginDescriptionFile pdFile = getDescription();

        registerCommands();
        registerEvents();
        registerConfig();

        logger.info(pdFile.getName() + " " + pdFile.getVersion() + " has been enabled ");
        logger.info("Wakey, wakey! Eggs and bakey!");
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdFile = getDescription();
        //Logger logger = getLogger();
        logger.info(pdFile.getName() + " has been disabled " + pdFile.getVersion());
        logger.info("It's night night time. It's sleepy time. It's time to go to bed.");
    }

    private void registerCommands() {
        this.getCommand("EZReload").setExecutor(new ReloadConfig(this));
        this.getCommand("EZUsePercent").setExecutor(new SetUsePercentPlayers(this));
        this.getCommand("EZSetPercent").setExecutor(new SetPercentPlayers(this));
        this.getCommand("EZUseNumber").setExecutor(new SetUseNumPlayers(this));
        this.getCommand("EZSetNumber").setExecutor(new SetNumPlayers(this));
        this.getCommand("EZTimelapse").setExecutor(new SetTimeLapse(this));
        this.getCommand("EZTimelapseSpeed").setExecutor(new SetTimeLapseSpeed(this));
        this.getCommand("EZSetEndStorm").setExecutor(new SetEndStorm(this));
        this.getCommand("EZSetUnsafeSleep").setExecutor(new SetAllowUnsafeSleep(this));
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new CancelSleepTimeSkip(this), this);
        pm.registerEvents(new PlayerEnterBed(this), this);
        pm.registerEvents(new PlayerLeaveBed(this), this);
    }

    private void registerConfig() {
        if (!getDataFolder().exists()) {
            logger.info("Config directory not found for SleepEZ. Creating directory.");
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            logger.info("config.yml not found. Creating default config.yml");
            saveDefaultConfig();
            return;
        }
        checkConfig();
    }

    private void checkConfig() {
        logger.info("checking config");
        File file = new File(getDataFolder(), "config.yml");
        Configuration current = YamlConfiguration.loadConfiguration(file);
        Configuration defaults = getConfig().getDefaults();
        boolean update = false;
        if (!getDescription().getVersion().equals(current.getString("Version"))) {
            logger.info("Config and Plugin versions do not match. Updating config.yml.");
            update = true;
        } else {
            for (String defaultKey : defaults.getKeys(true)) {
                if (!current.contains(defaultKey)) {
                    logger.info("Key(s) missing from config.yml. Repopulating missing key(s).");
                    update = true;
                    break;
                }
            }
        }
        if (update) {
            if (file.exists()) {
                file.delete();
            }
            saveDefaultConfig();
            reloadConfig();
            for (String defaultKey : defaults.getKeys(true)) {
                if (current.contains(defaultKey)) {
                    getConfig().set(defaultKey, current.get(defaultKey));
                }
            }
            logger.info("config.yml has been updated successfully.");
            getConfig().set("Version", getDescription().getVersion());
            saveConfig();
        } else {
            logger.info("Valid config.yml found.");
        }
    }
//End of Class
}
