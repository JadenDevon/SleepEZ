/**
 * Main handler for SleepEZ plugin
 *
 * @version 1.1.0
 * @author JadenDevon
 */
package com.devonsreach.sleepez;

import com.devonsreach.sleepez.commands.*;
import com.devonsreach.sleepez.configs.MainConfig;
import com.devonsreach.sleepez.configs.MessageConfig;
import com.devonsreach.sleepez.event.player.CancelSleepTimeSkip;
import com.devonsreach.sleepez.event.player.PlayerEnterBed;
import com.devonsreach.sleepez.event.player.PlayerLeaveBed;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;



public class SleepEZ extends JavaPlugin {

    final Logger logger = getLogger();

    public MainConfig config;
    public MessageConfig messageConfig;
    public ArrayList<Player> sleeperList;

    public boolean timeLapseTriggered = false;

    @Override
    public void onEnable() {
        config = new MainConfig(this);
        messageConfig = new MessageConfig(this);
        sleeperList = new ArrayList<>();

        registerCommands();
        registerEvents();
        checkLists();

        PluginDescriptionFile pdFile = getDescription();
        logger.info(pdFile.getName() + " " + pdFile.getVersion() + " has been enabled ");
        logger.info("Wakey, wakey! Eggs and bakey!");
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdFile = getDescription();
        logger.info(pdFile.getName() + " has been disabled " + pdFile.getVersion());
        logger.info("It's night night time. It's sleepy time. It's time to go to bed.");
    }

    private void registerCommands() {
        this.getCommand("EZReload").setExecutor(new ReloadConfig(this));
        this.getCommand("EZToggleNumberPercent").setExecutor(new ToggleNumberPercent(this));
        this.getCommand("EZSetPercent").setExecutor(new SetPercentPlayers(this));
        this.getCommand("EZSetNumber").setExecutor(new SetNumPlayers(this));
        this.getCommand("EZToggleTimeLapse").setExecutor(new ToggleTimeLapse(this));
        this.getCommand("EZTimelapseSpeed").setExecutor(new SetTimeLapseSpeed(this));
        this.getCommand("EZToggleEndStorm").setExecutor(new ToggleEndStorm(this));
        this.getCommand("EZToggleUnsafeSleep").setExecutor(new ToggleAllowUnsafeSleep(this));
        this.getCommand("EZToggleEnterBedDuringTimeLapse").setExecutor(new ToggleEnterBedDuringTimeLapse(this));
        this.getCommand("EZToggleExitBedDuringTimeLapse").setExecutor(new ToggleExitBedDuringTimeLapse(this));
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new CancelSleepTimeSkip(this), this);
        pm.registerEvents(new PlayerEnterBed(this), this);
        pm.registerEvents(new PlayerLeaveBed(this), this);
    }

    // Check in case of plugin reload by server while players are trying to sleep
    private void checkLists() {
        if (sleeperList.isEmpty()) {
            for (Player player : getServer().getOnlinePlayers()) {
                if (player.isSleeping()) {
                    if (!sleeperList.contains(player)) {
                        sleeperList.add(player);
                    }
                }
            }
        }
    }
//End of Class
}
